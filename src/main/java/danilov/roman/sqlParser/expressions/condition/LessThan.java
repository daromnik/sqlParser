package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class LessThan extends ConditionExpression {
    public LessThan() {
        this.setToken(QueryElements.LESS_THAN);
    }
}
