package com.snail.fitment.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snail.fitment.common.code.CommonStatusCode;

public class Response implements java.io.Serializable{
	private static final long serialVersionUID = 476412762724299172L;
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	int errcode = 0;
	
	String errmsg = "";

	Map<String, Object> result;

	public Response() {
		
	}
	
	public Response(int errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}
	
	public int getErrcode() {
		return errcode;
	}


	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrmsg() {
		if(StringUtils.isEmpty(errmsg)){
			errmsg = CommonStatusCode.desc(this.errcode);
		}
		
		if (StringUtils.isEmpty(errmsg)) {
			errmsg = "未知错误";
		}
		return errmsg;
	}

	public boolean isSuccess() {
		return this.errcode == 0;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void addResult(String key, Object value) {
		if (result == null) {
			result = new HashMap<String, Object>();
		}
		result.put(key, value);
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return this.toJson();
	}

	public String toJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errmsg", this.getErrmsg());
		map.put("errcode", this.errcode);
		if (this.result != null) {
			for (Entry<String, Object> entry : result.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return gson.toJson(map);
	}	
}
