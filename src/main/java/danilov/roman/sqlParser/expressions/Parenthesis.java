package danilov.roman.sqlParser.expressions;

import danilov.roman.sqlParser.QueryElements;

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
                QueryElements.LEFT_PARENTHESIS,
                getExpression(),
                QueryElements.RIGHT_PARENTHESIS
        );
    }
}
