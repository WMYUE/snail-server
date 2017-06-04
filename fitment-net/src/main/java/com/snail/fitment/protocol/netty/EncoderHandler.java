package com.snail.fitment.protocol.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.connection.BpConnection;
import com.snail.fitment.protocol.connection.ConnectionManager;

@Component("encoderHandler")
public class EncoderHandler extends ProtobufEncoder {
	private static final Logger log = Logger.getLogger(EncoderHandler.class);
	
	@Autowired 
	private ConnectionManager defaultConnectionManager;
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {

		if (!(msg instanceof IBpPackage)) {
	        return msg;
		}
		
		IBpPackage bpPackage = (IBpPackage)msg;
		BpConnection connection=defaultConnectionManager.getBpConnection(channel.getId());
		String secretKey=null;
		if(connection != null) {
			secretKey = connection.getSecretKey();
		}
		bpPackage.encode(secretKey);
		int bufferLength = bpPackage.getPackageBuffer() == null ? 0 : bpPackage.getPackageBuffer().length;		
		log.debug("EncoderHandler, bufferLength =" + bufferLength);  
		
		//处理加解密数据
		return ChannelBuffers.wrappedBuffer(bpPackage.getPackageBuffer());
	}
	public ConnectionManager getDefaultConnectionManager() {
		return defaultConnectionManager;
	}
	public void setDefaultConnectionManager(
			ConnectionManager defaultConnectionManager) {
		this.defaultConnectionManager = defaultConnectionManager;
	}

}
