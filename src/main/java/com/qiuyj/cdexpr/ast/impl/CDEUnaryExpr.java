package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.UnaryExprASTree;

/**
 * 一元表达式的默认实现
 * @author qiuyj
 * @since 2022-05-02
 */
public record CDEUnaryExpr(ExpressionASTree expression) implements UnaryExprASTree {

    @Override
    public ExpressionASTree getExpression() {
        return expression;
    }
}
