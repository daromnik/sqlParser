package danilov.roman.sqlParser.paths;

import danilov.roman.sqlParser.queries.Select;
import org.apache.commons.lang3.StringUtils;

public class Source {
    private String table;
    private String alias;
    private Select select;

    public Source(String table) {
        this.table = table;
    }

    public Source(Select select) {
        this.select = select;
    }

    public Source(String table, String alias) {
        this(table);
        this.alias = alias;
    }

    public Source(Select select, String alias) {
        this(select);
        this.alias = alias;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }
}
