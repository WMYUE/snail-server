package com.snail.fitment.protocol.client;

import com.snail.fitment.protocol.bpackage.IBpPackage;

public interface IChannelListener {
	public enum State {
		ChannelIni,
		ChannelConnecting,
		ChannelConnectFail,
		ChannelConnected,
		ChannelClosed,
	}
	public void onChannelStateChanged(IChannel channel, int stateCode, String stateDescr,State state);
	public void onReceive(IChannel channel, IBpPackage gdpPackage);
}