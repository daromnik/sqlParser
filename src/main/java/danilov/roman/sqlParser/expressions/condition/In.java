package danilov.roman.sqlParser.expressions.condition;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.ConditionExpression;

public class In extends ConditionExpression {
    public In() {
        this.setToken(TypesConditions.IN);
    }
}
