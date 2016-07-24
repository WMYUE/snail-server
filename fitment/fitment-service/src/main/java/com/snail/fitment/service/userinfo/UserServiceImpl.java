package com.snail.fitment.service.userinfo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.snail.fitment.api.userinfo.IUserService;
import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.database.mysql.dao.api.ILoginInfoDao;
import com.snail.fitment.model.Page;
import com.snail.fitment.model.request.ReturnResult;
import com.snail.fitment.model.userinfo.LoginInfo;

@Component("userServiceImpl")
public class UserServiceImpl implements IUserService {
	public static Logger log = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private ILoginInfoDao loginInfoDao;
	
	@Override
	public boolean isLoginNameExists(String loginName) throws Exception {
		if(StringUtils.isEmpty(loginName)){
			return false;
		}
		LoginInfo userInfo  = loginInfoDao.selectByLoginNameAndEmailAndMobile(loginName, null, null);
		if(userInfo == null){
			return false;
		}
		return true;
	}

	@Override
	public boolean isEmailExists(String email) throws Exception {
		if(StringUtils.isEmpty(email)){
			return false;
		}
		LoginInfo userInfo  = loginInfoDao.selectByLoginNameAndEmailAndMobile(null, email, null);
		if(userInfo == null){
			return false;
		}
		return true;
	}

	
	@Override
	public boolean isMobileExists(String mobile) throws Exception {
		if(StringUtils.isEmpty(mobile)){
			return false;
		}
		LoginInfo userInfo  = loginInfoDao.selectByLoginNameAndEmailAndMobile(null, null, mobile);
		if(userInfo == null){
			return false;
		}
		return true;
	}



	@Override
	public ReturnResult webRegister(String loginName, String password,
			String email, String mobile) throws Exception {
		ReturnResult result = ReturnResult.getSuccessInstance();
		
		//参数非空验证
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(email)||StringUtils.isEmpty(mobile)){
			result.setCode(CommonStatusCode.BP_REQUEST_PARAMS_ERROR);
			result.setDesc(CommonStatusCode.desc(CommonStatusCode.BP_REQUEST_PARAMS_ERROR));
			return result;
		}
		
		//验证开发者是否已存在
		if(isLoginNameExists(loginName) == true || isEmailExists(email) ==  true ||  isMobileExists(mobile) == true){
			result.setCode(CommonStatusCode.USER_EXISTED);
			result.setDesc(CommonStatusCode.desc(CommonStatusCode.USER_EXISTED));
			return result;
		}

		LoginInfo userInfo = new LoginInfo();
		userInfo.setLoginName(loginName);
		userInfo.setPassword(password);
		userInfo.setEmail(email);
		userInfo.setMobile(mobile);
		userInfo.setChecked(LoginInfo.REGISTER_STATUS_UNCHECKED);
		userInfo.setCreateTime(new Date());
		userInfo.setUpdateTime(new Date());
		loginInfoDao.insert(userInfo);
		
		result.setReturnContent(userInfo);
		return result;
	}


	@Override
	public ReturnResult modifyPasswordByLoginName(String loginName,
			String oldPassword, String newPassword) {
		ReturnResult result= ReturnResult.getSuccessInstance();
		
		LoginInfo loginInfo = loginInfoDao.selectByLoginName(loginName);
		if(loginInfo == null){
			result.setCode(CommonStatusCode.USER_RECORD_NOT_FOUND);
			result.setDesc(CommonStatusCode.desc(CommonStatusCode.USER_RECORD_NOT_FOUND));
			return result;
		}
		
		//密码错误
		if(!StringUtils.equals(loginInfo.getPassword(), oldPassword)){
			result.setCode(CommonStatusCode.USER_PASSWORD_ERR);
			result.setDesc(CommonStatusCode.desc(CommonStatusCode.USER_PASSWORD_ERR));
		}
		
		loginInfo.setPassword(newPassword);
		loginInfoDao.updateById(loginInfo);
		
		return result;
	}
	
	public ReturnResult<LoginInfo> modifyUserLoginInfo(long loginInfoId, String name, String email, String mobile) {
		ReturnResult<LoginInfo> result = ReturnResult.getSuccessInstance();
		
		try {
			LoginInfo loginInfo = loginInfoDao.selectById(loginInfoId);
			
			if(loginInfo == null){
				result.setCode(CommonStatusCode.USER_RECORD_NOT_FOUND);
				result.setDesc(CommonStatusCode.desc(CommonStatusCode.USER_RECORD_NOT_FOUND));
				return result;
			}
			
			loginInfo.setLoginName(name);
			loginInfo.setEmail(email);
			loginInfo.setMobile(mobile);

			loginInfoDao.updateById(loginInfo);
			
			result.setReturnContent(loginInfo);
		} catch (Exception e) {
			e.printStackTrace();
			
			result.setCode(CommonStatusCode.BP_SERVICE_ERROR);
			result.setDesc(e.getCause().getMessage());
			return result;
		}
		
		return result;
	}

	@Override
	public ReturnResult deleteUserLoginInfoByIdList(List<Long> loginInfoIdList) {
		ReturnResult result = ReturnResult.getSuccessInstance();
		
		try {
			int n = loginInfoDao.deleteByIdList(loginInfoIdList);
		} catch (Exception e) {
			e.printStackTrace();
			
			result.setCode(CommonStatusCode.BP_SERVICE_ERROR);
			result.setDesc(e.getCause().getMessage());
			return result;
		}
		
		return result;
	}

	@Override
	public ReturnResult<Page<LoginInfo>> queryloginInfoPages(String keywords, int status, int pageNo, int pageSize) {
		ReturnResult<Page<LoginInfo>> result = ReturnResult.getSuccessInstance();
		
		try {
			Page<LoginInfo> page = loginInfoDao.queryByParamsForPage(keywords,  status, pageNo, pageSize);
			result.setReturnContent(page);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(CommonStatusCode.BP_SERVICE_ERROR);
			result.setDesc(e.getCause().getMessage());
			return result;
		}
		
		return result;
	}

	@Override
	public ReturnResult webLogin(String loginName, String password, String systemName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
