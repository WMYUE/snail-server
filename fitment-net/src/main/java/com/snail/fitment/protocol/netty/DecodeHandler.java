package com.snail.fitment.protocol.netty;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.fitment.protocol.bpackage.BpHeader;
import com.snail.fitment.protocol.bpackage.BpPackageFactory;
import com.snail.fitment.protocol.bpackage.FlashPolicy;
import com.snail.fitment.protocol.bpackage.HeartBeat;
import com.snail.fitment.protocol.bpackage.IBpHeader;
import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.connection.BpConnection;
import com.snail.fitment.protocol.connection.ConnectionManager;

@Component("decodeHandler")
public class DecodeHandler extends FrameDecoder {//ProtobufDecoder
	
	private static final Logger log = Logger.getLogger(DecodeHandler.class);
	
	@Autowired 
	private ConnectionManager defaultConnectionManager;
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
	
		if(buffer.readableBytes()< 4) {
			return null;
		}
		//skip partial header 
		int length = 0;
        buffer.markReaderIndex();  //标记预读
        byte[] temp= new byte[4];
        buffer.readBytes(temp);  //协议标记
        if(Arrays.equals(IBpHeader.StackLabel.getBytes(),temp)) {//合法协议头
        	if(buffer.readableBytes()<BpHeader.FixedHeaderSize) {
            	log.debug("waiting package head");
            	buffer.resetReaderIndex();  //取消预读
    			return null;
    		}
        	buffer.readByte();       //协议版本
            buffer.readByte();       //消息类型
            buffer.readShort();       //状态码
            buffer.readLong();       //时间
            buffer.readInt();        //序列号
            length = buffer.readInt();
    		
    		buffer.resetReaderIndex();  //取消预读
        }else if(Arrays.equals(HeartBeat.Instance.getPackageBuffer(),temp)) {//心跳 
        	buffer.resetReaderIndex();  //取消预读
        	log.debug("received heartbeat");
        	buffer.readBytes(temp);
        	return HeartBeat.Instance;
        } else if(buffer.readableBytes()>=23-4) {//check if flash policy file
        	buffer.resetReaderIndex();  //取消预读
			byte[] dst = new byte[23];
			buffer.readBytes(dst);
			String content = new String(dst,"ISO-8859-1");
            if (content.indexOf("<policy-file-request/>") >= 0) {
    			if (log.isDebugEnabled()) {
    				log.debug("decode - end" 
    						+ ", received flash policy"
    						);
    			}
                return FlashPolicy.Instance;
            } else {
            	log.debug("decode illegal data : "+ buffer.toString());
            	channel.disconnect();
            }
		}else {//非法数据,断开
			buffer.resetReaderIndex();  //取消预读
        	log.debug("decode illegal data : "+ new String(temp));
        	log.debug("decode illegal data : "+ buffer.toString());
        	channel.disconnect();
        	return null;
        }
 		
		int totallen = length + IBpHeader.FixedHeaderSize;
		if(buffer.readableBytes()<totallen) {
           	log.debug("waiting package data"+"expect data length:"+ (totallen));
			return null;
		}

		byte[] bytebuffer= new byte[totallen];
		buffer.readBytes(bytebuffer);
		
		log.debug("decode, parse data");
		IBpPackage pkg = BpPackageFactory.parseNormalBpPackage(bytebuffer);
		
		if(pkg != null){
			log.debug("decode, " + pkg.getBpHeader().toString() + ", " + pkg.getBpBody().toJsonString());
		}
		
		//处理加密数据
		BpConnection connection=defaultConnectionManager.getBpConnection(channel.getId());
		String secretKey=null;
		if(connection != null) {
			secretKey = connection.getSecretKey();
		}
		pkg.decode(secretKey);
		
		return pkg;
	}
	public ConnectionManager getDefaultConnectionManager() {
		return defaultConnectionManager;
	}
	public void setDefaultConnectionManager(
			ConnectionManager defaultConnectionManager) {
		this.defaultConnectionManager = defaultConnectionManager;
	}

}
