package com.snail.fitment.protocol.connection;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.snail.fitment.common.collection.TimeLimitedRingArrayCache;
import com.snail.fitment.common.compress.CompressAlgorithm;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.common.network.BpUtils;
import com.snail.fitment.model.SessionInfo;
import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.bpackage.IBpPackage;

public class BpConnection {
	
	private static final Logger log = Logger.getLogger(BpConnection.class);

	public static Timer timer = new HashedWheelTimer();

	// 是否为短超时时间，默认为 true ，设置为短超时时间，以避免空连接过多
	private boolean shortOvertime = true;
	
	public boolean isShortOvertime() {
		return shortOvertime;
	}

	private AtomicInteger _seq = new AtomicInteger(1);

	private final ConnectionManager manager;
	private final Channel innerChannel;
	private final String connectionId;
	private final Date onlineTime;
	
	//用于记录连接的上下文信息，目前记录短连接认证状态
	private Map<String,Object> context = new HashMap<String,Object>();
	
	TimeLimitedRingArrayCache<IBpPackage> responseCache= new TimeLimitedRingArrayCache<>();
	//new TimeLimitedLRUMap<Integer, IBpPackage>(60000); 
	public int getSeq() {
		return _seq.getAndIncrement();
	}
	public BpConnection(Channel channel,String connectionId,ConnectionManager manager) {
		this.innerChannel = channel;
		onlineTime = new Date();
		this.connectionId = connectionId;
		this.manager= manager;
	}
	public int getResponseCacheSize() {
		return this.responseCache.size();
	}
	public IBpPackage putResponse(int seq, IBpPackage response) {
		return this.responseCache.put(seq, response);
	}
	public boolean containsResponseKey(int seq) {
		return this.responseCache.containsKey(seq);
	}
	public IBpPackage getResponse(int seq) {
		return this.responseCache.get(seq);
	}
	public IBpPackage removeResponse(int seq) {
		return this.responseCache.remove(seq);
	}
	
	public Channel getInnerChannel() {
		return innerChannel;
	}

	public String getClientAddress() {
		return BpUtils.getRemoteAddress(innerChannel);
	}

	public String getConnectionId() {
		return connectionId;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}
	

	public boolean contextContainsKey(String key) {
		return context.containsKey(key);
	}
	
	public Object getContextValue(String key) {
		return context.get(key);
	}
	
	public Object putContextValue(String key, Object value) {
		if(log.isTraceEnabled()){
			log.trace("putContextValue - put "+key + ":" + value +" to connection "+ connectionId);
		}
		return context.put(key, value);
	}

	public void close() {
		if(log.isInfoEnabled()){
			log.info("close connection, connectionId: " + connectionId + ",innerChannel.id: " + innerChannel.getId());
		}		
		innerChannel.close();
	}


	
	/**
	 * 支持返回附件的响应
	 */
	public void sendResponse(IBpPackage response) {
		if(log.isDebugEnabled()){
			log.debug("sendResponse - response: " + response);
		}
		innerChannel.write(response);
	}
	
	public void sendResponse(HttpResponse response){
		if(log.isDebugEnabled()){
			log.debug("sendResponse - innerChannel.id: " + innerChannel.getId() +",  response:"+ JsonConvert.SerializeObject(response));
		}
		if (innerChannel.isConnected()){						
			innerChannel.write(response).addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	/**
	 * 发送请求
	 * @param body
	 */
	public void sendRequest(Map<String, Object> result) {
		this.sendRequest(result, (byte)0, (byte)CompressAlgorithm.ALGORITHM_NOZIP_ID);
	}
	
	
	/**
	 * 发送请求,指定加密或压缩算法
	 * 容错逻辑：判断客户端版本号，低版本不再发通知
	 * @param body
	 */

	public void sendRequest(Map<String, Object>  result, byte encryptType, byte compressType) {

		// 以下发送请求
		int seq = _seq.getAndIncrement();
		IBpPackage request = BpPackageFactory.createPackage(IBpHeader.MESSAGE_TYPE_REQUEST, seq, encryptType, compressType, 0, JsonConvert.SerializeObject(result));

		// 将请求保存到带超时机制的缓存中，供接收到响应时使用
		innerChannel.write(request);
	}
	
	public void setSecretKey(String secretKey) {
		if(secretKey == null) {
			context.remove("secretKey");
		} else {
			context.put("secretKey", secretKey);
		}	
	}
	public String getSecretKey() {
		return (String) context.get("secretKey");
	}
	public void bindSession(SessionInfo session) {
		context.put("session", session);
		this.manager.putNotifyConnection(session.getUserUniId(), this);
	}
	public void unbindSession() {
		this.responseCache.destroy();
		SessionInfo session=(SessionInfo) context.remove("session");
		if(session != null) {
			this.manager.removeNotifyConnection(session.getUserUniId(),this);
		}
	}
	
}
