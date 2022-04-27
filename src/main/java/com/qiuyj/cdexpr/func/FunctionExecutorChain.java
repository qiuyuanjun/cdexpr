package com.qiuyj.cdexpr.func;

import com.qiuyj.cdexpr.func.internal.SystemFunctionExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 函数执行器链
 * @author qiuyj
 * @since 2022-04-23
 */
@SuppressWarnings("unchecked")
public class FunctionExecutorChain {

    private static final List<FunctionExecutor> FUNCTION_EXECUTORS = new CopyOnWriteArrayList<>();
    private static final Set<Class<FunctionExecutor>> EXISTED_FUNCTION_EXECUTORS =
            Collections.newSetFromMap(new ConcurrentHashMap<>());
    static {
        List.of(new SystemFunctionExecutor(), new InvocableFunctionExecutor())
                .forEach(FunctionExecutorChain::addFunctionExecutor);
    }

    /**
     * 根据给定的函数名，选择合适的函数执行器，并执行相应的函数
     * @param functionName 函数名
     * @param args 函数执行的传入参数
     * @return 函数执行结果
     */
    public static Object callFunction(String functionName, Object... args) {
        for (FunctionExecutor executor : FUNCTION_EXECUTORS) {
            FunctionPrototype funcProto = executor.getFunctionPrototype(functionName, args);
            if (Objects.nonNull(funcProto)) {
                return executor.execute(funcProto, args);
            }
        }
        throw new IllegalStateException("No such function: " + functionName);
    }

    /**
     * 新增{@code FunctionExecutor}函数执行器
     * @param functionExecutor 要新增的函数执行器
     */
    public static void addFunctionExecutor(FunctionExecutor functionExecutor) {
        Objects.requireNonNull(functionExecutor);
        Class<FunctionExecutor> klass =
                (Class<FunctionExecutor>) functionExecutor.getClass();
        if (EXISTED_FUNCTION_EXECUTORS.add(klass)) {
            FUNCTION_EXECUTORS.add(functionExecutor);
        }
        else {
            throw new IllegalArgumentException("The current FunctionExecutor: " + klass + " has been already existed");
        }
    }
}
