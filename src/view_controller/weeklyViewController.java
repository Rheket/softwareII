package view_controller;


import DAO.DBConnection;
import Model.Appointment;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

import static Model.Appointment.allAppointments;

public class weeklyViewController implements Initializable {

    public ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();

    public static LocalDate workingWeek = LocalDate.now();

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentTitleTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentLocationTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentStartTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentEndTableColumn;
    @FXML private Label weekLabel;


    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    public void mainMenu(ActionEvent actionEvent) throws IOException {

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

    public void switchToMonthlyView(ActionEvent actionEvent) throws IOException {

        Parent monthlyViewParent = FXMLLoader.load(getClass().getResource("/view_controller/monthlyView.fxml"));
        Scene monthlyViewScene = new Scene(monthlyViewParent);

        Stage monthlyViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        monthlyViewWindow.setScene(monthlyViewScene);
        monthlyViewWindow.show();

    }

    public void addAppointmentWindow(ActionEvent actionEvent) throws IOException {

        //change scenes to add appointment view controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_controller/addAppointmentView.fxml"));
        Parent addAppointmentParent = loader.load();

        Scene addAppointmentScene = new Scene(addAppointmentParent);

        //access add appointment controller
        addAppointmentViewController controller = loader.getController();
        //set update button to not visible
        controller.updateButton.setVisible(false);

        Stage addAppointmentWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        addAppointmentWindow.setScene(addAppointmentScene);
        addAppointmentWindow.show();

    }

    public void updateAppointment(ActionEvent actionEvent) throws IOException {

        //select appointmentId to modify
        Appointment appointmentId = appointmentTableView.getSelectionModel().getSelectedItem();

        if(appointmentId != null) {
            //change scenes to add appointment view controller
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_controller/addAppointmentView.fxml"));
            Parent addAppointmentParent = loader.load();

            Scene addAppointmentScene = new Scene(addAppointmentParent);

            //access add appointment controller
            addAppointmentViewController controller = loader.getController();
            controller.initializeToUpdate(appointmentId);
            //pass selected appointmentId to appointment view controller
            controller.selectedAppointmentId = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();
            controller.updateButton.setVisible(true);
            controller.saveButton.setVisible(false);

            //set variables for appointment view controller
            Stage addAppointmentWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            addAppointmentWindow.setScene(addAppointmentScene);
            addAppointmentWindow.show();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Appointment selected");
            alert.setContentText("An appointment must be selected to modify");

            alert.showAndWait();

        }

    }

    public void deleteAppointment() {

        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {

            int delAppointment = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId().get();

            //confirm they want to delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete the appointment with ID " + delAppointment + "?");

            //Lambda to confirm deletion uses less code making it easier to read
            alert.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    try {

                        String sqlStatement = "Delete FROM appointment WHERE appointmentID=" + delAppointment;
                        Statement stmt;
                        stmt = DBConnection.conn.createStatement();
                        stmt.executeUpdate(sqlStatement);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            });

            //clear table and update with new appointments
            allAppointments.clear();
            appointmentTableView.getItems().clear();
            initializeAppointments();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Appointment selected");
            alert.setContentText("An appointment must be selected to delete");

            alert.showAndWait();

        }

        setWeekAppointmentTableView();

    }

