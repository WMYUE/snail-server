package com.snail.fitment.protocol.connection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.cache.redis.Redis;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.constants.CommonConstants;
import com.snail.fitment.common.thread.BluePrintThreadFactory;
import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.bpackage.IBpPackage;

import redis.clients.jedis.Jedis;
/**
 * 
 * @author admin
 * 
 * 依赖于不同的Channel的id不会重复
 */
@Component("defaultConnectionManager")
public class DefaultConnectionManager implements ConnectionManager {
	private static final Logger log = Logger.getLogger(DefaultConnectionManager.class);

	private static final IBpPackage EMPTY= BpPackageFactory.createEmptyPackage(IBpHeader.MESSAGE_TYPE_RESPONSE, 0, (byte)0, (byte)0);
	private final ConcurrentMap<String, BpConnection> BpConnectionMap = new ConcurrentHashMap<String, BpConnection>();
	
	private final Map<String,Set<BpConnection>> notifyConnectionMap = new HashMap<>();
	
	public static class SendResultRequestBusTask implements Runnable {
		private IBpPackage pkg;
		private BpConnection connection;
		public SendResultRequestBusTask(final BpConnection connection, final IBpPackage srcPkg) {
			this.pkg=srcPkg;
			this.connection=connection;
		}
		@Override
		public void run() {
			connection.sendResponse(pkg);
		}
		public void buzyResponse() {
			 log.warn("buzyResponse: request:"+pkg+",connection:"+connection) ;
		}
	}
	
	public static class SendHttpResponseBusTask implements Runnable {
		private HttpResponse response;
		private BpConnection connection;
		public SendHttpResponseBusTask(final BpConnection connection, final HttpResponse response) {
			this.response=response;
			this.connection=connection;
		}
		@Override
		public void run() {
			connection.sendResponse(response);
		}
		public void buzyResponse() {
			 log.warn("buzyResponse: request:"+response+",connection:"+connection) ;
		}
	}
	
	/*
	 * 以下为分队列策略 
	 */
	private RejectedExecutionHandler discardOldest = new RejectedExecutionHandler() {
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			if (!e.isShutdown()) {
				SendResultRequestBusTask task = (SendResultRequestBusTask) e.getQueue().poll();
				task.buzyResponse();
				e.execute(r);
			}
		}
	};
	
	private ThreadPoolExecutor sendExecutor = new ThreadPoolExecutor(
			FitmentConfig.BP_SEND_TASK_POOL_CORE_SIZE,
			FitmentConfig.BP_SEND_TASK_POOL_MAX_SIZE, 60, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(
					FitmentConfig.BP_SEND_TASK_QUEUE_SIZE, false),
			new BluePrintThreadFactory("BluePrintSend #"), discardOldest);
	
	// key: connectionId + "-" + seq, value: sent response
