package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class LessThan extends ConditionExpression {
    public LessThan() {
        this.setToken(TypesConditions.LESS_THAN);
    }
}
