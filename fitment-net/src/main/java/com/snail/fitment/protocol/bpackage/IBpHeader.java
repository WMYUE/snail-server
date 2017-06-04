package com.snail.fitment.protocol.bpackage;

import com.snail.fitment.common.constants.OperationConstants;

public interface IBpHeader {
	/**
	 * 协议标记： 4 byte
	 */
	public static final String StackLabel = "BPTP";

	public static final byte MESSAGE_TYPE_REQUEST = OperationConstants.TYPE_REQ;
	public static final byte MESSAGE_TYPE_RESPONSE = OperationConstants.TYPE_RES;

	public static final String ACTION_TYPE_REQUEST = "request";
	public static final String ACTION_TYPE_RESPONSE = "response";

	public static final byte ENCRYPT_TYPE_AES = 2;
	public static final byte ENCRYPT_TYPE_AES_V2 = 3;

	/**
	 * header中的固定的前32个字节
	 */
	public static final int FixedHeaderSize = 32;

	public byte getStackVersion();

	public void setStackVersion(byte stackVersion);

	public byte getMessageType();

	public void setMessageType(byte messageType);

	public short getStatusCode();

	public void setStatusCode(short code);

	public long getTime();

	public void setTime(long time);

	public int getSeq();

	public void setSeq(int seq);

	public int getLength();

	public void setLength(int length);

	public int getOpCode();

	public void setOpCode(int opCode);

	public byte getDataType();

	public void setDateType(byte dataType);

	public byte getBodyFormat();

	public void setBodyFormat(byte bodyFormat);

	public byte getEncryptType();

	public void setEncryptType(byte encryptType);

	public byte getCompressType();

	public void setCompressType(byte compressType);

	// 解析包头
	public void parseBpHeader(byte[] headerBuffer);

}
