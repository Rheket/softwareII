package view_controller;

import DAO.DBConnection;
import Model.Appointment;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static Model.Appointment.getAllAppointments;
import static Model.Customer.getCustomerAppId;
import static Model.User.getUserId;
import static Model.User.getUsername;


public class addAppointmentViewController implements Initializable {

    @FXML private Label addAppointmentLabel;
    @FXML private ComboBox customerNameComboBox;
    @FXML private TextField appointmentTitleTextField;
    @FXML private TextField appointmentDescriptionTextField;
    @FXML private TextField appointmentLocationTextField;
    @FXML private TextField appointmentContactTextField;
    @FXML private TextField appointmentTypeTextField;
    @FXML private TextField appointmentUrlTextField;
    @FXML private DatePicker appointmentDate;
    @FXML private ComboBox appointmentStartTimeComboBox;
    @FXML private ComboBox appointmentEndTimeComboBox;

    private String customerName;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentContact;
    private String appointmentType;
    private String appointmentUrl;
    private String appointmentStart;
    private String appointmentStartTime;
    private String appointmentEnd;
    private String appointmentEndTime;

    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();

    private int userAppId = 0;

    public void cancelAppointmentView(ActionEvent actionEvent) throws IOException {


        //change scenes to calendar view controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/calendarView.fxml"));
        Parent calendarViewParent = loader.load();

        Scene calendarViewScene = new Scene(calendarViewParent);

        //access appointment table view controller
        calendarViewController controller = loader.getController();
        //clear table first
        //controller.appointmentTableView.getItems().clear();
        controller.setAppointmentTableView();

        Stage calendarViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        calendarViewWindow.setScene(calendarViewScene);
        calendarViewWindow.show();


    }

    public void saveAddAppointment(ActionEvent actionEvent) throws IOException{

        //User user = new User();
        Appointment appointment = new Appointment();

        customerName = String.valueOf(customerNameComboBox.getSelectionModel().getSelectedItem());
        appointmentTitle = appointmentTitleTextField.getText();
        appointmentDescription = appointmentDescriptionTextField.getText();
        appointmentLocation = appointmentLocationTextField.getText();
        appointmentContact = appointmentContactTextField.getText();
        appointmentType = appointmentTypeTextField.getText();
        appointmentUrl = appointmentUrlTextField.getText();
        appointmentStart = String.valueOf(appointmentDate.getValue());
        appointmentStartTime = String.valueOf(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem());
        appointmentEnd = String.valueOf(appointmentDate.getValue());
        appointmentEndTime = String.valueOf(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem());

        Timestamp ts = new Timestamp(System.currentTimeMillis());

        int appointmentId = appointment.getAppIdCounter() + 1;
        String usr = getUsername();
        int customerAppsId = getCustomerAppId(customerName);
        userAppId = getUserId(usr);
        String fullStart = appointmentStart + " " + appointmentStartTime;
        String fullEnd = appointmentEnd + " " + appointmentEndTime;

        try {

            Statement stmt = DBConnection.conn.createStatement();
            String sqlStatement = "INSERT INTO `appointment` VALUES (" + appointmentId + "," + customerAppsId + "," + userAppId + ",'" + appointmentTitle + "','" + appointmentDescription + "','" + appointmentLocation + "','" + appointmentContact + "','" + appointmentType + "','" + appointmentUrl + "','" + fullStart + "','" + fullEnd + "','" + ts + "','" + usr + "','" + ts + "','" + usr + "')" ;
            stmt.executeUpdate(sqlStatement);

            //System.out.println(sqlStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

    public void saveUpdateAppointment(ActionEvent actionEvent) throws IOException {

        //int selectedAppointmentId = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();

        try {

            String sqlStatement = "SELECT * FROM appointment WHERE appointmentId=";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {



            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }


    private void setCustomerSelections() {

        try {

            String sqlStatement = "SELECT * FROM customer";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                String getName = result.getString("customerName");
                customerNames.add(getName);
                customerNameComboBox.setItems(customerNames);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setStartTimes() {

        //Business hours are 9am - 5pm PACIFIC Time
        //Appointments are in 30 minute intervals

        LocalTime startingHour = LocalTime.of(9,0,0);

        for(int i = 0; i < 15; i++) {

            businessHours.add(startingHour);
            startingHour = startingHour.plusMinutes(30);

        }

        appointmentStartTimeComboBox.setItems(businessHours);
        appointmentEndTimeComboBox.setItems(businessHours);

    }

    public void initializePartToUpdate(Appointment appointment) {

        appointmentTitleTextField.setText(appointment.getAppointmentTitle());
        appointmentDescriptionTextField.setText(appointment.getAppointmentDescription());
        appointmentLocationTextField.setText(appointment.getAppointmentLocation());
        appointmentContactTextField.setText(appointment.getAppointmentContact());
        appointmentTypeTextField.setText(appointment.getAppointmentType());
        appointmentUrlTextField.setText(appointment.getAppointmentUrl());

        customerNameComboBox.setVisible(false);
        addAppointmentLabel.setText("Editing appointment for: " + appointment.getCustomerId());




    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        setCustomerSelections();
        setStartTimes();

    }

}
