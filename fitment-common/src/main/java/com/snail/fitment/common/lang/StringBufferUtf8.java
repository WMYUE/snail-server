package com.snail.fitment.common.lang;

import java.io.Serializable;
import java.nio.charset.Charset;

public class StringBufferUtf8 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4795007705843588805L;

	public static Charset UTF8 = Charset.forName("utf-8");
	
	private final String _strValue;
	private final byte[] _buffer;
	private final int _length;

	public StringBufferUtf8(String strValue) {
		strValue = strValue == null ? "" : strValue;
		_buffer = strValue.getBytes(UTF8);
		_length = _buffer.length;
		_strValue = strValue;
	}

	public StringBufferUtf8(byte[] buffer) {
		if (buffer == null) buffer = new byte[0];
		_buffer = buffer;
		_length = _buffer.length;
		_strValue = new String(buffer,UTF8);
	}

	public String getStrValue() {
		return _strValue;
	}

	public byte[] getBuffer() {
		return _buffer;
	}

	public int getLength() {
		return _length;
	}

	@Override
	public String toString() {
		return "StringBufferUtf8 [_strValue=" + _strValue + "]";
	}
}
