package com.qiuyj.cdexpr.func;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * 自定义的静态方法函数原型
 * @author qiuyj
 * @since 2022-04-23
 */
public class InvocableFunctionPrototype implements FunctionPrototype {

    /**
     * 目标函数
     */
    private final Method targetMethod;

    /**
     * 是否执行函数之前的相关检查（参数数量，参数类型检查）
     */
    private boolean preInvokeCheck;

    public InvocableFunctionPrototype(Method targetMethod) {
        this.targetMethod = Objects.requireNonNull(targetMethod);
        if (!Modifier.isStatic(targetMethod.getModifiers())) {
            throw new IllegalStateException("Method must be static!");
        }
        if (!targetMethod.trySetAccessible()) {
            throw new IllegalStateException("Method is not executable!");
        }
    }

    public void setPreInvokeCheck(boolean preInvokeCheck) {
        this.preInvokeCheck = preInvokeCheck;
    }

    @Override
    public String name() {
        return targetMethod.getName();
    }

    @Override
    public Class<?>[] parameterTypes() {
        return targetMethod.getParameterTypes();
    }

    /**
     * 根据传入进来的参数，执行对应的函数
     * @param args 参数
     * @return 执行函数的结果
     */
    public Object invoke(Object... args) {
        if (preInvokeCheck) {
            preInvokeCheck(targetMethod);
        }
        try {
            return targetMethod.invoke(null, args);
        }
        catch (IllegalAccessException e) {
            // ignore, never reach here
        }
        catch (InvocationTargetException e) {
            throw new IllegalStateException("Invoke function " + name() + " error", e.getTargetException());
        }
        throw new IllegalStateException("Never reach here");
    }

    private void preInvokeCheck(Method targetMethod, Object... args) {
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
        int len = parameterTypes.length,
                actualParameterCount = args.length;
        if (actualParameterCount != len) {
            throw new IllegalArgumentException("Parameter count mismatch");
        }
        Class<?> expectType,
                actualType;
        Object actual;
        for (int i = 0; i < len; i++) {
            expectType = parameterTypes[i];
            actual = args[i];
            if (Objects.isNull(actual) && expectType.isPrimitive()) {
                throw new IllegalArgumentException("Null value match primitive type");
            }
            else if (Objects.nonNull(actual)
                    && (expectType != (actualType = actual.getClass())
                    || !expectType.isAssignableFrom(actualType))) {
                throw new IllegalArgumentException("Parameter type mismatch");
            }
        }
    }
}
