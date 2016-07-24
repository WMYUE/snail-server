package com.snail.fitment.database.mongodb.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snail.fitment.model.ResourceInfo;

public interface ResourceModelRepository extends MongoRepository<ResourceInfo, String> {
	
	public ResourceInfo findByResourceId(String resourceId);
	
	public List<ResourceInfo> findByUserUniId(String userUniId);
}
