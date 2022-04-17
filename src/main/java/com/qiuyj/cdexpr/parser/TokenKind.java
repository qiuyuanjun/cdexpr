package com.qiuyj.cdexpr.parser;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public enum TokenKind {

    IDENTIFIER,         // 标识符
    STRING_LITERAL,     // 字符串字面量
    NUMERIC_LITERAL,    // 数字字面量
    EQ("=="),
    ASSIGN("="),
    QMARK("?"),
    COLON(":"),
    PLUS("+"),
    MINUS("-"),
    INC("++"),
    DEC("--"),

    UNDEFINED,
    ;

    private final String name;

    TokenKind() {
        this(null);
    }

    TokenKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
