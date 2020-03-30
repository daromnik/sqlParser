package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class GreaterThanEquals extends ConditionExpression {
    public GreaterThanEquals() {
        this.setToken(TypesConditions.GREATER_THAN_EQUALS);
    }
}
