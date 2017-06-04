package com.snail.fitment.common.utils;


import java.util.HashMap;
import java.util.Map;

/**
 * Java基本数据类型定义.<br>
 * <p/> Copyright &copy; 2005 Runway Technology Co., LTD. All rights reserved.
 * 
 * @author <a href="mailto:wangxianzhu@runway.cn.net">Wang Xianzhu</a>
 * @author <a href="mailto:zhangxiaohai@runway.cn.net">Zhang Xiaohai</a>
 * @version $Revision: 1.3 $
 */
public final class Primitives {
    public static final String TYPE_BOOLEAN = "boolean";

    public static final String TYPE_BYTE = "byte";

    public static final String TYPE_CHAR = "char";

    public static final String TYPE_DOUBLE = "double";

    public static final String TYPE_FLOAT = "float";

    public static final String TYPE_INT = "int";

    public static final String TYPE_LONG = "long";

    public static final String TYPE_SHORT = "short";

    private static final Map primitiveTypes;

    private static final Map primitiveTypesToClass;

    private static final Map primitiveClassToTypes;
    
    private static final Map primitiveTypesDefaultValue;

    static {
        primitiveTypes = new HashMap();
        primitiveTypes.put(TYPE_BOOLEAN, Boolean.TYPE);
        primitiveTypes.put(TYPE_BYTE, Byte.TYPE);
        primitiveTypes.put(TYPE_CHAR, Character.TYPE);
        primitiveTypes.put(TYPE_DOUBLE, Double.TYPE);
        primitiveTypes.put(TYPE_FLOAT, Float.TYPE);
        primitiveTypes.put(TYPE_INT, Integer.TYPE);
        primitiveTypes.put(TYPE_LONG, Long.TYPE);
        primitiveTypes.put(TYPE_SHORT, Short.TYPE);

        primitiveTypesToClass = new HashMap();
        primitiveTypesToClass.put(Integer.TYPE, Integer.class);
        primitiveTypesToClass.put(Long.TYPE, Long.class);
        primitiveTypesToClass.put(Float.TYPE, Float.class);
        primitiveTypesToClass.put(Double.TYPE, Double.class);
        primitiveTypesToClass.put(Boolean.TYPE, Boolean.class);
        primitiveTypesToClass.put(Byte.TYPE, Byte.class);
        primitiveTypesToClass.put(Character.TYPE, Character.class);
        primitiveTypesToClass.put(Short.TYPE, Short.class);

        primitiveClassToTypes = new HashMap();
        primitiveClassToTypes.put(Integer.class, Integer.TYPE);
        primitiveClassToTypes.put(Long.class, Long.TYPE);
        primitiveClassToTypes.put(Float.class, Float.TYPE);
        primitiveClassToTypes.put(Double.class, Double.TYPE);
        primitiveClassToTypes.put(Boolean.class, Boolean.TYPE);
        primitiveClassToTypes.put(Byte.class, Byte.TYPE);
        primitiveClassToTypes.put(Character.class, Character.TYPE);
        primitiveClassToTypes.put(Short.class, Short.TYPE);

        primitiveTypesDefaultValue = new HashMap();
        primitiveTypesDefaultValue.put(Integer.class, Integer.valueOf(0));
        primitiveTypesDefaultValue.put(Long.class, Long.valueOf(0));
        primitiveTypesDefaultValue.put(Float.class, Float.valueOf(0));
        primitiveTypesDefaultValue.put(Double.class, Double.valueOf(0));
        primitiveTypesDefaultValue.put(Boolean.class, Boolean.valueOf(false));
        primitiveTypesDefaultValue.put(Byte.class, Byte.valueOf((byte)0));
        primitiveTypesDefaultValue.put(Character.class, Character.valueOf((char)0));
        primitiveTypesDefaultValue.put(Short.class, Short.valueOf((short)0));
    }

    /**
     * 根据指定的基本类型字符串获取其对应的基本类型Class对象.
     * @param primitiveType 基本类型
     * @return 基本类型Class对象.
     */
    public static Class getPrimitiveTypeClass(String primitiveType) {
        return (Class)primitiveTypes.get(primitiveType);
    }

    /**
     * 根据指定的基本类型Class对象获取对应的数据类型的Class对象.比如根据Long.TYPE获取Long.class.
     * @param primitiveTypeClass 基本类型Class对象.
     * @return 数据类型Class对象.
     */
    public static Class getTypeClass(Class primitiveTypeClass) {
        return (Class)primitiveTypesToClass.get(primitiveTypeClass);
    }

    public static boolean isPrimitiveClass(Class clazz) {
        return primitiveClassToTypes.containsKey(clazz);
    }

    public static Object getDefaultValue(Class clazz) {
    	return primitiveTypesDefaultValue.get(clazz);
    }
    
    private Primitives() {
        // utility class
    }

}
