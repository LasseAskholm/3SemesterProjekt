package Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataMain {

    // singeltion instance
    private static DataMain instance;


    // database setup online
    /*
    private String url = "hattie.db.elephantsql.com";
    private int port = 5432;
    private String databaseName = "rneumnoj";
    private String username = "rneumnoj";
    private String password = "d8DQNzagLUSxnbAE-W709BxmR86KKTj-";
    */

    // localhost
    private String url = "localhost";
    private int port = 3306;
    private String databaseName = "db";
    private String username = "root";
    private String password = "secret";

    private static Connection connection = null;

    private DataMain() throws SQLException {
        initializePostgresqlDatabase();
    }

    public static DataMain getInstance() throws SQLException {
        if (instance == null) {
            instance = new DataMain();
        }
        return instance;
    }

    private void initializePostgresqlDatabase() throws SQLException {
        try {
            //DriverManager.registerDriver(new org.postgresql.Driver());
            //mysql - Localhost
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + databaseName, username, password);

            //postgres - Online
            //connection = DriverManager.getConnection("jdbc:postgresql://" + url + ":" + port + "/" + databaseName, username, password);
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) System.exit(-1);
        }
    }

    private static void testInsert(String s) throws SQLException {


        try{
            connection.setAutoCommit(false);

            String test = "INSERT INTO java_tests(tests) values(\"test1\")";
            PreparedStatement prep = connection.prepareStatement(test);
            prep.execute();
            prep.close();

            connection.commit();
        }catch (Exception e){
            System.out.println("caught");
            e.printStackTrace();
        }


    }
    public static void main(String[] args) throws SQLException {

        DataMain db = DataMain.getInstance();

        testInsert("s");


    }


   /* public List<String> getGenres(int prod_id) {

        List<String> returnList = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT genre " +
                            "FROM genre "+
                            "JOIN genres_production_association ON genre.id = genres_production_association.genre_id " +
                            "WHERE production_id = ?;");
            stmt.setInt(1, prod_id);

            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()) {
                returnList.add(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnList;
    }*/
}
