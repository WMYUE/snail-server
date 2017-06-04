package com.snail.fitment.protocol.client;

import java.net.InetSocketAddress;

import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.client.IChannelListener.State;

public interface IChannel {

	public static final int ERR_CODE_OFF_LINE = -5;
	public static final int CODE_TIMEOUT = -12;
	public void connect(InetSocketAddress address);
	public ISendTask sendPackage(IBpPackage request, IProtocolListener listener, int[] intervals);
	public IBpPackage sendPackageSync(IBpPackage request, int[] intervals);
	public void close();
	///
	public void onDisconnected();
	public void onReceivePackage(IBpPackage gdpPackage);
	public void onReceiveProgress(short seq, double progress);
	///
	public int getSendingTaskCount();
	public State state();
	public InetSocketAddress connectAddress();
	public String domain();
}
