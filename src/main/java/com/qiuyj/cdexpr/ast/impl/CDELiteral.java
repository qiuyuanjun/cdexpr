package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ast.LiteralASTree;

import java.math.BigDecimal;

/**
 * @author qiuyj
 * @since 2022-05-02
 */
public class CDELiteral implements LiteralASTree {

    private final Object literal;

    private boolean numeric;

    public CDELiteral(String literal) {
        this.literal = literal;
    }

    public CDELiteral(Number literal) {
        this.literal = literal;
        this.numeric = true;
    }

    @Override
    public Object getLiteral() {
        return literal;
    }

    public String getString() {
        return literal.toString();
    }

    public Number getNumeric() {
        return numeric ? (Number) literal : new BigDecimal(getString());
    }
}
