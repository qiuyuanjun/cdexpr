package com.qiuyj.cdexpr.ast;

import com.qiuyj.cdexpr.ExpressionContext;
import com.qiuyj.cdexpr.parser.TokenKind;
import com.qiuyj.cdexpr.utils.ParserUtils;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-05-03
 */
public abstract class OperatorExprASTree implements ASTree {

    public enum OperandPosition {

        LEFT, RIGHT
    }

    public enum OperatorType {

        NONE,
        PREINC {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return numberOperation(operatorExprASTree, context, OperandPosition.RIGHT, 1);
            }
        },
        PREDEC {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return numberOperation(operatorExprASTree, context, OperandPosition.RIGHT, -1);
            }
        },
        POSTINC {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return numberOperation(operatorExprASTree, context, OperandPosition.LEFT, 1);
            }
        },
        POSTDEC {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return numberOperation(operatorExprASTree, context, OperandPosition.LEFT, -1);
            }
        },
        BANG {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                ExpressionASTree operand = operatorExprASTree.getOperand(OperandPosition.RIGHT);
                Object value = operand.getValue(context);
                if (!(value instanceof Boolean)) {
                    throw new RuntimeException("The operand of the operator '!' must be of type boolean");
                }
                return !((boolean) value);
            }
        },
        EQ {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return comparableOperation(operatorExprASTree, context, this);
            }
        },
        NEQ {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return comparableOperation(operatorExprASTree, context, this);
            }
        },
        GT {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return comparableOperation(operatorExprASTree, context, this);
            }
        },
        LT {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return comparableOperation(operatorExprASTree, context, this);
            }
        },
        GTEQ {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return comparableOperation(operatorExprASTree, context, this);
            }
        },
        LTEQ {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                return comparableOperation(operatorExprASTree, context, this);
            }
        },
        PLUS {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                Object leftValue = operatorExprASTree.getOperand(OperandPosition.LEFT).getValue(context);
                Object rightValue = operatorExprASTree.getOperand(OperandPosition.RIGHT).getValue(context);
                return leftValue instanceof String || rightValue instanceof String
                        ? leftValue + Objects.toString(rightValue) // ???????????? + ??????
                        : ParserUtils.numberAdd(leftValue, rightValue);
            }
        },
        MINUS {

            @Override
            public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
                Number right = numberOperation(operatorExprASTree, context, OperandPosition.RIGHT, 0);
                return numberOperation(operatorExprASTree, context, OperandPosition.LEFT, ParserUtils.negate(right));
            }
        }, AMP, AMPAMP, BAR, BARBAR;

        public Object operation(OperatorExprASTree operatorExprASTree, ExpressionContext context) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private static boolean comparableOperation(OperatorExprASTree operatorExprASTree, ExpressionContext context, OperatorType type) {
            Object leftValue = operatorExprASTree.getOperand(OperandPosition.LEFT).getValue(context);
            Object rightValue = operatorExprASTree.getOperand(OperandPosition.RIGHT).getValue(context);
            if (leftValue instanceof Comparable<?> leftComparable
                    && rightValue instanceof Comparable<?> rightComparable) {
                int compareResult = Objects.<Comparable>compare(leftComparable, rightComparable, Comparator.naturalOrder());
                return switch (type) {
                    case EQ -> compareResult == 0;
                    case NEQ -> compareResult != 0;
                    case GT -> compareResult > 0;
                    case LT -> compareResult < 0;
                    case GTEQ -> compareResult >= 0;
                    case LTEQ -> compareResult <= 0;
                    default -> throw new RuntimeException("Comparison operators are not supported");
                };
            }
            throw new RuntimeException("Operands are not comparable");
        }

        private static Number numberOperation(OperatorExprASTree operatorExprASTree, ExpressionContext context, OperandPosition position, Number constantOperand) {
            ExpressionASTree operand = operatorExprASTree.getOperand(position);
            Object value = operand.getValue(context);
            return ParserUtils.numberAdd(value, constantOperand);
        }

        public static OperatorType fromTokenKind(boolean prefix, TokenKind kind) {
            return switch (kind) {
                case INC -> prefix ? PREINC : POSTINC;
                case DEC -> prefix ? PREDEC : POSTDEC;
                case BANG -> BANG;
                case EQ, NEQ, GT, LT, GTEQ, LTEQ, PLUS, MINUS, AMP, AMPAMP, BAR, BARBAR
                        -> OperatorType.valueOf(kind.name());
                default -> NONE;
            };
        }

        /**
         * ???????????????token??????????????????????????????????????????
         * @param kind token??????
         * @return ????????????????????????{@code true}???????????????{@code false}
         */
        public static boolean isBinaryOperator(TokenKind kind) {
            return switch (kind) {
                // ASSIGN???????????????
                case EQ, NEQ, GT, LT, GTEQ, LTEQ, PLUS, /*ASSIGN,*/ MINUS, AMP, AMPAMP, BAR, BARBAR -> true;
                default -> false;
            };
        }
    }

    /**
     * ??????????????????
     */
    private final OperatorType type;

    protected OperatorExprASTree(OperatorType type) {
        this.type = type;
    }

    public OperatorType getType() {
        return type;
    }

    /**
     * ????????????????????????
     * @param position ???????????????????????????????????????
     * @return ??????????????????
     */
    public abstract ExpressionASTree getOperand(OperandPosition position);
}
