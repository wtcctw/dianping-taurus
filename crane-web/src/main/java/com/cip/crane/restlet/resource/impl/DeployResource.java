package com.cip.crane.restlet.resource.impl;

import com.cip.crane.generated.mapper.HostMapper;
import com.cip.crane.generated.mapper.TaskMapper;
import com.cip.crane.generated.module.Host;
import com.cip.crane.generated.module.HostExample;
import com.cip.crane.generated.module.Task;
import com.cip.crane.generated.module.TaskExample;
import com.cip.crane.restlet.resource.IDeployResource;
import com.cip.crane.zookeeper.common.deploy.DeployStatus;
import com.cip.crane.zookeeper.common.deploy.Deployer;
import com.cip.crane.zookeeper.common.deploy.DeploymentContext;
import com.cip.crane.zookeeper.common.deploy.DeploymentException;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import jodd.util.StringUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeployResource extends ServerResource implements IDeployResource {

	@Autowired
	private Deployer deployer;

	@Autowired
	private TaskMapper taskMapper;

	@Autowired
	private HostMapper hostMapper;

	private String webUrl = "";

	private static final Log LOG = LogFactory.getLog(DeployResource.class);

	private static final String createUrlPattern = "http://%s/task?appname=%s&path=%s&ip=%s";

	private static final String updateUrlPattern = "http://%s/schedule?appname=%s&path=%s";

	private static Map<String, DeployResult> deployResults = new LinkedHashMap<String, DeployResult>(1000, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<String, DeployResult> arg0) {
			return size() >= 1000;
		}
	};

	private ExecutorService deployThreadPool = new ThreadPoolExecutor(5, 10, 1L, TimeUnit.SECONDS,
	      new LinkedBlockingQueue<Runnable>());

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

    public String status(String deployId, String name){
        Map<String, Object> result = new HashMap<String, Object>();
        if (name != null) {
            TaskExample example = new TaskExample();
            example.createCriteria().andAppnameEqualTo(name).andStatusNotEqualTo(3);
            List<Task> tasks = taskMapper.selectByExample(example);
            if (tasks == null || tasks.size() == 0) {
                HostExample he = new HostExample();
                he.createCriteria().andIsonlineEqualTo(true);
                List<Host> hosts = hostMapper.selectByExample(he);
                List<String> ips = new ArrayList<String>();
                for (Host host : hosts) {
                    ips.add(host.getIp());
                }
                result.put("hosts", ips);
            } else {
                List<String> ips = new ArrayList<String>();
                String hostIp = tasks.get(0).getHostname();
                ips.add(hostIp);
                result.put("hosts", ips);
            }
        } else {
            DeployResult deployResult = deployResults.get(deployId);
            if (deployResult == null) {
                result.put("status", DeployStatus.UNKNOWN);
            } else {
                result.put("status", deployResult.status);
                result.put("createurl", deployResult.createUrl);
                result.put("updateurl", deployResult.updateUrl);
            }
        }
        JSONObject jsonObject =  new JSONObject(result);
        return jsonObject.toString();
    }

	@Override
	@Get
	public Representation status() {
		String deployId = getQueryValue("deployId");
		String name = getQueryValue("appName");
		Map<String, Object> result = new HashMap<String, Object>();
		if (name != null) {
			TaskExample example = new TaskExample();
			example.createCriteria().andAppnameEqualTo(name).andStatusNotEqualTo(3);
			List<Task> tasks = taskMapper.selectByExample(example);
			if (tasks == null || tasks.size() == 0) {
				HostExample he = new HostExample();
				he.createCriteria().andIsonlineEqualTo(true);
				List<Host> hosts = hostMapper.selectByExample(he);
				List<String> ips = new ArrayList<String>();
				for (Host host : hosts) {
					ips.add(host.getIp());
				}
				result.put("hosts", ips);
			} else {
				List<String> ips = new ArrayList<String>();
				String hostIp = tasks.get(0).getHostname();
				ips.add(hostIp);
				result.put("hosts", ips);
			}
		} else {
			DeployResult deployResult = deployResults.get(deployId);
			if (deployResult == null) {
				result.put("status", DeployStatus.UNKNOWN);
			} else {
				result.put("status", deployResult.status);
				result.put("createurl", deployResult.createUrl);
				result.put("updateurl", deployResult.updateUrl);
			}
		}
        return new JsonRepresentation(result);
	}
