package danilov.roman.sqlParser.models;

import danilov.roman.sqlParser.QueryElements;
import danilov.roman.sqlParser.expressions.*;
import danilov.roman.sqlParser.expressions.condition.*;
import danilov.roman.sqlParser.expressions.split.AndExpression;
import danilov.roman.sqlParser.expressions.split.OrExpression;
import danilov.roman.sqlParser.paths.*;
import danilov.roman.sqlParser.queries.Select;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ParseSelectQuery extends AbstractParseQuery {

    private final String selectWord = "SELECT";
    private final char SPACE = ' ';

    /**
     * Метод парсит передаваемый запрос и разбивает его на составные объекты.
     * Строка разбивается по словам операторам и
     * потом эти части передаются соответствующим методам на дальнешую обработку.
     *
     * @param sql String Запрос, который нужно распарсить
     */
    public Select parse(String sql) {
        Select select = new Select();
        sql = StringUtils.replaceEach(sql, new String[]{", ", ";", "\n"}, new String[]{",", "", ""});
        select.setSelectColumns(parseSelectColumns(substringBetween(sql, selectWord, fromWord)));
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
                    select.setFrom(parseFromSources(data, select.getJoins()));
                    break;
                case whereWord:
                    select.setWhereClauses(parseWhere(data));
                    break;
                case havingWord:
                    select.setHavingClauses(parseWhere(data));
                    break;
                case limitWord:
                    select.setLimit(parseLimit(data));
                    break;
                case offsetWord:
                    select.setOffset(parseOffset(data));
                    break;
                case orderByWord:
                    select.setSortColumns(parseOrder(data));
                    break;
                case groupByWord:
                    select.setGroupByColumns(parseGroup(data));
                    break;
                case joinWord:
                case leftJoinWord:
                case rightJoinWord:
                case innerJoinWord:
                case fullJoinWord:
                    select.addJoins(parseJoins(data, allElementsInQuery.get(i)));
                    break;
            }
        }

        return select;
    }

    /**
     * Парсинг колонок, которые запрашиваются в запросе
     *
     * @param str String
     */
    private List<Column> parseSelectColumns(String str) {
        List<Column> columns = new ArrayList<>();
        String[] sColumns = str.trim().split(",");
        for (String column : sColumns) {
            if (column.indexOf(SPACE) != -1) {
                String[] split = StringUtils.split(column, String.valueOf(SPACE), 2);
                columns.add(new Column(split[0], split[1]));
            } else {
                columns.add(new Column(column));
            }
        }
        return columns;
    }

    /**
     * Парсинг таблиц, из которых будет браться информация.
     * Строка делится по "," и получаются таблицы из которых нужно получить данные в запросе.
     * Так же ищутся подзапросы.
     * Первая таблица возвращается для значения from,
     * Остальные отправляются в JOIN.
     *
     * @param str String
     */
    private Source parseFromSources(String str, List<Join> joinsTable) {
        StringTokenizer st = new StringTokenizer(str.trim(), "( ),", true);
        String token;
        Source source = null;
        StringBuilder fromBuild = new StringBuilder();
        List<Source> froms = new ArrayList<>();
        int leftParenthesis = 0;
        int rightParenthesis = 0;

        while(st.hasMoreTokens()) {
            token = st.nextToken();

            if (token.equals(QueryElements.LEFT_PARENTHESIS)) {
                leftParenthesis++;
                if (leftParenthesis > 1) {
                    fromBuild.append(token);
                }
            } else if(token.equals(QueryElements.RIGHT_PARENTHESIS)) {
                rightParenthesis++;
                if (leftParenthesis == rightParenthesis) {
                    leftParenthesis = rightParenthesis = 0;
                } else {
                    // Это делается для дальнейшего отделения select запроса от его алиаса
                    fromBuild.append("|");
                }
            } else if (token.equals(",") && leftParenthesis == 0) {
                froms.add(createSourceFromData(fromBuild.toString()));
                fromBuild.setLength(0);
            } else {
                fromBuild.append(token);
            }
            if (!st.hasMoreTokens()) {
                froms.add(createSourceFromData(fromBuild.toString()));
            }
        }

        if (froms.size() > 0) {
            source = froms.get(0);
            for (int i = 1; i < froms.size(); i++) {
                joinsTable.add(new Join(froms.get(i), ""));
            }
        }

        return source;
    }

    /**
     * Создает из строки объект Source
     *
     * @param data String
     * @return Source
     */
    private Source createSourceFromData(String data) {
        if (data.indexOf('|') != -1) {
            String[] selectQueryAndAlias = StringUtils.split(data, '|');
            return new Source(parse(selectQueryAndAlias[0]), selectQueryAndAlias[1]);
        } else {
            if (data.indexOf(SPACE) != -1) {
                String[] split = StringUtils.split(data, String.valueOf(SPACE), 2);
                return new Source(split[0], split[1]);
            } else {
                return new Source(data);
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

            if (expression == null && leftParenthesis == 0 && !isBetween && token.equalsIgnoreCase(QueryElements.AND)) {
                expression = new AndExpression(
                        parenthesisExpression != null ?
                                parenthesisExpression :
                                parseCondition(leftBuildExp.toString())
                );
            } else if (expression == null && leftParenthesis == 0 && token.equalsIgnoreCase(QueryElements.OR)) {
                expression = new OrExpression(
                        parenthesisExpression != null ?
                                parenthesisExpression :
                                parseCondition(leftBuildExp.toString())
                );
            } else {
                if (expression != null) {
                    rightBuildExp.append(token);
                } else {
                    if (token.equals(QueryElements.LEFT_PARENTHESIS)) {
                        if (!isSqlFunc) {
                            leftParenthesis++;
                        } else {
                            startFuncArgs = true;
                        }
                    } else if (token.equals(QueryElements.RIGHT_PARENTHESIS)) {
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
                    } else if (token.equalsIgnoreCase(QueryElements.BETWEEN)) {
                        // помечаем, что встретили оператор BETWEEN, что бы в дальнейшем ничего не делать с его AND
                        isBetween = true;
                    } else if (token.isBlank()) {
                        // блок для проверки sql функций COUNT(*), SUM(*), ...
                        // если текущий токен == "(" и isSqlFunc == true - то значит это функция
                        isSqlFunc = false;
                    } else {
                        isSqlFunc = true;
                    }
                    leftBuildExp.append(token);
                }
                // сбрасываем для других BETWEEN
                if (isBetween && token.equalsIgnoreCase(QueryElements.AND)) {
                    isBetween = false;
                }
            }
        }

        if (expression != null && rightBuildExp.length() > 0) {
            expression.setRightPart(parseWhere(rightBuildExp.toString()));
        } else {
            if (parenthesisExpression != null) {
                return parenthesisExpression;
            } else {
                expression = parseCondition(leftBuildExp.toString());
            }
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
                case QueryElements.EQUALS_TO:
                    expression = new EqualsTo();
                    break;
                case QueryElements.NOT_EQUALS_TO:
                    expression = new NotEqualsTo();
                    break;
                case QueryElements.GREATER_THAN:
                    expression = new GreaterThan();
                    break;
                case QueryElements.GREATER_THAN_EQUALS:
                    expression = new GreaterThanEquals();
                    break;
                case QueryElements.LESS_THAN:
                    expression = new LessThan();
                    break;
                case QueryElements.LESS_THAN_EQUALS:
                    expression = new LessThanEquals();
                    break;
                case QueryElements.BETWEEN:
                    expression = new Between();
                    isBetween = true;
                    break;
                case QueryElements.SPACE:
                    break;
                default:
                    if (expression == null) {
                        left.append(token);
                    } else {
                        if (isBetween) {
                            if (!token.equalsIgnoreCase(QueryElements.AND)) {
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
    private Limit parseLimit(String str) {
        if (str.indexOf(',') != -1) {
            String[] limitAndOffset = str.split(",");
            return new Limit(
                    Integer.parseInt(limitAndOffset[1].trim()),
                    Integer.parseInt(limitAndOffset[0].trim())
            );
        } else {
            return new Limit(Integer.parseInt(str.trim()));
        }
    }

    /**
     * Определение значения Offset
     *
     * @param str String
     */
    private Offset parseOffset(String str) {
        return new Offset(Integer.parseInt(str.trim()));
    }

    /**
     * Определение колонок, по которым сортируется данные из запроса
     *
     * @param str String
     */
    private List<Sort> parseOrder(String str) {
        List<Sort> columns = new ArrayList<>();
        String[] orderFields = str.trim().split(",");
        for (String field : orderFields) {
            if (field.indexOf(SPACE) != -1) {
                String[] columnAndOrder = StringUtils.split(field, String.valueOf(SPACE));
                columns.add(new Sort(columnAndOrder[0], columnAndOrder[1]));
            } else {
                columns.add(new Sort(field));
            }
        }
        return columns;
    }

    /**
     * Определение колонок, по которым группируются данные из запроса
     *
     * @param str String
     */
    private List<Column> parseGroup(String str) {
        List<Column> columns = new ArrayList<>();
        String[] groupFields = str.split(",");
        for (String field : groupFields) {
            columns.add(new Column(field));
        }
        return columns;
    }

    /**
     * Парсинг строк для нахождения join-нов.
     *
     * @param str String
     * @param type String Тип join
     * @return List<Join>
     */
    private List<Join> parseJoins(String str, String type) {
        List<Join> joinsTable = new ArrayList<>();
        if (StringUtils.containsIgnoreCase(str, QueryElements.JOIN)) {
            StringTokenizer st = new StringTokenizer(str.trim(), " ", true);
            String token = "";
            String lastToken = "";
            String joinType = type;
            String nextJoinType = "";
            StringBuilder joinBuild = new StringBuilder();
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (token.equals(QueryElements.JOIN)) {
                    if (StringUtils.equalsAnyIgnoreCase(
                            lastToken,
                            QueryElements.FULL,
                            QueryElements.LEFT,
                            QueryElements.RIGHT,
                            QueryElements.INNER
                    )) {
                        joinBuild.setLength(joinBuild.length() - lastToken.length() - 2);
                        nextJoinType = lastToken;
                    } else {
                        nextJoinType = "";
                    }
                    joinsTable.add(parseJoin(joinBuild.toString(), joinType));
                    joinBuild.setLength(0);
                    joinType = nextJoinType;
                } else {
                    joinBuild.append(token);
                    if (!token.isBlank()) {
                        lastToken = token;
                    }
                    if (!st.hasMoreTokens()) {
                        joinsTable.add(parseJoin(joinBuild.toString(), joinType));
                    }
                }
            }
        } else {
            joinsTable.add(parseJoin(str, type));
        }

        return joinsTable;
    }

    /**
     * Парсинг строки и создание объекта JOIN
     *
     * @param str String
     * @param typeJoin String Тип join запроса: можно передать только сам тип (left, right, ...),
     *                 А можно полностью: LEFT JOIN, RIGHT JOIN, ...
     * @return Join
     */
    private Join parseJoin(String str, String typeJoin) {
        Join join = new Join();
        String type = "";

        if (StringUtils.containsIgnoreCase(typeJoin, QueryElements.JOIN)) {
            String[] typeJoinPaths = typeJoin.toUpperCase().split(QueryElements.JOIN);
            if (typeJoinPaths.length > 0) {
                type = typeJoinPaths[0].trim();
            }
        } else {
            type = typeJoin.trim().toUpperCase();
        }

        switch (type) {
            case QueryElements.FULL:
                join.setFull(true);
                break;
            case QueryElements.LEFT:
                join.setLeft(true);
                break;
            case QueryElements.RIGHT:
                join.setRight(true);
                break;
            case QueryElements.INNER:
                join.setInner(true);
                break;
            default:
                join.setSimple(true);
        }

        String[] joinPaths = str.split("\\s+(?i)on\\s+");

        if (joinPaths.length > 1) {
            join.setOnExpression(parseWhere(joinPaths[1]));
        }

        join.setSource(createSourceFromData(joinPaths[0].trim()));

        return join;
    }
}
