package com.snail.fitment.protocol.setup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.snail.fitment.common.collection.CollectionTools;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.namespace.URL;
import com.snail.fitment.common.profile.ProfileStatHolder;
import com.snail.fitment.common.timer.TimerManager;
import com.snail.fitment.common.utils.ServiceLocator;
import com.snail.fitment.protocol.connection.DefaultConnectionManager;
import com.snail.fitment.protocol.handler.HttpProxyHandler;
import com.snail.fitment.protocol.netty.BluePrintPackageHandler;
import com.snail.fitment.protocol.netty.BluePrintPipeLineFactory;
import com.sun.net.httpserver.HttpServer;

public class StartNioServer {
	private static final Logger log = LoggerFactory.getLogger(StartNioServer.class);

	// private static final int port = 62722;
	private static int flashPort = 8430;
	private static final int transferPort = 3334;
	private static final int webPort = 8088;

	static {
		// 非root用户启动，flash端口改为8430;
		String exeUser = System.getProperty("user.name");
		if (!StringUtils.isEmpty(exeUser) && !exeUser.endsWith("root")) {
			flashPort = 8430;
			log.warn("当前使用非root用户启动，请在系统中将843端口的请求转发到8430端口");
		} else {
			log.warn("当前使用root用户启动,会给系统带来风险，请尽量使用非root用户启动");
		}
	}

	private static final Timer timer = TimerManager.getTimer("ServerInfo Timer");

	// 为防止 3333 端口在 wifi 下被拦截，增加监听其他端口
	// private static final Integer[] portList = new Integer[] {port, flashPort,
	// transferPort, 8080, 62716};
	private static final Integer[] portList = new Integer[] { FitmentConfig.BP_PORT, 62723 };

	/**
	 * 入口函数
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		try {
			long start = System.currentTimeMillis();

			ApplicationContext context = initSpringContext();
			addShutdownHook(context);

			// 初始化tcp
			initTcpServer();
			if (FitmentConfig.endProxy) {
				initWebProxy();
			}
			if (log.isInfoEnabled()) {
				log.info("BluePrint Server startup in " + (System.currentTimeMillis() - start)
						+ " ms. listening ports: " + CollectionTools.toString(portList));
			}

			for (;;) {
				mainLoop(false);
			}

		} catch (Throwable e) {
			log.error("main - exception happened, exit server !", e);
			System.exit(-1);
		}
	}

	protected static void mainLoop(boolean standalone) {
		try {
			TimeUnit.MINUTES.sleep(1);
			BluePrintPackageHandler bpPackageHandler = (BluePrintPackageHandler) ServiceLocator
					.getBean("fitmentPackageHandler");
			DefaultConnectionManager defaultConnectionManager = (DefaultConnectionManager) ServiceLocator
					.getBean("defaultConnectionManager");

			log.info("connections count: " + defaultConnectionManager.size() + ", cached responses count: "
					+ defaultConnectionManager.getResponseCacheSize() + ", buzy responses count: "
					+ bpPackageHandler.getAndResetBuzyResponseCount() + ", first queue size: "
					+ bpPackageHandler.getFirstExecutor().getQueue().size() + ", normal queue size: "
					+ bpPackageHandler.getNormalExecutor().getQueue().size());

			ProfileStatHolder.printPerformanceLog("Op-");

		} catch (Throwable e) {
			log.error("main - exception happened in for loop !", e);
		}
	}

	protected static ApplicationContext initStandaloneSpringContext() {
		// Configure the server.
		String[] config = new String[] { "/applicationContext-gdp-main.xml", "/application-dubbo-consumer.xml" };

		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
		context.registerShutdownHook();// 非web应用的环境下使用Spring的IoC容器，让容器优雅的关闭，并调用singleton
										// bean上的相应析构回调方法 @PreDestroy
		context.start();
		return context;
	}

	protected static ApplicationContext initSpringContext() {
		List<String> config = new ArrayList<String>();
		config.add("/applicationContext.xml");

		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				config.toArray(new String[0]));
		context.registerShutdownHook();// 非web应用的环境下使用Spring的IoC容器，让容器优雅的关闭，并调用singleton
										// bean上的相应析构回调方法 @PreDestroy
		context.start();
		return context;
	}

	protected static void addShutdownHook(final ApplicationContext context) {
		Runtime.getRuntime().addShutdownHook(new Thread("Stop Servers") {
			public void run() {
				// 将在线的 SessionInfo 改为离线状态

				timer.cancel();
				if (log.isInfoEnabled()) {
					log.info("BluePrint Server stop on exit");

				}

				// 优雅关闭dubbo
				ProtocolConfig.destroyAll();
				if (httpProxy != null) {
					httpProxy.stop(0);
				}
			}
		});
	}

	protected static void initTcpServer() {
		ChannelPipelineFactory bpPipeLineFactory = new BluePrintPipeLineFactory();
		ServerBootstrap tcpBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		tcpBootstrap.setPipelineFactory(bpPipeLineFactory);
		tcpBootstrap.setOption("child.tcpNoDelay", true);

		// Bind and start to accept incoming connections.
		for (int p : portList) {
			tcpBootstrap.bind(new InetSocketAddress(p));
		}
	}

	static HttpServer httpProxy;

	public static void initWebProxy() throws IOException {
		URL url = URL.valueOf(FitmentConfig.proxyUrl);
		httpProxy = HttpServer.create(new InetSocketAddress(url.getHost(), url.getPort(8888)), 0);// 设置HttpServer的端口为8888
		HttpProxyHandler httpProxyHandler = (HttpProxyHandler) ServiceLocator.getBean("httpProxyHandler");
		httpProxy.createContext("/" + url.getPath(), httpProxyHandler);// 用httpProxyHandler类内处理到/的请求
		httpProxy.setExecutor(Executors.newCachedThreadPool()); // creates a
																// default
																// executor
		httpProxy.start();
	}

	public static void initWebServer() {
		// 配置服务器-使用java线程池作为解释线程
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		// 设置 pipeline factory.
		ChannelPipeline pipeline = bootstrap.getPipeline();

		// 连接管理
		pipeline.addLast("webconnectionhandler", (ChannelHandler) ServiceLocator.getBean("connectionManagerHandler"));

		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());

		// 设置块的最大字节数
		pipeline.addLast("aggregator", new HttpChunkAggregator(10 * 1024 * 1024));

		// http处理handler
		pipeline.addLast("webhandler", (ChannelHandler) ServiceLocator.getBean("webServerHandler"));

		// 绑定端口
		bootstrap.bind(new InetSocketAddress(webPort));
	}
}
