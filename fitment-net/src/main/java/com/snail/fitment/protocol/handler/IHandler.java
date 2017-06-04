package com.snail.fitment.protocol.handler;

public interface IHandler {
	public void handle(HandleContext context);

	public void setNextHandle(IHandler handler);
}
