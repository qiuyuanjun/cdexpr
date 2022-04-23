package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.internal.SystemFunctionPrototype;

import java.time.LocalDate;

/**
 * {@code NowDate}函数用于获取当前系统日期，{@link LocalDate#now()}
 * @author qiuyj
 * @since 2022-04-20
 */
public class NowDateFunctionPrototype extends SystemFunctionPrototype {

    private static final String NAME = "NowDate";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Object call(Object... args) {
        return LocalDate.now();
    }
}
