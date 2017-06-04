package com.snail.fitment.model.vo;

import java.io.Serializable;

public class RequestContext implements Serializable {
	private static final long serialVersionUID = -6805386403137408964L;
	
	private String connectionId;
	private String clientAddress;
	private byte msgType;
	private int opCode;
	private String requestBody;
	private byte[] requestAttachment;
	
	public RequestContext(String connectionId, String clientAddress, byte msgType, int opCode, String requestBody,
			byte[] requestAttachment) {
		this.connectionId = connectionId;
		this.clientAddress = clientAddress;
		this.msgType = msgType;
		this.opCode = opCode;
		this.requestBody = requestBody;
		this.requestAttachment = requestAttachment;
	}
	public RequestContext() {}
	public byte getMsgType() {
		return msgType;
	}
	public void setMsgType(byte msgType) {
		this.msgType = msgType;
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
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	public int getOpCode() {
		return opCode;
	}
	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	public byte[] getRequestAttachment() {
		return requestAttachment;
	}
	public void setRequestAttachment(byte[] requestAttachment) {
		this.requestAttachment = requestAttachment;
	}
	
}
