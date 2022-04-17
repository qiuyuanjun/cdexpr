package com.qiuyj.cdexpr.test;

import com.qiuyj.cdexpr.parser.CDEScanner;
import com.qiuyj.cdexpr.parser.Lexer;
import com.qiuyj.cdexpr.parser.Token;
import com.qiuyj.cdexpr.parser.TokenKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public class LexerTest {

    @Test
    public void test_CDEScanner() {
        String expr = "${CITY_CODE} == '110100' ? 'Y' : 'N'";
        Lexer lexer = new CDEScanner(expr);
        Token token;
        do {
            lexer.nextToken();
            token = lexer.token();
        }
        while (Objects.nonNull(token));
        token = lexer.prevToken();
        Assertions.assertEquals(TokenKind.STRING_LITERAL, token.getKind());
    }
}
