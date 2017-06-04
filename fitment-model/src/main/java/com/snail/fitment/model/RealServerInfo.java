package com.snail.fitment.model;

import java.io.Serializable;

public class RealServerInfo extends BaseModel implements Serializable, IIdAware {
	private static final long serialVersionUID = 463317516105836178L;

	private Long id;
	private String domain;
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
