package com.snail.fitment.common.cache.redis;


import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

public class FitmentJedisPool extends JedisPool {

	public FitmentJedisPool(final Config poolConfig, final String host) {
		super(poolConfig,host);
	}
    public FitmentJedisPool(final Config poolConfig, final String host, int port,
            int timeout, final String password) {
        super(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE);
    }

    public FitmentJedisPool(final Config poolConfig, final String host, final int port) {
        super(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE);
    }

    public FitmentJedisPool(final Config poolConfig, final String host, final int port, final int timeout) {
        super(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE);
    }
	
	
	public int getNumActive(){
		return internalPool.getNumActive();
	}
	
	public int getNumIdle(){
		return internalPool.getNumIdle();
	}
	
	public int getMaxActive(){
		return internalPool.getMaxActive();
	}
}