    public void initializeAppointments () {

        Statement stmt;

        try {

            stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                Appointment appointment = new Appointment();

                appointment.setAppointmentId(result.getInt("appointmentId"));

                appointment.setCustomerId(result.getInt("customerId"));
                appointment.setUserId(result.getInt("userId"));
                appointment.setAppointmentTitle(result.getString("title"));
                appointment.setAppointmentDescription(result.getString("description"));
                appointment.setAppointmentLocation(result.getString("location"));
                appointment.setAppointmentContact(result.getString("contact"));
                appointment.setAppointmentType(result.getString("type"));
                appointment.setAppointmentUrl(result.getString("url"));

                //get the times from the sql table
                LocalDateTime ldtStart = LocalDateTime.parse(result.getString("start"), formatter);
                LocalDateTime ldtEnd = LocalDateTime.parse(result.getString("end"), formatter);
                //Database times are in UTC
                ZonedDateTime startTimeUTC = ZonedDateTime.of(ldtStart, ZoneId.of("UTC"));
                ZonedDateTime endTimeUTC = ZonedDateTime.of(ldtEnd, ZoneId.of("UTC"));
                //convert values to display in local time
                ZonedDateTime startTime =  startTimeUTC.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime endTime = endTimeUTC.withZoneSameInstant(ZoneId.systemDefault());

                appointment.setAppointmentStart(formatter.format((startTime)));
                appointment.setAppointmentEnd(formatter.format(endTime));
                appointment.setAppointmentCreateDate(result.getString("createDate"));
                appointment.setAppointmentCreatedBy(result.getString("createdBy"));
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                allAppointments.add(appointment);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setWeekAppointmentTableView() {

        appointmentTableView.getItems().clear();

        TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
        workingWeek = workingWeek.with(fieldISO, 1);

        LocalDate start = workingWeek;
        LocalDate end = workingWeek.plusWeeks(1);

        weekLabel.setText("Week: " + start + " - " + end);

        Statement stmt;

        try {

            stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment where start >= '" + start + "' and start <= '" + end + "'";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                Appointment appointment = new Appointment();

                appointment.setAppointmentId(result.getInt("appointmentId"));

                appointment.setCustomerId(result.getInt("customerId"));
                appointment.setUserId(result.getInt("userId"));
                appointment.setAppointmentTitle(result.getString("title"));
                appointment.setAppointmentDescription(result.getString("description"));
                appointment.setAppointmentLocation(result.getString("location"));
                appointment.setAppointmentContact(result.getString("contact"));
                appointment.setAppointmentType(result.getString("type"));
                appointment.setAppointmentUrl(result.getString("url"));

                //get the times from the sql table
                LocalDateTime ldtStart = LocalDateTime.parse(result.getString("start"), formatter);
                LocalDateTime ldtEnd = LocalDateTime.parse(result.getString("end"), formatter);
                //Database times are in UTC
                ZonedDateTime startTimeUTC = ZonedDateTime.of(ldtStart, ZoneId.of("UTC"));
                ZonedDateTime endTimeUTC = ZonedDateTime.of(ldtEnd, ZoneId.of("UTC"));
                //convert values to display in local time
                ZonedDateTime startTime =  startTimeUTC.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime endTime = endTimeUTC.withZoneSameInstant(ZoneId.systemDefault());

                appointment.setAppointmentStart(formatter.format((startTime)));
                appointment.setAppointmentEnd(formatter.format(endTime));
                appointment.setAppointmentCreateDate(result.getString("createDate"));
                appointment.setAppointmentCreatedBy(result.getString("createdBy"));
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                weeklyAppointments.add(appointment);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        appointmentTableView.setItems(weeklyAppointments);

        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentId().asString());
        appointmentTitleTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTitle());
        appointmentLocationTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentLocation());
        appointmentTypeTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentType());
        appointmentStartTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentStart());
        appointmentEndTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentEnd());

    }

    public void next() {

        appointmentTableView.getItems().clear();
        workingWeek = workingWeek.plusWeeks(1);

        LocalDate start = workingWeek;
        LocalDate end = workingWeek.plusWeeks(1);
        weekLabel.setText("Week: " + start + " - " + end);

        Statement stmt;

        try {

            stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment where start >= '" + start + "' and start <= '" + end + "'";
            ResultSet result = stmt.executeQuery(sqlStatement);


            while (result.next()) {

                Appointment appointment = new Appointment();

                appointment.setAppointmentId(result.getInt("appointmentId"));

                appointment.setCustomerId(result.getInt("customerId"));
                appointment.setUserId(result.getInt("userId"));
                appointment.setAppointmentTitle(result.getString("title"));
                appointment.setAppointmentDescription(result.getString("description"));
                appointment.setAppointmentLocation(result.getString("location"));
                appointment.setAppointmentContact(result.getString("contact"));
                appointment.setAppointmentType(result.getString("type"));
                appointment.setAppointmentUrl(result.getString("url"));

                //get the times from the sql table
                LocalDateTime ldtStart = LocalDateTime.parse(result.getString("start"), formatter);
                LocalDateTime ldtEnd = LocalDateTime.parse(result.getString("end"), formatter);
                //Database times are in UTC
                ZonedDateTime startTimeUTC = ZonedDateTime.of(ldtStart, ZoneId.of("UTC"));
                ZonedDateTime endTimeUTC = ZonedDateTime.of(ldtEnd, ZoneId.of("UTC"));
                //convert values to display in local time
                ZonedDateTime startTime =  startTimeUTC.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime endTime = endTimeUTC.withZoneSameInstant(ZoneId.systemDefault());

                appointment.setAppointmentStart(formatter.format((startTime)));
                appointment.setAppointmentEnd(formatter.format(endTime));
                appointment.setAppointmentCreateDate(result.getString("createDate"));
                appointment.setAppointmentCreatedBy(result.getString("createdBy"));
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                weeklyAppointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void previous() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

        appointmentTableView.getItems().clear();
        workingWeek = workingWeek.minusWeeks(1);

        LocalDate start = workingWeek;
        LocalDate end = workingWeek.plusWeeks(1);
        weekLabel.setText("Week: " + start + " - " + end);


        Statement stmt;

        try {

            stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment where start >= '" + start + "' and start <= '" + end + "'";
            ResultSet result = stmt.executeQuery(sqlStatement);


            while (result.next()) {

                Appointment appointment = new Appointment();

                appointment.setAppointmentId(result.getInt("appointmentId"));

                appointment.setCustomerId(result.getInt("customerId"));
                appointment.setUserId(result.getInt("userId"));
                appointment.setAppointmentTitle(result.getString("title"));
                appointment.setAppointmentDescription(result.getString("description"));
                appointment.setAppointmentLocation(result.getString("location"));
                appointment.setAppointmentContact(result.getString("contact"));
                appointment.setAppointmentType(result.getString("type"));
                appointment.setAppointmentUrl(result.getString("url"));

                //get the times from the sql table
                LocalDateTime ldtStart = LocalDateTime.parse(result.getString("start"), formatter);
                LocalDateTime ldtEnd = LocalDateTime.parse(result.getString("end"), formatter);
                //Database times are in UTC
                ZonedDateTime startTimeUTC = ZonedDateTime.of(ldtStart, ZoneId.of("UTC"));
                ZonedDateTime endTimeUTC = ZonedDateTime.of(ldtEnd, ZoneId.of("UTC"));
                //convert values to display in local time
                ZonedDateTime startTime =  startTimeUTC.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime endTime = endTimeUTC.withZoneSameInstant(ZoneId.systemDefault());

                appointment.setAppointmentStart(formatter.format((startTime)));
                appointment.setAppointmentEnd(formatter.format(endTime));

                appointment.setAppointmentCreateDate(result.getString("createDate"));
                appointment.setAppointmentCreatedBy(result.getString("createdBy"));
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                weeklyAppointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        setWeekAppointmentTableView();

    }
}