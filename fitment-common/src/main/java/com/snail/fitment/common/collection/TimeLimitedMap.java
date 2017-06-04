package com.snail.fitment.common.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.snail.fitment.common.timer.TimerManager;


public class TimeLimitedMap<K, V> implements Map<K, V>, Serializable  {
    private static final long serialVersionUID = 3257001042985433396L;

    private static final Logger log =  Logger.getLogger(TimeLimitedMap.class);

    // 定时调度Timer类
    private static final Timer timer = TimerManager.getTimer("TimeLimitedMap SelfRemove");

    // 默认的超时时限为一小时
    private long delay;

    // HashMap<Object, TaskObject>,其中Object为key
    private Map<K, OvertimeTask> taskMap = new HashMap<K, OvertimeTask>();

    // HashMap<Object, Object>,put是存放的Map
    private Map<K, V> valueMap = new HashMap<K, V>();

    public TimeLimitedMap() {
        delay = 60 * 60 * 1000;
    }

    /**
     * @param delay 默认超时时限
     */
    public TimeLimitedMap(int delay) {
        this.delay = delay;
    }

    /**
     * 从容器中取出指定key映射的value
     * @param key 需要查找的value
     * @return 指定key映射的value
     */
    public synchronized V get(Object key) {
        return valueMap.get(key);
    }

    /**
     * 往容器中放入一个映射关系，超时时限为默认时限
     * 
     * @param key 需要存放映射的key
     * @param value 需要存放映射的value
     * @return 放入value对应的key
     */
    public synchronized V put(K key, V value) {
        return put(key, value, delay);
    }

    /**
     * 往容器中放入一个映射关系，并指定超时时限
     * @param key 需要存放映射的key
     * @param value 需要存放映射的value
     * @param delay0 清除时限
     * @return 放入value对应的key
     */
    public synchronized V put(K key, V value, long delay0) {
        return put(key, value, delay0, null);
    }

    /**
     * 往容器中放入一个映射关系，超时时限为默认时限,指定超时后的处理方式
     * @param key 需要存放映射的key
     * @param value 需要存放映射的value
     * @param overtimeProcess 超时后处理的任务
     * @return 放入value对应的key
     */
    public synchronized V put(K key, V value,
    		OvertimeProcess<K, V> overtimeProcess) {
        return put(key, value, delay, overtimeProcess);
    }

    /**
     * 往容器中放入一个映射关系，并指定超时时限和超时后的处理方法
     * @param key 需要存放映射的key
     * @param value 需要存放映射的value
     * @param delay0 清除时限
     * @param overtimeProcess 超时后处理的任务
     * @return 放入value对应的key
     */
    public synchronized V put(K key, V value, long delay0,
    		OvertimeProcess<K, V> overtimeProcess) {
        if (delay0 <= 0)
            throw new IllegalArgumentException("清除时限必须为大于0");
        V result = valueMap.put(key, value);

        refresh0(key, delay0, overtimeProcess);

        return result;
    }

    /**
     * 刷新指定key对应value的清除时限，并且清除原有的超时处理
     * @param key 需要存放映射的key
     * @param delay0 清除时限
     * @return 如果不存在key对应的value返回null
     */
    public synchronized V refreshDelayAndClearOvertimeProcess(K key,
            long delay0) {
        return refreshDelayAndChangeOvertimeProcess(key, delay0, null);
    }

    /**
     * 刷新指定key对应value的清除时限,但是原有的超时方式将会保留
     * @param key 需要存放映射的key
     * @param delay0 清除时限
     * @return 如果不存在key对应的value返回null
     */
    public synchronized V refreshDelayAndHoldOvertimeProcess(K key,
            long delay0) {
        return refreshDelayAndChangeOvertimeProcess(key, delay0,
                ((OvertimeTask) taskMap.get(key)).getOvertimeProcess());
    }

    /**
     * 刷新指定key对应value的清除时限,但是原有的超时时间及超时方式将会保留
     * @param key 需要存放映射的key
     * @return 如果不存在key对应的value返回null
     */
    public synchronized V refreshAndHoldOvertimeProcess(K key) {
    	OvertimeTask oldTask = taskMap.get(key);
        return refreshDelayAndChangeOvertimeProcess(key, oldTask.getOldDelay(),
        		oldTask.getOvertimeProcess());
    }

    /**
     * 刷新指定key对应value的清除时限和超时后的处理方式
     * @param key 需要存放映射的key
     * @param delay0 清除时限
     * @param overtimeProcess 超时后处理的任务
     * @return 如果不存在key对应的value返回null
     */
    public synchronized V refreshDelayAndChangeOvertimeProcess(K key,
            long delay0, OvertimeProcess<K, V> overtimeProcess) {
        if (delay0 <= 0)
            throw new IllegalArgumentException("超时时限必须为大于0");
        V result = valueMap.get(key);
        if (result != null)
            refresh0(key, delay0, overtimeProcess);
        return result;
    }

