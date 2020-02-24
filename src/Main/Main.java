package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/*
Server name: 3.227.166.251
Database name: U060YT
Username: U060YT
Password: 53688667824
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view_controller/loginView.fxml"));
        //primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {

        //ResourceBundle rb = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());

        /*
        if (Locale.getDefault().getLanguage().equals("es")) {

            System.out.println(rb.getString("hello") + " " + rb.getString("world"));

        }

         */

        launch(args);

    }

}