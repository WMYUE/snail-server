package com.snail.fitment.api.resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IFileStorageService {
	public String saveUserFile(String userUniId, String fileName, String ext, byte[] fileContent) throws IOException;
	
	public byte[] readUserFile(String filePath) throws IOException;
	
	public byte[] readUserFile(String filePath, Long position, Long length) throws IOException;
	
	public File mergeFiles(String outFile, List<String> files) throws Exception;
	
	public void removeFile(List<String> files);
	
	public void removeFile(String file);
	
	public String md5Hex(String file) throws Exception;
}
