package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ExpressionContext;
import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.FunctionCallASTree;
import com.qiuyj.cdexpr.func.FunctionExecutorChain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiuyj
 * @since 2022-05-02
 */
public record CDEFunctionCall(ExpressionASTree functionName,
                              List<? extends ExpressionASTree> arguments) implements FunctionCallASTree {

    private static final Object[] NO_ARGS = new Object[0];

    @Override
    public ExpressionASTree functionSelect() {
        return functionName;
    }

    @Override
    public List<? extends ExpressionASTree> getArguments() {
        return arguments;
    }

    @Override
    public Object getValue(ExpressionContext context) {
        Object[] functionArgs;
        if (arguments.isEmpty()) {
            functionArgs = NO_ARGS;
        }
        else {
            List<Object> args = new ArrayList<>(arguments.size());
            for (ExpressionASTree expr : arguments) {
                args.add(expr.getValue(context));
            }
            functionArgs = args.toArray(NO_ARGS);
        }
        return FunctionExecutorChain.callFunction((String) functionName.getValue(context), functionArgs);
    }
}
