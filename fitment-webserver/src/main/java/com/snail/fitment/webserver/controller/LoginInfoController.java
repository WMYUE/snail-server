package com.snail.fitment.webserver.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snail.fitment.api.userinfo.IUserService;
import com.snail.fitment.common.code.CommonStatusCode;
import com.snail.fitment.common.constants.CookieConstants;
import com.snail.fitment.common.exception.StateException;
import com.snail.fitment.common.utils.CookieUtils;
import com.snail.fitment.common.utils.Response;
import com.snail.fitment.model.SessionInfo;
import com.snail.fitment.model.request.ReturnResult;

@Controller
@RequestMapping("loginInfo")
public class LoginInfoController {
	@Qualifier("userServiceImpl")
	private IUserService userServiceImpl;
	
	private static final Log log = LogFactory.getLog(LoginInfoController.class);
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public @ResponseBody String login(String loginName, String password, String systemName, HttpServletResponse httpServletResponse){
		Response response = new Response();
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(systemName)){
			response.setErrcode(CommonStatusCode.BP_REQUEST_PARAMS_ERROR);
			return response.toJson();
		}
		
		SessionInfo result = null;
		try {
			//result = userServiceImpl.webLogin(loginName, password, systemName);
		} catch (Exception e) {

			response.setErrcode(CommonStatusCode.BP_SERVICE_ERROR);
			response.setErrmsg(e.getMessage());
			return response.toJson();
		}
		
		if(result == null){
			response.setErrcode(CommonStatusCode.BP_SERVICE_ERROR);
			return response.toJson();
		}
		
		CookieUtils.setCookie(httpServletResponse, CookieConstants.COOKIE_LOGIN_NAME, result.getSessionId(), CookieConstants.COOKIE_EXPIRE_TIME);
		response.addResult("result", result);
		return response.toJson();
	}
	
	
	@RequestMapping(value = "/applyUser", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public @ResponseBody String applyUser(HttpServletResponse httpServletResponse, String name, String password, String email, String mobile){
		Response response = new Response();
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email) || StringUtils.isEmpty(mobile)){
			response.setErrcode(CommonStatusCode.BP_REQUEST_PARAMS_ERROR);
			return response.toJson();
		}
		
		ReturnResult result = null;
		try {
			result = userServiceImpl.webRegister(name, password, email, mobile);
			processRPCResult(result);
		} catch (Exception e) {
			return new Response(CommonStatusCode.BP_SERVICE_ERROR,e.getMessage()).toJson();
		}
		
		response.addResult("result", result.getReturnContent());
		return response.toJson();
	}
	
	
	@RequestMapping(value = "/modifyUser", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public @ResponseBody String modifyUser(long loginInfoId, String username, String email, String mobile){
		Response response = new Response();
		if(StringUtils.isEmpty(username)  || StringUtils.isEmpty(email) || StringUtils.isEmpty(mobile)){
			response.setErrcode(CommonStatusCode.BP_REQUEST_PARAMS_ERROR);
			return response.toJson();
		}
		
		ReturnResult result = null;
		try {
			result = userServiceImpl.modifyUserLoginInfo(loginInfoId, username, email, mobile);
		} catch (Exception e) {
			response.setErrcode(CommonStatusCode.BP_SERVICE_ERROR);
			response.setErrmsg(e.getMessage());
			return response.toJson();
		}
		
		response.addResult("loginInfo", result.getReturnContent());
		return response.toJson();
	}
	
	@RequestMapping(value = "/deleteUser", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteUser(String developerIdList){
		Response response = new Response();
		if(StringUtils.isEmpty(developerIdList)){
			response.setErrcode(CommonStatusCode.BP_REQUEST_PARAMS_ERROR);
			return response.toJson();
		}
		
		String[] stringList = developerIdList.split(",");
		List<Long> idList = new ArrayList();
		for(String ss : stringList){
			idList.add(Long.valueOf(ss));
		}
		
		ReturnResult result = null;
		try {
			result = userServiceImpl.deleteUserLoginInfoByIdList(idList);
			processRPCResult(result);
		} catch (Exception e) {
			
			response.setErrcode(CommonStatusCode.BP_SERVICE_ERROR);
			response.setErrmsg(e.getMessage());
			return response.toJson();
		}
		
		return response.toJson();
	}
	
	
	
	@RequestMapping(value = "/queryUser", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public @ResponseBody String queryUser(String keywords, int status, int pageNo, int pageSize){
		Response response = new Response();
		if(status < 0){
			response.setErrcode(CommonStatusCode.BP_REQUEST_PARAMS_ERROR);
			return response.toJson();
		}
		
		ReturnResult result = null;
		try {
			result = userServiceImpl.queryloginInfoPages(keywords, status, pageNo, pageSize);
			processRPCResult(result);
		} catch (Exception e) {
			response.setErrcode(CommonStatusCode.BP_SERVICE_ERROR);
			response.setErrmsg(e.getMessage());
			return response.toJson();
		}
		
		response.addResult("result", result.getReturnContent());
		return response.toJson();
	}
	
	private void processRPCResult(ReturnResult result) {
		if(result == null){
			log.info("RPC ERROR, return is null");
			throw new StateException(CommonStatusCode.BP_SERVICE_ERROR, CommonStatusCode.desc(CommonStatusCode.BP_SERVICE_ERROR));
		}
		
		if(result.getCode() != 0){
			throw new StateException(result.getCode(), result.getDesc());
		}
		
	}
}
