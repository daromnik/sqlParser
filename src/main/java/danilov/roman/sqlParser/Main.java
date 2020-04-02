package danilov.roman.sqlParser;

import danilov.roman.sqlParser.exeptions.SqlParseExeption;
import danilov.roman.sqlParser.models.SqlParser;
import danilov.roman.sqlParser.queries.IQuery;

public class Main {

    public static void main(String[] args) {
        System.out.println("test");

        String sql = "Select author.name, count(book.id) as cbi, sum(book.cost), ph.* \n" +
                "FROM author, author1, author2 \n" +
                "JOIN ABC \n" +
                "JOIN cook ON (author2.id = author1.author_id) \n" +
                "LEFT JOIN book ON (author.id = book.author_id) \n" +
                "RIGHT JOIN publishing_house as ph ON author.id = ph.author_id \n" +
                "LEFT JOIN book2 ON author1.id = book2.author_id \n" +
                "GROUP BY author.name, author.family \n" +
                //"ORDER BY author.name ASC \n" +
                "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 \n" +
                "WHERE (a = 1 or p < 1 or COUNT(*) > 5) and b > 100 AND v != 1000 AND c between 1 AND 3 \n" +
                //"WHERE a = 1 and b > 100 and c != 10 \n" +
                "LIMIT 10";

        // SELECT author.name, count(book.i) as cbi, sum(book.cos), ph.*
        // FROM author
        // JOIN author1
        // JOIN author2
        // JOIN ABC
        // JOIN cook ON (author2.id = author1.author_id)
        // LEFT JOIN book ON (author.id = book.author_id)
        // RIGHT JOIN publishing_house as ph ON author.id = ph.author_id
        // LEFT JOIN book2 ON author1.id = book2.author_id
        // GROUP BY author.name, author.family
        // HAVING COUNT(*) > 1 AND SUM(book.cost) > 500
        // WHERE (a = 1 OR p < 1 OR COUNT(*) > 5) AND b > 100 AND v != 1000 AND c BETWEEN 1 AND 3
        // LIMIT 10

        //String sql = "SELECT * FROM book b, (SELECT * FROM worker) AS ACTION where a = 1 and b > 100 LIMIT 5, 10";
        //String sql = "Select author.name AS an, author.sername ass FROM users where id = 1;";
        //String sql = "SELECT * FROM (SELECT * FROM worker) AS ACTION, A, B WHERE ID";

        try {
            IQuery select = SqlParser.parse(sql);
            System.out.println("end");

        } catch (SqlParseExeption e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
