package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class NotEqualsTo extends ConditionExpression {
    public NotEqualsTo() {
        this.setToken(QueryElements.NOT_EQUALS_TO);
    }
}
