package view_controller;

import DAO.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class loginViewController implements Initializable {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button loginButton;

    boolean spanish = false;

    @FXML private void handleLogin() {

        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();

        try {

            DBConnection.makeConnection();
            //create statement obj
            Statement stmt = DBConnection.conn.createStatement();
            //SQL statement
            String sqlStatement = "SELECT * FROM user";

            // Execute statement and create ResultSet obj
            ResultSet result = stmt.executeQuery(sqlStatement);

            ResourceBundle rb = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());

            while (result.next()) {

                if (username.equals(result.getString("userName")) && password.equals(result.getString("password"))) {

                    System.out.println("SUCCESS!!!!");

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
                        alert.setTitle(rb.getString("loginError"));
                        alert.setContentText(rb.getString("alertMessage"));

                        alert.showAndWait();

                    }

                    //testing by printing out to console
                    //System.out.println("ERROR Logging in");
                    //System.out.println(username);
                    //System.out.println(password);
                }

            }


        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        // set to spanish if Locale.getDefault is in es
        ResourceBundle resource = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("es")) {

            usernameTextField.setPromptText(resource.getString("username"));
            passwordTextField.setPromptText(resource.getString("password"));
            loginButton.setText(resource.getString("login"));

            spanish = true;

        }

    }

}
