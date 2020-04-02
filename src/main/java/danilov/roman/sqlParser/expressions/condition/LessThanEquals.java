package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class LessThanEquals extends ConditionExpression {
    public LessThanEquals() {
        this.setToken(QueryElements.LESS_THAN_EQUALS);
    }
}
