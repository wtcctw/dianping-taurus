package com.dp.bigdata.taurus.web.utils;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by kirinli on 14-9-30.
 */
public class ZabbixUtil {
    static final String CPU_KEY = "system.cpu.load[,avg1";
    static final String MEMERY_KEY = "vm.memory.size[free";
    static String authID = null;
    //用户名
    static String user;
    //密码
    static String password;
    //请求URL
    static String url;
    //zabbix API 版本
    static String version;
    static Integer id;

    static {
        try {

            id = 0;
            user = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.user");
            //密码
            password = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.password");
            //请求URL
            url = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.url");
            //zabbix API 版本
            version = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.version");
            authID = user_login();

        } catch (LionException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static JsonObject paramJson(String method, JsonObject paramsJSON) {
        JsonObject authRequestJSON = new JsonObject();
        authRequestJSON.addProperty("jsonrpc", "2.0");
        authRequestJSON.addProperty("method", method);
        authRequestJSON.add("params", paramsJSON);
        authRequestJSON.addProperty("id", id);
        authRequestJSON.addProperty("auth", authID);


        id++;
        return authRequestJSON;
    }

    /**
     * 登录验证获取auth
     *
     * @return
     */
    public static String user_login() throws IOException {
        JsonObject paramsJSON = new JsonObject();
        paramsJSON.addProperty("user", user);
        paramsJSON.addProperty("password", password);


        String result = execute("user.login", paramsJSON);
        try {
            @SuppressWarnings("unchecked")
            // Map<String, Object> jsonMap = (Map<String, Object>) JSONObject.parse(result);
                    //Map<String, Object> jsonMap = (Map<String, Object>) new ().readValue(result, Map.class);
                    JSONObject jsonMap = new JSONObject(result);
            String authID = jsonMap.getString("result");


            System.out.println(authID);

            return authID;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP 请求
     *
     * @param jsonArgument
     * @return
     */
    public static String get_data(JsonObject jsonArgument) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json-rpc");
            // 表单参数
            String param = jsonArgument.toString().replace("\\", "").replace("\"[", "[").replace("]\"", "]");

            byte[] bypes = param.getBytes();
            conn.getOutputStream().write(bypes);// 输入参数
            //返回
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = reader.readLine();
            return result.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 执行
     *
     * @param method     方法名称
     * @param paramsJSON 模板参数集合
     * @return
     */
    public static String execute(String method, JsonObject paramsJSON) {
        try {
            JsonObject tempJson = paramJson(method, paramsJSON);
            return get_data(tempJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHosts() {
        try {
            JsonObject paramsJSON = new JsonObject();
            String output = "[" + "\"hostid\",\"name\"" + "]";
            paramsJSON.addProperty("output", output);
            paramsJSON.addProperty("groupids", "372");

            String result = execute("host.get", paramsJSON);

            //Map<String, Object> jsonMap = (Map<String, Object>) new ObjectMapper().readValue(result, Map.class);
            JSONObject jsonMap = new JSONObject(result);
            String jsonData = jsonMap.getString("result").replace("=", ":");
            return jsonData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getItemId(String hostId, String key) {

        try {
            JsonObject paramsJSON = new JsonObject();
            paramsJSON.addProperty("output", "itemids");
            paramsJSON.addProperty("hostids", hostId);
            JsonObject searchParams = new JsonObject();
            searchParams.addProperty("key_", key);

            paramsJSON.add("search", searchParams);
            String result = execute("item.get", paramsJSON);

            //  Map<String, Object> jsonMap = (Map<String, Object>) new ObjectMapper().readValue(result, Map.class);

            JSONObject jsonMap = new JSONObject(result);
            String itemIdTemp = jsonMap.getString("result");
            JSONObject itemJson = new JSONObject(itemIdTemp.replace("[","").replace("]",""));
            String itemId = itemJson.getString("itemid");

            return itemId;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getItemValue(String itemId, String history) {

        try {
            long end_time = System.currentTimeMillis() / 1000;
            long start_time = end_time - 900;
            JsonObject paramsJSON = new JsonObject();
            paramsJSON.addProperty("history", history);
            paramsJSON.addProperty("itemids", "[" + itemId + "]");
            paramsJSON.addProperty("time_from", start_time);
            paramsJSON.addProperty("time_till", end_time);
            paramsJSON.addProperty("output", "extend");

            String result = execute("history.get", paramsJSON);

          //  Map<String, Object> jsonMap = (Map<String, Object>) new ObjectMapper().readValue(result, Map.class);
            JSONObject jsonMap = new JSONObject(result);

            String returnValue = jsonMap.getString("result").replace("[", "").replace("]", "");

            if (returnValue.isEmpty()) {
                return null;
            }

            String[] itemArray = returnValue.split("},");

            if (itemArray == null || itemArray[0].isEmpty()) {
                return null;
            }

            String itemValue = itemArray[0] +"}";

            if (itemValue.isEmpty() || itemValue.split(",")[2].isEmpty()) {
                return null;
            }
            JSONObject valueJson = new JSONObject(itemValue);
            String value = valueJson.getString("value");

            return value;
        }  catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCpuLoadInfo(String hostId) {
        String itemId = getItemId(hostId, CPU_KEY);

        return getItemValue(itemId, "0");
    }

    public static String getMemeryLoadInfo(String hostId) {
        String itemId = getItemId(hostId, MEMERY_KEY);

        return getItemValue(itemId, "0");
    }

    public static void main(String[] args) {
        String jsonData = getHosts();
        try {
            JSONArray hostJson = new JSONArray(jsonData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

