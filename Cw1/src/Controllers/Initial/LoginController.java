package Controllers.Initial;

import Classes.Translating;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField textFieldAccountNumber;

    @FXML
    private Button buttonSignIn;

    private SimpleBooleanProperty canSignIn = new SimpleBooleanProperty();

    public final SimpleBooleanProperty canSignInProperty() {
        return canSignIn;
    };

    public final Boolean getCanSignIn() {
        return canSignIn.getValue();
    }

    public final void setCanSignIn(Boolean _canSignIn) {
        this.canSignIn.setValue(_canSignIn);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bindings
        buttonSignIn.disableProperty().bind(canSignIn.not());

        // Listeners
        textFieldAccountNumber.textProperty().addListener((Observable, oldValue, newValue) -> {
            if (newValue == "")
                setCanSignIn(false);
            else
                setCanSignIn(true);
        });

        //GenerateBanksIfUnfound();
    }

    @FXML
    public void onEnter(ActionEvent event) {
        if (getCanSignIn())
            SignIn(textFieldAccountNumber.getText());
    }

    @FXML
    public void onClick(ActionEvent event) {
        SignIn(textFieldAccountNumber.getText());
    }

    public void SignIn(String accountNumber) {
        if (isSignInSuccessful(accountNumber)) {
            Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
            alertSignIn.setTitle("Sign In Attempt");
            alertSignIn.setHeaderText("Sign In Attempt Successful");
            alertSignIn.showAndWait();

            updateStageOnSuccessfulSignIn();
        }
        else
        {
            Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
            alertSignIn.setTitle("Sign In Attempt");
            alertSignIn.setHeaderText("Sign In Attempt Failed");
            alertSignIn.showAndWait();

            textFieldAccountNumber.requestFocus();
        }

    }

    public Boolean isSignInSuccessful(String accountNumber) {
        System.out.println("Attempting to sign in as '" + accountNumber + "'...");
        if (accountNumber.equals("123"))
            return true;
        else
            return false;
    }

    public void updateStageOnSuccessfulSignIn() {
        Stage stage = (Stage) buttonSignIn.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Initial/Tabs.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
        } catch (Exception ex) {
            System.out.println(ex);

            new Alert(Alert.AlertType.ERROR, "Failed to load next stage").show();
        }
    }
}
