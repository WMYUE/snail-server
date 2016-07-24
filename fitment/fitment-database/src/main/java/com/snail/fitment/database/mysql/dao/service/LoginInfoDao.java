package com.snail.fitment.database.mysql.dao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.snail.fitment.common.lang.StringUtils;
import com.snail.fitment.database.mysql.core.MyBatisDao;
import com.snail.fitment.database.mysql.dao.api.ILoginInfoDao;
import com.snail.fitment.model.Page;
import com.snail.fitment.model.userinfo.LoginInfo;

@Component
public class LoginInfoDao extends MyBatisDao<LoginInfo> implements ILoginInfoDao {
	
	private static final String NAMESPACE="mapper.LoginInfoMapper";

	@Override
	protected String getNameSpace() {
		return NAMESPACE;
	}

	@Override
	public LoginInfo selectByLoginNameAndEmailAndMobile(String loginName,
			String email, String mobile) {
		
		Map<String, String> params =  new HashMap<String, String>();
		
		if(StringUtils.isNotEmpty(loginName)){
			params.put("loginName", loginName);
		}	
		if(StringUtils.isNotEmpty(email)){
			params.put("email", email);
		}
		if(StringUtils.isNotEmpty(mobile)){
			params.put("mobile", mobile);
		}
		
		return this.getSqlSession().selectOne(NAMESPACE + ".selectByLoginNameAndEmailAndMobile", params);
	}
	
	@Override
	public LoginInfo selectByEmail(String email) {
		return this.getSqlSession().selectOne(NAMESPACE+".selectByEmail", email);
	}
	

	@Override
	public LoginInfo selectByMobile(String mobile) {
		return this.getSqlSession().selectOne(NAMESPACE+".selectByMobile", mobile);
	}

	@Override
	public LoginInfo selectByLoginName(String loginName) {
		return this.getSqlSession().selectOne(NAMESPACE+".selectByLoginName", loginName);
	}

	@Override
	public List<LoginInfo> selectByKeywords(String keywords) {
		return  this.getSqlSession().selectList(NAMESPACE+".selectByKeywords", keywords);
	}
	
	@Override
	public LoginInfo selectByUserId(long userId) {
		return this.getSqlSession().selectOne(NAMESPACE+".selectByUserId", userId);
	}

	@Override
	public Page<LoginInfo> queryByParamsForPage(String keywords, int status, int pageNo, int pageSize) {
		Page<LoginInfo> page = new Page<LoginInfo>(pageNo, pageSize);
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(keywords)){
			params.put("keywords",   "%"+keywords+"%");
		}
		
		if(status > 0){
			params.put("status",  status);
		}
		
		int count = countByParams(params);
		page.setTotalItems(count);
		
		if(pageNo >= 0 && pageSize >0){
			params.put("limit",  pageSize);
			params.put("offset",  (pageNo-1)*pageSize);
		}
		
		List<LoginInfo> queryList = this.getSqlSession().selectList(NAMESPACE+".queryByParamsForPage", params);
		page.setResult(queryList);
		return page;
	}

	private int countByParams(Map<String, Object> params) {
		return this.getSqlSession().selectOne(NAMESPACE+".countByParams", params);
	}
	
}
