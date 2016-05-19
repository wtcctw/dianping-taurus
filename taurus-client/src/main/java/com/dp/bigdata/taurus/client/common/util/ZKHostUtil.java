package com.dp.bigdata.taurus.client.common.util;

import com.dp.bigdata.taurus.client.common.constants.EnvConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chenzhencheng on 15/12/12.
 */
public class ZKHostUtil {
    private final static Logger log = LoggerFactory.getLogger(ZKHostUtil.class);
    public static String getZookeeperHost(String environment) throws Exception{
        log.info("获取environment:"+environment);
        String cfgPath ="";
        cfgPath= EnvConstants.ENVCONSTANTMAP.get(environment);
        //如果传入参数取不到路径，则用开发环境地址
        if (null==cfgPath||cfgPath.equals("")){
            throw new IllegalArgumentException("environment 不正确,beta 或 online为可选值");
        }
        // 读取jar包中的文件流
        Properties conf = new Properties();
        InputStream is = null;
        String address="";
        try {
            is = ZKHostUtil.class.getResourceAsStream(cfgPath);
            conf.load(is);
            address=conf.getProperty("zookeeperAddress");
            log.info("从配置文件中获取到的 zookeeperAddress:"+address);
        } catch (IOException e) {
            throw new IOException("读取配置文件异常",e);
        }catch (Exception e){
            throw new Exception("加载属性异常",e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                }catch (Exception e){}
            }
        }
        return address;
    }
}
