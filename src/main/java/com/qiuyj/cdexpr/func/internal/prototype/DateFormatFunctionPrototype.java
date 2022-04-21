package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.FunctionPrototype;
import com.qiuyj.cdexpr.utils.FunctionUtils;
import com.qiuyj.cdexpr.utils.ReflectionUtils;

/**
 * 时间日期格式化函数原型，{@link FunctionUtils#dateFormat(Object, String)}
 * @author qiuyj
 * @since 2022-04-21
 */
public class DateFormatFunctionPrototype extends FunctionPrototype {

    private static final String NAME = "DateFormat";

    private static final Class<?>[] PARAMETER_TYPES = {
            Object.class,
            String.class
    };

    public DateFormatFunctionPrototype() {
        super(ReflectionUtils.getMethod(FunctionUtils.class, "dateFormat", PARAMETER_TYPES));
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Class<?>[] parameterTypes() {
        return PARAMETER_TYPES;
    }
}
