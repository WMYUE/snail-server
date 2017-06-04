package com.snail.fitment.operation.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.snail.fitment.model.SessionInfo;

public class OperationContext {
	private SessionInfo session;
	private byte[] attachmentBytes;

	// 如果要处理的是响应包，此处传递相关请求中的参数
	private Map<String, Object> reqParams = new HashMap<String, Object>();
	
	
	
	public byte[] getAttachmentBytes() {
		return attachmentBytes;
	}

	public void setAttachmentBytes(byte[] attachmentBytes) {
		this.attachmentBytes = attachmentBytes;
	}

	public Map<String, Object> getReqParams() {
		return reqParams;
	}

	public void setReqParams(Map<String, Object> reqParams) {
		this.reqParams = reqParams;
	}

	public SessionInfo getSession() {
		return session;
	}

	public void setSession(SessionInfo session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "OperationContext [session=" + session + ", attachmentBytes=" + Arrays.toString(attachmentBytes)
				+ ", reqParams=" + reqParams + "]";
	}
	
	

}
