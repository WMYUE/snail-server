package com.snail.fitment.common.cache.memcache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICache {

	public void put(Object key,Object value);
	
	/**
	 * 设置有过期时间的缓存
	 * @param key
	 * @param value
	 * @param timeout 单位为秒
	 */
	public void put(Object key,Object value,int timeout);
	
	public Object get(Object key);
	
	public void remove(Object key);
	
	public <T,V> Map<T,V> mget_(List<T> keys);
	
	public void mput(List<Object> list);
	
	public void mremove(Object... keys);
	
	public void setTimeout(int timeout);
	
	public void setRegion(String region);
	
	public Set<String> getAllKeys();

	/**
	 *  使用json方式存储数据需要此项,目前仅RedisCache用到
	 * @param classType
	 */
	void setClazzType(String classType);

	/**
	 * 版本信息
	 * 
	 * @param version
	 */
	void setVersion(String version);

	void clear();
	
}
