package com.snail.fitment.common.code;

import java.util.HashMap;
import java.util.Map;

public class CommonStatusCode {
private static final Map<Integer,String> codeMap = new HashMap<Integer,String>();
	
	//0~99
	public static final int SYSTEM_BUSY = -1;
	public static final int SUCCESS_OK = 0;
	public static final int COMMON_CLIENT_NEED_UPDATE = 1;
	public static final int COMMON_USER_PASSWORD_ERROR = 2;
	public static final int COMMON_CONNECTION_ERROR = 3;
	public static final int COMMON_SYSTEM_ERROR = 4;
	public static final int COMMON_SYSTEM_MAINTAIN= 5;
	public static final int COMMON_SERVER_ERROR = 6;
	public static final int COMMON_REQUEST_UNSUPPORTED= 7;
	public static final int COMMON_REQUEST_PARAM_ERROR = 8; //请求参数不符合规范(非空、长度、类型)
	public static final int COMMON_LACK_BALANCE = 9; // 余额不足
	public static final int COMMON_DECRYPT_ERROR = 10; //数据加密错误
	public static final int COMMON_TIMESTAMP_ERROR = 11;//时间戳错误，已过期
	public static final int COMMON_PASSWORD_DIGEST_ERROR = 12;//基于口令的摘要验证错误
	public static final int COMMON_NO_PERMISSION = 13;//没有操作权限
	public static final int COMMON_CERTIFICATE_IS_INVALID = 14;//服务端证书无效或已过期，需重新下载
	public static final int COMMON_KEYWORDS_ERROR = 15;
	

	

	//100+, 用户相关
	public static final int USER_RECORD_NOT_FOUND = 101; //找不到用户
	public static final int USER_RECORD_NOT_PERMISSION=102; //用户权限不够
	public static final int USER_PASSWORD_ERR = 103; //用户名、密码错误
	public static final int SESSIONINFO_NOT_EXISTED_ERROR = 104;
	public static final int USER_EXISTED = 105; //用户已经存在
	public static final int USER_CHECK_VALUE_ERR = 106; //用户尚未通过验证
	public static final int USER_BINGDING_NOT_EXISTED = 107; //用户尚未绑定
	
	//200+,商城applicatoin应用相关
	public static final int APP_DEPENDENCY_NOT_FOUND = 201; //上传app所需依赖在商城无法找到
	public static final int APPLICATION_PARSE_FILEAD = 202; //上传app文件包解析失败
	public static final int APPLICATION_UPGRADE_FILEAD = 203; //升级app文件包失败
	
	//300+, 资源相关
	public static final int RESOURCE_RESOURCE_ID_EXISTED = 301;
	public static final int RESOURCE_OUT_OFF_MAX_SIZE = 302;
	public static final int RESOURCE_RESOURCE_ID_NOT_FOUND = 303;
	public static final int RESOURCE_FILE_NOT_FOUND = 304;
	public static final int RESOURCE_PART_RESOURCE_ID_ERROR = 305;
	public static final int RESOURCE_MD5_ERROR = 306;
	public static final int RESOURCE_UPLOWDPATH_ERROR = 307;
	public static final int RESOURCE_READ_ERROR = 308;
	public static final int RESOURCE_PART_LIST_EMPTY_ERROR = 309;
	
	//400+, 数据相关
	public static final int DATA_ALREADY_EXISTS = 400; //数据已存在
	public static final int DATA_REQUEST_FAILED = 401;
	
	
	//800+ WEB-RPC请求相关
	public static final int BP_REQUEST_PARAMS_ERROR= 8001;
	public static final int BP_PARAMS_ERROR_DATA = 8002;
	public static final int BP_SERVICE_ERROR = 8003;
	public static final int BP_NOTIFY_GUDONG_SERVICE_FAILED = 8004;
	public static final int BP_AUTH_FAILED = 8005;
	public static final int BP_APP_ALREADY_EXISTS_ERROR= 8006;
	public static final int BP_APP_MODEL_ALREADY_EXISTS_ERROR = 8007;
	public static final int BP_APP_ANALYZE_ERROR= 8008;
	public static final int BP_APP_MODEL_ANALYZE_ERROR= 8009;
	public static final int BP_APP_FILE_ANALYZE_ERROR= 8010;
	
