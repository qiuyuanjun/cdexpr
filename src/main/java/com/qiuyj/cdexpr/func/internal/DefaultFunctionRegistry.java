package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.FunctionPrototype;
import com.qiuyj.cdexpr.func.FunctionRegistry;
import com.qiuyj.cdexpr.func.internal.prototype.DateFormatFunctionPrototype;
import com.qiuyj.cdexpr.func.internal.prototype.NowDateFunctionPrototype;
import com.qiuyj.cdexpr.func.internal.prototype.NowDateTimeFunctionPrototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的方法原型注册器，该注册器会注册所有的系统函数
 * @author qiuyj
 * @since 2022-04-21
 */
public class DefaultFunctionRegistry implements FunctionRegistry {

    private final Map<String, FunctionPrototype> registry = new ConcurrentHashMap<>();
    private final Map<String, List<FunctionPrototype>> overrideFunctionMap = new ConcurrentHashMap<>();

    private static volatile DefaultFunctionRegistry theInstance;

    private DefaultFunctionRegistry() {
        registerTrustedFunction(new NowDateFunctionPrototype());
        registerTrustedFunction(new NowDateTimeFunctionPrototype());
        registerTrustedFunction(new DateFormatFunctionPrototype());
    }

    @Override
    public void registerFunction(FunctionPrototype functionPrototype) {
        registerTrustedFunction(Objects.requireNonNull(functionPrototype));
    }

    @Override
    public FunctionPrototype getByName(String functionName) {
        List<FunctionPrototype> overrideFunctions = overrideFunctionMap.get(functionName);
        if (Objects.isNull(overrideFunctions) || overrideFunctions.size() != 1) {
            throw new IllegalStateException();
        }
        return overrideFunctions.get(0);
    }

    private void registerTrustedFunction(FunctionPrototype functionPrototype) {
        String signature = functionPrototype.signature();
        if (Objects.nonNull(registry.putIfAbsent(signature, functionPrototype))) {
            throw new IllegalStateException("Duplicate function prototype: " + signature);
        }
        String name = functionPrototype.name();
        List<FunctionPrototype> overrideFunctions = overrideFunctionMap.computeIfAbsent(name, s -> new ArrayList<>());
        overrideFunctions.add(functionPrototype);
    }

    public static DefaultFunctionRegistry getInstance() {
        if (Objects.isNull(theInstance)) {
            synchronized (DefaultFunctionRegistry.class) {
                if (Objects.isNull(theInstance)) {
                    theInstance = new DefaultFunctionRegistry();
                }
            }
        }
        return theInstance;
    }
}
