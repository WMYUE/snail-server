package com.snail.fitment.common.json;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;


/**
 * ObjectMapper 是线程安全的，可以共享，但共享的结果是效率比较低（内部应该还是有竞争冲突）
 * 
 * 合理的做法还是线程变量
 * 
 * @author chenlei
 *
 */
public class JsonConvert {
	private static final Logger logger = Logger.getLogger(
			JsonConvert.class.getName());
	private static  ThreadLocal localMapper = new ThreadLocal();
	
	private static ObjectMapper getMapper() {
		ObjectMapper mapper = (ObjectMapper) localMapper.get();
		if(mapper == null) {
			mapper = new ObjectMapper();
			localMapper.set(mapper);
		}
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);

		return mapper;
	}
	public static Object DeserializeObject(String string, JavaType type) {
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		Object userData;
		try {
			userData = getMapper().readValue(string, type);
			return userData;
		} catch (JsonParseException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+string+", type:"+type+" error.", e);
			//}
		} catch (JsonMappingException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+string+", type:"+type+" error.", e);
			//}
		} catch (IOException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+string+", type:"+type+" error.", e);
			//}
		}
		return null;
	}
	public static Object convertObject(Object orig, JavaType type) 
	{
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		Object userData;
		userData = getMapper().convertValue(orig, type);
		return userData;

	}
	
	public static <T> List<T> deserializeJsonToList(String json , Class<T> clazz){
		List<T> list;
		try {
			list = getMapper().readValue(json, TypeFactory.collectionType(List.class, clazz));
			return list;
		} catch (JsonParseException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("deserializeJsonToList:"+json+", type:"+clazz+" error.", e);
			//}
		} catch (JsonMappingException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("deserializeJsonToList:"+json+", type:"+clazz+" error.", e);
			//}
		} catch (IOException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("deserializeJsonToList:"+json+", type:"+clazz+" error.", e);
			//}
		}
		return null;
	}
	
	public static Object convertObject(Object orig, Class<?> type) 
	{
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		Object userData;
		userData = getMapper().convertValue(orig, type);
		return userData;

	}
	
	public static <T> T DeserializeObject(String string, Class<T> type) {
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		T userData;
		try {
			if(string == null) return null;
			userData = getMapper().readValue(string, type);
			return userData;
		} catch (JsonParseException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+string+", type:"+type+" error.", e);
			//}
		} catch (JsonMappingException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+string+", type:"+type+" error.", e);
			//}
		} catch (IOException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+string+", type:"+type+" error.", e);
			//}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> DeserializeGdpObject(String jsonString) {
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> userData;
		try {
			userData = getMapper().readValue(jsonString, Map.class);
			return userData;
		} catch (JsonParseException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+jsonString+" error.", e);
			//}
		} catch (JsonMappingException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+jsonString+" error.", e);
			//}
		} catch (IOException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+jsonString+" error.", e);
			//}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<?, ?> DeserializeObject(String jsonString) {
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		Map<String, Object> userData;
		try {
			userData = getMapper().readValue(jsonString, Map.class);
			return userData;
		} catch (JsonParseException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+jsonString+" error.", e);
			//}
		} catch (JsonMappingException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+jsonString+" error.", e);
			//}
		} catch (IOException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("DeserializeObject:"+jsonString+" error.", e);
			//}
		}
		return null;
	}

	public static String SerializeObject(Object _systemList) {
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
		try {
			return getMapper().writeValueAsString(_systemList);
		} catch (JsonGenerationException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("SerializeObject:"+_systemList+" error.", e);
			//}
		} catch (JsonMappingException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("SerializeObject:"+_systemList+" error.", e);
			//}
		} catch (IOException e) {
			//if(logger.isErrorEnabled()) {
				logger.error("SerializeObject:"+_systemList+" error.", e);
			//}
		}
		return null;
	}

	public static Date GetStandardTime(long lastSynchTime) {
		return new Date(lastSynchTime);
//		Date lastTime = DefaultValues.DEFAULT_DATE;
//		if (lastSynchTime != 0) {
//			lastTime = new Date(lastSynchTime);
//		}
//		return lastTime;
	}
	
	public static <T> T convertObject(Object value, Class<T> paramClass, T defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (paramClass.isInstance(value)) {
			return (T)value;
		}
		return (T)convertObject(value, TypeFactory.fastSimpleType(paramClass));
	}
	
	public static <T> List<T> convertList(Object value,	Class<T> itemClass, List<T> defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return (List<T>)convertObject(value, TypeFactory.collectionType(List.class, itemClass));
	}
	public static <T, E> Map<T, E> deserializeJsonToMap(String json, Class<T> keyType,  
            Class<E> valueType) {  
        try {  
            Map<T, E> map = getMapper().readValue(json,  
                    TypeFactory.mapType(HashMap.class, keyType, valueType));  
            return map;  
        } catch (JsonParseException e) {  
            e.printStackTrace();  
        } catch (JsonMappingException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
}