	static {
		//0~99
		codeMap.put(SYSTEM_BUSY, "系统繁忙");   //-1
		codeMap.put(SUCCESS_OK, "操作成功");    //0
		codeMap.put(COMMON_CLIENT_NEED_UPDATE, "客户端需要升级"); //1
		codeMap.put(COMMON_USER_PASSWORD_ERROR, "客用户名、密码错误"); //2
		codeMap.put(COMMON_CONNECTION_ERROR, "连接错误"); //3
		codeMap.put(COMMON_SYSTEM_ERROR, "系统内部错误");  //4
		codeMap.put(COMMON_SYSTEM_MAINTAIN, "系统维护中，请稍后访问");  //5
		codeMap.put(COMMON_SERVER_ERROR, "服务器异常");   //6
		codeMap.put(COMMON_REQUEST_UNSUPPORTED, "服务器不支持此操作"); //7
		codeMap.put(COMMON_REQUEST_PARAM_ERROR, "请求参数不符合规范(非空、长度、类型)"); //8
		codeMap.put(COMMON_LACK_BALANCE, "余额不足");  // 9
		codeMap.put(COMMON_DECRYPT_ERROR, "数据解密失败"); //10
		codeMap.put(COMMON_TIMESTAMP_ERROR, "时间已过期！");  //11
		codeMap.put(COMMON_PASSWORD_DIGEST_ERROR, "认证信息有误，请重试！"); //12
		codeMap.put(COMMON_NO_PERMISSION, "没有操作权限");  //13
		codeMap.put(COMMON_CERTIFICATE_IS_INVALID, "服务端证书无效或已过期，需重新下载");  //14
		codeMap.put(COMMON_KEYWORDS_ERROR, "内容中包含敏感词：{0}！");   //15
		
		//1XX
		codeMap.put(USER_RECORD_NOT_FOUND, "找不到用户");    //101
		codeMap.put(USER_RECORD_NOT_PERMISSION, "用户权限不够"); //102
		codeMap.put(USER_PASSWORD_ERR, "用户名或密码错误"); //103
		codeMap.put(SESSIONINFO_NOT_EXISTED_ERROR, "找不到Session信息"); //104
		codeMap.put(USER_EXISTED, "用户已经存在"); //105
		codeMap.put(USER_CHECK_VALUE_ERR, "用户尚未通过验证"); //106
		codeMap.put(USER_BINGDING_NOT_EXISTED, "用户尚未绑定"); //107

		//2XX
		codeMap.put(APP_DEPENDENCY_NOT_FOUND, "上传app所需依赖在商城无法找到"); //201
		codeMap.put(APPLICATION_PARSE_FILEAD, "上传app文件包解析失败"); //202
		codeMap.put(APPLICATION_UPGRADE_FILEAD, "升级app文件包失败"); //203
		
		//3XX
		codeMap.put(RESOURCE_RESOURCE_ID_EXISTED, "资源ID已经存在（上传资源时，不能重复）");  //301
		codeMap.put(RESOURCE_OUT_OFF_MAX_SIZE, "上传的资源字节数超出限制"); //302
		codeMap.put(RESOURCE_RESOURCE_ID_NOT_FOUND, "资源ID不存在(客户端可延迟再下载)");//303
		codeMap.put(RESOURCE_FILE_NOT_FOUND, "资源文件不存在(服务端移除或丢失文件，客户端不需要再下载)");//304
		codeMap.put(RESOURCE_UPLOWDPATH_ERROR, "资源上传路径错误（上传文件指定的路径错误）");//305
		codeMap.put(RESOURCE_PART_RESOURCE_ID_ERROR, "分块资源ID错误");//306
		codeMap.put(RESOURCE_MD5_ERROR, "资源MD5值校验失败");//307
		codeMap.put(RESOURCE_READ_ERROR, "资源读取错误");//308
		codeMap.put(RESOURCE_PART_LIST_EMPTY_ERROR, "分块资源id集合不能为空");//309
		
		//4XX
		codeMap.put(DATA_REQUEST_FAILED, "数据请求失败");   //401
		codeMap.put(DATA_ALREADY_EXISTS, "数据已存在");    //402
		
		
		/**
		 * 8XXX web-RPC相关
		 */
		codeMap.put(BP_REQUEST_PARAMS_ERROR, "请求参数不符合规范(非空、长度、类型)");  //8001
		codeMap.put(BP_PARAMS_ERROR_DATA, "提交的JSON数据格式有误！"); //8002
		codeMap.put(BP_SERVICE_ERROR, "蓝图内部服务提交失败！"); //8003
		codeMap.put(BP_NOTIFY_GUDONG_SERVICE_FAILED, "调用蓝信通知失败！"); //8004
		codeMap.put(BP_AUTH_FAILED, "安全校验有误！"); //8005
		codeMap.put(BP_APP_ALREADY_EXISTS_ERROR, "app应用已经存在！"); //8006
		codeMap.put(BP_APP_MODEL_ALREADY_EXISTS_ERROR, "app应用模型已经存在！"); //8007
		codeMap.put(BP_APP_ANALYZE_ERROR, "app应用解析失败"); //8008
		codeMap.put(BP_APP_MODEL_ANALYZE_ERROR, "app模型解析失败"); //8009
		codeMap.put(BP_APP_FILE_ANALYZE_ERROR, "app包解压错误"); //8010
	}
	
    public static String desc(int code){
    	String desc = codeMap.get(code);    	
    	return desc != null ? desc : "未定义错误";
    }
}
