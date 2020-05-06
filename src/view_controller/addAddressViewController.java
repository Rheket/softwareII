package view_controller;

import DAO.DBConnection;
import Model.Address;
import Model.City;
import Model.Country;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static Model.Address.*;
import static Model.City.*;
import static Model.Country.*;
import static Model.User.getUsername;

public class addAddressViewController implements Initializable {

    //combo boxes
    @FXML private ComboBox cityCB;
    @FXML private ComboBox countryCB;
    @FXML private ComboBox addressCB;

    //text fields
    @FXML private TextField addressTxtField;
    @FXML private TextField cityTxtField;
    @FXML private TextField zipTxtField;
    @FXML private TextField phoneTxtField;
    @FXML private TextField countryTxtField;

    //Radio Buttons
    @FXML private RadioButton newAddressRb;
    @FXML private RadioButton newCountryRb;
    @FXML private RadioButton newCityRb;
    @FXML private RadioButton editAddressRb;
    @FXML private RadioButton editCountryRb;
    @FXML private RadioButton editCityRB;
    @FXML private RadioButton delAddressRb;
    @FXML private RadioButton delCountryRb;
    @FXML private RadioButton delCityRb;

    //buttons
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ToggleGroup toggleGroup;

    private void initializeView(){

        cityCB.getItems().clear();
        countryCB.getItems().clear();
        addressCB.getItems().clear();

        setPhoneSelections();
        setCountrySelections();
        cityCB.setItems(associatedCity);
        countryCB.setItems(associatedCountry);
        addressCB.setItems(associatedAddress);

    }

