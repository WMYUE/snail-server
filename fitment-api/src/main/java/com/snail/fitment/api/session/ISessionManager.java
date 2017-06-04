package com.snail.fitment.api.session;

import com.snail.fitment.model.SessionInfo;

public interface ISessionManager {
	/**
	 * 返回对应的session信息，如果超时或没有认证，返回null
	 * @param sessionId
	 * @return
	 */
	public SessionInfo getSession(String sessionId);
	
	/**
	 * 更新session信息，如果存在，根据策略更新，否则新建
	 * @param session
	 * @return
	 */
	public boolean updateSession(SessionInfo session);

	/**
	 * 注销session信息
	 * @param sessionId
	 */
	public void logoutSession(String sessionId);
	
}
