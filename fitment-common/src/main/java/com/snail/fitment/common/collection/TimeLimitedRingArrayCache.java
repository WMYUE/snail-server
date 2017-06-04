package com.snail.fitment.common.collection;

import java.util.Timer;
import java.util.TimerTask;

import com.snail.fitment.common.timer.TimerManager;

public class TimeLimitedRingArrayCache<T> {
	private static class CacheObj<T> {
		private long cacheTime;
		private T value;
		private int key;
	}
	static final int MAXIMUM_CAPACITY = 1 << 30;
	static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
	
	volatile private CacheObj<T>[] innercache;
	private volatile int size;
	private long delay;
	private static final Timer timer = TimerManager.getTimer("TimeLimitedLRUMap SelfRemove");
	private TimerTask overTimeTask;

	public TimeLimitedRingArrayCache() {
		this(8);
	}

	public int size() {
		return size;
	}
	public TimeLimitedRingArrayCache(int maxSize) {
		this(maxSize,60 *1000);
	}
	
	@SuppressWarnings("unchecked")
	public TimeLimitedRingArrayCache(int maxSize,long delay) {
		this.delay = delay;
		if(maxSize<=0) {
			innercache = new CacheObj[0];
		} else {
			innercache = new CacheObj[tableSizeFor(maxSize)];
		}
		overTimeTask = new TimerTask() {
			@Override
			public void run() {
				checkOverTime();
			}
		};
		timer.schedule(overTimeTask, delay, 10000);
	}
	public synchronized T put(int key, T value) {
		if(innercache.length==0) return null;
		int index = (innercache.length-1) & key;
		CacheObj<T> obj = innercache[index];
		T oldValue=null;
		if(obj == null) {
			obj = new CacheObj<T>();
			size++;
		} else {
			oldValue=obj.value;
		}
		obj.cacheTime=System.currentTimeMillis();
		obj.key=key;
		obj.value=value;
		innercache[index]=obj;
		return oldValue;
	}
	
	public synchronized T get(int key) {
		if(innercache.length==0) return null;
		int index = (innercache.length-1) & key;
		CacheObj<T> obj = innercache[index];
		if(obj == null || obj.key != key) {
			return null;
		} else {
			return obj.value;
		}
	}
	
	public synchronized T remove(int key) {
		if(innercache.length==0) return null;
		int index = (innercache.length-1) & key;
		CacheObj<T> obj = innercache[index];
		innercache[index]=null;
		if(obj != null) {
			size--;
			return obj.value;
		}
		return null;
	}

	private synchronized void checkOverTime() {
		long current = System.currentTimeMillis();
		for(int i=0;i<innercache.length;i++) {
			CacheObj<T> obj = innercache[i];
			if(obj != null && current-obj.cacheTime>delay) {
				size--;
				innercache[i] = null;
			}
		}
	}

	public synchronized boolean containsKey(int seq) {
		if(innercache.length==0) return false;
		int index = (innercache.length-1) & seq;
		CacheObj<T> obj = innercache[index];
		if(obj != null && obj.key == seq) {
			return true;
		}
		return false;
	}

	public synchronized void destroy() {
		overTimeTask.cancel();
	}
}
