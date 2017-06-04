package com.snail.fitment.model.vo;

import java.util.HashSet;
import java.util.Set;

public class BehaviorHandleNode {
	
	private int num = 0;
	
	//监管人
	private Set<String> supervisor;
	
	//负责人
	private String responser;
	
	//协作人
	private Set<String> cooperator;
	
	//执行人
	private Set<String> executor;	
	
	public BehaviorHandleNode(){
		
	}

	
	public Set<String> getViewers() {
		Set<String> viewers = new HashSet<>();
		if(executor != null) {
			viewers.addAll(executor);
		}
		if(cooperator != null) {
			viewers.addAll(cooperator);
		}
		if(supervisor != null) {
			viewers.addAll(supervisor);
		}
		if(responser != null) {
			viewers.add(responser);
		}
		return viewers;
	}
	
	
	public BehaviorHandleNode(int num){
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Set<String> getSupervisor() {
		if(supervisor == null) return new HashSet<>();
		return supervisor;
	}

	public void setSupervisor(Set<String> supervisor) {
		this.supervisor = supervisor;
	}

	public String getResponser() {
		return responser;
	}

	public void setResponser(String responser) {
		this.responser = responser;
	}

	public Set<String> getCooperator() {
		if(cooperator == null) return new HashSet<>();
		return cooperator;
	}

	public void setCooperator(Set<String> cooperator) {
		this.cooperator = cooperator;
	}

	public Set<String> getExecutor() {
		if(executor == null) return new HashSet<>();
		return executor;
	}

	public void setExecutor(Set<String> executor) {
		this.executor = executor;
	}

	@Override
	public String toString() {
		return "BehaviorHandleNode [num=" + num + ", supervisor=" + supervisor + ", responser=" + responser
				+ ", cooperator=" + cooperator + ", executor=" + executor + "]";
	}
}
