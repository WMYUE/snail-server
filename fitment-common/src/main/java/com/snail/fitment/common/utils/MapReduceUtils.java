package com.snail.fitment.common.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 用于生成count和sum的MapReduce，javascript文件
 * @author dyp
 *
 */
public class MapReduceUtils {
	
	private String mapFunction = null;
	
	private String reduceFunction = null;
	
	public String getMapFunction() {
		return mapFunction;
	}

	public void setMapFunction(String mapFunction) {
		this.mapFunction = mapFunction;
	}


	public String getReduceFunction() {
		return reduceFunction;
	}


	public void setReduceFunction(String reduceFunction) {
		this.reduceFunction = reduceFunction;
	}


	public String createCountMapFunction(List<String> keyList){
		if(StringUtils.isNotEmpty(this.getMapFunction())){
			return this.getMapFunction();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("function () {\n");
		sb.append("var key = {");
		
		for (String key : keyList) {
			sb.append(key).append(":this.").append(key).append(",");
		}
	
		sb.deleteCharAt(sb.length()-1).append("};\n");
		sb.append("emit(key, 1);\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	
	public String createSumMapFunction(List<String> keyList, String value){
		if(StringUtils.isNotEmpty(this.getMapFunction())){
			return this.getMapFunction();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("function () {\n");
		sb.append("var key = {");
		
		for (String key : keyList) {
			sb.append(key).append(":this.").append(key).append(",");
		}
	
		sb.deleteCharAt(sb.length()-1).append("};\n");
		sb.append("emit(key, this.").append(value).append(");\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	
	public String createCountReduceFunction(){
		if(StringUtils.isNotEmpty(this.getReduceFunction())){
			return this.getReduceFunction();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("function (key, values) {");
		sb.append("var sum = 0;");
		sb.append("for (var i = 0; i < values.length; i++)");
		sb.append("sum += values[i];");
		sb.append("return sum;");
		sb.append("}");
		return sb.toString();
	}
	
	
	public String createSumReduceFunction(){
		if(StringUtils.isNotEmpty(this.getReduceFunction())){
			return this.getReduceFunction();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("function (key, values) {");
		sb.append("var sum = 0;");
		sb.append("for (var i = 0; i < values.length; i++)");
		sb.append("sum += values[i];");
		sb.append("return sum;");
		sb.append("}");
		return sb.toString();
	}
	
	public String createSingleMapFunction(String key, String value){
		StringBuffer sb = new StringBuffer();
		sb.append("function () {\n");
		sb.append("emit(this.").append(key);
		sb.append(",{sum:this.").append(value);
		sb.append(",count:1}");
		sb.append(")\n}");
		return sb.toString();
	}
	
	public String createSumAndCountReduceFunction(){
	StringBuffer sb = new StringBuffer();
	sb.append("function (key, values) {\n");
	sb.append("var reduced = {count:0, sum:0};\n");
	sb.append("values.forEach(function(val){\n");
	sb.append("reduced.count += val.count;\n");
	sb.append("reduced.sum += val.sum;\n});\n");
	sb.append("return reduced\n");
	sb.append("}");
	return sb.toString();
}
}
