package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class Like extends ConditionExpression {
    public Like() {
        this.setToken(TypesConditions.LIKE);
    }
}
