package com.qiuyj.cdexpr.func;

/**
 * 函数原型注册服务
 * @author qiuyj
 * @since 2022-04-21
 */
public interface FunctionRegistry {

    void registerFunction(FunctionPrototype functionPrototype);

    FunctionPrototype getByName(String functionName);
}
