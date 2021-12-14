package Data;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

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
    public Map<String,Integer> sendValues() throws SQLException {
        Map<String,Integer> data = new HashMap<>();
        String read = "SELECT product_id, amount, speed FROM newbatches";
        PreparedStatement prep = connection.prepareStatement(read);
        ResultSet resultSet = prep.executeQuery();
        while(resultSet.next()){
            data.put("product_id",resultSet.getInt(1));
            data.put("amount",resultSet.getInt(2));
            data.put("speed",resultSet.getInt(3));
        }
        prep.close();
        return  data;
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
    public String getCommand() throws SQLException {
        try{
            String selectStmt="SELECT command from commands";
            PreparedStatement pStmt=connection.prepareStatement(selectStmt);
            ResultSet resultSet = pStmt.executeQuery();
            resultSet.next();

            String returnString=resultSet.getString(1);
            String deleteString ="TRUNCATE TABLE commands";
            PreparedStatement pStmt2= connection.prepareStatement(deleteString);
            pStmt2.execute();
            return returnString;
        }catch (SQLException e){
            return null;

        }

    }
    public static void main(String[] args) throws SQLException {



    }

    public void liveUpdate(Map<String, String> map) throws SQLException {

        connection.setAutoCommit(false);

        //YYYY-MM-DD HH:MM:SS

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        /* + now + ", " + now + ", "+ */


        try {
            PreparedStatement prepStmt = connection.prepareStatement(
                    "INSERT INTO live_batches(created_at, updated_at, prod_processed_count, prod_defective_count, mach_speed, humidity, temperature, vibration, productID) " +
                        "values (?, ?, " + map.get("prod_processed_count") + ", " + map.get("prod_defective_count") + ", " + map.get("mach_speed") + ", " + map.get("humidity") + ", " + map.get("temperature") + ", " + map.get("vibration") +", ? "+")");

            prepStmt.setTimestamp(1, timestamp);
            prepStmt.setTimestamp(2, timestamp);
            int productID=(int)Double.parseDouble(map.get("productID"));
            productID+=10;
            prepStmt.setInt(3, productID);
            int row = prepStmt.executeUpdate();


            prepStmt.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        connection.commit();

    }

    public void updateInventory(Map<String, Float> map) throws SQLException {

        connection.setAutoCommit(false);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try {
            PreparedStatement prepStmt = null;

            prepStmt = connection.prepareStatement("UPDATE ingredients SET amount = ?, updated_at = ? WHERE product = ?");

            prepStmt.setFloat(1, map.get("Barley"));
            prepStmt.setTimestamp(2, timestamp);
            prepStmt.setString(3, "Barley");
            prepStmt.addBatch();

            prepStmt.setFloat(1, map.get("Hops"));
            prepStmt.setTimestamp(2, timestamp);
            prepStmt.setString(3, "Hops");
            prepStmt.addBatch();

            prepStmt.setFloat(1, map.get("Malt"));
            prepStmt.setTimestamp(2, timestamp);
            prepStmt.setString(3, "Malt");
            prepStmt.addBatch();

            prepStmt.setFloat(1, map.get("Wheat"));
            prepStmt.setTimestamp(2, timestamp);
            prepStmt.setString(3, "Wheat");
            prepStmt.addBatch();

            prepStmt.setFloat(1, map.get("Yeast"));
            prepStmt.setTimestamp(2, timestamp);
            prepStmt.setString(3, "Yeast");
            prepStmt.addBatch();

            prepStmt.executeBatch();

            prepStmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        connection.commit();

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
