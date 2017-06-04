package com.snail.fitment.operation.sessionmanager;

import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.common.config.CaConfig;
import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.common.security.SecurityUtil;
import com.snail.fitment.model.ClientInfo;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.request.ReturnResult;
import com.snail.fitment.operation.api.OperationContext;
import com.snail.fitment.operation.support.AbstractOperation;

/**
 * 初始化连接
 * @date 2014-5-30
 */
@Lazy(false) @Component
public class InitConnectionRequestOperation extends AbstractOperation {
	
	private static final Logger log = Logger.getLogger(InitConnectionRequestOperation.class);
	
	/**
	 * 
	 */
	public InitConnectionRequestOperation() {
		
		super.initName("InitConnection", OperationConstants.TYPE_REQ, OperationConstants.OP_INIT_CONNECTION);
		
		_paramDefine.put("clientInfo", TypeFactory.fastSimpleType(ClientInfo.class));
		_paramDefine.put("initSeq", TypeFactory.fastSimpleType(String.class));
	}

	@Override
	protected OperationResult doExecute(OperationContext context, OperationResult result) throws Exception {
		
		ClientInfo clientInfo = getParamValue(context.getReqParams(), "clientInfo", null);
		String initSeq = getParamValue(context.getReqParams(), "initSeq", "");
		
		if (log.isDebugEnabled()) {
			log.debug("GetRandomRequestOperation - begin , initSeq: " + initSeq);
		}

		//initSeq 户端生成随机且复杂的随机数，再使用客户端公钥加密后的base64值
		ReturnResult returnResult = ReturnResult.getSuccessInstance();
        try {
        	//使用私钥，解密密码
        	initSeq = SecurityUtil.decryptWithAsyAlgType(initSeq, clientInfo == null ? 0 : clientInfo.getAsyAlgType());
        	initSeq = new String(Base64.getDecoder().decode(initSeq.getBytes()));
        	String secretKey = Base64.getEncoder().encodeToString(DigestUtils.md5(initSeq));
        	
        	//将securityKey设置到Connect对象，用于协议包体的解密
        	result.getResultList().put("secretKey",secretKey);
        	
		} catch (IllegalArgumentException e) {
			log.error("initConnection error: ", e);
			returnResult.setCode(CommonStatusCode.COMMON_REQUEST_PARAM_ERROR);
		} catch (Throwable e) {
			log.error("initConnection error: ", e);
			returnResult.setCode(CommonStatusCode.COMMON_CERTIFICATE_IS_INVALID);
			result.getResultList().put("certSerialNo", CaConfig.serialNumber);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("GetRandomRequestOperation - end, returnResult: " + returnResult);
		}
		
		result.getResultList().put("stateCode", returnResult.getCode());
		result.getResultList().put("stateDesc", returnResult.getReturnContent());
		
		return result;
	}

}