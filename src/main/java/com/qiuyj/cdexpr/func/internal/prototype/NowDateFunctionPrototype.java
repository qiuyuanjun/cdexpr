package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.FunctionPrototype;
import com.qiuyj.cdexpr.utils.ReflectionUtils;

import java.time.LocalDate;

/**
 * {@code NowDate}函数用于获取当前系统日期，{@link LocalDate#now()}
 * @author qiuyj
 * @since 2022-04-20
 */
public class NowDateFunctionPrototype extends FunctionPrototype {

    private static final String NAME = "NowDate";

    public NowDateFunctionPrototype() {
        super(ReflectionUtils.getMethod(LocalDate.class, "now"));
    }

    @Override
    public String name() {
        return NAME;
    }
}
