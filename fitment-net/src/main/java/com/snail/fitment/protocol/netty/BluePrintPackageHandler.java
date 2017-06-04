package com.snail.fitment.protocol.netty;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.common.thread.BluePrintThreadFactory;
import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.connection.BpConnection;
import com.snail.fitment.protocol.connection.ConnectionManager;
import com.snail.fitment.protocol.handler.DefaultHandlerContext;
import com.snail.fitment.protocol.handler.HandleContext;
import com.snail.fitment.protocol.handler.IHandler;

@Component
public class BluePrintPackageHandler extends SimpleChannelUpstreamHandler {
	private static final Logger log = Logger.getLogger(BluePrintPackageHandler.class);
	private static AtomicInteger buzyResponseCount = new AtomicInteger(0);

	private IHandler baseHandler;

	@Autowired
	private ConnectionManager connectManager;

	/*
	 * 以下为分队列策略
	 */
	private RejectedExecutionHandler discardOldest = new RejectedExecutionHandler() {
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			if (!e.isShutdown()) {
				OperationHandleBusTask task = (OperationHandleBusTask) e.getQueue().poll();
				task.buzyResponse();
				e.execute(r);
			}
		}
	};

	private ThreadPoolExecutor normalExecutor = new ThreadPoolExecutor(FitmentConfig.BP_NORMAL_TASK_POOL_CORE_SIZE,
			FitmentConfig.BP_NORMAL_TASK_POOL_MAX_SIZE, 60, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(FitmentConfig.BP_NORMAL_TASK_QUEUE_SIZE, false),
			new BluePrintThreadFactory("BluePrintNormal #"), discardOldest);

	private ThreadPoolExecutor firstExecutor = new ThreadPoolExecutor(FitmentConfig.BP_FIRST_TASK_POOL_CORE_SIZE,
			FitmentConfig.BP_FIRST_TASK_POOL_MAX_SIZE, 60, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(FitmentConfig.BP_FIRST_TASK_QUEUE_SIZE, false),
			new BluePrintThreadFactory("BluePrintFirst #"), discardOldest);

	public ThreadPoolExecutor getNormalExecutor() {
		return normalExecutor;
	}

	public ThreadPoolExecutor getFirstExecutor() {
		return firstExecutor;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {

		Object message = e.getMessage();
		log.debug("messageReceived, receive new message!");

		if (message instanceof IBpPackage) {
			log.debug("messageReceived, parse new message!");

			log.debug(((IBpPackage) message).getBpHeader().getStackVersion());
			String s1 = new String(((IBpPackage) message).getBpBody().getBodyBuffer(), "UTF-8");
			log.debug("messageReceived, bodyBuffer=" + s1);
			final IBpPackage pkg = (IBpPackage) message;
			final BpConnection conn = connectManager.getBpConnection(e.getChannel().getId());

			// 判断客户端重发的情况
			if (this.canReuseResponse(conn, pkg) && pkg.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST
					&& conn.containsResponseKey(pkg.getBpHeader().getSeq())) {
				// 如果为客户端重发（是请求消息且响应缓存中存在对应 key），不再重复处理
				log.debug("收到客户端重发请求");

				IBpPackage response = conn.getResponse(pkg.getBpHeader().getSeq());
				if (response != null) {
					// 缓存的响应不为空，表示已经处理过，直接发送原有响应
					connectManager.sendBpPackage(conn.getConnectionId(), response);
				} else {
					// 缓存的响应为空，表示正在处理，丢弃重复请求，日志中打印警告
					log.debug("原请求正在处理，丢弃重发请求");
				}
			} else {
				if (pkg.getBpHeader().getOpCode() == 5201) {
					connectManager.sendBpPackage(conn.getConnectionId(), pkg);
				} else {
					// 如果是请求消息，先缓存空的响应（标记已经收到过请求，正在处理）
					if (this.canReuseResponse(conn, pkg)
							&& pkg.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST) {
						conn.putResponse(pkg.getBpHeader().getSeq(), null);
					}

					// 使用 baseHandler -> operation 处理（另启线程处理，区分任务优先级）
					OperationHandleBusTask buzTask = new OperationHandleBusTask(conn, pkg, baseHandler);
					if (this.shouldExecuteInFirst(pkg)) {
						this.firstExecutor.execute(buzTask);
					} else {
						this.normalExecutor.execute(buzTask);
					}
				}
			}
		} else {
			ctx.sendUpstream(e);
		}
	}

	private boolean shouldExecuteInFirst(IBpPackage pkg) {// login with token or
															// heartbeat
		return pkg.getBpHeader().getOpCode() == 1013;// oauth
	}

	/**
	 * 判断是否支持重用响应（对客户端重发相同 seq 请求时，重用原先的响应）
	 * 
	 * @param conn
	 * @return
	 */
	private boolean canReuseResponse(BpConnection conn, IBpPackage pkg) {
		// 若 seq 不为 0 ，则允许重用，否则不重用（iPhone 客户端已经修改为判断超时）
		return pkg.getBpHeader().getSeq() != 0;
	}

	public void setBaseHandler(IHandler baseHandler) {
		this.baseHandler = baseHandler;
	}

	private class OperationHandleBusTask implements Runnable {
		private IBpPackage pkg;
		private BpConnection connection;
		private IHandler handler;

		public OperationHandleBusTask(final BpConnection connection, final IBpPackage srcPkg, final IHandler handler) {
			this.pkg = srcPkg;
			this.connection = connection;
			this.handler = handler;
		}

		public void buzyResponse() {// 繁忙时候响应
			IBpPackage response = BpPackageFactory.createEmptyResponseByRequest(pkg);
			response.getBpBody().setBodyString(getBuzyResponseString());
			connection.sendResponse(response);
			log.warn("buzyResponse:" + this.detailInfo());
		}

		@Override
		public void run() {
			HandleContext context = new DefaultHandlerContext(pkg, connection.getConnectionId(),
					connection.getClientAddress());
			handler.handle(context);
			connectManager.sendBpPackage(connection.getConnectionId(), context.getResponse());
		}

		public String detailInfo() {
			return "request:" + pkg + ",connection:" + connection;
		}

		private String buzyResponseString = null;

		private final String getBuzyResponseString() {
			if (buzyResponseString == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("stateCode", 505);
				paramMap.put("stateDesc", "system busy");
				buzyResponseString = JsonConvert.SerializeObject(paramMap);
			}
			buzyResponseCount.incrementAndGet();
			return buzyResponseString;
		}
	}

	public static int getAndResetBuzyResponseCount() {
		return buzyResponseCount.getAndSet(0);
	}
}
