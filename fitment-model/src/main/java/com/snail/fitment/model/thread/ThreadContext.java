package com.snail.fitment.model.thread;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.snail.fitment.common.lang.StringTools;
import com.snail.fitment.model.SessionInfo;

public class ThreadContext {
	// -------------------------------------- 通用 context 相关 --------------------------------------
	/**
	 * 用于保存通用的线程上下文对象
	 */
	public static final String TRACKIDENTIFIER = "trackIdentifier";
	private static final ThreadLocal<Map<String, Object>> contextMap = new ThreadLocal<Map<String, Object>>();
	public static Object putContext(String key, Object value) {
		return getAndEnsureThreadMap().put(key, value);
	}
	public static Object removeContext(String key) {
		return getAndEnsureThreadMap().remove(key);
	}
	public static Object getContext(String key) {
		return getAndEnsureThreadMap().get(key);
	}

	private static Map<String, Object> getAndEnsureThreadMap() {
		Map<String, Object> localMap = contextMap.get();
		if (localMap == null) {
			localMap = new HashMap<String, Object>();
			contextMap.set(localMap);
		}
		return localMap;
	}
	
	// -------------------------------------- locale 相关 --------------------------------------
	private static final ThreadLocal<String> _locale = new ThreadLocal<String>();

	public static String getLocale() {
		return _locale.get();
	}

	public static void setLocale(String locale) {
		_locale.set(locale);
	}

	
	public static void removeLocale(){
		_locale.remove();
	}
	
	
	// -------------------------------------- sessionInfo 相关 --------------------------------------
	/**
	 * 线程对应的当前用户信息
	 */
	private static final ThreadLocal<SessionInfo> sessionInfo = new ThreadLocal<SessionInfo>();
	
	/**
	 * 直接覆盖 sessionInfo。
	 * 用于 bp 及 thread 初始化的情况。
	 * @param paramSessionInfo
	 */
	public static void setSessionInfo(SessionInfo paramSessionInfo) {
		innerSetSessionInfo(paramSessionInfo);
		incCallDepth();
	}
	private static void innerSetSessionInfo(SessionInfo paramSessionInfo) {
		sessionInfo.set(paramSessionInfo);
		setLoggerContextInfo(paramSessionInfo);
	}
	private static void setLoggerContextInfo(SessionInfo sessionInfo) {
		// add by jiaoxiantong 获取trackId并放入loggerContextInfo中。
		String trackId = (String) ThreadContext.getContext(TRACKIDENTIFIER);
		if (trackId == null) {
			trackId = "";
		}
		if (sessionInfo == null) {
			
		} else {
			StringBuilder sb = new StringBuilder();
			if (trackId.equals("")) {
				if (sessionInfo.getClientNativeId()!= null) {
					sb.append(sessionInfo.getClientNativeId());
				}
			}else {
				sb.append(trackId);
			}
			if (sessionInfo.getUserName() != null) {
				sb.append("-").append(sessionInfo.getUserName());
			}
			if (sessionInfo.getUserUniId() != null) {
				sb.append("-").append(sessionInfo.getUserUniId());
			}
			
		}
	}

	
	public static void removeSessionInfo(){
		sessionInfo.remove();
		callDepth.remove();
	}

	public static SessionInfo getSessionInfo() {
		return sessionInfo.get();
	}

	/**
	 * 设置 sessionInfo 的关键字段
	 * @param loginName
	 * @param clientNativeId
	 * @return
	 */
	public static boolean setSessionInfo(String loginName, String clientNativeId) {
		if (StringTools.isNullOrWhiteSpace(loginName) 
				&& StringTools.isNullOrWhiteSpace(clientNativeId)) {
			return false;
		}
		SessionInfo curSessionInfo = getSessionInfo();
		if (curSessionInfo != null 
				&& StringUtils.equals(loginName, curSessionInfo.getUserName()) 
				&& StringUtils.equals(clientNativeId, curSessionInfo.getClientNativeId())
				) {
			// 与当前缓存 sessionInfo 一致，不再设置，直接返回，防止覆盖 orgId 等有用信息
			return true;
		}
		
		SessionInfo localSessionInfo = new SessionInfo();
		localSessionInfo.setUserName(loginName);
		localSessionInfo.setClientNativeId(clientNativeId);
		setSessionInfo(localSessionInfo);
		return true;
	}
	
	/**
	 * 针对 sessionInfo 可能被嵌套方法重复设置的情况，使用此方法，防止覆盖初始的 sessionInfo。
	 * 主要用于 service 的方法中。
	 * 
	 * @param paramSessionInfo
	 */
	public static void completeSessionInfo(SessionInfo paramSessionInfo) {
		if (paramSessionInfo == null) {
			return;
		}
		SessionInfo localSessionInfo = sessionInfo.get();
		//if (!SessionInfo.isComplete(localSessionInfo)) {
			innerSetSessionInfo(paramSessionInfo);
		//} else {
			// 原有 sessionInfo 信息完整，则不覆盖，防止丢失初始用户的信息
		//}
	}
	// -------------------------------------- callDepth 相关 --------------------------------------
	private static final ThreadLocal<Integer> callDepth = new ThreadLocal<Integer>();
	
	public static void incCallDepth() {
		if (callDepth.get() == null) {
			callDepth.set(0);
		}
		callDepth.set(callDepth.get()+1);
	}
	public static void decCallDepth() {
		callDepth.set(callDepth.get()-1);
	}
	public static int getCallDepth() {
		return callDepth.get();
	}

}
