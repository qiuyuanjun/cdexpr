package com.qiuyj.cdexpr.parser;

import java.io.InputStream;
import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
class InternalCharStream extends InputStream {

    private static final InternalCharStream EMPTY = new InternalCharStream("");

    private final String sourceStr;

    private final int length;

    private final int maxPos;

    private int pos = -1;

    private InternalCharStream(String sourceStr) {
        this.sourceStr = sourceStr;
        length = sourceStr.length();
        maxPos = length - 1;
    }

    @Override
    public int read() {
        return get();
    }

    public int getLength() {
        return length;
    }

    public char get() {
        if (pos >= maxPos) {
            throw new StringIndexOutOfBoundsException("char index out of range: " + pos);
        }
        return fastGet();
    }

    public char fastGet() {
        return sourceStr.charAt(++pos);
    }

    public char fastPrevChar() {
        return sourceStr.charAt(pos - 1);
    }

    public char fastCharAt(int index) {
        return sourceStr.charAt(index);
    }

    public String fastSubstring(int start, int end) {
        return sourceStr.substring(start, end);
    }

    /**
     * 得到当前的位置
     * @return 当前的位置
     */
    public int currentPosition() {
        return pos;
    }

    public void fastSetPosition(int pos) {
        this.pos = pos;
    }

    public boolean remaining() {
        return pos < maxPos;
    }

    public static InternalCharStream wrap(String s) {
        return Objects.isNull(s) || s.isEmpty()
                ? EMPTY
                : new InternalCharStream(s);
    }
}
