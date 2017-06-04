package com.snail.fitment.protocol.handler;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.exception.ExceptionOutput;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.operation.api.IOperation;
import com.snail.fitment.operation.api.IOperationManager;
import com.snail.fitment.operation.api.OperationContext;
import com.snail.fitment.operation.support.AbstractOperation;
import com.snail.fitment.protocol.bpackage.BpPackage;
import com.snail.fitment.protocol.bpackage.IBpHeader;

@Component("baseHandler")
public class BaseHandler implements IHandler {

	private static final Logger log = Logger.getLogger(BaseHandler.class);
	
	@Autowired
	private IOperationManager operationManager;
	
	@Autowired
	private IHandler notifyConnectionHandler;
	
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
			
			//源数据包是请求包，无特殊响应码且响应包无内容，说明不支持对应操作（能够处理的消息响应都会有内容），响应状态码设置为 11
			if (context.getSrcPackage().getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST 
					&& context.getResponse().getBpHeader().getStatusCode() == 0 && !context.getResponse().getBpBody().hasContent()) {
				context.getResponse().getBpHeader().setStatusCode((byte)11); 
	        	context.getResponse().getBpBody().setBodyString("");

				log.info("no response content! srcPkg: " + context.getSrcPackage());

			}
			
		} catch (Exception ex) {
			log.error("request "+context.getSrcPackage()+" execute exception: ",ex);
			Map<String,Object> paramMap= new HashMap<String,Object>();
			paramMap.put("stateCode", 404);
			paramMap.put("stateDesc", ExceptionOutput.convert(ex));
			context.getResponse().getBpBody().setBodyString(JsonConvert.SerializeObject(paramMap));
		}

		notifyConnectionHandler.handle(context);
	}

	

	@Override
	public void setNextHandle(IHandler handler) {
		this.notifyConnectionHandler = handler;
	}

	private void handleRequest(HandleContext context) {
		BpPackage pkg = (BpPackage)context.getSrcPackage();

		IOperation operation = operationManager.getOperationByOpCode(pkg.getBpHeader().getMessageType(), pkg.getBpHeader().getOpCode());

		if (operation == null) {
			log.warn("ignore operation, opCode: " + pkg.getBpHeader().getOpCode());
			context.getResponse().getBpBody().setBodyString(this.getNotSupportResponseString("不支持的操作"));
			context.getResponse().getBpBody().parseBpBody();
			return;
		}
		
		Map<String, Object> jsonParams = ((BpPackage)pkg).getBpBody().getParams();
		
		// process the operation
		this.processOperation(context, operation, jsonParams, pkg.getBpHeader().getMessageType() == IBpHeader.MESSAGE_TYPE_REQUEST);
	}

	private void processOperation(HandleContext context, IOperation operation, Map<String, Object> jsonParams, boolean isRequest) {
		OperationContext opcontext = new OperationContext();
		opcontext.setAttachmentBytes(context.getSrcPackage().getBpBody().getAttachment());//传递附件
		opcontext.setReqParams(AbstractOperation.getObjParams(jsonParams, operation.getParamDefine()));
		
        OperationResult opResult = operation.execute(opcontext);
        
        int stateCode = opResult.getResultList() != null && opResult.getResultList().get("stateCode") != null ?
        		(Integer)opResult.getResultList().get("stateCode") : 0;
        		
        if(stateCode!=0){
        	log.debug("process operation error, stateCode = " + stateCode + ", opCode = " +  operation.getOpCode());
        }
        
        if (opResult != null) {
			// 保存要发送给本连接的响应数据
        	//response请求无需再response给客户端，因此无需下面步骤.
        	if(context.getResponse()!=null){
        		context.getResponse().addResponseOperation(operation.getOpCode(), opResult.getResultList());
        		//context.getResponse().getBpBody().setAttachment(opcontext.getAttachmentBytes());   //modified by dyp, 2016-10-20, avoid return attchment
        	}
        	
			// 保存处理过程中触发的要发送给其他连接的请求
			//context.addRequestList(opResult.getRequestList());
		}
    }
    
	
	private String notSupportResponseString=null;
	private final String getNotSupportResponseString(String desc) {
		if(notSupportResponseString == null) {
			Map<String,Object> paramMap= new HashMap<String,Object>();
			paramMap.put("stateCode", 504);
			paramMap.put("stateDesc", desc);
			notSupportResponseString= JsonConvert.SerializeObject(paramMap);
		}
		return notSupportResponseString;
	}
}
