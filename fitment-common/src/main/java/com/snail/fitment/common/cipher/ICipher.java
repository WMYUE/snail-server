package com.snail.fitment.common.cipher;

public interface ICipher {
	
	public final static String CIPHER_ALGORITHM_BASE64 = "base64";
	public final static String CIPHER_ALGORITHM_AES = "aes";
	
	public static final String TRANSFORMATION_ALGORITHM_AES_PKCS7 = "AES/ECB/PKCS7Padding";
	public static final String TRANSFORMATION_ALGORITHM_AES_PKCS5 = "AES/ECB/PKCS5Padding";
	public static final String TRANSFORMATION_ALGORITHM_RSA_PKCS1 = "RSA/ECB/PKCS1Padding";	
	public static final String TRANSFORMATION_ALGORITHM_DES_PKCS7 = "DESede/ECB/PKCS7Padding";
	
	/**
	 * 加密
	 * @param plainText 待加密数据
	 * @param key 加密密钥
	 * @return 加密值
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] plainText, byte[] key) throws Exception;
	
	/**
	 * 解密
	 * @param ciphertext 待解密的数据
	 * @param key 加密密钥
	 * @return 明文数据
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception;
	
}
