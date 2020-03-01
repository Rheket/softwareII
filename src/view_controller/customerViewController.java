package view_controller;

import DAO.DBConnection;
import Model.City;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class customerViewController implements Initializable {

    public ObservableList<String> associatedCountry = FXCollections.observableArrayList();
    public ObservableList<String> associatedCity = FXCollections.observableArrayList();
    public ObservableList<String> associatedAddress = FXCollections.observableArrayList();
    public ObservableList<String> associatedZipCode = FXCollections.observableArrayList();

    @FXML private TextField nameTextField;
    @FXML private ComboBox countryComboBox;
    @FXML private ComboBox cityComboBox;
    @FXML private ComboBox addressComboBox;
    @FXML private ComboBox zipComboBox;

    public void mainMenu(ActionEvent actionEvent) throws IOException {

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

    // Add update delete customer records
    public void addCustomerRecord() {

        String name = nameTextField.getText();
        String country = String.valueOf(countryComboBox.getSelectionModel().getSelectedItem());
        String city = String.valueOf(cityComboBox.getSelectionModel().getSelectedItem());
        String address = String.valueOf(addressComboBox.getSelectionModel().getSelectedItem());
        String zipCode = String.valueOf(zipComboBox.getSelectionModel().getSelectedItem());

        int cityId;

        for (int i = 0; i < 3; i++) {

            if (city.equals("123 Main")) {
                cityId = 1;
            } else if (city.equals("123 Elm")) {
                cityId = 2;
            } else {
                cityId = 3;
            }

        }


        Timestamp ts = new Timestamp(System.currentTimeMillis());

        int customerId = 4;

        try {
            DBConnection.makeConnection();
            Statement stmt = DBConnection.conn.createStatement();
            String sqlStatement = "INSERT INTO `customer` VALUES (" + customerId + ", '" + name + "'," + address + ",1,'" + ts + "','test','" + "test','2019-01-01 00:00:00','test')";

            stmt.executeUpdate(sqlStatement);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    /*
    FIXME Write code to add customer data to the SQL database
     */

    }

    public void updateCustomerRecord() {

        String name = nameTextField.getText();
        String country = String.valueOf(countryComboBox.getSelectionModel().getSelectedItem());
        String city = String.valueOf(cityComboBox.getSelectionModel().getSelectedItem());
        String address = String.valueOf(addressComboBox.getSelectionModel().getSelectedItem());
        String zipCode = String.valueOf(zipComboBox.getSelectionModel().getSelectedItem());

        /*
    FIXME Write code to UPDATE customer data to the SQL database
     */

    }

    public void deleteCustomerRecord() {

        String name = nameTextField.getText();
        String country = String.valueOf(countryComboBox.getSelectionModel().getSelectedItem());
        String city = String.valueOf(cityComboBox.getSelectionModel().getSelectedItem());
        String address = String.valueOf(addressComboBox.getSelectionModel().getSelectedItem());
        String zipCode = String.valueOf(zipComboBox.getSelectionModel().getSelectedItem());

    /*
    FIXME Write code to DELETE customer data to the SQL database
     */

    }

    public void getCountrySelections () {

        try {

            String sqlStatement = "SELECT * FROM country";

            Statement stmt = DBConnection.conn.createStatement();

            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                String getCountry = result.getString("country");
                associatedCountry.add(getCountry);
                countryComboBox.setItems(associatedCountry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getCitySelections () {

        try {

            String sqlStatement = "SELECT * FROM city";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getCity = result.getString("city");
                associatedCity.add(getCity);
                cityComboBox.setItems(associatedCity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAddressSelections () {

        try {

            String sqlStatement = "SELECT * FROM address";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getAddress = result.getString("Address");

                associatedAddress.add(getAddress);

                addressComboBox.setItems(associatedAddress);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getZipSelections() {

        try {

            String sqlStatement = "SELECT * FROM address";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getZip = result.getString("postalCode");
                associatedZipCode.add(getZip);

                zipComboBox.setItems(associatedZipCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize (URL url, ResourceBundle rb) {

        getCountrySelections();
        getAddressSelections();
        getCitySelections();
        getZipSelections();

    }

}