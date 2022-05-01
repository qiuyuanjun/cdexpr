package com.qiuyj.cdexpr.ast;

/**
 * 三元表达式抽象语法树
 * condition ? trueExpression : falseExpression
 * @author qiuyj
 * @since 2022-05-02
 */
public interface TernaryExprASTree extends ExpressionASTree {

    ExpressionASTree getCondition();

    ExpressionASTree getTrueExpression();

    ExpressionASTree getFalseExpression();
}