//	private final TimeLimitedMap<String, IBpPackage> responseCache = 
//		new TimeLimitedMap<String, IBpPackage>(60000); // 默认缓存发出响应超时时间为 5分钟
	

	public Set<String> getConnectionIds() {
		return BpConnectionMap.keySet();
	}

	public Collection<BpConnection> getConnections() {
		return BpConnectionMap.values();
	}

	@Override
	public BpConnection getBpConnection(Integer channelId) {
		return BpConnectionMap.get(getConnectionId(channelId));
	}
	
	@Override
	public BpConnection getBpConnection(String connectionId) {
		return BpConnectionMap.get(connectionId);
	}

	@Override
	public boolean connectionExists(String connectionId) {
		return BpConnectionMap.containsKey(connectionId);
	}

	@Override
	public synchronized boolean putIfAbsent(Channel channel) {
		BpConnection connection = new BpConnection(channel, getConnectionId(channel),this);
		BpConnection old=BpConnectionMap.putIfAbsent(connection.getConnectionId(), connection);
		if(old != null) {
			old.unbindSession();
		}
		return true;
	}

	@Override
	public synchronized boolean remove(Channel channel) {
		return removeConnection(getConnectionId(channel));
	}

	@Override
	public boolean removeConnection(String connectionId) {
		BpConnection old =BpConnectionMap.remove(connectionId);	
		if(old != null) {
			old.unbindSession();
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return BpConnectionMap.size();
	}
	
	@Override
	public boolean sendBpPackage(String connectionId,IBpPackage pkg) {
		BpConnection conn = this.getBpConnection(connectionId);
		if (conn != null) {
			if(pkg.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_RESPONSE) {
				conn.putResponse(pkg.getBpHeader().getSeq(), pkg);
			}
			this.sendExecutor.execute(new SendResultRequestBusTask(conn,pkg));
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean sendHttpResponse(String connectionId, HttpResponse response) {
		BpConnection conn = this.getBpConnection(connectionId);
		
		if (conn != null) {
			this.sendExecutor.execute(new SendHttpResponseBusTask(conn,response));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getConnectionId(Integer channelId) {
		return new StringBuilder().append(channelId).append("@").append(FitmentConfig.connectionNode).toString();
	}

	@Override
	public String getConnectionId(Channel channel) {
		return getConnectionId(channel.getId());
	}
	
	
//	// 请求缓存
//	private String getCacheKey(String connectionId, int seq) {
//		return new StringBuilder(connectionId).append("-").append(seq).toString();
//	}

	
	// 响应缓存
//	@Override
//	public boolean containsResponseKey(String connectionId, int seq) {
//		BpConnection connection = this.getBpConnection(connectionId);
//		if(connection != null) {
//			return connection.getResponseCache().containsKey(seq);
//		}
//		return false;
//	}

//	@Override
//	public IBpPackage getResponse(String connectionId, int seq) {
//		BpConnection connection = this.getBpConnection(connectionId);
//		if(connection != null) {
//			IBpPackage response= connection.getResponseCache().get(seq);
//			if(response == EMPTY) {
//				return null;
//			}
//			return response;
//		}
//		return null;
//	}

	@Override
	public int getResponseCacheSize() {
		int total =0;
		for(BpConnection connection:this.BpConnectionMap.values()) {
			total=total+connection.getResponseCacheSize();
		}
		return total;
	}

//	@Override
//	public IBpPackage putResponse(String connectionId, int seq,
//			IBpPackage response) {
//		if(response == null) {
//			response = EMPTY;
//		}
//		return responseCache.put(getCacheKey(connectionId, seq), response);
//	}
//
//	@Override
//	public IBpPackage removeResponse(String connectionId, int seq) {
//		return responseCache.remove(getCacheKey(connectionId, seq));
//	}

	@Override
	public void putNotifyConnection(String userUniId, BpConnection bpConnection) {
		synchronized(this.notifyConnectionMap) {
			Set<BpConnection> connections = this.notifyConnectionMap.get(userUniId);
			if(connections == null) {
				connections= new HashSet<>();
				this.notifyConnectionMap.put(userUniId, connections);
				registNode(userUniId);
			}
			connections.add(bpConnection);
		}
	}

	@Override
	public void removeNotifyConnection(String userUniId, BpConnection bpConnection) {
		synchronized(this.notifyConnectionMap) {
			Set<BpConnection> connections = this.notifyConnectionMap.get(userUniId);
			if(connections != null) {
				connections.remove(bpConnection);
				if(connections.size()==0) {
					this.notifyConnectionMap.remove(userUniId);
					unregistNode(userUniId);
				}
			}
		}
	}
	
	@Override
	public Set<BpConnection> getNotifyConnection(String userUniId) {
		synchronized(this.notifyConnectionMap) {
			return this.notifyConnectionMap.get(userUniId);
		}
	}
	private void unregistNode(String userUniId) {
		if(FitmentConfig.listenerUrl.length()>0) return;//是前端不需要注册redis的侦听
		Jedis jedis = null;
		try {
			jedis = Redis.getJedis();
			jedis.hdel(CommonConstants.REDIS_CHANNEL_PREFIX+userUniId,FitmentConfig.connectionNode);
		} catch (Throwable t) {
			Redis.returnBrokenResource(jedis);
		} finally {
			Redis.returnResource(jedis);
		}
	}
	
	private void registNode(String userUniId) {
		if(FitmentConfig.listenerUrl.length()>0) return;//是前端不需要注册redis的侦听
		Jedis jedis = null;
		try {
			jedis = Redis.getJedis();
			jedis.hsetnx(CommonConstants.REDIS_CHANNEL_PREFIX+userUniId, FitmentConfig.connectionNode,FitmentConfig.connectionNode);
		} catch (Throwable t) {
			Redis.returnBrokenResource(jedis);
		} finally {
			Redis.returnResource(jedis);
		}
	}

	
}
