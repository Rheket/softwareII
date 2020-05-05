package Model;

import DAO.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address {

    private static int addressId;
    private int cityId;
    private int zipCode;
    private String phone;
    private static String addressName;
    private static String zip;

    public static String customerAddress;

    public static ObservableList<String> associatedAddress = FXCollections.observableArrayList();
    public static ObservableList<String> associatedZipCode = FXCollections.observableArrayList();
    public static ObservableList<String> associatedPhone = FXCollections.observableArrayList();



    public static int getAddressId(String aName) {

        try {

            String sqlStatement = "SELECT * FROM address WHERE address ='" + aName + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                addressId = Integer.parseInt(result.getString("addressId"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressId;
    }

    public static String getAddressName(int addressId) {

        try {

            String sqlStatement = "SELECT * FROM address WHERE addressId='" + addressId + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                addressName = result.getString("address");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressName;

    }

    public static String getZip(int addressZip) {

        try {

            String sqlStatement = "SELECT * FROM address WHERE addressId='" + addressZip + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                zip = result.getString("postalCode");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zip;
    }

    public static void setAddressSelections() {

        associatedAddress.clear();
        try {

            String sqlStatement = "SELECT * FROM address";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getAddress = result.getString("address");
                associatedAddress.add(getAddress);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setZipSelections() {

        associatedZipCode.clear();
        try {

            String sqlStatement = "SELECT * FROM address";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getZip = result.getString("postalCode");
                associatedZipCode.add(getZip);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPhoneSelections() {

        associatedPhone.clear();
        try {

            String sqlStatement = "SELECT * FROM address";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getPhone = result.getString("phone");
                associatedPhone.add(getPhone);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAddressIdCounter() {


        int counter = 0;

        try {

            String sqlStatement = "SELECT * FROM address";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                counter = result.getInt("addressId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counter;

    }

    public static boolean checkAddressDel(int aId) {

        Boolean addressCheck = false;

        // check if city is associated with an address
        try {

            String sqlStatement = "SELECT customerName FROM customer WHERE addressId=" + aId;
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while(result.next()) {

                addressCheck = true;
                customerAddress = result.getString("customerName");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressCheck;

    }

}
