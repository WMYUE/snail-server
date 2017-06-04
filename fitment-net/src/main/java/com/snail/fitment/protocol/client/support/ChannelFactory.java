package com.snail.fitment.protocol.client.support;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.utils.ServiceLocator;
import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.client.IChannel;
import com.snail.fitment.protocol.client.IChannelFactory;
import com.snail.fitment.protocol.client.IChannelListener;

@Lazy(false)
@Component("channelFactory")
public class ChannelFactory implements IChannelFactory {
	private static final Logger log = Logger.getLogger(ChannelFactory.class);
	
	private ClientBootstrap bootstrap;
	private Map<Integer, IChannel> channelMap = new ConcurrentHashMap<Integer, IChannel>();

	@Override
	public IChannel build(IChannelListener listener,String domain) {
		BpChannel channel = new BpChannel(this, listener,domain);
		return channel;
	}

	@Override
	public ChannelFuture connect(InetSocketAddress address) {
		return bootstrap.connect(address);
	}

	@PostConstruct
	public void initBootStrap() {
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));
		BpClientPipeLineFactory bpPipeLineFactory = new BpClientPipeLineFactory();
		bootstrap.setPipelineFactory(bpPipeLineFactory);
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("connectTimeoutMillis", 25000);
		bootstrap.setOption("sendBufferSize", 1048576);
		bootstrap.setOption("receiveBufferSize", 1048576);
	}

	@Override
	public void registChannel(IChannel bpChannel, Channel nettyChannel) {
		channelMap.put(nettyChannel.getId(), bpChannel);
	}

	@Override
	public void unregistChannel(IChannel bpChannel, Channel nettyChannel) {
		channelMap.remove(nettyChannel.getId());
	}

	private void onMessage(Channel channel, IBpPackage message) {
		IChannel bpChannel = channelMap.get(channel.getId());
		if (bpChannel != null) {
			bpChannel.onReceivePackage(message);
		}
	}

	private void onReceiveProgress(Channel channel, short seq, double progress) {
		IChannel bpChannel = channelMap.get(channel.getId());
		if (bpChannel != null) {
			bpChannel.onReceiveProgress(seq, progress);
		}
	}

	private void onChannelDisconected(Channel channel) {
		IChannel bpChannel = channelMap.remove(channel.getId());
		if (bpChannel != null) {
			bpChannel.onDisconnected();
		}
	}

	private class BpClientPipeLineFactory implements ChannelPipelineFactory {
		public ChannelPipeline getPipeline() throws Exception {

			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("decoder",(ChannelHandler)ServiceLocator.getBean("decodeHandler"));
			pipeline.addLast("encoder",(ChannelHandler)ServiceLocator.getBean("encoderHandler"));
			
			pipeline.addLast("onmsg", new SimpleChannelUpstreamHandler() {
				@Override
				public void messageReceived(ChannelHandlerContext ctx,
						MessageEvent e) throws Exception {
					Object message = e.getMessage();
					if (message instanceof IBpPackage) {
                    	log.debug("received:" + message);
                        onMessage(ctx.getChannel(), (IBpPackage) message);
                    } else {
                    	log.debug( "not a bpPkg:" + message);
                    }
				}

				@Override
				public void channelDisconnected(ChannelHandlerContext ctx,
						ChannelStateEvent e) throws Exception {
					ctx.sendUpstream(e);
					onChannelDisconected(ctx.getChannel());
				}
				@Override
				public void exceptionCaught(ChannelHandlerContext ctx,
						ExceptionEvent e) throws Exception {
					log.error(ctx.getName(),e.getCause());
				}
			});
			return pipeline;
		}
	}
}
