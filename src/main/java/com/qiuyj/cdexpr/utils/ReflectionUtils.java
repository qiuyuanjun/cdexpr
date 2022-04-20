package com.qiuyj.cdexpr.utils;

import java.lang.reflect.Method;

/**
 * 反射工具类
 * @author qiuyj
 * @since 2022-04-21
 */
public abstract class ReflectionUtils {

    private ReflectionUtils() {
        // for private
    }

    public static Method getMethod(Class<?> klass, String methodName, Class<?>... parameterTypes) {
        Class<?> currentKlass = klass;
        do {
            try {
                return currentKlass.getDeclaredMethod(methodName, parameterTypes);
            }
            catch (NoSuchMethodException e) {
                // ignore
            }
            currentKlass = currentKlass.getSuperclass();
        }
        while (currentKlass != Object.class);
        throw new IllegalArgumentException("Can not find method: " + methodName + " in class: " + klass.getName());
    }
}
