package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.internal.SystemFunctionPrototype;

import java.util.Objects;

/**
 * {@code FirstNotNull}函数用于获取给定的参数列表中，第一个不为{@code null}的值，否则返回{@code null}
 * @author qiuyj
 * @since 2022-04-27
 */
public class FirstNotNullFunctionPrototype extends SystemFunctionPrototype {

    private static final String NAME = "FirstNotNull";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Object call(Object... args) {
        for (Object arg : args) {
            if (Objects.nonNull(arg)) {
                return arg;
            }
        }
        return null;
    }
}
