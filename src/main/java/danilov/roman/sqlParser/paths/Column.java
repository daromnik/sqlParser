package danilov.roman.sqlParser.paths;

import org.apache.commons.lang3.StringUtils;

public class Column {
    private String name;
    private String table;
    private String alias;
    private String func;

    public Column(String columnInfo) {
        parse(columnInfo.trim());
    }

    public Column(String columnInfo, String alias) {
        this(columnInfo);
        this.alias = alias;
    }

    private void parse(String str) {
        if (str.indexOf('(') != -1){
            String[] split = StringUtils.split(str, '(');
            func = split[0];
            parseTable(split[1].substring(0, split[1].length() - 1));
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

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String toString() {
        return (getFunc() != null ? getFunc() + "(" + (getTable() != null ? getTable() + "." : "") + getName() + ")" :
                (getTable() != null ? getTable() + "." : "") + getName()) +
                (getAlias() != null ? " " + getAlias() : "");
    }
}
