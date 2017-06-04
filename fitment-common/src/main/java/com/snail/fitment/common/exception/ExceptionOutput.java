package com.snail.fitment.common.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionOutput {
	public static String convert(Exception exception) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));
		String exceptionInfo = baos.toString();
		return exceptionInfo;
	}
}
