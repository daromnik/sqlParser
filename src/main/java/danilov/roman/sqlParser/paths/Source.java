package danilov.roman.sqlParser.paths;

import danilov.roman.sqlParser.queries.Select;
import org.apache.commons.lang3.StringUtils;

public class Source {
    private String table;
    private String alias;
    private Select select;

    public Source(String str) {
        parse(StringUtils.trim(str));
    }

    public Source(Select select) {
        this.select = select;
    }

    private void parse(String str) {
        char space = ' ';
        if (str.indexOf(space) != -1) {
            String[] split = StringUtils.split(str, String.valueOf(space), 2);
            alias = split[1];
            table = split[0];
        } else {
            table = str;
        }
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
