package danilov.roman.sqlParser.queries;

import org.apache.commons.lang3.StringUtils;

public class AbstractQuery {

    protected String fromWord = "FROM";
    protected String leftJoinWord = "LEFT JOIN";
    protected String rightJoinWord = "RIGHT JOIN";
    protected String groupByWord = "GROUP BY";
    protected String orderByWord = "ORDER BY";
    protected String limitWord = "LIMIT";
    protected String whereWord = "WHERE";
    protected String offsetWord = "OFFSET";

    /**
     * Возвращает строку, заключенную между двумя переданными словами
     *
     * @param str String
     * @param open String
     * @param close String
     * @return String
     */
    protected String substringBetween(String str, String open, String close) {
        if (str != null && open != null && close != null) {
            int start = StringUtils.indexOfIgnoreCase(str, open, 0);
            if (start != -1) {
                int end = StringUtils.indexOfIgnoreCase(str, close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }
            return null;
        } else {
            return null;
        }
    }

}
