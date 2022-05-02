package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.CDExpression;
import com.qiuyj.cdexpr.ast.ASTree;
import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.impl.CDEFunctionCall;
import com.qiuyj.cdexpr.ast.impl.CDEIdentifier;
import com.qiuyj.cdexpr.ast.impl.CDEUnaryExpr;
import com.qiuyj.cdexpr.utils.ParserUtils;

import java.util.ArrayList;
import java.util.List;
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
            if (!maybeUnaryExpr() || !parseBinaryExpr() || !parseTernaryExpr()) {
                parseError("Can not parse expression");
            }
        }

        private boolean parseTernaryExpr() {
            return false;
        }

        private boolean parseBinaryExpr() {
            return false;
        }

        /**
         * 解析一元表达式
         * @return 如果可以解析，那么返回true，否则返回false
         */
        private boolean maybeUnaryExpr() {
            if (maybePostfixExpr() || maybePrefixExpr()) {
                if (ast instanceof ExpressionASTree expr) {
                    ast = new CDEUnaryExpr(expr);
                }
                else {
                    parseError("Unary expression mismatch");
                }
                return true;
            }
            return false;
        }

        private boolean maybePrefixExpr() {
            return false;
        }

        private boolean maybePostfixExpr() {
            Token token = token();
            if (token.getKind() != TokenKind.IDENTIFIER) {
                return false;
            }
            String name = ((Token.StringToken) token).getStringVal();
            String sourceString = token.getSourceString();
            token = nextToken();
            if (name.equals(sourceString) && token.getKind() == TokenKind.LPAREN) {
                // 函数，继续解析参数列表
                List<? extends ExpressionASTree> arguments = parseAndGetArgumentList();
                ast = new CDEFunctionCall(new CDEIdentifier(name, false), arguments);
            }
            else if (name.equals(sourceString)) {
                parseError("Variable name must be between '${' and '}'");
            }
            else {
                TokenKind kind = token.getKind();
                if (kind == TokenKind.INC || kind == TokenKind.DEC) {

                }
            }
            return true;
        }

        private List<? extends ExpressionASTree> parseAndGetArgumentList() {
            ASTree originAst = ast;
            List<ExpressionASTree> arguments = new ArrayList<>();
            Token token;
            do {
                this.parseExpression();
                arguments.add((ExpressionASTree) ast);
                token = nextToken();
            }
            while (token.getKind() == TokenKind.COMMA);
            if (token().getKind() != TokenKind.RPAREN) {
                parseError("Function call must ends with ')");
            }
            ast = originAst;
            return arguments;
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
