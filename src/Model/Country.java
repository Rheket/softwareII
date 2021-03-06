package Model;

import DAO.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Country {

    public static ObservableList<String> associatedCountry = FXCollections.observableArrayList();

    public static String delCity;

    public static int getAssociatedCountryId(String c) {

        int theId = 0;

        try {

            String sqlStatement = "SELECT countryId from country where country='" + c + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                theId = result.getInt("countryId");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return theId;
    }

    public static void setCountrySelections() {

        associatedCountry.clear();
        try {

            String sqlStatement = "SELECT * FROM country";

            Statement stmt = DBConnection.conn.createStatement();

            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getCountry = result.getString("country");
                associatedCountry.add(getCountry);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getCountryIdCounter() {

        int counter = 0;

        try {

            String sqlStatement = "SELECT * FROM country";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                counter = result.getInt("countryId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counter;

    }

    public static boolean checkCountryDel(int cId) {

        Boolean countryCheck = false;

        // check if city is associated with an address
        try {

            String sqlStatement = "SELECT city FROM city WHERE countryId=" + cId;
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while(result.next()) {

                countryCheck = true;
                delCity = result.getString("city");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countryCheck;

    }

}
