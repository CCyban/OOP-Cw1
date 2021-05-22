package Controllers.Tabs.ViewTestResultsManagement;

import Classes.Banks;
import Classes.Quiz.Result;
import Classes.Quiz.Test;
import Controllers.Tabs.DoTestManagement.DoTestDetailsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class ViewTestResultsController implements Initializable {
    @FXML
    private TableView tableViewResults;

    @FXML
    private TextField textFieldSearch;

    private ObservableList<Result> resultsObservableList = FXCollections.observableArrayList();

    private ObservableList<Test> testsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Result> predicateContainsNonCaseStringOnly = q -> (q.getResultUUID().toString().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewResults.setItems(resultsObservableList.filtered(predicateContainsNonCaseStringOnly));
        });

        // Load (if any) stored tests into a ObservableList
        Banks.loadResultBank(false, resultsObservableList);

        // Load TableView with its columns & the newly made ObservableList
        initTableViewResults();
    }

    public void initTableViewResults() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Result Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Result, UUID>("resultUUID"));
        idCol.setPrefWidth(100);

        TableColumn testTitleCol = new TableColumn("Test Id");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Result, String>("testUUID"));

        TableColumn totalMarksAchievedCol = new TableColumn("Total Marks Achieved");
        totalMarksAchievedCol.setCellValueFactory(new PropertyValueFactory<Result, Integer>("totalMarksAchieved"));

        // Add the constructed columns to the TableView
        tableViewResults.getColumns().addAll(idCol, testTitleCol, totalMarksAchievedCol);

        // Hook up the observable list with the TableView
        tableViewResults.setItems(resultsObservableList);
    }


    public void onEditSelectedResultClick(ActionEvent event) {
        System.out.println("clicked");
        openTestView();
    }

    public void openTestView() {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewResults.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/DoTestManagement/DoTestDetails.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            System.out.println("Could not find resource");
        }

        Scene scene = new Scene(parent, 1200, 700);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        DoTestDetailsController dialogController = fxmlLoader.getController();

        // Updating the stage & classes with key details depending on why the dialog is being used
        stage.setTitle("Complete Test");

        Result result = (Result) tableViewResults.getSelectionModel().getSelectedItem();
        UUID testUUID = result.getTestUUID();

        Banks.loadTestBank(false, testsObservableList);

        Test selectedTest = testsObservableList.stream()
                .filter(test -> testUUID.equals(test.getTestUUID()))
                .findFirst()
                .orElse(null);

        dialogController.setSelectedTest(selectedTest);

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.show();

        dialogController.setSelectedResult(result); // Here so it runs after the init method

        //TODO: Give a setPurpose prop so it knows to not make a new result on finish and just edit the current given one instead

        tableViewResults.refresh();   // Updates the TableView so it can show the latest version of all results
        // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.
        // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
        // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
    }

    @FXML
    public void onRemoveSelectedResultClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewResults.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }
        resultsObservableList.remove(
                tableViewResults.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the resultsObservableList
    }

    @FXML
    public void onLoadResultsClick(ActionEvent event) {
        Banks.loadResultBank(true, resultsObservableList);
    }

    @FXML
    public void onSaveResultsClick(ActionEvent event) {
        Banks.saveResultBank(true, resultsObservableList);
    }
}
