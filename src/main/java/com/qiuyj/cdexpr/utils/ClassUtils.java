package com.qiuyj.cdexpr.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 涉及到{@code Class}相关的操作的工具类
 * @author qiuyj
 * @since 2022-04-21
 */
public abstract class ClassUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES = new HashMap<>();
    static {
        PRIMITIVE_TYPES.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TYPES.put(Long.TYPE, Long.class);
        PRIMITIVE_TYPES.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_TYPES.put(Short.TYPE, Short.class);
        PRIMITIVE_TYPES.put(Character.TYPE, Character.class);
        PRIMITIVE_TYPES.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TYPES.put(Float.TYPE, Float.class);
        PRIMITIVE_TYPES.put(Double.TYPE, Double.class);
    }

    private ClassUtils() {
        // for private
    }

    public static Method getMethod(Class<?> klass, String methodName, Class<?>... parameterTypes) {
        Class<?> currentKlass = klass;
        while (Objects.nonNull(currentKlass) && currentKlass != Object.class) {
            try {
                return currentKlass.getDeclaredMethod(methodName, parameterTypes);
            }
            catch (NoSuchMethodException e) {
                // ignore
            }
            currentKlass = currentKlass.getSuperclass();
        }
        throw new IllegalArgumentException("Can not find method: " + methodName + " in class: " + klass.getName());
    }

    /**
     * 判断两个类型是否能匹配上
     * @param expect 期望的类型
     * @param actual 实际的值
     * @return 如果能匹配上，那么返回{@code true}，否则返回{@code false}
     */
    public static boolean parameterTypeMatch(Class<?> expect, Object actual) {
        if (Objects.isNull(actual)) {
            return !expect.isPrimitive();
        }
        Class<?> actualType = actual.getClass();
        return expect.isPrimitive()
                ? actualType == PRIMITIVE_TYPES.get(expect)
                : expect.isAssignableFrom(actualType);
    }

    public static String methodSignature(String methodName, Class<?>[] parameterTypes) {
        StringBuilder signature = new StringBuilder(methodName);
        StringJoiner parameter = new StringJoiner(",", "(", ")");
        if (parameterTypes.length > 0) {
            for (Class<?> parameterType : parameterTypes) {
                parameter.add(parameterType.descriptorString());
            }
            signature.append(parameter);
        }
        return signature.toString();
    }

    public static Class<?> getGenericActualType(Class<?> genericClass, int index) {
        if (Objects.isNull(genericClass)) {
            throw new IllegalArgumentException("Generic class can not be null");
        }
        Type t = genericClass.getGenericSuperclass();
        if (t instanceof ParameterizedType parameterizedType) {
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            Objects.checkIndex(index, actualTypes.length);
            return (Class<?>) actualTypes[index];
        }
        return null;
    }
}
