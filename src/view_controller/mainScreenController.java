package view_controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainScreenController {

    public void handleCalendarViewSwitch(ActionEvent actionEvent) throws IOException {

        Parent calendarViewParent = FXMLLoader.load(getClass().getResource("/view_controller/calendarView.fxml"));
        Scene calendarViewScene = new Scene(calendarViewParent);

        Stage calendarViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        calendarViewWindow.setScene(calendarViewScene);
        calendarViewWindow.show();
    }

    public void handleCustomerViewSwitch(ActionEvent actionEvent) throws IOException {

        Parent customerViewParent = FXMLLoader.load(getClass().getResource("/view_controller/customerView.fxml"));
        Scene customerViewScene = new Scene(customerViewParent);

        Stage customerViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        customerViewWindow.setScene(customerViewScene);
        customerViewWindow.show();

    }

    public void handleReportsViewSwitch(ActionEvent actionEvent) throws IOException {

        Parent reportsViewParent = FXMLLoader.load(getClass().getResource("/view_controller/reportsView.fxml"));
        Scene reportsViewScene = new Scene(reportsViewParent);

        Stage reportsViewWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        reportsViewWindow.setScene(reportsViewScene);
        reportsViewWindow.show();

    }

}
