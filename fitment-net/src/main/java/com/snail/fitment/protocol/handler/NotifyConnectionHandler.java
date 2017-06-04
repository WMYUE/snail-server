package com.snail.fitment.protocol.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.model.SessionInfo;
import com.snail.fitment.protocol.bpackage.BpPackage;
import com.snail.fitment.protocol.connection.BpConnection;
import com.snail.fitment.protocol.connection.ConnectionManager;

@Component("notifyConnectionHandler")
public class NotifyConnectionHandler implements IHandler  {

	@Autowired
	private ConnectionManager connectionManager;
	
	@Override
	public void handle(HandleContext context) {
		BpPackage pkg = (BpPackage)context.getResponse();
		Map<String, Object> jsonParams = ((BpPackage)pkg).getBpBody().getParams();
		if(jsonParams.containsKey("session")) {
			Object temp=jsonParams.remove("session");
			SessionInfo session =null;
			if(temp instanceof SessionInfo) {
				session = (SessionInfo)temp;
			} else {
				session = (SessionInfo)JsonConvert.convertObject(temp, SessionInfo.class);
			}
			if(FitmentConfig.isTestServer) {
				jsonParams.put("session", session);
			}
			pkg.addResponseOperation(pkg.getBpHeader().getOpCode(), jsonParams);
			BpConnection connection=connectionManager.getBpConnection(context.getConnectionId());
			connection.bindSession(session);
		} else if(pkg.getBpHeader().getOpCode() == OperationConstants.OP_INIT_CONNECTION) {
			String secretKey= (String) jsonParams.get("secretKey");
			if(secretKey != null) {
				jsonParams.remove("secretKey");
			}
			pkg.addResponseOperation(pkg.getBpHeader().getOpCode(), jsonParams);
			BpConnection connection=connectionManager.getBpConnection(context.getConnectionId());
			connection.setSecretKey(secretKey);
		}
	}

	@Override
	public void setNextHandle(IHandler handler) {
		// TODO Auto-generated method stub
		
	}

}
