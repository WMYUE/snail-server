package com.snail.fitment.common.security;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

//import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.snail.fitment.common.cipher.CipherAlgorithm;
import com.snail.fitment.common.cipher.CipherManager;
import com.snail.fitment.common.config.CaConfig;

/**
 * 安全相关操作工具类
 * @author hanj
 * @date 2011-6-8
 */
public final class SecurityUtil {
	
	public static final String ALGORITHM_RSA = "RSA";
	private static String SIGNATURE_ALG = "SHA1WithRSA";
	
	public static final int ASY_ALG_TYPE_RSA = 0;
	public static final int ASY_ALG_TYPE_SM9 = 1;
	
	/**
	 * 创建会话加密密钥
	 * @param random 随机数
	 * @param password 用户的密文密码(base64)
	 * @return 16个字节的加密密钥，Base64编码值
	 */
	public static String createSessionKey(String random,String password){
		if ( StringUtils.isEmpty(random) ) {
			throw new IllegalArgumentException("Input parameter random is invalid. random=" + random);
		}
		if ( StringUtils.isEmpty(password) ) {
			throw new IllegalArgumentException("Input parameter password is invalid. password=" + password);
		}
		
		byte[] random_md5 = DigestUtils.md5(random);
		byte[] password_md5 = Base64.getDecoder().decode(password);
		byte[] sessionKey = new byte[32];
		System.arraycopy(random_md5, 0, sessionKey, 0, 16);
		System.arraycopy(password_md5, 0, sessionKey, 16, 16);
		sessionKey = DigestUtils.md5(sessionKey);
		return Base64.getEncoder().encodeToString(sessionKey);
	}
	
	/**
	 * 创建安全随机数
	 * @param seed 种子
	 * @return 随机数
	 */
	public static String createSecurityRandom(String seed){
		if ( StringUtils.isEmpty(seed) ) {
			throw new IllegalArgumentException("Input parameter seed is invalid. seed=" + seed);
		}
		String random = DigestUtils.md5Hex(seed);
		return random;
	}
	
	/**
	 * 解析用户在客户端注册时的密码
	 * @param loginName
	 * @param random
	 * @return MD5的密文字符串,Base64编码值
	 */
	public static String decodeClientRegeistPassword(String loginName,String random,String encodePassword){	
		if ( StringUtils.isEmpty(loginName) ) {
			throw new IllegalArgumentException("Input parameter loginName is invalid. loginName=" + loginName);
		}
		if ( StringUtils.isEmpty(random) ) {
			throw new IllegalArgumentException("Input parameter random is invalid. random=" + random);
		}
		if ( StringUtils.isEmpty(encodePassword) ) {
			throw new IllegalArgumentException("Input parameter encodePassword is invalid. encodePassword=" + encodePassword);
		}
		
		byte[] enc_pass = Base64.getDecoder().decode(encodePassword);
		byte[] pre_pass = DigestUtils.md5(loginName+random);
		byte[] password_md5 = new byte[16];
		for(int i = 0; i < pre_pass.length; i++){				
			password_md5[i] = (byte)(enc_pass[i] ^ pre_pass[i]);
		}		
		return Base64.getEncoder().encodeToString(password_md5);
	}
	
	/**
	 * 使用会话密钥解密数据
	 * @param base64Str base64的密文数据
	 * @param base64SessionKey base64的会话密钥
	 * @return byte[] 解密后的数据
	 * @throws Exception
	 */
	public static byte[] decryptWithSessionKey(String base64Str,String base64SessionKey) throws Exception{
		if ( StringUtils.isEmpty(base64Str) ) {
			throw new IllegalArgumentException("Input parameter base64Str is invalid. base64Str=" + base64Str);
		}
		if ( StringUtils.isEmpty(base64SessionKey) ) {
			throw new IllegalArgumentException("Input parameter base64SessionKey is invalid. base64SessionKey=" + base64SessionKey);
		}
		return CipherManager.getInstance().getCipher(CipherAlgorithm.ALGORITHM_AES_ID).decrypt(Base64.getDecoder().decode(base64Str),Base64.getDecoder().decode(base64SessionKey));
	}
	
	/**
	 * 使用会话密钥解密数据
	 * @param data base64的密文数据
	 * @param base64SessionKey base64的会话密钥
	 * @return byte[] 解密后的数据
	 * @throws Exception
	 */
	public static byte[] decryptWithSessionKey(byte[] data,String base64SessionKey) throws Exception{
		if ( data == null ) {
			throw new IllegalArgumentException("Input parameter base64Str is invalid. data=" + data);
		}
		if ( StringUtils.isEmpty(base64SessionKey) ) {
			throw new IllegalArgumentException("Input parameter base64SessionKey is invalid. base64SessionKey=" + base64SessionKey);
		}
		return CipherManager.getInstance().getCipher(CipherAlgorithm.ALGORITHM_AES_ID).decrypt(data,Base64.getDecoder().decode(base64SessionKey));
	}
	
