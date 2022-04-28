package com.qiuyj.cdexpr.func.internal.prototype;

import com.qiuyj.cdexpr.func.internal.SystemFunctionPrototype;

import java.util.Objects;

/**
 * {@code FirstNotEmptyString}函数用于获取给定的参数列表中，第一个不是空字符串（包含{@code null}和""）的值，否则返回{@code null}
 * @author qiuyj
 * @since 2022-04-27
 */
public class FirstNotEmptyStringFunctionPrototype extends SystemFunctionPrototype {

    private static final String NAME = "FirstNotEmptyString";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Object call(Object... args) {
        for (Object arg : args) {
            if (arg instanceof String s && !s.isEmpty()) {
                return arg;
            }
        }
        return null;
    }
}
