package com.qiuyj.cdexpr.test;

import com.qiuyj.cdexpr.CDExpression;
import com.qiuyj.cdexpr.parser.CDEScanner;
import com.qiuyj.cdexpr.parser.Lexer;
import com.qiuyj.cdexpr.parser.Token;
import com.qiuyj.cdexpr.parser.TokenKind;
import com.qiuyj.cdexpr.utils.ClassUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Assertions.assertTrue(ClassUtils.typeValueMatch(int.class, 5));
        Assertions.assertTrue(ClassUtils.typeValueMatch(CharSequence.class, "abc"));

        int[] intArr = { 1, 2 };
        Assertions.assertTrue(ClassUtils.typeValueMatch(int[].class, intArr));

        expr = "function_test(${INPUT_CONTENT}, 'busiEntityCity', ++${CITY_CODE})";
        CDExpression.newInstance(expr);

        expr = "function_test2() || function_test3() || function_test4(${ARGUMENT1}, 'argument2')";
        CDExpression.newInstance(expr);

        expr = "function_test5() + function_test6() == 100 ? function_test7('test_string_literal', 123) : 'hello world'";
        CDExpression.newInstance(expr);

        expr = "USER_SOURCE";
        CDExpression.newInstance(expr);

        expr = "1 == 2 ? 10 : 5";
        System.out.println(CDExpression.newInstance(expr).getValue(null));

        expr = "6 + 9 - 100";
        System.out.println(CDExpression.newInstance(expr).getValue(null));

        expr = "DateFormat(NowDateTime(), 'yyyyMMddHHmmss')";
        System.out.println(CDExpression.newInstance(expr).getValue(null));
    }
}
