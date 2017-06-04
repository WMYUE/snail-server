package com.snail.fitment.service.session;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.fitment.api.session.IAuthManager;
import com.snail.fitment.api.session.IAuthManagerFactory;
import com.snail.fitment.api.session.ISessionManager;
import com.snail.fitment.api.session.IToken;
import com.snail.fitment.api.session.ITokenManager;
import com.snail.fitment.common.constants.CommonConstants;
import com.snail.fitment.common.utils.RandomUtils;
import com.snail.fitment.model.Auth2TokenInfo;
import com.snail.fitment.model.SessionInfo;

public abstract class AbstractAuthManager implements IAuthManager {

	@Autowired
	private ISessionManager sessionManager;
	
	@Autowired
	private ITokenManager tokenManager;
	
	@Autowired
	protected IAuthManagerFactory authManagerFactory;
	
	@PostConstruct
	public void registSelf() {
		authManagerFactory.regAuthManager(this.getAppKey(), this);
	}
	
	@Override
	public IToken createToken(Map<String, String> tokenParams) {
		return new Auth2Token(tokenParams);
	}
	
	public SessionInfo createTempSessionInfoWithUserName(String username, String userUniId){
		SessionInfo info = new SessionInfo();
		long sessionId = RandomUtils.next(-8000000, -1000);//为了不与数据库真实ID重复，采用负值
		
		while (sessionManager.getSession(String.valueOf(sessionId))!=null) {
			sessionId = RandomUtils.next(-8000000, -1000);//为了不与数据库真实ID重复，采用负值
		}
		
		info.setSessionId(String.valueOf(sessionId));
		info.setUserUniId(userUniId);
		info.setUserName(username);
		info.setUpdateTime(new Date().getTime());

		sessionManager.updateSession(info);
		return info;
	}

	public SessionInfo createWebSession(String loginName, long userId, long loginInfoId, String systemName){
		SessionInfo info = new SessionInfo();
		long sessionId = RandomUtils.next(-8000000, -1000);//为了不与数据库真实ID重复，采用负值
		
		while (sessionManager.getSession(String.valueOf(sessionId))!=null) {
			sessionId = RandomUtils.next(-8000000, -1000);//为了不与数据库真实ID重复，采用负值
		}
		
		info.setSessionId(String.valueOf(sessionId));
		info.setUserUniId(null);
		info.setUserId(userId);
		info.setLoginInfoId(loginInfoId);
		info.setUserName(loginName);
		info.setUpdateTime(new Date().getTime());
		
		if(StringUtils.equals(systemName, CommonConstants.PLATFORM_TYPE_BLUEPRINT)){
			info.setUserUniId(userId+ CommonConstants.DEFAULT_BLUEPRINT_DOMAIN);
		}
		sessionManager.updateSession(info);
		return info;
	}

	
	protected SessionInfo createTempSessionInfo(SessionInfo oldSessionInfo){
		SessionInfo info=sessionManager.getSession(oldSessionInfo.getSessionId());
		if(info.getUserUniId().equals(oldSessionInfo.getUserUniId())) {
			return info;
		}
		long sessionId = RandomUtils.next(-80000000, -8000000);//为了不与数据库真实ID重复，采用负值	
		while (sessionManager.getSession(String.valueOf(sessionId))!=null) {
			sessionId = RandomUtils.next(-80000000, -8000000);//为了不与数据库真实ID重复，采用负值
		}
		
		oldSessionInfo.setSessionId(String.valueOf(sessionId));
		oldSessionInfo.setUpdateTime(System.currentTimeMillis());
		sessionManager.updateSession(oldSessionInfo);
		return oldSessionInfo;
	}

	@Override
	public SessionInfo logout(String sessionId, String token) {
		SessionInfo info = sessionManager.getSession(sessionId);
		if (token != null) {
			Auth2TokenInfo auth2Token = tokenManager.getToken(info.getUserUniId(), sessionId);
			if (auth2Token != null) {
				if (StringUtils.equals(auth2Token.getAccessToken(), token)) {
					tokenManager.removeToken(auth2Token);
					sessionManager.logoutSession(sessionId);
					return info;
				}
			}
		} else {
			sessionManager.logoutSession(sessionId);
			return info;
		}
		return null;
	}

	@Override
	public Map<String, Object> getExAuthInfo(SessionInfo sessionInfo, IToken token) {
		return null;
	}
}
