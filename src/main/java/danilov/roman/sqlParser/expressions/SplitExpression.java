package danilov.roman.sqlParser.expressions;

public abstract class SplitExpression implements Expression {
    private Expression leftPart;
    private Expression rightPart;

    private String token;

    public Expression getLeftPart() {
        return leftPart;
    }

    public void setLeftPart(Expression leftPart) {
        this.leftPart = leftPart;
    }

    public Expression getRightPart() {
        return rightPart;
    }

    public void setRightPart(Expression rightPart) {
        this.rightPart = rightPart;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return String.format("%s %s %s", this.getLeftPart(), this.getToken(), this.getRightPart());
    }
}
