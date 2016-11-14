package com.snail.fitment.common.collection;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;

public class CollectionTools {
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
			throw new RuntimeException("指定排序字段错误！"); 
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
		if(!CollectionUtils.isEmpty(list)){
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
}
