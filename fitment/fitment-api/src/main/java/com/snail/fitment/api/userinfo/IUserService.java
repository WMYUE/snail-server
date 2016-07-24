package com.snail.fitment.api.userinfo;

import java.util.List;

import com.snail.fitment.model.Page;
import com.snail.fitment.model.request.ReturnResult;
import com.snail.fitment.model.userinfo.LoginInfo;

public interface IUserService {
	
	/**
     * 判断登录名是否已经存在
     * @param loginName
     * @return
     * @throws Exception 
     */
	boolean isLoginNameExists(String loginName) throws Exception;
    
	
    /**
    * 判断邮箱是否已经存在
    * @param loginName
    * @return
    * @throws Exception 
    */
	boolean isEmailExists(String email) throws Exception;
   
   
   /**
   * 判断手机号是否已经存在
   * @param loginName
   * @return
   * @throws Exception 
   */
	boolean isMobileExists(String mobile) throws Exception;
    
    /**
     * 用户注册
     * @param loginName 登录名
     * @param password 密码（加密）
     * @param email 邮箱
     * @param mobile 手机号
     * @return
     */
    ReturnResult webRegister(String loginName, String password,
			String email, String mobile) throws Exception;
    
    /**
     * 用户登录
     * @param loginName 登录名，一般为手机号
     * @param password 密码
     * @param name 姓名
     * @return
     */
    ReturnResult webLogin(String loginName, String password, String systemName) throws Exception;
    
    
    /**
     * 根据登录名修改密码（web使用）
     * @param loginName
     * @param password 与需要存储的密码一致
     */
    ReturnResult modifyPasswordByLoginName(String loginName,  String oldPassword,  String newPassword);
    
    /**
     * 
     * @param developerId
     * @param name
     * @param email
     * @param mobilea
     * @return
     */
    public ReturnResult<LoginInfo> modifyUserLoginInfo(long loginInfoId, String name, String email, String mobile);
    
    
    /**
     * 删除注册信息
     * @param loginInfoIdList
     * @return
     */
    ReturnResult deleteUserLoginInfoByIdList(List<Long> loginInfoIdList);
    
    /**
     * 分页查询
     * @param keywords
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ReturnResult<Page<LoginInfo>> queryloginInfoPages(String keywords,  int status, int pageNo, int pageSize);
    
}
