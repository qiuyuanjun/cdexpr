package com.qiuyj.cdexpr.utils;

import com.qiuyj.cdexpr.parser.ParseError;
import com.qiuyj.cdexpr.parser.TokenKind;

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
     * 判断当前的token是否是二元表达式支持的操作符
     * @param kind token类型
     * @return 如果是，那么返回{@code true}，否则返回{@code false}
     */
    public static boolean isBinaryOperator(TokenKind kind) {
        return switch (kind) {
            case EQ, NEQ, GT, LT, GTEQ, LTEQ, ASSIGN, PLUS, MINUS, AMP, AMPAMP, BAR, BARBAR -> true;
            default -> false;
        };
    }
}
