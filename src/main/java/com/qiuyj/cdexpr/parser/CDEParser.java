package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.CDExpression;

/**
 * 默认的语法解析器，会根据传入的词法解析器，解析词法，生成对应的抽象语法树
 * @author qiuyj
 * @since 2022-04-19
 */
public class CDEParser implements Parser {

    private final Lexer lexer;

    public CDEParser(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public CDExpression parseExpression() {
        lexer.nextToken();
        return null;
    }
}
