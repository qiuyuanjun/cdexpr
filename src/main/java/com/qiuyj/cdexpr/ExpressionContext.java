package com.qiuyj.cdexpr;

/**
 * @author qiuyj
 * @since 2022-05-04
 */
public interface ExpressionContext {

    /**
     * 得到对应key的值
     * @param key key
     * @return 对应的值
     */
    Object getValue(String key);
}
