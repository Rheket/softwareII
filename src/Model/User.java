package Model;

import DAO.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

    private static String username;

    public User() {}

    public static void setUsername(String usr) {
        username = usr;
    }

    public static String getUsername() {
        return username;
    }

    public static int getUserId(String usr) {

        int appointmentUserId = 0;

        try {

            String sqlStatement = "SELECT * FROM user WHERE userName = '" + usr + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                int userAppId = result.getInt("userId");
                System.out.println(userAppId);
                appointmentUserId = userAppId;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentUserId;
    }



}
