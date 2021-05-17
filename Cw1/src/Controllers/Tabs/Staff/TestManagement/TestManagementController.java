package Controllers.Tabs.Staff.TestManagement;

import Classes.Quiz.Question;
import Classes.Quiz.Test;
import Classes.Translating;
import Controllers.Tabs.Staff.QuestionManagement.QuestionDetailsController;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class TestManagementController implements Initializable {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewTests;

    private ObservableList<Test> testObservableList = FXCollections.observableArrayList();

    public enum TestDetailsPurpose { Add, Edit };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Test> predicateContainsNonCaseStringOnly = q -> (q.getTestTitle().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewTests.setItems(testObservableList.filtered(predicateContainsNonCaseStringOnly));
        });

        // Load (if any) stored tests into a ObservableList
        loadTestBank(false);

        // Load TableView with its columns & the newly made ObservableList
        initTableViewTests();
    }

    public void initTestsObservableList() {

        Question q1 = new Question("What is 32+23?", Question.QuestionType.Arithmetic, "55", 5, Arrays.asList("Year 3", "Maths"));
        Question q2 = new Question("What is 1+1? [1, 2, 3, 4]", Question.QuestionType.MultiChoice, "2", 5, Arrays.asList("Year 1", "Maths"));

        Test t1 = new Test("End of Year Maths Exam", Arrays.asList(q1, q2));
        Test t2 = new Test("Summer Maths fun", Arrays.asList(q2));

        // Load tests from stored data here instead of hardcoded data

        System.out.println(t1);
        testObservableList.addAll(t1, t2);
    }

    public void initTableViewTests() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Test, UUID>("testUUID"));
        idCol.setPrefWidth(100);

        TableColumn testTitleCol = new TableColumn("Test Title");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Test, String>("TestTitle"));

        TableColumn questionsCol = new TableColumn("Questions");
        questionsCol.setCellValueFactory(new PropertyValueFactory<Test, List<Question>>("Questions"));

        // Add the constructed columns to the TableView
        tableViewTests.getColumns().addAll(idCol, testTitleCol, questionsCol);

        // Hook up the observable list with the TableView
        tableViewTests.setItems(testObservableList);
    }

    @FXML
    public void onAddNewTestClick(ActionEvent event) {
        openTestDetails(TestDetailsPurpose.Add);
    }

    @FXML
    public void onEditSelectedTestClick(ActionEvent event) {
        openTestDetails(TestDetailsPurpose.Edit);
    }

    @FXML
    public void onRemoveSelectedTestClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewTests.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }
        testObservableList.remove(
                tableViewTests.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the questionsObservableList
    }


    public void openTestDetails(TestDetailsPurpose testDetailsPurpose) {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewTests.getSelectionModel().getSelectedItem() == null && testDetailsPurpose != TestDetailsPurpose.Add) {
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/Staff/TestManagement/TestDetails.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            System.out.println("Could not find resource");
        }

        Scene scene = new Scene(parent, 900, 500);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        TestDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(testObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (testDetailsPurpose) {
            case Add:
                stage.setTitle("Add New Test");
                dialogController.setTestDetailsPurpose(TestDetailsPurpose.Add);
                break;
            case Edit:
                stage.setTitle("Edit Selected Test");
                dialogController.setTestDetailsPurpose(TestDetailsPurpose.Edit);
                dialogController.setSelectedTest((Test) tableViewTests.getSelectionModel().getSelectedItem());
                break;
            default:
                System.out.println("testDetailsPurpose value not recognised");
                break;
        }

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();

        if (testDetailsPurpose == testDetailsPurpose.Edit) {
            tableViewTests.refresh();   // Updates the TableView so it can show the latest version of an edited test
            // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.

            // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
            // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
        }
    }

    @FXML
    public void onLoadTestsClick() {
        loadTestBank(true);
    }

    @FXML
    public void onSaveTestsClick() {
        saveTestBank(true);
    }


    public void loadTestBank(Boolean useDialogResult) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("testBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            testObservableList.clear();
            testObservableList.addAll(retrievedData);
        };
    }

    public void saveTestBank(Boolean useDialogResult) {
        // Sending the data from the ObservableList to be serialised as a questionBank file
        Translating.serialiseObject(testObservableList.stream().toList() ,"testBank.ser", useDialogResult);
    }
}
