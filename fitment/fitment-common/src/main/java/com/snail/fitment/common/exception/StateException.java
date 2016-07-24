package com.snail.fitment.common.exception;


public class StateException extends RuntimeException {

	private static final long serialVersionUID = 6807878409053506677L;

	
	private int stateCode;

	public StateException(Throwable t)
	{
		super(t);
		this.stateCode = 101;
	}
	
	public StateException(String msg)
	{
		this(101,msg);
	}
	
	public StateException(String msg, Throwable t)
	{
		this(101,msg,t);
	}
	
	public StateException(int code, String desc) {
		super(desc);
		stateCode = code;
	}

	public StateException(int code, String desc, Throwable t) {
		super(desc, t);
		stateCode = code;
	}
	
	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [stateCode=" + stateCode + ", stateDesc="
				+ getLocalizedMessage() + "]";
	}
	
	
}
