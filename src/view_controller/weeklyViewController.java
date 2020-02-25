package view_controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class weeklyViewController implements Initializable {

    @FXML private RadioButton monthView;
    @FXML private RadioButton weekView;

    private ToggleGroup viewToggleGroup;


    public void mainMenu(ActionEvent actionEvent) throws IOException {

        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view_controller/mainScreen.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage mainMenuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        mainMenuWindow.setScene(mainMenuScene);
        mainMenuWindow.show();
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {

        viewToggleGroup = new ToggleGroup();
        this.monthView.setToggleGroup(viewToggleGroup);
        this.weekView.setToggleGroup(viewToggleGroup);

    }
}