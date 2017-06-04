package com.snail.fitment.protocol.connection;

import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.snail.fitment.protocol.bpackage.IBpPackage;

public interface ConnectionManager {
	public abstract int size();

	public abstract String getConnectionId(Integer channelId);

	public abstract String getConnectionId(Channel channel);

	public abstract BpConnection getBpConnection(String connectionId);

	public abstract BpConnection getBpConnection(Integer channelId);

	public abstract boolean connectionExists(String connectionId);

	public abstract boolean putIfAbsent(Channel channel);

	public abstract boolean remove(Channel channel);

	public abstract boolean removeConnection(String connectionId);

	// 缓存服务端发出的响应（以支持客户端重发相同 seq 请求的处理）
	public int getResponseCacheSize();

	// public IBpPackage putResponse(String connectionId, int seq, IBpPackage
	// response);
	// public boolean containsResponseKey(String connectionId, int seq);
	// public IBpPackage getResponse(String connectionId, int seq);
	// public IBpPackage removeResponse(String connectionId, int seq);

	boolean sendBpPackage(String connectionId, IBpPackage pkg);

	boolean sendHttpResponse(String connectionId, HttpResponse response);

	public abstract void putNotifyConnection(String userUniId, BpConnection bpConnection);

	public abstract void removeNotifyConnection(String userUniId, BpConnection bpConnection);

	public abstract Set<BpConnection> getNotifyConnection(String userUniId);

}
