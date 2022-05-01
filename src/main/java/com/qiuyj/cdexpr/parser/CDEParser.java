package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.CDExpression;
import com.qiuyj.cdexpr.ast.ASTree;
import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.utils.ParserUtils;

import java.util.function.Consumer;

/**
 * 默认的语法解析器，会根据传入的词法解析器，解析词法，生成对应的抽象语法树
 * @author qiuyj
 * @since 2022-04-19
 */
public record CDEParser(Lexer lexer) implements Parser {

    @Override
    public CDExpression parseExpression() {
        CDETokenVisitor tokenVisitor = new CDETokenVisitor();
        lexer.consumeTokens(tokenVisitor);
        return tokenVisitor.generateExpression();
    }

    private class CDETokenVisitor implements Consumer<Token> {

        private ASTree ast;

        @Override
        public void accept(Token token) {
            // 判断当前的token是否是 '{'，如果是的话，那么表示是block，否则是expression
            if (token.getKind() != TokenKind.LBRACE) {
                parseExpression();
            }
            else {
                parseBlock();
            }
        }

        /**
         * 解析表达式
         * expression -> unary_expr | binary_expr | ternary_expr
         */
        private void parseExpression() {
            if (!parseUnaryExpr() || !parseBinaryExpr() || !parseTernaryExpr()) {
                parseError("Can not parse expression");
            }
        }

        private boolean parseTernaryExpr() {
            return false;
        }

        private boolean parseBinaryExpr() {
            return false;
        }

        private boolean parseUnaryExpr() {
            return false;
        }

        private void parseBlock() {
            if (ast instanceof ExpressionASTree) {
                // 如果是解析block，那么要求ast一定是null或者是StatementTree
                parseError("CDExpr must be expression or block");
            }
        }

        CDExpression generateExpression() {
            return null;
        }

        /**
         * 获取下一个{@code Token}
         * @return 下一个token，可能为null
         */
        private Token nextToken() {
            Lexer lexer = CDEParser.this.lexer;
            lexer.nextToken();
            return lexer.token();
        }

        private Token token() {
            return CDEParser.this.lexer.token();
        }

        private void parseError(String msg) {
            Token token = token();
            ParserUtils.parseError(msg, token.getStartPos(), token.getEndPos());
        }
    }
}
