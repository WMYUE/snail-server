package com.snail.fitment.common.constants;

public final class OperationConstants {	
	/*
	 * 操作码类型定义
	 * */
	public static final byte TYPE_REQ = 1;
	public static final byte TYPE_RES = 2;

	/*
	 * 资源管理操作码定义, 1***
	 * */
	public static final int OP_DOWN_SYNC_REF_SCHEME = 1105;
	
	public static final int OP_UP_SYNC_RES_PART = 1101;
	public static final int OP_UP_SYNC_RES_COMPLETE = 1102;	
	public static final int OP_QUERY_RES = 1103;
	public static final int OP_DOWNLOAD_RES = 1104;
	public static final int OP_QUERY_SERVER_INFO = 1105;
	public static final int COMPLETE_RES = 1106;
	public static final int OP_QUERY_RUNTIME_INFO = 1107;
	public static final int OP_QUERY_SERVER_CONFIG = 1108;
	/*
	 * 应用管理操作码定义, 2***
	 * */
	public static final int OP_APP_INFO_SYNC = 2101;
	public static final int OP_APP_INFO_SYNC_BY_ID = 2103;
	public static final int OP_APP_SETTINGS_SYNC = 2104;
	
	/*
	 * 数据管理操作码定义,3***
	 * */
	public static final int OP_QUERY_DATA = 3101;
	public static final int OP_MODIFY_DATA = 3102;
	public static final int OP_DOWN_SYNC_DATA = 3103;
	public static final int OP_QUERY_DATA_BY_COMMAND = 3104;
	
	public static final int OP_CREATE_DATA_BY_COMMAND = 3105;
	
	public static final int OP_QUERY_DATA_RECORD_MODEL = 3106;
	
	public static final int OP_IMPORT_DATA = 3107;
	
	public static final int OP_QUERY_ORG_DATA = 3108;
	public static final int OP_QUERY_USERS_BY_TAG = 3109;
	public static final int OP_QUERY_MEMBER_INFO = 3110;
	public static final int OP_QUERY_RESOURCE_INFO = 3111;
	public static final int OP_QUERY_STATISTICS_DATA = 3112;
//	public static final int OP_CHECK_OUT_CONF_RES = 3104;
//	public static final int OP_QUERY_CONF_VES = 3105;
//	public static final int OP_QUERY_SERVER_COMMAND = 3106;
	

	/*
	 * 会话管理操作码定义,4***
	 * */
	public static final int OP_SESSION_OAUTH = 4101;
	public static final int OP_SESSION_DEVELOPER_LOGIN = 4102;
	public static final int OP_SESSION_USER_ROLE_TAGS = 4103;
	public static final int OP_SESSION_ROLE_TAG_USERS = 4104;
	public static final int OP_SESSION_REFRESH_AUTH = 4105;
	public static final int OP_SESSION_LOGOUT = 4106;
	public static final int OP_AUTH_BY_SESSION = 4107;
	public static final int OP_SESSION_REFRESH = 4108;
	public static final int OP_SESSION_RANDOM_SALT = 4109;
	public static final int OP_SESSION_SEND_CHECK_CODE = 4110;//发送验证码
	public static final int OP_SESSION_RESET_PASSWORD = 4111;//修改密码
	/*
	 * 同步管理操作码定义,5***
	 *
	 * */
	public static final int OP_SYNCH_BATCH = 5201;
	
	public static final int OP_UP_BEHAVIOR_DATA = 6101;
	
	public static final int OP_INIT_CONNECTION=7108;
	
	/*
	 * 只同步源数据
	 */
	public static final int OP_UP_EXCHANGE_DATA=8101;
	public static final int OP_DOWN_EXCHANGE_DATA=8201;
	
