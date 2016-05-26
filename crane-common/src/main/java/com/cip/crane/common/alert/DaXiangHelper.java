package com.cip.crane.common.alert;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author   mingdongli
 * 16/5/12  上午11:33.
 */
public class DaXiangHelper {

    private static Logger logger = LoggerFactory.getLogger(DaXiangHelper.class);

    private final static String DAXIANG_URL = "http://opdx.dper.com/alarm/send/text";

    private static HttpClient httpClient;

    static {
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static String sendDaXiang(String user, String content) {

        HttpPost method = new HttpPost(DAXIANG_URL);

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("user", user);
            jsonParam.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);

        HttpResponse result;
        String resData = null;
        try {
            result = httpClient.execute(method);
            resData = EntityUtils.toString(result.getEntity());
        } catch (IOException e) {
            logger.error(String.format("Send %s to %s error", content, DAXIANG_URL));
        } finally {
            method.releaseConnection();
        }
        return resData;
    }

}
