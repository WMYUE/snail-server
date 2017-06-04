package com.snail.fitment.service.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.ITokenManager;
import com.snail.fitment.common.cache.redis.Redis;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.constants.CommonConstants;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.model.Auth2TokenInfo;

import redis.clients.jedis.Jedis;

@Lazy(false) @Component("tokenManager")

public class TokenManager implements ITokenManager {
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private final Map<String,Auth2TokenInfo> localCache= new ConcurrentHashMap<>();
	
	@Override
	public void saveToken(Auth2TokenInfo token) {
		localCache.put(token.getId(), token);
		executor.execute(new Runnable() {
			public void run() {
				Auth2TokenInfo[] array = new Auth2TokenInfo[localCache.size()];
				array=localCache.values().toArray(array);
				for(Auth2TokenInfo info:array) {
					localCache.remove(info.getId());
				}
				Jedis jedis = null;
				try{
					jedis = Redis.getJedis();
					for(Auth2TokenInfo token:array) {
						String tokenJson = JsonConvert.SerializeObject(token);
						jedis.setex(CommonConstants.REDIS_TOKEN_PREFIX+token.getId(), FitmentConfig.defaultTokenTimeout, tokenJson);
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
	public Auth2TokenInfo getToken(String userUniId, String sessionId) {
		Jedis jedis = null;
		Auth2TokenInfo	tokenInfo = null;
		try{
			jedis = Redis.getJedis();
			String tokenJson=jedis.get(CommonConstants.REDIS_TOKEN_PREFIX+Auth2TokenInfo.buildId(userUniId,sessionId));
			if(tokenJson != null) {
				tokenInfo = JsonConvert.DeserializeObject(tokenJson, Auth2TokenInfo.class);
			}
		}catch(Throwable t){
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
		return tokenInfo;
	}

	@Override
	public void removeToken(Auth2TokenInfo token) {
		Jedis jedis = null;
		try{
			jedis = Redis.getJedis();
			jedis.del(CommonConstants.REDIS_TOKEN_PREFIX+token.getId());
		}catch(Throwable t){
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
	}

}
