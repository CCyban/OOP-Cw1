package Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        //Configuring properties
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Views/Initial/Login.fxml"));
        }
        catch(Exception ex) {
            System.out.println("Failed to load login");
        }
        primaryStage.setTitle("Cw1");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1000, 600));

        //Important configuration: Closes all child windows as well if the main window is closed
        primaryStage.setOnHidden(e -> Platform.exit());

        // Show main window
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
