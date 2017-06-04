package com.snail.fitment.protocol.bpackage;

import java.util.Map;

public final class FlashPolicy implements IBpPackage {
	public static final IBpPackage Instance = new FlashPolicy();
	private byte[] content = new byte[] { 0, 0, 0, 0 };

	@Override
	public void decode(String sessionKey) {
	}

	@Override
	public void encode(String sessionKey) {
	}

	@Override
	public IBpHeader getBpHeader() {
		return mockHeader;
	}

	@Override
	public IBpBody getBpBody() {
		return null;
	}

	@Override
	public byte[] getPackageBuffer() {
		return content;
	}

	@Override
	public void setPackageBuffer(byte[] packageBuffer) {
		content = packageBuffer;
	}

	@Override
	public void fillPackageBuffer() {
	}

	@Override
	public void addResponseOperation(int opCode, Map<String, Object> params) {
	}

	private final IBpHeader mockHeader = new IBpHeader() {

		@Override
		public byte getStackVersion() {
			return 0;
		}

		@Override
		public void setStackVersion(byte stackVersion) {
		}

		@Override
		public byte getMessageType() {
			return 0;
		}

		@Override
		public void setMessageType(byte messageType) {
		}

		@Override
		public short getStatusCode() {
			return 0;
		}

		@Override
		public void setStatusCode(short code) {
		}

		@Override
		public long getTime() {
			return 0;
		}

		@Override
		public void setTime(long time) {
		}

		@Override
		public int getSeq() {
			return 0;
		}

		@Override
		public void setSeq(int seq) {
		}

		@Override
		public int getLength() {
			return 0;
		}

		@Override
		public void setLength(int length) {
		}

		@Override
		public int getOpCode() {
			return 0;
		}

		@Override
		public void setOpCode(int opCode) {
		}

		@Override
		public byte getDataType() {
			return 0;
		}

		@Override
		public void setDateType(byte dataType) {
		}

		@Override
		public byte getBodyFormat() {
			return 0;
		}

		@Override
		public void setBodyFormat(byte bodyFormat) {
		}

		@Override
		public byte getEncryptType() {
			return 0;
		}

		@Override
		public void setEncryptType(byte encryptType) {
		}

		@Override
		public byte getCompressType() {
			return 0;
		}

		@Override
		public void setCompressType(byte compressType) {
		}

		@Override
		public void parseBpHeader(byte[] headerBuffer) {
		}

	};
}
