package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.CDExpression;
import com.qiuyj.cdexpr.ast.ASTree;
import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.OperatorExprASTree;
import com.qiuyj.cdexpr.ast.impl.CDEBinary;
import com.qiuyj.cdexpr.ast.impl.CDEFunctionCall;
import com.qiuyj.cdexpr.ast.impl.CDEIdentifier;
import com.qiuyj.cdexpr.ast.impl.CDELiteral;
import com.qiuyj.cdexpr.ast.impl.CDETernary;
import com.qiuyj.cdexpr.ast.impl.CDEUnary;
import com.qiuyj.cdexpr.utils.ParserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
            if (!maybeExpression()) {
                parseError("Can not parse expression");
            }
        }

        /**
         * 判断并解析表达式
         * @return 如果是，那么返回{@code true}，否则返回{@code false}
         */
        private boolean maybeExpression() {
            return maybeUnaryExpr() || maybeBinaryExpr() || maybeTernaryExpr();
        }

        /**
         * 判断并解析三元表达式
         * condition ? trueExpression : falseExpression
         * @return 如果是，那么返回{@code true}，否则返回{@code false}
         */
        private boolean maybeTernaryExpr() {
            boolean hasCondition;
            if ((hasCondition = Objects.nonNull(ast)) && !(ast instanceof ExpressionASTree)) {
                parseError("If the condition value of ternary expression is not null, it must be ExpressionASTree");
            }
            if (token().getKind() == TokenKind.QMARK) {
                if (!hasCondition) {
                    parseError("Ternary expression must has condition");
                }
                final ExpressionASTree condition = (ExpressionASTree) ast;
                // 解析trueExpression
                final ExpressionASTree trueExpression = parseNextExpression();
                if (nextToken().getKind() != TokenKind.COLON) {
                    parseError("Ternary expression error, must has ':' part");
                }
                final ExpressionASTree falseExpression = parseNextExpression();
                ast = new CDETernary(condition, trueExpression, falseExpression);
                return true;
            }
            return false;
        }

        /**
         * 解析并返回下一个expression
         * @return 解析成功的expression对象
         */
        private ExpressionASTree parseNextExpression() {
            nextToken();
            parseExpression();
            return (ExpressionASTree) ast;
        }

        /**
         * 判断并解析二元表达式
         * expression binary_operator expression
         * @return 如果是，那么返回{@code true}，否则返回{@code false}
         */
        private boolean maybeBinaryExpr() {
            boolean hasLeftOperand;
            if ((hasLeftOperand = Objects.nonNull(ast)) && !(ast instanceof ExpressionASTree)) {
                parseError("If the left value of binary expression is not null, it must be ExpressionASTree");
            }
            // 判断是否是二元表达式的操作符
            TokenKind kind;
            if (ParserUtils.isBinaryOperator((kind = token().getKind()))) {
                if (!hasLeftOperand) {
                    parseError("Binary expression must has left operand");
                }
                final ExpressionASTree left = (ExpressionASTree) ast;
                final OperatorExprASTree.OperatorType operatorType
                        = OperatorExprASTree.OperatorType.fromTokenKind(false, kind);
                // 继续解析操作符后面的表达式
                final ExpressionASTree right = parseNextExpression();
                ast = new CDEBinary(left, right, operatorType);
                return true;
            }
            return false;
        }

        /**
         * 判断并解析一元表达式
         * @return 如果可以解析，那么返回true，否则返回false
         */
        private boolean maybeUnaryExpr() {
            if (maybePostfixExpr()
                    || maybeLiteral()
                    || maybePrefixExpr()) {
                if (Objects.isNull(ast) || !(ast instanceof ExpressionASTree)) {
                    parseError("Unary expression mismatch");
                }
                if (!(ast instanceof CDEUnary)) {
                    ast = new CDEUnary((ExpressionASTree) ast, OperatorExprASTree.OperatorType.NONE);
                }
                return true;
            }
            return false;
        }

        /**
         * 判断并解析字面量
         * @return 如果是字面量，那么返回{@code true}，否则返回{@code false}
         */
        private boolean maybeLiteral() {
            Token token = token();
            TokenKind kind = token.getKind();
            boolean isNumeric;
            if ((isNumeric = kind == TokenKind.NUMERIC_LITERAL) || kind == TokenKind.STRING_LITERAL) {
                ast = isNumeric
                        ? new CDELiteral(((Token.NumericToken) token).getNumeric())
                        : new CDELiteral(((Token.StringToken) token).getStringVal());
                return true;
            }
            return false;
        }

        /**
         * 判断并解析前缀表达式
         * @return 如果是前缀表达式，那么返回{@code true}，否则返回{@code false}
         */
        private boolean maybePrefixExpr() {
            Token token = token();
            TokenKind kind = token.getKind();
            if (kind == TokenKind.INC || kind == TokenKind.DEC || kind == TokenKind.BANG) {
                token = nextToken();
                if (token.getKind() != TokenKind.IDENTIFIER) {
                    parseError("Prefix expression must ends with identifier");
                }
                ast = new CDEUnary(new CDEIdentifier(((Token.StringToken) token).getStringVal(), true),
                        OperatorExprASTree.OperatorType.fromTokenKind(true, kind));
                return true;
            }
            return false;
        }

        /**
         * 判断并解析后缀表达式
         * @return 如果是后缀表达式，那么返回{@code true}，否则返回{@code false}
         */
        private boolean maybePostfixExpr() {
            Token token = token();
            if (token.getKind() != TokenKind.IDENTIFIER) {
                return false;
            }
            String name = ((Token.StringToken) token).getStringVal();
            String sourceString = token.getSourceString();
            token = nextToken();
            TokenKind kind = token.getKind();
            if (name.equals(sourceString) && kind == TokenKind.LPAREN) {
                // 函数，继续解析参数列表
                final List<? extends ExpressionASTree> arguments = backupASTAndDoAction(this::parseAndGetArgumentList);
                ast = new CDEFunctionCall(new CDEIdentifier(name, false), arguments);
            }
            else if (!isSpecialToken(token) && name.equals(sourceString)) {
                parseError("Variable name must be between '${' and '}'");
            }
            else if (kind != TokenKind.INC && kind != TokenKind.DEC) {
                CDEParser.this.lexer.setPushedBack();
                ast = new CDEIdentifier(((Token.StringToken) token()).getStringVal(), true);
            }
            else {
                ast = new CDEUnary(new CDEIdentifier(name, true),
                        OperatorExprASTree.OperatorType.fromTokenKind(false, kind));
            }
            return true;
        }

        /**
         * 解析函数的参数列表并返回
         * @return 函数的参数列表
         */
        private List<? extends ExpressionASTree> parseAndGetArgumentList() {
            if (nextToken().getKind() == TokenKind.RPAREN) {
                // 该函数是空参数列表，直接返回
                return Collections.emptyList();
            }
            else {
                // 不是空参数，那么需要设置下词法解析器pushedBack状态
                // 因为已经调用了nextToken已经解析了参数的词法
                // parseExpression之前再次调用nextToken应该什么都不做
                CDEParser.this.lexer.setPushedBack();
                List<ExpressionASTree> arguments = new ArrayList<>();
                do {
                    nextToken();
                    parseExpression();
                    arguments.add((ExpressionASTree) ast);
                }
                while (nextToken().getKind() == TokenKind.COMMA);
                if (token().getKind() != TokenKind.RPAREN) {
                    parseError("Function call must ends with ')");
                }
                return arguments;
            }
        }

        private <T> T backupASTAndDoAction(Supplier<T> action) {
            final ASTree originAst = ast;
            T result = action.get();
            ast = originAst;
            return result;
        }

        private void parseBlock() {
            if (ast instanceof ExpressionASTree) {
                // 如果是解析block，那么要求ast一定是null或者是StatementTree
                parseError("CDExpr must be expression or block");
            }
        }

        /**
         * 根据解析生成的抽象语法树，生成对应的表达式对象
         * @return 表达式对象
         */
        CDExpression generateExpression() {
            return null;
        }

        /**
         * 获取下一个{@code Token}
         * @return 下一个token，如果下一个token是null，那么默认返回{@link Token#DUMMY}
         */
        private Token nextToken() {
            Lexer lexer = CDEParser.this.lexer;
            lexer.nextToken();
            Token token = lexer.token();
            return Objects.nonNull(token) ? token : Token.DUMMY;
        }

        /**
         * 获取当前解析的token
         * @return 当前解析的token，如果是null，那么默认返回{@link Token#DUMMY}
         */
        private Token token() {
            Token token = CDEParser.this.lexer.token();
            return Objects.nonNull(token) ? token : Token.DUMMY;
        }

        private void parseError(String msg) {
            Token token = token();
            ParserUtils.parseError(msg, token.getStartPos(), token.getEndPos());
        }

        /**
         * 判断是否是特殊的token。对于变量，文法规定需要使用在'${'和'}'之间
         * 但是对于只写了一个变量的表达式，例如"VARIABLE_1"，那么也是允许将"VARIABLE_1"单独解析成IDENTIFIER类型的token的
         * @param token 要判断的token
         * @return 如果是特殊的token，那么返回{@code true}，否则返回{@code false}
         */
        private boolean isSpecialToken(Token token) {
            return token == Token.DUMMY
                    && CDEParser.this.lexer.currentTokenCount() == 1;
        }
    }
}
