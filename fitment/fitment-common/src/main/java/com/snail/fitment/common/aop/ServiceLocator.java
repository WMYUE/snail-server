package com.snail.fitment.common.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 璧勬簮瀹氫綅鍣紝缁ф壙浜咮eanFactoryAware鏂规硶銆�
 * 浣跨敤鏂规硶濡備笅锛� ServiceLocator.getService(beanName) 鍙傛暟beanName涓篠pring閰嶇疆鏂囦欢涓负bean鎵�閰嶇疆鐨勫悕瀛椼��
 * 璇锋敞鎰忓湪浣跨敤鍓嶉渶鍦⊿pring xml閰嶇疆鏂囦欢涓０鏄庡苟鍒濆鍖栬serviceLocator
 * <bean name="serviceLocator" class="com.comisys.gudong.server.common.utils.ServiceLocator" scope="singleton" lazy-init="false"/>
 * 
 * 鍏朵腑 scope="singleton" lazy-init="false" 涓や釜閰嶇疆寰堥噸瑕併�� 
 * 
 */
public class ServiceLocator implements BeanFactoryAware {
		
	    private static BeanFactory beanFactory = null;

	    private static ServiceLocator servlocator = null;

	    public void setBeanFactory(BeanFactory factory) throws BeansException {
	        beanFactory = factory;
	    }

	    public BeanFactory getBeanFactory() {
	        return beanFactory;
	    }

	    /**
	    * 鍒涘缓璇诲彇Bean鏈嶅姟绫诲疄渚�(浠巗pring.xml涓姞杞�)
	    */
	    public static ServiceLocator getInstance() {
	        if (servlocator == null)
	              servlocator = (ServiceLocator) beanFactory.getBean("serviceLocator");
	        return servlocator;
	    }

	    /**
	    * 鏍规嵁鎻愪緵鐨刡ean鍚嶇О寰楀埌鐩稿簲鐨勬湇鍔＄被     
	    * @param servName bean鍚嶇О     
	    */
	    public static Object getBean(String servName) {
	        return beanFactory.getBean(servName);
	    }

	    /**
	    * 鏍规嵁鎻愪緵鐨刡ean鍚嶇О寰楀埌瀵瑰簲浜庢寚瀹氱被鍨嬬殑鏈嶅姟绫�
	    * @param servName bean鍚嶇О
	    * @param clazz 杩斿洖鐨刡ean绫诲瀷,鑻ョ被鍨嬩笉鍖归厤,灏嗘姏鍑哄紓甯�
	    */
	    public static Object getBean(String servName, Class clazz) {
	        return beanFactory.getBean(servName, clazz);
	    }


	}

