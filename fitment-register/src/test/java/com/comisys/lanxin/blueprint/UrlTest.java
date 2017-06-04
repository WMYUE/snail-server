package com.comisys.lanxin.blueprint;

import org.junit.Test;

import com.snail.fitment.common.constants.RegisterConstants;
import com.snail.fitment.common.namespace.URL;

public class UrlTest {
	private final String        root = "blueprint";
	
	
	@Test
	public void testCreateUrl(){
		
		String s1 = "blueprint://127.0.0.1/route?version=1.0.0&group=test";
		URL url = URL.valueOf(s1);
		System.out.println(url.getAddress());
		System.out.println(url.getBackupAddress());
		System.out.println(url.getProtocol());
		System.out.println(url.getPath());
		System.out.println(url.toParameterString());
		System.out.println(toServicePath(url));
		System.out.println(toCategoryPath(url));
		System.out.println(toUrlPath(url));
		
	}
	
	 private String toRootDir() {
	        if (root.equals(RegisterConstants.PATH_SEPARATOR)) {
	            return root;
	        }
	        return root + RegisterConstants.PATH_SEPARATOR;
	    }
	    
	    private String toRootPath() {
	        return root;
	    }
	    
	    private String toServicePath(URL url) {
	        String name = url.getServiceInterface();
	        if (RegisterConstants.ANY_VALUE.equals(name)) {
	            return toRootPath();
	        }
	        return toRootDir() + URL.encode(name);
	    }

	    private String toCategoryPath(URL url) {
	        return toServicePath(url) + RegisterConstants.PATH_SEPARATOR + url.getParameter(RegisterConstants.CATEGORY_KEY, RegisterConstants.DEFAULT_CATEGORY);
	    }

	    private String toUrlPath(URL url) {
	        return toCategoryPath(url) + RegisterConstants.PATH_SEPARATOR + URL.encode(url.toFullString());
	    }
}
