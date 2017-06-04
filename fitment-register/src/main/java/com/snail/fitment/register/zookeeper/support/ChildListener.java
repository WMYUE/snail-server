package com.snail.fitment.register.zookeeper.support;

import java.util.List;

public interface ChildListener {
	public void childChanged(String path, List<String> childUrls);
}
