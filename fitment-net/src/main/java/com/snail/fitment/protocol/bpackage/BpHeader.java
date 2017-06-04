package com.snail.fitment.protocol.bpackage;

import java.util.Date;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;


public class BpHeader implements IBpHeader {

	private static final Logger log = Logger.getLogger(BpHeader.class);
	
	//字段1（4 byte）：协议标记
	//继承字段，StackLabel
	String _stackLable = StackLabel;
	
	//字段2（1 byte）：协议版本 ,必填，与协议版本无关,1 第一个版本
	byte _stackVersion = 1; 
	
	//字段3（1  byte）：消息类型， 1 byte,1: 请求（request）,2: 响应（response）
	protected byte _messageType = 1; 
	
	//字段4（2  byte）状态码：响应消息中针对请求中消息头信息的问题给出的状态码，请求消息中此字段直接填 0可。响应消息中，0 表示无问题，非 0 表示有问题。
	protected short _statusCode = 0; 

	//字段5（8  byte）时间： 8 byte,long ，系统标准时间，格林威治时间
	protected long _time = new Date().getTime(); 
	
	//字段6（4  byte）：序列号，用来匹配请求响应对，对每个 客户端来说，在一段时间内唯一（可能是一个比较大的循环的值，如65535）
	int _seq = 0; 
		
	//字段7（4 byte）：总长度: 必填，与协议版本无关，这个总长度不包含这四字节
	int _length = 0; 
	
	//字段8（4  byte）操作代码，以数值形式表示的消息体操作定义，操作代码为十进制表示，从高到低依次为①系统编号、②三位模块编号、③一位操作方向编号（客户端到服务端为1，服务端到客户端为2）、④两位操作编号，按照整数格式，高位零省略。
	protected int _opCode = 0000100; //操作代码

	//字段9（1  byte）：数据类型：1: 数据,2: 命令
	protected byte _dataType = 1;
	
	//字段10（1  byte）：消息体内容格式类型：1: JSON,2: xml,3: Bcode,4: binary
	protected byte _bodyFormat = 1; 

	//字段11（1 byte）：加密类型，0: 不加密
	protected byte _encryptType = 0;
	
	//字段12（1  byte）：压缩类型， 1: 不压缩,2: GZIP
	protected byte _compressType = 1; 
		

		
	public byte getStackVersion() {
		return _stackVersion;
	}

	public void setStackVersion(byte stackVersion) {
		this._stackVersion = stackVersion;	
	}
	
	public byte getEncryptType() {
		return _encryptType;
	}

	public void setEncryptType(byte encryptType) {
		this._encryptType = encryptType;
		
	}
	
	public void setCompressType(byte compressType) {
		_compressType = compressType;
	}

	public byte getCompressType() {
		return _compressType;
	}

	public byte getMessageType() {
		return _messageType;
	}
	
	public void setMessageType(byte messageType) {
		_messageType = messageType;
	}

	public int getSeq() {
		return _seq;
	}
	
	public void setSeq(int seq) {
		this._seq = seq;
	}

	public short getStatusCode() {
		return _statusCode;
	}
	
	public void setStatusCode(short code) {
		_statusCode = code;
	}
	
	public byte getBodyFormat() {
		return _bodyFormat;
	}
	
	public void setBodyFormat(byte bodyFormat) {
		this._bodyFormat = bodyFormat;
	}

	public long getTime() {
		return this._time;
	}


	public void setTime(long time) {
		this._time = time;
	}

	public int getLength() {
		return _length;
	}
	
	public void setLength(int length) {
		this._length = length;
	}


	public void setDateType(byte dataType) {
		this._dataType = dataType;
	}


	public byte getDataType() {
		return this._dataType;
	}


	public int getOpCode() {
		return _opCode;
	}
	
	public void setOpCode(int opCode) {
		_opCode = opCode;
	}

	public BpHeader(){
		
	}
	
	public BpHeader(byte[] headerBuffer){
		this.parseBpHeader(headerBuffer);
	}
	
	public BpHeader(byte messageType, int seq,  byte encryptType, byte compressType){
		_messageType = messageType;
		_seq = seq;
		_encryptType = encryptType;
		_compressType = compressType;
	}
	
	public BpHeader(byte stackVersion,byte messageType,short statusCode,
			 long time, int seq, int length,
			int opCode, byte dataType,byte bodyFormat, byte encryptType, byte compressType){
		_stackVersion = stackVersion;
		_messageType = messageType;
		_statusCode = statusCode;
		
		_time = time;
		
		_seq = seq;
		_length = length;

		_opCode = opCode;
		_dataType = dataType;
		_bodyFormat = bodyFormat;
		_encryptType = encryptType;
		_compressType = compressType;
	}
	
	public void parseBpHeader(byte[] headerBuffer) {
		if(headerBuffer == null || headerBuffer.length<  FixedHeaderSize) {
	    	log.debug("parsePackageHeader, the package head is not completed");
	    	return;
		}
		 
		BigEndianHeapChannelBuffer reader = new BigEndianHeapChannelBuffer(headerBuffer);
		byte[] temp= new byte[4];
		reader.readBytes(temp);
		if (!StackLabel.equals(new String(temp))) {
				log.warn("parseBpHeader - no StackLabel parsed! expected: " + StackLabel 
						+ ", real: " + temp);
		}
		_stackVersion = reader.readByte();
		_messageType = reader.readByte();
		_statusCode = reader.readShort();
		
		_time = reader.readLong();
		
		_seq = reader.readInt();
		_length = reader.readInt();
		
		_opCode = reader.readInt();
		_dataType = reader.readByte();
		_bodyFormat = reader.readByte();
		
		_encryptType = reader.readByte();
		_compressType = reader.readByte();
	}

	@Override
	public String toString() {
		return "BpHeader [_stackLable=" + _stackLable + ", _stackVersion="
				+ _stackVersion + ", _messageType=" + _messageType
				+ ", _statusCode=" + _statusCode + ", _time=" + _time
				+ ", _seq=" + _seq + ", _length=" + _length + ", _opCode="
				+ _opCode + ", _dataType=" + _dataType + ", _bodyFormat="
				+ _bodyFormat + ", _encryptType=" + _encryptType
				+ ", _compressType=" + _compressType + "]";
	}
	
}
