package com.snail.fitment.protocol.client.support;

import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.client.IProtocolListener;

public class BlockingListener implements IProtocolListener {

	private IBpPackage reply = null;

	@Override
	public synchronized void onResponse(IBpPackage request, IBpPackage response) {
		this.reply= response;
		notify();
	}

	@Override
	public synchronized void onError(IBpPackage request, int stateCode, String descr) {
		this.reply= BpPackageFactory.createEmptyResponseByRequest(request);
		this.reply.getBpBody().setBodyString("{\"stateCode\":"+stateCode+",\"stateDesc\":\""+descr+"\"}");
		notify();
	}

	@Override
	public synchronized void onProcess(IBpPackage request, double process) {
		//todo
	}

	public IBpPackage getReply() {
		return this.reply;
	}
}
