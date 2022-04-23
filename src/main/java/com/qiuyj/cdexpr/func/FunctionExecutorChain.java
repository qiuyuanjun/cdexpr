package com.qiuyj.cdexpr.func;

import com.qiuyj.cdexpr.func.internal.SystemFunctionExecutor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 函数执行器链
 * @author qiuyj
 * @since 2022-04-23
 */
public class FunctionExecutorChain {

    private static final Set<FunctionExecutor<? extends FunctionPrototype>> FUNCTION_EXECUTORS = new HashSet<>();
    static {
        FUNCTION_EXECUTORS.add(new SystemFunctionExecutor());
        FUNCTION_EXECUTORS.add(new InvocableFunctionExecutor());
    }

    public static Object callFunction(String functionName, Object... args) {
        for (FunctionExecutor<?> executor : FUNCTION_EXECUTORS) {
            FunctionPrototype funcProto = executor.getFunctionPrototype(functionName, args);
            if (Objects.nonNull(funcProto)) {
                return executor.execute(funcProto, args);
            }
        }
        throw new IllegalStateException("No such function: " + functionName);
    }
}
