package com.snail.fitment.operation.sessionmanager;

import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.ISessionManager;
import com.snail.fitment.api.session.ITokenManager;
import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.common.security.OAuthUtil;
import com.snail.fitment.model.Auth2TokenInfo;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.SessionInfo;
import com.snail.fitment.model.request.ReturnResult;
import com.snail.fitment.operation.api.OperationContext;
import com.snail.fitment.operation.support.AbstractOperation;

@Lazy(false)
@Component("authBySessionInfoOperation")
public class AuthBySessionInfoOperation extends AbstractOperation {

	@Autowired
	private ITokenManager tokenManager;

	@Autowired
	private ISessionManager sessionManager;

	public AuthBySessionInfoOperation() {
		super.initName("authBySessionInfoSession", OperationConstants.TYPE_REQ, OperationConstants.OP_AUTH_BY_SESSION);

		_paramDefine.put("signature", TypeFactory.fastSimpleType(String.class));
		_paramDefine.put("sessionId", TypeFactory.fastSimpleType(String.class));
		_paramDefine.put("nonce", TypeFactory.fastSimpleType(String.class));
	}

	@Override
	protected OperationResult doExecute(OperationContext context, OperationResult result) throws Exception {
		Map<String, Object> paramList = context.getReqParams();
		String signature = getParamValue(paramList, "signature", "");
		String sessionId = OAuthUtil.decodeSessionId(getParamValue(paramList, "sessionId", ""));
		String nonce = getParamValue(paramList, "nonce", "");

		SessionInfo info = sessionManager.getSession(sessionId);
		if (info != null) {
			String timestamp = String.valueOf(info.getUpdateTime());
			if (signature.equals(OAuthUtil.signature(info.getUserUniId(), timestamp, nonce, sessionId))) {
				Auth2TokenInfo tokenInfo = tokenManager.getToken(info.getUserUniId(), sessionId);
				if (tokenInfo == null) {
					tokenInfo = new Auth2TokenInfo();
					tokenInfo.setAccessToken(UUID.randomUUID().toString());
					tokenInfo.setSessionInfo(info);
				}
				tokenManager.saveToken(tokenInfo);
				ReturnResult<String> returnResult = ReturnResult.getSuccessInstance();

				result.getResultList().put("stateCode", returnResult.getCode());
				result.getResultList().put("stateDesc", returnResult.getDesc());
				result.getResultList().put("sessionId", info.getSessionId());
				result.getResultList().put("accessToken", tokenInfo.getAccessToken());
				return result;
			}
		}

		result.getResultList().put("stateCode", CommonStatusCode.COMMON_SERVER_ERROR);
		result.getResultList().put("stateDesc", "认证失败");
		return result;
	}

}
