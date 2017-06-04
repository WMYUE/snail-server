package com.snail.fitment.model.vo;

public class ValueObject {
	private String id;
	
	private Object value;

	  public String getId() {
	    return id;
	  }
	
	  public Object getValue() {
	    return value;
	  }
	
	  public void setValue(Object value) {
	    this.value = value;
	  }
	
	  @Override
	  public String toString() {
	    return "ValueObject [id=" + id + ", value=" + value + "]";
	  }
}
