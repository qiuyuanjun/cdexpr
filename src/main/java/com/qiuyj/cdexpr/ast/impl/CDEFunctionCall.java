package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.FunctionCallASTree;

import java.util.List;

/**
 * @author qiuyj
 * @since 2022-05-02
 */
public record CDEFunctionCall(ExpressionASTree functionName,
                              List<? extends ExpressionASTree> arguments) implements FunctionCallASTree {

    @Override
    public ExpressionASTree functionSelect() {
        return functionName;
    }

    @Override
    public List<? extends ExpressionASTree> getArguments() {
        return arguments;
    }
}
