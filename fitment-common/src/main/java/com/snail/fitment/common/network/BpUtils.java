package com.snail.fitment.common.network;

import org.jboss.netty.channel.Channel;

public class BpUtils {
	public static String getRemoteAddress(Channel channel) {
		return channel == null || channel.getRemoteAddress() == null ? null : channel.getRemoteAddress().toString();
	}

	public static String getLocalAddress(Channel channel) {
		return channel == null || channel.getLocalAddress() == null ? null : channel.getLocalAddress().toString();
	}
}
