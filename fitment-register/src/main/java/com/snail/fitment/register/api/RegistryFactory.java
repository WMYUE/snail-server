package com.snail.fitment.register.api;


import com.snail.fitment.common.namespace.URL;

/**
 * RegistryFactory. (SPI, Singleton, ThreadSafe)
 * 
 * @see com.snail.fitment.register.support.AbstractRegistryFactory
 * @author Dengyiping
 */

public interface RegistryFactory {
    /**
     * 连接注册中心.
     * 
     * 连接注册中心需处理契约：<br>
     * 
     * @param url 注册中心地址，不允许为空
     * @return 注册中心引用，总不返回空
     */
    Registry getRegistry(URL url);

}