package Controllers.Tabs.Staff.QuestionManagement;

import Classes.Quiz.Question;
import Classes.Translating;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.util.*;
import java.util.function.Predicate;

public class QuestionManagementController implements Initializable {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewQuestions;

    private ObservableList<Question> questionsObservableList = FXCollections.observableArrayList();

    public enum QuestionDetailsPurpose { Add, Edit, Clone };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> { // TODO: Make it search for a given multiple tags, not just one

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Question> predicateContainsNonCaseStringOnly = q -> (q.getTags().toString().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewQuestions.setItems(questionsObservableList.filtered(predicateContainsNonCaseStringOnly));

        });

        loadQuestionBank(false);
        // Load pre-defined questions into a ObservableList
        //initQuestionsObservableList();

        // Load TableView with its columns & the newly made ObservableList
        initTableViewQuestions();

    }

    public void initQuestionsObservableList() {
        Question q1 = new Question("What is 32+23?", Question.QuestionType.Arithmetic, "55", 5, Arrays.asList("Year 3", "Maths"));
        Question q2 = new Question("What is 1+1? [1, 2, 3, 4]", Question.QuestionType.MultiChoice, "2", 5, Arrays.asList("Year 1", "Maths"));

        questionsObservableList.addAll(q1, q2);
    }

    public void initTableViewQuestions() {
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
        tableViewQuestions.getColumns().addAll(idCol, typeCol, correctMarksCol, tagsCol, questionCol, correctAnswerCol);

        // Hook up the observable list with the TableView
        tableViewQuestions.setItems(questionsObservableList);
    }

    @FXML
    public void onAddNewQuestionClick(ActionEvent event) {
        openQuestionDetails(QuestionDetailsPurpose.Add);
    }

    @FXML
    public void onEditSelectedQuestionClick(ActionEvent event) {
        openQuestionDetails(QuestionDetailsPurpose.Edit);
    }

    @FXML
    public void onCloneSelectedQuestionClick(ActionEvent event) {
        openQuestionDetails(QuestionDetailsPurpose.Clone);
    }

    @FXML
    public void onRemoveSelectedQuestionClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewQuestions.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }

        questionsObservableList.remove(
                tableViewQuestions.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the questionsObservableList
    }

    public void openQuestionDetails(QuestionDetailsPurpose questionDetailsPurpose) {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewQuestions.getSelectionModel().getSelectedItem() == null && questionDetailsPurpose != QuestionDetailsPurpose.Add) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/Staff/QuestionManagement/QuestionDetails.fxml"));
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

        QuestionDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(questionsObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (questionDetailsPurpose) {
            case Add:
                stage.setTitle("Add New Question");
                dialogController.setQuestionDetailsPurpose(QuestionDetailsPurpose.Add);
                break;
            case Edit:
                stage.setTitle("Edit Selected Question");
                dialogController.setQuestionDetailsPurpose(QuestionDetailsPurpose.Edit);
                dialogController.setSelectedQuestion((Question) tableViewQuestions.getSelectionModel().getSelectedItem());
                break;
            case Clone:
                stage.setTitle("Clone Selected Question");
                dialogController.setQuestionDetailsPurpose(QuestionDetailsPurpose.Clone);
                dialogController.setSelectedQuestion((Question) tableViewQuestions.getSelectionModel().getSelectedItem());
                break;
            default:
                System.out.println("questionDetailsPurpose value not recognised");
                break;
        }

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();

        if (questionDetailsPurpose == questionDetailsPurpose.Edit) {
            tableViewQuestions.refresh();   // Updates the TableView so it can show the latest version of an edited question
            // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.

            // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
            // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
        }
    }

    @FXML
    public void onLoadQuestionsClick() {
        loadQuestionBank(true);
    }

    @FXML
    public void onSaveQuestionsClick() {
        saveQuestionBank(true);
    }

    // Loads the data into the ObservableList
    public void loadQuestionBank(Boolean useDialogResult) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("questionBank.ser", useDialogResult);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            questionsObservableList.clear();
            questionsObservableList.addAll(retrievedData);
        }
    }

    // Saves the data from the ObservableList
    public void saveQuestionBank(Boolean useDialogResult) {
        // Sending the data from the ObservableList to be serialised as a questionBank file
        Translating.serialiseObject(questionsObservableList.stream().toList() ,"questionBank.ser", useDialogResult);
    }
}
