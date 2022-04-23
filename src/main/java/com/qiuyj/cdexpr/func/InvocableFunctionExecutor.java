package com.qiuyj.cdexpr.func;

/**
 * @author qiuyj
 * @since 2022-04-21
 */
public class InvocableFunctionExecutor extends AbstractFunctionExecutor<InvocableFunctionPrototype> {


    @Override
    public InvocableFunctionPrototype getFunctionPrototype(String functionName, Object... parameters) {
        return null;
    }

    @Override
    protected Object doExecutor(InvocableFunctionPrototype functionPrototype, Object... parameters) {
        return functionPrototype.invoke(parameters);
    }
}
