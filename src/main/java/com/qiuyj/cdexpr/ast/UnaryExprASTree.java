package com.qiuyj.cdexpr.ast;

/**
 * 一元表达式抽象语法树
 * operator identifier
 * identifier operator
 * @author qiuyj
 * @since 2022-05-02
 */
public interface UnaryExprASTree extends ExpressionASTree {

    ExpressionASTree getExpression();
}
