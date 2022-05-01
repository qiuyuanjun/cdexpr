package com.qiuyj.cdexpr.ast;

/**
 * 二元表达式抽象语法树
 * leftOperand operator rightOperand
 * @author qiuyj
 * @since 2022-05-01
 */
public interface BinaryExprASTree extends ExpressionASTree {

    ExpressionASTree getLeftOperand();

    ExpressionASTree getRightOperand();
}
