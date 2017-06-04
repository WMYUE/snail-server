package com.snail.fitment.service.session;

import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.IToken;
import com.snail.fitment.model.SessionInfo;

@Component("mockAuthManager")
public class MockAuthManager extends AbstractAuthManager{
	

	@Override
	public SessionInfo authWithToken(IToken token) {
		return createTempSessionInfoWithUserName("testUser", token.getParams().get("authToken"));
	}

	@Override
	public String getAppKey() {
		return "test";
	}

	@Override
	public SessionInfo refreshWithToken(IToken token) {
		return createTempSessionInfoWithUserName("testUser", token.getParams().get("authToken"));
	}
}
