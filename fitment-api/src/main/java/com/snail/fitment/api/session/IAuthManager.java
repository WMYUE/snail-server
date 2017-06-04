package com.snail.fitment.api.session;

import java.util.Map;

import com.snail.fitment.model.SessionInfo;

public interface IAuthManager {
	/**
	 * 返回适用的appkey
	 * @return
	 */
	public String getAppKey();
	
	public IToken createToken(Map<String,String> tokenParams);
	
	public SessionInfo authWithToken(IToken token);
	
	/**
	 * 刷新认证token，成功返回新的session信息（如果旧的session还在，刷新并返回旧有session），目前主要给webserver用
	 * @param token
	 * @return
	 */
	public SessionInfo refreshWithToken(IToken token);
	
	public SessionInfo logout(String sessionId,String token);
	
	/**
	 * 获取额外的授权认证信息
	 * @param sessionInfo
	 * @param token
	 * @return
	 */
	public Map<String,Object> getExAuthInfo(SessionInfo sessionInfo, IToken token);
}
