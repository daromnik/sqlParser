package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;
import danilov.roman.sqlParser.expressions.Expression;

public class NotEqualsTo extends ConditionExpression {
    public NotEqualsTo() {
        this.setToken(TypesConditions.NOT_EQUALS_TO);
    }
}
