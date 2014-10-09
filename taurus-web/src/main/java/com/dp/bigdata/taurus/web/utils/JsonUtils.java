package com.dp.bigdata.taurus.web.utils;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by kirinli on 14-9-30.
 */

public class JsonUtils {
    /**
     * 将json字符串转换为List集合
     *
     * @param jsonArrStr
     * @return
     */
    public static List<Map<String, Integer>> jsonObjList(String jsonArrStr) {
        List<Map<String, Integer>> jsonList = new ArrayList<Map<String, Integer>>();
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(jsonArrStr);
            jsonList = (List<Map<String, Integer>>)JsonUtils.jsonToList(jsonArr);
        } catch (JSONException e) {
            System.out.println("Json字符串转换异常！");
            e.printStackTrace();
        }
        return jsonList;
    }
    /**
     * 将json对象的键值存放在集合，映射table的column
     *
     * @param map
     * @return
     */
    public static List<String> jsonMapKeysList(Map<?, ?> map) {
        List<String> jsonjsonList = new ArrayList<String>();
        String columnStr = "column";
        for (int i = 0; i < map.keySet().size(); i++) {
            jsonjsonList.add(columnStr + (i + 1));
        }
        System.out.println(jsonjsonList.size());
        return jsonjsonList;
    }
    /**
     * 将传递近来的json数组转换为List集合
     *
     * @param jsonArr
     * @return
     * @throws JSONException
     */
    private static List<?> jsonToList(JSONArray jsonArr)
            throws JSONException {
        List<Object> jsonToMapList = new ArrayList<Object>();
        for (int i = 0; i < jsonArr.length(); i++) {
            Object object = jsonArr.get(i);
            if (object instanceof JSONArray) {
                jsonToMapList.add(JsonUtils.jsonToList((JSONArray) object));
            } else if (object instanceof JSONObject) {
                jsonToMapList.add(JsonUtils.jsonToMap((JSONObject) object));
            } else {
                jsonToMapList.add(object);
            }
        }
        return jsonToMapList;
    }
    /**
     * 将传递近来的json对象转换为Map集合
     *
     * @param jsonObj
     * @return
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> jsonToMap(JSONObject jsonObj)
            throws JSONException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Iterator<String> jsonKeys = jsonObj.keys();
        while (jsonKeys.hasNext()) {
            String jsonKey = jsonKeys.next();
            Object jsonValObj = jsonObj.get(jsonKey);
            if (jsonValObj instanceof JSONArray) {
                jsonMap.put(jsonKey, JsonUtils.jsonToList((JSONArray) jsonValObj));
            } else if (jsonValObj instanceof JSONObject) {
                jsonMap.put(jsonKey, JsonUtils.jsonToMap((JSONObject) jsonValObj));
            } else {
                jsonMap.put(jsonKey, jsonValObj);
            }
        }
        return jsonMap;
    }

    public static String mapToJson(Map<String, Object> map) {
        Set<String> keys = map.keySet();
        String key = "";
        String value = "";
        StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            key = (String) it.next();
            value = (String)map.get(key);
            jsonBuffer.append(key + ":" +"\""+ value+"\"");
            if (it.hasNext()) {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }

}


