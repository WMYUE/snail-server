package com.snail.fitment.common.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩类
 * 
 * @author hanjiong
 * @date 2011-7-5
 */
public class GZIPCompress implements ICompress {

	@Override
	public String algorithm() {
		// TODO Auto-generated method stub
		return CompressAlgorithm.ALGORITHM_GZIP;
	}

	/**
	 * 压缩
	 * 
	 * @param data
	 *            待压缩数据
	 * @return 压缩后的数据
	 * @throws Exception
	 */
	@Override
	public byte[] zip(byte[] data) throws Exception {
		if (data == null) {
			return null;
		}
		// TODO Auto-generated method stub
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteOutputStream);
		gzipOutputStream.write(data);
		gzipOutputStream.flush();
		gzipOutputStream.finish();
		byte[] bufferBytes = byteOutputStream.toByteArray();

		gzipOutputStream.close();
		byteOutputStream.close();
		return bufferBytes;
	}

	/**
	 * 解压缩
	 * 
	 * @param data
	 *            压缩后的压缩数据
	 * @return 解压缩后的数据
	 * @throws Exception
	 */
	@Override
	public byte[] unzip(byte[] data) throws Exception {
		if (data == null) {
			return null;
		}
		// TODO Auto-generated method stub
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		GZIPInputStream gzipInputStream = new GZIPInputStream(byteInputStream);
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = gzipInputStream.read(buffer)) != -1) {
			byteOutputStream.write(buffer, 0, len);
		}
		byte[] bufferBytes = byteOutputStream.toByteArray();

		byteInputStream.close();
		byteOutputStream.close();
		gzipInputStream.close();
		return bufferBytes;
	}

}
