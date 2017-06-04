package com.snail.fitment.model;

import java.util.HashMap;
import java.util.Map;

import com.snail.fitment.common.json.JsonConvert;

public enum SynchType {
	defaultData,
	BehaviorData,
	Application,
	sysMessage;
	
	public final static String getSynchType(Integer synchType,Object data,String userUniId){
		Map<String, Object> map =  new HashMap<>();
		map.put("synchType",synchType);
		if(data != null) {
			map.put("data", data);
		}
		if(userUniId != null) {
			map.put("userUniId", userUniId);
		}
		return JsonConvert.SerializeObject(map);
	}
	public final static String getSynchType(String synchType,Object data){
		return getSynchType(synchType,data,null);
	}
	public final static String getSynchType(String synchType,Object data,String userUniId){
		Integer syncType= SynchType.defaultData.ordinal();	
		if("syncBehavior".equals(synchType)){
			syncType = SynchType.BehaviorData.ordinal();
		}else if("application".equals(synchType)){
			syncType = SynchType.Application.ordinal();
		}else{
			syncType =SynchType.sysMessage.ordinal();
		}
		return getSynchType(syncType,data,userUniId);
	}
	
}
