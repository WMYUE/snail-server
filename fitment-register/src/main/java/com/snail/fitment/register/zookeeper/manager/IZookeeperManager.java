package com.snail.fitment.register.zookeeper.manager;


import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import com.snail.fitment.register.zookeeper.support.ChildListener;

public interface IZookeeperManager {
	
	public void connect(String hosts, String defaultPrefixString, boolean debug) throws Exception;
	
	public void reconnect();
	
	public String createNode(String path, String value, CreateMode createMode) throws Exception;
	
	public void deleteNode(String path);
	
	public String readUrl(String url, Watcher watcher) throws Exception;
	
	public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException;
	
	public void makeDir(String dir, String data);
	
	public void writePersistentUrl(String url, String value) throws Exception;
	
	public List<String> getRootChildren();
	
	public List<String> getChildren(String path);
	
	public List<String> getChildren(String path, Watcher watcher);

	List<String> addChildListener(String path, ChildListener listener);

	void removeChildListener(String path, ChildListener listener);
	
	public boolean exists(String path) throws Exception;
	
	public void close() throws InterruptedException;
	
	public boolean isConnected();
}
