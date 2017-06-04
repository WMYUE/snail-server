package com.snail.fitment.protocol.bpackage;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;

/**
 * 蓝图协议包定义
 * @author Deng Yiping,2016-03-30
 *
 */
public class BpPackage extends AbstractCodecPackage {
	
	private static final long serialVersionUID = -3891257264299485291L;

	private static final Logger log = Logger.getLogger(BpPackage.class);
	
	private IBpHeader _bpHeader;
	private IBpBody _bpBody;
	private byte[] _buffer;
	
	//以下为包体的字段
	public BpPackage(byte stackVersion,byte messageType,short statusCode,
			Date time, int seq, 
			byte dataType,byte bodyFormat,
			byte encryptType, byte compressType, 
			String body, byte[] attachmentBytes) {
		//_bpHeader = new BpHeader(stackVersion, messageType, statusCode, time, seq, dataType, bodyFormat, encryptType, compressType);
		_bpBody = new BpBody();
		this.getBpBody().setBodyString(body);
		this.getBpBody().setAttachment(attachmentBytes);
	}
	
	public BpPackage(byte messageType, int seq, 
			byte encryptType, byte compressType) {
		_bpHeader = new BpHeader( messageType, seq, encryptType, compressType);
		_bpBody = new BpBody();
	}
	
	
	public BpPackage( String body, byte[] attachmentBytes) {
		_bpHeader = new BpHeader();
		_bpBody = new BpBody();
		this.getBpBody().setBodyString(body);
		this.getBpBody().setAttachment(attachmentBytes);
	}
	
	public BpPackage(byte[]  buffer) {
		this(buffer,false,false);
	}

	public BpPackage(byte[]  buffer, boolean isHeartbeat) {
		this(buffer,false,isHeartbeat);
	}
	
	public BpPackage(byte[]  buffer, boolean isFlashPolicy,boolean isHeartbeat) {
		this._buffer = buffer;
		parsePackage();
	}
	
	public IBpBody getBpBody() {
		return _bpBody;
	}
	
	public IBpHeader getBpHeader() {
		return this._bpHeader;
	}

	public byte[] getPackageBuffer() {
		return _buffer;
	}
	
	public void setPackageBuffer(byte[] packageBuffer) {
		_buffer = packageBuffer;
	}

	public byte[] getTotalBuffer() {
		return getPackageBuffer();
	}
	
	
	public void fillPackageBuffer() {
		int _length = BpHeader.FixedHeaderSize;
		
		//body长度
		if ( _bpBody.getBodyBuffer() != null && _bpBody.getBodyBuffer().length > 0  ) {
			_length = _length + _bpBody.getBodyBuffer().length + 4;
		}else{
			_length = _length + 4;
		}
		
		//int _length = BpHeader.FixedHeaderSize + _bpBody.getBodyBuffer().length + 4 ;
		//附件长度
		if ( _bpBody.getAttachment() != null && _bpBody.getAttachment().length > 0 ) {
			//增加附件长度记录记录4字节+附件内容
			_length = _length + 4 + _bpBody.getAttachment().length;	
		}
		
		//初始化数组时，需要加上包体长度记录本身4字节。
		this._buffer = new byte[_length];
		BigEndianHeapChannelBuffer writer = new BigEndianHeapChannelBuffer(_buffer);
		writer.setIndex(0, 0);
		
		//输出包头
		writer.writeBytes(BpHeader.StackLabel.getBytes());
		writer.writeByte(_bpHeader.getStackVersion());
		writer.writeByte(_bpHeader.getMessageType());
		
	
		writer.writeShort(_bpHeader.getStatusCode());
		writer.writeLong(_bpHeader.getTime());
		
		writer.writeInt(_bpHeader.getSeq());
		writer.writeInt(_length-BpHeader.FixedHeaderSize);
		
		writer.writeInt(_bpHeader.getOpCode());
		writer.writeByte(_bpHeader.getDataType());
		writer.writeByte(_bpHeader.getBodyFormat());
		writer.writeByte(_bpHeader.getEncryptType());
		writer.writeByte(_bpHeader.getCompressType());

		
		//输出包体
		if ( _bpBody.getBodyBuffer() != null && _bpBody.getBodyBuffer().length > 0  ) {
			writer.writeInt(_bpBody.getBodyBuffer().length);
			writer.writeBytes(_bpBody.getBodyBuffer());
		}else{ //没有包体的时候，包体长度设为0
			writer.writeInt(0);
		}
		
		//输出附件内容
		if ( _bpBody.getAttachment() != null && _bpBody.getAttachment().length > 0  ) {
			writer.writeInt( _bpBody.getAttachment().length);
			writer.writeBytes( _bpBody.getAttachment());
		}
	}

	
	
