package danilov.roman.sqlParser.expressions.split;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.Expression;
import danilov.roman.sqlParser.expressions.SplitExpression;

public class AndExpression extends SplitExpression {

    public AndExpression() {
        this.setToken(QueryElements.AND);
    }

    public AndExpression(Expression left) {
        this();
        this.setLeftPart(left);
    }

    public AndExpression(Expression left, Expression right) {
        this();
        this.setLeftPart(left);
        this.setRightPart(right);
    }
}
