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
    private List<Column> groupByColumns = new ArrayList<>();
    private List<Sort> sortColumns = new ArrayList<>();
    private Limit limit;
    private Offset offset;

    private String selectWord = "SELECT";

    private final char SPACE = ' ';


    public Select() {
    }

    public void parse(String sql) {
        parseSelectColumns(substringBetween(sql, selectWord, fromWord));
        List<String> allElementsInQuery = findAllElementsInQuery(sql);

        String data = "";

        for (int i = 0; i < allElementsInQuery.size(); i++) {
            if (i == allElementsInQuery.size() - 1) {
                data = substringBetween(sql, allElementsInQuery.get(i), null);
            } else {
                data = substringBetween(sql, allElementsInQuery.get(i), allElementsInQuery.get(i + 1));
            }

            switch (allElementsInQuery.get(i)) {
                case fromWord:
                    parseFromSources(data);
                    break;
                case whereWord:
                    parseFromSources(data);
                    break;
                case limitWord:
                    parseLimit(data);
                    break;
                case offsetWord:
                    parseOffset(data);
                    break;
                case orderByWord:
                    parseOrder(data);
                    break;
                case groupByWord:
                    parseGroup(data);
                    break;
            }

            String ss = "1";
        }


        String ss = "1";

    }

    /**
     * Парсинг колонок, которые запрашиваются в запросе
     *
     * @param str String
     */
    private void parseSelectColumns(String str) {
        String[] selectColumns = StringUtils.split(str, ',');
        for (String column : selectColumns) {
            if (column.indexOf(SPACE) != -1) {
                String[] split = StringUtils.split(column, String.valueOf(SPACE), 2);
                columns.add(new Column(split[0], split[1]));
            } else {
                columns.add(new Column(column));
            }
        }
    }

    /**
     * Парсинг таблиц, из которых будет браться информация.
     * Сначала ищются подзапросы.
     * В оставшейся части строки находим таблицы с алиасами.
     *
     * @param str String
     */
    private void parseFromSources(String str) {
        String selectStr = "";
        if (str.indexOf('(') != -1) {
            selectStr = StringUtils.substringBetween(str, "(", ",");
            if (selectStr == null) {
                selectStr = StringUtils.substringAfter(str, "(");
            }
            String[] selectPaths = StringUtils.split(selectStr, ')');
            Select select = new Select();
            select.parse(selectPaths[0]);
            fromSources.add(new Source(select, selectPaths[1]));
        }

        if (selectStr.length() > 0) {
            str = StringUtils.remove(str, "(" + selectStr);
        }

        String[] tables = StringUtils.split(str, ',');
        for (String table : tables) {
            if (!StringUtils.isBlank(table)) {
                if (table.indexOf(SPACE) != -1) {
                    String[] split = StringUtils.split(table, String.valueOf(SPACE), 2);
                    fromSources.add(new Source(split[0], split[1]));
                } else {
                    fromSources.add(new Source(str));
                }
            }
        }
    }

    /**
     * Определение значения лимита извлекаемых строк.
     * Так же проверка на Offset.
     *
     * @param str String
     */
    private void parseLimit(String str) {
        if (str.indexOf(',') != -1) {
            String[] limitAndOffset = StringUtils.split(str, ',');
            limit = new Limit(
                    Integer.parseInt(StringUtils.trim(limitAndOffset[1])),
                    Integer.parseInt(StringUtils.trim(limitAndOffset[0]))
            );
        } else {
            limit = new Limit(Integer.parseInt(str));
        }
    }

    /**
     * Определение значения Offset
     *
     * @param str String
     */
    private void parseOffset(String str) {
        offset = new Offset(Integer.parseInt(StringUtils.trim(str)));
    }

    /**
     * Определение колонок, по которым сортируется данные из запроса
     *
     * @param str String
     */
    private void parseOrder(String str) {
        str = StringUtils.trim(str);
        String[] orderFields = StringUtils.split(str, ',');
        for (String field : orderFields) {
            if (field.indexOf(SPACE) != -1) {
                String[] columnAndOrder = StringUtils.split(field, String.valueOf(SPACE));
                sortColumns.add(new Sort(columnAndOrder[0], columnAndOrder[1]));
            } else {
                sortColumns.add(new Sort(field));
            }
        }
    }

    /**
     * Определение колонок, по которым группируются данные из запроса
     *
     * @param str String
     */
    private void parseGroup(String str) {
        String[] groupFields = StringUtils.split(str, ',');
        for (String field : groupFields) {
            groupByColumns.add(new Column(field));
        }
    }
}
