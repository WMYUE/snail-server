package com.snail.fitment.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class DateUtils {

	ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>();
	
	public static String fmtFullDate(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}

	public static String fmtDate(Date d, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(d);
	}
	
	public static String fmtSimpleDate(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}
	
	public static Date long2FullDate(long times) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(times);  
	    String d = df.format(time);  
	    Date date = null;
		try {
			date = df.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return date;
	}

	
	/**
	 * 返回前一天。
	 * 
	 * @param date
	 * @return
	 */
	public static Date theDayBefore(Date date) {
		Calendar cdate = Calendar.getInstance();
		cdate.setTime(date);
		cdate.add(Calendar.DATE, -1);
		Date ldate = new Date(cdate.getTime().getTime());
		return ldate;
	}

	public static Date theSimpleDayBefore(Date date) {
		Date d = theDayBefore(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(fmtSimpleDate(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 得到指定日期的在这个月的第一日. 00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Long firstDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null)
			cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime().getTime();
	}

	/**
	 * 得到服务运行时间
	 * 
	 * @return 当月最后一天
	 */
	public static Long currentTime() {
		return System.currentTimeMillis();

	}

	public static Long getStartTime(Date date) {
		return firstDayOfMonth(date);
	}

	public static Long getEndTime(Date date) {
		if (date == null || "".equals(date)) {
			return currentTime();
		} else {
			return date.getTime();
		}
	}

	public static Date string2FullDate(String date) {
		SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (date == "" || "".equals(date)) {
				return null;
			} else {
				return new Date(clsFormat.parse(date).getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		/*System.out.println(string2Date("2015-10-03").getTime());
		System.out.println(firstDayOfMonth(new Date()));
		System.out.println(currentTime());
		System.out.println(fmt1Date(new Date()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		Date d = calendar.getTime();
		System.out.println(getBeforeDay(new Date()));
		
		System.out.println(new Date());
		System.out.println(formatTime((long) 9577));
		System.out.println(fmtDate(new Date(new Long("1437908002113"))));*/
		/*startTime====1446307200000
				endTime====1448294400000*/
		System.out.println(fmtFullDate(new Date(new Long("1438764610770"))));
		//System.out.println(splitString("1005.unicom-lanxin"));
		//String s = JSON.toJSONString(new Date(Long.parseLong("0")), getJsonConfig(), null);
		//System.out.println(s);
		System.out.println(string2FullDate(fmtFullDate(new Date())));
	}
	
	public static String splitString(String s){
		String L = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals(".")) {
				L = s.substring(0, i).trim();
			}
		}
		return L;
		
	}
	
	public static Date getBeforeDay(Date date) {
		Date d=new Date(date.getTime()-1000*60*60*24);
		SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
		return string2FullDate(sp.format(d));
	}
	public static boolean isBlankOrNull(String jsonString){
		 if(null==jsonString)return true;  
        //return str.length()==0?true:false;  
        return jsonString.length()==0;  
	}
	public static String replaceSpecialtyStr(String str,String pattern,String replace){  
		if (isBlankOrNull(pattern))
			pattern = "\\s*|\t|\r|\n";// 去除字符串中空格、换行、制表
		if (isBlankOrNull(replace))
			replace = "";
		return Pattern.compile(pattern).matcher(str).replaceAll(replace);
	}
	
	private static SerializeConfig jsonConfig = new SerializeConfig();
	
	static {
		jsonConfig.put(Date.class, new SimpleDateFormatSerializer(
				"yyyy-MM-dd'T'HH:mm:ss.SSS Z"));
		/*jsonConfig.put(java.sql.Date.class, new SimpleDateFormatSerializer(
				"yyyy-MM-dd"));*/
		
	}

	public static SerializeConfig getJsonConfig() {
		return jsonConfig;
	}

	public static void setJsonConfig(SerializeConfig jsonconfig) {
		jsonConfig = jsonconfig;
	}
	
	
	// a integer to xx:xx:xx
    public static String secToTime(int time) {
    	String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 24){
                	return "99:59:59";
                	
                }else{
                	minute = minute % 60;
                	second = time - hour * 3600 - minute * 60;
                	timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
                }
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    
    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        
        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }
}
