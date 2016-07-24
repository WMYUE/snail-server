package com.snail.fitment.common.config;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.snail.fitment.common.security.CertUtil;

public class CaConfig {
	private static final Logger logger = Logger.getLogger(CaConfig.class);
	public static String publicKey = null;
	public static String certString = null;
	public static String privateKey = null;
	public static String serialNumber = null;
	public static List<X509TrustManager> trustManagerList = null;
	
	static {
		try {
			init();
		} catch (Exception e) {
			logger.error("初始化服务端证书失败，请检查" + FitmentConfig.SERVER_JKS_PATH + "，Exception",e);
		}
		try {
			initDefaultTrustManager();
		} catch (Exception e) {
			logger.error("初始化SSL信任库失败，请检查" + FitmentConfig.SSL_TRUST_JKS_PASSWORD + "，Exception",e);
		}
		logger.warn("CaConfig init success.");
	}
	
	private static void init() throws Exception{
		InputStream inputStream = CaConfig.class.getClassLoader().getResourceAsStream(FitmentConfig.SERVER_JKS_PATH);
		KeyStore ks = KeyStore.getInstance("pkcs12");
        ks.load(inputStream,FitmentConfig.SERVER_JKS_PASSWORD.toCharArray());
        Enumeration<String> aliases = ks.aliases();
        String keyAlias = "";
        while (aliases.hasMoreElements()) {
            keyAlias = aliases.nextElement();
            break;
        }        
        X509Certificate cert = (X509Certificate)ks.getCertificate(keyAlias);
        PrivateKey key = (PrivateKey) ks.getKey(keyAlias, FitmentConfig.SERVER_JKS_PASSWORD.toCharArray());
        certString = Base64.getEncoder().encodeToString(cert.getEncoded());
        publicKey = Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded());
        privateKey = Base64.getEncoder().encodeToString(key.getEncoded());   
        serialNumber = CertUtil.getSerialNumber(CaConfig.certString);
	}
	
	public static List<X509TrustManager> getDefaultTrustManager() throws Exception{
		return trustManagerList;
	}
	
	private static final List<X509TrustManager> initDefaultTrustManager() throws CertificateException, Exception{
    	// 载入 jks 文件
    	KeyStore ks = KeyStore.getInstance("JKS");
    	InputStream is = CaConfig.class.getClassLoader().getResourceAsStream(FitmentConfig.SSL_TRUST_JKS_PATH);
    	ks.load(is, FitmentConfig.SSL_TRUST_JKS_PASSWORD.toCharArray());
    	TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
    	tmf.init(ks);        
    	TrustManager[] tms = (TrustManager[]) tmf.getTrustManagers();        
    	// 筛选出 X509 格式的信任证书        
    	trustManagerList = new ArrayList<X509TrustManager>();
    	for (int i = 0; i < tms.length; i++) {
	    	if (tms[i] instanceof X509TrustManager) { 
	    		trustManagerList.add((X509TrustManager) tms[i]);
	    	}        
    	}
    	return trustManagerList;
    }	
}
