package danilov.roman.sqlParser.queries;

import danilov.roman.sqlParser.expressions.Expression;
import danilov.roman.sqlParser.paths.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Select implements IQuery {

    private List<Column> selectColumns;
    private Source from;
    private List<Join> joins = new ArrayList<>();
    private Expression whereClauses;
    private Expression havingClauses;
    private List<Column> groupByColumns;
    private List<Sort> sortColumns;
    private Limit limit;
    private Offset offset;

    public Select() {
    }

    public List<Column> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<Column> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public Source getFrom() {
        return from;
    }

    public void setFrom(Source from) {
        this.from = from;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public void addJoins(List<Join> joins) {
        this.joins.addAll(joins);
    }

    public Expression getWhereClauses() {
        return whereClauses;
    }

    public void setWhereClauses(Expression whereClauses) {
        this.whereClauses = whereClauses;
    }

    public Expression getHavingClauses() {
        return havingClauses;
    }

    public void setHavingClauses(Expression havingClauses) {
        this.havingClauses = havingClauses;
    }

    public List<Column> getGroupByColumns() {
        return groupByColumns;
    }

    public void setGroupByColumns(List<Column> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public List<Sort> getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(List<Sort> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

    public String toString() {
        return "SELECT " +
                selectColumns.stream().map(Column::toString).collect(Collectors.joining(", ")) +
                " FROM " + from + " " +
                joins.stream().map(Join::toString).collect(Collectors.joining(" ")) +
                (groupByColumns != null ? " GROUP BY " + groupByColumns.stream().map(Column::toString).collect(Collectors.joining(", ")) : "") +
                (havingClauses != null ? " HAVING " + havingClauses : "") +
                (whereClauses != null ? " WHERE " + whereClauses : "") +
                (sortColumns != null ? " " + sortColumns.stream().map(Sort::toString).collect(Collectors.joining(", ")) : "") +
                (limit != null ? " " + limit : "") +
                (offset != null ? " " + offset : "");
    }
}
