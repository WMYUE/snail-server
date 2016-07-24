package com.snail.fitment.model;

import java.io.Serializable;

public class SessionInfo implements Serializable{
	private static final long serialVersionUID = -2372964148180130251L;
	
	private String sessionId;
	private long userId;
	private long loginInfoId;
	private String loginName;
	private String userUniId;
    private String clientNativeId;
    private long updateTime;//刷新时间

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public long getLoginInfoId() {
		return loginInfoId;
	}

	public void setLoginInfoId(long loginInfoId) {
		this.loginInfoId = loginInfoId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}
	

	public String getUserUniId() {
		return userUniId;
	}

	public void setUserUniId(String userUniId) {
		this.userUniId = userUniId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getClientNativeId() {
		return clientNativeId;
	}

	public void setClientNativeId(String clientNativeId) {
		this.clientNativeId = clientNativeId;
	}
    
    
}
