package com.qiuyj.cdexpr.utils;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
class InternalCharStreamVersionUp8 extends InternalCharStream {

    static final InternalCharStreamVersionUp8 EMPTY = new InternalCharStreamVersionUp8("");

    private final String sourceStr;

    InternalCharStreamVersionUp8(String sourceStr) {
        super(sourceStr);
        this.sourceStr = sourceStr;
    }

    @Override
    public char fastCharAt(int index) {
        return sourceStr.charAt(index);
    }
}
