package com.qiuyj.cdexpr.func;

/**
 * @author qiuyj
 * @since 2022-04-23
 */
@SuppressWarnings("unchecked")
public abstract class AbstractFunctionExecutor<T extends FunctionPrototype> implements FunctionExecutor<T> {

    @Override
    public Object execute(FunctionPrototype functionPrototype, Object... parameters) {
        return doExecutor((T) functionPrototype, parameters);
    }

    protected abstract Object doExecutor(T functionPrototype, Object... parameters);

}
