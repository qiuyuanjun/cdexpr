package com.qiuyj.cdexpr.parser;

import java.util.List;

/**
 * 词法解析器
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

    /**
     * 解析所有的token，并返回一个{@code List}集合
     * @return 解析之后的token集合
     */
    List<Token> nextAllTokens();
}
