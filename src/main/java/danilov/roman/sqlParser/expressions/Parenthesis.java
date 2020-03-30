package danilov.roman.sqlParser.expressions;

import danilov.roman.sqlParser.TypesConditions;

public class Parenthesis implements Expression {
    private Expression expression;

    public Parenthesis() {
    }

    public Parenthesis(Expression expression) {
        this.setExpression(expression);
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String toString() {
        return String.format("%s%s%s",
                TypesConditions.LEFT_PARENTHESIS,
                getExpression(),
                TypesConditions.RIGHT_PARENTHESIS
        );
    }
}