    private void clearSelections() {

        addressCB.setValue(null);
        countryCB.setValue(null);
        cityCB.setValue(null);


        addressTxtField.setText("");
        countryTxtField.setText("");
        cityTxtField.setText("");
        zipTxtField.setText("");
        phoneTxtField.setText("");

    }
    //handles adding a new city to database
    public void saveNewCity() {

        if(cityTxtField.getText().trim().equals("") || zipTxtField.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new City");
            alert.setContentText("All labels must be filled");

            alert.showAndWait();

        } else if (countryCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new City");
            alert.setContentText("Please select a country from the drop down");

            alert.showAndWait();

        } else {

            City city = new City();

            int cityId = city.getCityIdCounter() + 1;
            String cityName = cityTxtField.getText();
            String countryName = countryCB.getSelectionModel().getSelectedItem().toString();
            int countryId = getAssociatedCountryId(countryName);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String usr = getUsername();

            try{

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "INSERT INTO `city` VALUES (" + cityId + ",'" + cityName + "'," + countryId + ",'" + ts + "','" + usr + "','" + ts + "','" + usr + "')";
                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("New City added");
                alert.setContentText("New City " + cityName + " has been added");
                alert.showAndWait();

                clearSelections();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            cityCB.getItems().clear();
            setCitySelections();
            cityCB.setItems(associatedCity);

        }

    }
    //handles adding a new country to database
    public void saveNewCountry() {

        if(countryTxtField.getText().trim().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new Country");
            alert.setContentText("Country label must not be empty");

            alert.showAndWait();

        } else {

            Country country = new Country();

            int countryId = country.getCountryIdCounter() + 1;
            String countryName = countryTxtField.getText();
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String usr = getUsername();

            try{

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "INSERT INTO `country` VALUES (" + countryId + ",'" + countryName + "','" + ts + "','" + usr + "','" + ts + "','" + usr + "')";
                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("New Country added");
                alert.setContentText("New Country " + countryName + " has been added");

                alert.showAndWait();

                clearSelections();


            } catch (SQLException e) {
                e.printStackTrace();
            }

            countryCB.getItems().clear();
            setCountrySelections();
            countryCB.setItems(associatedCountry);

        }

    }
    //handles adding a new address to database
    public void saveNewAddress() {

        if (addressTxtField.getText().trim().equals("") || zipTxtField.getText().trim().equals("") || phoneTxtField.getText().trim().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new Address");
            alert.setContentText("All labels must be filled");

            alert.showAndWait();

        } else if (cityCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new Address");
            alert.setContentText("Please select a city from the drop down");

            alert.showAndWait();

            } else {

            Address address = new Address();

            int addressId = address.getAddressIdCounter() + 1;
            String addressName = addressTxtField.getText();
            String cityName = cityCB.getSelectionModel().getSelectedItem().toString();
            String zipCode = zipTxtField.getText();
            String phone = phoneTxtField.getText();
            int cityId = City.getCityId(cityName);


            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String usr = getUsername();

            try{

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "INSERT INTO `address` VALUES (" + addressId + ",'" + addressName + "',''," + cityId + ",'" + zipCode + "','" + phone + "','" + ts + "','" + usr + "','" + ts + "','" + usr + "')";

                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("New Address added");
                alert.setContentText("New Address " + addressName + " has been added");
                alert.showAndWait();

                clearSelections();



            } catch (SQLException e) {
                e.printStackTrace();
            }

            addressCB.getItems().clear();
            setAddressSelections();
            addressCB.setItems(associatedAddress);

        }

    }
    //handles updating existing city
    public void saveUpdateCity() {

        if(cityTxtField.getText().trim().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new City");
            alert.setContentText("All labels must be filled");

            alert.showAndWait();

        } else if (countryCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new City");
            alert.setContentText("Please select a country from the drop down");

            alert.showAndWait();

        } else if (cityCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding new City");
            alert.setContentText("Please select a city from the drop down");

            alert.showAndWait();

        } else {

            String cityName = cityTxtField.getText();
            String cName = cityCB.getSelectionModel().getSelectedItem().toString();
            int cityId = getCityId(cName);
            String countryName = countryCB.getSelectionModel().getSelectedItem().toString();
            int countryId = getAssociatedCountryId(countryName);

            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String usr = getUsername();

            try {

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "UPDATE city SET city='" + cityName + "', countryId=" + countryId + ", lastUpdate='" + ts + "', lastUpdateBy='" + usr + "' WHERE cityId=" + cityId;
                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("City Updated");
                alert.setContentText(cName + " has been updated to " + cityName);
                alert.showAndWait();

                clearSelections();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            cityCB.getItems().clear();
            setCitySelections();
            cityCB.setItems(associatedCity);

        }


    }
    //handles updating existing country
    public void saveUpdateCountry() {

        if(countryTxtField.getText().trim().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error editing Country");
            alert.setContentText("Country label must not be empty");

            alert.showAndWait();

        } else if (countryCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error editing Country");
            alert.setContentText("Please select a country from the drop down");

            alert.showAndWait();

        } else {
            String cName = countryCB.getSelectionModel().getSelectedItem().toString();
            String countryName = countryTxtField.getText();
            int countryId = getAssociatedCountryId(cName);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String usr = getUsername();

            try {

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "UPDATE country SET country='" + countryName + "', lastUpdate='" + ts + "', lastUpdateBy='" + usr + "' WHERE countryId=" + countryId;
                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Country Updated");
                alert.setContentText(countryName + " has been updated");
                alert.showAndWait();

                clearSelections();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            countryCB.getItems().clear();
            setCountrySelections();
            countryCB.setItems(associatedCountry);

        }


    }
    //handles updating existing address
    public void saveUpdateAddress() {

        if(addressCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error editing Address");
            alert.setContentText("Please select an address to edit from the drop down");

            alert.showAndWait();

        } else if(cityCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error editing Address");
            alert.setContentText("Please select a city from the drop down");

            alert.showAndWait();

        } else if(addressTxtField.getText().trim().equals("") || zipTxtField.getText().trim().equals("") || phoneTxtField.getText().trim().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error editing Address");
            alert.setContentText("All labels must be filled");

            alert.showAndWait();

        } else {

            String addressName = addressTxtField.getText();
            String aName = addressCB.getSelectionModel().getSelectedItem().toString();
            int addressId = Address.getAddressId(aName);
            String cityName = cityCB.getSelectionModel().getSelectedItem().toString();
            int cityId = getCityId(cityName);
            String zipCode = zipTxtField.getText();
            String phone = phoneTxtField.getText();
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String usr = getUsername();

            try {

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "UPDATE address SET address='" + addressName + "', cityId=" + cityId + ", postalCode='" + zipCode + "', phone='" + phone + "', lastUpdate='" + ts + "', lastUpdateBy='" + usr + "' WHERE addressId='" + addressId + "'";
                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Address Updated");
                alert.setContentText(addressName + " has been updated");
                alert.showAndWait();

                clearSelections();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            addressCB.getItems().clear();
            setAddressSelections();
            addressCB.setItems(associatedAddress);

        }

    }
    //handles deleting a city
    public void deleteCity() {

        if (cityCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error deleting City");
            alert.setContentText("Please select a city from the drop down");

            alert.showAndWait();

        } else {

            String cityName = cityCB.getSelectionModel().getSelectedItem().toString();
            int delCityId = getCityId(cityName);

            if (checkCityDel(delCityId)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Error deleting City");
                alert.setContentText("Address " + delAddress + " is associated with City " + cityName + " which must first be deleted");

                alert.showAndWait();


            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete the city " + cityName + "?");

                alert.showAndWait().ifPresent(response -> {

                    if (response == ButtonType.OK) {

                        try {

                            String sqlStatement = "DELETE FROM city WHERE cityId=" + delCityId;
                            Statement stmt;
                            stmt = DBConnection.conn.createStatement();
                            stmt.executeUpdate(sqlStatement);

                        } catch (SQLException e) {

                            e.printStackTrace();

                        }

                    }

                });

                cityCB.getItems().clear();
                setCitySelections();
                cityCB.setItems(associatedCity);

            }


        }

        clearSelections();

    }
    //handles deleting a country
    public void deleteCountry() {

        if (countryCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error deleting Country");
            alert.setContentText("Please select a country from the drop down");

            alert.showAndWait();

        } else {

            String countryName = countryCB.getSelectionModel().getSelectedItem().toString();
            int delCountryId = getAssociatedCountryId(countryName);

            if (checkCountryDel(delCountryId)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Error deleting Country");
                alert.setContentText("City " + delCity + " is associated with Country " + countryName + " which must first be deleted");

                alert.showAndWait();


            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete the Country " + countryName + "?");

                alert.showAndWait().ifPresent(response -> {

                    if (response == ButtonType.OK) {

                        try {

                            String sqlStatement = "DELETE FROM country WHERE countryId=" + delCountryId;
                            Statement stmt;
                            stmt = DBConnection.conn.createStatement();
                            stmt.executeUpdate(sqlStatement);

                        } catch (SQLException e) {

                            e.printStackTrace();

                        }

                    }

                });

                countryCB.getItems().clear();
                setCountrySelections();
                countryCB.setItems(associatedCountry);

            }

        }


    }
    //handles deleting an address
    public void deleteAddress() {

        if (addressCB.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error deleting Address");
            alert.setContentText("Please select an address from the drop down");

            alert.showAndWait();

        } else {

            String addressName = addressCB.getSelectionModel().getSelectedItem().toString();
            int delAddressId = getAddressId(addressName);

            if (checkAddressDel(delAddressId)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Error deleting Address");
                alert.setContentText("unable to delete, " + customerAddress + " is set to address " + addressName + ".\n" + customerAddress + "'s address will need to be edited, or " + customerAddress + " must be deleted.");

                alert.showAndWait();


            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete the Address " + addressName + "?");

                alert.showAndWait().ifPresent(response -> {

                    if (response == ButtonType.OK) {

                        try {

                            String sqlStatement = "DELETE FROM address WHERE addressId=" + delAddressId;
                            Statement stmt;
                            stmt = DBConnection.conn.createStatement();
                            stmt.executeUpdate(sqlStatement);

                        } catch (SQLException e) {

                            e.printStackTrace();

                        }

                    }

                });

                addressCB.getItems().clear();
                setAddressSelections();
                addressCB.setItems(associatedAddress);

            }


        }


    }

    private void initializeToggleGroup() {

        toggleGroup = new ToggleGroup();

        this.newAddressRb.setToggleGroup(toggleGroup);
        this.newCityRb.setToggleGroup(toggleGroup);
        this.newCountryRb.setToggleGroup(toggleGroup);
        this.editAddressRb.setToggleGroup(toggleGroup);
        this.editCountryRb.setToggleGroup(toggleGroup);
        this.editCityRB.setToggleGroup(toggleGroup);
        this.delAddressRb.setToggleGroup(toggleGroup);
        this.delCountryRb.setToggleGroup(toggleGroup);
        this.delCityRb.setToggleGroup(toggleGroup);

        newAddressRb.setSelected(true);

    }

    public void cancelAddNewAddress(ActionEvent actionEvent) throws IOException {

        Parent customerViewParent = FXMLLoader.load(getClass().getResource("/view_controller/customerView.fxml"));
        Scene customerViewScene = new Scene(customerViewParent);

        Stage customerViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        customerViewWindow.setScene(customerViewScene);
        customerViewWindow.show();


    }

    public void saveNewClicked() {

        if (this.toggleGroup.getSelectedToggle().equals(newAddressRb)) {

            saveNewAddress();

        } else if (this.toggleGroup.getSelectedToggle().equals(newCountryRb)) {

            saveNewCountry();

        } else if (this.toggleGroup.getSelectedToggle().equals(newCityRb)) {

            saveNewCity();

        }

    }

    public void saveEditClicked() {

        if (this.toggleGroup.getSelectedToggle().equals(editAddressRb)) {

            saveUpdateAddress();

        } else if (this.toggleGroup.getSelectedToggle().equals(editCountryRb)) {

            saveUpdateCountry();


        } else if (this.toggleGroup.getSelectedToggle().equals(editCityRB)) {

            saveUpdateCity();

        }

    }

    public void deleteClicked() {

        if (this.toggleGroup.getSelectedToggle().equals(delAddressRb)) {

            deleteAddress();

        } else if (this.toggleGroup.getSelectedToggle().equals(delCountryRb)) {

            deleteCountry();

        } else if (this.toggleGroup.getSelectedToggle().equals(delCityRb)) {

            deleteCity();

        }

    }

    private void newAddressSelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(true);
        countryCB.setVisible(false);
        countryTxtField.setVisible(false);
        cityCB.setVisible(true);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(true);
        phoneTxtField.setVisible(true);
        editButton.setVisible(false);
        deleteButton.setVisible(false);

    }

    private void newCountrySelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(false);
        countryCB.setVisible(false);
        countryTxtField.setVisible(true);
        cityCB.setVisible(false);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(false);
        phoneTxtField.setVisible(false);

    }

    private void newCitySelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(false);
        countryCB.setVisible(true);
        countryTxtField.setVisible(false);
        cityCB.setVisible(false);
        cityTxtField.setVisible(true);
        zipTxtField.setVisible(true);
        phoneTxtField.setVisible(false);

    }

    private void editAddressSelected() {

        addressCB.setVisible(true);
        addressTxtField.setVisible(true);
        countryCB.setVisible(false);
        countryTxtField.setVisible(false);
        cityCB.setVisible(true);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(true);
        phoneTxtField.setVisible(true);

    }

    private void editCountrySelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(false);
        countryCB.setVisible(true);
        countryTxtField.setVisible(true);
        cityCB.setVisible(false);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(false);
        phoneTxtField.setVisible(false);
    }

