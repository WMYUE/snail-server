package com.snail.fitment.protocol.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.common.utils.HttpUtils;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.vo.RequestContext;
import com.snail.fitment.protocol.bpackage.BpPackage;

@Component("baseStubHandler")
public class BaseStubHandler implements IHandler {

	private static final Logger log = Logger.getLogger(BaseStubHandler.class);

	@Autowired
	private NotifyConnectionHandler notifyConnectionHandler;

	@Override
	public void handle(HandleContext context) {
		try {

			switch (context.getSrcPackage().getBpHeader().getStackVersion()) {
			case 1:
				handleRequest(context);
				break;
			default:
				context.getResponse().getBpBody().setBodyString(this.getNotSupportResponseString("不支持的版本"));
			}

		} catch (Exception ex) {
			log.error("request " + context.getSrcPackage() + " execute exception: ", ex);
		}

		notifyConnectionHandler.handle(context);
	}

	@Override
	public void setNextHandle(IHandler handler) {
		// this.notifyConnectionHandler = handler;
	}

	private void handleRequest(HandleContext context) {
		BpPackage pkg = (BpPackage) context.getSrcPackage();

		RequestContext reqcontext = new RequestContext(context.getConnectionId(), context.getClientAddress(),
				pkg.getBpHeader().getMessageType(), pkg.getBpHeader().getOpCode(), pkg.getBpBody().getBodyString(),
				pkg.getBpBody().getAttachment());

		try {
			String res = HttpUtils.postWithJSON(FitmentConfig.proxyUrl, JsonConvert.SerializeObject(reqcontext));
			OperationResult opResult = JsonConvert.DeserializeObject(res, OperationResult.class);
			int stateCode = opResult.getResultList() != null && opResult.getResultList().get("stateCode") != null
					? (Integer) opResult.getResultList().get("stateCode") : 0;

			if (stateCode != 0) {
				log.debug("process operation error, stateCode = " + stateCode + ", opCode = " + reqcontext.getOpCode());
			}

			if (opResult != null) {
				if (context.getResponse() != null) {
					context.getResponse().addResponseOperation(reqcontext.getOpCode(), opResult.getResultList());
				}
			}
		} catch (Throwable e) {// 异常处理
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("stateCode", 404);
			paramMap.put("stateDesc", e.getMessage());
			context.getResponse().getBpBody().setBodyString(JsonConvert.SerializeObject(paramMap));
		}
	}

	private String notSupportResponseString = null;

	private final String getNotSupportResponseString(String desc) {
		if (notSupportResponseString == null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("stateCode", 504);
			paramMap.put("stateDesc", desc);
			notSupportResponseString = JsonConvert.SerializeObject(paramMap);
		}
		return notSupportResponseString;
	}
}
