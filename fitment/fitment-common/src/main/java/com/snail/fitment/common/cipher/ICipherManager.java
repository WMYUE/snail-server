package com.snail.fitment.common.cipher;

public interface ICipherManager {
	
	public ICipher getCipher(String algorithm);
	
	public ICipher getCipher(int cipherType);

}
