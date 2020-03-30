package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;
import danilov.roman.sqlParser.expressions.Expression;

public class EqualsTo extends ConditionExpression {
    public EqualsTo() {
        this.setToken(TypesConditions.EQUALS_TO);
    }
}
