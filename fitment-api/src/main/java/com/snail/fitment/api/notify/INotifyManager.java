package com.snail.fitment.api.notify;

public interface INotifyManager {
	public static final String STUB = "WebServer";// 为了通知webserver

	/**
	 * 通知用户同步
	 * 
	 * @param userUniId
	 * @param syncType
	 */
	public void notifyUserSync(String userUniId, String syncType);

	/**
	 * 带数据通知用户同步 目前主要供webserver用
	 * 
	 * @param userUniId
	 * @param syncType
	 * @param info
	 */
	public void notifyUserSyncData(String userUniId, String syncType, Object info);
}
