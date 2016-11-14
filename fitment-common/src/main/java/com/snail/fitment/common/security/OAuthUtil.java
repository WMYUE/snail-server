package com.snail.fitment.common.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

public class OAuthUtil {
	private static int modNum = 36;
	private static char[] encodeCharList = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
	
	public static String signature(String sessionKey, String timestamp, String nonce, String sessionId) {
		List<String> ss = new ArrayList<String>();
		ss.add(timestamp);
		ss.add(nonce);
		ss.add(sessionKey);
		ss.add(sessionId);
		Collections.sort(ss);
		StringBuilder builder = new StringBuilder();
		for (String s : ss) {
			builder.append(s);
		}
		return DigestUtils.shaHex(builder.toString()).toLowerCase();
	}
	
	public static String signature(String inputSS){
		Preconditions.checkArgument(StringUtils.isNotEmpty(inputSS), "输入字符串不能为空");
		
		String upperSS =  inputSS.replace("-", "").toUpperCase();
		Preconditions.checkArgument(StringUtils.isNotEmpty(upperSS), "输入字符串不能全为【-】");
		
		String reverseSS= new StringBuffer(upperSS).reverse().toString();
		Preconditions.checkArgument(upperSS.length() == reverseSS.length(), "内部错误，倒序前后长度不一致");

		char[] charArray1 = upperSS.toCharArray();	
		char[] charArray2 = reverseSS.toCharArray();	
		
		
		int charLength = charArray1.length;
		char[] returnArray = new char[charLength];
		
		for(int i = 0; i< charLength; i++){
			int index = ((int)charArray1[i]+(int)charArray2[i]) % modNum;
			returnArray[i] = encodeCharList[index];
		}
	
		return new String(returnArray);
	}
	
}
