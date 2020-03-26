package danilov.roman.sqlParser.paths;

public class Sort {
    private Column column;
    private String order;

    private static final String orderDefault = "ASC";

    public Sort(String columnData) {
        this.column = new Column(columnData);
        this.order = orderDefault;
    }

    public Sort(String columnData, String order) {
        this(columnData);
        this.order = order;
    }
}
