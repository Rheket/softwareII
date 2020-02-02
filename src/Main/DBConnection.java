package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String databaseName = "U060YT";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/U060YT";
    private static final String username = "U060YT";
    private static final String password = "53688667824";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;

    public static void makeConnection() throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        conn = DriverManager.getConnection(DB_URL, username, password);
        System.out.println("Connection successful");

    }

    public static void closeConnection() throws SQLException {

        conn.close();
        System.out.println("Connection closed");

    }


}
