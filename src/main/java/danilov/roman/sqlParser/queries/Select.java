package danilov.roman.sqlParser.queries;

import danilov.roman.sqlParser.paths.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Select extends AbstractQuery implements IQuery {

    private List<Column> columns = new ArrayList<>();
    private List<Source> fromSources = new ArrayList<>();
    private List<Join> joins = new ArrayList<>();
    private List<WhereClause> whereClauses = new ArrayList<>();
    private List<Group> groupByColumns = new ArrayList<>();
    private List<Sort> sortColumns = new ArrayList<>();
    private Integer limit;
    private Integer offset;

    private String selectWord = "SELECT";

    public Select() {
    }

    public void parse(String sql) {

        String selectString = substringBetween(sql, selectWord, fromWord);
        parseSelectColumns(selectString);
        String ss = "1";

    }

    private void parseSelectColumns(String str) {
        String[] selectColumns = StringUtils.split(str, ',');
        for (String column : selectColumns) {
            columns.add(new Column(column));
        }
    }
}
