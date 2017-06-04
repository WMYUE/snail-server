package com.snail.fitment.api.session;

import com.snail.fitment.model.Auth2TokenInfo;

public interface ITokenManager {
	public void saveToken(Auth2TokenInfo token);
	public Auth2TokenInfo getToken(String userUniId, String sessionId);
	public void removeToken(Auth2TokenInfo token);
}
