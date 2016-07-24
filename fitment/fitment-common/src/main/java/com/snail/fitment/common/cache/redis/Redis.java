package com.snail.fitment.common.cache.redis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.snail.fitment.common.config.FitmentConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class Redis {
	
	/**
	 * redis初始化
	 */
	private static final Logger logger=Logger.getLogger(Redis.class);
	public static final FitmentJedisPool pool;
	private static String key="0$mRG22DVpk%WE6$OQmD";
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(10000);
		config.setMaxWait(10000);
		config.setMaxIdle(50);
		config.setTestOnBorrow(true);
		config.setMinIdle(10);
		config.setTestWhileIdle(true);
		config.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
		try {
			if(StringUtils.isEmpty("")){
				pool=new FitmentJedisPool(config, FitmentConfig.REDIS_SERVER);
			}else{
				pool = new FitmentJedisPool(config, FitmentConfig.REDIS_SERVER,FitmentConfig.REDIS_PORT,FitmentConfig.REDIS_TIMEOUT, FitmentConfig.REDIS_PASSWD);
			}
		} catch (Exception e) {
			logger.error("redis初始化失败，密码解密错误");
			throw new RuntimeException("redis初始化失败，密码解密错误");
		}
	}
	
	public final static Jedis getJedis() {
		Jedis jedis=null;
		try{
		 jedis=pool.getResource();
		 return jedis;
		}catch (Exception e){
			logger.error("获取jedis连接失败，稍后将重试",e);
			returnBrokenResource(jedis);		
			waitSeconds(30000);
			return getJedis();
		}
	}
	
	public final static void returnResource(Jedis jedis) {
		if(jedis!=null){
			pool.returnResource(jedis);
		}
	}
	
	public final static void returnBrokenResource(Jedis jedis) {
		if(jedis!=null){
			pool.returnBrokenResource(jedis);
			jedis=null;
			logger.error("returnBrokenResource exception , " +getPoolStatus());
		}
	}
	
	private static String getPoolStatus(){
		StringBuilder b=new StringBuilder("redis pool status: active ").append(pool.getNumActive())
		.append(" , idle ").append(pool.getNumIdle()).append(" , max ").append(pool.getMaxActive());
		return b.toString();
	}
	
	public static void printPoolStatus(){
		if(logger.isInfoEnabled()){
			logger.info(getPoolStatus());
		}
		
	}
	
	private static void waitSeconds(long mileseconds) {
		try {
			Thread.sleep(mileseconds);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
	}
}
