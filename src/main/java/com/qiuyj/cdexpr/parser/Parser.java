package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.CDExpression;

/**
 * 语法解析器
 * @author qiuyj
 * @since 2022-04-17
 */
public interface Parser {

    /**
     * 将字符串的表达式解析成{@link CDExpression}对象
     * @return 解析后的{{@link CDExpression}对象
     */
    CDExpression parseExpression();
}
