package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.FunctionExecutor;
import com.qiuyj.cdexpr.func.FunctionPrototype;

/**
 * @author qiuyj
 * @since 2022-04-21
 */
public class DefaultFunctionExecutor implements FunctionExecutor {

    public static final DefaultFunctionExecutor INSTANCE = new DefaultFunctionExecutor();

    @Override
    public Object execute(String functionName, Object... parameters) {
        return null;
    }

    @Override
    public Object execute(FunctionPrototype functionPrototype, Object... parameters) {
        return null;
    }
}