	private void parsePackage() {
		BigEndianHeapChannelBuffer reader = new BigEndianHeapChannelBuffer(_buffer);
		log.debug("parseBuffer, _buffer length=" + _buffer.length);
		byte[] temp= new byte[4];
		reader.readBytes(temp);
		if (!IBpHeader.StackLabel.equals(new String(temp))) {
				log.warn("parseBuffer - no StackLabel parsed! expected: " + IBpHeader.StackLabel 
						+ ", real: " + temp);
		}
		
		
		byte stackVersion = reader.readByte();
		byte messageType = reader.readByte();
		short statusCode = reader.readShort();
		
		long time = reader.readLong();
		
		int seq = reader.readInt();
		int length = reader.readInt();
		
		int opCode = reader.readInt();
		byte dataType = reader.readByte();
		byte bodyFormat = reader.readByte();
		
		byte encryptType = reader.readByte();
		byte compressType = reader.readByte();
		
		_bpHeader = new BpHeader(stackVersion, messageType, statusCode, time
				,seq, length
				,opCode, dataType, bodyFormat,encryptType, compressType);
		
		log.debug("parseBuffer, header=" + _bpHeader.toString());
		

		_bpBody = new BpBody();
		int bodyLen = reader.readInt();
		if (bodyLen > 0) {
			log.debug("parseBuffer, body length=" + bodyLen);
			byte[] bodytemp = new byte[bodyLen];
			reader.readBytes(bodytemp);
			this.getBpBody().setBodyBuffer(bodytemp);
		} else {
			this.getBpBody().setBodyBuffer(new byte[]{});
		}
		

		if ( reader.readableBytes() > 4 ) {
			//如果在包体后还有可读数据，那么认为是附件内容
			int attachmentLength = reader.readInt();
			if ( attachmentLength > 0 ) {
				byte[] attachmenttemp = new byte[attachmentLength];
				reader.readBytes(attachmenttemp);
				this.getBpBody().setAttachment(attachmenttemp);	
			}			
		}		
		
		this.initBodyBinaryFlag();
	}

	public String toRawString() {
		return "BpPackage [_buffer=" + Arrays.toString(_buffer)+"]";
	}
	
	//跟协议相关的方法  - begin
	public void addResponseOperation(int opCode, Map<String, Object> params) {
		this.getBpHeader().setOpCode(opCode);
		Map<String, Object> temp = new HashMap<>(params);
		this.getBpBody().clear();
		this.getBpBody().getParams().putAll(temp);		
		this.getBpBody().setBodyBuffer(this.getBpBody().toJsonString().getBytes());
	}	
	//跟协议相关的方法  - end
	
	@Override
	public String toString() {
	
		return "BpPackage [_opCode=" + _bpHeader.getOpCode()
				+ ", _body=" + this.getBodyLogString() 
				+ ", _length=" + _bpHeader.getLength()
				+ ", _stackVersion=" + _bpHeader.getStackVersion()
				+ ", _messageType=" + _bpHeader.getMessageType() 
				+ ", _seq=" + _bpHeader.getSeq()
				+ ", _compressType=" + _bpHeader.getCompressType() 
				+ ", _encryptType=" + _bpHeader.getEncryptType()
				+ ", _bodyFormat=" + _bpHeader.getBodyFormat() + ", _dataType=" + _bpHeader.getDataType()
				+ ", _statusCode=" + _bpHeader.getStatusCode()+ ", _time=" + _bpHeader.getTime() 
				+ "]";
	}
}

