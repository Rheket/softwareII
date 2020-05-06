package Model;

import DAO.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Customer {

    private int customerId = 0;
    private String customerName = null;

    private String customerAddress = null;
    private int customerActive = 0;

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public Customer() {
    }

    public int getCustomerId() {

        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerActive(int customerActive) {
        this.customerActive = customerActive;
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static int getCustomerAppId(String cName) {

        int appointmentCustomerId = 0;

        try {

            String sqlStatement = "SELECT * FROM customer WHERE customerName = '" + cName + "'";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                appointmentCustomerId = result.getInt("customerId");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentCustomerId;
    }


}
