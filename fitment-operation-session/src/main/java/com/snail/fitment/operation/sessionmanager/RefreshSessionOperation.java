package com.snail.fitment.operation.sessionmanager;

import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.request.ReturnResult;
import com.snail.fitment.operation.api.OperationContext;
import com.snail.fitment.operation.support.AbstractSessionAwareOperation;

@Lazy(false)
@Component("refreshSessionOperation")
public class RefreshSessionOperation extends AbstractSessionAwareOperation {

	public RefreshSessionOperation() {
		super.initName("RefreshSession", OperationConstants.TYPE_REQ, OperationConstants.OP_SESSION_REFRESH);
		_paramDefine.put("sessionId", TypeFactory.fastSimpleType(String.class));
	}

	@Override
	protected OperationResult innerExecute(OperationContext context, OperationResult result) throws Exception {
		ReturnResult<String> returnResult = ReturnResult.getSuccessInstance();
		result.getResultList().put("stateCode", returnResult.getCode());
		result.getResultList().put("stateDesc", returnResult.getDesc());

		return result;
	}

}
