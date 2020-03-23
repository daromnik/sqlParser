package danilov.roman.sqlParser;

import danilov.roman.sqlParser.queries.IQuery;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

public class SqlParser {

    private static final String PACKAGE_QUERIES_CLASSES = IQuery.class.getPackage().getName() + ".";

    public static IQuery parse(String sql)
            throws SqlParseExeption, ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {

        if (sql.length() == 0) {
            throw new SqlParseExeption("Передан пустой запрос");
        }

        IQuery query = null;
        //String[] splitSql = StringUtils.split(sql);

        for (QueryTypes type: QueryTypes.values()) {
            if (StringUtils.startsWithIgnoreCase(sql, type.name())) {
            //if (StringUtils.equalsIgnoreCase(splitSql[0], type.name())) {
                Class<?> clazz = Class.forName(PACKAGE_QUERIES_CLASSES + firstUpperCase(type.name()));
                query = (IQuery)clazz.getDeclaredConstructor().newInstance();
            }
        }

        if (query == null) {
            throw new SqlParseExeption("В строке не нашлось известных запросов!");
        }

        query.parse(sql);

        return query;
    }

    private static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

}
