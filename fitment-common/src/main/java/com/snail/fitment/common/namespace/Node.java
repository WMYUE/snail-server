package com.snail.fitment.common.namespace;

/**
 * Node. (API/SPI, Prototype, ThreadSafe)
 * 
 * @author Deng Yiping
 */
public interface Node {

    /**
     * get url.
     * 
     * @return url.
     */
    URL getUrl();
    
    /**
     * is available.
     * 
     * @return available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();

}