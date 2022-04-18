package com.qiuyj.cdexpr.utils;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
class InternalCharStreamVersionGT8 extends InternalCharStream {

    private final String sourceStr;

    InternalCharStreamVersionGT8(String sourceStr) {
        super(sourceStr);
        this.sourceStr = sourceStr;
    }

    @Override
    public char fastCharAt(int index) {
        return sourceStr.charAt(index);
    }
}
