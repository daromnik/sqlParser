package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class EqualsTo extends ConditionExpression {
    public EqualsTo() {
        this.setToken(QueryElements.EQUALS_TO);
    }
}
