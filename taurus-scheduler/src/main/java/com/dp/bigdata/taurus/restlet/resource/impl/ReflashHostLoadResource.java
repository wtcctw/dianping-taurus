package com.dp.bigdata.taurus.restlet.resource.impl;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;

import com.dp.bigdata.taurus.restlet.resource.IReflashHostLoadResource;
import com.dp.bigdata.taurus.restlet.utils.ReFlashHostLoadTask;

public class ReflashHostLoadResource extends ServerResource implements IReflashHostLoadResource {

	@Override
	public boolean isHostOverLoad() {
		String hostName = (String) getRequest().getAttributes().get("hostName");
		
		boolean result = true;
        if (hostName == null || hostName.isEmpty()) {
            result = true;
        } else {
            String jsonString = ReFlashHostLoadTask.hostLoadJsonData;
            if (jsonString == null || jsonString.isEmpty()) {
                result = true;
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        if (jo == null) {
                            result = true;
                            return result;
                        }

                        String zabbixHostName = "";

                        if (jo.get("hostName") != null) {
                            zabbixHostName = jo.get("hostName").toString();
                        }

                        if (StringUtils.isNotBlank(zabbixHostName)) {
                            if (zabbixHostName.equals(hostName)) {
                                String cpuLoad = "";
                                if (jo.get("cpuLoad") != null) {
                                    cpuLoad = jo.get("cpuLoad").toString();
                                }

                                if (StringUtils.isNotBlank(cpuLoad)) {
                                    Double highValue;
                                    if (cpuLoad.trim().equals("null")) {
                                        highValue = 10.0;
                                    } else {
                                        highValue = Double.parseDouble(cpuLoad);
                                    }

                                    if (highValue <= 4.0) {
                                        result = false;
                                    } else {
                                        result = true;
                                    }

                                } else {
                                    result = true;
                                }

                                break;
                            }

                        } else {
                            result = true;
                        }

                    }

                    return result;
                } catch (JSONException e) {
                    return true;
                }
            }
        }
        return result;
    }

}
