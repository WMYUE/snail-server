package com.snail.fitment.model.mongodb;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document(collection = "Ids")
@Component("Ids")
public class Ids {
	@Indexed
	private String collectionName;
	@Indexed
	private Long ids;
	
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public Long getIds() {
		return ids;
	}
	public void setIds(Long ids) {
		this.ids = ids;
	}
	@Override
	public String toString() {
		return "Ids [collectionName=" + collectionName + ", ids=" + ids + "]";
	}
	
	
	

}
