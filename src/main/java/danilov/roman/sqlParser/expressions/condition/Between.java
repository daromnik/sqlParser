package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class Between extends ConditionExpression {
    public Between() {
        this.setToken(QueryElements.BETWEEN);
    }
}
