package com.snail.fitment.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.lang.StringUtils;

/**
 * 拼音查找的辅助类
 */
@Component
public class PinyinHelper {
	private static final Logger log = Logger.getLogger(PinyinHelper.class);
    private static final char END = '\n';
    private static final int SIZE = 20902;
    private static short[] pinyinIndex = new short[SIZE];
    private static StringBuilder pinyinData = new StringBuilder();
    private static final String propertyFileName = "/pinyin.txt";
    
    static {
		try{
			//装载配置文件中配置项
			PinyinHelper.initialize(propertyFileName);
			log.info("PinyinHelper pinyinIndex length = " + pinyinIndex.length);
		}catch(Exception e){
			log.error("PinyinHelper init,error:",e);
		}

	}
    
    public static void initialize(String fileName) {
    	log.info("PinyinHelper initialize");
        BufferedReader reader = null;
        try {
            //相同的pinyin使用相同的index
            HashMap<String, Short> pinyin2index = new HashMap<>();
    		BufferedInputStream is = new BufferedInputStream(PinyinHelper.class.getClass().getResourceAsStream(fileName));
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            int i = 0;
            do {
                Short index = pinyin2index.get(line);
                if (index == null) {
                    int length = pinyinData.length();
                    if (length > Short.MAX_VALUE) {
                        throw new Exception("pinyinData.length() is too large, change short to int");
                    }
                    pinyinData.append(line);
                    pinyinData.append(END);//以END结尾
                    index = (short) length;
                    pinyin2index.put(line, index);
                }
                pinyinIndex[i] = index;
                line = reader.readLine();
                i++;

                if (line == null)
                    break;
            } while (i < 20902);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static String getPinyin(char c) {
        if ((c >= '一') && (c <= 40869)) {
            int length = pinyinData.length();
            int start = pinyinIndex[(c - '一')];
            int end = start;
            while (end < length && pinyinData.charAt(end) != END) {
                end++;
            }
            return pinyinData.substring(start, end);
        }
        return null;
    }

    private static String getSinglePinyin(char c) {
        String py = getPinyin(c);
        if (py == null) {
            return null;
        }
        return py.split(",")[0];
    }

    private static String[] getMultiPinyin(char c) {
        String py = getPinyin(c);
        if (py == null) {
            return null;
        }
        return py.split(",");
    }

    public static String getSinglePinyin(String zhongwen) {
        if (TextUtils.isEmpty(zhongwen)) return "";

        StringBuilder buffer = new StringBuilder();
        char[] chars = zhongwen.toCharArray();
        boolean ispin = true;
        for (char aChar : chars) {
            String pinyin = getSinglePinyin(aChar);
            if (pinyin != null) {
                if (!ispin) {// 增加空格分隔符
                    buffer.append(" ");
                }
                buffer.append(pinyin);
                buffer.append(" ");
                ispin = true;
            } else {
                buffer.append(aChar);
                ispin = false;
            }
        }
        return buffer.toString().trim();
    }

    public static String[] getMultiPinyin(String zhongwen) {
        if (TextUtils.isEmpty(zhongwen)) return null;

        char[] chars = zhongwen.toCharArray();
        String[][] temp = new String[zhongwen.length()][];
        char c;
        for (int i = 0; i < chars.length; i++) {
            c = chars[i];
            if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
                temp[i] = getMultiPinyin(c);
            } else if (((int) c >= 65 && (int) c <= 90) || ((int) c >= 97 && (int) c <= 122)) {
                temp[i] = new String[]{String.valueOf(c)};
            } else {
                temp[i] = new String[]{""};
            }
        }
        return concatPinyin(temp);
    }

    /**
     * 对包含多音字的字符串的拼音进行拼接
     * 如朝阳，则返回2*1 = 2种结果
     */
    private static String[] concatPinyin(String[][] pinyinArray) {
        String[][] temp = doConcat(pinyinArray);
        return temp[0];
    }

    /**
     * 递归 对包含多音字的字符串的拼音进行拼接
     */
    private static String[][] doConcat(String[][] pingyinArray) {
        int len = pingyinArray.length;
        if (len >= 2) {
            int len1 = pingyinArray[0].length;
            int len2 = pingyinArray[1].length;
            int newlen = len1 * len2;
            String[] temp = new String[newlen];
            int Index = 0;
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    temp[Index] = pingyinArray[0][i] + " " + pingyinArray[1][j];
                    Index++;
                }
            }
            String[][] newArray = new String[len - 1][];
            System.arraycopy(pingyinArray, 2, newArray, 1, len - 2);
            newArray[0] = temp;
            return doConcat(newArray);
        } else {
            return pingyinArray;
        }
    }

    public static String getPinyinByTrim(String string) {
        return getPinyinByTrimFromPinyin(getSinglePinyin(string));
    }

    public static String getPinyinByTrimFromPinyin(String pinyin) {
        return StringUtils.stringExceptSpaces(pinyin);
    }

    public static String getPinyinShortByTrimFromPinyin(String pinyin) {
        return getPinyinByTrimFromPinyin(getPinyinShortFromPinyin(pinyin));
    }

    private static String getPinyinShortFromPinyin(String pinyin) {
        String[] split = pinyin.split(" ");
        StringBuilder result = new StringBuilder();
        for (String item : split) {
            if (item.length() > 0) {
                result.append(item.charAt(0)).append(" ");
            }
        }
        return result.toString();
    }
}
