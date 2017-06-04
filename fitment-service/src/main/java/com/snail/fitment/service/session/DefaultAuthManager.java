package com.snail.fitment.service.session;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.IAuthManager;
import com.snail.fitment.api.session.IToken;
import com.snail.fitment.model.SessionInfo;

@Lazy(false)
@Component("defaultAuthManager")
public class DefaultAuthManager extends AbstractAuthManager {
	
	@Override
	public SessionInfo authWithToken(IToken token) {
		String appKey = token.getParams().get("appKey");		
		IAuthManager authManager = authManagerFactory.getAuthManager(appKey);
		SessionInfo info = authManager.authWithToken(token);
		return info;
	}
	
	@Override
	public Map<String, Object> getExAuthInfo(SessionInfo sessionInfo, IToken token) {
		String appKey = token.getParams().get("appKey");		
		IAuthManager authManager = authManagerFactory.getAuthManager(appKey);
		return authManager.getExAuthInfo(sessionInfo, token);
	}
	
	@Override
	public SessionInfo refreshWithToken(IToken token) {
		String appKey = token.getParams().get("appKey");
		IAuthManager authManager = authManagerFactory.getAuthManager(appKey);
		SessionInfo info = authManager.refreshWithToken(token);
		
		return info;
	}

	@Override
	public String getAppKey() {
		return "default";//不会用到
	}

}
