package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.internal.SystemFunctionPrototype;
import com.qiuyj.cdexpr.utils.FunctionUtils;

/**
 * {@code DateFormat}函数用于时间日期格式化函数，{@link FunctionUtils#dateFormat(Object, String)}
 * @author qiuyj
 * @since 2022-04-21
 */
public class DateFormatFunctionPrototype extends SystemFunctionPrototype {

    private static final String NAME = "DateFormat";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Object call(Object... args) {
        return FunctionUtils.dateFormat(checkTypeAndGetFromInvocation(0, Object.class, args),
                checkTypeAndGetFromInvocation(1, String.class, args));
    }
}
