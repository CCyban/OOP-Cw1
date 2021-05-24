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
        Banks.loadResultBank(false, true, resultsObservableList);

        // Load TableView with its columns & the newly made ObservableList
        initTableViewResults();
    }

    public void initTableViewResults() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Result Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Result, UUID>("resultUUID"));
        idCol.setPrefWidth(100);

        TableColumn testTitleCol = new TableColumn("Test Title");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Result, String>("TestTitle"));

        // Add the constructed columns to the TableView
        tableViewResults.getColumns().addAll(idCol, testTitleCol);

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
        stage.setTitle("Review Test Attempt");

        Result result = (Result) tableViewResults.getSelectionModel().getSelectedItem();
        UUID testUUID = result.getTestUUID();

        Banks.loadTestBank(false, true, testsObservableList);

        Test selectedTest = testsObservableList.stream()
                .filter(test -> testUUID.equals(test.getTestUUID()))
                .findFirst()
                .orElse(null);

        dialogController.setSelectedTest(selectedTest);
        dialogController.setSelectedResult(result); // Important that this runs after the setSelectedTest as setSelectedTest generates the UI and this one fills it with the given answers
        dialogController.setTestDoPurpose(DoTestDetailsController.DoTestDetailsPurpose.Edit);

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.show();
    }

    @FXML
    public void onRemoveSelectedResultClick(ActionEvent event) {
        // If a result is not selected then the action cannot proceed
        if (tableViewResults.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No result is selected with your action").show();
            return;
        }

        resultsObservableList.remove(
                tableViewResults.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the resultsObservableList
        new Alert(Alert.AlertType.INFORMATION, "Result Deleted").show();
    }

    @FXML
    public void onLoadResultsClick(ActionEvent event) {
        Banks.loadResultBank(true, true, resultsObservableList);
    }

    @FXML
    public void onSaveResultsClick(ActionEvent event) {
        Banks.saveResultBank(true, true, resultsObservableList);
    }
}
