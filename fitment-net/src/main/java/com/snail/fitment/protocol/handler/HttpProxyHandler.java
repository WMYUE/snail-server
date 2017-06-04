package com.snail.fitment.protocol.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.model.OperationResult;
import com.snail.fitment.model.vo.RequestContext;
import com.snail.fitment.operation.api.IOperation;
import com.snail.fitment.operation.api.IOperationManager;
import com.snail.fitment.operation.api.OperationContext;
import com.snail.fitment.operation.support.AbstractOperation;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@Component("httpProxyHandler")
public class HttpProxyHandler implements HttpHandler {
	private static final Logger log = Logger.getLogger(HttpProxyHandler.class);
	
	@Autowired
	private IOperationManager operationManager;
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		InputStream in = arg0.getRequestBody();
		byte[] buffer = new byte[in.available()];
		in.read(buffer, 0, in.available());
		String jsonStr = new String(buffer);
		if(log.isDebugEnabled()) {
			log.debug("proxyreceive:"+jsonStr);
		}
		RequestContext context=JsonConvert.DeserializeObject(jsonStr, RequestContext.class);
		OperationResult opResult= OperationResult.getDefaultSuccessResult();
		IOperation operation = operationManager.getOperationByOpCode(context.getMsgType(), context.getOpCode());
		if(operation != null) {
			OperationContext opcontext = new OperationContext();
			opcontext.setAttachmentBytes(context.getRequestAttachment());//传递附件
			opcontext.setReqParams(AbstractOperation.getObjParams((Map)JsonConvert.DeserializeGdpObject(context.getRequestBody()), operation.getParamDefine()));
			opResult = operation.execute(opcontext);
		} else {
			opResult.getResultList().put(OperationResult.resultCodeKey, 504);
		}
        
		String response=  JsonConvert.SerializeObject(opResult);
		if(log.isDebugEnabled()) {
			log.debug("proxysend:"+response);
		}
		byte[] resbytes= response.getBytes();
		arg0.sendResponseHeaders(200, resbytes.length);   
		OutputStream os = arg0.getResponseBody();   
		os.write(resbytes);  
		os.flush();
		os.close();  
	}

}
