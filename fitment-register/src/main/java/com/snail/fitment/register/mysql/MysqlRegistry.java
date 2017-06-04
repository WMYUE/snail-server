package com.snail.fitment.register.mysql;

import com.snail.fitment.common.namespace.URL;
import com.snail.fitment.register.support.AbstractRegistry;


public class MysqlRegistry extends AbstractRegistry{

	public MysqlRegistry(URL url) {
		super(url);
	}

	@Override
	public boolean isAvailable() {
		return false;
	}

}
