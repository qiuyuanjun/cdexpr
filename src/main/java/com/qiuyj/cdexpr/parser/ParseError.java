package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.ExpressionException;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public class ParseError extends ExpressionException {

    public ParseError(String message, int pos) {
        super(message + " @pos[" + pos + "]");
    }
}
