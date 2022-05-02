package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.OperatorExprASTree;
import com.qiuyj.cdexpr.ast.UnaryExprASTree;

/**
 * 一元表达式的默认实现
 * @author qiuyj
 * @since 2022-05-02
 */
public final class CDEUnary extends OperatorExprASTree implements UnaryExprASTree {

    private final ExpressionASTree expression;

    /**
     */
    public CDEUnary(ExpressionASTree expression, OperatorType type) {
        super(type);
        this.expression = expression;
    }

    @Override
    public ExpressionASTree getExpression() {
        return expression;
    }

    @Override
    public ExpressionASTree getOperand(OperandPosition position) {
        return expression;
    }
}
