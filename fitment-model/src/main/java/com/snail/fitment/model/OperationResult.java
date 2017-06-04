package com.snail.fitment.model;


import java.util.HashMap;
import java.util.Map;

public class OperationResult {
	
	public static final String resultCodeKey = "stateCode";
	/**
	 * 若为 null 则不需要转发，否则，需要转发
	 */
	private String redirectConnectionId = null;
	private Map<String, Object> resultList = new HashMap<String, Object>();

	/**
	 * 跨服务器透传的用户操作请求，不需要同步返回响应结果
	 */
	private boolean needSynchReturnResponse = true;
	

	public static OperationResult getDefaultSuccessResult(){
		OperationResult result = new OperationResult();
		result.getResultList().put(resultCodeKey, 0);
		return result;
	}
	
	public boolean isSuccessed(){
		Integer resultCode = (Integer)resultList.get(resultCodeKey);
		return resultCode!=null && resultCode==0;
	}
	
	public Map<String, Object> getResultList() {
		return resultList;
	}

	public void setResultList(Map<String, Object> resultList) {
		this.resultList = resultList;
	}

	public String getRedirectConnectionId() {
		return redirectConnectionId;
	}

	public void setRedirectConnectionId(String redirectConnectionId) {
		this.redirectConnectionId = redirectConnectionId;
	}


	public boolean isNeedSynchReturnResponse() {
		return needSynchReturnResponse;
	}

	public void setNeedSynchReturnResponse(boolean needSynchReturnResponse) {
		this.needSynchReturnResponse = needSynchReturnResponse;
	}

	@Override
	public String toString() {
		return "OperationResult [redirectConnectionId=" + redirectConnectionId + ", resultList=" + resultList +
				", needSynchReturnResponse=" + needSynchReturnResponse + "]";
	}

}
