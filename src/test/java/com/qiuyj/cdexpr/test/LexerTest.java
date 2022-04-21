package com.qiuyj.cdexpr.test;

import com.qiuyj.cdexpr.func.FunctionExecutor;
import com.qiuyj.cdexpr.parser.CDEScanner;
import com.qiuyj.cdexpr.parser.Lexer;
import com.qiuyj.cdexpr.parser.Token;
import com.qiuyj.cdexpr.parser.TokenKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

/**
 * @author qiuyj
 * @since 2022-04-17
 */
public class LexerTest {

    @Test
    public void test_CDEScanner() {
        String expr = "${CITY_CODE} == '110100' ? 0x7a19965f : 0.99821";
        Lexer lexer = new CDEScanner(expr);
        lexer.nextToken();
        List<Token> tokenStream = lexer.nextAllTokens();
        Assertions.assertEquals(7, tokenStream.size());
        Assertions.assertEquals(TokenKind.NUMERIC_LITERAL, tokenStream.get(tokenStream.size() - 1).getKind());

        expr = "5 - 7 + 2";
        lexer = new CDEScanner(expr);
        tokenStream = lexer.nextAllTokens();
        Assertions.assertEquals(5, tokenStream.size());

        expr = "${CITY_PREFIX} + json_get(${INPUT_CONTENT}, 'busiEntityCity')";
        lexer = new CDEScanner(expr);
        tokenStream = lexer.nextAllTokens();
        Assertions.assertEquals(8, tokenStream.size());

        System.out.println(FunctionExecutor.defaultExecutor().execute("DateFormat", new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}
