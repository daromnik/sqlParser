package danilov.roman.sqlParser.expressions.split;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.Expression;
import danilov.roman.sqlParser.expressions.SplitExpression;

public class OrExpression extends SplitExpression {

    public OrExpression() {
        this.setToken(TypesConditions.OR);
    }

    public OrExpression(Expression left) {
        this();
        this.setLeftPart(left);
    }

    public OrExpression(Expression left, Expression right) {
        this();
        this.setLeftPart(left);
        this.setRightPart(right);
    }
}
