package com.snail.fitment.register.zookeeper.manager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;

import com.snail.fitment.common.network.NetUtils;
import com.snail.fitment.register.zookeeper.support.ChildListener;

public class AbstractZookeeperManager implements Watcher, IZookeeperManager {
	protected static final Logger LOGGER = Logger.getLogger(AbstractZookeeperManager.class);

	// 会话超时，10 秒会话时间 ，避免频繁的session expired
	private static final int SESSION_TIMEOUT = 10000;
	// 连接超时，3秒
	private static final int CONNECT_TIMEOUT = 3000;
    // 最大重试次数
    public static final int MAX_RETRIES = 10;
    // 每次重试超时时间
    public static final int RETRY_PERIOD_SECONDS = 2;
    
	//Zk对象
	protected ZooKeeper zk;
	
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	
	private static String internalHost = "";
	
	// 是否调试状态
	private boolean debug = false;
	
    private static final Charset CHARSET = Charset.forName("UTF-8");


	@Override
	public void connect(String hosts, String defaultPrefixString, boolean debug)
			throws Exception {
	   internalHost = hosts;
       
	   connect(hosts);
	 
       LOGGER.info("zoo prefix: " + defaultPrefixString);

       // 新建父目录
       makeDir(defaultPrefixString, NetUtils.getHostIp());
	}

	/**
     * @param hosts
     *
     * @return void
     *
     * @throws IOException
     * @throws InterruptedException
     * @Description: 连接ZK
     */
    public void connect(String hosts) throws IOException, InterruptedException {
        internalHost = hosts;
        zk = new ZooKeeper(internalHost, SESSION_TIMEOUT, this);

        // 连接有超时哦
        connectedSignal.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

        LOGGER.info("zookeeper: " + hosts + " , connected.");
    }
    
	@Override
	 /**
     * 含有重试机制的retry，加锁, 一直尝试连接，直至成功
     */
    public synchronized void reconnect() {

        LOGGER.info("start to reconnect....");

        int retries = 0;
        while (true) {

            try {

                if (!zk.getState().equals(States.CLOSED)) {
                    break;
                }

                LOGGER.warn("zookeeper lost connection, reconnect");

                close();

                connect(internalHost);

            } catch (Exception e) {

                LOGGER.error(retries + "\t" + e.toString());

                // sleep then retry
                try {
                    LOGGER.warn("sleep " + RETRY_PERIOD_SECONDS);
                    TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
                } catch (InterruptedException e1) {
                }
            }
        }
    }
	

	@Override
	public String createNode(String path, String value, CreateMode createMode)
			throws Exception {
		 int retries = 0;
	        while (true) {

	            try {

	                Stat stat = zk.exists(path, false);

	                if (stat == null) {
	                    return zk.create(path, value.getBytes(CHARSET), Ids.OPEN_ACL_UNSAFE, createMode);
	                } else {

	                    if (value != null) {
	                        zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
	                    }
	                }

	                return path;

	            } catch (KeeperException.SessionExpiredException e) {

	                throw e;

	            } catch (KeeperException e) {

	                LOGGER.warn("createEphemeralNode connect lost... will retry " + retries + "\t" + e.toString());

	                if (retries++ == MAX_RETRIES) {
	                    throw e;
	                }
	                // sleep then retry
	                int sec = RETRY_PERIOD_SECONDS * retries;
	                LOGGER.warn("sleep " + sec);
	                TimeUnit.SECONDS.sleep(sec);
	            }
	        }
	}

	@Override
	public void deleteNode(String path) {
		try {

            zk.delete(path, -1);

        } catch (KeeperException.NoNodeException e) {

            LOGGER.error("cannot delete path: " + path, e);

        } catch (InterruptedException e) {

            LOGGER.warn(e.toString());

        } catch (KeeperException e) {

            LOGGER.error("cannot delete path: " + path, e);
        }
	}

