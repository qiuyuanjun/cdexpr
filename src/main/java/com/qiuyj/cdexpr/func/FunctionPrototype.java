package com.qiuyj.cdexpr.func;

/**
 * 函数原型抽象接口
 * @author qiuyj
 * @since 2022-04-20
 */
public interface FunctionPrototype {

    Class<?>[] ZERO_PARAMETER = new Class<?>[0];

    /**
     * 对应的函数的名称
     * @return 函数名称
     */
    String name();

    /**
     * 函数参数类型
     * @return 函数参数类型
     */
    default Class<?>[] parameterTypes() {
        return ZERO_PARAMETER;
    }
}
