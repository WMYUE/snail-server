package com.snail.fitment.operation.api;

import java.util.Map;

public interface IOperationManager {

	Map<String, Integer> getOpCodeByName();

	Map<Integer, String> getOpNameByCode();

	int getOpCodeByName(String operationName);

	String getOpNameByCode(int opCode);

	IOperation getOperationByOpCode(byte msgType, int opCode);

	IOperation getOperationByName(String operationName, byte actionType);

	void addOperation(IOperation op);

}