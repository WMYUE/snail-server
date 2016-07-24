package com.snail.fitment.common.cipher;

import org.apache.commons.lang.StringUtils;

public class CipherManager implements ICipherManager {

	private final static ICipherManager cipherManager = new CipherManager();
	
	private CipherManager(){
	}
	
	public static ICipherManager getInstance(){
		return cipherManager;
	}
	
	@Override
	public ICipher getCipher(String algorithm) {
		
		if(StringUtils.equals(algorithm, ICipher.CIPHER_ALGORITHM_BASE64)){
			return new Base64Cipher();
		}
		
		else if(StringUtils.equals(algorithm, ICipher.CIPHER_ALGORITHM_AES)){
			return new AESCipher();
		}
		
		else{
			
		}
		
		return new Base64Cipher();
	}

	@Override
	public ICipher getCipher(int cipherType) {
		if(cipherType < CipherAlgorithm.ALGORITHM_AES_ID){
			return new Base64Cipher();
		}
		
		else {
			return new AESCipher();
		}
	}
	
	
}
