package com.snail.fitment.register.zookeeper.manager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import com.snail.fitment.common.network.NetUtils;
import com.snail.fitment.register.zookeeper.support.ChildChangedWatcher;
import com.snail.fitment.register.zookeeper.support.ChildListener;

/**
 * ZK统一管理器
 *
 */
public class BluePrintZookeeperManager<TargetChildListener> extends AbstractZookeeperManager {
	protected static final Logger LOGGER = Logger.getLogger(BluePrintZookeeperManager.class);
   
    private final ConcurrentMap<String, ConcurrentMap<ChildListener, TargetChildListener>> childListeners = new ConcurrentHashMap<String, ConcurrentMap<ChildListener, TargetChildListener>>();
    
	@Override
	public void connect(String hosts, String defaultPrefixString, boolean debug)
			throws Exception {

        try {

            initInternal(hosts, defaultPrefixString, debug);

            LOGGER.debug("ZookeeperMgr init.");

        } catch (Exception e) {

            throw new Exception("zookeeper init failed. ", e);
        }
	}
	
	
	
    @Override
	public String createNode(String path, String value, CreateMode createMode)
			throws Exception {
		
		int i = path.lastIndexOf('/');
		if (i > 0) {
			this.createNode(path.substring(0, i), value, createMode);
		}
			
		super.createNode(path, value, createMode);
	
		return "";
	}

	private void initInternal(String hosts, String defaultPrefixString, boolean debug)
            throws IOException, InterruptedException {
    	
        connect(hosts);

        LOGGER.info("zoo prefix: " + defaultPrefixString);

        // 新建父目录
        makeDir(defaultPrefixString, NetUtils.getHostIp());
    }

	
	@Override
	public List<String> addChildListener(String path, ChildListener listener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if (listeners == null) {
			childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener, TargetChildListener>());
			listeners = childListeners.get(path);
		}
		TargetChildListener targetListener = listeners.get(listener);
		if (targetListener == null) {
			ChildChangedWatcher watch = new ChildChangedWatcher(this.zk, path, listener);
			
			listeners.putIfAbsent(listener, (TargetChildListener)watch);
			targetListener = listeners.get(listener);
		}
		
		return getChildren(path, (Watcher)targetListener);
	}


	@Override
	public void removeChildListener(String path, ChildListener listener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if (listeners != null) {
			TargetChildListener targetListener = listeners.remove(listener);
			if (targetListener != null) {
				getChildren(path);
			}
		}
		
	}

}
