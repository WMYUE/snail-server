package com.snail.fitment.service.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.ISessionManager;
import com.snail.fitment.common.cache.redis.Redis;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.constants.CommonConstants;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.model.SessionInfo;

import redis.clients.jedis.Jedis;

@Lazy(false) @Component("remoteSessionManager")
public class RemoteSessionManager implements ISessionManager {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private final Map<String,SessionInfo> localCache= new ConcurrentHashMap<>();
	private void updateAsyn(SessionInfo session) {
		localCache.put(session.getSessionId(), session);
		executor.execute(new Runnable() {
			public void run() {
				SessionInfo[] array = new SessionInfo[localCache.size()];
				array=localCache.values().toArray(array);
				for(SessionInfo info:array) {
					localCache.remove(info.getSessionId());
				}
				Jedis jedis = null;
				try{
					jedis = Redis.getJedis();
					for(SessionInfo session:array) {
						String sessionJson = JsonConvert.SerializeObject(session);
						jedis.setex(CommonConstants.REDIS_SESSION_PREFIX+session.getSessionId(), FitmentConfig.defaultSessionTimeout, sessionJson);
					}
				}catch(Throwable t){
					Redis.returnBrokenResource(jedis);
				}finally{
					Redis.returnResource(jedis);
				}
			}
		});
	}
	@Override
	public SessionInfo getSession(String authToken) {
		Jedis jedis = null;
		SessionInfo	sessionInfo = null;
		try{
			jedis = Redis.getJedis();
			String sessionJson=jedis.get(CommonConstants.REDIS_SESSION_PREFIX+authToken);
			if(sessionJson != null) {
				sessionInfo = JsonConvert.DeserializeObject(sessionJson, SessionInfo.class);
				this.updateAsyn(sessionInfo);
			}
		}catch(Throwable t){
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
		return sessionInfo;
	}

	@Override
	public boolean updateSession(SessionInfo session) {
		this.updateAsyn(session);
		return true;
	}
	@Override
	public void logoutSession(String sessionId) {
		Jedis jedis = null;
		try{
			jedis = Redis.getJedis();
			if(jedis.exists(CommonConstants.REDIS_SESSION_PREFIX + sessionId)){				
				jedis.del(CommonConstants.REDIS_SESSION_PREFIX + sessionId);
			}
		}catch(Throwable t){
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
		
	}
}
