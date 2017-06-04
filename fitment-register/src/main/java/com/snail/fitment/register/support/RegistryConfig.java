package com.snail.fitment.register.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.snail.fitment.common.lang.StringUtils;
import com.snail.fitment.common.namespace.URL;
import com.snail.fitment.common.network.NetUtils;

/**
 * RegistryConfig
 * 
 * @author Dengyiping
 * @export
 */
@Component
public class RegistryConfig implements Serializable {

	private static final long serialVersionUID = 5508512956753757169L;
	
	public static final String NO_AVAILABLE = "N/A";

    public RegistryConfig() {
    }
    
    public RegistryConfig(String host, int port) {
        setHost(host);
        setPort(port);
    }
    
    // 注册中心地址
    private String            host = null;
    
    // 注册中心用户名
    private String            username  = null;
    
    // 注册中心密码
    private String            password  = null;
   
    // 注册中心缺省端口
    private Integer           port = 2181;
    
    // 注册中心协议
    private String            protocol  = null;

    // 注册中心路径（mysql对应database）
    private String            path  = null;   
    
    private String            group   = null;

	private String            version  = null;
	
	private String            exHost  =  null;
	
	private Integer           exPort  =  62722;
	
	private String            exProtocol = "TCP";
	
	private String            platform = null;
	
	private String            domain  = null;

    // 注册中心请求超时时间(毫秒)
    private Integer           timeout = 2000;
    
    // 动态注册中心列表存储文件
    private String            file  = null;
    
    // 启动时检查注册中心是否存在
    private Boolean           check = false;

    // 在该注册中心上注册是动态的还是静态的服务
    private Boolean           dynamic= false;
    
    // 在该注册中心上服务是否暴露
    private Boolean           register= false;
    
    // 在该注册中心上服务是否引用
    private Boolean           subscribe = false;

    // 自定义参数
    private Map<String, String> parameters  = null;

    // 是否为缺省
    private Boolean             isDefault = false;

    public URL getRegistryURL(){
    	return new URL(this.protocol, this.host,  this.port);
    }
    
    public List<URL> getServiceURL(){
    	List<URL> urlList = new ArrayList<URL>();
    	Map<String, String> params = new HashMap<String, String>();
    	if(this.domain != null){
    		params.put("group", group);
    		params.put("version", version);
    		String[] domainList = domain.split(",");
    		if(StringUtils.isEmpty(exHost)){
    			exHost = NetUtils.getLocalHost();
    		}
    		
    		for(String tmpDomain : domainList){
    			params.put("platform", this.platform);
    			params.put("domain", tmpDomain);
    			urlList.add(new URL(this.protocol, this.exHost, this.exPort, this.path, params));
    		}
    	}
    	
    	return urlList;
    }
    
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Boolean getDynamic() {
		return dynamic;
	}

	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}

	public Boolean getRegister() {
		return register;
	}

	public void setRegister(Boolean register) {
		this.register = register;
	}

	public Boolean getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Boolean subscribe) {
		this.subscribe = subscribe;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getExHost() {
		return exHost;
	}

	public void setExHost(String exHost) {
		this.exHost = exHost;
	}

	public Integer getExPort() {
		return exPort;
	}

	public void setExPort(Integer exPort) {
		this.exPort = exPort;
	}

	public String getExProtocol() {
		return exProtocol;
	}

	public void setExProtocol(String exProtocol) {
		this.exProtocol = exProtocol;
	}
}