package com.qiuyj.cdexpr;

import java.util.Map;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public interface CDExpression {

    /**
     * 根据传入的上下文得到当前表达式对应的计算结果
     * @param context 上下文
     * @return 对应的执行结果
     */
    Object getValue(Map<String, Object> context);
}
