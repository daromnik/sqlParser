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

        if (StringUtils.isBlank(sql)) {
            throw new SqlParseExeption("Передан пустой запрос");
        }

        IQuery query = null;

        // Определение типа переданного запроса.
        // Сравнение первого слова запроса с тему, что допущенны в программе.
        // Если есть совпадение, то находим одноименный класс и создаем его объект.
        for (QueryTypes type: QueryTypes.values()) {
            if (StringUtils.startsWithIgnoreCase(sql, type.name())) {
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
