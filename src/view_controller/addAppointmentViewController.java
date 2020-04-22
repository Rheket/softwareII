package view_controller;

import DAO.DBConnection;
import Model.Appointment;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Model.Appointment.allAppointments;
import static Model.Customer.getCustomerAppId;
import static Model.User.getUserId;
import static Model.User.getUsername;


public class addAppointmentViewController implements Initializable {

    @FXML public Button saveButton;
    @FXML public Button updateButton;

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
    private String app;
    private String appointmentStartTime;
    private String appointmentEndTime;

    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();

    private int userAppId = 0;
    public SimpleIntegerProperty selectedAppointmentId;
    private boolean appCheck = false;
    private boolean validStart = true;
    private String overlappingTime;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
    private DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void cancelAppointmentView(ActionEvent actionEvent) throws IOException {


        //change scenes to calendar view controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/monthlyView.fxml"));
        Parent calendarViewParent = loader.load();

        Scene calendarViewScene = new Scene(calendarViewParent);

        //access appointment table view controller
        monthlyViewController controller = loader.getController();
        //clear table and reset tableview
        controller.setMonthAppointmentTableView();
        controller.initializeAppointments();

        Stage calendarViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        calendarViewWindow.setScene(calendarViewScene);
        calendarViewWindow.show();


    }

