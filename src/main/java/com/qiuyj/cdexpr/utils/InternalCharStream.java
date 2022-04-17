package com.qiuyj.cdexpr.utils;

import java.io.InputStream;
import java.util.Objects;

/**
 * 内部词法分析使用的字符流
 * @author qiuyj
 * @since 2022-04-17
 */
public abstract class InternalCharStream extends InputStream {

    static final boolean JAVA_VERSION_UP_8;
    static {
        boolean javaVersion8 = true;
        if (!System.getProperty("java.version", "1.8").startsWith("1.8")) {
            try {
                // java.lang.Runtime.version() is a static method available on Java 9+
                // that returns an instance of java.lang.Runtime.Version which has the
                // following method: public int major()
                Object version = Runtime.class.getMethod("version").invoke(null);
                if ((int) version.getClass().getMethod("major").invoke(version) > 8) {
                    javaVersion8 = false;
                }
            }
            catch (Exception e) {
                // ignore
            }
        }
        JAVA_VERSION_UP_8 = !javaVersion8;
    }

    private static volatile InternalCharStream theEmptyInstance;

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

    /**
     * 得到当前解析位置的前一个字符，该方法不会校验边界信息
     * @return 前一个字符
     */
    public char fastPrevChar() {
        return fastCharAt(pos - 1);
    }

    /**
     * 根据给定的下标，获取对应下标的字符，该方法不会校验边界信息
     * @param index 下标
     * @return 下标对应的字符
     */
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
     * 设置位置信息，该方法不会校验边界信息
     * @param pos 要设置的位置信息
     */
    public void fastSetPosition(int pos) {
        this.pos = pos;
    }

    public boolean remaining() {
        return pos < maxPos;
    }

    public static InternalCharStream wrap(String s) {
        return Objects.isNull(s) || s.isEmpty()
                ? defaultEmptyInstance()
                : chooseBetterVersion(s);
    }

    private static InternalCharStream chooseBetterVersion(String s) {
        return JAVA_VERSION_UP_8
                ? new InternalCharStreamVersionUp8(s)
                : new InternalCharStreamVersion8(s);
    }

    private static InternalCharStream defaultEmptyInstance() {
        if (Objects.isNull(theEmptyInstance)) {
            synchronized (InternalCharStream.class) {
                if (Objects.isNull(theEmptyInstance)) {
                    theEmptyInstance = chooseBetterVersion("");
                }
            }
        }
        return theEmptyInstance;
    }
}
