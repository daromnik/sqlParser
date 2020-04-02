package danilov.roman.sqlParser.models;

import danilov.roman.sqlParser.queries.IQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class AbstractParseQuery {

    protected final String fromWord = "FROM";
    protected final String joinWord = "JOIN";
    protected final String leftJoinWord = "LEFT JOIN";
    protected final String rightJoinWord = "RIGHT JOIN";
    protected final String innerJoinWord = "INNER JOIN";
    protected final String fullJoinWord = "FULL JOIN";
    protected final String groupByWord = "GROUP BY";
    protected final String orderByWord = "ORDER BY";
    protected final String limitWord = "LIMIT";
    protected final String whereWord = "WHERE";
    protected final String havingWord = "HAVING";
    protected final String offsetWord = "OFFSET";

    abstract IQuery parse(String sql);

    protected Map<String, Integer> orderElementsInQuery = new HashMap<>(){
    	{
    		put(fromWord, 0);
    		put(joinWord, 0);
    		put(leftJoinWord, 0);
    		put(rightJoinWord, 0);
    		put(innerJoinWord, 0);
    		put(fullJoinWord, 0);
    		put(groupByWord, 0);
    		put(orderByWord, 0);
    		put(limitWord, 0);
    		put(whereWord, 0);
    		put(havingWord, 0);
    		put(offsetWord, 0);
    	}
    };

    /**
     * В переданном запросе ищем служебные слова разделители,
     * оставляем в map те которые есть в строке и
     * сортируем по индексу их нахождения в строке
     *
     * @param str String Строка с запросом
     * @return String
     */
    protected List<String> findAllElementsInQuery(String str) {
            return orderElementsInQuery.entrySet().stream()
                .peek(element -> element.setValue(StringUtils.indexOfIgnoreCase(str, element.getKey(), 0)))
                .filter(element -> element.getValue() > 0)
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * Возвращает строку, заключенную между двумя переданными словами
     *
     * @param str String Строка с запросом
     * @param open String Элемент от которого начинать обрезку строки
     * @param close String Элемент до которого обрезать строку | Если null - то до конца строки
     * @return String
     */
    protected String substringBetween(String str, String open, String close) {
        if (str != null && open != null) {
            int start = StringUtils.indexOfIgnoreCase(str, open, 0);
            if (start != -1) {
                if (close != null) {
                    int end = StringUtils.indexOfIgnoreCase(str, close, start + open.length());
                    if (end != -1) {
                        return str.substring(start + open.length(), end);
                    }
                } else {
                    return str.substring(start + open.length());
                }
            }
            return null;
        } else {
            return null;
        }
    }

}
