package Controllers.Tabs.TestManagement;

import Classes.Banks;
import Classes.Quiz.Test;
import Classes.Translating;
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
import java.util.*;
import java.util.function.Predicate;

public class TestManagementController implements Initializable {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewTests;

    private ObservableList<Test> testsObservableList = FXCollections.observableArrayList();

    public enum TestDetailsPurpose { Add, Edit };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Test> predicateContainsNonCaseStringOnly = q -> (q.getTestTitle().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewTests.setItems(testsObservableList.filtered(predicateContainsNonCaseStringOnly));
        });

        // Load (if any) stored tests into a ObservableList
        Banks.loadTestBank(false, testsObservableList);

        // Load TableView with its columns & the newly made ObservableList
        initTableViewTests();
    }

    public void initTestsObservableList() {

        //Question q1 = new Question("What is 32+23?", Question.QuestionType.Arithmetic, "55", 5, Arrays.asList("Year 3", "Maths"));
        //Question q2 = new Question("What is 1+1? [1, 2, 3, 4]", Question.QuestionType.MultiChoice, "2", 5, Arrays.asList("Year 1", "Maths"));

        //Test t1 = new Test("End of Year Maths Exam", Arrays.asList(q1, q2));
        //Test t2 = new Test("Summer Maths fun", Arrays.asList(q2));

        // Load tests from stored data here instead of hardcoded data

        //System.out.println(t1);
        //testObservableList.addAll(t1, t2);
    }

    public void initTableViewTests() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Test, UUID>("testUUID"));
        idCol.setPrefWidth(100);

        TableColumn testTitleCol = new TableColumn("Test Title");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Test, String>("TestTitle"));

        TableColumn totalMarksCol = new TableColumn("Total Marks");
        totalMarksCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("totalMarks"));

        // Add the constructed columns to the TableView
        tableViewTests.getColumns().addAll(idCol, testTitleCol, totalMarksCol);

        // Hook up the observable list with the TableView
        tableViewTests.setItems(testsObservableList);
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
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }
        testsObservableList.remove(
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/TestManagement/TestDetails.fxml"));
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

        TestDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(testsObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (testDetailsPurpose) {
            case Add:
                stage.setTitle("Add New Test");
                dialogController.setTestDetailsPurpose(TestDetailsPurpose.Add);
                Test newTest = new Test("", new ArrayList<>());
                testsObservableList.add(newTest);
                dialogController.setSelectedTest(newTest);

                // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
                stage.showAndWait();

                // If the user didn't finish adding the test, remove it
                if (newTest.getTestTitle() == "") {
                    testsObservableList.remove(newTest);
                }
                break;
            case Edit:
                stage.setTitle("Edit Selected Test");
                dialogController.setTestDetailsPurpose(TestDetailsPurpose.Edit);
                dialogController.setSelectedTest((Test) tableViewTests.getSelectionModel().getSelectedItem());

                // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
                stage.showAndWait();
                break;
            default:
                System.out.println("testDetailsPurpose value not recognised");
                break;
        }

        tableViewTests.refresh();   // Updates the TableView so it can show the latest version of all tests
        // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.
        // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
        // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
    }

    @FXML
    public void onLoadTestsClick() {
        Banks.loadTestBank(true, testsObservableList);
    }

    @FXML
    public void onSaveTestsClick() {
        Banks.saveTestBank(true, testsObservableList);
    }
}
