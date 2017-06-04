package com.snail.fitment.protocol.handler;

import org.apache.log4j.Logger;

import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.bpackage.IBpPackage;

public class DefaultHandlerContext implements HandleContext {
	
    private static final Logger log =  Logger.getLogger(DefaultHandlerContext.class);

    protected String connectionId;
    protected String clientAddress;
	
    protected IBpPackage srcPackage;
    protected IBpPackage response = null; // 如果 srcPackage 是 request ，则此处保存期对应的 response
    protected IBpPackage request = null; // 如果 srcPackage 是 response ，则此处保存其对应的 request


	public DefaultHandlerContext(IBpPackage srcPackage, String connectionId, String clientAddress) {
		this.srcPackage = srcPackage;
		this.connectionId = connectionId;
		this.clientAddress = clientAddress;
		
		// 如果是请求，生成相关响应包
		if (srcPackage.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST) {
			this.response = BpPackageFactory.createEmptyResponseByRequest(srcPackage);
		}
	}

	public DefaultHandlerContext(IBpPackage srcPackage, String connectionId,
			String clientAddress, IBpPackage request) {
		this.srcPackage = srcPackage;
		this.connectionId = connectionId;
		this.clientAddress = clientAddress;
		this.request = request;

		// 如果是请求，生成相关响应包
		if (srcPackage.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST) {
			this.response = BpPackageFactory.createEmptyResponseByRequest(srcPackage);
		}
	}

	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public String getClientAddress() {
		return clientAddress;
	}

	@Override
	public IBpPackage getSrcPackage() {
		return srcPackage;
	}

	@Override
	public IBpPackage getResponse() {
		return response;
	}

	@Override
	public IBpPackage getRequest() {
		return request;
	}


	@Override
	public void addResponseAttachment(byte[] attachment) {
		response.getBpBody().setAttachment(attachment);
	}

	@Override
	public byte[] getResponseAttachment() {
		return response.getBpBody().getAttachment();
	}

	@Override
	public String toString() {
		return "DefaultHandlerContext [connectionId=" + connectionId
				+ ", clientAddress=" + clientAddress + ", srcPackage="
				+ srcPackage + ", response=" + response + ", request="
				+ request  + ", requestList=" 
				+ "]";
	}

	//implemented from netty

}
