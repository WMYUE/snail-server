package com.snail.fitment.register.mysql;

import com.snail.fitment.common.namespace.URL;
import com.snail.fitment.register.api.Registry;
import com.snail.fitment.register.support.AbstractRegistryFactory;

public class MysqlRegistryFactory extends AbstractRegistryFactory{

	@Override
	protected Registry createRegistry(URL url) {
		return new MysqlRegistry(url);
	}
	
}
