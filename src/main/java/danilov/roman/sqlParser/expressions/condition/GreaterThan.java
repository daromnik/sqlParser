package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class GreaterThan extends ConditionExpression {
    public GreaterThan() {
        this.setToken(QueryElements.GREATER_THAN);
    }
}