    public void saveAddAppointment(ActionEvent actionEvent) throws IOException {

        if (customerNameComboBox.getValue() == null | appointmentTitleTextField.getText().trim().equals("") | appointmentDescriptionTextField.getText().trim().equals("") | appointmentLocationTextField.getText().trim().equals("") | appointmentContactTextField.getText().trim().equals("") | appointmentTypeTextField.getText().trim().equals("") | appointmentUrlTextField.getText().trim().equals("") | appointmentStartTimeComboBox.getValue() == null | appointmentEndTimeComboBox.getValue() == null | appointmentDate.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error adding Appointment");
            alert.setContentText("Please make sure all fields are filled");

            alert.showAndWait();

        } else {

            Appointment appointment = new Appointment();

            customerName = String.valueOf(customerNameComboBox.getSelectionModel().getSelectedItem());
            appointmentTitle = appointmentTitleTextField.getText();
            appointmentDescription = appointmentDescriptionTextField.getText();
            appointmentLocation = appointmentLocationTextField.getText();
            appointmentContact = appointmentContactTextField.getText();
            appointmentType = appointmentTypeTextField.getText();
            appointmentUrl = appointmentUrlTextField.getText();
            app = String.valueOf(appointmentDate.getValue());
            appointmentStartTime = String.valueOf(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem());
            appointmentEndTime = String.valueOf(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem());

            Timestamp ts = new Timestamp(System.currentTimeMillis());

            int appointmentId = appointment.getAppIdCounter() + 1;
            String usr = getUsername();
            int customerAppsId = getCustomerAppId(customerName);
            userAppId = getUserId(usr);
            String fullStart = app + " " + appointmentStartTime;
            String fullEnd = app + " " + appointmentEndTime;

            LocalDateTime checkStartOverlap = LocalDateTime.parse(fullStart, secondFormatter);
            LocalDateTime checkEndOverlap = LocalDateTime.parse(fullEnd, secondFormatter);

            validateStart(checkStartOverlap, checkEndOverlap);
            checkOverlapping(checkStartOverlap, checkEndOverlap);

            if(appCheck) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Error Adding Appointment");
                alert.setContentText("An appointment already exists from: " + "\n" + overlappingTime + "\nplease choose a time that doesn't overlap.");

                alert.showAndWait();
                appCheck = false;

            } else if (!validStart) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Start time error");
                alert.setContentText("Appointment start time can not be after or the same as end time");
                alert.showAndWait();
                validStart = true;

            } else {

                try {

                    Statement stmt = DBConnection.conn.createStatement();
                    String sqlStatement = "INSERT INTO `appointment` VALUES (" + appointmentId + "," + customerAppsId + "," + userAppId + ",'" + appointmentTitle + "','" + appointmentDescription + "','" + appointmentLocation + "','" + appointmentContact + "','" + appointmentType + "','" + appointmentUrl + "','" + fullStart + "','" + fullEnd + "','" + ts + "','" + usr + "','" + ts + "','" + usr + "')" ;
                    stmt.executeUpdate(sqlStatement);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle("Appointment added");
                    alert.setContentText("Appointment from " + fullStart + " to " + fullEnd + " has been added.");

                    alert.showAndWait();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
                Scene mainMenuScene = new Scene(mainMenuParent);

                Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                mainMenuWindow.setScene(mainMenuScene);
                mainMenuWindow.show();

            }

        }

    }

    public void checkOverlapping(LocalDateTime start, LocalDateTime end) {

        for (Appointment allAppointment : allAppointments) {

            LocalDateTime checkStart = LocalDateTime.parse(allAppointment.getAppointmentStart().getValue(), formatter);
            LocalDateTime checkEnd = LocalDateTime.parse(allAppointment.getAppointmentEnd().getValue(), formatter);

            if (start.plusSeconds(1).isAfter(checkStart) && start.minusSeconds(1).isBefore(checkEnd)) {

                appCheck = true;
                overlappingTime = checkStart.getHour() + ":" + checkStart.getMinute() + " to " + checkEnd.getHour() + ":" + checkEnd.getMinute();

            } else if (end.plusSeconds(1).isAfter(checkStart) && end.minusSeconds(1).isBefore(checkEnd)) {
                appCheck = true;
                overlappingTime = checkStart.getHour() + ":" + checkStart.getMinute() + " to " + checkEnd.getHour() + ":" + checkEnd.getMinute();
            }

        }



        }

    public void saveUpdateAppointment(ActionEvent actionEvent) throws IOException {

        customerName = String.valueOf(customerNameComboBox.getSelectionModel().getSelectedItem());
        appointmentTitle = appointmentTitleTextField.getText();
        appointmentDescription = appointmentDescriptionTextField.getText();
        appointmentLocation = appointmentLocationTextField.getText();
        appointmentContact = appointmentContactTextField.getText();
        appointmentType = appointmentTypeTextField.getText();
        appointmentUrl = appointmentUrlTextField.getText();
        app = String.valueOf(appointmentDate.getValue());
        appointmentStartTime = String.valueOf(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem());

        appointmentEndTime = String.valueOf(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem());

        Timestamp ts = new Timestamp(System.currentTimeMillis());

        String usr = getUsername();
        userAppId = getUserId(usr);
        String fullStart = app + " " + appointmentStartTime;
        String fullEnd = app + " " + appointmentEndTime;

        LocalDateTime checkStartOverlap = LocalDateTime.parse(fullStart, secondFormatter);
        LocalDateTime checkEndOverlap = LocalDateTime.parse(fullEnd, secondFormatter);

        checkOverlapping(checkStartOverlap, checkEndOverlap);

        if(appCheck) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error Adding Appointment");
            alert.setContentText("An appointment already exists from: " + "\n" + overlappingTime + "\nplease choose a time that doesn't overlap.");


            alert.showAndWait();
            appCheck = false;

        } else if(!validStart) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Start time error");
            alert.setContentText("Appointment start time can not be after or the same as end time");
            alert.showAndWait();
            validStart = true;

        } else {

            try {

                String sqlStatement = "UPDATE appointment SET userId=" + userAppId + ", title='" + appointmentTitle + "', description='" + appointmentDescription + "', location='" + appointmentLocation + "', contact='" + appointmentContact + "', type='" + appointmentType + "', url='" + appointmentUrl + "', start='" + fullStart + "', end='" + fullEnd + "', lastUpdate='" + ts + "', lastUpdateBy='" + usr + "' WHERE appointmentId=" + selectedAppointmentId;
                Statement stmt = DBConnection.conn.createStatement();
                stmt.executeUpdate(sqlStatement);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Appointment updated");
                alert.setContentText("Appointment from " + fullStart + " to " + fullEnd + " has been updated.");

                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        //change scenes to calendar view controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/monthlyView.fxml"));
        Parent calendarViewParent = loader.load();

        Scene calendarViewScene = new Scene(calendarViewParent);

        //access appointment table view controller
        monthlyViewController controller = loader.getController();
        //clear table and reset table view
        controller.setMonthAppointmentTableView();
        controller.initializeAppointments();

        Stage calendarViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        calendarViewWindow.setScene(calendarViewScene);
        calendarViewWindow.show();
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

        //Business hours are 9am - 5pm UTC Time
        //Appointments are in 30 minute intervals

        LocalTime startingHour = LocalTime.of(9,0,0);

        for(int i = 0; i < 15; i++) {

            businessHours.add(startingHour);
            startingHour = startingHour.plusMinutes(30);

        }

        appointmentStartTimeComboBox.setItems(businessHours);
        appointmentEndTimeComboBox.setItems(businessHours);

    }

    private void validateStart(LocalDateTime s, LocalDateTime e) {

        if(s.isAfter(e) | s.isEqual(e)) {

            validStart = false;

        }

    }

    public void initializeToUpdate(Appointment appointment) {

        appointmentTitleTextField.setText(appointment.getAppointmentTitle().get());
        appointmentDescriptionTextField.setText(appointment.getAppointmentDescription().get());
        appointmentLocationTextField.setText(appointment.getAppointmentLocation().get());
        appointmentContactTextField.setText(appointment.getAppointmentContact());
        appointmentTypeTextField.setText(appointment.getAppointmentType().get());
        appointmentUrlTextField.setText(appointment.getAppointmentUrl());

        customerNameComboBox.setVisible(false);
        saveButton.setVisible(false);
        addAppointmentLabel.setText("Editing appointment for: " + appointment.getCustomerId());

    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        setCustomerSelections();
        setStartTimes();

    }

}
