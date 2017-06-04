package com.snail.fitment.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SessionInfo implements Serializable {
	private static final long serialVersionUID = -2372964148180130251L;
	private String sessionId; // 会话ID
	private long userId; // 用户ID
	private long loginInfoId; // 登录ID
	private String loginName; // 登录名
	private String userName; // 用户名(非登录名)
	private String userUniId; // 当前登录身份在所属组织内的唯一ID
	private String clientNativeId; //
	private String domain; // 当前登录身份的组织
	private long updateTime;// 刷新时间
	private int role;// 当前角色
	private Map<String, String> additionalParameters; // web额外参数

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public void addParameters(String key, String value) {
		if (additionalParameters == null) {
			additionalParameters = new HashMap<>();
		}
		additionalParameters.put(key, value);
	}

	public String getParameters(String key) {
		if (additionalParameters == null) {
			return "";
		}
		return additionalParameters.get(key);
	}

	public String getSecondDomain() { // 二级域名
		return getParameters("secondDomain");
	}

	public void setSecondDomain(String secondDomain) {
		addParameters("secondDomain", secondDomain);
	}

	public String getDomianName() { // domain名称
		return getParameters("domainName");
	}

	public void setDomianName(String domianName) {
		addParameters("domainName", domianName);
	}

	public String getDomainLogo() { // domain Logo
		return getParameters("domianLogo");
	}

	public void setDomainLogo(String domainLogo) {
		addParameters("domainLogo", domainLogo);
	}

	public Map<String, String> getAdditionalParameters() {
		return additionalParameters;
	}

	public void setAdditionalParameters(Map<String, String> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String toString() {
		return "SessionInfo [sessionId=" + sessionId + ", userId=" + userId + ", loginInfoId=" + loginInfoId
				+ ", loginName=" + loginName + ", userName=" + userName + ", userUniId=" + userUniId
				+ ", clientNativeId=" + clientNativeId + ", domain=" + domain + ", updateTime=" + updateTime + ", role="
				+ role + ", additionalParameters=" + additionalParameters + "]";
	}

}
