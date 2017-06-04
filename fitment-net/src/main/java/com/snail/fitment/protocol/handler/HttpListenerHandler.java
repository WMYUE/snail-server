package com.snail.fitment.protocol.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.model.SynchType;
import com.snail.fitment.protocol.bpackage.BpPackage;
import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.connection.BpConnection;
import com.snail.fitment.protocol.connection.ConnectionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@Component("httpListenerHandler")
public class HttpListenerHandler implements HttpHandler {
	private static final Logger log = Logger.getLogger(HttpListenerHandler.class);
	
	@Autowired
	private ConnectionManager manager;
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		InputStream in = arg0.getRequestBody();
		byte[] buffer = new byte[in.available()];
		in.read(buffer, 0, in.available());
		String jsonStr = new String(buffer);
		if(log.isDebugEnabled()) {
			log.debug("HttpListenerHandler receive:"+jsonStr);
		}
		Map<?,?> value = JsonConvert.DeserializeObject(jsonStr);
		String userUniId=(String) value.get("userUniId");
		Integer synchType = (Integer) value.get("synchType");
		Object data = value.get("data");
		
		if(userUniId != null) {
			Set<BpConnection> connections =manager.getNotifyConnection(userUniId);
			if(connections != null) {
				for(BpConnection connection:connections) {
					BpPackage pkg =BpPackageFactory.createEmptyPackage(IBpHeader.MESSAGE_TYPE_REQUEST, connection.getSeq(), (byte)0, (byte)0);
					pkg.getBpBody().setBodyString(SynchType.getSynchType(synchType,data,null));
					pkg.getBpHeader().setOpCode(OperationConstants.OP_SYNCH_BATCH);
					manager.sendBpPackage(connection.getConnectionId(), pkg);
				}
			}
		}
		
		
		String response=  "ok";
		if(log.isDebugEnabled()) {
			log.debug("HttpListenerHandler send:"+response);
		}
		arg0.sendResponseHeaders(200, response.getBytes().length);   
		OutputStream os = arg0.getResponseBody();   
		os.write(response.getBytes());   
		os.close();  
	}

}
