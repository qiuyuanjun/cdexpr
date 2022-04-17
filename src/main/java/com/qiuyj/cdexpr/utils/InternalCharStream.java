package com.qiuyj.cdexpr.utils;

import java.io.InputStream;
import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public abstract class InternalCharStream extends InputStream {

    private static final boolean JAVA_VERSION_UP_8;
    static {
        String javaVersionStr = System.getProperty("java.version", "17");
        double javaVersion;
        try {
            javaVersion = Double.parseDouble(javaVersionStr);
        }
        catch (NumberFormatException e) {
            javaVersion = 17;
        }
        JAVA_VERSION_UP_8 = Double.compare(javaVersion, 8) > 0;
    }

    private final String source;

    private final int length;

    private final int maxPos;

    private int pos = -1;

    protected InternalCharStream(String source) {
        this.source = source;
        this.length = source.length();
        this.maxPos = length - 1;
    }

    @Override
    public int read() {
        return get();
    }

    /**
     * 获取下一个字符，并将{@code pos}加一
     * @return 下一个字符
     */
    public char get() {
        if (pos >= maxPos) {
            throw new StringIndexOutOfBoundsException("char index out of range: " + pos);
        }
        return fastGet();
    }

    /**
     * 获取下一个字符，并将{@code pos}加一，该方法不会校验边界信息
     * @return 下一个字符
     */
    public char fastGet() {
        return fastCharAt(++pos);
    }

    public char fastPrevChar() {
        return fastCharAt(pos - 1);
    }

    public abstract char fastCharAt(int index);

    public String fastSubstring(int start, int end) {
        return source.substring(start, end);
    }

    public int getLength() {
        return length;
    }

    /**
     * 得到当前的位置
     * @return 当前的位置
     */
    public int currentPosition() {
        return pos;
    }

    /**
     * 设置位置信息，不做相关边界校验
     * @param pos 要设置的位置信息
     */
    public void fastSetPosition(int pos) {
        this.pos = pos;
    }

    public boolean remaining() {
        return pos < maxPos;
    }

    public static InternalCharStream wrap(String s) {
        if (Objects.isNull(s) || s.isEmpty()) {
            return JAVA_VERSION_UP_8
                    ? InternalCharStreamVersionUp8.EMPTY
                    : InternalCharStreamVersion8.EMPTY;
        }
        return chooseBetterVersion(s);
    }

    private static InternalCharStream chooseBetterVersion(String s) {
        return JAVA_VERSION_UP_8
                ? new InternalCharStreamVersionUp8(s)
                : new InternalCharStreamVersion8(s);
    }
}
