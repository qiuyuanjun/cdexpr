package com.qiuyj.cdexpr.ast;

import com.qiuyj.cdexpr.ExpressionContext;

/**
 * 抽象语法树
 * @author qiuyj
 * @since 2022-05-01
 */
public interface ASTree {

    Object getValue(ExpressionContext context);
}
