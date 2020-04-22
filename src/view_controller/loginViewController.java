package view_controller;

import DAO.DBConnection;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class loginViewController implements Initializable {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button loginButton;

    boolean spanish = false;

    public void handleLogin(ActionEvent actionEvent) throws IOException {

        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");


        try {

            DBConnection.makeConnection();
            //create statement obj
            Statement stmt = DBConnection.conn.createStatement();
            //SQL statement
            String sqlStatement = "SELECT * FROM user";

            // Execute statement and create ResultSet obj
            ResultSet result = stmt.executeQuery(sqlStatement);

            ResourceBundle rb = null;

            try {

                rb = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());

            } catch (Exception e) {
                e.printStackTrace();
            }

            while (result.next()) {

                if (username.equals(result.getString("userName")) && password.equals(result.getString("password"))) {

                    //log login timestamp if log in is successful
                    String filename = "login_Timestamps.txt";

                    //Get local time and convert to UTC for logs
                    ZonedDateTime now = ZonedDateTime.now();
                    ZonedDateTime nowUTC = now.withZoneSameInstant(ZoneId.of("UTC"));

                    //set current user instance
                    //User user = new User();
                    User.setUsername(username);

                    //append data to existing file instead of overwriting
                    FileWriter fileWriter = new FileWriter(filename, true);
                    PrintWriter outputFile = new PrintWriter(fileWriter);

                    outputFile.println( username + " logged in at " + nowUTC);

                    outputFile.close();

                    // switch to main screen after logging timestamp
                    Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
                    Scene mainScreenScene = new Scene(mainScreenParent);

                    Stage mainScreenWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    mainScreenWindow.setScene(mainScreenScene);
                    mainScreenWindow.show();


                } else {

                    if (!spanish) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setTitle("Error logging in");
                        alert.setContentText("Unable to log in with provided username and password. Please try again.");

                        alert.showAndWait();
                    } else {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        assert rb != null;
                        alert.setTitle(rb.getString("loginError"));
                        alert.setContentText(rb.getString("alertMessage"));

                        alert.showAndWait();

                    }

                }

            }


        } catch (SQLException | ClassNotFoundException e) {

            e.printStackTrace();

        }

        check15Min();

    }

    private void check15Min() {

        LocalDateTime now = LocalDateTime.now();

        try {

            Statement stmt = DBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment WHERE start >= '" + now + "' and start <= '" + now.plusMinutes(15) + "'";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while(result.next()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Appointment in 15 minutes");
                alert.setContentText("There is an upcoming appointment at " + result.getString("start"));

                alert.showAndWait();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        // set to spanish if Locale.getDefault is in es
        ResourceBundle resource = null;

        try {
            resource = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Locale.getDefault().getLanguage().equals("es")) {

            assert resource != null;
            usernameTextField.setPromptText(resource.getString("username"));
            passwordTextField.setPromptText(resource.getString("password"));
            loginButton.setText(resource.getString("login"));

            spanish = true;

        }

    }


}