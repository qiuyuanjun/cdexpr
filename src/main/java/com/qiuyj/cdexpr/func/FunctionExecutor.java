package com.qiuyj.cdexpr.func;

import com.qiuyj.cdexpr.func.internal.DefaultFunctionExecutor;

/**
 * 函数执行器
 * @author qiuyj
 * @since 2022-04-21
 */
public interface FunctionExecutor {

    Object execute(String functionName, Object... parameters);

    Object execute(FunctionPrototype functionPrototype, Object... parameters);

    /**
     * 得到默认的函数执行器
     * @return 默认的函数执行器，{@link DefaultFunctionExecutor}
     */
    static FunctionExecutor defaultExecutor() {
        return DefaultFunctionExecutor.INSTANCE;
    }
}
