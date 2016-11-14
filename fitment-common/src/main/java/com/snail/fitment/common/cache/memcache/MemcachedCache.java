package com.snail.fitment.common.cache.memcache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class MemcachedCache extends AbstractCache implements ICache {
	@Autowired
	private MemcachedClient memcachedClient;
	private Logger logger=Logger.getLogger(MemcachedCache.class);
	private int defaultTimeout = 3600;
	@Override
	public void put(Object key, Object value) {
		try{
		if(value!=null){
			memcachedClient.set(this.getKey(key), getTimeout(), value);
			}
		}catch (Exception e){
			logger.error("error:put "+key+" from memcached ,region "+this.region+" ,"+ e);
		}
	}

	@Override
	public void put(Object key, Object value, int timeout) {
		try{
		if(value!=null){
			memcachedClient.set(this.getKey(key), timeout, value);
			}
		}catch (Exception e){
			logger.error("error:put "+key+" from memcached ,region "+this.region+" ,"+ e);
		}
	}

	@Override
	public Object get(Object key) {
		try{
		Object value=memcachedClient.get(this.getKey(key));
		return value;
		}catch (Exception e){
			logger.error("error:get "+key+" from memcached ,region "+this.region+" ,"+ e);
			return null;
		}
	}

	@Override
	public void remove(Object key) {
		try{
		memcachedClient.delete(this.getKey(key));
		
		Object obj=get(key);
		if(obj!=null){
			logger.error("delete cache failed "+obj);
		}
	}catch (Exception e){
		logger.error("error:delete "+key+" from memcached ,region "+this.region+" ,"+ e);
	}
	}
	
	@Override
	public <T,V> Map<T, V> mget_(List<T> keys) {
		if ( keys == null || keys.isEmpty() ) {
			return new HashMap<T,V>();
		}
		
		try{
			Map<T, V> resultMap = new HashMap<T, V>();
			Map<String, T> keyRefMap = new HashMap<String, T>();
			for (T key : keys) {
				String hashKey = this.getKey(key);
				keyRefMap.put(hashKey, key);
			}
			
			Map<String,Object> cashValueMap =  memcachedClient.getBulk(keyRefMap.keySet());
			if (cashValueMap != null && !cashValueMap.isEmpty()) {
				for(Entry<String,Object> entry:cashValueMap.entrySet()){
					if(entry.getValue()!=null){
						resultMap.put(keyRefMap.get(entry.getKey()), (V)entry.getValue());
					}	
				}	
			}
			return resultMap;
		}catch (Exception e){
			logger.error("memcached mget error: ", e);	
			return new HashMap<T, V>();
		}
	}
	
	@Override
	public void mput(List<Object> list) {
		throw new UnsupportedOperationException("不支持此操作");
	}

	@Override
	public void mremove(Object... keys) {
		throw new UnsupportedOperationException("不支持此操作");
	}

	
	public String getKey(Object key){
		return DigestUtils.md5Hex((new StringBuilder(getRegion()).append("-")
				.append(key).toString()));
	}

	@Override
	public void setClazzType(String classType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		if (super.getTimeout() == 0)
			return this.defaultTimeout;
		else
			return super.getTimeout();
	}
	
	
	public static void main(String[] args) {
		MemcachedCache m = new MemcachedCache();
		m.setRegion("blueCardDetail");
		String key = m.getKey("89407");
		System.out.println(key);
	}
}
