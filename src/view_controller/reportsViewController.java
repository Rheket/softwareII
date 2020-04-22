package view_controller;

import DAO.DBConnection;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static Model.Customer.allCustomers;

public class reportsViewController {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    public void mainMenu(ActionEvent actionEvent) throws IOException {

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

    public void appointmentsPerMonth() {

        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());

        Month month = monthStart.getMonth();

        int num = 0;

        PrintWriter txtWriter = null;
        try {
            txtWriter = new PrintWriter("Monthly_Appointments.txt");
            txtWriter.println("Number of Appointments per month:\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            //cycle through future months
            for (int i = 0; i < 13 - month.getValue(); i++) {

                LocalDate start = monthStart.plusMonths(i);
                LocalDate end = monthEnd.plusMonths(i);



                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "SELECT * FROM appointment WHERE start >= '" + start + "' and start <= '" + end + "'";
                ResultSet result = stmt.executeQuery(sqlStatement);

                // cycle through appointments, if an appointment is found increase appointment count
                while (result.next()) {

                    if (result.getInt("appointmentId") != 0) {

                        num = num + 1;

                    }
                }

                //add text to file
                txtWriter.println(start.getMonth() + ": " + num);

                // set count back to 0
                num = 0;

            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Report generated");
        alert.setContentText("Monthly_Appointments.txt has been saved");
        alert.showAndWait();

        txtWriter.close();



    }

    public void consultantSchedule() {

        LocalDate now = LocalDate.now();
        int userId = User.getUserId(User.getUsername());

        PrintWriter txtWriter = null;
        try {
            txtWriter = new PrintWriter("Schedule.txt");
            txtWriter.println("Upcoming schedule for " + User.getUsername() + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            Statement stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment WHERE start >= '" + now + "' and userId='" + userId + "'";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                LocalDateTime ldtStart = LocalDateTime.parse(result.getString("start"), formatter);
                txtWriter.println(ldtStart.getMonth() + " | " + ldtStart.getDayOfWeek() + " | " + ldtStart.getHour() + ":" + ldtStart.getMinute());

            }


        } catch (SQLException e){
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Report generated");
        alert.setContentText("Schedule.txt has been saved");
        alert.showAndWait();

        txtWriter.close();

    }

    public void numberOfAppointmentsCustomer() {

        customerViewController.initializeCustomers();

        int num = 0;

        PrintWriter txtWriter = null;
        try {
            txtWriter = new PrintWriter("Appointments_Per_Cutomer.txt");
            txtWriter.println("Number of appointments per customer\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            for (Model.Customer allCustomer : allCustomers) {

                int customerId = allCustomer.getCustomerId();

                Statement stmt = DBConnection.conn.createStatement();
                String sqlStatement = "SELECT * FROM appointment WHERE customerId = " + customerId;
                ResultSet result = stmt.executeQuery(sqlStatement);

                while (result.next()) {
                    if (result.getInt("appointmentId") != 0) {
                        num = num + 1;
                    }
                }

                txtWriter.println(allCustomer.getCustomerName() + ": " + num);
                num = 0;
            }


        } catch (SQLException e){
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Report generated");
        alert.setContentText("Appointments_Per_Cutomer.txt has been saved");
        alert.showAndWait();

        txtWriter.close();

    }


}
