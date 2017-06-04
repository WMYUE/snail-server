package com.snail.fitment.service.session;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.ISessionManager;
import com.snail.fitment.common.collection.TimeLimitedLRUCache;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.model.SessionInfo;

@Lazy(false) @Component("sessionManager")
public class LocalSessionManager implements ISessionManager {
	public static Logger log = Logger.getLogger(LocalSessionManager.class);
	
	private TimeLimitedLRUCache<String,SessionInfo> sessionCache = new TimeLimitedLRUCache<>(2048,FitmentConfig.defaultSessionTimeout);
	
	@Autowired
	private ISessionManager remoteSessionManager;
	
	@PostConstruct
	private void init(){
		//do something
	}
	
	@Override
	public SessionInfo getSession(String sessionId) {
		log.info("LocalSessionManager getSession sessionId = " + sessionId);
		SessionInfo session = sessionCache.get(sessionId);
		if (session == null && remoteSessionManager != null) {
			session = remoteSessionManager.getSession(sessionId);
		} 
		if(session != null){
			this.updateSession(session);
		}
		return session;
	}
	
	

	@Override
	public boolean updateSession(SessionInfo session) {
		log.info("LocalSessionManager updateSession");
		//sessionCache.put(session.getSessionId(), session);
		sessionCache.refresh(session.getSessionId(),session);
		if(remoteSessionManager != null) {
			remoteSessionManager.updateSession(session);
		}
		return true;
	}

	@Override
	public void logoutSession(String sessionId) {
		log.info("LocalSessionManager logoutSession sessionId = " + sessionId);
		sessionCache.remove(sessionId);
		if(remoteSessionManager != null) {
			remoteSessionManager.logoutSession(sessionId);
		}
		
	}

}
