package com.snail.fitment.common.collection;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang.ArrayUtils;

import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.exception.StateException;
import com.snail.fitment.common.utils.ObjectTools;

public class CollectionUtils {
	
	/**
	 * 将数组格式化为字符串输出
	 * 
	 * @param array
	 *            数组
	 * @return 字符串
	 */
	public static <T> String toString(T[] array) {
		if (array == null)
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < array.length; i++) {
			if (i != 0)
				sb.append(",");
			sb.append(array[i]);
		}
		sb.append("}");
		return sb.toString();
	}

	public static <K, V> String arrayToLogString(Object array) {
		return arrayToLogString(array, FitmentConfig.getNormalElementLimit(), FitmentConfig.getNormalCharLimit());
	}
	
	public static <K, V> String arrayToLogString(Object array, int elementLimit, int charLimit) {
		return new StringBuilder()
				.append("array=(").append("length=").append(ArrayUtils.getLength(array)).append(")")
				.append(innerArrayObjToLogString(array, elementLimit, charLimit))
				.toString();
	}
	
	private static String innerArrayObjToLogString(Object array, int elementLimit, int charLimit) {
		if (array instanceof long[]) {
			long[] arr = (long[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof int[]) {
        	int[] arr = (int[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof short[]) {
        	short[] arr = (short[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof byte[]) {
        	byte[] arr = (byte[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof char[]) {
        	char[] arr = (char[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof double[]) {
        	double[] arr = (double[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof float[]) {
        	float[] arr = (float[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else if (array instanceof boolean[]) {
        	boolean[] arr = (boolean[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } else {
			Object[] arr = (Object[]) array;
			return innerArrayToLogString(arr, arr.length, (i)->arr[i], elementLimit, charLimit);
        } 
	}

	private static <T> String innerArrayToLogString(
			Object array, int length, Function<Integer, Object> getElementI, 
			int elementLimit, int charLimit) {
		if (array == null)
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < length; i++) {
			if(i >= elementLimit || sb.length() > charLimit){
				sb.append("......");
				break;
			}

			if (i != 0)
				sb.append(", ");
			sb.append(ObjectTools.objToLogString(getElementI.apply(i)));
		}
		sb.append("}");
		return sb.toString();
	}

	public static <K, V> String toLogString(Map<K, V> map) {
		return toLogString(map, 10, 2000);
	}
	
	public static <K, V> String toLogString(Map<K, V> map, int elementLimit, int charLimit) {
		return new StringBuilder()
				.append("map=(").append("size=").append(map.size()).append(")")
				.append(innerToLogString(map.entrySet(), elementLimit, charLimit)).toString();
	}

	public static <T> String toLogString(Collection<T> collection) {
		return toLogString(collection, FitmentConfig.getNormalElementLimit(), FitmentConfig.getNormalCharLimit());
	}
	
	public static <T> String toLogString(Collection<T> collection, int elementLimit, int charLimit) {
		return new StringBuilder()
				.append("collection=(").append("size=").append(collection.size()).append(")")
				.append(innerToLogString(collection, elementLimit, charLimit)).toString();
	}

	private static <T> String innerToLogString(Collection<T> collection, int elementLimit, int charLimit) {
		if (collection == null)
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		int i = 0;
		for (T item : collection) {
			if(i >= elementLimit || sb.length() > charLimit){
				sb.append("......");
				break;
			}
			if (i != 0)
				sb.append(", ");

			sb.append(ObjectTools.objToLogString(item));

			i++;
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 拷贝 List
	 * @author HaoNing
	 */
	@SuppressWarnings("unchecked")
	public static void copyList(List src, List dest) {
		for (Object obj : src) {
			dest.add(obj);
		}
	}
	
	public static String stringList2CommaString(List<String> list){
		if(list==null || list.isEmpty()){
			return "";
		}else{
			StringBuffer sbf=new StringBuffer();
			for(String str:list){
				sbf.append(str).append(",");
			}
			return sbf.toString();
		}
	}
	
	/**
	 * 用<T>中fieldName，按指定语言规则排序
	 * @param list
	 * @param fieldName
	 */
	public static <T> void sortListWithLanguageByField(List<T> list, final Locale language, String ... fieldNames){
		if(CollectionUtils.isEmpty(list) || fieldNames==null){
			return;
		}
		T t = list.get(0);
		int desCompareFiledSize = fieldNames.length;
		Field [] fields = t.getClass().getDeclaredFields();
		Field [] matchField = new Field[desCompareFiledSize];
		int matchFieldSize = 0;
		for(int i = 0; i < desCompareFiledSize; i++){
			String fieldName = fieldNames[i];
			for(Field field : fields){
				if(field.getName().equals(fieldName)){
					field.setAccessible(true);
					matchField[i] = field;
					matchFieldSize ++;
					break;
				}
			}
		}
		if(matchFieldSize < desCompareFiledSize){
			throw new StateException(110, "指定排序字段错误！");
		}
		final Field [] compareFields = matchField;
		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				try {
					int result = 0 ;
					for(Field field : compareFields){
						result = Collator.getInstance(language).compare(field.get(o1), field.get(o2));
						if(result!=0)break;
					}
					return result;
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return 0;
			}
		});
		
	}
	
	/**
	 * 用<T>中fieldName，按中文排序
	 * @param list
	 * @param fieldName
	 */
	public static <T> void sortListWithChineseByField(List<T> list, String ... fieldName){
		sortListWithLanguageByField(list, Locale.CHINESE, fieldName);
	}
	
	/**
	 * 数字逆序排序
	 * @param list
	 */
	public static void descSort(List<Long> list){
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		
		Collections.sort(list, new Comparator<Long>(){
			@Override
			public int compare(Long v1, Long v2) {
				if(v2 > v1){
					return 1;
				}else if (v2 < v1){
					return -1;
				}else{
					return 0;
				}
			}
		});
	}
	
	public static String listToString(List<String> list){
		String str = "";
		if(CollectionUtils.isNotEmpty(list)){
			for(String elementStr : list){
				if("".equals(str)){
					str = str + elementStr;
				}else{
					str = str +"," + elementStr;
				}
			}
		}
		return str;
	}
	
	public static <V> Collection<List<V>> toSubListCollection(List<V> list , int subSize){
		Collection<List<V>> subListCollection = new LinkedList<List<V>>();
		if(CollectionUtils.isEmpty(list)){
			return subListCollection;
		}
		int size = list.size();
		if(size < subSize){
			subListCollection.add(list);
		}else{
			int batchTimes = (size - 1)/subSize + 1;
			for(int i = 0 ; i < batchTimes ; i ++){
				int start = i * subSize;
				int end = (i+1) * subSize;
				end =  end > size ? size : end;
				subListCollection.add(list.subList(start, end));
			}
		}
		return subListCollection;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> sort(List<T> list) {
		if (list != null && list.size() > 0) {
			Collections.sort((List)list);
		}
		return list;
	}

	public static <E> List<E> subList(List<E> list, int pageNo, int pageSize) {
		if(isEmpty(list)){
			return list;
		}
		int total = list.size();
		if(pageNo > 0 && pageSize > 0){
			int fromIndex = (pageNo-1) * pageSize;
			int toIndex = pageNo * pageSize;
			if(fromIndex > total){
				return new ArrayList<E>();
			}
			if(toIndex > total){
				toIndex = total;
			}
			List<E> newList = new ArrayList<>();
			for(int i = fromIndex; i < toIndex; i++){
				newList.add(list.get(i));
			}
			return newList;
		}else{
			return list;
		}
	}
	
	private static final Comparator<String> SIMPLE_NAME_COMPARATOR = new Comparator<String>() {
		public int compare(String s1, String s2) {
			if (s1 == null && s2 == null) {
				return 0;
			}
			if (s1 == null) {
				return -1;
			}
			if (s2 == null) {
				return 1;
			}
			int i1 = s1.lastIndexOf('.');
			if (i1 >= 0) {
				s1 = s1.substring(i1 + 1);
			}
			int i2 = s2.lastIndexOf('.');
			if (i2 >= 0) {
				s2 = s2.substring(i2 + 1);
			}
			return s1.compareToIgnoreCase(s2);
		}
	};
	
	public static List<String> sortSimpleName(List<String> list) {
		if (list != null && list.size() > 0) {
			Collections.sort(list, SIMPLE_NAME_COMPARATOR);
		}
		return list;
	}

	public static Map<String, Map<String, String>> splitAll(Map<String, List<String>> list, String separator) {
		if (list == null) {
			return null;
		}
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		for (Map.Entry<String, List<String>> entry : list.entrySet()) {
			result.put(entry.getKey(), split(entry.getValue(), separator));
		}
		return result;
	}
	
	public static Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator) {
		if (map == null) {
			return null;
		}
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
			result.put(entry.getKey(), join(entry.getValue(), separator));
		}
		return result;
	}

	public static Map<String, String> split(List<String> list, String separator) {
		if (list == null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		if (list == null || list.size() == 0) {
			return map;
		}
		for (String item : list) {
			int index = item.indexOf(separator);
			if (index == -1) {
				map.put(item, "");
			} else {
				map.put(item.substring(0, index), item.substring(index + 1));
			}
		}
		return map;
	}

	public static List<String> join(Map<String, String> map, String separator) {
		if (map == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		if (map == null || map.size() == 0) {
			return list;
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null || value.length() == 0) {
				list.add(key);
			} else {
				list.add(key + separator + value);
			}
		}
		return list;
	}

	public static String join(List<String> list, String separator) {
	    StringBuilder sb = new StringBuilder();
        for(String ele : list) {
            if(sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(ele);
        }
        return sb.toString();
	}
	
	public static boolean mapEquals(Map<?, ?> map1, Map<?, ?> map2) {
		if (map1 == null && map2 == null) {
			return true;
		}
		if (map1 == null || map2 == null) {
			return false;
		}
		if (map1.size() != map2.size()) {
			return false;
		}
		for (Map.Entry<?, ?> entry : map1.entrySet()) {
			Object key = entry.getKey();
			Object value1 = entry.getValue();
			Object value2 = map2.get(key);
			if (! objectEquals(value1, value2)) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean objectEquals(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 == null || obj2 == null) {
			return false;
		}
		return obj1.equals(obj2);
	}
	
	public static Map<String, String> toStringMap(String... pairs) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

	@SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(Object ... pairs) {
	    Map<K, V> ret = new HashMap<K, V>();
	    if (pairs == null || pairs.length == 0) return ret;
	
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
        }        
        int len = pairs.length / 2;
        for (int i = 0; i < len; i ++) {
            ret.put((K) pairs[2 * i], (V) pairs[2 * i + 1]);
        }
	    return ret;
	}
	
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
	}

	private CollectionUtils() {
	}
}
