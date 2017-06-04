package com.snail.fitment.protocol.bpackage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.common.lang.StringBufferUtf8;

/**
 * 蓝图协议包体定义
 * @author Deng Yiping,2016-03-30
 *
 */
public class BpBody implements IBpBody, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1932150748641464610L;
	
	
	//字段13（4  byte）消息体， UTF-8编码转换后的 byte
	protected StringBufferUtf8 _bodyBuffer = null; 
	
	//字段14（动态数组）附件内容：消息体中包含了需要传递的主要业务数据，下面将专门对其格式定义进行介绍。
	byte[] _attachmentBytes = null;
	
	//解析body得到的请求参数
	Map<String, Object> paramMap = new HashMap<String, Object>();
	
	public BpBody() {
	}

	public BpBody(String bodyString) {
		this.fromJsonString(bodyString);
	}
		
//	public void fillBodyBuffer() {
//		int _length = 0;
//		//先存bodyBuffer
//		if ( this.getBodyBuffer() != null && this.getBodyBuffer().length > 0  ) {
//			_length = _length + getBodyBuffer().length;
//		}
//		if ( this.getAttachment() != null && this.getAttachment().length > 0  ) {
//			_length = _length + getAttachment().length;
//		}
//		
//		if(_length == 0){
//			return;
//		}
//	
//		this._bodyContent = new byte[_length];
//		BigEndianHeapChannelBuffer writer = new BigEndianHeapChannelBuffer(this._bodyContent);
//		
//		//先存bodyBuffer
//		if ( this.getBodyBuffer() != null && this.getBodyBuffer().length > 0  ) {
//			int bodyLength  = getBodyBuffer().length;
//			writer.writeInt(bodyLength);
//			writer.writeBytes(getBodyBuffer());
//		}
//		
//		//再存attachment
//		if ( this.getAttachment() != null && this.getAttachment().length > 0  ) {
//			int attachLength = getAttachment().length;
//			writer.writeInt(attachLength);
//			writer.writeBytes(getAttachment());
//		}
//
//	}

	public void parseBpBody() {
		this.fromJsonString(_bodyBuffer.getStrValue());
	}

	public String getBodyString() {
		return _bodyBuffer == null ? null : _bodyBuffer.getStrValue();

	}
	
	public byte[] getBodyBuffer() {
		return _bodyBuffer == null ? null : _bodyBuffer.getBuffer();
	}

	public void setBodyBuffer(byte[] bodyBuffer) {
		_bodyBuffer = new StringBufferUtf8(bodyBuffer);
	}

	public void setBodyString(String bodyStr) {
		_bodyBuffer = new StringBufferUtf8(bodyStr);
	}
	
	public byte[] getAttachment(){
		return this._attachmentBytes;
	}
	
	public void setAttachment(byte[] attachmentBytes){
		this._attachmentBytes = attachmentBytes;
	}
	
	public void fromJsonString(String jsonString) {
		paramMap.putAll((Map) JsonConvert.DeserializeGdpObject(jsonString));
	}

	public String toJsonString() {
		return JsonConvert.SerializeObject(paramMap);
	}
	
	public <T> T getParam(String key, Class<T> paramClass, T defaultValue) {
		return JsonConvert.convertObject(paramMap.get(key), paramClass, defaultValue);
	}

	public <T> List<T> getParamToList(String key, Class<T> itemClass, List<T> defaultValue) {
		return JsonConvert.convertList(paramMap.get(key), itemClass, defaultValue);
	}

	public boolean hasContent() {
		return paramMap != null && paramMap.size() > 0;
	}

	public Object getContentObj() {
		return paramMap;
	}
	
	public void clear() {
		this.paramMap.clear();
	}


	public Map<String, Object> getParams() {
		return paramMap;
	}
	
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
}
