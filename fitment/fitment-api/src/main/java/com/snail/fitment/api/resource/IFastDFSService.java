package com.snail.fitment.api.resource;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public interface IFastDFSService {
	
	/**
	 * 上传文件
	 * @param fileName 全路径文件名
	 * @return groupName 分组名；
	 * @return remoteFileName 远程文件名；
	 */
	public Map<String, Object> uploadFile(String fileName);
	
	/**
	 * 上传文件
	 * @param fileName 文件名
	 * @param fileBytes 文件内容
	 * @return groupName 分组名；
	 * @return remoteFileName 远程文件名；
	 */
	public Map<String, Object> uploadFile(String fileName, byte[] fileBytes);
	
	/**
	 * 分段上传文件（首次上传调用）
	 * @param fileName 全路径文件名
	 * @return groupName 分组名；
	 * @return remoteFileName 远程文件名；
	 */
	public Map<String, Object> uploadAppendSyn(String fileName);
	
	
	/**
	 * 分段上传文件（首次上传调用）
	 * @param fileName 全路径文件名
	 * @param fileBytes 文件内容
	 * @return groupName 分组名；
	 * @return remoteFileName 远程文件名；
	 */
	public Map<String, Object> uploadAppendSyn(String fileName, byte[] fileBytes);
	
	/**
	 * 分段上传文件，异步方式
	 * @param fileName 全路径文件名
	 * @return groupName 分组名；
	 * @return remoteFileName 远程文件名；
	 */
	public Map<String, Object> uploadAppendAsy(String fileName);
	
	/**
	 * 分段上传文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @param fileBytes 待上传分段文件内容
	 * @return  错误代码
	 */
	public int appendFile(String groupName, String remoteFileName, byte[] fileBytes);
	
	
	/**
	 * 分段上传文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @param fileName 待上传分段文件内容全路径
	 * @return  错误代码
	 */
	public int appendFile(String groupName, String remoteFileName, String fileName);
	
	
	/**
	 * 分段上传文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @param fileBytes 待上传分段文件内容
	 * @return  上传成功与否
	 */
	public boolean modifyFile(String groupName, String appendFileName, long fileOffset, byte[] fileBytes);
	
	/**
	 * 上传从文件
	 * @param groupName 分组名
	 * @param masterFileName 主文件远程文件名
	 * @param fileName 全路径文件名
	 * @param prefixName 从文件标记（主文件名之后，文件类型之前）
	 * @return  错误码， 0表示存储成功
	 */
	public int uploadSlaveFile(String groupName, String masterFileName, String fileName, String prefixName);
	
	/**
	 * 下载文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @param savePath 保存路径（如果为null，保存到默认下载目录）
	 */
	public void downloadFile(String groupName, String remoteFileName, String savePath);
	
	
	/**
	 * 下载文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @return byte[] 文件字节流
	 */
	public byte[] downloadFile(String groupName, String remoteFileName);
	
	
	
	/**
	 * 清空文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @return boolean 是否清除成功
	 */
	public boolean truncateFile(String groupName, String remoteFileName);
	
	
	
	
	/**
	 * 下载文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @param fileOffset 下载其实位置
	 * @param downloadBytes 下载文件长度， 0表示从fileOffset开始，下载剩余文件
	 * @return byte[] 文件字节流
	 */
	public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes);
	
	
	/**
	 * 下载文件
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @param boolean true删除成功，false删除失败
	 */
	public boolean deleteFile(String groupName, String remoteFileName);
	
	
	/**
	 * 获取文件后缀
	 * @param fileName 文件名
	 * @return 后缀名（可为空）
	 */
	public String getFileExtName(String fileName);
	
	/**
	 * 获取文件长度
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @return 文件长度
	 */
	public long getFileLength(String groupName, String remoteFileName);
	
	/**
	 * 获取元数据
	 * @param groupName 分组名
	 * @param remoteFileName 远程文件名
	 * @return 文件长度
	 */
	public String getOrigianlFileName(String groupName, String remoteFileName);

	/**
	 * 获得可用的trackServer列表
	 * @return
	 */
	List<InetSocketAddress> checkTrackServers();
	
}
