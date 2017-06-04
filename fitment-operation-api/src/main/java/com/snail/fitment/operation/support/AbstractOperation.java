package com.snail.fitment.operation.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.common.exception.ExceptionOutput;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.common.profile.ProfileStatHolder;
import com.snail.fitment.model.ClientInfo;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.thread.ThreadContext;
import com.snail.fitment.operation.api.IOperation;
import com.snail.fitment.operation.api.IOperationManager;
import com.snail.fitment.operation.api.OperationContext;

public abstract class AbstractOperation implements IOperation {
	public static final Logger log = Logger.getLogger(AbstractOperation.class);

	/**
	 * 存放消息中参数的名称及类型定义
	 */
	protected final Map<String, JavaType> _paramDefine = new HashMap<String, JavaType>();

	private byte operationType;

	private String operationName;

	private int opCode;

	private String name;

	@Autowired
	private IOperationManager operationManager;

	// @Autowired
	// private ITriggerManager triggerManager;

	protected String getName() {
		return name;
	}

	/**
	 * 用于初始化名称的方法
	 */
	protected void initName(String opName, byte opType, int opCode) {
		this.operationType = opType;

		this.operationName = opName;

		this.opCode = opCode;

		this.name = new StringBuilder().append("Op-").append(opCode).append("-").append(opName).append(".")
				.append(opType).toString();

		// this.registerToOperationManager();
	}

	@PostConstruct
	/**
	 * 向 OperationManager 注册自己，确保自己可以处理相应的 blueprint协议数据包
	 */
	protected void registerToOperationManager() {
		operationManager.addOperation(this);
	}

	public static Map<String, Object> getObjParams(Map<String, Object> jsonParams, Map<String, JavaType> paramDefine) {
		Map<String, Object> paramList = new HashMap<String, Object>();
		Map<String, JavaType> paramDef = paramDefine;
		for (Entry<String, Object> jsonParam : jsonParams.entrySet()) {
			if (paramDef.containsKey(jsonParam.getKey())) {
				paramList.put(jsonParam.getKey(),
						JsonConvert.convertObject(jsonParam.getValue(), paramDef.get(jsonParam.getKey())));
			}
		}
		return paramList;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getParamValue(Map<String, Object> paramList, String name, T defaultValue) {
		T res = defaultValue;
		if (paramList.containsKey(name)) {
			res = (T) (paramList.get(name));
		}

		if (res instanceof String) {
			res = (T) ((String) res).trim();
		}

		return res;
	}

	// private void afterExec(OperationContext context, ITrigger[] triggers) {
	// for (ITrigger trigger : triggers) {
	// List<IAction> actions = trigger.getAfterActions();
	// for(IAction action:actions) {
	// if(!action.exec(context)) {
	// break;
	// }
	// }
	// }
	// }
	//
	// private void beforeExec(OperationContext context, ITrigger[] triggers) {
	// for (ITrigger trigger : triggers) {
	// List<IAction> actions = trigger.getBeforeActions();
	// for(IAction action:actions) {
	// if(!action.exec(context)) {
	// break;
	// }
	// }
	// }
	// }

	@Override
	public String getOperationName() {
		return operationName;
	}

	@Override
	public byte getOperationType() {
		return operationType;
	}

	@Override
	public int getOpCode() {
		return opCode;
	}

	@Override
	public Map<String, JavaType> getParamDefine() {

		return _paramDefine;
	}

	@Override
	public OperationResult execute(OperationContext context) {
		// List<ITrigger> triggerList = triggerManager.getTriggers(context,
		// this);
		OperationResult result = new OperationResult();
		// ITrigger[] triggers = new ITrigger[triggerList.size()];
		// triggers = triggerList.toArray(triggers);

		// beforeExec(context, triggers);
		Long executeTime = 0L;
		try {
			// 调用子类的方法进行实际处理
			Long startTime = System.currentTimeMillis();
			result = doExecute(context, result);
			executeTime = System.currentTimeMillis() - startTime;

		} catch (Exception exception) {
			// 异常，返回系统内部错误
			String exceptionInfo = ExceptionOutput.convert(exception);
			log.debug("Operation Execute ERROR, caused by : " + ExceptionOutput.convert(exception));
			if (result == null) {
				result = new OperationResult();
			}
			result.getResultList().put("stateCode", CommonStatusCode.COMMON_SYSTEM_ERROR);
			result.getResultList().put("stateDesc", exceptionInfo);

		} finally {
			// 其他操作，比如记录 operation 的性能统计数据
			ProfileStatHolder.saveOrUpdateCounter(this.getName(), executeTime);
		}

		// afterExec(context, triggers);
		return result;
	}

	/**
	 * 调用业务方法进行处理，不需要处理异常信息，由外围方法处理
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	abstract protected OperationResult doExecute(OperationContext context, OperationResult result) throws Exception;

	protected void putThreadContext(OperationContext operationContext, String loginName, ClientInfo clientInfo) {
		String clientNativeId = clientInfo == null ? null : clientInfo.getClientNativeId();
		ThreadContext.setSessionInfo(loginName, clientNativeId);
	}

}
