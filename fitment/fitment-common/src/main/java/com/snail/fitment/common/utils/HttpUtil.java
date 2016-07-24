package com.snail.fitment.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class HttpUtil {
	public static Logger log= Logger.getLogger(HttpUtil.class);

	
    public static StringBuffer getRequestContextPath(HttpServletRequest req) {
        StringBuffer url = new StringBuffer();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        String contextPath = req.getContextPath();
        url.append(scheme); // http, https
        url.append("://");
        url.append(req.getServerName());
        if ((scheme.equals("http") && port != 80)
                || (scheme.equals("https") && port != 443)) {
            url.append(':');
            url.append(req.getServerPort());
        }
        url.append(contextPath);
        return url;
    }
    
	public static String getRequestUri(HttpServletRequest request,String path) {
		String url= HttpUtil.getRequestContextPath(request).append(path).toString();
		if(log.isDebugEnabled()){
			log.info("getRequestUri result  "+url);
		}

		return path;
	}
	
	public static String readServletInputStream(InputStream in, int len) throws IOException {
		byte[] postedBytes = new byte[len];
        try {
            int offset = 0;

            do {
                int inputLen = in.read(postedBytes, offset, len - offset);
                /*if (inputLen <= 0) {
                    throw new IllegalArgumentException("读取数据不全，post数据少于contentLength");
                }*/
                offset += inputLen;
            } while ((len - offset) > 0);

        } catch (IOException e) {
        	log.error(e);
        } finally{
        	in.close();
        }
		return new String(postedBytes);
	}
	
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static Map<String, String> parseQuery(String requestStr) {
		// args=0,18753155013,951234,%B5%BD8%3A00%B2%C5%D3%D0%CA%B1%BC%E4,20121019164904;0,15810708705,951234,%B5%BD8%3A00%B2%C5%D3%D0%CA%B1%BC%E4,20121019164904;
		if (org.apache.commons.lang.StringUtils.isEmpty(requestStr)) {
			return Collections.EMPTY_MAP;
		}
		String[] params = requestStr.split("&");
		Map<String, String> paramMap = new HashMap<String, String>();
		for (String param : params) {
			String[] paramArray = param.split("=");
			if (paramArray.length == 2) {
//				paramMap.put(paramArray[0], paramArray[1]);
				//从流中获取数据，需要base64转码
				paramMap.put(paramArray[0], URLDecoder.decode(paramArray[1]));
			} else {
				log.error("参数格式错误 " + param);
			}
		}
		return paramMap;
	}
	
	
	public static Map<String, String> readPostData(HttpServletRequest request) throws IOException{
		return parseQuery(readServletInputStream(request.getInputStream(), request.getContentLength()));
	}
}