	/**
	 * 发送服务端日志
	 */
	public static final int OP_PUSH_LOGS_TO_WEBSERVER = 9001;
	
	
	/*
	 * web协议操作码定义,101***
	 * 101001~101019, 开发者相关协议
	 * */
	public static final int OP_WEB_APPLY_DEVELOPER = 101001;
	public static final int OP_WEB_MODIFY_DEVELOPER = 101002;
	public static final int OP_WEB_DELETE_DEVELOPER = 101003;
	public static final int OP_WEB_QUERY_DEVELOPER = 101004;
	public static final int OP_WEB_AUTHORIZE_DEVELOPER = 101005;
	public static final int OP_WEB_ACTIVE_DEVELOPER = 101006;
	public static final int OP_WEB_CHECK_DEVELOPER = 101007;
	public static final int OP_WEB_LOGIN_DEVELOPER = 101008;
	/*
	 * web协议操作码定义,101***
	 * 101021~101039, 权限相关协议
	 * */
	public static final int OP_WEB_ADD_PERMISSION = 101021;
	public static final int OP_WEB_MODIFY_PERMISSION = 101022;
	public static final int OP_WEB_DELETE_PERMISSION = 101023;
	public static final int OP_WEB_QUERY_PERMISSION = 101024;
	public static final int OP_WEB_GET_TYPE_PERMISSION = 101025;
	public static final int OP_WEB_SELECT_BY_ID_PERMISSION = 101026;
	
	/*
	 * web协议操作码定义,101***
	 * 101041~101059, 角色相关协议
	 * */
	public static final int OP_WEB_ADD_ROLE = 101041;
	public static final int OP_WEB_MODIFY_ROLE = 101042;
	public static final int OP_WEB_DELETE_ROLE = 101043;
	public static final int OP_WEB_QUERY_ROLE= 101044;
	public static final int OP_WEB_GET_TYPE_ROLE = 101045;
	public static final int OP_WEB_BINDING_PERMISSION_ROLE = 101046;
	
	/*
	 * web协议操作码定义,101***
	 * 101061~101069, app商城相关
	 * */
	public static final int OP_WEB_PUB_APP = 101061; //发布app
	public static final int OP_WEB_QUERY_APP_PUB_INFO_BY_ID = 101062; //查询app
	public static final int OP_WEB_QUERY_APP_PUB_INFO_PAGES_BY_PARAMS = 101063; //查询app
	public static final int OP_WEB_UPDATE_APP_PUBLISH_INFO = 101064; //更新app发布信息
	public static final int OP_WEB_DELETE_APP_PUBLISH_INFO_BY_ID = 101065; //删除app发布信息
	public static final int OP_WEB_INSERT_APPPUSHRECORD_DIRECTLY = 101066; //分发app
	public static final int OP_WEB_INSERT_APPPUSHRECORD_ASYN = 101067; //同步app信息
	/*
	 * web协议操作码定义,101***
	 * 101071~101075, app资源相关
	 * */
	public static final int OP_WEB_UPLOAD_RESOURCE = 101071;
	public static final int OP_WEB_QUERY_RESOURCE = 101072;
	public static final int OP_WEB_DELETE_RESOURCE= 101073;
	
	/*
	 * web协议操作码定义,101***
	 * 101076~101079, app使用日志
	 **/
	public static final int OP_WEB_ADD_APP_USAGE_LOG = 101076;
	
	/*
	 * web协议操作码定义,101***
	 * 101081~101089, app分类信息
	 * */
	public static final int OP_WEB_GET_APPCATEGORY_BY_PID = 101081;
	public static final int OP_WEB_GET_APPCATEGORY_BY_ID = 101082;
	public static final int OP_WEB_GET_ALL_APPCATEGORY = 101083;
	
	/*
	 * 101091~101099 app订阅
	 */
	public static final int OP_WEB_QUERY_SUB_APP = 101091; //订阅app
	public static final int OP_WEB_QUERY_SUB_APP_BY_PARAMS = 101092; //f分页查询app
	public static final int OP_WEB_QUERY_APP_IS_SUBSCRIBED = 101093; //判定是否在订阅app
	public static final int OP_WEB_DELETE_APP_SUBSCRIPTION = 101094; //删除订阅
	public static final int OP_WEB_QUERY_APP_ORDER_NUMBER = 101095; //app订阅人数
	public static final int OP_WEB_CHANGE_APP_CHARGE_PLAN = 101096; //更改app订阅方式
	public static final int OP_WEB_QUERY_SUB_APP_BY_ID = 101097; //更改app订阅方式
	public static final int OP_WEB_GENERATE_APP_BILL = 101098; //生成app账单
	

	
	/*
	 * 查询组织信息
	 */
	public static final int OP_WEB_ORGINFO_QUERY_ORG_DATA_By_STRUCTID = 101201; //查询组织信息
	/*
	 * web协议操作码定义,101***
	 * 101101~101119, 配置相关操作
	 * */
	public static final int OP_WEB_SAVE_CONFIG = 101101;
	
