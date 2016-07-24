package com.snail.fitment.common.cipher;

import java.util.Base64;

public class Base64Cipher implements ICipher {

	@Override
	public byte[] encrypt(byte[] plainText, byte[] key) throws Exception {
		return Base64.getEncoder().encode(plainText);
	}

	@Override
	public byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception {
		return Base64.getDecoder().decode(ciphertext);
	}

}
