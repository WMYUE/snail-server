package com.snail.fitment.protocol.bpackage;

import java.util.Map;

public interface IBpBody {

	// 由 _bodyBuffer 直接解析出消息体对象，忽略加解密及压缩算法
	public byte[] getBodyBuffer();

	public void setBodyBuffer(byte[] bodyBuffer);

	public String getBodyString();

	public void setBodyString(String bodyStr);

	public byte[] getAttachment();

	public void setAttachment(byte[] attachmentBytes);

	void parseBpBody();

	public void fromJsonString(String jsonString);

	public String toJsonString();

	public boolean hasContent();

	public void clear();

	public Map<String, Object> getParams();
}