    /**
     * 从容器中移除指定key的映射关系
     * @param key 移除映射关系的key
     * @return 放入key对应的value
     */
    public synchronized V remove(Object key) {
        OvertimeTask task = (OvertimeTask) taskMap.remove(key);
        if (task != null) {
            task.cancel();
        }
        return valueMap.remove(key);
    }

	public void putAll(Map<? extends K, ? extends V> m) {
        // 不直接delegate，而是调用put
        for (Iterator it = m.entrySet().iterator(); it.hasNext(); ) {
            Entry<? extends K, ? extends V> entry = (Entry<? extends K, ? extends V>)it.next();
            this.put(entry.getKey(), entry.getValue());
        }
	}

    /**
     * 清空容器
     */
    public synchronized void clear() {
        valueMap.clear();
        taskMap.clear();
    }

    /**
     * 返回所有key的Set
     * @return 所有key的Set
     */
    public synchronized Set<K> keySet() {
        return valueMap.keySet();
    }

    /**
     * 返回所有value的Collection
     * @return 所有value的Collection
     */
    public synchronized Collection<V> values() {
        return valueMap.values();
    }

    /**
     * 返回所有映射关系的Set视图
     * @return valueMap.entrySet
     */
    public synchronized Set<Entry<K, V>> entrySet() {
        return valueMap.entrySet();
    }

    /**
     * 返回容器的大小
     * @return 容器的大小
     */
    public synchronized int size() {
        return valueMap.size();
    }

    /**
     * 返回 <tt>true</tt> 如果容器为空
     * @return <tt>true</tt> 如果容器为空
     */
    public synchronized boolean isEmpty() {
        return valueMap.isEmpty();
    }

    /**
     * 返回 <tt>true</tt> 如果容器包含指定key的映射
     * @param key 需要查找的key
     * @return <tt>true</tt> 如果容器包含指定key的映射
     */
    public synchronized boolean containsKey(Object key) {
        return valueMap.containsKey(key);
    }

    /**
     * 返回 <tt>true</tt> 如果容器包含指定value的映射
     * @param value 需要查找的value
     * @return <tt>true</tt> 如果容器包含指定value的映射
     */
    public synchronized boolean containsValue(Object value) {
        return valueMap.containsValue(value);
    }

    /**
     * 
     * @see java.util.Map#toString()
     */
    public synchronized String toString() {
        return valueMap.toString();
    }

    /**
     * 超时处理接口。
     * 
     * @author PeiLijie
     *
     */
    public static interface OvertimeProcess<K, V> {
    	public abstract void postRemove(TimeLimitedMap<K, V> outerMap, K key, V value, long oldDelay);
    }
    
    /**
     * taskMap 中存放的任务对象定义 
     *
     */
    private class OvertimeTask extends TimerTask {
    	private K key;
    	private long oldDelay;
        private OvertimeProcess<K, V> overtimeProcess;

        private OvertimeTask(K key, long delay, OvertimeProcess<K, V> overtimeProcess) {
        	this.key = key;
        	this.oldDelay = delay;
            this.overtimeProcess = overtimeProcess;
        }

        public OvertimeProcess<K, V> getOvertimeProcess() {
			return overtimeProcess;
		}

		public long getOldDelay() {
			return oldDelay;
		}

		public void run() {
            try {
				if (log.isDebugEnabled()) {
					log.debug("remove Entry on timeout, key: " + key
							+ ", oldDelay: " + oldDelay);
				}
				V value = remove(key);
				if (overtimeProcess != null) {
					overtimeProcess.postRemove(TimeLimitedMap.this, key, value,
							oldDelay);
				}
			} catch (Throwable t) {
					log.error("exception happened when running task!", t);
			}
        }
    }

    /* 
     * 刷新已经在Map中的对象，如果不存在要刷新的对象返回null
     */
    private synchronized void refresh0(final K key, long delay0,
            final OvertimeProcess<K, V> overtimeProcessTask) {
        if (delay0 <= 0)
            throw new IllegalArgumentException("超时时限必须为大于0");

        OvertimeTask oldTask = (OvertimeTask) taskMap.get(key);

        if (null != oldTask)
            oldTask.cancel();

        OvertimeTask task = new OvertimeTask(key, delay0, overtimeProcessTask);
        taskMap.put(key, task);
        timer.schedule(task, delay0);
    }
    
    public static void main(String[] args) throws InterruptedException {
		
    	Map<String,Object> timelimteMap = new TimeLimitedMap<String, Object>(1000);
    	timelimteMap.put("123", "123");
    	Thread.currentThread().sleep(10000);
	}
}
