package com.snail.fitment.database.mysql.dao.api;

import java.util.List;

import com.snail.fitment.database.mysql.core.IDao;
import com.snail.fitment.model.Page;
import com.snail.fitment.model.userinfo.LoginInfo;

public interface ILoginInfoDao extends IDao<LoginInfo>{
	
	public LoginInfo selectByLoginNameAndEmailAndMobile(String loginName, String email, String mobile);
	
	public List<LoginInfo> selectByKeywords(String keywords);
	
	public LoginInfo selectByEmail(String email);
	
	public LoginInfo selectByMobile(String mobile);
	
	public LoginInfo selectByLoginName(String loginName);
	
	public LoginInfo selectByUserId(long userId);

	public Page<LoginInfo> queryByParamsForPage(String keywords, int status, int pageNo, int pageSize);
	
}
