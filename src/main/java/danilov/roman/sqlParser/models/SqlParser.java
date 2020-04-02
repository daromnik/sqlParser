package danilov.roman.sqlParser.models;

import danilov.roman.sqlParser.QueryTypes;
import danilov.roman.sqlParser.exeptions.SqlParseExeption;
import danilov.roman.sqlParser.queries.IQuery;
import org.apache.commons.lang3.StringUtils;

public class SqlParser {

    /**
     * Метод берет первое слово запроса,
     * проверяет на поддерживаемые запросы парсером,
     * создает соответсвующий запросу объект парсера,
     * и запускает процес парсиинга.
     *
     * @param sql String Запрос, который нужно распарсить
     * @return IQuery Возвращается обЪект запроса с разделенными по структуре элментами
     * @throws SqlParseExeption
     */
    public static IQuery parse(String sql) throws SqlParseExeption {

        if (StringUtils.isBlank(sql)) {
            throw new SqlParseExeption("Передан пустой запрос");
        }

        AbstractParseQuery parserQuery = null;

        // Определение типа переданного запроса.
        // Сравнение первого слова запроса с теми, что допущенны в программе.
        // Если есть совпадение, то создаем соотуветсвующий класс для парсинга.
        for (QueryTypes type: QueryTypes.values()) {
            if (StringUtils.startsWithIgnoreCase(sql, type.name())) {
                switch (type) {
                    case SELECT:
                        parserQuery = new ParseSelectQuery();
                        break;
//                    case INSERT:
//                        parserQuery = new ParseInsertQuery();
//                        break;
//                    case UPDATE:
//                        parserQuery = new ParseUpdateQuery();
//                        break;
//                    case DELETE:
//                        parserQuery = new ParseDeleteQuery();
//                        break;

                }
            }
        }

        if (parserQuery == null) {
            throw new SqlParseExeption("В строке не нашлось известных запросов!");
        }

        return parserQuery.parse(sql);
    }

}
