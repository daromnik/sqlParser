package danilov.roman.sqlParser.queries;

import danilov.roman.sqlParser.TypesConditions;
import danilov.roman.sqlParser.expressions.*;
import danilov.roman.sqlParser.expressions.condition.*;
import danilov.roman.sqlParser.expressions.split.AndExpression;
import danilov.roman.sqlParser.expressions.split.OrExpression;
import danilov.roman.sqlParser.paths.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Select extends AbstractQuery implements IQuery {

    private List<Column> columns = new ArrayList<>();
    private List<Source> fromSources = new ArrayList<>();
    private List<Join> joins = new ArrayList<>();
    private Expression whereClauses;
    private Expression havingClauses;
    private List<Column> groupByColumns = new ArrayList<>();
    private List<Sort> sortColumns = new ArrayList<>();
    private Limit limit;
    private Offset offset;

    private String selectWord = "SELECT";

    private final char SPACE = ' ';


    public Select() {
    }

    public void parse(String sql) {
        sql = sql.replace(", ", ",").replace(";", "");
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
                    whereClauses = parseWhere(data);
                    break;
                case havingWord:
                    havingClauses = parseWhere(data);
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
        String[] selectColumns = str.trim().split(",");
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
        str = str.trim();
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

        String[] tables = str.split(",");
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
     * Парсинг строки условий WHERE;
     * Разделяем строку по символам "( )" и идем по разделенным элементам;
     * Делим строку на 2 части по AND или OR или по выражению в скобках;
     * Левую часть отправляем в метод (parseCondition) определения (если только это не выражение в скобках,
     * если это так, то отправляем повторно парсится в текущий метод), какое используется условие (=,>,<...),
     * правую еще раз отправляем парсится в текущий метод.
     * И так пока не кончится строка.
     *
     * @param str String
     * @return Expression
     */
    private Expression parseWhere(String str) {
        StringTokenizer st = new StringTokenizer(str.trim(), "( )", true);
        String token;
        SplitExpression expression = null;
        Expression parenthesisExpression = null;
        StringBuilder leftBuildExp = new StringBuilder();
        StringBuilder rightBuildExp = new StringBuilder();
        boolean isBetween = false;
        boolean isSqlFunc = false;
        boolean startFuncArgs = false;
        int leftParenthesis = 0;
        int rightParenthesis = 0;

        while(st.hasMoreTokens()) {
            token = st.nextToken();

            if (expression == null && leftParenthesis == 0 && !isBetween && token.equalsIgnoreCase(TypesConditions.AND)) {
                expression = new AndExpression(
                        parenthesisExpression != null ?
                                parenthesisExpression :
                                parseCondition(leftBuildExp.toString())
                );
            } else if (expression == null && leftParenthesis == 0 && token.equalsIgnoreCase(TypesConditions.OR)) {
                expression = new OrExpression(
                        parenthesisExpression != null ?
                                parenthesisExpression :
                                parseCondition(leftBuildExp.toString())
                );
            } else {
                if (expression != null) {
                    rightBuildExp.append(token);
                } else {
                    if (token.equals(TypesConditions.LEFT_PARENTHESIS)) {
                        if (!isSqlFunc) {
                            leftParenthesis++;
                        } else {
                            startFuncArgs = true;
                        }
                    } else if (token.equals(TypesConditions.RIGHT_PARENTHESIS)) {
                        if (!startFuncArgs) {
                            rightParenthesis++;
                            if (leftParenthesis == rightParenthesis) {
                                leftBuildExp.deleteCharAt(0);
                                parenthesisExpression = new Parenthesis(parseWhere(leftBuildExp.toString()));
                                leftParenthesis = rightParenthesis = 0;
                            }
                        } else {
                            startFuncArgs = false;
                            isSqlFunc = false;
                        }
                    } else if (token.equalsIgnoreCase(TypesConditions.BETWEEN)) {
                        // помечаем, что встретили оператор BETWEEN, что бы в дальнейшем ничего не делать с его AND
                        isBetween = true;
                    } else if (token.isBlank()) {
                        isSqlFunc = false;
                    } else {
                        isSqlFunc = true;
                    }
                    // блок для проверки sql функций COUNT(*), SUM(*), ...
                    // если текущий токен == "(" и isSqlFunc == true - то значит это функция
//                    if (!token.isBlank()) {
//                        isSqlFunc = true;
//                    } else {
//                        isSqlFunc = false;
//                    }
                    leftBuildExp.append(token);
                }
                // сбрасываем для других BETWEEN
                if (isBetween && token.equalsIgnoreCase(TypesConditions.AND)) {
                    isBetween = false;
                }
            }
        }

        if (expression != null && rightBuildExp.length() > 0) {
            expression.setRightPart(parseWhere(rightBuildExp.toString()));
        } else {
            expression = parseCondition(leftBuildExp.toString());
        }

        return expression;
    }

    /**
     * Парсинг строки для определения условия, которое используется для выборки в запросе.
     * Разделяем строку по символам "( )" и идем по разделенным элементам;
     * Находим элмент, который совпадает с условием в switch;
     * И делим строку на 2 части относительно этого условия;
     * И возвращаем объект класса этого условия, куда передаем левую и правую часть выражения.
     *
     * @param str String
     * @return ConditionExpression
     */
    private ConditionExpression parseCondition(String str) {
        StringTokenizer st = new StringTokenizer(str.trim(), "( )", true);
        String token;
        ConditionExpression expression = null;
        StringBuilder left = new StringBuilder();
        Expression right = null;
        boolean isBetween = false;
        String leftBetween = "";
        String rightBetween = "";
        boolean isIn = false;

        while(st.hasMoreTokens()) {
            token = st.nextToken();

            switch (token.toUpperCase()) {
                case TypesConditions.EQUALS_TO:
                    expression = new EqualsTo();
                    break;
                case TypesConditions.NOT_EQUALS_TO:
                    expression = new NotEqualsTo();
                    break;
                case TypesConditions.GREATER_THAN:
                    expression = new GreaterThan();
                    break;
                case TypesConditions.GREATER_THAN_EQUALS:
                    expression = new GreaterThanEquals();
                    break;
                case TypesConditions.LESS_THAN:
                    expression = new LessThan();
                    break;
                case TypesConditions.LESS_THAN_EQUALS:
                    expression = new LessThanEquals();
                    break;
                case TypesConditions.BETWEEN:
                    expression = new Between();
                    isBetween = true;
                    break;
                case TypesConditions.SPACE:
                    break;
                default:
                    if (expression == null) {
                        left.append(token);
                    } else {
                        if (isBetween) {
                            if (!token.equalsIgnoreCase(TypesConditions.AND)) {
                                if (leftBetween.isEmpty()) {
                                    leftBetween = token;
                                } else {
                                    rightBetween = token;
                                }
                            }
                            if (!leftBetween.isEmpty() && !rightBetween.isEmpty()) {
                                right = new BetweenValue(leftBetween, rightBetween);
                            }
                        } else {
                            right = new StringValue(token);
                        }
                    }
            }
        }

        expression.setParts(new StringValue(left.toString()), right);

        return expression;
    }

    /**
     * Определение значения лимита извлекаемых строк.
     * Так же проверка на Offset.
     *
     * @param str String
     */
    private void parseLimit(String str) {
        if (str.indexOf(',') != -1) {
            String[] limitAndOffset = str.split(",");
            limit = new Limit(
                    Integer.parseInt(limitAndOffset[1].trim()),
                    Integer.parseInt(limitAndOffset[0].trim())
            );
        } else {
            limit = new Limit(Integer.parseInt(str.trim()));
        }
    }

    /**
     * Определение значения Offset
     *
     * @param str String
     */
    private void parseOffset(String str) {
        offset = new Offset(Integer.parseInt(str.trim()));
    }

    /**
     * Определение колонок, по которым сортируется данные из запроса
     *
     * @param str String
     */
    private void parseOrder(String str) {
        String[] orderFields = str.trim().split(",");
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
        String[] groupFields = str.split(",");
        for (String field : groupFields) {
            groupByColumns.add(new Column(field));
        }
    }
}
