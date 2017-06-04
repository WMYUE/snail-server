package com.snail.fitment.api.session;

public interface IAuthManagerFactory {
	public IAuthManager getAuthManager(String appKey);

	public void regAuthManager(String appKey, IAuthManager authManager);
}
