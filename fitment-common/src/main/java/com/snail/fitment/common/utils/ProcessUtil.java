package com.snail.fitment.common.utils;

public class ProcessUtil {

	/**
	 * @descrption 执行外部exe公用方法
	 * @author chenlei
	 * @param cmdStr
	 * 命令字符串
	 * 
	 */

	public static void execProcess(String cmdStr) {
		Process process = null;
		try {
			System.out.println(cmdStr);
			process = Runtime.getRuntime().exec(cmdStr);
			new ProcessClearStream(process.getInputStream(), "INFO").start();
			new ProcessClearStream(process.getErrorStream(), "ERROR").start();
			int status = process.waitFor();
			System.out.println("Process exitValue:" + status);
		} catch (Exception e) {
			System.out.println("执行" + cmdStr + "出现错误，" + e.toString());
		} finally {
			if (process != null) {
				process.destroy();
			}
			process = null;
		}

	}

}
