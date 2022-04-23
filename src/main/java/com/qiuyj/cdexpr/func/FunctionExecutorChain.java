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

    private static final List<FunctionExecutor<? extends FunctionPrototype>> FUNCTION_EXECUTORS;
    private static final Set<Class<FunctionExecutor<? extends FunctionPrototype>>> EXISTED_FUNCTION_EXECUTORS;
    static {
        EXISTED_FUNCTION_EXECUTORS = Collections.newSetFromMap(new ConcurrentHashMap<>());
        FUNCTION_EXECUTORS = new CopyOnWriteArrayList<>(List.of(new SystemFunctionExecutor(), new InvocableFunctionExecutor()));
        Class<FunctionExecutor<? extends FunctionPrototype>> klass;
        for (FunctionExecutor<? extends FunctionPrototype> executor : FUNCTION_EXECUTORS) {
            klass = (Class<FunctionExecutor<? extends FunctionPrototype>>) executor.getClass();
            EXISTED_FUNCTION_EXECUTORS.add(klass);
        }
    }

    /**
     * 根据给定的函数名，选择合适的函数执行器，并执行相应的函数
     * @param functionName 函数名
     * @param args 函数执行的传入参数
     * @return 函数执行结果
     */
    public static Object callFunction(String functionName, Object... args) {
        for (FunctionExecutor<?> executor : FUNCTION_EXECUTORS) {
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
    public static void addFunctionExecutor(FunctionExecutor<? extends FunctionPrototype> functionExecutor) {
        Objects.requireNonNull(functionExecutor);
        Class<FunctionExecutor<? extends FunctionPrototype>> klass =
                (Class<FunctionExecutor<? extends FunctionPrototype>>) functionExecutor.getClass();
        if (EXISTED_FUNCTION_EXECUTORS.add(klass)) {
            FUNCTION_EXECUTORS.add(functionExecutor);
        }
        else {
            throw new IllegalArgumentException("The current FunctionExecutor: " + klass + " has been already existed");
        }
    }
}
