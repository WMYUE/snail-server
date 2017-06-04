package com.snail.fitment.protocol.netty;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.IdleStateHandler;

import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.utils.ServiceLocator;
import com.snail.fitment.protocol.connection.BpConnection;
import com.snail.fitment.protocol.connection.ConnectionManager;
import com.snail.fitment.protocol.handler.BaseHandler;

public class BluePrintPipeLineFactory implements ChannelPipelineFactory {
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {

		ChannelPipeline pipeline = Channels.pipeline();
		
		// 当 FitmentConfig.getDefaultIdleTimeout() 毫秒内没有给客户端发送数据时，断开连接
		// （默认使用服务端发送数据作为超时判断依据，以避免客户端持续发送无意义的数据导致服务器不断开连接的情况）
		// 系统设置时间为毫秒，这里转为秒
		pipeline.addLast("idlestate", new IdleStateHandler(BpConnection.timer,0,FitmentConfig.defaultIdleTimeout/1000,0));
		pipeline.addLast("idlestateprocess", (ChannelHandler)ServiceLocator.getBean("idleStateProcessHandler"));
		
		//连接管理
		pipeline.addLast("handler", (ChannelHandler)ServiceLocator.getBean("connectionManagerHandler"));
		
		//编解码handler
		DecodeHandler dh= new DecodeHandler();
		EncoderHandler eh= new EncoderHandler();
		dh.setDefaultConnectionManager((ConnectionManager) ServiceLocator.getBean("defaultConnectionManager"));
		eh.setDefaultConnectionManager((ConnectionManager) ServiceLocator.getBean("defaultConnectionManager"));
		pipeline.addLast("decoder",dh/*(ChannelHandler)ServiceLocator.getBean("decodeHandler")*/);
		pipeline.addLast("encoder",eh/*(ChannelHandler)ServiceLocator.getBean("encoderHandler")*/);
		
		//适配器和拦截器
		//pipeline.addLast("adaptor",(ChannelHandler)ServiceLocator.getBean("adaptorHandler"));
		//pipeline.addLast("interceptor",(ChannelHandler)ServiceLocator.getBean("interceptorHandler"));

		//业务处理handler
		pipeline.addLast("flashPolicy", (ChannelHandler)ServiceLocator.getBean("flashPolicyHandler"));
		pipeline.addLast("heartbeat", (ChannelHandler)ServiceLocator.getBean("heartbeatHandler"));
		///
		BluePrintPackageHandler phandler=(BluePrintPackageHandler)ServiceLocator.getBean("bluePrintPackageHandler");
		BaseHandler baseHandler = (BaseHandler)ServiceLocator.getBean("baseHandler");
		phandler.setBaseHandler(baseHandler);
		pipeline.addLast("phandler", phandler);
		return pipeline;
	}

}
