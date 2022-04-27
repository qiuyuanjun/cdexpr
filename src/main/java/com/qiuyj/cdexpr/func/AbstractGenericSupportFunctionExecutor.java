package com.qiuyj.cdexpr.func;

import com.qiuyj.cdexpr.utils.ClassUtils;

import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-04-23
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGenericSupportFunctionExecutor<T extends FunctionPrototype> implements FunctionExecutor {

    private final Class<T> actualType;

    protected AbstractGenericSupportFunctionExecutor() {
        actualType = (Class<T>) ClassUtils.getGenericActualType(getClass(), 0);
        internalCheck();
    }

    private void internalCheck() {
        Objects.requireNonNull(actualType);
    }

    @Override
    public Object execute(FunctionPrototype functionPrototype, Object... parameters) {
        Class<? extends FunctionPrototype> functionPrototypeType = functionPrototype.getClass();
        if (actualType.isAssignableFrom(functionPrototypeType)) {
            return doExecutor((T) functionPrototype, parameters);
        }
        throw new IllegalArgumentException("FunctionPrototype type mismatch, require: " + actualType + " but actual is: " + functionPrototypeType);
    }

    protected abstract Object doExecutor(T functionPrototype, Object... parameters);

}
