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
    public Map<String,Integer> sendValues(int id) throws SQLException {
        Map<String,Integer> data = new HashMap<>();
        String read = "SELECT product_id, amount, speed, batchID FROM newbatches WHERE batchID = ?";
        PreparedStatement prep = connection.prepareStatement(read);
        prep.setInt(1, id);
        ResultSet resultSet = prep.executeQuery();
        while(resultSet.next()){
            data.put("product_id",resultSet.getInt(1));
            data.put("amount",resultSet.getInt(2));
            data.put("speed",resultSet.getInt(3));
            data.put("batchID",resultSet.getInt(4));
        }
        prep.close();
        return data;
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
    public Map<String, String> getCommand() throws SQLException {
        try{
            String selectStmt="SELECT command, batchID from commands";
            PreparedStatement pStmt=connection.prepareStatement(selectStmt);
            ResultSet resultSet = pStmt.executeQuery();
            resultSet.next();

            String command=resultSet.getString(1);
            String batchID=resultSet.getString(2);

            Map<String, String> map = new HashMap<>();
            map.put("command", command);
            map.put("batchID", batchID);

            String deleteString ="TRUNCATE TABLE commands";
            PreparedStatement pStmt2= connection.prepareStatement(deleteString);
            pStmt2.execute();

            return map;
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
                    "INSERT INTO live_batches(created_at, updated_at, prod_processed_count, prod_defective_count, mach_speed, humidity, temperature, vibration, productID, stateID, batchID) " +
                        "values (?, ?, " + map.get("prod_processed_count") + ", " + map.get("prod_defective_count") + ", " + map.get("mach_speed") + ", " + map.get("humidity") + ", " + map.get("temperature") + ", " + map.get("vibration") +", ?, ?, ? "+")");

            prepStmt.setTimestamp(1, timestamp);
            prepStmt.setTimestamp(2, timestamp);
            int productID=(int)Double.parseDouble(map.get("productID"));
            if(productID == -1){
                return;
            }
            prepStmt.setInt(3, productID);
            int stateID=(int)Double.parseDouble(map.get("stateID"));
            prepStmt.setInt(4, stateID);
            int batchID=(int)Double.parseDouble(map.get("batchID"));
            prepStmt.setInt(5, batchID);


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

    public boolean makeBatchReport(Map<String, String> batchDetails) throws SQLException {

        Float amount = Float.parseFloat(batchDetails.get("amount"));
        String batchID = batchDetails.get("batchID");
        Float machspeed = Float.parseFloat(batchDetails.get("machspeed"));
        String productID = batchDetails.get("productID");

        //total time spent in seconds
        Float totalTime = (amount/machspeed) * 60;

        //1 entry every 5 seconds
        Float numberOfEntries = totalTime/2;

        Float count = (float) countRows();

        Float step = count/10;


        for (int i = 0; i < 10; i++) {

            //average
            Map<String, String> map = readTen(i*step, (i*step)+step);

            //report
            //id	created_at	updated_at	productID	prod_processed_count	prod_defective_count	mach_speed	humidity	temperature	vibration	batchID
            writeTen(map, machspeed, productID, batchID);
        }

        return true;
    }

    private int countRows() throws SQLException {
        String selectStmt = "select count(prod_processed_count) from live_batches";
        PreparedStatement pStmt=connection.prepareStatement(selectStmt);
        ResultSet result = pStmt.executeQuery();
        if(result.next()){
            return result.getInt(1);
        }
        return 0;
    }

    private void writeTen(Map<String, String> map, Float machspeed, String productID, String batchID) throws SQLException {

        connection.setAutoCommit(false);

        Timestamp timestamp = Timestamp.valueOf(map.get("time"));

        PreparedStatement prepStmt = connection.prepareStatement(
                "INSERT INTO batch_report(created_at, updated_at, prod_processed_count, prod_defective_count, mach_speed, humidity, temperature, vibration, productID, batchID) " +
                        "values (?, ?, ?, ?"  +  ", ? "  + ", " + map.get("humidity") + ", " + map.get("temperature") + ", " + map.get("vibration") +", ?, ? "+")");

        prepStmt.setTimestamp(1, timestamp);
        prepStmt.setTimestamp(2, timestamp);
        prepStmt.setString(3, map.get("processed"));
        prepStmt.setString(4, map.get("defective"));
        prepStmt.setString(5, String.valueOf(machspeed));
        prepStmt.setString(6, productID);
        int batch = (int) Double.parseDouble(batchID);
        prepStmt.setInt(7, batch);

        int row = prepStmt.executeUpdate();

        prepStmt.close();

        connection.commit();
    }

    private Map<String, String> readTen(Float start, Float stop) throws SQLException {

        // read where id > star && id < stop

        ArrayList<String> defective = new ArrayList<>(10);
        ArrayList<String> humidity = new ArrayList<>(10);
        ArrayList<String> temperature = new ArrayList<>(10);
        ArrayList<String> vibration = new ArrayList<>(10);


        for (double i = Math.floor(start) ; i <= Math.floor(stop); i++) {
            String selectStmt="SELECT prod_defective_count, humidity, temperature, vibration from live_batches where id = ?";
            PreparedStatement pStmt=connection.prepareStatement(selectStmt);
            pStmt.setInt(1, (int) i);
            ResultSet resultSet = pStmt.executeQuery();
            if(resultSet.next()){
                defective.add(resultSet.getString(1));
                humidity.add(resultSet.getString(2));
                temperature.add(resultSet.getString(3));
                vibration.add(resultSet.getString(4));
            }
            pStmt.close();
        }

        // average

        double sumDefective = defective.stream().mapToDouble(Double::parseDouble).sum();
        double avgHumidity = humidity.stream().mapToDouble(Double::parseDouble).average().orElse(0.0);
        double avgTemp = temperature.stream().mapToDouble(Double::parseDouble).average().orElse(0.0);
        double avgVibration = vibration.stream().mapToDouble(Double::parseDouble).average().orElse(0.0);
        double processedCount = 0;
        Timestamp time = new Timestamp(System.currentTimeMillis());

        String selectStmt = "SELECT prod_processed_count, created_at from live_batches where id = ?";
        PreparedStatement pStmt=connection.prepareStatement(selectStmt);
        pStmt.setInt(1, (int) Math.floor(stop));
        ResultSet resultSet = pStmt.executeQuery();
        if(resultSet.next()){
            processedCount = Double.parseDouble(resultSet.getString(1));
            time = resultSet.getTimestamp(2);
            System.out.println(processedCount +" at: " + (int) Math.floor(stop));
        }else{
            System.out.println("nothing in set at: " + (int) Math.floor(stop));
        }
        pStmt.close();


        Map<String, String> map = new HashMap<>();
        map.put("defective", String.valueOf(sumDefective));
        map.put("humidity", String.valueOf(avgHumidity));
        map.put("temperature", String.valueOf(avgTemp));
        map.put("vibration", String.valueOf(avgVibration));
        map.put("processed", String.valueOf(processedCount));
        map.put("time", time.toString());


        return map;

        //return map : prod_processed_count(Largest)	prod_defective_count(sum of all) 	humidity(average)	temperature(average)	vibration(average)

    }

    public void resetTable(String tableName) throws SQLException {

        String deleteString ="TRUNCATE TABLE "+ tableName;
        PreparedStatement pStmt2= connection.prepareStatement(deleteString);

        pStmt2.execute();

    }

    public void setState(int stateIn) throws SQLException {

        connection.setAutoCommit(false);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String statement = "INSERT INTO current_states(created_at, updated_at, state) VALUES (?,?,?)";


        PreparedStatement pstm = connection.prepareStatement(statement);
        pstm.setTimestamp(1,timestamp);
        pstm.setTimestamp(2,timestamp);
        pstm.setInt(3,stateIn);


        pstm.executeUpdate();
        pstm.close();

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
