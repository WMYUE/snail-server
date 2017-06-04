package com.snail.fitment.protocol.handler;


import com.snail.fitment.protocol.bpackage.IBpPackage;

public interface HandleContext{

	public String getConnectionId();
	
	public String getClientAddress();
	
	public IBpPackage getSrcPackage();

	public IBpPackage getResponse();
	
	public IBpPackage getRequest();
	
	public void addResponseAttachment(byte[] attachment);
	
	public byte[] getResponseAttachment();
}