	/*
	 * 101120~101129 数据上下行
	 */
	public static final int OP_WEB_QUERY_DATA = 101120;
	public static final int OP_WEB_MODIFY_DATA = 101121;
	public static final int OP_WEB_IMPORT_DATA = 101122;
	
	
	/**
	 * 快消相关
	 */
	public static final int OP_CONSUMER_REG_WITH_CHECKCODE = 106101;
	
	public static final int OP_CONSUMER_CHANGE_MOBILE = 106102;
	public static final int OP_CONSUMER_CHANGE_PASSWORD = 106103;
	
	public static final int OP_CONSUMER_CHECK_ROLE = 106104;
	public static final int OP_CONSUMER_MODIFY_USER = 106105;
	public static final int OP_CONSUMER_QUERY_CONTACT_LIST = 106106;
	public static final int OP_CONSUMER_DELETE_CONTACT = 106107;
	
	public static final int OP_CONSUMER_QUERY_COMMODITY = 106108;
	public static final int OP_CONSUMER_QUERY_COMMODITY_69 = 106109;
	public static final int OP_CONSUMER_ADD_COMMODITY = 106110;
	
	public static final int OP_CONSUMER_QUERY_SPEC = 106111;
	public static final int OP_CONSUMER_ADD_SPEC = 106112;
	public static final int OP_CONSUMER_DELETE_SPEC = 106113;
	
	public static final int OP_CONSUMER_QUERY_STOCK = 106114;
	
	public static final int OP_CONSUMER_QUERY_PRICE = 106115;
	public static final int OP_CONSUMER_ADD_PRICE = 106116;
	public static final int OP_CONSUMER_DELETE_PRICE = 106117;
	
	public static final int OP_CONSUMER_COMMIT_BILL = 106118;
	public static final int OP_CONSUMER_DELETE_BILL = 106119;
	
	public static final int OP_CONSUMER_COMMIT_BILLDETAIL = 106120;
	public static final int OP_CONSUMER_DELETE_BILLDETAIL = 106121;
	
	public static final int OP_CONSUMER_QUERY_CARGO = 106122;
	
	public static final int OP_CONSUMER_QUERY_ADDR = 106123;
	public static final int OP_CONSUMER_ADD_ADDR = 106124;
	public static final int OP_CONSUMER_DELETE_ADDR = 106125;
	
	public static final int OP_CONSUMER_ADD_CONTACT = 106126;
	public static final int OP_CONSUMER_QUERY_USER = 106127;
	
	public static final int OP_CONSUMER_QUERY_BILL = 106128;
	public static final int OP_CONSUMER_MODIFY_CONTACTOR = 106129;
	
	public static final int OP_CONSUMER_QUERY_CUSTOMER_BY_GROUP = 106130;
	public static final int OP_CONSUMER_QUERY_EXPIRED_COMMODITY = 106131;
	
	public static final int OP_CONSUMER_QUERY_COMMODITY_CATEGORY = 106132;
	public static final int OP_CONSUMER_ADD_COMMODITY_CATEGORYE = 106133;
	public static final int OP_CONSUMER_DELETE_COMMODITY_CATEGORY= 106134;
	public static final int OP_CONSUMER_QUERY_MY_BOSS= 106135;
	public static final int OP_CONSUMER_MODIFY_STOCK_INFO = 106136;
	public static final int OP_CONSUMER_MANUAL_COMMIT_ACCOUNT_BILL_DETAIL = 106137;
	public static final int OP_CONSUMER_QUERY_ACCOUNT_BILL_DETAIL = 106138;
	public static final int OP_CONSUMER_QUERY_CUSTOMER_HAS_ORDER_IN_MONTH = 106139;
	public static final int OP_CONSUMER_QUERY_CUSTOMER_HAS_DEBIT = 106140;
	public static final int OP_CONSUMER_MODIFY_COMMODITY = 106141;
	public static final int OP_CONSUMER_QUERY_CITY = 106142;
	public static final int OP_CONSUMER_QUERY_ALL_CITY = 106143;
}
