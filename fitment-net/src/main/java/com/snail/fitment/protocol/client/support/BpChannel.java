package com.snail.fitment.protocol.client.support;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelFutureProgressListener;

import com.snail.fitment.protocol.bpackage.HeartBeat;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.client.IChannel;
import com.snail.fitment.protocol.client.IChannelFactory;
import com.snail.fitment.protocol.client.IChannelListener;
import com.snail.fitment.protocol.client.IChannelListener.State;
import com.snail.fitment.protocol.client.IProtocolListener;
import com.snail.fitment.protocol.client.ISendTask;

public class BpChannel implements IChannel,ChannelFutureListener {
	private static final Logger log = Logger.getLogger(BpChannel.class);
    private static class SendingTask implements Runnable, ISendTask, ChannelFutureProgressListener {
		private IBpPackage request;
	    private int[] intervals;
	    private IProtocolListener listener;
	    private int sentTimes;
	    private BpChannel channel;

	    private volatile boolean finish;
	    public SendingTask(BpChannel channel,IBpPackage request, int[] intervals, IProtocolListener listener) {
	        this.request = request;
	        this.intervals = intervals;
	        this.listener= listener;
	        this.channel= channel;
	    }
	    private void onError(int stateCode, String descr) {
			if (!this.finish) {//尽量防止重复响应
				this.finish = true;
				if (this.listener != null) {
					listener.onError(this.request, stateCode, descr);
				}
			}
	    }
	    private void onResponse(IBpPackage response) {
			if (!this.finish) {//尽量防止重复响应
				this.finish = true;
				if (this.listener != null) {
					listener.onResponse(this.request, response);
				}
			}
	    }
	    private void onReceiveProgress(double progress) {
	    	if(this.listener != null) {
	    		listener.onProcess(this.request, progress);
	    	}
	    }
	    public void run() {
			try {// 如果已经发送的次数小于期望的最大发送次数，则发送请求，并安排下一次执行
				if(this.finish) return;
				if (this.sentTimes < this.intervals.length) {
					BpChannel.sendPackageExecutor.schedule(this,
							this.intervals[this.sentTimes],
							TimeUnit.SECONDS);
					ChannelFuture future = channel.write(request);
					log.debug("send:" + request);
					future.addListener(this);
					this.sentTimes++;

				} else { // 最后一次执行，清除缓存的请求消息，并且报告超时异常
					SendingTask task = channel.removeSendingTask(request.getBpHeader().getSeq());
					if(task != null) {
						task.onError(-12, "网络超时");
						if(request.equals(HeartBeat.Instance)) {
							channel.onTimeOut();
						} else {
							channel.checkChannelBlock();
						}
					}
				}

			} catch (Throwable e) {
	            // 一般应catch it, 防止timer线程退出，但这是一种程序错误，还是应抛出
	            // 特别是为了让测试程序能正常运行
	            try {
	                log.error("SendMessageTask", e);
	                channel.removeSendingTask(request.getBpHeader().getSeq());
	                this.onError(-5, e.getMessage());
	            } catch (Throwable t) {
	            }
	        }
	    }

		@Override
		public void cancel() {
			this.finish= true;
			channel.removeSendingTask(request.getBpHeader().getSeq());
		}
		@Override
		public void operationProgressed(ChannelFuture arg0, long amount,
				long current, long total) throws Exception {
			if(listener != null) {
				listener.onProcess(this.request, (double)(current*1.0 /total));
			}
			channel.onHeartBeatResponse(HeartBeat.Instance);
		}
		@Override
		public void operationComplete(ChannelFuture arg0) throws Exception {
            // 这部分一定不能打开。有响应请求回来。虽然以为着网络可能没问题。
            // 但是此时网络可能很差。丢包严重。
            // 如果给响应，就会使“心跳包收不到加速死亡”(断线)的功能失效。
			// channel.onHeartBeatResponse(IBpPackage.heartBeartPkg);
		}
	}


	/////////////////////////BpChannel

	private static final short[] SEQ_LOCKER = new short[0];

	private static final int[] timeOutIntervals ={45};
	private volatile State state = State.ChannelIni;
	private volatile Channel channel;
	private final IChannelListener listener;
	private short seq;

	private final IChannelFactory channelManager;

	private InetSocketAddress address = null;
	private String domain;
	
