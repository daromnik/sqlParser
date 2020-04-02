package danilov.roman.sqlParser.paths;

public class Limit {
    private int count;
    private Offset offset;

    public Limit(int count) {
        this.count = count;
    }

    public Limit(int count, int offsetCount) {
        this(count);
        offset = new Offset(offsetCount);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public String toString() {
        return "LIMIT " + count + (offset != null ? " " + offset : "");
    }
}
