package com.snail.fitment.service.session;

import java.util.Map;

import com.snail.fitment.api.session.IToken;

public class Auth2Token implements IToken {

	private Map<String,String> tokenParams;
	
	public Auth2Token(String tokenStr) {
		
	}
	
	public Auth2Token(Map<String,String> tokenParams) {
		this.tokenParams= tokenParams;
	}
	
	@Override
	public Map<String, String> getParams() {
		return tokenParams;
	}

}
