package com.qiuyj.cdexpr.ast;

import java.util.List;

/**
 * 函数调用的抽象语法树
 * identifier ( arguments )
 * @author qiuyj
 * @since 2022-05-02
 */
public interface FunctionCallASTree extends ExpressionASTree {

    ExpressionASTree functionSelect();

    List<? extends ExpressionASTree> getArguments();
}
