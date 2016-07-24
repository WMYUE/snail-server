package com.snail.fitment.api.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;

public interface ISpringMongoService {

	public <T> void insert(T entityClass);

	public <T> void insertAll(List<T> list, Class<T> entityClass);

	public <T> List<T> findAll(Class<T> entityClass);
	
	public <T> T findOne(Query query, Class<T> entityClass);
	
	public <T> List<T> findPage(Query query, int pageNow, int pageSize, Class<T> entityClass);

	public CommandResult findByExecuteCommand(String command);

	public <T> Long getCount(Query query, Class<T> entityClass);

	public <T> void removeOne(Query query, Class<T> entityClass);

	public <T> void removeAll(Class<T> entityClass);

	public <T> List<T> findAll(Query query, Class<T> entityClass);

	public <T> void deleteAll(Query query, Update update, Class<T> entityClass);
	
	public <T> void createCollection(String collectionName);

	public <T> void insertOne(Object batchToSave, String collectionName);

	public <T> void upinsertOne(Object batchToSave, String collectionName);
	
	public <T> List<T> findAll(Query query, Class<T> entityClass, String collectionName);

	public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName,
			String mapFunction, String reduceFunction,
			MapReduceOptions mapReduceOptions, Class<T> entityClass);

	public <T> MapReduceResults<T> mapReduce(String inputCollectionName,
			String mapFunction, String reduceFunction, Class<T> entityClass);

	public <T> MapReduceResults<T> mapReduce(String inputCollectionName,
			String mapFunction, String reduceFunction,
			MapReduceOptions mapReduceOptions, Class<T> entityClass);

	public <T> GroupByResults<T> group(Criteria criteria, String inputCollectionName,
			GroupBy groupBy, Class<T> entityClass);

	public <T> T findOne(Query query, Class<T> entityClass, String collectionName);

	public <T> void removeOne(Query query, Class<T> entityClass, String collectionName);

	public <T> void findAllAndRemove(Query query, String collectionName);

	public <T> Long getCount(Query query, String modelCollectionName);

	public <T> List<T> findPage(Query query, int pageSize,
			Class<T> entityClass, String collectionName);

	public <T> boolean findAndModify(Query query, Update update, Class<T> entityClass);

	public <T> boolean updateMulti(Query query, Update update, Class<T> entityClass);

	public DBCollection getCollection(String modelCollectionName);

	
}
