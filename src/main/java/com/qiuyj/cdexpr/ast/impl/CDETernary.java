package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ExpressionContext;
import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.TernaryExprASTree;

/**
 * @author qiuyj
 * @since 2022-05-04
 */
public record CDETernary(ExpressionASTree condition,
                         ExpressionASTree trueExpression,
                         ExpressionASTree falseExpression) implements TernaryExprASTree {

    @Override
    public ExpressionASTree getCondition() {
        return condition;
    }

    @Override
    public ExpressionASTree getTrueExpression() {
        return trueExpression;
    }

    @Override
    public ExpressionASTree getFalseExpression() {
        return falseExpression;
    }

    @Override
    public Object getValue(ExpressionContext context) {
        Object conditionValue = condition.getValue(context);
        if (!(conditionValue instanceof Boolean)) {
            throw new RuntimeException("The condition of a ternary expression must be Boolean");
        }
        boolean b = (boolean) conditionValue;
        return b ? trueExpression.getValue(context) : falseExpression.getValue(context);
    }
}
