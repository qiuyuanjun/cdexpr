package com.qiuyj.cdexpr.func;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 函数原型抽象接口
 * @author qiuyj
 * @since 2022-04-20
 */
public abstract class FunctionPrototype {

    public static final Class<?>[] NO_ARGS = new Class<?>[0];

    private String signature;

    /**
     * 实际要执行的方法的{@code Method}对象
     * 必须是静态方法
     */
    private final Method targetMethod;

    protected FunctionPrototype(Method targetMethod) {
        this.targetMethod = Objects.requireNonNull(targetMethod);
        if (!Modifier.isStatic(targetMethod.getModifiers())) {
            throw new IllegalStateException("The method must be static!");
        }
        if (!targetMethod.trySetAccessible()) {
            throw new IllegalStateException("Method is not executable!");
        }
    }

    /**
     * 返回该原型对应的函数名称
     * @return 函数名称
     */
    public abstract String name();

    /**
     * 返回该原型对应的函数的参数的类型
     * @return 函数参数的类型
     */
    public Class<?>[] parameterTypes() {
        return NO_ARGS;
    }

    /**
     * 获取该函数的签名
     * @return 函数签名
     */
    public String signature() {
        if (Objects.isNull(signature)) {
            synchronized (this) {
                if (Objects.isNull(signature)) {
                    StringBuilder sb = new StringBuilder(name());
                    Class<?>[] parameterTypes = parameterTypes();
                    StringJoiner joiner = new StringJoiner(",", "(", ")");
                    if (Objects.nonNull(parameterTypes) && parameterTypes.length > 0) {
                        for (Class<?> parameterType : parameterTypes) {
                            joiner.add(parameterType.descriptorString());
                        }
                    }
                    sb.append(joiner);
                    signature = sb.toString();
                }
            }
        }
        return signature;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }
}
