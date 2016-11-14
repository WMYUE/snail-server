package com.snail.fitment.common.aop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * 属性文件操作工具类 date:2010-10-27
 * 
 * @author zhaobo
 * 
 */
public class PropertiesUtil {
	private static Properties properties = new Properties();
	private static InputStream in;
	
	/**
	 * 记录属性文件改变时间
	 */
	public static long oldModifyTime = 0;
	
	public static boolean LOCK = false; 
	
	private static Logger logger = Logger.getLogger(PropertiesUtil.class);
	/**
	 * 从属性文件中根据Key取值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String key,String propertiesFileLocation) {
		File f = new File(PropertiesUtil.class.getResource("/").getPath() + propertiesFileLocation) ;
		logger.info("properties path="+f.getPath());
		try {
			if(f.exists()){
				//如果本次文件修改时间和上一次修改时间不等，重新读取配置信息
				if(oldModifyTime != f.lastModified()){
					in = null;
					in = PropertiesUtil.class.getClassLoader().getResourceAsStream(
							propertiesFileLocation);
					try {
						properties.load(in);
					} catch (IOException e) {
						in.close();
						logger.info(e);
					}
					//记录本次修改时间
					oldModifyTime = f.lastModified();
				} else {
					if (in == null) {
						in = PropertiesUtil.class.getClassLoader().getResourceAsStream(
								propertiesFileLocation);
					}
					try {
						properties.load(in);
					} catch (IOException e) {
						in.close();
						logger.info(e);
					}
				}
				String val = properties.get(key).toString();
				logger.info("properties value="+val);
				in.close();
				return val;
			} else {
				logger.info("配置文件：" + propertiesFileLocation + "不存在！");
			}
		} catch (Exception e) {
			try {
				in.close();
			} catch (IOException e1) {
				logger.info(e1);
			}
			logger.info(e);
		}
		return null;
		
	}
	
	public static String getValueByKey(String key) {
		return getValueByKey(key, "config.properties");
	}
	
	 public static void updateKey(String key, String value,String propertiesFileLocation) {
	        Properties prop = new Properties();
	        InputStream fis = null;
	        OutputStream fos = null;
	        String fPath = "";
	        try {
	        	File file = new File(PropertiesUtil.class.getResource("/").getPath() + propertiesFileLocation) ;
	        	if (!file.exists())
	                file.createNewFile();
	        	fPath = file.getName();
	            fis = new FileInputStream(file);
	            prop.load(fis);
	            fis.close();//一定要在修改值之前关闭fis
	            fos = new FileOutputStream(file);
	            prop.setProperty(key, value);
	            prop.store(fos, "Update '" + key + "' value");
	            fos.close();
	            
	        } catch (IOException e) {
	            System.err.println("Visit " + fPath + " for updating "
	            + value + " value error");
	        } 
	        finally{
	            try {
	                fos.close();
	                fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    } 
	 public static void main(String[] args){
//		 updateKey("tCountPerCpu", "4");
		 System.out.println(System.getProperty("user.dir"));
//		 System.out.println(getValueByKey("ip"));
	 }
}
