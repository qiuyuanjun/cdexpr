package com.qiuyj.cdexpr.parser;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public interface Lexer {

    /**
     * 解析下一个token
     */
    void nextToken();

    /**
     * 获取当前解析的token
     * @return token
     */
    Token token();

    Token prevToken();
}
