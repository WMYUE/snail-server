package com.snail.fitment.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ResourceInfo extends BaseModel implements Serializable, IIdAware {
	
	private static final long serialVersionUID = -3983447220983555258L;

	private Long id;
	
	private String resourceId;
	
	private String fileName;
	
	private String systemFileName;
	
	private String userUniId;
	
	private String mimeType;
	
	private long size = 0;
	
	private String filePath;
	
	private String md5;
	
	private byte[] content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSystemFileName() {
		return systemFileName;
	}

	public void setSystemFileName(String systemFileName) {
		this.systemFileName = systemFileName;
	}

	public String getUserUniId() {
		return userUniId;
	}

	public void setUserUniId(String userUniId) {
		this.userUniId = userUniId;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	

	@Override
	public String toString() {
		return "ResourceModel [id=" + id + ", resourceId=" + resourceId
				+ ", fileName=" + fileName + ", systemFileName="
				+ systemFileName + ", userUniId=" + userUniId + ", mimeType="
				+ mimeType + ", size=" + size + ", filePath=" + filePath
				+ ", md5=" + md5 + "]";  //content不打印
	}
	
}
