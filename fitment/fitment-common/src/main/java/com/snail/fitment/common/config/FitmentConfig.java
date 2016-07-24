package com.snail.fitment.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FitmentConfig extends Configure{
	public static Map<String,Object> getProperties(){
		Map<String,Object> map=new HashMap<>();
		Iterator<String> keyIterator=config.getKeys();
		while(keyIterator.hasNext()){
			String key=keyIterator.next();
			if(!key.contains("password")){
				map.put(key, config.getProperty(key));
			}
			
		}
		return map;
	}
	
	public static String getConfigFile(){
		return config.getBasePath();
	}
	
	/**
	 * 重载配置文件的时间间隔，默认60s
	 * @return
	 */
	public static long getReloadPropertiesInterval(){ return getProperty("reload.properties.interval",60000); }
	
	/**
	 * 连接节点
	 */
	public static final String connectionNode=getProperty("protocol.node","node1");
	
	/**
	 * 域平台标示，标示是否需要跨域传
	 */
	public static final String domainServerId=getProperty("domain.serverid","lanxin");
	/**
	 * 其他平台的是否转换为统一的代理，同步到对应平台，然后再分发具体用户同步信息
	 */
	public static final boolean otherServer2Proxy=getProperty("server.proxy",false);
	/*----------------------协议相关配置------------------------*/
	/**
	 * 	当 defaultIdleTimeout内没有给客户端发送数据时，断开连接
	 * （默认使用服务端发送数据作为超时判断依据,
	 * 以避免客户端持续发送无意义的数据导致服务器不断开连接的情况）,
	 * 单位为毫秒, 一般无需修改
	 */
	public static final int defaultIdleTimeout=getProperty("protocol.defaultIdleTimeout",90000);
	/**
	 * session超时时间，默认60分钟
	 */
	public static final int defaultSessionTimeout=getProperty("protocol.defaultSessionTimeout",3600000);
	/**
	 * 心跳超时时间，默认为6分钟
	 */
	public static final int heartTimeout() {return getProperty("protocol.heartTimeout",360000);};
	/**
	 * 服务端单向心跳间隔，服务端发包，客户端不回包，验证服务端保持连接方法，0 表示不发
	 */
	public static final int serverHeartInterval = getProperty("protocol.serverHeartInterval", 0); // 默认为 0 ，不发，与以前的程序保持一致
	
	public static final int BP_NORMAL_TASK_POOL_CORE_SIZE = getProperty("bp.normal.pool.core",2);
	public static final int BP_NORMAL_TASK_POOL_MAX_SIZE = getProperty("bp.normal.pool.max",Runtime.getRuntime().availableProcessors() * 10);
	public static final int BP_NORMAL_TASK_QUEUE_SIZE = getProperty("bp.normal.queue",BP_NORMAL_TASK_POOL_MAX_SIZE * 500 / 20);
	
	public static final int BP_FIRST_TASK_POOL_CORE_SIZE = getProperty("bp.first.pool.core",2);
	public static final int BP_FIRST_TASK_POOL_MAX_SIZE = getProperty("bp.first.pool.max",Runtime.getRuntime().availableProcessors() * 10);
	public static final int BP_FIRST_TASK_QUEUE_SIZE = getProperty("bp.first.queue",BP_FIRST_TASK_POOL_MAX_SIZE * 1000 / 100);
	
	public static final int BP_SEND_TASK_POOL_CORE_SIZE = getProperty("bp.send.pool.core",2);
	public static final int BP_SEND_TASK_POOL_MAX_SIZE = getProperty("bp.send.pool.max",Runtime.getRuntime().availableProcessors() * 10);
	public static final int BP_SEND_TASK_QUEUE_SIZE = getProperty("bp.send.queue",BP_SEND_TASK_POOL_MAX_SIZE * 1000 / 100);

	/**
	 * 资源相关配置
	 * @return
	 */
	public static final String FILE_ROOT_PATH = getProperty("file.root.path","/opt/lanxin/data/");
	//资源文件下载地址头
	public static final  String RESOURCE_DOWNLOAD_ADDRESS = getProperty("http.download.address","http://uniform.wqapp.cn/");
	/**
	 * 文件系统类型
	 */
	public static final  String FILE_STRORAGE_TYPE = getProperty("file.storage.type","fastdfs");
	/**
	 * CDN下载配置
	 */
	public static final boolean CDN_IS_AVAILABLE = getProperty("cdn.is.available",false);
	public static final String CDN_UPLOAD_PATH = getProperty("cdn.upload.path","/opt/lanxin/data/blueprint/download");
	public static final String CDN_DOWNLOAD_PATH = getProperty("cdn.download.path","http://cdn.lanxin.cn/d/up/blueprint/");
	
	
	//资源大小限制（2G）
	public static final long getResourceMaxSize(){return getProperty("resource.max.size", 100*1024*1024);};	
	//块资源大小限制（合理为10M，目前设置为100M同资源大小，因为web未实现分块上传下载）
	public static final long getPartResourceMaxSize(){return getProperty("part.resource.max.size", 2147483647);};

	/**
	 * redis 相关设置
	 */
	public static final String REDIS_SERVER= getProperty("redis.server","192.168.1.214");
	public static final	int REDIS_PORT=getProperty("redis.port", 6379);
	public static final	int REDIS_TIMEOUT=getProperty("redis.timeout", 2000);
	public static final String REDIS_PASSWD=getProperty("redis.password", "");
	
	/**
	 * kafka 相关设置
	 */
	public static final String ZOOKEEPER_CONNECTION=getProperty("zk.connect","172.168.63.221:9092");
	public static final String kafka_metadata_broker_list = getProperty("metadata.broker.list","172.168.63.221:9092");
	public static final String kafka_serializer_class = getProperty("serializer.class","kafka.serializer.StringEncoder");
	public static final String kafka_key_serializer_class = getProperty("key.serializer.class","kafka.serializer.StringEncoder");
	public static final String kafka_partitioner_class = getProperty("partitioner.class","com.catt.kafka.demo.PartitionerDemo");
	public static final String kafka_request_required_ack = getProperty("request.required.ack","1");
	

	public static final int compressThreshold =  getProperty("protocol.compressThreshold",512); // 数据包压缩的阈值（body 超过此大小则进行压缩，单位为 byte）
	
	/**
	 * mongodb 相关设置
	 */
	public static final String MONGODB_SERVER= getProperty("mongodb.url","demo.lanxin.cn");
	public static final	int MONGODB_PORT=getProperty("mongodb.port", 27017);
	public static final	String MONGODB_USER=getProperty("mongodb.username", "");
	public static final String MONGODB_PASSWORD=getProperty("mongodb.password", "");
	public static final String MONGODB_DATABASE=getProperty("mongodb.db", "bptest");
	//默认分页大小
	public static final int PAGE_SIZE = 6;
	
	public static Map<Object, String> m = new HashMap<Object, String>();
	
	static{
		m.put(0, "$regex");
		m.put(1, "$ne");//不相似、等确认？
		m.put(2, "$regex");//相似
		m.put(3, "$ne");
		m.put(4, "$gt");
		m.put(5, "$lte");
		m.put(6, "$lt");
		m.put(7, "$gte");
		m.put(8, "$in");
		m.put(9, "$nin");
		m.put(10, "$and");
		m.put(11, "$or");
		m.put(112, "$exists");
	}
	
	public static String getValue(int key){
		String value = m.get(key);
		return value;
	}
	
	public static String getResourceVersion(String modelName){
		return getProperty(modelName+".resources.version","1.0");
	}
	
	
	public static final String EMALI_VALID_CHECK_URL=getProperty("email.valid.check.url", "https://demo.lanxin.cn:8088/");
	
	//fast DFS相关配置
	public static final String FDFS_CONFIG_PATH = getProperty("fdfs.config.path", "/gudong/server/conf/fdfs_client.conf");
	
	//mysql 相关配置
	/**
	 * 数据分批处理的批次大小，经过测试，1000是个比较合理的值
	 */
	public static final int DATA_BATCH_SIZE=getProperty("data.batch.size",5000);
	
	
	//平台证书路径（旧证书：gudong_protocol_server.pfx 新证书：lanxin_server.pfx）
	public static final String SERVER_JKS_PATH=getProperty("server.jks.path","ca/gudong_protocol_server.pfx");
	//平台证书访问口令（旧证书口令：comisys 新证书口令：comisys1406）
	public static final String SERVER_JKS_PASSWORD=getProperty("server.jks.password","comisys");
	//平台证书路径 （支持lanxin_ca.crt和gudong_ca.crt）
	public static final String SSL_TRUST_JKS_PATH=getProperty("ssl.trust.jks.path","ca/ssltrust.jks");
	//平台证书访问口令
	public static final String SSL_TRUST_JKS_PASSWORD=getProperty("ssl.trust.jks.path","comisys1406");
	
	
	/**
	 * 用于profile分析
	 */
	public static final int PERFORMANCE_THRESHOLD =getProperty("performance.threshold",10);
	
	public static final String LANXIN_OPEN_URL =  getProperty("lanxin.open.url","https://api.lanxin.cn/");
	public static final String LANXIN_APP_ID =  getProperty("lanxin.app.id", "https://api.lanxin.cn/");
	
	public static final String SALARY_APP_ID = getProperty("salary.app.id", "100010");
	
	public static final boolean REGISTRY_SWITCH = getProperty("registry.switch", false);
	
}
