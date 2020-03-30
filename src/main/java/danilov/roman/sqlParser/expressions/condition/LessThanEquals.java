package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class LessThanEquals extends ConditionExpression {
    public LessThanEquals() {
        this.setToken(TypesConditions.LESS_THAN_EQUALS);
    }
}
