package com.snail.fitment.operation.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.operation.api.IOperation;
import com.snail.fitment.operation.api.IOperationManager;

public abstract class AbstractOperationManager implements IOperationManager {
	protected static final Logger log = Logger.getLogger(AbstractOperationManager.class);
	
	protected final Map<Byte, Map<Integer, IOperation>> operationByMsgTypeOpCode = new HashMap<Byte, Map<Integer, IOperation>>();

	protected final Map<String, Integer> opCodeByName = new HashMap<String, Integer>();
	protected final Map<Integer, String> opNameByCode = new HashMap<Integer, String>();
	
	public AbstractOperationManager() {
		super();
		operationByMsgTypeOpCode.put(OperationConstants.TYPE_REQ, new HashMap<Integer, IOperation>());
		operationByMsgTypeOpCode.put(OperationConstants.TYPE_RES, new HashMap<Integer, IOperation>());	
		initOperationCode();
	}

	protected abstract void initOperationCode();
	
	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#getOpCodeByName()
	 */
	@Override
	public Map<String, Integer> getOpCodeByName() {
		return opCodeByName;
	}

	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#getOpNameByCode()
	 */
	@Override
	public Map<Integer, String> getOpNameByCode() {
		return opNameByCode;
	}


	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#getOpCodeByName(java.lang.String)
	 */
	@Override
	public int getOpCodeByName(String operationName) {
		return opCodeByName.get(operationName);
	}
	
	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#getOpNameByCode(int)
	 */
	@Override
	public String getOpNameByCode(int opCode) {
		return opNameByCode.get(opCode);
	}
	
	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#getOperationByOpCode(byte, int)
	 */
	@Override
	public IOperation getOperationByOpCode(byte msgType, int opCode) {
		IOperation op = operationByMsgTypeOpCode.get(msgType).get(opCode);
		if (log.isTraceEnabled()) {
			log.trace("getOperationByOpCode - msgType: " + msgType + ", opCode: " + opCode + ", op: " + op);
		}
		return op;
	}
	
	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#getOperationByName(java.lang.String, java.lang.String)
	 */
	@Override
	public IOperation getOperationByName(String operationName, 
			byte actionType) {
		if(opCodeByName.containsKey(operationName)){ //判断非空
			int opCode = opCodeByName.get(operationName);
			IOperation op = operationByMsgTypeOpCode.get(actionType).get(opCode);
			return op;
		}
		
		return null;
	}
	
	
	protected void addOpNameCode(String opName, int opCode) {
		
		log.debug(this.getClass().getSimpleName() + ".addOpNameCode - opName: " + opName + ", opCode: " + opCode);

		this.opCodeByName.put(opName, opCode);
		this.opNameByCode.put(opCode, opName);
	}

	
	/* (non-Javadoc)
	 * @see com.comisys.lanxin.blueprint.databus.operation.IOperationManager#addOperation(com.comisys.lanxin.blueprint.databus.operation.IOperation)
	 */
	@Override
	public void addOperation(IOperation op) {
		if (log.isInfoEnabled()) {
			log.info(this.getClass().getSimpleName() + ".addOperation - Operation name: "
					+  op.getOperationName() 
					+ ", opCode: " + op.getOpCode());
		}
		
		// add into opCode Maps
		this.addOpNameCode(op.getOperationName(), op.getOpCode());
		
		// add into map by opCode
		operationByMsgTypeOpCode.get(op.getOperationType()).put(op.getOpCode(), op);
		
	}
}
