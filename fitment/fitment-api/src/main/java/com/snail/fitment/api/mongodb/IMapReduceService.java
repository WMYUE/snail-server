package com.snail.fitment.api.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Query;

public interface IMapReduceService<T>{
	public <S> List<S> mapReduceForSum(Query query, String inputCollectionName, List<String> keyList, String value, MapReduceOptions mapReduceOptions, Class<S> outputClass);
	
	public <S> List<S> mapReduceForCount(Query query, String inputCollectionName, List<String> keyList, MapReduceOptions mapReduceOptions,  Class<S> outputClass);
	
	public <S> List<S> mapReduce(Query query, String inputCollectionName, List<String> keyList, Class<S> outputClass);
}
