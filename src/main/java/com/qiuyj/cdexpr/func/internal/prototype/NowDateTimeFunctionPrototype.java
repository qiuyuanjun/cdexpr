package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.internal.SystemFunctionPrototype;

import java.time.LocalDateTime;

/**
 * {@code NowDateTime}函数用于获取当前系统日期时间，{@link LocalDateTime#now()}
 * @author qiuyj
 * @since 2022-04-20
 */
public class NowDateTimeFunctionPrototype extends SystemFunctionPrototype {

    private static final String NAME = "NowDateTime";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Object call(Object... args) {
        return LocalDateTime.now();
    }
}
