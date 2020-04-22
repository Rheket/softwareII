package view_controller;

import DAO.DBConnection;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.Database.*;
import static Model.Customer.allCustomers;
import static Model.Customer.getAllCustomers;

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
    @FXML private Button saveNewButton;
    @FXML private Button saveEditButton;

    //editing labels
    @FXML private Label editLabel;
    @FXML private Label editName;
    @FXML private Label editCountry;
    @FXML private Label editZip;
    @FXML private Label editAddress;
    @FXML private Label editCity;

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> idColumn;

    String name = "";
    String country = "";
    String city = "";
    String address = "";
    String zipCode = "";

    public static int idCounter = 0;

    Boolean toggleEditButton = false;
    Boolean toggleNewButton = true;

    public void mainMenu(ActionEvent actionEvent) throws IOException {

        //clear the table first
        customerTableView.getItems().clear();
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

    // Add update delete customer records
    public void addCustomerRecord() {

        if(nameTextField.getText().trim().equals("") | addressComboBox.getValue() == null | cityComboBox.getValue() == null | countryComboBox.getValue() == null | zipComboBox.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding Customer");
            alert.setContentText("Please make sure all fields are filled");

            alert.showAndWait();

        } else {
            name = nameTextField.getText();
            country = String.valueOf(countryComboBox.getSelectionModel().getSelectedItem());
            city = String.valueOf(cityComboBox.getSelectionModel().getSelectedItem());
            address = String.valueOf(addressComboBox.getSelectionModel().getSelectedItem());
            zipCode = String.valueOf(zipComboBox.getSelectionModel().getSelectedItem());

            int addressId = getAddressId(address);

            Timestamp ts = new Timestamp(System.currentTimeMillis());

            idCounter++;

            try {
                //DBConnection.makeConnection();
                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "INSERT INTO `customer` VALUES (" + idCounter + ", '" + name + "'," + addressId + ",1,'" + ts +  "','test','2019-01-01 00:00:00','test')";

                stmt.executeUpdate(sqlStatement);
                //System.out.println(sqlStatement);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //clear table and update with new customers
        }
        customerTableView.getItems().clear();
        initializeCustomers();

    }

    public void editCustomerRecord() {

        if (customerTableView.getSelectionModel().getSelectedItem() != null) {

            int selectedCustomerId = customerTableView.getSelectionModel().getSelectedItem().getCustomerId();

            try {

                String sqlStatement = "SELECT * FROM customer WHERE customerId=" + selectedCustomerId;
                Statement stmt = DBConnection.conn.createStatement();
                ResultSet result = stmt.executeQuery(sqlStatement);

                while (result.next()) {

                    String cName = result.getString("customerName");
                    int cAddressId = result.getInt("addressId");

                    String cCountry = getCountryName(cAddressId);
                    String cCity = getCityName(cAddressId);
                    String cAddress = getAddressName(cAddressId);
                    String cZip = getZip(cAddress);

                    editLabel.setText("Use left options to edit " + cName);
                    editName.setText(cName);
                    editAddress.setText(cAddress);
                    editCity.setText(cCity);
                    editCountry.setText(cCountry);
                    editZip.setText(cZip);



                }
                toggleEditButton = true;
                toggleNewButton = false;

                setButtons();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Customer selected");
            alert.setContentText("A customer must be selected to modify");

            alert.showAndWait();

        }


    }

    public void saveCustomerRecord() {

        if (customerTableView.getSelectionModel().getSelectedItem() != null) {

            if (nameTextField.getText().trim().equals("") | addressComboBox.getValue() == null | cityComboBox.getValue() == null | countryComboBox.getValue() == null | zipComboBox.getValue() == null) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Error editing Customer");
                alert.setContentText("Please make sure all fields are filled");

                alert.showAndWait();

            } else {

                name = nameTextField.getText();
                country = String.valueOf(countryComboBox.getSelectionModel().getSelectedItem());
                city = String.valueOf(cityComboBox.getSelectionModel().getSelectedItem());
                address = String.valueOf(addressComboBox.getSelectionModel().getSelectedItem());
                zipCode = String.valueOf(zipComboBox.getSelectionModel().getSelectedItem());
                int selectedCustomerId = customerTableView.getSelectionModel().getSelectedItem().getCustomerId();

                int addressId = getAddressId(address);

                //User usr = new User();
                String theUser = User.getUsername();
                Timestamp ts = new Timestamp(System.currentTimeMillis());

                try {

                    String sqlStatement = "UPDATE customer SET customerName='" + name + "', addressId=" + addressId + ", active=1," + " lastUpdate='" + ts + "', lastUpdateBy='" + theUser + "' WHERE customerId=" + selectedCustomerId;
                    Statement stmt = DBConnection.conn.createStatement();
                    stmt.executeUpdate(sqlStatement);

                    editLabel.setText("");
                    editName.setText("");
                    editAddress.setText("");
                    editCity.setText("");
                    editCountry.setText("");
                    editZip.setText("");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Customer selected");
            alert.setContentText("A customer must be selected to modify");

            alert.showAndWait();

        }
        //clear table and update with new customers
        customerTableView.getItems().clear();
        initializeCustomers();

    }

    public void deleteCustomerRecord() {

        if(customerTableView.getSelectionModel().getSelectedItem() != null) {

            int delCustomer = customerTableView.getSelectionModel().getSelectedItem().getCustomerId();
            String name = customerTableView.getSelectionModel().getSelectedItem().getCustomerName();

            try {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete the Customer " + name + "?");

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();

                //process delete
                if (result.orElse(noButton) == yesButton) {

                    String sqlStatement = "DELETE FROM customer WHERE customerID=" + delCustomer;
                    Statement stmt = DBConnection.conn.createStatement();
                    stmt.executeUpdate(sqlStatement);

                }

                //System.out.println(sqlStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            initIdCounter();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Customer selected");
            alert.setContentText("A Customer must be selected to modify");

            alert.showAndWait();

        }

        //clear table and update with new customers
        customerTableView.getItems().clear();
        initializeCustomers();

    }

    public void setCountrySelections() {

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

    public void setCitySelections() {

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

    public void setAddressSelections() {

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

    public static void initializeCustomers () {

        Statement stmt = null;

        try {
            stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM customer";
            ResultSet result = stmt.executeQuery(sqlStatement);

            //ResourceBundle rb = null;

            while (result.next()) {

                Customer customer = new Customer();

                customer.setCustomerId(result.getInt("customerId"));
                customer.setCustomerName(result.getString("customerName"));
                customer.setCustomerAddress(result.getString("addressId"));
                customer.setCustomerActive(result.getInt("active"));

                allCustomers.add(customer);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    private void initIdCounter() {

        try {
            Statement stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM customer";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                idCounter = result.getInt("customerId");
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    private void setButtons () {

        saveNewButton.setVisible(toggleNewButton);
        saveEditButton.setVisible(toggleEditButton);
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {


        setCountrySelections();
        setAddressSelections();
        setCitySelections();
        getZipSelections();

        initializeCustomers();
        initIdCounter();
        setButtons();

        customerTableView.setItems(getAllCustomers());
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

    }

}