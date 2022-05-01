package com.qiuyj.cdexpr.parser;

/**
 * 词法单元，通过{@code Lexer}的nextToken方法生成
 * @author qiuyj
 * @since 2022-04-17
 */
public class Token {

    public static final Token DUMMY = new Token(TokenKind.UNDEFINED, -1, -1);

    private final TokenKind kind;

    /**
     * token start position
     */
    private final int startPos;

    /**
     * token end position (include)
     */
    private final int endPos;

    private final String sourceString;

    public Token(TokenKind kind, int startPos, int endPos) {
        this(kind, startPos, endPos, null);
    }

    public Token(TokenKind kind, int startPos, int endPos, String sourceString) {
        this.kind = kind;
        this.startPos = startPos;
        this.endPos = endPos;
        this.sourceString = sourceString;
    }

    public TokenKind getKind() {
        return kind;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public String getSourceString() {
        return sourceString;
    }

    @Override
    public String toString() {
        return "Token{" +
                "kind=" + kind +
                ", @position(" + startPos +
                "," + endPos +
                ")}";
    }

    public static class StringToken extends Token {

        private final String stringVal;

        public StringToken(String stringVal, TokenKind kind, int startPos, int endPos) {
            this(stringVal, kind, startPos, endPos, stringVal);
        }

        public StringToken(String stringVal, TokenKind kind, int startPos, int endPos, String sourceString) {
            super(kind, startPos, endPos, sourceString);
            this.stringVal = stringVal;
        }

        public String getStringVal() {
            return stringVal;
        }
    }

    public static class NumericToken extends Token {

        private final Number numeric;

        public NumericToken(Number numeric, int startPos, int endPos, String sourceString) {
            super(TokenKind.NUMERIC_LITERAL, startPos, endPos, sourceString);
            this.numeric = numeric;
        }

        public Number getNumeric() {
            return numeric;
        }
    }

}
