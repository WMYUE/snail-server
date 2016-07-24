package com.snail.fitment.common.cipher;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESCipher implements ICipher {
	static{
		Security.addProvider(new BouncyCastleProvider());
	}
	/**
	 * 加密
	 * @param plainText 待加密数据
	 * @param key 加密密钥
	 * @return 加密值
	 * @throws Exception
	 */
	@Override
	public byte[] encrypt(byte[] plainText, byte[] key) throws Exception{
		if ( plainText == null) {
			throw new IllegalArgumentException("Input parameter plainText is null.");
		}
		if ( key == null) {
			throw new IllegalArgumentException("Input parameter key is null. " );
		}
		
		SecretKey secretKey = new SecretKeySpec(key,CIPHER_ALGORITHM_AES);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION_ALGORITHM_AES_PKCS7, BouncyCastleProvider.PROVIDER_NAME);//苹果系统只支持PKCS7Padding，而jdb不支持，需使用BouncyCastleProvider
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);		
		byte[] result = cipher.doFinal(plainText);
		return result;
	}

	/**
	 * 解密
	 * @param ciphertext 待解密的数据
	 * @param key 解密密钥
	 * @return 明文数据
	 * @throws Exception
	 */
	@Override
	public byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception{
		if ( ciphertext == null) {
			throw new IllegalArgumentException("Input parameter ciphertext is null. ciphertext is null" );
		}
		if ( key == null) {
			throw new IllegalArgumentException("Input parameter key is null.");
		}
		
		SecretKey secretKey = new SecretKeySpec(key,CIPHER_ALGORITHM_AES);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION_ALGORITHM_AES_PKCS7, BouncyCastleProvider.PROVIDER_NAME);//苹果系统只支持PKCS7Padding，而jdb不支持，需使用BouncyCastleProvider
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] result = cipher.doFinal(ciphertext);
		return result;
	}

}
