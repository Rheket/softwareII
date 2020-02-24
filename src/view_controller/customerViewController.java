package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class customerViewController implements Initializable {

    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField zipCodeTextField;

    // Add update delete customer records
    public void addCustomerRecord() {

        String customerName = nameTextField.getText();
        String customerAddress = addressTextField.getText();
        String customerPhoneNumber = phoneNumberTextField.getText();
        String city = cityTextField.getText();
        String zipCode = zipCodeTextField.getText();

    /*
    FIXME Write code to add customer data to the SQL database
     */

    }

    public void updateCustomerRecord() {

        String customerName = nameTextField.getText();
        String customerAddress = addressTextField.getText();
        String customerPhoneNumber = phoneNumberTextField.getText();
        String city = cityTextField.getText();
        String zipCode = zipCodeTextField.getText();

        /*
    FIXME Write code to UPDATE customer data to the SQL database
     */

    }

    public void deleteCustomerRecord() {

        String customerName = nameTextField.getText();
        String customerAddress = addressTextField.getText();
        String customerPhoneNumber = phoneNumberTextField.getText();
        String city = cityTextField.getText();
        String zipCode = zipCodeTextField.getText();

    /*
    FIXME Write code to DELETE customer data to the SQL database
     */

    }


    @Override
    public void initialize (URL url, ResourceBundle rb) {

    }

}
