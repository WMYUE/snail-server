package com.snail.fitment.common.cipher;

public abstract class CipherAlgorithm {
	public static final String ALGORITHM_AES = "AES";
	public static final int ALGORITHM_AES_ID = 1;
	public static final int ALGORITHM_AES_V2_ID = 3;
	public static final String ALGORITHM_DES = "DESede";
	public static final int ALGORITHM_DES_ID = 4;	
	public static final String ALGORITHM_SM4 = "SM4";
	public static final int ALGORITHM_SM4_ID = 5;
	public static final String ALGORITHM_RC4 = "RC4";
	public static final int ALGORITHM_RC4_ID = 6;
	
	public static final String ALGORITHM_RSA = "RSA";
	public static final int ALGORITHM_RSA_ID = 11;
	public static final String ALGORITHM_SM9 = "SM9";
	public static final int ALGORITHM_SM9_ID = 12;
		
	public static final String TRANSFORMATION_ALGORITHM_AES_PKCS7 = "AES/ECB/PKCS7Padding";
	public static final String TRANSFORMATION_ALGORITHM_AES_PKCS5 = "AES/ECB/PKCS5Padding";
	public static final String TRANSFORMATION_ALGORITHM_RSA_PKCS1 = "RSA/ECB/PKCS1Padding";	
	public static final String TRANSFORMATION_ALGORITHM_DES_PKCS7 = "DESede/ECB/PKCS7Padding";
}
