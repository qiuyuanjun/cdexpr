package com.qiuyj.cdexpr;

import com.qiuyj.cdexpr.parser.CDEParser;
import com.qiuyj.cdexpr.parser.CDEScanner;

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
    Object getValue(ExpressionContext context);

    /**
     * 解析传入的表达式字符串，生成对应的{@code CDExpression}对象
     * @param expressionString 表达式字符串
     * @return {@link CDExpression}对象
     */
    static CDExpression newInstance(String expressionString) {
        return new CDEParser(new CDEScanner(expressionString)).parseExpression();
    }
}
