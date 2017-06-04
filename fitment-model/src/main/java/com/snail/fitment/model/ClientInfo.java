package com.snail.fitment.model;

import java.io.Serializable;
import java.util.Date;

public class ClientInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CLIENT_NATIVE_ID_SYSTEM = "system";
	public static final String CLIENT_NATIVE_ID_SMS = "sms";
	public static final long CLIENT_ID_SYSTEM = 0L;

    public static final int CLIENT_TYPE_SYSTEM = 0; // 后台
	public static final int CLIENT_TYPE_MOBILE = 1;
	public static final int CLIENT_TYPE_WEB = 2;
	public static final int CLIENT_TYPE_PC = 3;
	public static final int CLIENT_TYPE_SMS = 4;
	
	 public static final String OS_SYMBIAN="Symbian";
	 public static final String OS_ANDROID="Linux";
	 public static final String OS_IOS="iPhone OS";
	 public static final String OS_Windows="Windows PC";
	 public static final String OS_Mac="Mac OS";
	 
	 public static final int EDITION_ALL = -1;
	 
	 
	/**
	 * 客户端id
	 * 系统生成
	 */
    private long id;
	/**
	 * 客户端自身Id
	 * 客户端的唯一标识，由客户端自动生成，客户端需要保证唯一性
	 * (客户端生成规则？)
	 */
    private String clientNativeId;
	/**
	 * 客户端类型
	 * 1: 手机
	 * 2: WEB
	 * 3: PC
	 */
    private int clientType=0;
	/**
	 * 终端操作系统
	 * Sybian，IPhone，Android
	 */

    private String osName;
    
	/**
	 * 终端操作系统版本
	 * S60v3...
	 */

    private String osVersion;
    
	/**
	 * 终端厂商
	 * Nokia 等
	 */

    private String platformVendor;
    
	/**
	 * 终端型号
	 * N95、E61、iphone 3GS
	 */

    private String platformModel;
    
	/**
	 * 屏幕分辨率-宽
	 */

    private int screenWidth;

	/**
	 * 屏幕分辨率-高
	 */
    private int screenHeight;
	/**
	 * 客户端主版本号
	 */
    private String versionMajor;
    
	/**
	 * 客户端次版本号
	 */
    private String versionMin;

    private Date createTime=new Date();

    private Date modifyTime=new Date();
    
    private byte cryptoType = 0;
    
    private byte compressType = 1;
    
    private String locale;
    
    private byte asyAlgType = 0;
    
    private int edition = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientNativeId() {
        return clientNativeId;
    }

    public void setClientNativeId(String clientNativeId) {
        this.clientNativeId = clientNativeId == null ? null : clientNativeId.trim();
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName == null ? null : osName.trim();
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion == null ? null : osVersion.trim();
    }

    public String getPlatformVendor() {
        return platformVendor;
    }

    public void setPlatformVendor(String platformVendor) {
        this.platformVendor = platformVendor == null ? null : platformVendor.trim();
    }

    public String getPlatformModel() {
        return platformModel;
    }

    public void setPlatformModel(String platformModel) {
        this.platformModel = platformModel == null ? null : platformModel.trim();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getVersionMajor() {
        return versionMajor;
    }

    public void setVersionMajor(String versionMajor) {
        this.versionMajor = versionMajor == null ? null : versionMajor.trim();
    }

    public String getVersionMin() {
        return versionMin;
    }

    public void setVersionMin(String versionMin) {
        this.versionMin = versionMin == null ? null : versionMin.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
 
	public byte getCryptoType() {
		return cryptoType;
	}

	public void setCryptoType(byte cryptoType) {
		this.cryptoType = cryptoType;
	}

	public byte getCompressType() {
		return compressType;
	}

	public void setCompressType(byte compressType) {
		this.compressType = compressType;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public byte getAsyAlgType() {
		return asyAlgType;
	}

	public void setAsyAlgType(byte asyAlgType) {
		this.asyAlgType = asyAlgType;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	@Override
	public String toString() {
		return "ClientInfo [id=" + id + ", clientNativeId=" + clientNativeId + ", clientType=" + clientType + ", osName=" + osName + ", osVersion=" + osVersion
				+ ", platformVendor=" + platformVendor + ", platformModel=" + platformModel + ", screenWidth=" + screenWidth + ", screenHeight=" + screenHeight
				+ ", versionMajor=" + versionMajor + ", versionMin=" + versionMin + ", createTime=" + createTime + ", modifyTime=" + modifyTime
				+ ", cryptoType=" + cryptoType + ", compressType=" + compressType + ", locale=" + locale + ", asyAlgType=" + asyAlgType + ", edition="
				+ edition + "]";
	}
}