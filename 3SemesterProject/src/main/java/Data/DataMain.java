package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataMain {

    // singeltion instance
    private static DataMain instance;

    // database setup
    private String url = "hattie.db.elephantsql.com";
    private int port = 5432;
    private String databaseName = "rneumnoj";
    private String username = "rneumnoj";
    private String password = "d8DQNzagLUSxnbAE-W709BxmR86KKTj-";

    private Connection connection = null;

    private DataMain() {
        initializePostgresqlDatabase();
    }

    public static DataMain getInstance() {
        if (instance == null) {
            instance = new DataMain();
        }
        return instance;
    }

    private void initializePostgresqlDatabase() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://" + url + ":" + port + "/" + databaseName, username, password);
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) System.exit(-1);
        }
    }

    public static void main(String[] args) {
        DataMain db = DataMain.getInstance();

    }
}
