package danilov.roman.sqlParser.paths;

import org.apache.commons.lang3.StringUtils;

public class Column {
    private String name;
    private String table;
    private String alias;

    public Column(String name) {
        parse(StringUtils.trim(name));
    }

    private void parse(String str) {
        char space = ' ';
        if (str.indexOf(space) != -1) {
            String[] split = StringUtils.split(str, String.valueOf(space), 2);
            alias = split[1];
            parseTable(split[0]);
        } else {
            parseTable(str);
        }
    }

    private void parseTable(String str) {
        char dot = '.';
        if (str.indexOf(dot) != -1) {
            String[] split = StringUtils.split(str, dot);
            table = split[0];
            name = split[1];
        } else {
            name = str;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
