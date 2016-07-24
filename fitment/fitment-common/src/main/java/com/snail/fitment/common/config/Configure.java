package com.snail.fitment.common.config;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * 如需支持动态加载配置文件，请配置环境变量 GUDONG_HOME,指向安装目录
 * 
 * 并将变量的获取方法改为 get方法，具体可参考GudongConfig中的getReloadPropertiesInterval()方法。
 * 
 * @author suxiaoyong
 * 
 */
@Component("configure")
public class Configure {

	private static final Logger logger = Logger.getLogger(Configure.class);
	static final Properties configProerties = new Properties();
	static PropertiesConfiguration config = new PropertiesConfiguration();
	static {
		try{
			//装载配置文件中配置项
			init("/config.properties","BLUEPRINT_HOME");	
		}catch(Exception e){
			logger.error("Configure init,error:",e);
		}

	}
	
	//保留原从配置文件中读取的功能

	protected static void init(final String propertyFileName,final String homedir) throws ConfigurationException {
		Map<String,String> systemVariableMap = System.getenv();
		String path=systemVariableMap.get(StringUtils.upperCase(homedir));
		if(StringUtils.isEmpty(path)){
			path=systemVariableMap.get(StringUtils.lowerCase(homedir));
		}
		//分隔符失效,用","隔开的分隔符仍然解析成字符串，不解析成list
		config.setDelimiterParsingDisabled(true);
		if(StringUtils.isNotEmpty(path)){
			path=new StringBuilder(path).append(File.separator).append("conf").append(File.separator).append(propertyFileName).toString();
			File file=new File(path);
			if(file.exists()){
				config.load(path);
			}else{
				logger.warn("没有找到文件"+path+",从classpath中加载"+propertyFileName);
				
				loadConfigFromClassPath(propertyFileName);
			}
		}else{
			loadConfigFromClassPath(propertyFileName);
		}
		//配置文件文件自动重载策略
		FileChangedReloadingStrategy reloadStrategy=new FileChangedReloadingStrategy();
		reloadStrategy.setRefreshDelay(FitmentConfig.getReloadPropertiesInterval());
		config.setReloadingStrategy(reloadStrategy);
	}


	private static void loadConfigFromClassPath(final String propertyFileName)
			throws ConfigurationException {
		URL fileUrl=Configure.class.getResource(propertyFileName);
		if (fileUrl != null) {
			config.load(fileUrl);
		} 
	}
	

	protected static String getProperty(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	protected static boolean getProperty(String key, boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	protected static int getProperty(String key, int defaultValue) {
		return config.getInt(key, defaultValue);
	}

	protected static long getProperty(String key, long defaultValue) {
		return config.getLong(key, defaultValue);
	}
	
	/**
	 * 逗号分隔的多个配置可以直接返回为一个列表
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected static List getProperty(String key, List defaultValue) {
		return config.getList(key, Lists.newArrayList());
	}

}
