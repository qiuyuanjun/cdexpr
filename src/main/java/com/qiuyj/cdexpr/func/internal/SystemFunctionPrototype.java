package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.FunctionPrototype;
import com.qiuyj.cdexpr.utils.ClassUtils;

import java.util.Objects;

/**
 * 系统自定义函数抽象类，系统自定义函数必须是静态函数
 * @author qiuyj
 * @since 2022-04-23
 */
public abstract class SystemFunctionPrototype implements FunctionPrototype {

    /*
     * 系统自定义的函数，约定不存在重名的函数，因此所有的系统自定义的函数都不需要返回参数列表的类型的数组
     */
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

    /**
     * 从参数列表获取给定的下标对应的参数，并检测对应的类型是否匹配
     * @param i 要获取参数列表的下标
     * @param required 参数的类型
     * @param args 参数列表
     * @return 对应下标的参数值
     * @param <T> 给定的类型
     */
    @SuppressWarnings("unchecked")
    protected <T> T checkTypeAndGetFromArgs(int i, Class<T> required, Object... args) {
        Object parameter = args[Objects.checkIndex(i, args.length)];
        if (!ClassUtils.typeValueMatch(required, parameter)) {
            throw new IllegalArgumentException("Parameter type mismatch, require " + required.getName() + ", but actual is " + (Objects.isNull(parameter) ? "null" : parameter.getClass().getName()));
        }
        return (T) parameter;
    }
}