	/**
	 * 使用会话密钥加密数据
	 * @param plain 待加密数据 
	 * @param base64SessionKey base64的会话密钥
	 * @return byte[] 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encryptWithSessionKey(byte[] plain,String base64SessionKey) throws Exception{
		if ( plain == null ) {
			throw new IllegalArgumentException("Input parameter plain is null.");
		}
		if ( StringUtils.isEmpty(base64SessionKey) ) {
			throw new IllegalArgumentException("Input parameter base64SessionKey is invalid. base64SessionKey=" + base64SessionKey);
		}
		return CipherManager.getInstance().getCipher(CipherAlgorithm.ALGORITHM_AES_ID).encrypt(plain,Base64.getDecoder().decode(base64SessionKey));
	}
	
	/**
	 * 初始化RSA的公私钥对
	 * @return String[]{私钥，公钥}
	 * @throws NoSuchAlgorithmException
	 */
	public static String[] initKeyPair() throws NoSuchAlgorithmException{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
		return new String[]{privateKey,publicKey};
	}
	
	/**
	 * 使用公钥加密数据
	 * @param plainText 待加密数据
	 * @param publicKey RSA公钥
	 * @return 加密后的数据(base64编码)
	 * @throws Exception
	 */
	public static String encodeWithPublicKey(byte[] plainText,String base64PublicKey) throws Exception{
		if ( plainText == null ) {
			throw new IllegalArgumentException("Input parameter plainText is invalid.");
		}
		if ( StringUtils.isEmpty(base64PublicKey) ) {
			throw new IllegalArgumentException("Input parameter publicKey is invalid. base64PublicKey=" + base64PublicKey);
		}
		
		byte[] encText =  CipherManager.getInstance().getCipher(CipherAlgorithm.ALGORITHM_RSA_ID).encrypt(plainText, Base64.getDecoder().decode(base64PublicKey));
		return Base64.getEncoder().encodeToString(encText);
	}
	
	/**
	 * 使用私钥解密数据
	 * @param ciphertext 待解密数据
	 * @param privateKey RSA私钥
	 * @return 解密后的数据(base64编码)
	 * @throws Exception
	 */
	public static String decryptWithPrivateKey(String Base64Ciphertext,String base64PrivateKey) throws Exception{
		if ( StringUtils.isEmpty(Base64Ciphertext) ) {
			throw new IllegalArgumentException("Input parameter Base64Ciphertext is invalid. Base64Ciphertext=" + Base64Ciphertext);
		}
		if ( StringUtils.isEmpty(base64PrivateKey) ) {
			throw new IllegalArgumentException("Input parameter base64PrivateKey is invalid. base64PrivateKey=" + base64PrivateKey);
		}
		byte[] plainText = CipherManager.getInstance().getCipher(CipherAlgorithm.ALGORITHM_RSA_ID).decrypt(Base64.getDecoder().decode(Base64Ciphertext), Base64.getDecoder().decode(base64PrivateKey));
		return Base64.getEncoder().encodeToString(plainText);
	}
	
	/**
	 * base64编码
	 * @param plantData
	 * @return
	 */
	public static String encodeBase64(String plantData){
		if ( StringUtils.isEmpty(plantData) ) {
			return "";
		}
		return Base64.getEncoder().encodeToString(plantData.getBytes());
	}
	
	/**
	 * base64解码
	 * @param base64Data
	 * @return
	 */
	public static String decodeBase64(String base64Data){
		if ( StringUtils.isEmpty(base64Data) ) {
			return "";
		}
		return new String(Base64.getDecoder().decode(base64Data.getBytes()));
	}
	
	
	/**
	 * 使用私钥签名
	 * @param plainText 待签名数据
	 * @param base64Privatekey 私钥(base64)
	 * @return Base64编码的签名值
	 */
	public static String sign(String plainText, String base64Privatekey) throws Exception{
		if ( StringUtils.isEmpty(plainText) ) {
			throw new IllegalArgumentException("Input parameter plainText is invalid. plainText=" + plainText);
		}
		if ( StringUtils.isEmpty(base64Privatekey) ) {
			throw new IllegalArgumentException("Input parameter base64Privatekey is invalid. base64Privatekey=" + base64Privatekey);
		}
		byte[] sign = sign(plainText.getBytes(),Base64.getDecoder().decode(base64Privatekey));
		return Base64.getEncoder().encodeToString(sign);
	}
	
