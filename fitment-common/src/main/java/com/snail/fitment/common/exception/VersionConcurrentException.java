package com.snail.fitment.common.exception;

/**
 * 发送通知失败异常
 * 
 * @author chenlei
 *
 */
public class VersionConcurrentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public VersionConcurrentException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionConcurrentException(String message) {
		super(message);
	}

	public VersionConcurrentException(Throwable cause) {
		super(cause);
	}
}
