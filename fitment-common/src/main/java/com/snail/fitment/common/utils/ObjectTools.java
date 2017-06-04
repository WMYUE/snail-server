package com.snail.fitment.common.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.snail.fitment.common.collection.CollectionUtils;

/**
 * @author Administrator
 * 
 */
public class ObjectTools {
	private static final Object[] EMPTY_ARGS_ARRAY = new Object[0];
	private static final Date DEFAULT_DATE = DefaultValues.DEFAULT_DATE;
	private static final Object DEFAULT_OBJECT = new Object();
	private static final Hashtable TypeDefaultValues = new Hashtable();
	private static final Class<?>[] classes = { Long.class, Integer.class,
			Short.class, Float.class, Double.class, Byte.class, Character.class };
	
	public static final Logger log=Logger.getLogger(ObjectTools.class);

	private static Map<Class<?>, Object> primitivesDefaultValue = new HashMap<Class<?>, Object>();
	static {
		primitivesDefaultValue.put(long.class, Long.valueOf(0));
		primitivesDefaultValue.put(short.class, Short.valueOf((short) 0));
		primitivesDefaultValue.put(double.class, Double.valueOf(0));
		primitivesDefaultValue.put(float.class, Float.valueOf(0));
		primitivesDefaultValue.put(int.class, Integer.valueOf(0));
		primitivesDefaultValue.put(byte.class, Byte.valueOf((byte) 0));
		primitivesDefaultValue.put(boolean.class, Boolean.FALSE);
		primitivesDefaultValue.put(char.class, Character.valueOf((char) 0));
	}

	public static String objToLogString(Object o){
		if(o==null)
			return null;
		if (o instanceof String) {
			return formatLogString((String)o, 2000);
		} else if(o instanceof Collection){
			return CollectionUtils.toLogString((Collection)o);
		} else if (o instanceof Map) {
			return CollectionUtils.toLogString((Map)o);
		} else if (o.getClass().isArray()) {
			return CollectionUtils.arrayToLogString(o);
		} else if (o instanceof Date) {
			return DateTools.format((Date)o);
		} else if (o instanceof Map.Entry) {
			Map.Entry entry = (Map.Entry)o;
			return new StringBuilder().append(objToLogString(entry.getKey()))
					.append("=").append(objToLogString(entry.getValue()))
					.toString();
		} else {
			return o.toString();
		}
	}

	public static String objToLogString(Object o, int elementLimit, int charLimit){
		if(o==null)
			return null;
		if (o instanceof String) {
			return formatLogString((String)o, charLimit);
		} else if (o instanceof Collection){
			return CollectionUtils.toLogString((Collection)o, elementLimit, charLimit);
		} else if (o instanceof Map) {
			return CollectionUtils.toLogString((Map)o, elementLimit, charLimit);
		} else if (o.getClass().isArray()) {
			return CollectionUtils.arrayToLogString(o, elementLimit, charLimit);
		} else if (o instanceof Date) {
			return DateTools.format((Date)o);
		} else if (o instanceof Map.Entry) {
			Map.Entry entry = (Map.Entry)o;
			return new StringBuilder().append(objToLogString(entry.getKey(), elementLimit, charLimit))
					.append("=").append(objToLogString(entry.getValue(), elementLimit, charLimit))
					.toString();
		} else {
			return o.toString();
		}
	}

	public static String formatLogString(String str, int charLimit){
		//debug模式或长度小于1410，输入body的完整内容
		if ( str == null || str.length() <= charLimit) {
			return str;
		}
		
		//按 charLimit 输出前面和后面各一部分字节，中间以点号省略
		int halfLimit = charLimit/2;
		StringBuffer buffer = new StringBuffer();
		buffer.append(str.substring(0, halfLimit))
			.append("..........")
			.append(str.substring(str.length() - halfLimit));
		
		return buffer.toString();
	}

