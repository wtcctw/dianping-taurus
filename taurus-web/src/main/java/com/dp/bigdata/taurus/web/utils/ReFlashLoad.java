package com.dp.bigdata.taurus.web.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kirinli on 14-10-11.
 */
public class ReFlashLoad {
    public static String reFlashHostLoadData(){
        ZabbixUtil.init();
        String jsonData = ZabbixUtil.getHosts();
        JsonArray hostLoadJsonData = new JsonArray();

        try {
            JSONArray hostJson = new JSONArray(jsonData);
            int len = hostJson.length();

            for (int i = 0; i < len; i++) {
                JSONObject jsonObject = (JSONObject) hostJson.get(i);
                String hostId = jsonObject.getString("hostid");
                String name = jsonObject.getString("name");
                String cpuLoad = ZabbixUtil.getCpuLoadInfo(hostId);
                String memeryLoad = ZabbixUtil.getMemeryLoadInfo(hostId);
                if (memeryLoad != null) {
                    Float memLoadFloat = Float.parseFloat(memeryLoad);
                    memeryLoad = (int) (memLoadFloat / (1024 * 1024)) + "MB";
                } else {
                    memeryLoad = "异常数据";
                }

                JsonObject hostLoadJson = new JsonObject();
                hostLoadJson.addProperty("hostId", hostId);
                hostLoadJson.addProperty("hostName", name);
                hostLoadJson.addProperty("cpuLoad", cpuLoad);
                hostLoadJson.addProperty("memLoad", memeryLoad);
                hostLoadJsonData.add(hostLoadJson);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = hostLoadJsonData.toString();
        return jsonString;

    }
}
