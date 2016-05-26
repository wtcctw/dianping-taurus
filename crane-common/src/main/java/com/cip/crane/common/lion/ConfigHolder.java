package com.cip.crane.common.lion;

import com.dianping.lion.client.ConfigChange;
import com.dianping.lion.client.Lion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigHolder {
	
	private static boolean init = false;
	
	private static final Log log = LogFactory.getLog(ConfigHolder.class);
	
	private final static Map<String , String> lionConfigMap = new ConcurrentHashMap<String, String>();
	
	public static String get(LionKeys keyEnum){
		return get(keyEnum.value(),"");
	}
	
	public static String get(String key){
		return get(key,"");
	}
	
	public static String get(String key, String defaultValue){
		String result = null;
		
		try {
			result = lionConfigMap.get(key);
			if(result == null){
				result = getFromLion(key);
				if(StringUtils.isBlank(result)){
					result = defaultValue;
				}
			}
		} catch (Exception e) {
			log.info("Read Lion Key Error, read from defaultValue: " + defaultValue);
			result = defaultValue;
		}
		
		return result;
	}
	
	public void init(){
		
		if(!init){
			synchronized(ConfigHolder.class){
				
				if(!init){
					log.info("ConfigHolder initialize ...");
					
					for(LionKeys key : LionKeys.values()){

						getFromLion(key.value());
					}
					
					Lion.addConfigChangeListener(new ConfigChange(){

						@Override
						public void onChange(String key, String value) {
							
							lionConfigMap.put(key, value);
							
						}
						
					});
					
					init = true;
				}
				
			}
			
		}
	}

	private static String getFromLion(String key){
		String lionValue = Lion.get(key);

		if(StringUtils.isBlank(lionValue)) {
			lionValue = "";
		}

		lionConfigMap.put(key, lionValue);

		return lionValue;
	}
	
}
