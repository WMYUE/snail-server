package com.snail.fitment.api.resource;

import java.util.List;

import com.snail.fitment.model.ResourceInfo;
import com.snail.fitment.model.request.ReturnResult;

public interface IResourceService {
	/**
	 * 增加资源
	 * @param sessionId
	 * @param fileName
	 * @param mimeType
	 * @param resourceId
	 * @param fileContent
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<ResourceInfo> addResource(String sessionId, String fileName, String mimeType, String resourceId, byte[] fileContent);
	
	/**
	 * 获取资源
	 * @param resourceId
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<ResourceInfo> getResource(String resourceId);
	
	/**
	 * 获取资源
	 * @param sessionId
	 * @param resourceIdList
	 * @return ReturnResult<List<ResourceModel>>
	 */
	public ReturnResult<List<ResourceInfo>> getResourceList(List<String> resourceIdList);
	
	/**
	 * 查询资源短信息
	 * @param resourceId
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<ResourceInfo> queryResourceInfo(String resourceId);
	
	/**
	 * 查询资源短信息
	 * @param resourceIdList
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<List<ResourceInfo>> queryResourceInfoList(List<String> resourceIdList);

	/**
	 * 获取块资源
	 * @param resourceId
	 * @param position
	 * @param length
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<ResourceInfo> getPartResource(String resourceId, Long position, Long length);
	
	/**
	 * 合并块资源
	 * @param fileName
	 * @param mimeType
	 * @param md5
	 * @param resourceId
	 * @param appId
	 * @param partResourceIdList
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<ResourceInfo> completePartResource(String userUniId, String md5, String resourceId, List<String> partResourceIdList);
	
	
	/**
	 * 查询已上传的块资源
	 * @param sessionId
	 * @param resourceId
	 * @return ReturnResult<ResourceModel>
	 */
	public ReturnResult<List<ResourceInfo>> queryPartsResourceInfo(String resourceId);
	
	
	/**
	 * 将资源数据解压，如果其中包含业务数据（data.txt），就将其保存到数据库中
	 * @param resourceId 资源ID
	 * @return
	 */
	//public boolean zipAndSaveData(String resourceId, File mergeFile);
}
