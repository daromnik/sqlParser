package danilov.roman.sqlParser.expressions;

import danilov.roman.sqlParser.TypesConditions;

public class StringValue implements Expression {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return getValue();
    }
}
