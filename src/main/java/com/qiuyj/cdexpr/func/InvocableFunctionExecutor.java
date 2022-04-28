package com.qiuyj.cdexpr.func;

import com.qiuyj.cdexpr.utils.ClassUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author qiuyj
 * @since 2022-04-21
 */
public class InvocableFunctionExecutor extends AbstractGenericSupportFunctionExecutor<InvocableFunctionPrototype> {

    /**
     * 当前函数执行器所注册的所有函数原型
     */
    private static final Map<String, List<InvocableFunctionPrototype>> FUNCTIONS = new ConcurrentHashMap<>();
    private static final Set<String> FUNCTION_SIGNATURES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public FunctionPrototype getFunctionPrototype(String functionName, Object... parameters) {
        List<InvocableFunctionPrototype> functions = FUNCTIONS.get(functionName);
        int size;
        if (Objects.nonNull(functions) && (size = functions.size()) > 0) {
            if (size == 1) {
                return functions.get(0);
            }
            // 有多个同名的函数，那么需要根据参数类型确定
            for (InvocableFunctionPrototype function : functions) {
                Class<?>[] parameterTypes = function.parameterTypes();
                int length;
                if ((length = parameters.length) == parameterTypes.length) {
                    boolean match = true;
                    for (int i = 0; i < length; i++) {
                        Class<?> expectType = parameterTypes[i];
                        Object actual = parameters[i];
                        if (!ClassUtils.typeValueMatch(expectType, actual)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        return function;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected Object doExecutor(InvocableFunctionPrototype functionPrototype, Object... parameters) {
        return functionPrototype.invoke(parameters);
    }

    /**
     * 给该函数执行器注册函数原型
     * @param functionPrototype 函数原型
     */
    public static void registerFunction(InvocableFunctionPrototype functionPrototype) {
        Objects.requireNonNull(functionPrototype);
        String functionName = functionPrototype.name();
        if (Objects.isNull(functionName) || functionName.isEmpty()) {
            throw new IllegalArgumentException("Function name can not be null or empty");
        }
        if (FUNCTION_SIGNATURES.add(ClassUtils.methodSignature(functionName, functionPrototype.parameterTypes()))) {
            FUNCTIONS.computeIfAbsent(functionName, key -> new CopyOnWriteArrayList<>())
                    .add(functionPrototype);
        }
        else {
            // 函数重复
            throw new IllegalArgumentException("The current FunctionPrototype has been already existed");
        }
    }
}
