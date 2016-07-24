package com.snail.fitment.common.constants;
import java.util.ArrayList;
import java.util.List;


public final class CommonConstants {
    public static final int STATUS_ALL=-1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DELETED = 1;
    public static final int STATUS_WAITING = 2;
    
    public static final String DEFAULT_BLUEPRINT_DOMAIN = "@0.BLUEPRINT";
    public static final String DEFAULT_BLUEPRINT_DOMAIN_FLAG = "0.BLUEPRINT";
    
    public static final String PLATFORM_TYPE_BLUEPRINT = "BLUEPRINT";
    public static final String PLATFORM_TYPE_LANXIN = "LANXIN";
    
    
    public static final String SERVER_TYPE_MONGODB = "MONGODB";
    public static final String SERVER_TYPE_REDIS = "REDIS";
    public static final String SERVER_TYPE_MYSQL = "MYSQL";
    public static final String SERVER_TYPE_ZOOKEEPER = "ZOOKEEPER";
    public static final String SERVER_TYPE_MEMCACHE = "MEMCACHE";
    
    public static final List<Integer> STATUS_NORMAL_AND_WAITING = new ArrayList<Integer>();
    
	/*
	 * redis类别前缀
	 */
	public static final String REDIS_SESSION_PREFIX="session-";
	public static final String REDIS_CHANNEL_PREFIX="channel-";
	
    public static final String CONNECTION_CONTEXT_SECRET_KEY = "secretKey";
    public static final String CONNECTION_CONTEXT_SESSIONKEY = "sessionkey";
	public static final String CONNECTION_CONTEXT_GDP_VERSION = "gdpVersion";
	public static final String CONNECTION_CONTEXT_CLIENT_OS_NAME = "clientOsName";
	public static final String CONNECTION_CONTEXT_CLIENT_VERSION_MIN = "clientVersionMin";
    public static final String CONNECTION_CONTEXT_CLIENT_NATIVE_ID = "clientNativeId";
    public static final String CONNECTION_CONTEXT_USER_UNI_ID = "userUniId";
    public static final String CONNECTION_CONTEXT_LOGIN_NAME = "loginName";
    public static final String CONNECTION_CONTEXT_SESSION_ID = "sessionId";
    public static final String CONNECTION_CONTEXT_ORG_ID = "orgId";
    public static final String CONNECTION_CONTEXT_CRYPTO_TYPE = "cryptoType";
    public static final String CONNECTION_CONTEXT_COMPRESS_TYPE = "compressType";
    public static final String CONNECTION_CONTEXT_LOCALE = "locale";
    
    public static final String QUEUE_NAME_AUDIO_CON_EVENT = "audioConEventQueue";
    public static final String QUEUE_NAME_SIP_EVENT = "sipEventQueue";
    public static final int CLIENT_CAN_BE_CALLIN = 1 ;//客户端支持呼入模式的电话会议;
    public static final int CLIENT_CANNOT_BE_CALLIN = 0 ; //客户端不支持呼入模式的电话会议;
    
    //客户端登录方式
    public static final int AUTO_LOGIN = 1 ;//自动登录
    public static final int MANUAL_LOGIN = 0;//手动登录
    
    //web端登录
    public static final int WEB_MANAGER_LOGIN = 1 ;//管理员登录
    public static final int WEB_NORMAL_LOGIN = 0;//普通用户登录
    
    //iphone 4分辨率长宽比
    public static final String IPHONE_4_RESOLUTION = "15";
    //iphone 4以上版本分辨率长宽比
    public static final String IPHONE_4_ABOVE_RESOLUTION = "17";
    /**
     * 语言环境
     */
    public static final String LOCALE_CHINESE_CHINA = "zh_CN";
    public static final String LOCALE_CHINESE_TAIWAN = "zh_TW";
    public static final String LOCALE_ENGLISH_UNITEDSTATES = "en_US";
    
    public static final String GDYJ_ACCOUNT_LOGIN_TYPE = "gdyjaccountlogin"; 
    public static final String LANXIN_MOBILE_LOGIN_TYPE = "lanxinmobilelogin"; 
    
    public static int getIntFromBoolean(boolean value){
    	if(value){
    		return 1;
    	}else{
    		return 0;
    	}
    }
//    public static final List<String> LOCALE_SUPPORTABLE_LIST = new LinkedList<String>();
//    static{
//    	LOCALE_SUPPORTABLE_LIST.add(LOCALE_CHINESE_CHINA);
//    	LOCALE_SUPPORTABLE_LIST.add(LOCALE_CHINESE_TAIWAN);
//    	LOCALE_SUPPORTABLE_LIST.add(LOCALE_ENGLISH_UNITEDSTATES);
//    }
    
    static{
    	STATUS_NORMAL_AND_WAITING.add(STATUS_NORMAL);
    	STATUS_NORMAL_AND_WAITING.add(STATUS_WAITING);
    }
}
