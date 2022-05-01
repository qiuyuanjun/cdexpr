package com.qiuyj.cdexpr.ast;

/**
 * 数组访问的抽象语法树
 * expression [ index ]
 * @author qiuyj
 * @since 2022-05-02
 */
public interface ArrayAccessASTree extends ExpressionASTree {

    ExpressionASTree getExpression();

    ExpressionASTree getIndex();
}
