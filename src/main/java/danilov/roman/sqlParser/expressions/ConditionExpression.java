package danilov.roman.sqlParser.expressions;

public abstract class ConditionExpression extends SplitExpression {
    public void setParts(Expression left, Expression right ) {
        this.setLeftPart(left);
        this.setRightPart(right);
    }
}
