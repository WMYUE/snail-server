package com.snail.fitment.service.session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.snail.fitment.api.session.IToken;
import com.snail.fitment.api.session.ITokenManager;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.security.OAuthUtil;
import com.snail.fitment.model.Auth2TokenInfo;
import com.snail.fitment.model.SessionInfo;

@Component("auth2AuthManager")
public class Auth2AuthManager extends AbstractAuthManager {
	public static Logger log = Logger.getLogger(Auth2AuthManager.class);

	// @Autowired
	// private IOpenService lanxinOpenService;

	@Autowired
	private ITokenManager tokenManager;

	@Override
	public SessionInfo authWithToken(IToken token) {
		JSONObject json = JSONObject.parseObject(token.getParams().get("authToken"));

		String accessToken = json.getString("accessToken");
		String userUniId = json.getString("userUniId");
		String openUrl = json.getString("openUrl");
		String signature = json.getString("signature");
		if (signature != null) {// webServer 校验
			if (signature
					.equals(OAuthUtil.signature(FitmentConfig.WEB_SERVER_SECRET, openUrl, accessToken, userUniId))) {
				SessionInfo info = this.createTempSessionInfoWithUserName(userUniId, userUniId);
				saveAuth2TokenInfo(accessToken, info);
				return info;
			}
		}
		if (openUrl == null) {
			openUrl = FitmentConfig.LANXIN_OPEN_URL;
		}

		return null;
	}

	private void saveAuth2TokenInfo(String accessToken, SessionInfo info) {
		Auth2TokenInfo auth2Token = new Auth2TokenInfo();
		auth2Token.setAccessToken(accessToken);
		auth2Token.setSessionInfo(info);
		tokenManager.saveToken(auth2Token);
	}

	@Override
	public SessionInfo refreshWithToken(IToken token) {
		String authToken = token.getParams().get("authToken");
		JSONObject json = JSONObject.parseObject(authToken);

		String signature = json.getString("signature");
		String userUniId = json.getString("userUniId");
		String sessionId = json.getString("sessionId");
		String timestamp = json.getString("timestamp");
		String nonce = json.getString("nonce");

		Auth2TokenInfo auth2Token = tokenManager.getToken(userUniId, sessionId);
		if (auth2Token != null) {
			if (signature.equals(OAuthUtil.signature(auth2Token.getAccessToken(), timestamp, nonce, sessionId))) {
				SessionInfo oldinfo = auth2Token.getSessionInfo();
				SessionInfo info = createTempSessionInfo(oldinfo);
				auth2Token.setSessionInfo(info);
				tokenManager.saveToken(auth2Token);
				return info;
			}
		}
		return null;
	}

	@Override
	public String getAppKey() {
		return "auth2";
	}
}
