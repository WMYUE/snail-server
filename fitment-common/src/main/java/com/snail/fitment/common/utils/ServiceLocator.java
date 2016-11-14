package com.snail.fitment.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

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