package com.qiuyj.cdexpr.utils;

import com.qiuyj.cdexpr.parser.ParseError;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public abstract class ParserUtils {

    private ParserUtils() {
        // for private
    }

    public static boolean isIdentifierStart(char ch) {
        return ch == '$'
                || (ch >= 'a' && ch <= 'z')
                || (ch >= 'A' && ch <= 'Z')
                || ch == '_';
    }

    public static boolean isIdentifierPart(char ch) {
        return isIdentifierStart(ch)
                || Character.isDigit(ch)
                || ch == '{'
                || ch == '}';
    }

    public static boolean isWhitespace(char ch) {
        return Character.isWhitespace(ch);
    }

    public static void validNumericEndChar(char endChar,
                                           int pos,
                                           boolean isLong,
                                           boolean isDouble,
                                           boolean isFloat) {
        if (endChar == '.') {
            lexError("numeric literal ends with '.'", pos);
        }
        if (!Character.isDigit(endChar)) {
            if (isLong && (endChar != 'l' && endChar != 'L')) {
                lexError("long numeric literal not ends with 'L' or 'l'", pos);
            }
            if (isDouble && (endChar != 'd' && endChar != 'D')) {
                lexError("double numeric literal not ends with 'D' or 'd'", pos);
            }
            if (isFloat && (endChar != 'f' && endChar != 'F')) {
                lexError("float numeric literal not ends with 'F' or 'f'", pos);
            }
        }
    }

    public static void lexError(String message, int pos) {
        throw new ParseError("Lex error, " + message, pos);
    }

    public static void parseError(String message, int start, int end) {
        throw new ParseError("Parse error, " + message, start, end);
    }

    public static Number parseNumber(String s, boolean binary, boolean octal, boolean hex, boolean isLong, boolean isDouble, boolean isFloat) {
        if (binary || octal || hex) {
            return Integer.parseInt(s, binary ? 2 : (octal ? 8 : 16));
        }
        else if (isLong) {
            return Long.parseLong(s);
        }
        else if (isDouble) {
            return Double.parseDouble(s);
        }
        else if (isFloat) {
            return Float.parseFloat(s);
        }
        throw new IllegalStateException("parse numeric error");
    }

    /**
     * 将数字取反
     * @param value 要取反的数字
     * @return 取反后的数字
     */
    public static Number negate(Number value) {
        if (value instanceof Integer
                || value instanceof Short
                || value instanceof Byte) {
            return Math.negateExact((int) value);
        }
        else if (value instanceof Long longVal) {
            return Math.negateExact(longVal);
        }
        else if (value instanceof Float || value instanceof Double) {
            return -value.doubleValue();
        }
        else if (value instanceof BigDecimal bigDecimalVal) {
            return bigDecimalVal.negate();
        }
        else if (value instanceof BigInteger bigIntegerVal) {
            return bigIntegerVal.negate();
        }
        else if (value instanceof AtomicInteger atomicIntegerVal) {
            atomicIntegerVal.updateAndGet(Math::negateExact);
            return atomicIntegerVal;
        }
        else if (value instanceof AtomicLong atomicLongVal) {
            atomicLongVal.updateAndGet(Math::negateExact);
            return atomicLongVal;
        }
        throw new IllegalStateException("Operand does not support negate operation");
    }

    /**
     * 将两个数字类型的数据相加
     * @param value 数字1
     * @param toBeAdd 相加的数字2
     * @return 相加后的结果
     */
    public static Number numberAdd(Object value, Object toBeAdd) {
        if (!(value instanceof Number) || !(toBeAdd instanceof Number constantOperand)) {
            throw new RuntimeException("The operand of the operator must be of type number");
        }
        if (value instanceof Integer
                || value instanceof Short
                || value instanceof Byte) {
            return ((int) value) + constantOperand.intValue();
        }
        else if (value instanceof Long longVal) {
            return longVal + constantOperand.longValue();
        }
        else if (value instanceof Float || value instanceof Double) {
            return ((double) value) + constantOperand.doubleValue();
        }
        else if (value instanceof BigDecimal bigDecimalVal) {
            return bigDecimalVal.add(BigDecimal.valueOf(constantOperand.intValue()));
        }
        else if (value instanceof BigInteger bigIntegerVal) {
            return bigIntegerVal.add(BigInteger.valueOf(constantOperand.intValue()));
        }
        else if (value instanceof AtomicInteger atomicIntegerVal) {
            atomicIntegerVal.addAndGet(constantOperand.intValue());
            return atomicIntegerVal;
        }
        else if (value instanceof AtomicLong atomicLongVal) {
            atomicLongVal.addAndGet(constantOperand.longValue());
            return atomicLongVal;
        }
        else if (value instanceof DoubleAccumulator doubleAccumulatorVal) {
            doubleAccumulatorVal.accumulate(constantOperand.doubleValue());
            return doubleAccumulatorVal;
        }
        else if (value instanceof LongAccumulator longAccumulatorVal) {
            longAccumulatorVal.accumulate(constantOperand.longValue());
            return longAccumulatorVal;
        }
        else if (value instanceof DoubleAdder doubleAdderVal) {
            doubleAdderVal.add(constantOperand.doubleValue());
            return doubleAdderVal;
        }
        else if (value instanceof LongAdder longAdderVal) {
            longAdderVal.add(constantOperand.longValue());
            return longAdderVal;
        }
        throw new IllegalStateException("Never reach here");
    }
}
