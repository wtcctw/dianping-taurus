package com.dp.bigdata.taurus.springmvc.controller.api;

import com.sankuai.meituan.config.util.AuthUtil;
import com.sankuai.meituan.config.util.TimeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * Author   mingdongli
 * 16/5/4  下午10:58.
 */
public class APIControllerTest {

    private static DefaultHttpClient httpclient;

    @Before
    public void setUp() throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, 5000);
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParams, registry);
        httpclient = new DefaultHttpClient(cm, httpParams);
    }

    @Test
    public void testAddJob() throws Exception {
        httpAddJobPost("http://localhost:8080/api/job/addJob", "/api/job/addJob");

    }

    public void httpStopJobPost(String url) throws JSONException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        HttpPost method = new HttpPost(url);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization("/api/job/stopJob", "POST", date, "hotel_mtazhilian", "b4db8cd497ff274c0e60af06ba6f2da3");
        method.setHeader("Date", date);
        method.setHeader("Authorization", authorization);


        // 接收参数json列表
        JSONObject jsonParam = new JSONObject();

        jsonParam.put("jobId", "task_201605041836_0003");

        StringEntity entity = new StringEntity(jsonParam.toString(),"UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);

        System.out.println(jsonParam);
        HttpResponse result = httpclient.execute(method);

        String resData = EntityUtils.toString(result.getEntity());
        System.out.println(resData);
    }


    public static void httpAddJobPost(String url, String uri) throws JSONException, IOException, InvalidKeyException, NoSuchAlgorithmException {

        HttpPost method = new HttpPost(url);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization(uri, "POST", date, "hotel_mtazhilian", "b4db8cd497ff274c0e60af06ba6f2da3");
        method.setHeader("Date", date);
        method.setHeader("Authorization", authorization);


        // 接收参数json列表
        JSONObject jsonParam = new JSONObject();

        jsonParam.put("taskName", "alpha-test-api");
        jsonParam.put("taskType", "default");
        jsonParam.put("taskCommand", "date");
        jsonParam.put("crontab", "0/13 * * * * ?");
        jsonParam.put("proxyUser", "nobady");
        jsonParam.put("maxExecutionTime", 60);
        jsonParam.put("maxWaitTime", 60);
        jsonParam.put("retryTimes", 0);
        jsonParam.put("creator", "mingdong.li");
        jsonParam.put("description", "");
        jsonParam.put("alertCondition", "");
        jsonParam.put("alertType", "");
        jsonParam.put("alertGroup", "");
        jsonParam.put("alertUser", "");
        jsonParam.put("taskUrl", "");
        jsonParam.put("mainClass", "");
        jsonParam.put("appName", "");
        jsonParam.put("iskillcongexp", false);
        jsonParam.put("hostName", "192.168.78.42");

        StringEntity entity = new StringEntity(jsonParam.toString(),"UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);

        System.out.println(jsonParam);
        HttpResponse result = httpclient.execute(method);

        String resData = EntityUtils.toString(result.getEntity());
        System.out.println(resData);
    }

    public String get(String url, List<? extends NameValuePair> nvps) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        //构造nvps为queryString
        if (nvps != null && nvps.size() > 0) {
            String query = URLEncodedUtils.format(nvps, "UTF-8");
            url += "?" + query;
        }
        HttpGet httpGet = new HttpGet(url);

        HttpEntity entity;
        String result;
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization("/api/job/getJobTrace", "GET", date, "hotel_mtazhilian", "b4db8cd497ff274c0e60af06ba6f2da3");
        httpGet.setHeader("Date", date);
        httpGet.setHeader("Authorization", authorization);
        HttpResponse response = httpclient.execute(httpGet);
        entity = response.getEntity();
        InputStream ins = entity.getContent();
        result = IOUtils.toString(ins, "UTF-8");
        System.out.println(result);
        return result;
    }
}