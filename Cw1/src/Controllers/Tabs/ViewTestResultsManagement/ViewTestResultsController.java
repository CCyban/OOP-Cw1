package Controllers.Tabs.ViewTestResultsManagement;

import Classes.Quiz.Result;
import Classes.Quiz.Test;
import Classes.Translating;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class ViewTestResultsController implements Initializable {
    @FXML
    private TableView tableViewResults;

    @FXML
    private TextField textFieldSearch;

    private ObservableList<Result> resultsObservableList = FXCollections.observableArrayList();


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
        loadResultBank(false);

        // Load TableView with its columns & the newly made ObservableList
        initTableViewResults();
    }

    public void initTableViewResults() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Result Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Test, UUID>("resultUUID"));
        idCol.setPrefWidth(100);

        TableColumn testTitleCol = new TableColumn("Test Id");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Test, String>("testUUID"));

        // Add the constructed columns to the TableView
        tableViewResults.getColumns().addAll(idCol, testTitleCol);

        // Hook up the observable list with the TableView
        tableViewResults.setItems(resultsObservableList);
    }

    // Loads the data into the ObservableList
    public void loadResultBank(Boolean useDialogResult) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("resultBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            resultsObservableList.clear();
            resultsObservableList.addAll(retrievedData);
        }
    }

    public void onEditSelectedResultClick(ActionEvent event) {
        System.out.println("clicked");
    }
}
