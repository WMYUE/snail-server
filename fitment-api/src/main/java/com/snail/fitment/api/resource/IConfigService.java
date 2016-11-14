package com.snail.fitment.api.resource;

import java.util.List;

import com.snail.fitment.model.request.ReturnResult;

public interface IConfigService {
	public ReturnResult queryConfigyByKey(String key);
	
	public ReturnResult queryConfigyByKeyList(List<String> keyList);
	
	public ReturnResult saveConfig(String key, String value, String description, String file);
	
	public ReturnResult getConfigResourceByKey(String key);
	
	public ReturnResult queryServerInfo(String serverType);
}
