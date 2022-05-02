package com.qiuyj.cdexpr.ast;

/**
 * 字面量抽象语法树
 * @author qiuyj
 * @since 2022-05-02
 */
public interface LiteralASTree extends ExpressionASTree {

    Object getLiteral();
}
