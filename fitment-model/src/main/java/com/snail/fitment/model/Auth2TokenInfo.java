package com.snail.fitment.model;

import java.io.Serializable;

public class Auth2TokenInfo implements Serializable {
	private static final long serialVersionUID = -1080326385555096860L;
	private SessionInfo sessionInfo;
	private String accessToken;

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getId() {
		return buildId(sessionInfo.getUserUniId(), sessionInfo.getSessionId());
	}

	public static String buildId(String userUniId, String sessionId) {
		return userUniId + "|" + sessionId;
	}
}
