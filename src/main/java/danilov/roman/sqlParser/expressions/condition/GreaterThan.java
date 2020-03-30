package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class GreaterThan extends ConditionExpression {
    public GreaterThan() {
        this.setToken(TypesConditions.GREATER_THAN);
    }
}
