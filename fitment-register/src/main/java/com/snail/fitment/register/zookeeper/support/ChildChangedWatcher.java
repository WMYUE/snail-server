package com.snail.fitment.register.zookeeper.support;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class ChildChangedWatcher implements Watcher{
	//Zk对象
	protected ZooKeeper zk;
	
	protected String path;
	
	protected ChildListener listener;
	
	public ChildChangedWatcher(ZooKeeper zk, String path, ChildListener listener){
		this.zk = zk;
		this.path = path;
		this.listener = listener;
	}
	
	@Override
	public void process(WatchedEvent event) {
		if(event.getType() == EventType.NodeChildrenChanged){
			try {
				List<String>  childUrls = zk.getChildren(path, true);
				listener.childChanged(path, childUrls);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{ // 继续保持Watcher
			try {
				zk.getChildren(path, true);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
