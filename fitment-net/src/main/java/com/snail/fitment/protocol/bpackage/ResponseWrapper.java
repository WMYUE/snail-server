package com.snail.fitment.protocol.bpackage;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ResponseWrapper {

	private final String requestSeq;
    private IBpPackage response;
    private final ReentrantLock lock;
    private final Condition waitResp;
    
    public ResponseWrapper(String requestSeq) {
        this.requestSeq=requestSeq;
        this.lock = new ReentrantLock(false);
        this.waitResp = lock.newCondition();
    }
    /**
     * 唤醒响应等待
     */
    public void signal() {
        lock.lock();
        try {
            waitResp.signal();
        } finally {
            lock.unlock();
        }
    }
	public IBpPackage getResponse() {
		return response;
	}
	public void setResponse(IBpPackage response) {
		this.response = response;
	}
	public String getRequestSeq() {
		return requestSeq;
	}
	public Condition getWaitResp() {
		return waitResp;
	}
	
	@Override
	public String toString() {
		return "ResponseWrapper [requestSeq=" + requestSeq + ", response="
				+ response + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestSeq == null) ? 0 : requestSeq.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponseWrapper other = (ResponseWrapper) obj;
		if (requestSeq == null) {
			if (other.requestSeq != null)
				return false;
		} else if (!requestSeq.equals(other.requestSeq))
			return false;
		return true;
	}
	public ReentrantLock getLock() {
		return lock;
	}
    
    
}
