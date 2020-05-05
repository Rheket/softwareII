package Model;

import DAO.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Appointment {

    private SimpleIntegerProperty appointmentId;
    private int customerId;
    private int userId;
    private SimpleStringProperty appointmentTitle;
    private SimpleStringProperty appointmentLocation;
    private SimpleStringProperty appointmentDescription;
    private SimpleStringProperty appointmentType;
    private SimpleStringProperty appointmentStart;
    private SimpleStringProperty appointmentEnd;
    private String appointmentContact;
    private String appointmentUrl;
    private String appointmentCreateDate;
    private String appointmentCreatedBy;
    private String appointmentLastUpdate;
    private String appointmentLastUpdatedBy;

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public SimpleIntegerProperty getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public SimpleStringProperty getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = new SimpleStringProperty(appointmentTitle);
    }

    public SimpleStringProperty getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = new SimpleStringProperty(appointmentDescription);
    }

    public SimpleStringProperty getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = new SimpleStringProperty(appointmentLocation);
    }

    public String getAppointmentContact() {
        return appointmentContact;
    }

    public void setAppointmentContact(String appointmentContact) {
        this.appointmentContact = appointmentContact;
    }

    public SimpleStringProperty getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = new SimpleStringProperty(appointmentType);
    }

    public String getAppointmentUrl() {
        return appointmentUrl;
    }

    public void setAppointmentUrl(String appointmentUrl) {
        this.appointmentUrl = appointmentUrl;
    }

    public SimpleStringProperty getAppointmentStart() {
        return appointmentStart;
    }

    public void setAppointmentStart(String appointmentStart) {
        this.appointmentStart = new SimpleStringProperty(appointmentStart);
    }

    public SimpleStringProperty getAppointmentEnd() {
        return appointmentEnd;
    }

    public void setAppointmentEnd(String appointmentEnd) {
        this.appointmentEnd = new SimpleStringProperty(appointmentEnd);
    }

    public void setAppointmentCreateDate(String appointmentCreateDate) {
        this.appointmentCreateDate = appointmentCreateDate;
    }

    public void setAppointmentCreatedBy(String appointmentCreatedBy) {
        this.appointmentCreatedBy = appointmentCreatedBy;
    }

    public void setAppointmentLastUpdate(String appointmentLastUpdate) {
        this.appointmentLastUpdate = appointmentLastUpdate;
    }

    public void setAppointmentLastUpdatedBy(String appointmentLastUpdatedBy) {
        this.appointmentLastUpdatedBy = appointmentLastUpdatedBy;
    }

    public int getAppIdCounter() {

        int counter = 0;
        try {

            String sqlStatement = "SELECT * FROM appointment";
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                counter = result.getInt("appointmentId");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


}