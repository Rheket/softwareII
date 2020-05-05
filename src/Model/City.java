package Model;

import DAO.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class City {

    private static int cityId;
    private int countryId;
    private static String cityName;

    public static String delAddress;

    public static ObservableList<String> associatedCity = FXCollections.observableArrayList();

    public static int getCityId(String cName) {

        try {

            String sqlStatement = "SELECT * FROM city WHERE city ='" + cName + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                cityId = result.getInt("cityId");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public static String getCityName(int cityId) {

        try {

            String sqlStatement = "SELECT * FROM city WHERE cityId='" + cityId + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                cityName = result.getString("city");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityIdCounter() {

        int counter = 0;

        try {

            String sqlStatement = "SELECT * FROM city";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                counter = result.getInt("cityId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counter;

    }

    public static void setCitySelections() {

        associatedCity.clear();
        try {

            String sqlStatement = "SELECT * FROM city";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getCity = result.getString("city");
                associatedCity.add(getCity);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkCityDel(int cId) {

        Boolean cityCheck = false;

        // check if city is associated with an address
        try {

            String sqlStatement = "SELECT address FROM address WHERE cityId=" + cId;
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while(result.next()) {

                cityCheck = true;
                delAddress = result.getString("address");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cityCheck;

    }

}
