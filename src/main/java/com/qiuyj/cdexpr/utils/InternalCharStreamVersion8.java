package com.qiuyj.cdexpr.utils;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
class InternalCharStreamVersion8 extends InternalCharStream {

    static final InternalCharStreamVersion8 EMPTY = new InternalCharStreamVersion8("");

    private final char[] chars;

    InternalCharStreamVersion8(String sourceStr) {
        super(sourceStr);
        this.chars = sourceStr.toCharArray();
    }

    @Override
    public char fastCharAt(int index) {
        return chars[index];
    }

}
