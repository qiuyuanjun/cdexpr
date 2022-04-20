package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.FunctionPrototype;
import com.qiuyj.cdexpr.utils.ReflectionUtils;

import java.time.LocalDateTime;

/**
 * {@code NowDateTime}函数用于获取当前系统日期时间，{@link LocalDateTime#now()}
 * @author qiuyj
 * @since 2022-04-20
 */
public class NowDateTimeFunctionPrototype extends FunctionPrototype {

    private static final String NAME = "NowDateTime";

    public NowDateTimeFunctionPrototype() {
        super(ReflectionUtils.getMethod(LocalDateTime.class, "now"));
    }

    @Override
    public String name() {
        return NAME;
    }
}
