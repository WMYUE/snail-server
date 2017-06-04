package com.snail.fitment.protocol.bpackage;

import java.util.Map;

public interface IBpPackage {
	// 编解码方法，会考虑加解密及压缩算法, 由AbstractCodecPackage实现了
	public void decode(String sessionKey);

	public void encode(String sessionKey);

	public IBpHeader getBpHeader();

	public IBpBody getBpBody();

	public byte[] getPackageBuffer();

	public void setPackageBuffer(byte[] packageBuffer);

	// 根据消息对象内容直接生成二进制的 _buffer，忽略加解密及压缩算法；
	// 调用直接得先初始化_buffer
	void fillPackageBuffer();

	// 跟协议相关的方法
	public void addResponseOperation(int opCode, Map<String, Object> params);
}
