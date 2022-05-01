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
        Token token;
        if (ParserUtils.isIdentifierStart(c)) {
            token = callFunctionAndFallbackOneChar(startPos, this::tryLexIdentifier);
        }
        else {
            token = switch (c) {
                case Character.MIN_VALUE -> null;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' ->
                        callFunctionAndFallbackOneChar(startPos, this::tryLexNumericLiteral);
                case ',' -> new Token(TokenKind.COMMA, startPos, startPos);
                case ':' -> new Token(TokenKind.COLON, startPos, startPos);
                case '?' -> new Token(TokenKind.QMARK, startPos, startPos);
                case '(' -> new Token(TokenKind.LPAREN, startPos, startPos);
                case ')' -> new Token(TokenKind.RPAREN, startPos, startPos);
                case '\'' -> tryLexStringLiteral(startPos);
                case '=' -> new Token(nextCharIs('=') ? TokenKind.EQ : TokenKind.ASSIGN, startPos, pos());
                case '+' -> new Token(nextCharIs('+') ? TokenKind.INC : TokenKind.PLUS, startPos, pos());
                case '-' -> new Token(nextCharIs('-') ? TokenKind.DEC : TokenKind.MINUS, startPos, pos());
                case '!' -> new Token(nextCharIs('=') ? TokenKind.NEQ : TokenKind.NOT, startPos, pos());
                default -> {
                    lexError("unexpect char '" + c + "'", startPos);
                    yield null;
                }
            };
        }
        return token;
    }

    private Token tryLexNumericLiteral(int startPos) {
        char c = getChar(),
             prevChar = source.fastPrevChar();
        boolean binary = false,
                hex = false,
                octal = false,
                isLong = false,
                isFloat = false,
                isDouble = false,
                maybeDouble = false;
        if (prevChar == '0') {
            binary = c == 'b' || c == 'B';
            hex = c == 'x' || c == 'X';
            octal = Character.isDigit(c);
            // 如果是2进制或者16进制，那么对应的b和x已经读取并解析，因此需要继续读取下一个字符供后续使用
            if (hex || binary) {
                c = getChar();
            }
        }
        for (;; c = getChar()) {
            switch (c) {
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    continue;
                }
                case 'L', 'l' -> {
                    isLong = true;
                    continue;
                }
                case 'D', 'd' -> {
                    isDouble = !hex;
                    maybeDouble = true;
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
                case '.' -> {
                    if (Character.isDigit(source.fastPrevChar())) {
                        isDouble = true;
                        continue;
                    }
                }
            }
            break;
        }
        ParserUtils.validNumericEndChar(source.fastPrevChar(), pos(), isLong, isDouble, isFloat);
        String sourceString = source.fastSubstring(startPos, pos());
        return binary || octal || hex || isLong || isDouble || isFloat
                ? new Token.NumericToken(ParserUtils.parseNumber(source.fastSubstring(hex || binary ? startPos + 2 : startPos, isLong || isFloat || (maybeDouble && isDouble) ? pos() - 1 : pos()), binary, octal, hex, isLong, isDouble, isFloat), startPos, pos() - 1, sourceString)
                : new Token.NumericToken(Integer.parseInt(sourceString), startPos, pos() - 1, sourceString);
    }

    private Token tryLexStringLiteral(int startPos) {
        for (char c = getChar();; c = getChar()) {
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
        final String sourceString = identifier;
        if (maybeVariable) {
            if (source.fastCharAt(endPos) != '}') {
                lexError("expect char '}' at end of typeof variable identifier", endPos);
            }
            identifier = identifier.substring(2, identifier.length() - 1);
            startPos += 2; // remove '${'
            endPos--; // remove '}'
        }
        // todo 判断是否是关键字
        return new Token.StringToken(identifier, TokenKind.IDENTIFIER, startPos, endPos, sourceString);
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
     * @param funcArgs 要执行的函数的参数
     * @param func 要执行的函数
     * @return 函数执行的结果
     * @param <R> 函数返回值类型
     * @param <T> 要执行的函数参数类型
     */
    private <R, T> R callFunctionAndFallbackOneChar(T funcArgs, Function<T, R> func) {
        final R ret = func.apply(funcArgs);
        source.fastSetPosition(pos() - 1);
        return ret;
    }

}
