package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ast.IdentifierASTree;

/**
 * @author qiuyj
 * @since 2022-05-02
 */
public record CDEIdentifier(String name, boolean variable) implements IdentifierASTree {

    @Override
    public String getName() {
        return name;
    }
}
