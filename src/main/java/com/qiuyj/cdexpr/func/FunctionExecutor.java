package com.qiuyj.cdexpr.func;

/**
 * 函数执行器
 * @author qiuyj
 * @since 2022-04-21
 */
public interface FunctionExecutor {

    /**
     * 执行给定的函数名对应的函数，该方法只能执行该函数名唯一的函数，如果对应的有多个函数名（函数参数不同），会抛出异常
     * @param functionPrototype 函数原型
     * @param parameters 函数参数
     * @return 函数执行结果
     */
    Object execute(FunctionPrototype functionPrototype, Object... parameters);

    /**
     * 判断当前的函数执行器是否可以执行传入的函数名
     * @param functionName 函数名
     * @param parameters 函数参数
     * @return 对应的函数原型
     */
    FunctionPrototype getFunctionPrototype(String functionName, Object... parameters);
}