public void deployer(String deployId, String deployIp, String deployFile, String url, String appName){

    final String id = deployId;
    final String ip = deployIp;
    final String file = deployFile;
    final String callback = url;
    final String name = appName;
    setStatus(Status.SUCCESS_OK);
    deployThreadPool.execute(new Runnable() {
        @Override
        public void run() {
            deployInternal(ip, file, id, callback, name);
        }
    });
}
	@Override
	@Post
	public void deploy(final Representation re) {
		Form form = new Form(re);
		Map<String, String> valueMap = form.getValuesMap();
		final String id = valueMap.get("deployId");
		final String ip = valueMap.get("ip");
		final String file = valueMap.get("file");
		final String callback = valueMap.get("url");
		final String name = valueMap.get("name");

		setStatus(Status.SUCCESS_OK);
		deployThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				deployInternal(ip, file, id, callback, name);
			}
		});

	}

    private static String splitCMD(String cmd, String job_name){
        int lastPost =  job_name.lastIndexOf('-');
        if(cmd.contains("SNAPSHOT")){
            lastPost = job_name.substring(0,lastPost).lastIndexOf('-');
        }

        String realJobName = job_name.substring(0,lastPost);
        System.out.println("CMD:"+cmd+"  # jobName:"+job_name);
        String[] cmdTmpLists = cmd.split(" ");
        StringBuffer newCMD = new StringBuffer();
        for (String tmpCmd : cmdTmpLists){
            if (tmpCmd.contains(realJobName)){
                int first = tmpCmd.lastIndexOf('/');
                String realPath = tmpCmd.substring(0,first+1) + job_name;
                newCMD.append(realPath);
                newCMD.append(" ");
            }else {
                newCMD.append(tmpCmd);
                newCMD.append(" ");
            }
        }

        return newCMD.toString().trim();
    }

    public static void main(String[] args) {
        System.out.println(splitCMD("java -jar /data/app/taurus-agent/jobs/group-recommendnote-job/group-recommendnote-job-product-2.0.0-SNAPSHOT.jar","group-recommendnote-job-product-1.0.0-SNAPSHOT.jar"));
    }

	private void deployInternal(String ip, String file, String id, String callback, String name) {
		String path = null;
		DeployResult dr = new DeployResult();
        boolean needReplace = false;

        ArrayList<Task>  tasks = null;

        ArrayList<String> oldCMDs = new ArrayList<String>();
        try {
			DeploymentContext context = new DeploymentContext();
			deployResults.put(id, dr);
			context.setDepolyId(id);
			context.setName(name);
			context.setUrl(file);
			testFileUrl(file);
			LOG.info(String.format("Start to depoly %s to %s", file, ip));
            System.out.println(String.format("Start to depoly %s to %s", file, ip));
            dr.status = DeployStatus.DEPLOYING;
			path = deployer.deploy(ip, context);

            String [] tmpStrArray = file.split("/");
            String jarName = tmpStrArray[tmpStrArray.length - 1];

            String tmpPath = "";
            if (StringUtils.isNotBlank(jarName)&& StringUtils.isNotBlank(path)){
                if(path.contains(jarName)){
                    tmpPath = path;
                }else{
                    tmpPath = path + "/" + jarName;
                }
                try {
                    System.out.println("===getTaskByAppNameIP===");
                    tasks  = taskMapper.getTaskByAppNameIP(name, ip);

                    if (tasks != null){
                        for (Task task : tasks){

                            if (task != null && StringUtil.isNotBlank(task.getCommand())){
                                needReplace = true;
                                oldCMDs.add(task.getCommand());
                                String realCMD = splitCMD(task.getCommand(), jarName);

                                if(StringUtils.isNotBlank(realCMD)){
                                    System.out.println("Task CMD update:"+ realCMD);
                                    LOG.error("Task CMD update:"+ realCMD);
                                    task.setCommand(realCMD);
                                    taskMapper.updateByPrimaryKey(task);
                                }else {
                                    LOG.error("Update Task["+task.getName()+"] exception");
                                    System.out.println("Update Task["+task.getName()+"] exception");
                                }
                                System.out.println("=====Task CMD:"+ task.getCommand());
                            }
                        }

                    }


                }catch (Exception e){
                    LOG.error("update Task cmd Error");
                    System.out.println("update Task cmd Error"+e);
                }
            }



            webUrl = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.deploy.weburl");
            String taurusUrl = String.format(createUrlPattern, webUrl, name, tmpPath , ip);
			String updateUrl = String.format(updateUrlPattern, webUrl, name, tmpPath);
			callback(dr, callback, DeployStatus.SUCCESS, taurusUrl, updateUrl);
			LOG.debug("deploy success");
		} catch (DeploymentException e) {
			LOG.error(String.format("Fail to depoly %s to %s", file, ip), e);
            if (needReplace){
                if (tasks != null){
                    int i = 0;
                    for (Task task : tasks){

                        if (task != null && StringUtil.isNotBlank(task.getCommand())){
                            task.setCommand(oldCMDs.get(i));

                            taskMapper.updateByPrimaryKey(task);
                        }
                        i++;
                    }

                }

            }
			callback(dr, callback, e.getStatus(), null, null);
		} catch (Exception e) {
			callback(dr, callback, DeployStatus.FAIL, null, null);
            if (needReplace){
                if (tasks != null){
                    int i = 0;
                    for (Task task : tasks){

                        if (task != null && StringUtil.isNotBlank(task.getCommand())){
                            task.setCommand(oldCMDs.get(i));

                            taskMapper.updateByPrimaryKey(task);
                        }
                        i++;
                    }

                }

            }
			LOG.error(String.format("Fail to depoly %s to %s", file, ip), e);
		}
	}

	private void testFileUrl(String file) throws DeploymentException {
		try {
			URL url = new URL(file);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			byte[] buffer = new byte[1204];
			if (inStream.read(buffer) == 0) {
				throw new FileNotFoundException();
			}
		} catch (Exception e) {
			DeploymentException de = new DeploymentException("File source not found", e);
			de.setStatus(DeployStatus.NO_SOURCE);
			throw de;
		}
	}

	private void callback(DeployResult dr, String callback, int statusCode, String createUrl, String updateUrl) {
		dr.status = statusCode;
		dr.createUrl = createUrl;
		dr.updateUrl = updateUrl;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(callback);
		LOG.info("callback:" + callback +" params: {status: "+String.valueOf(statusCode)+ ", createurl:"+createUrl + ", updateurl:"+ updateUrl);
		method.addParameter("status", String.valueOf(statusCode));
		if (createUrl != null) {
			method.addParameter("createurl", createUrl);
		}
		if (updateUrl != null) {
			method.addParameter("updateurl", updateUrl);
		}
		try {
			client.executeMethod(method);
		} catch (Exception e) {
			LOG.error(e, e);
		}
		method.releaseConnection();
	}

	static class DeployResult {
		int status;

		String createUrl;

		String updateUrl;
	}
}
