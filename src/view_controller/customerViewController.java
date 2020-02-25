package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class customerViewController implements Initializable {

    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField zipCodeTextField;

    public void mainMenu(ActionEvent actionEvent) throws IOException {

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

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