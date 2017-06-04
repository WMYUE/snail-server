package com.snail.fitment.operation.sessionmanager;

import java.util.Map;

import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.IAuthManager;
import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.SessionInfo;
import com.snail.fitment.model.request.ReturnResult;
import com.snail.fitment.operation.api.OperationContext;
import com.snail.fitment.operation.support.AbstractSessionAwareOperation;

@Lazy(false)
@Component("logoutOperation")
public class LogoutOperation extends AbstractSessionAwareOperation {

	@Autowired
	private IAuthManager defaultAuthManager;

	public LogoutOperation() {
		super.initName("Logout", OperationConstants.TYPE_REQ, OperationConstants.OP_SESSION_LOGOUT);

		_paramDefine.put("token", TypeFactory.fastSimpleType(String.class));
		_paramDefine.put("sessionId", TypeFactory.fastSimpleType(String.class));
	}

	@Override
	protected OperationResult innerExecute(OperationContext context,
			OperationResult result) throws Exception {
		Map<String, Object> paramList = context.getReqParams();
		String token = getParamValue(paramList, "token", null);
		
		SessionInfo info = defaultAuthManager.logout(context.getSession().getSessionId(), token);
		
		if (info != null) {
			ReturnResult returnResult = ReturnResult.getSuccessInstance();
			result.getResultList().put("stateCode", returnResult.getCode());
			result.getResultList().put("stateDesc", returnResult.getDesc());
		} else {
			result.getResultList().put("stateCode", CommonStatusCode.COMMON_SERVER_ERROR);
			result.getResultList().put("stateDesc", "session不存在");
			return result;
		}
		
		return result;
	}

}
