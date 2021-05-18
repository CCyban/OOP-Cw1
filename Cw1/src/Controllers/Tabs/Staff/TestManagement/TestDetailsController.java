package Controllers.Tabs.Staff.TestManagement;

import Classes.Quiz.Question;
import Classes.Quiz.Test;
import Classes.Translating;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class TestDetailsController implements Initializable {

    @FXML
    private Button buttonFinishTest;

    @FXML
    private TextField textFieldTestTitleInput;

    @FXML
    private TableView tableViewQuestionBank;

    @FXML
    private TableView tableViewTestQuestions;

    @FXML
    private Label labelTotalTestMarks;

    private ObservableList<Test> testObservableList;
    private TestManagementController.TestDetailsPurpose testDetailsPurpose;
    private Test selectedTest;

    private ObservableList<Question> questionBankObservableList = FXCollections.observableArrayList();
    private ObservableList<Question> testQuestionsObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Loads (if any) stored questions into a ObservableList
        loadQuestionBank(false);

        initQuestionBankTableView();
        initTestQuestionsTableView();
    }

    @FXML
    public void onFinishTestClick(ActionEvent event) {
        // The Gathering begins...

        // Gather Test Title value
        String testTitleInput = textFieldTestTitleInput.getText();
        if (testTitleInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // No need to gather the testQuestions list because I can just reference it directly

        // The Gathering is complete

        // Time to do whatever the purpose of this dialog is with the inputted values
        switch (testDetailsPurpose) {
            case Add:
                // Load the gathered inputs into the constructor
                Test newTest = new Test(testTitleInput, new LinkedList<>(testQuestionsObservableList));
                // Add the new constructed Test to the list
                testObservableList.add(newTest);
                break;
            case Edit:
                selectedTest.setTestTitle(testTitleInput);
                break;
            default: System.out.println("Unknown testDetailPurpose value"); break;
        }

        // Closes this dialog now that the test is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Test> _testsObservableList) {
        this.testObservableList = _testsObservableList;
    }

    public void setTestDetailsPurpose(TestManagementController.TestDetailsPurpose _testDetailsPurpose) {
        this.testDetailsPurpose = _testDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (testDetailsPurpose) {
            case Add: buttonFinishTest.setText("Create Test"); break;
            case Edit: buttonFinishTest.setText("Update Test"); break;
            default: System.out.println("Unknown TestDetailPurpose"); break;
        }
    }

    public void setSelectedTest(Test _selectedTest) {
        this.selectedTest = _selectedTest;

        this.testQuestionsObservableList = FXCollections.observableList(selectedTest.getQuestions());

        tableViewTestQuestions.setItems(testQuestionsObservableList);
        textFieldTestTitleInput.setText(selectedTest.getTestTitle());
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initTestQuestionsTableView() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Question, UUID>("questionUUID"));
        idCol.setPrefWidth(100);

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<Question, Question.QuestionType>("questionType"));

        TableColumn correctMarksCol = new TableColumn("Possible Marks");
        correctMarksCol.setCellValueFactory(new PropertyValueFactory<Question, Integer>("CorrectMarks"));

        TableColumn tagsCol = new TableColumn("Tags");
        tagsCol.setCellValueFactory(new PropertyValueFactory<Question, List<String>>("Tags"));

        TableColumn questionCol = new TableColumn("Question");
        questionCol.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));

        TableColumn correctAnswerCol = new TableColumn("Correct Answer");
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));

        // Add the constructed columns to the TableView
        tableViewTestQuestions.getColumns().addAll(idCol, typeCol, correctMarksCol, tagsCol, questionCol, correctAnswerCol);

        // Hook up observable list with the TableView
        tableViewTestQuestions.setItems(testQuestionsObservableList);
    }

    public void initQuestionBankTableView() {
        // Set the TableColumns up for the TableView
        TableColumn idColQuestionBank = new TableColumn("Id");
        idColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, UUID>("questionUUID"));
        idColQuestionBank.setPrefWidth(100);

        TableColumn typeColQuestionBank = new TableColumn("Type");
        typeColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, Question.QuestionType>("questionType"));

        TableColumn correctMarksColQuestionBank = new TableColumn("Possible Marks");
        correctMarksColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, Integer>("CorrectMarks"));

        TableColumn tagsColQuestionBank = new TableColumn("Tags");
        tagsColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, List<String>>("Tags"));

        TableColumn questionColQuestionBank = new TableColumn("Question");
        questionColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));

        TableColumn correctAnswerColQuestionBank = new TableColumn("Correct Answer");
        correctAnswerColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));

        // Add the constructed columns to the TableView
        tableViewQuestionBank.getColumns().addAll(idColQuestionBank, typeColQuestionBank,
                correctMarksColQuestionBank, tagsColQuestionBank, questionColQuestionBank, correctAnswerColQuestionBank);

        // Hook up observable list with the TableView
        tableViewQuestionBank.setItems(questionBankObservableList);
    }

    public void loadQuestionBank(Boolean useDialogResult) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("questionBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            questionBankObservableList.addAll(retrievedData);
        }
    }

    @FXML
    public void onAddToTestClick(ActionEvent event) {
        testQuestionsObservableList.add((Question) tableViewQuestionBank.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onRemoveFromTestClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewTestQuestions.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }

        testQuestionsObservableList.remove( (Question)
                tableViewTestQuestions.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the testQuestionsObservableList

        //selectedTest.removeQuestion((Question) tableViewTestQuestions.getSelectionModel().getSelectedItem());
    }
}
