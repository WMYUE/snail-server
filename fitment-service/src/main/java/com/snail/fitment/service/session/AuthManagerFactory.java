package com.snail.fitment.service.session;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.snail.fitment.api.session.IAuthManager;
import com.snail.fitment.api.session.IAuthManagerFactory;

@Component
public class AuthManagerFactory implements IAuthManagerFactory {
	private Map<String,IAuthManager> authManagerMap=new HashMap<>();
	
	@Override
	public IAuthManager getAuthManager(String appKey) {
		return authManagerMap.get(appKey);
//		IAuthManager authManager = null;
//		
//		if("test".equals(appKey)){
//			authManager = (IAuthManager) ServiceLocator.getBean("mockAuthManager");
//		}else if("lanxin".equals(appKey)){
//			authManager = (IAuthManager) ServiceLocator.getBean("lanxinAuthManager");
//		}else if("web".equals(appKey)){
//			authManager = (IAuthManager) ServiceLocator.getBean("webAuthManager");
//		} else if("auth2".equals(appKey)) {
//			authManager = (IAuthManager) ServiceLocator.getBean("auth2AuthManager");
//		} else if("blueprint".equals(appKey)) {
//			authManager = (IAuthManager) ServiceLocator.getBean("blueprintAuthManager");
//		}
//		
//		return authManager;
	}

	@Override
	public void regAuthManager(String appKey, IAuthManager authManager) {
		authManagerMap.put(appKey,authManager);
	}

}
