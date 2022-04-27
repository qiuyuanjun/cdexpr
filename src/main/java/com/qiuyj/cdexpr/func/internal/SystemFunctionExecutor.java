package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.AbstractGenericSupportFunctionExecutor;
import com.qiuyj.cdexpr.func.FunctionPrototype;
import com.qiuyj.cdexpr.func.internal.prototype.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统函数执行器
 * @author qiuyj
 * @since 2022-04-23
 */
public class SystemFunctionExecutor extends AbstractGenericSupportFunctionExecutor<SystemFunctionPrototype> {

    private static final Map<String, SystemFunctionPrototype> SYSTEM_FUNCTIONS;
    static {
        Map<String, SystemFunctionPrototype> temp = new HashMap<>();
        registerInternal(new DateFormatFunctionPrototype(), temp);
        registerInternal(new NowDateFunctionPrototype(), temp);
        registerInternal(new NowDateTimeFunctionPrototype(), temp);
        registerInternal(new FirstNotNullFunctionPrototype(), temp);
        registerInternal(new FirstNotEmptyStringFunctionPrototype(), temp);
        SYSTEM_FUNCTIONS = Collections.unmodifiableMap(temp);
    }

    private static void registerInternal(SystemFunctionPrototype systemFunctionPrototype, Map<String, SystemFunctionPrototype> map) {
        map.put(systemFunctionPrototype.name(), systemFunctionPrototype);
    }

    @Override
    public FunctionPrototype getFunctionPrototype(String functionName, Object... parameters) {
        return SYSTEM_FUNCTIONS.get(functionName);
    }

    @Override
    protected Object doExecutor(SystemFunctionPrototype functionPrototype, Object... parameters) {
        return functionPrototype.call(parameters);
    }
}
