package com.snail.fitment.operation.support;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.fitment.api.session.ISessionManager;
import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.common.lang.StringUtils;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.SessionInfo;
import com.snail.fitment.operation.api.OperationContext;

public abstract class AbstractSessionAwareOperation extends AbstractOperation {

	private static final Logger log = Logger.getLogger(AbstractSessionAwareOperation.class);

	@Autowired
	private ISessionManager sessionManager;

	@Override
	protected OperationResult doExecute(OperationContext context, OperationResult result) throws Exception {
		// 从参数列表中取得 sessionId 参数，目前 sessionId 的参数名称均为 "sessionId"
		Map<String, Object> paramList = context.getReqParams();
		String sessionId = this.getSessionId(paramList);

		SessionInfo session = null;
		if (StringUtils.isEmpty(sessionId)) {
			log.debug("AbstractSessionAwareOperation, param error,  sessionId is empty!");
			result.getResultList().put("stateCode", CommonStatusCode.SESSIONINFO_NOT_EXISTED_ERROR);
			result.getResultList().put("stateDesc",
					CommonStatusCode.desc(CommonStatusCode.SESSIONINFO_NOT_EXISTED_ERROR));
			return result;
		}

		session = sessionManager.getSession(sessionId);
		if (session == null) {
			log.debug("AbstractSessionAwareOperation, session is empty!");
			result.getResultList().put("stateCode", CommonStatusCode.SESSIONINFO_NOT_EXISTED_ERROR);
			result.getResultList().put("stateDesc",
					CommonStatusCode.desc(CommonStatusCode.SESSIONINFO_NOT_EXISTED_ERROR));
			return result;
		} else {
			context.setSession(session);
		}

		// 调用子类的方法进行实际处理
		result = innerExecute(context, result);
		return result;
	}

	/**
	 * 从参数列表中取得 sessionId 参数，若子类中操作中 sessionId 的参数名称不为 "sessionId" ，则需要覆盖此方法
	 * 
	 * @param paramList
	 * @return
	 */
	protected String getSessionId(Map<String, Object> paramList) {
		return getParamValue(paramList, "sessionId", "0");
	}

	/**
	 * 从参数列表中取得transactionId
	 * 
	 * @param paramList
	 * @return
	 */
	protected String getTransactionId(Map<String, Object> paramList) {
		return getParamValue(paramList, "transactionId", "");
	}

	/**
	 * 子类中进行实际处理的方法，不需要再判断 sessionId 与 connectionId 对应关系的正确性
	 *
	 * @param context
	 * @param result
	 * @return
	 */
	abstract protected OperationResult innerExecute(OperationContext context, OperationResult result) throws Exception;

}
