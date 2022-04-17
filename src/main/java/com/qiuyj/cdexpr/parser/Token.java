package com.qiuyj.cdexpr.parser;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public class Token {

    public static final Token DUMMY = new Token(null, -1, -1);

    private final TokenKind kind;

    /**
     * token start position
     */
    private final int startPos;

    /**
     * token end position (include)
     */
    private final int endPos;

    public Token(TokenKind kind, int startPos, int endPos) {
        this.kind = kind;
        this.startPos = startPos;
        this.endPos = endPos;
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
            super(kind, startPos, endPos);
            this.stringVal = stringVal;
        }

        public String getStringVal() {
            return stringVal;
        }
    }

    public static class NumericToken extends Token {

        private final Number number;

        public NumericToken(Number number, int startPos, int endPos) {
            super(TokenKind.NUMERIC_LITERAL, startPos, endPos);
            this.number = number;
        }

        public Number getNumber() {
            return number;
        }
    }
}
