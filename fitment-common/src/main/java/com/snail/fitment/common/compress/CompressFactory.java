package com.snail.fitment.common.compress;
/**
 * 压缩工厂类
 * @author hanjiong
 * @date 2011-7-5
 */
public class CompressFactory {
	
	private CompressFactory(){
		
	}
	
	public static ICompress getCompressInstance(int compressType){
		ICompress compress = null;
		switch (compressType) {
			case CompressAlgorithm.ALGORITHM_GZIP_ID:
				compress = new GZIPCompress();
				break;
			default :
				throw new IllegalArgumentException("Can't support this compressType:" + compressType);
		}
		return compress;
	}
}