    private void editCitySelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(false);
        countryCB.setVisible(true);
        countryTxtField.setVisible(false);
        cityCB.setVisible(true);
        cityTxtField.setVisible(true);
        zipTxtField.setVisible(false);
        phoneTxtField.setVisible(false);

    }

    private void deleteAddressSelected() {

        addressCB.setVisible(true);
        addressTxtField.setVisible(false);
        countryCB.setVisible(false);
        countryTxtField.setVisible(false);
        cityCB.setVisible(false);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(false);
        phoneTxtField.setVisible(false);

    }

    private void deleteCountrySelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(false);
        countryCB.setVisible(true);
        countryTxtField.setVisible(false);
        cityCB.setVisible(false);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(false);
        phoneTxtField.setVisible(false);

    }

    private void deleteCitySelected() {

        addressCB.setVisible(false);
        addressTxtField.setVisible(false);
        countryCB.setVisible(false);
        countryTxtField.setVisible(false);
        cityCB.setVisible(true);
        cityTxtField.setVisible(false);
        zipTxtField.setVisible(false);
        phoneTxtField.setVisible(false);

    }

    public void radioButtonSelected() {

        if (this.toggleGroup.getSelectedToggle().equals(newAddressRb)) {

            newAddressSelected();
            saveButton.setVisible(true);
            editButton.setVisible(false);
            deleteButton.setVisible(false);

        } else if (this.toggleGroup.getSelectedToggle().equals(newCountryRb)) {

            newCountrySelected();
            saveButton.setVisible(true);
            editButton.setVisible(false);
            deleteButton.setVisible(false);

        } else if (this.toggleGroup.getSelectedToggle().equals(newCityRb)) {

            newCitySelected();
            saveButton.setVisible(true);
            editButton.setVisible(false);
            deleteButton.setVisible(false);

        } else if (this.toggleGroup.getSelectedToggle().equals(editAddressRb)) {

            editAddressSelected();
            saveButton.setVisible(false);
            editButton.setVisible(true);
            deleteButton.setVisible(false);

        } else if (this.toggleGroup.getSelectedToggle().equals(editCountryRb)) {

            editCountrySelected();
            saveButton.setVisible(false);
            editButton.setVisible(true);
            deleteButton.setVisible(false);

        } else if (this.toggleGroup.getSelectedToggle().equals(editCityRB)) {

            editCitySelected();
            saveButton.setVisible(false);
            editButton.setVisible(true);
            deleteButton.setVisible(false);

        } else if (this.toggleGroup.getSelectedToggle().equals(delAddressRb)) {

            deleteAddressSelected();
            saveButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(true);

        } else if (this.toggleGroup.getSelectedToggle().equals(delCountryRb)) {

            deleteCountrySelected();
            saveButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(true);
        } else if (this.toggleGroup.getSelectedToggle().equals(delCityRb)) {

            deleteCitySelected();
            saveButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(true);

        }

    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        initializeToggleGroup();
        initializeView();
        newAddressRb.setSelected(true);
        newAddressSelected();



    }


}
