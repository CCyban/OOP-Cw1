package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AfterLoginController {

    @FXML
    private Button buttonLogout;

    @FXML
    public void onLogoutClick(ActionEvent event) {
        Stage stage = (Stage) buttonLogout.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to logout").show();
        }
    }
}


