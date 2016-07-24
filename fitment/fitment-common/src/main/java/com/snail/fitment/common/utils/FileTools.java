package com.snail.fitment.common.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;


public final class FileTools {
    
    private FileTools() {
    }


    public static void checkMkdir(String fileName) {
        String path = dirName(fileName);
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static String dirName(String fileName) {
        return dirName(fileName, false);
    }


    public static String dirName(String fileName, boolean preserveTrailingSlash) {
        if (StringTools.isNullOrWhiteSpace(fileName)) {
            return ".";
        }
        int index = fileName.lastIndexOf('/');
        if (index == -1) {
            index = fileName.lastIndexOf('\\');
        }
        switch (index) {
            case 0: 
                return "/";
            case -1:
                return ".";
            default:
                if (preserveTrailingSlash) {
                    return fileName.substring(0, index + 1);
                }
                return fileName.substring(0, index);
        }
    }


    public static String baseName(String fileName) {
        if (StringTools.isNullOrWhiteSpace(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf('/');
        if (index == -1) {
            index = fileName.lastIndexOf('\\');
        }
        // if index == -1, index + 1 will be 0
        return fileName.substring(index + 1);
    }


    public static boolean appendFile(String timestr, String filename)
            throws IOException {
        FileOutputStream stream;
        OutputStreamWriter writer;
        File file = new File(filename);

        if (!file.exists()) {
            return false;
        }

        stream = new FileOutputStream(filename, true);
        writer = new OutputStreamWriter(stream, "gb2312");
        try {
            writer.write(timestr);
        } finally {
            writer.close();
            stream.close();
        }

        return true;
    }

    public RandomAccessFile openRandomFile(String fileName, String mode)
            throws IOException {
        File f;
        RandomAccessFile raf;
        f = new File(fileName);
        raf = new RandomAccessFile(f, mode);

        return raf;
    }


    public static void createFile(String fileName) throws IOException {
        if (StringTools.isNullOrWhiteSpace(fileName)) {
            throw new IllegalArgumentException();
        }
        File file = new File(fileName);
        if (!file.exists()) {
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(file));
            writer.close();
        }
    }


    public static void deleteFile(String fileName) throws IOException {
        if (StringTools.isNullOrWhiteSpace(fileName)) {
            throw new IllegalArgumentException();
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        if (!file.delete()) {
            throw new IOException();
        }
    }


    public static boolean checkDirExists(String path, boolean write) {
        File p = new File(path);

        if (p.exists() && p.isDirectory()) {
            return true;  //all dir exist, so return true;
        }

        if (!write) {
            return false;
        }

        if (!p.mkdirs()) {
            System.err.println("can't create dir: " + p);
            return false;
        }

        return true;
    }


    public static String combineFileName(String basedir, String value) {
        String rst = null;
        if(StringUtils.isEmpty(basedir)){
        	throw new IllegalArgumentException("basedir 涓嶈兘涓虹┖");
        }

        basedir = formatFilePath(basedir);
        if (!basedir.endsWith("/")) {
            basedir = basedir + '/';
        }
        
   
        if (StringUtils.isNotEmpty(value)) {
            rst = basedir + value;
        }
        
        return formatFilePath(rst);
    }
    
    


    public static URL combineURL(URL baseURL, String value) 
            throws MalformedURLException {
        String baseURLFile = baseURL.getFile();
        if (!baseURLFile.endsWith("/")) {
            baseURLFile += '/';
        }
        return new URL(baseURL.getProtocol(), baseURL.getHost(), baseURL.getPort(),
                baseURLFile + value);
    }
    

    public static String formatFilePath(String file) {
        if (file == null) {
            return null;
        }
        file = removeIllegalPath(file);
        return new File(file).getAbsolutePath().replace('\\', '/');
    }

    //绉婚櫎闈炴硶璺緞
	public static String removeIllegalPath(String file) {
		file=file.replaceAll("\\.\\./", "");
		return file;
	}


    public static boolean isAbsolutePath(String path) {
        return new File(path).isAbsolute();
    }

    
    public static boolean exist(String fileName){
    	if (StringTools.isNullOrWhiteSpace(fileName)) {
            throw new IllegalArgumentException();
        }
    	File file = new File(fileName);
    	return file.exists();
    }
    

	public static String formartFileSize(long length) {
		String show = "";
		DecimalFormat df = new DecimalFormat("#.##");
		Double lengthDouble = new Double(length);
		if (length < 1024) {
			show = String.valueOf(length) + "B";
		} else if (length >= 1024*1024*1024) {
			show = df.format(lengthDouble / new Double(1024*1024*1024)) + "GB";
		} else if (length >= 1024*1024) {
			show = df.format(lengthDouble / new Double(1024*1024)) + "MB";			
		} else if (length >= 1024) {			
			show = df.format(lengthDouble / 1024.0) + "KB";
		}		
		return show;
	}
	
	
	public static byte[] toByteArray(File f) throws IOException {  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
        BufferedInputStream in = null;  
        try {  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while (-1 != (len = in.read(buffer, 0, buf_size))) {  
                bos.write(buffer, 0, len);  
            }  
            return bos.toByteArray();  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        }  
    }  
	
}
