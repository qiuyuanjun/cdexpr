package com.qiuyj.cdexpr.func.internal;

import com.qiuyj.cdexpr.func.AbstractFunctionExecutor;
import com.qiuyj.cdexpr.func.internal.prototype.DateFormatFunctionPrototype;
import com.qiuyj.cdexpr.func.internal.prototype.NowDateFunctionPrototype;
import com.qiuyj.cdexpr.func.internal.prototype.NowDateTimeFunctionPrototype;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统函数执行器
 * @author qiuyj
 * @since 2022-04-23
 */
public class SystemFunctionExecutor extends AbstractFunctionExecutor<SystemFunctionPrototype> {

    private static final Map<String, SystemFunctionPrototype> SYSTEM_FUNCTIONS;
    static {
        Map<String, SystemFunctionPrototype> temp = new HashMap<>();
        registerInternal(new DateFormatFunctionPrototype(), temp);
        registerInternal(new NowDateFunctionPrototype(), temp);
        registerInternal(new NowDateTimeFunctionPrototype(), temp);
        SYSTEM_FUNCTIONS = Collections.unmodifiableMap(temp);
    }

    private static void registerInternal(SystemFunctionPrototype systemFunctionPrototype, Map<String, SystemFunctionPrototype> map) {
        map.put(systemFunctionPrototype.name(), systemFunctionPrototype);
    }

    @Override
    public SystemFunctionPrototype getFunctionPrototype(String functionName, Object... parameters) {
        return SYSTEM_FUNCTIONS.get(functionName);
    }

    @Override
    protected Object doExecutor(SystemFunctionPrototype functionPrototype, Object... parameters) {
        return functionPrototype.call(parameters);
    }
}
