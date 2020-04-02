package danilov.roman.sqlParser.expressions;

import danilov.roman.sqlParser.QueryElements;

public class BetweenValue implements Expression {
    private String from;
    private String to;

    public BetweenValue(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String toString() {
        return String.format("%s %s %s", getFrom(), QueryElements.AND, getTo());
    }
}