	@Override
	public String readUrl(String url, Watcher watcher) throws Exception {
		return read(url, watcher, null);
	}

	@Override
	public String read(String path, Watcher watcher, Stat stat)
			throws InterruptedException, KeeperException {
		byte[] data = zk.getData(path, watcher, stat);
        return new String(data, CHARSET);
	}

	@Override
	public void makeDir(String dir, String data) {
		 try {

	            boolean deafult_path_exist = exists(dir);
	            if (!deafult_path_exist) {
	                LOGGER.info("create: " + dir);
	                this.writePersistentUrl(dir, data);
	            } else {
	            }

	        } catch (KeeperException e) {

	            LOGGER.error("cannot create path: " + dir, e);

	        } catch (Exception e) {

	            LOGGER.error("cannot create path: " + dir, e);
	        }
	}

	
	/**
     * @param path
     * @param value
     *
     * @return void
     *
     * @throws InterruptedException
     * @throws KeeperException
     * @Description: 写PATH数据, 是持久化的
     */
    public void write(String path, String value) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, false);

                if (stat == null) {

                    zk.create(path, value.getBytes(CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

                } else {

                    zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                }

                break;

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {

                LOGGER.warn("write connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }
    
	@Override
	public void writePersistentUrl(String url, String value) throws Exception {
		write(url, value);
	}

	@Override
	public List<String> getRootChildren() {
	 List<String> children = new ArrayList<String>();
        try {
            children = zk.getChildren("/", false);
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }

        return children;
	}

	@Override
	public List<String> getChildren(String path) {
		List<String> children = new ArrayList<String>();
        try {
            children = zk.getChildren(path, false);
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }

        return children;
	}
	
	

	@Override
	public List<String> getChildren(String path, Watcher watcher) {
		List<String> children = new ArrayList<String>();
        try {
            children = zk.getChildren(path, watcher);
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
        return children;
	}

	@Override
	public List<String> addChildListener(String path, ChildListener listener) {
		return null;
	}

	@Override
	public void removeChildListener(String path, ChildListener listener) {
		
	}

	@Override
	public boolean exists(String path) throws Exception {
		   int retries = 0;
	        while (true) {

	            try {

	                Stat stat = zk.exists(path, false);

	                if (stat == null) {

	                    return false;

	                } else {

	                    return true;
	                }

	            } catch (KeeperException.SessionExpiredException e) {

	                throw e;

	            } catch (KeeperException e) {

	                LOGGER.warn("exists connect lost... will retry " + retries + "\t" + e.toString());

	                if (retries++ == MAX_RETRIES) {
	                    LOGGER.error("connect final failed");
	                    throw e;
	                }

	                // sleep then retry
	                int sec = RETRY_PERIOD_SECONDS * retries;
	                LOGGER.warn("sleep " + sec);
	                TimeUnit.SECONDS.sleep(sec);
	            }
	        }
	}

	@Override
	public void close() throws InterruptedException {
		zk.close();
	}

	@Override
	public boolean isConnected() {
		return zk.getState() == ZooKeeper.States.CONNECTED;
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {

            LOGGER.info("zk SyncConnected");
            connectedSignal.countDown();

        } else if (event.getState().equals(KeeperState.Disconnected)) {

            // 这时收到断开连接的消息，这里其实无能为力，因为这时已经和ZK断开连接了，只能等ZK再次开启了
            LOGGER.warn("zk Disconnected");

        } else if (event.getState().equals(KeeperState.Expired)) {

            if (!debug) {

                // 这时收到这个信息，表示，ZK已经重新连接上了，但是会话丢失了，这时需要重新建立会话。
                LOGGER.error("zk Expired");

                // just reconnect forever
                reconnect();
            } else {
                LOGGER.info("zk Expired");
            }

        } else if (event.getState().equals(KeeperState.AuthFailed)) {

            LOGGER.error("zk AuthFailed");
        }
		
	}

}
