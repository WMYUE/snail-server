package com.snail.fitment.register.support;

/**
 * Wrapper异常，用于指示 {@link FailbackRegistry}跳过Failback。
 * <p>
 * NOTE: 期望找到其它更常规的指示方式。
 *
 * @author Dengyiping
 */
public class SkipFailbackWrapperException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8003247189738110617L;

	public SkipFailbackWrapperException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // do nothing
        return null;
    }
}
