package com.snail.fitment.common.security;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CertUtil {
	private static final Logger log = Logger.getLogger(CertUtil.class);
	public static String ISSUER_CN_CHINA_UNICOM_CLASS1_CA = "CHINA UNICOM CLASS1 CA";
	public static X509Certificate string2Cert(final String certBase64) throws CertificateException, Exception{
		String certStr = certBase64;
		certStr = certStr.replace("-----BEGIN CERTIFICATE-----", "");
		certStr = certStr.replace("-----END CERTIFICATE-----", "");
		certStr = certStr.replace(" ", "");
		certStr = certStr.replace("\\r\\n", "");
		certStr = certStr.replace("\\r", "");
		certStr = certStr.replace("\\t", "");
		
		InputStream is = null;
		try{
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			is = new ByteArrayInputStream(Base64.decodeBase64(certStr));
			Certificate cert = certFactory.generateCertificate(is);
			return (X509Certificate)cert;	
		} finally {
			if (is != null) {
				try{
					is.close();
				} catch(Exception e) {					
				}
				
			}			
		}			
	}
	
	
	public static boolean verifyCert(final String certBase64){
		X509Certificate cert;
		try {
			cert = string2Cert(certBase64);
		} catch (CertificateException e1) {
			log.error("CertificateException ,error:",e1);
			return false;
		} catch (Exception e1) {
			log.error("CertificateException ,error:",e1);
			return false;
		}
		try {
			cert.checkValidity();
		} catch (CertificateExpiredException e) {
			log.error("CertificateException ,error:",e);
			return false;
		} catch (CertificateNotYetValidException e) {
			log.error("CertificateException ,error:",e);
			return false;
		} catch (Exception e) {
			log.error("CertificateException ,error:",e);
			return false;
		}
		return true;
	}
	
	/**
	 * 是否被吊销
	 * @param certBase64
	 * @param crlBinaryData
	 * @return
	 * @throws Exception
	 */
	public static boolean isRevoked(final String certBase64,byte[] crlBinaryData) throws Exception{
		X509Certificate cert = string2Cert(certBase64);
		if (Base64.isArrayByteBase64(crlBinaryData)) {
			crlBinaryData = Base64.decodeBase64(crlBinaryData);
		}		
		InputStream is = null;
		try{
			is = new ByteArrayInputStream(crlBinaryData);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509CRL crl = (X509CRL) cf.generateCRL(is);
			return crl.isRevoked(cert);			
		} finally {
			if (is != null) {
				try{
					is.close();
				} catch(Exception e) {					
				}
				
			}			
		}		
	}
	
	public static String getCertCN(final String certBase64) throws Exception{
		X509Certificate cert;
		try {
			cert = string2Cert(certBase64);
		} catch (CertificateException e1) {
			throw e1;
		} catch (Exception e1) {
			throw e1;
		}
		
		String subjectDN = cert.getSubjectDN().getName();
		String cn = "";
		if (StringUtils.isNotEmpty(subjectDN)) {
			String[] dn = subjectDN.split(",");
			for(String dn_ : dn){
				dn_ = dn_.trim();
				if (dn_.startsWith("cn") || dn_.startsWith("CN")) {
					String[] cnStr = dn_.split("=");
					if (cnStr.length>1) {
						cn = dn_.split("=")[1];
						break;
					}					
				}
			}
		}
		return cn;
	}
	
	public static String getIssuerCn(final String certBase64) throws Exception{
		X509Certificate cert;
		try {
			cert = string2Cert(certBase64);
		} catch (CertificateException e1) {
			throw e1;
		} catch (Exception e1) {
			throw e1;
		}
		
		String issuerDn=cert.getIssuerDN().getName();
		String cn = "";
		if (StringUtils.isNotEmpty(issuerDn)) {
			String[] dn = issuerDn.split(",");
			for(String dn_ : dn){
				dn_ = dn_.trim();
				if (dn_.startsWith("cn") || dn_.startsWith("CN")) {
					String[] cnStr = dn_.split("=");
					if (cnStr.length>1) {
						cn = dn_.split("=")[1];
						break;
					}					
				}
			}
		}
		return cn;
	}
	
	public static byte[] getPublicKey(final String certBase64) throws Exception{
		X509Certificate cert;
		try {
			cert = string2Cert(certBase64);
		} catch (CertificateException e1) {
			log.error("CertificateException ,error:",e1);
			throw e1;
		} catch (Exception e1) {
			log.error("CertificateException ,error:",e1);
			throw e1;
		}
		return cert.getPublicKey().getEncoded();
	}
	
	public static String getSerialNumber(final String certBase64)throws Exception{
		X509Certificate cert;
		try {
			cert = string2Cert(certBase64);
		} catch (CertificateException e1) {
			log.error("CertificateException ,error:",e1);
			throw e1;
		} catch (Exception e1) {
			log.error("CertificateException ,error:",e1);
			throw e1;
		}
		
		return Long.toHexString(cert.getSerialNumber().longValue()).toUpperCase();
	}
	
	public static final void checkServerTrusted(X509Certificate[] chain, List<X509TrustManager> trustManagerList) throws CertificateException{
    	
    	CertificateException ex = null;
    	for (X509TrustManager trustManager : trustManagerList) {            
    		try {
    			trustManager.checkServerTrusted(chain, "RSA");
    			return;//验证成功，返回
			} catch (CertificateException e) {
				ex = e;
			}   
    	} 
    	throw ex;
    }
}
