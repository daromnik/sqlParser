package danilov.roman.sqlParser.paths;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class Join {
    private boolean right = false;
    private boolean left = false;
    private boolean full = false;
    private boolean inner = false;
    private boolean simple = false;
    private Source source;
    private Expression onExpression;

    public Join() {
    }

    public Join(Source source, String typeJoin) {
        this.source = source;
        setTypeJoin(typeJoin);
    }

    public void setTypeJoin(String typeJoin) {
        switch (typeJoin.toUpperCase()) {
            case QueryElements.FULL:
                setFull(true);
                break;
            case QueryElements.LEFT:
                setLeft(true);
                break;
            case QueryElements.RIGHT:
                setRight(true);
                break;
            case QueryElements.INNER:
                setInner(true);
                break;
            default:
                setSimple(true);
        }
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public boolean isInner() {
        return inner;
    }

    public void setInner(boolean inner) {
        this.inner = inner;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Expression getOnExpression() {
        return onExpression;
    }

    public void setOnExpression(Expression onExpression) {
        this.onExpression = onExpression;
    }

    public String toString() {
        String type = "";
        if (isRight()) {
            type = "RIGHT ";
        } else if (isFull()) {
            type = "FULL ";
        } else if (isLeft()) {
            type = "LEFT ";
        } else if (isInner()) {
            type = "INNER ";
        }

        return type + "JOIN " + getSource() + (getOnExpression() != null ? " ON " + getOnExpression() + "" : "");
    }
}
