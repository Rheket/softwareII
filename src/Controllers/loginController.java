package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class loginController {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;

    @FXML private void handleLogin() {

        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();



    }

}
