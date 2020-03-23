package danilov.roman.sqlParser;

public class Main {

    public static void main(String[] args) {
        System.out.println("test");

//        String sql = "Select author.name, count(book.id) as cbi, sum(book.cost), ph.* \n" +
//                "FROM author, author1, author2 \n" +
//                "LEFT JOIN book ON (author.id = book.author_id) \n" +
//                "RIGHT JOIN publishing_house as ph ON (author.id = ph.author_id) \n" +
//                "GROUP BY author.name, author.family \n" +
//                "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500\n" +
//                "LIMIT 10;";

        String sql = "Select * FROM users where id = 1;";

        try {

            SqlParser.parse(sql);

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
