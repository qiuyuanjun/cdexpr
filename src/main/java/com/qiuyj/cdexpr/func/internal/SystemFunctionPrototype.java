package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.FunctionPrototype;

import java.util.Objects;

/**
 * 系统自定义函数抽象类，系统自定义函数必须是静态函数
 * @author qiuyj
 * @since 2022-04-23
 */
public abstract class SystemFunctionPrototype implements FunctionPrototype {

    @Override
    public final Class<?>[] parameterTypes() {
        throw new IllegalStateException("System function should not call this method");
    }

    /**
     * 调用系统函数
     * @param args 函数参数
     * @return 函数调用结果
     */
    public abstract Object call(Object... args);

    @SuppressWarnings("unchecked")
    protected <T> T checkTypeAndGetFromInvocation(int i, Class<T> required, Object... args) {
        Object parameter = args[Objects.checkIndex(i, args.length)];
        if ((Objects.isNull(parameter) && required.isPrimitive())
                || (Objects.nonNull(parameter) && !required.isInstance(parameter))) {
            throw new IllegalArgumentException("Parameter type mismatch");
        }
        return (T) parameter;
    }
}
