package com.qiuyj.cdexpr.ast;

/**
 * 标识符的抽象语法树
 * @author qiuyj
 * @since 2022-05-02
 */
public interface IdentifierASTree extends ExpressionASTree {

    String getName();
}
