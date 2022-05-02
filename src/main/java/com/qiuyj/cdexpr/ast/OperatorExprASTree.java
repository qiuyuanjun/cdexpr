package com.qiuyj.cdexpr.ast;

/**
 * @author qiuyj
 * @since 2022-05-03
 */
public abstract class OperatorExprASTree {

    public enum OperandPosition {

        LEFT, RIGHT;
    }

    public enum OperatorType {

        NONE,

    }

    /**
     * 操作符的类型
     */
    private final OperatorType type;

    protected OperatorExprASTree(OperatorType type) {
        this.type = type;
    }

    /**
     * 得到对应的操作数
     * @param position 操作数的位置，左边还是右边
     * @return 对应的操作数
     */
    public abstract ExpressionASTree getOperand(OperandPosition position);
}
