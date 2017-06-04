package com.snail.fitment.common.utils;

import java.util.Date;

/**
 * @author lzh 2011-4-25
 */
public class DefaultValues {

	public static final Date DEFAULT_DATE = DateTools.parse("1900-01-01", "yyyy-MM-dd");

	// public static final TimeSpan ALLOWED_SPAN = TimeSpan.FromMinutes(30);

	public static final int FALSE_INT = 0;
	public static final int TRUE_INT = 1;

	public static final String FALSE_STR = "0";
	public static final String TRUE_STR = "1";

	public static final int AVALIABLEP_ROCESSORS_SIZE = Runtime.getRuntime().availableProcessors();
}
