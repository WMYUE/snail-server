package com.snail.fitment.common.compress;


public interface ICompress {
	
	String algorithm();
	
	/**
	 * 压缩
	 * @param data 待压缩数据
	 * @return 压缩后的数据
	 * @throws Exception
	 */
	byte[] zip(byte[] data) throws Exception;
	
	/**
	 * 解压缩
	 * @param data 压缩后的压缩数据
	 * @return 解压缩后的数据
	 * @throws Exception
	 */
	byte[] unzip(byte[] data) throws Exception;
}
