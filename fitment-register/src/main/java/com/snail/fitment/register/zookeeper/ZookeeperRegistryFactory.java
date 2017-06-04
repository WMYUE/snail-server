package com.snail.fitment.register.zookeeper;

import com.snail.fitment.common.namespace.URL;
import com.snail.fitment.register.api.Registry;
import com.snail.fitment.register.support.AbstractRegistryFactory;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {
	
	public Registry createRegistry(URL url) {
        return new ZookeeperRegistry(url);
    }

}