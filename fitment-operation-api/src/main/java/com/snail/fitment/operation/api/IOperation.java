package com.snail.fitment.operation.api;

import java.util.Map;

import org.codehaus.jackson.type.JavaType;

import com.snail.fitment.model.OperationResult;

public interface IOperation {

	public byte getOperationType();

	public String getOperationName();

	public int getOpCode();

	public Map<String, JavaType> getParamDefine();

	public OperationResult execute(OperationContext context);
}