	/**
	 * 使用私钥签名
	 * @param plainData 待签名数据
	 * @param privatekey 私钥
	 * @return 签名值
	 */
	public static byte[] sign(byte[] plainData, byte[] privatekey) throws Exception{
		if ( plainData == null ) {
			throw new IllegalArgumentException("Input parameter plainData is invalid. ");
		}
		if ( privatekey == null ) {
			throw new IllegalArgumentException("Input parameter privatekey is invalid. ");
		}
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privatekey);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		PrivateKey _PriKey = keyFactory.generatePrivate(spec);
		Signature sign = Signature.getInstance(SIGNATURE_ALG);
		sign.initSign(_PriKey);
		sign.update(plainData);
		byte[] result = sign.sign();
		return result;
	}
	
	/**
	 * 使用公钥验证签名
	 * @param plainText 原文数据
	 * @param base64SignText 签名数据(base64)
	 * @param base64PublicKey 公钥钥(base64)
	 * @return boolean
	 */
	public static boolean verify(String plainText, String base64SignText, String base64PublicKey)throws Exception{
		if ( StringUtils.isEmpty(plainText) ) {
			throw new IllegalArgumentException("Input parameter plainText is invalid. plainText=" + plainText);
		}
		if ( StringUtils.isEmpty(base64SignText) ) {
			throw new IllegalArgumentException("Input parameter base64SignText is invalid. base64SignText=" + base64SignText);
		}
		if ( StringUtils.isEmpty(base64PublicKey) ) {
			throw new IllegalArgumentException("Input parameter base64PublicKey is invalid. base64PublicKey=" + base64PublicKey);
		}
		return verify(plainText.getBytes(), Base64.getDecoder().decode(base64SignText), Base64.getDecoder().decode(base64PublicKey));		
	}
	
	/**
	 * 使用公钥验证签名
	 * @param plainData 原文数据
	 * @param signData 签名数据
	 * @param base64PublicKey 公钥钥(base64)
	 * @return boolean
	 */
	public static boolean verify(byte[] plainData, byte[] signData, byte[] publicKey)throws Exception{
		if ( plainData == null ) {
			throw new IllegalArgumentException("Input parameter plainData is invalid. ");
		}
		if ( signData == null ) {
			throw new IllegalArgumentException("Input parameter signData is invalid. ");
		}
		if ( publicKey == null ) {
			throw new IllegalArgumentException("Input parameter publicKey is invalid. ");
		}

		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		PublicKey _PubKey = keyFactory.generatePublic(spec);
		Signature sign = Signature.getInstance(SIGNATURE_ALG);
		sign.initVerify(_PubKey);
		sign.update(plainData);
		return sign.verify(signData);		
	}
	
	public static boolean verifyCertificate(String plainText, String base64SignText, Certificate certificate)throws Exception{
		if ( StringUtils.isEmpty(plainText) ) {
			throw new IllegalArgumentException("Input parameter plainText is invalid. plainText=" + plainText);
		}
		if ( StringUtils.isEmpty(base64SignText) ) {
			throw new IllegalArgumentException("Input parameter base64SignText is invalid. base64SignText=" + base64SignText);
		}

		return verifyCertificate(plainText.getBytes(), Base64.getDecoder().decode(base64SignText), certificate);		
	}
	
	public static boolean verifyCertificate(byte[] plainData, byte[] signData, Certificate certificate)throws Exception{
		if ( plainData == null ) {
			throw new IllegalArgumentException("Input parameter plainData is invalid. ");
		}
		if ( signData == null ) {
			throw new IllegalArgumentException("Input parameter signData is invalid. ");
		}
		if ( certificate == null ) {
			throw new IllegalArgumentException("Input parameter publicKey is invalid. ");
		}
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		Signature sign = Signature.getInstance(SIGNATURE_ALG);
		sign.initVerify(certificate);
		sign.update(plainData);
		return sign.verify(signData);		
	}
	
	/**
	 * 使用私钥解密数据
	 * @param ciphertext 待解密数据
	 * @param privateKey RSA私钥
	 * @return 解密后的数据(base64编码)
	 * @throws Exception
	 */
	public static String decryptWithAsyAlgType(String Base64Ciphertext,int asyAlgType) throws Exception{
		if ( StringUtils.isEmpty(Base64Ciphertext) ) {
			throw new IllegalArgumentException("Input parameter Base64Ciphertext is invalid. Base64Ciphertext=" + Base64Ciphertext);
		}
		String result = null;
		if (asyAlgType == ASY_ALG_TYPE_RSA) {
			result = decryptWithPrivateKey(Base64Ciphertext, CaConfig.privateKey);
		} else if (asyAlgType == ASY_ALG_TYPE_SM9) {
			byte[] plainText = CipherManager.getInstance().getCipher(CipherAlgorithm.ALGORITHM_SM9_ID).decrypt(Base64.getDecoder().decode(Base64Ciphertext), null);
			return Base64.getEncoder().encodeToString(plainText);
		}		
		return result;
	}
	
	/**
	 * 生成简单的签名
	 * @param args
	 * @return
	 */
	public static String signature(Object... args) {
		List<String> ss = new ArrayList<String>();
		for (Object arg : args) {  
			if(arg instanceof String)
			{
				ss.add(arg.toString());
			}
			else
			{
				ss.add(String.valueOf(arg));
			}
		}  
		
		Collections.sort(ss);
		StringBuilder builder = new StringBuilder();
		for(String s : ss) {
			builder.append(s);
		}
		return DigestUtils.shaHex(builder.toString()).toLowerCase();
	}
}
