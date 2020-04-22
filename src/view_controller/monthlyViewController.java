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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Model.Appointment.allAppointments;

public class monthlyViewController implements Initializable {

    @FXML public TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentTitleTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentLocationTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentStartTableColumn;
    @FXML private TableColumn<Appointment, String> appointmentEndTableColumn;
    @FXML private Label monthLabel;

    public ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();

    private static Appointment appointmentId;
    private String monthString;

    public static LocalDate workingMonth = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");


    public void mainMenu(ActionEvent actionEvent) throws IOException {

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
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
                appointment.setAppointmentStart(result.getString("start"));
                appointment.setAppointmentEnd(result.getString("end"));
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
        appointmentId = appointmentTableView.getSelectionModel().getSelectedItem();

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

            //set variables for appointment view controller
            //controller.updateCName = String.valueOf(customerNameComboB)
            Stage addAppointmentWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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
            appointmentTableView.getItems().clear();
            initializeAppointments();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Appointment selected");
            alert.setContentText("An appointment must be selected to delete");

            alert.showAndWait();

        }

        setMonthAppointmentTableView();

    }

    public void next() {

        appointmentTableView.getItems().clear();
        workingMonth = workingMonth.plusMonths(1);
        Month month = workingMonth.getMonth();
        monthLabel.setText(String.valueOf(month));

        LocalDate start = workingMonth.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);


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
                //convert values to ZonDateTime in local time
                ZonedDateTime startTime =  ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
                ZonedDateTime endTime = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());

                appointment.setAppointmentStart(formatter.format((startTime)));
                appointment.setAppointmentEnd(formatter.format(endTime));
                appointment.setAppointmentCreateDate(result.getString("createDate"));
                appointment.setAppointmentCreatedBy(result.getString("createdBy"));
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                monthlyAppointments.add(appointment);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void previous() {

        appointmentTableView.getItems().clear();
        workingMonth = workingMonth.minusMonths(1);
        Month month = workingMonth.getMonth();
        monthLabel.setText(String.valueOf(month));

        LocalDate start = workingMonth.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);

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
                //convert values to ZonDateTime in local time
                ZonedDateTime startTime =  ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
                ZonedDateTime endTime = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());

                appointment.setAppointmentStart(formatter.format((startTime)));
                appointment.setAppointmentEnd(formatter.format(endTime));

                appointment.setAppointmentCreateDate(result.getString("createDate"));
                appointment.setAppointmentCreatedBy(result.getString("createdBy"));
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                monthlyAppointments.add(appointment);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void setMonthAppointmentTableView() {

        appointmentTableView.getItems().clear();
        Month month = workingMonth.getMonth();
        monthLabel.setText(String.valueOf(month));

        LocalDate start = workingMonth.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);

        Statement stmt;

        try {

            stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment WHERE start >= '" + start + "' and start <= '" + end + "'";
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
                appointment.setAppointmentLastUpdate(result.getString("lastUpdate"));
                appointment.setAppointmentLastUpdatedBy(result.getString("lastUpdateBy"));

                monthlyAppointments.add(appointment);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        appointmentTableView.setItems(monthlyAppointments);

        //Lambda used to set table view for  improved readability
        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentId().asObject());
        appointmentTitleTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTitle());
        appointmentLocationTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentLocation());
        appointmentTypeTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentType());
        appointmentStartTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentStart());
        appointmentEndTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentEnd());

    }

    public void switchToWeeklyView(ActionEvent actionEvent) throws IOException {

        Parent weeklyViewParent = FXMLLoader.load(getClass().getResource("/view_controller/weeklyView.fxml"));
        Scene weeklyViewScene = new Scene(weeklyViewParent);

        Stage weeklyViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        weeklyViewWindow.setScene(weeklyViewScene);
        weeklyViewWindow.show();

    }

    //method to initialize current month
    private void initializeMonth() {

        Month month = workingMonth.getMonth();
        //get month and set as label
        monthString = String.valueOf(month);
        monthLabel.setText(monthString);

    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        appointmentTableView.getItems().clear();

        initializeMonth();

        initializeAppointments();

        setMonthAppointmentTableView();

    }

}