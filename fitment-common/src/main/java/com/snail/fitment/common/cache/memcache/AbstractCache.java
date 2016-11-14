package com.snail.fitment.common.cache.memcache;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractCache implements ICache {

	protected String version;
	protected String region;
	protected int timeout;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRegion() {
		if (StringUtils.isEmpty(this.version)) {
			return this.region;
		} else {
			return new StringBuilder(this.region).append("-")
					.append(this.version).toString();
		}
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
