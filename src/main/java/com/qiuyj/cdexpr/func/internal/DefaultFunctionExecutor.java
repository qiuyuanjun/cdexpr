package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.FunctionExecutor;
import com.qiuyj.cdexpr.func.FunctionPrototype;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-04-21
 */
public class DefaultFunctionExecutor implements FunctionExecutor {

    public static final DefaultFunctionExecutor INSTANCE = new DefaultFunctionExecutor();

    @Override
    public Object execute(String functionName, Object... parameters) {
        return execute(DefaultFunctionRegistry.getInstance().getByName(functionName), parameters);
    }

    @Override
    public Object execute(FunctionPrototype functionPrototype, Object... parameters) {
        Method targetMethod = Objects.requireNonNull(functionPrototype).getTargetMethod();
        int parameterCount = targetMethod.getParameterCount(),
            actualParameterCount = Objects.isNull(parameters) ? 0 : parameters.length;
        if (targetMethod.getParameterCount() != actualParameterCount) {
            throw new IllegalArgumentException("The number of function parameters does not match, need " + parameterCount + " but actual is " + actualParameterCount);
        }
        return functionPrototype.invoke(parameters);
    }
}
