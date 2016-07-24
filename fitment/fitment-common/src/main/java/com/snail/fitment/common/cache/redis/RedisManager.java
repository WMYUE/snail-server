package com.snail.fitment.common.cache.redis;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

public class RedisManager {
	public static Logger log = Logger.getLogger(RedisManager.class);
	
	public static String getFromCachedServer(String key){
		Jedis jedis=null;
		try{
			log.info("get value from redis server start ,key = " + key);
			jedis = Redis.getJedis();
			String result = jedis.get(key);
			return result;
		}catch(Throwable t){
			log.error("监听跨节点消息线程异常终止, 1秒后将会重新启动",t);
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			log.error(e);
		}
		
		return null;
	}
	
	public static void putIntoCachedServer(String key, String value, int expireTime){
		Jedis jedis=null;
		try{
			log.info("put value into redis server start, key = "+ key + ", value =" + value);
			jedis = Redis.getJedis();
			jedis.set(key, value);
			if(expireTime > 0){				
				jedis.expire(key, expireTime);
			}
		}catch(Throwable t){
			log.error("监听跨节点消息线程异常终止, 1秒后将会重新启动",t);
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}
	
	public static void deleteFromCachedServer(String key){
		Jedis jedis=null;
		
		try{
			log.info("delete value into redis server start, key =" + key);
			jedis = Redis.getJedis();
			if(jedis.exists(key)){				
				jedis.del(key);
			}
		}catch(Throwable t){
			log.error("监听跨节点消息线程异常终止, 1秒后将会重新启动",t);
			Redis.returnBrokenResource(jedis);
		}finally{
			Redis.returnResource(jedis);
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}
	
}
