package com.snail.fitment.common.network;

import org.jboss.netty.channel.Channel;

public class AddressUtils {
	
	public static String getRemoteAddress(Channel channel)
	{
		return channel == null || channel.getRemoteAddress() == null ? 
				null : channel.getRemoteAddress().toString();
	}
	
	
	public static String getLocalAddress(Channel channel)
	{
		return channel == null || channel.getLocalAddress() == null ? 
				null : channel.getLocalAddress().toString();
	}
	
	/**
	 * 组合serverId.nodeName，在注册连接和发起请求时都使用这个作为serverId,
	 * 以保证集群环境中每个发起请求的服务器可以收到对应的影响。
	 * @param destServerId
	 * @return
	 */
	public static String combinedServerIdAndNodeName() {
		//return GudongConfig.CLUSTER_NODE_NAME+"."+GudongConfig.SERVER_IDENTITY;
		return null;
	}
}
