package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.utils.InternalCharStream;
import com.qiuyj.cdexpr.utils.ParserUtils;

import java.util.function.Function;

import static com.qiuyj.cdexpr.utils.ParserUtils.lexError;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public record CDETokenizer(InternalCharStream source) {

    /**
     * 解析并处理一个{@code token}，忽略空白字符
     * @return 解析得到的{{@code Token}对象
     */
    public Token processOne() {
        char c = skipWhitespaceUntil();
        int startPos = pos();
        Token token = null;
        if (ParserUtils.isIdentifierStart(c)) {
            token = callFunctionAndFallbackOneChar(startPos, this::tryLexIdentifier);
        }
        else {
            switch (c) {
                case Character.MIN_VALUE:
                    return null;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                    token = callFunctionAndFallbackOneChar(startPos, this::tryLexNumericLiteral);
                    break;
                case ':':
                    token = new Token(TokenKind.COLON, startPos, pos());
                    break;
                case '?':
                    token = new Token(TokenKind.QMARK, startPos, pos());
                    break;
                case '\'':
                    token = tryLexStringLiteral(startPos);
                    break;
                case '=':
                    token = new Token(nextCharIs('=') ? TokenKind.EQ : TokenKind.ASSIGN, startPos, pos());
                    break;
                case '+':
                    token = new Token(nextCharIs('+') ? TokenKind.INC : TokenKind.PLUS, startPos, pos());
                    break;
                case '-':
                    token = new Token(nextCharIs('-') ? TokenKind.DEC : TokenKind.MINUS, startPos, pos());
                    break;
                default:
                    lexError("unexpect char '" + c + "'", startPos);
            }
        }
        return token;
    }

    private Token tryLexNumericLiteral(int startPos) {
        char c = getChar();
        char prevChar = source.fastPrevChar();
        boolean binary = prevChar == '0' && (c == 'b' || c == 'B');
        boolean hex = prevChar == '0' && (c == 'x' || c == 'X');
        boolean octal = prevChar == '0' && Character.isDigit(c);
        if (hex || binary) {
            c = getChar();
        }
        boolean isLong = false;
        boolean isFloat = false;
        boolean isDouble = false;
        boolean doubleEndFlag = false;
        for (; ; c = getChar()) {
            if (Character.isDigit(c)) {
                continue;
            }
            if (c == '.' && Character.isDigit(source.fastPrevChar())) {
                isDouble = true;
                continue;
            }
            switch (c) {
                case 'L', 'l' -> {
                    isLong = true;
                    continue;
                }
                case 'D', 'd' -> {
                    isDouble = !hex;
                    doubleEndFlag = true;
                    continue;
                }
                case 'F', 'f' -> {
                    isFloat = !hex;
                    continue;
                }
                case 'A', 'a', 'B', 'b', 'C', 'c', 'E', 'e' -> {
                    if (!hex) {
                        lexError("not an hex string literal but has hex character '" + c + "'", pos());
                    }
                    continue;
                }
            }
            break;
        }
        ParserUtils.validNumericEndChar(source.fastPrevChar(), pos(), isLong, isDouble, isFloat);
        if (binary || octal || hex || isLong || isDouble || isFloat) {
            int start = startPos;
            int end = pos();
            if (hex || binary) {
                start += 2;
            }
            if (isLong || isFloat || (isDouble && doubleEndFlag)) {
                end--;
            }
            return new Token.NumericToken(ParserUtils.parseNumber(source.fastSubstring(start, end), binary, octal, hex, isLong, isDouble, isFloat), startPos, pos() - 1);
        }
        return new Token.NumericToken(Integer.parseInt(source.fastSubstring(startPos, pos())), startPos, pos() - 1);
    }

    private Token tryLexStringLiteral(int startPos) {
        for (char c = getChar(); ; c = getChar()) {
            if (c == '\'') {
                // 判断上一个字符是否是转义符
                char prev = source.fastPrevChar();
                if (prev != '\\') {
                    break;
                }
            }
            else if (c == Character.MIN_VALUE) {
                // 没有结束符
                lexError("no terminator ' in string literal", pos());
            }
        }
        String literal = source.fastSubstring(startPos + 1, pos()).translateEscapes();
        return new Token.StringToken(literal, TokenKind.STRING_LITERAL, startPos + 1, pos() - 1);
    }

    /**
     * 读取并判断下一个字符是否是给定的字符，如果不相等，那么将位置重置为读取前的位置
     * @param c 要判断的字符
     * @return 如果相等，那么返回{@code true}，否则返回{@code false}
     */
    private boolean nextCharIs(char c) {
        int currentPos = pos();
        boolean nextCharIs = getChar() == c;
        if (!nextCharIs) {
            source.fastSetPosition(currentPos);
        }
        return nextCharIs;
    }

    private Token tryLexIdentifier(int startPos) {
        char c;
        boolean maybeVariable;
        for (maybeVariable = (c = getChar()) == '{' && source.fastPrevChar() == '$';
             ParserUtils.isIdentifierPart(c);
             c = getChar()) {
            if (!maybeVariable && (c == '}' || c == '{')) {
                lexError("can not recognize char '" + c + "' as identifier", pos());
            }
        }
        int endPos = pos() - 1; // identifier end character include
        String identifier = source.fastSubstring(startPos, pos());
        if (maybeVariable) {
            if (source.fastCharAt(endPos) != '}') {
                lexError("expect char '}' at end of typeof variable identifier", endPos);
            }
            identifier = identifier.substring(2, identifier.length() - 1);
            startPos += 2; // remove '${'
            endPos--; // remove '}'
        }
        // todo 判断是否是关键字
        return new Token.StringToken(identifier, TokenKind.IDENTIFIER, startPos, endPos);
    }

    /**
     * 跳过空白字符，并返回第一个非空白字符
     * @return 第一个非空白字符
     */
    private char skipWhitespaceUntil() {
        char c;
        do {
            c = getChar();
        }
        while (ParserUtils.isWhitespace(c));
        return c;
    }

    /**
     * 得到下一个字符，如果已经是末尾了，那么返回{@code \u0000}
     * @return 下一个字符
     */
    private char getChar() {
        if (source.remaining()) {
            return source.fastGet();
        }
        if (pos() < source.getLength()) {
            source.fastSetPosition(pos() + 1);
        }
        return Character.MIN_VALUE;
    }

    /**
     * 得到当前解析的位置
     * @return 当前解析的位置
     */
    private int pos() {
        return source.currentPosition();
    }

    /**
     * 执行对应的函数，然后将字符流的位置回退一个字符
     * @param startPos 开始位置
     * @param func 要执行的函数
     * @return 函数执行的结果
     * @param <R> 函数返回值泛型对象
     */
    private <R> R callFunctionAndFallbackOneChar(int startPos, Function<Integer, R> func) {
        final R ret = func.apply(startPos);
        source.fastSetPosition(pos() - 1);
        return ret;
    }

}