	/**
	 * 比较2个Long行数据的值
	 * 
	 * @return
	 */
	public static boolean equals(Long a, Long b) {
		if (a == null && b == null)
			return true;
		if (a == null || b == null)
			return false;
		return a.longValue() == b.longValue();
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(Integer a, Integer b) {
		if (a == null && b == null)
			return true;
		if (a == null || b == null)
			return false;
		return a.intValue() == b.intValue();
	}

	public static boolean propsEquals(Object x, Object y,
			Set<String> ignoreProps) {
		// 都为 null ，返回 true
		if (x == null && y == null) {
			return true;
		}
		// 只有一个 为 null ，返回 false
		if (x == null || y == null) {
			return false;
		}
		if (!x.getClass().equals(y.getClass())) {
			return false;
		}
		// 两个都不为 null ，依次比较字段
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(x);
		for (int i = 0; i < origDescriptors.length; i++) {
			// 如果在需要忽略的属性列表里，直接跳过
			PropertyDescriptor field = origDescriptors[i];
			if (ignoreProps != null && ignoreProps.contains(field.getName())) {
				continue;
			}
			// 只判断 ValueType[基本类型], String, Enum 类型的字段，不处理其它类型的字段（对象类型的字段）
			Class<?> classz = field.getPropertyType();
			if (!(checkPrimitive(classz) || classz.isPrimitive()
					|| classz.isEnum() || String.class.equals(classz))) {
				continue;
			}
			String name = field.getName();
			try {
				Object xValue = BeanTools.getProperty(x, name);
				Object yValue = BeanTools.getProperty(y, name);

				if (classz.equals(String.class)) {
					String xString = StringUtils.trimToEmpty((String) xValue);
					String yString = StringUtils.trimToEmpty((String) yValue);
					if (xString.equals(yString)) {
						continue;
					} else {
						return false;
					}
				}

				if (xValue == null) {
					if (yValue != null)
						return false;
					continue;
				}
				//日期比较时，仅精确到秒比较
//				if(classz.equals(Date.class))
//				{
//					long xTime=((Date)xValue).getTime()/1000;
//					long yTime=((Date)yValue).getTime()/1000;
//					if(xTime==yTime){
//						continue; }
//					else{
//						return false;
//					}
//				}
//				else {
				if (!xValue.equals(yValue))
					return false;
			} catch (Exception e) {
				// 如有异常，为不等
				return false;
			}

		}
		return true;
	}

	public static boolean checkPrimitive(Class<?> className) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].equals(className))
				return true;
		}
		return false;
	}

	/**
	 * 如src中的属性为空或默认值或等于dest中相同的属性值，返回true
	 * 
	 * @param src
	 * @param dest
	 * @param ignoreProps
	 * @return
	 */
	public static boolean propsLess(Object src, Object dest,
			Set<String> ignoreProps) {
		if (src == null)
			return true;
		if (dest == null)
			return false;
		PropertyDescriptor[] propertyDesList = PropertyUtils
				.getPropertyDescriptors(src.getClass());
		for (PropertyDescriptor pi : propertyDesList) {
			if (ignoreProps != null && ignoreProps.contains(pi.getName())) {
				continue;
			}
			// 只判断值类型, String, Enum 类型的字段，不处理其它类型的字段（对象类型的字段）
			Class<?> classz = pi.getPropertyType();
			if (!(checkPrimitive(classz) || classz.isPrimitive()
					|| classz.isEnum() || String.class.equals(classz))) {
				continue;
			}
			String name = pi.getName();
			try {
				Object xValue = BeanTools.getProperty(src, name);
				if (isDefaultValue(xValue)) {
					continue;
				}
				Object yValue = BeanTools.getProperty(dest, name);
				if (classz.equals(String.class)) {
					String xString = StringUtils.trimToEmpty((String) xValue);
					String yString = StringUtils.trimToEmpty((String) yValue);
					if (xString.equals(yString)) {
						continue;
					} else {
						return false;
					}
				}
//				//日期比较时，仅精确到秒比较
//				if(classz.equals(Date.class))
//				{
//					long xTime=((Date)xValue).getTime()/1000;
//					long yTime=((Date)yValue).getTime()/1000;
//					if(xTime!=yTime)
//						return false;
//					else {
//						continue;
//					}
//				}
				 if (!xValue.equals(yValue)) {
					return false;
				}
			} catch (Exception e) {
				// 如有异常，为不等
				return false;
			}
		}
		return true;
	}

	public static boolean mergeNoDefaultProps(Object from, Object to,
			boolean overwriteConflict, Set<String> ignoreProps) {
		// 只有一个 为 null ，返回 false
		if (from == null || to == null) {
			return false;
		}
		if (!from.getClass().equals(to.getClass())) {
			return false;
		}
		// 两个都不为 null ，依次比较字段
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(from);
		for (int i = 0; i < origDescriptors.length; i++) {
			// 如果在需要忽略的属性列表里，直接跳过
			PropertyDescriptor field = origDescriptors[i];
			if (ignoreProps != null && ignoreProps.contains(field.getName())) {
				continue;
			}
			// 只处理 ValueType, String,Date, Enum 类型的字段，不处理其它类型的字段（对象类型的字段）
			Class classz = field.getPropertyType();
			if (!(checkPrimitive(classz) || classz.isPrimitive()
					|| classz.isEnum() || String.class.equals(classz)|| Date.class.equals(classz))) {
				continue;
			}

			String name = field.getName();
			try {
				Object fromPropValue = BeanTools.getProperty(from, name);
				Object toPropValue = BeanTools.getProperty(to, name);
				// 对比源属性值与目标属性值，如果需要复制，则复制
				if (field.getWriteMethod()!=null && needCopyPropValue(fromPropValue, toPropValue,
						overwriteConflict)) {
					// 使用反射方法将源属性复制到目标对象的目标属性
					BeanTools.setProperty(to, name, fromPropValue);
				}

			} catch (Exception e) {
				// 如有异常，为不等
				log.error("copy property error",e);
				continue;
			}
		}
		return true;
	}

	// / <summary>
	// / 判断是否需要将一个属性对象复制到另一个属性对象中（只用于基本数据类型的属性的比较）
	// / </summary>
	// / <param name="fromPropValue"></param>
	// / <param name="toPropValue"></param>
	// / <param name="overwriteConflict"></param>
	// / <returns></returns>
	public static boolean needCopyPropValue(Object fromPropValue,
			Object toPropValue, boolean overwriteConflict) {
//		// 若两个值相等，跳过（包括了两个属性都为默认值及都为 null 的情况）
//		if ((fromPropValue == null && toPropValue == null)) {
//			return false;
//		}
//		if (fromPropValue != null && toPropValue != null
//				&& fromPropValue.equals(toPropValue)) {
//			return false;
//		}
//		if (toPropValue == null)
//			return true;
//
//		// 若源属性或目标属性有一个为默认值，则复制非默认值的属性（两个不全等，所以一定有一个非默认值）
//		if (!isDefaultValue(fromPropValue )|| !isDefaultValue(toPropValue)) {
//			if (isDefaultValue(toPropValue)){
//				// 目标属性为默认值，需要复制
//				return true;
//			}
//			// 源属性为默认值，不需要复制
//			return false;
//		}

		// 两个都不为默认值，存在冲突，根据冲突处理策略决定
		return overwriteConflict;
	}

	/**
	 * 得到类型的默认值 除日期返回自定义值外其他遵循java规范
	 * 
	 * @param type
	 * @return
	 */
	public static Object getDefaultValue(Class type) {
		/*
		 * Object defaultValue; if(Primitives.isPrimitiveClass(type)) { return
		 * Primitives.getDefaultValue(type); } //if (!(type.isPrimitive() ||
		 * type.isEnum() || type.)) { // 非值类型、枚举类型，默认值为 null // defaultValue =
		 * null; //} //else { // 其他类型（简单类型） if (Date.class.equals(type)) { //
		 * 对日期类型需单独处理 defaultValue = DEFAULT_DATE; } else { // 其他类型 defaultValue
		 * = TypeDefaultValues.get(type); if (defaultValue == null) {
		 * defaultValue = DEFAULT_OBJECT; try { defaultValue =
		 * type.newInstance(); } catch (InstantiationException e) {
		 * e.printStackTrace(); } catch (IllegalAccessException e) {
		 * e.printStackTrace(); } TypeDefaultValues.put(type, defaultValue);; }
		 * } //} return defaultValue;
		 */
		Object defaultValue;
		if (Primitives.isPrimitiveClass(type)) {
			return Primitives.getDefaultValue(type);
		}
		if (type.isPrimitive()) {
			return primitivesDefaultValue.get(type);
		}

		if (Date.class.equals(type)) {
			defaultValue = DEFAULT_DATE;
		} else {
			return null;
		}
		return defaultValue;
	}

	public static boolean contains(List list, Object o,
			IEqualityComparer comparer) {
		for (int i = 0; i < list.size(); i++) {
			if (comparer.equals(list.get(i), o))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param o
	 * @param attribute
	 * @return
	 */
	public static boolean contains(Object o, IEqualityComparer comparer) {
		return false;
	}

	// / <summary>
	// / 对两个 IList 泛型对象进行比较，不考虑顺序。
	// / 要求泛型参数类自己实现严格比较的方法。
	// / 复杂度为 n 的平方，只适用于 List 比较小的情况，如名片中的集合属性。
	// / </summary>
	// / <typeparam name="T"></typeparam>
	// / <param name="x"></param>
	// / <param name="y"></param>
	// / <returns></returns>
	public static boolean listEquals(List x, List y) {
		return equals(x, y, new IEqualityComparer() {

			public boolean equals(Object x, Object y) {
				if (x == null && y == null)
					return true;
				if (x != null)
					return x.equals(y);
				if (y != null)
					return y.equals(x);
				return false;
			}

			@Override
			public int hashCode(Object obj) {
				return obj.hashCode();
			}
		});
	}

	// / <summary>
	// / 用定制的 comparer 对两个 IList 泛型对象进行比较，不考虑顺序。
	// / 要求泛型参数类自己实现严格比较的方法。
	// / 复杂度为 n 的平方，只适用于 List 比较小的情况，如名片中的集合属性。
	// / </summary>
	// / <typeparam name="T"></typeparam>
	// / <param name="x"></param>
	// / <param name="y"></param>
	// / <param name="comparer">定制的 comparer </param>
	// / <returns></returns>
	public static boolean equals(List x, List y, IEqualityComparer comparer) {
		// 都为 null ，返回 true
		if (x == null && y == null) {
			return true;
		}
		// 只有一个 为 null ，进一步判断另一个是否为空列表
		if (x == null || y == null) {
			// 如果非 null 的参数为空列表，返回 true
			if (x != null && x.size() == 0) {
				return true;
			}
			if (y != null && y.size() == 0) {
				return true;
			}
			// 非 null 的参数不为空列表，返回 false
			return false;
		}
		// 元素个数不等，返回 false
		if (x.size() != y.size()) {
			return false;
		}
		// 以下按照内容进行逐个比较

		List xTemp = new ArrayList();
		List yTemp = new ArrayList();

		// 初始化两个临时列表，以防修改参数中原始列表
		for (Object t : x) {
			xTemp.add(t);
		}
		for (Object t : y) {
			yTemp.add(t);
		}
		// 算法：
		// 从两个临时列表中依次删除相等元素，
		// 若有一个临时列表中还有剩余，则不等，否则为相等
		int xi = 0, yi = 0;
		boolean foundEqual = false;

		while (xi < xTemp.size()) {
			Object tx = xTemp.get(xi); // 总是取 xTemp 中第一个未判断过的记录进行比较
			foundEqual = false;
			for (yi = 0; yi < yTemp.size(); yi++) {
				Object ty = yTemp.get(yi);
				if (comparer.equals(tx, ty)) {
					foundEqual = true;
					break;
				}
			}
			if (foundEqual) {
				// 若找到了相等元素，删除当前比较的元素
				xTemp.remove(xi);
				yTemp.remove(yi);
				// xTemp 中仍然取当前位置的元素
			} else {
				// 未找到相等元素
				// xTemp 中取下一个位置的元素
				xi++;
			}
			if (yTemp.size() == 0) {
				break;
			}
		}

		// 有剩余元素，则说明两个列表不等
		if (xTemp.size() > 0 || yTemp.size() > 0) {
			return false;
		}

		return true;
	}

	/**
	 * 判断一个数值是否是默认值 by zhujing 2011-4-27
	 * 
	 * @param value
	 */
	public static boolean isDefaultValue(Object value) {
		if (value == null) {
			return true;
		}

		Class clazz=value.getClass();
		// 对字符串做特殊判断（null 或者空串都认为是默认值）
		if (clazz.equals(String.class)) {
			return StringUtils.isBlank((String) value);
		}

		// 根据属性类型计算默认值
		Object defaultValue = getDefaultValue(clazz);

		return value.equals(defaultValue);
	}
}
