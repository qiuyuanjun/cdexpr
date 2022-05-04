package com.qiuyj.cdexpr.ast.impl;

import com.qiuyj.cdexpr.ExpressionContext;
import com.qiuyj.cdexpr.ast.BinaryExprASTree;
import com.qiuyj.cdexpr.ast.ExpressionASTree;
import com.qiuyj.cdexpr.ast.OperatorExprASTree;

/**
 * @author qiuyj
 * @since 2022-05-03
 */
public class CDEBinary extends OperatorExprASTree implements BinaryExprASTree {

    private final ExpressionASTree leftOperand;

    private final ExpressionASTree rightOperand;

    public CDEBinary(ExpressionASTree leftOperand, ExpressionASTree rightOperand, OperatorType type) {
        super(type);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public ExpressionASTree getLeftOperand() {
        return leftOperand;
    }

    @Override
    public ExpressionASTree getRightOperand() {
        return rightOperand;
    }

    @Override
    public ExpressionASTree getOperand(OperandPosition position) {
        return position == OperandPosition.LEFT
                ? getLeftOperand()
                : getRightOperand();
    }

    @Override
    public Object getValue(ExpressionContext context) {
        return getType().operation(this, context);
    }
}