	private final Map<Integer,SendingTask> sendingTasks = new ConcurrentHashMap<>();
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "sendPackage #" + mCount.getAndIncrement());
		}
	};
	private static final ScheduledExecutorService sendPackageExecutor = Executors.newScheduledThreadPool(2,sThreadFactory);
	private static final ThreadFactory rThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "receivePackage #" + mCount.getAndIncrement());
		}
	};
	private static final ScheduledExecutorService receivePackageExecutor = Executors.newScheduledThreadPool(2,rThreadFactory);

	public BpChannel(IChannelFactory channelManager, IChannelListener listener,String domain) {
		this.channelManager= channelManager;
		this.listener = listener;
		this.domain = domain;
	}


    @Override
    public synchronized void connect(InetSocketAddress address) {
        int stateCode = 0;
        String descr = "";
		if (this.state == State.ChannelIni) {
			this.state = State.ChannelConnecting;
			this.address = address;

			log.debug("connect:" + address);
			ChannelFuture future = channelManager.connect(address);
			//future.addListener(this);
			future.awaitUninterruptibly();
			this.onOperationComplete(future);
		}
		listener.onChannelStateChanged(this, stateCode, descr, this.state);
    }



	@Override
	public IBpPackage sendPackageSync(IBpPackage request, int[] intervals) {
		BlockingListener blockListener = new BlockingListener();
		 synchronized (blockListener) {
			 sendPackage(request,blockListener,intervals);
			 if (blockListener.getReply() != null) {
                 return blockListener.getReply();
              }
              int waiting =10;
              if(intervals != null) {
                  for(int i=0;i<intervals.length;i++) {
                      waiting =waiting+intervals[i];
                  }
              }
              try {
            	  blockListener.wait(waiting*1000);
			} catch (InterruptedException e) {}
            return blockListener.getReply();
		 }
	}
	
	@Override
	public ISendTask sendPackage(IBpPackage request, IProtocolListener listener,
			int[] intervals) {
		if(intervals == null || intervals.length<=0) {
			intervals = timeOutIntervals;
		}
		if(this.state != State.ChannelConnected) {
			if(listener != null){
				listener.onError(request, -4, "未连接");
			}
			return null;
		}

		if(!request.equals(HeartBeat.Instance)) {
			request.getBpHeader().setSeq(this.getSeq());
		}
		SendingTask task = new SendingTask(this,request,intervals,listener);
		synchronized(this.sendingTasks) {
			SendingTask oldTask =this.sendingTasks.put(request.getBpHeader().getSeq(), task);
			if(oldTask != null) {
				oldTask.cancel();
			}
			sendPackageExecutor.schedule(task, 0, TimeUnit.SECONDS);
			return task;
		}
	}

	@Override
	public synchronized void onDisconnected() {
		log.debug("disconnect:" + address);
		this.closeChannelWithErr(-4, "netty channel disconnect");
	}

	@Override
	public void onReceiveProgress(short seq, double progress) {
		SendingTask task = this.sendingTasks.get(seq);
		if(task != null) {
			task.onReceiveProgress(progress);
		}
	}

	private void onHeartBeatResponse(final IBpPackage IBpPackage) {
		SendingTask task = this.removeSendingTask(IBpPackage.getBpHeader().getSeq());
		if(task != null) {
			task.onResponse(IBpPackage);
		}
	}

	@Override
	public void onReceivePackage(final IBpPackage bpPackage) {
		log.debug("received:"+bpPackage);
		if(bpPackage.equals(HeartBeat.Instance)) {
			onHeartBeatResponse(bpPackage);
		} else if (bpPackage.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST) {// request 采用excutor 尽量防止阻塞挂死
			receivePackageExecutor.execute(new Runnable() {
				public void run() {
					try {
						listener.onReceive(BpChannel.this, bpPackage);
					} catch (Throwable t) {
						log.error( "MessageManager", t);
					}
				}
			});
		} else {// is response
			SendingTask task = this.removeSendingTask(bpPackage.getBpHeader().getSeq());
			if(task != null) {
				task.onResponse(bpPackage);
			} else {
				log.warn("no match request for:"+bpPackage);
			}
		}
	}

	@Override
	public synchronized void close() {
		this.closeChannelWithErr(0, "");
	}

	private synchronized void onOperationComplete(final ChannelFuture future){
		if (future.isSuccess()) {
			this.channel = future.getChannel();
			this.state= State.ChannelConnected;
			this.channelManager.registChannel(this, this.channel);
			listener.onChannelStateChanged(this, 0, "", this.state);
		} else {
			this.state = State.ChannelConnectFail;
			listener.onChannelStateChanged(this, -2, "网络异常", this.state);
		}
	}

	@Override
	public void operationComplete(final ChannelFuture future) {
//		receivePackageExecutor.execute(new Runnable() {
//			public void run() {
//				try {
					onOperationComplete(future);
//				} catch (Throwable t) {
//					log.error("MessageManager", t);
//				}
//			}
//		});
	}

	private void schedualCheckChannelBlock(int next) {//如果阻塞发心跳加速死亡
		synchronized(this.sendingTasks) {
			SendingTask oldTask =this.sendingTasks.get(0);
			if(oldTask != null) {
				return;
			} else {
				SendingTask task = new SendingTask(this,HeartBeat.Instance,new int[]{3,2},null);
				this.sendingTasks.put(0,task);
				sendPackageExecutor.schedule(task, next, TimeUnit.SECONDS);
			}
		}
	}
	private void checkChannelBlock() {
		schedualCheckChannelBlock(0);
	}
	private synchronized void onTimeOut() {
		this.closeChannelWithErr(-12, "网络阻塞");
	}

	private short getSeq() {
		synchronized (SEQ_LOCKER) {
			if(seq ==0) seq++;
			return seq++;
		}
	}

	private void closeChannelWithErr(int stateCode, String stateDescr) {
		this.state= State.ChannelClosed;
		this.listener.onChannelStateChanged(this, stateCode, stateDescr, this.state);
		this.channelManager.unregistChannel(this, this.channel);
		this.cancelAllSendingTask();
		this.channel.disconnect();//need?
	}

	

	private void cancelAllSendingTask() {
		synchronized(this.sendingTasks) {
			for(SendingTask task:this.sendingTasks.values()) {
				task.onError(-6, "");//"cancel");
			}
			this.sendingTasks.clear();
		}
	}

	private SendingTask removeSendingTask(Integer key) {
		synchronized(this.sendingTasks) {
			return this.sendingTasks.remove(key);
		}
	}

	private ChannelFuture write(IBpPackage request) {
		return this.channel.write(request);
	}
	@Override
	public State state() {
		return this.state;
	}
	@Override
	public String domain() {
		return this.domain;
	}
	@Override
	public InetSocketAddress connectAddress() {
		return this.address;
	}
	@Override
	public int getSendingTaskCount() {
		return this.sendingTasks.size();
	}

}
