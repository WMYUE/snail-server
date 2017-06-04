package com.snail.fitment.protocol.client;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

public interface IChannelFactory {
	public IChannel build(IChannelListener listener,String domain);
	
	public ChannelFuture connect(InetSocketAddress address);
	public void registChannel(IChannel gdChannel, Channel nettyChannel);
	public void unregistChannel(IChannel gdChannel, Channel nettyChannel);
}
