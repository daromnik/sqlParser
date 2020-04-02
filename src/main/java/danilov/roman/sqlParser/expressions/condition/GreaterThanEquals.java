package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class GreaterThanEquals extends ConditionExpression {
    public GreaterThanEquals() {
        this.setToken(QueryElements.GREATER_THAN_EQUALS);
    }
}
