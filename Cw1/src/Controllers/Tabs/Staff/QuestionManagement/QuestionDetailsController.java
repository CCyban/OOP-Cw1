package Controllers.Tabs.Staff.QuestionManagement;

import Classes.Quiz.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Classes.Quiz.Question.QuestionType;
import static Classes.Quiz.Question.QuestionType.Manual;
import static Classes.Quiz.Question.QuestionType.Arithmetic;
import static Classes.Quiz.Question.QuestionType.MultiChoice;

public class QuestionDetailsController implements Initializable {

    @FXML
    private Button buttonCreateQuestion;

    @FXML
    private ComboBox comboBoxQuestionTypeInput;

    @FXML
    private TextArea textAreaQuestionInput;

    @FXML
    private TextArea textAreaAnswerInput;

    @FXML
    private TextField textFieldAmountMarksInput;

    @FXML
    private TextField textFieldTagsInput;

    private ObservableList<Question> questionsObservableList;
    private QuestionManagementController.QuestionDetailsPurpose questionDetailsPurpose;
    private Question selectedQuestion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Inputting the questionTypes into the comboBox, any new types can be added here
        ObservableList<QuestionType> questionTypes = FXCollections.observableArrayList(
                Arithmetic, MultiChoice, Manual);
        comboBoxQuestionTypeInput.setItems(questionTypes);

        // Adding a integer-only TextFormatter to the textField 'Amount of Marks' input
        textFieldAmountMarksInput.setTextFormatter(new TextFormatter<>(c ->
        {
            // If the user is trying to make the input empty, let them
            if ( c.getControlNewText().isEmpty() )
                return c;

            // If the new length of the text would be more than 3 digits, then do not allow
            if (c.getControlNewText().length() > 3)
                return null;

            // Checking if new character passes as an integer
            try {
                 Integer.parseInt(c.getControlNewText());
            }
            // Means that the new character isn't an integer, therefore isn't a valid input which cannot be allowed
            catch (NumberFormatException e)
            {
                return null;
            }

            // Returns (allows) the character now that it passed the integer check
            return c;
        }));

        // Adding a single-line only TextFormatter to the textArea 'Question' input
        textAreaQuestionInput.setTextFormatter(new TextFormatter<>(c ->
        {
            // If the user is trying to make the input empty, let them
            if ( c.getControlNewText().isEmpty() )
                return c;

            // If the new length of the text would be more than 1024 characters, then do not allow
            if (c.getControlNewText().length() > 1024)
                return null;

            // If the new text contains a new line, do not allow
            if (c.getControlNewText().contains("\n"))
                return null;

            // Allows the new text now that it passed the checks
            return c;
        }));

        // Applying the same single-line only TextFormatter to the textArea 'Answer' input
        textAreaAnswerInput.setTextFormatter(new TextFormatter<>(c ->
        {
            // If the user is trying to make the input empty, let them
            if ( c.getControlNewText().isEmpty() )
                return c;

            // If the new length of the text would be more than 1024 characters, then do not allow
            if (c.getControlNewText().length() > 1024)
                return null;

            // If the new text contains a new line, do not allow
            if (c.getControlNewText().contains("\n"))
                return null;

            // Allows the new text now that it passed the checks
            return c;
        }));
    }

    @FXML
    public void onAddNewQuestionClick(ActionEvent event) {


        // Gathering begins...

        // Gather QuestionType value
        QuestionType questionTypeInput = (QuestionType) comboBoxQuestionTypeInput.getValue();
        if (questionTypeInput == null) {
            showIncompleteFormError();
            return;
        }

        // Gather Question value
        String questionInput = textAreaQuestionInput.getText();
        if (questionInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }
        // Gather Answer value
        String answerInput = textAreaAnswerInput.getText();
        if (answerInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // Gather Amount of Marks value
        if (textFieldAmountMarksInput.getLength() == 0) {
            showIncompleteFormError();
            return;
        }
        int amountMarksInput = Integer.parseInt(textFieldAmountMarksInput.getText());


        // Gather a list of Tags
        List<String> tagsInput = Arrays.stream(textFieldTagsInput.getText().split(","))  // Makes a list with each new element after a comma, then converts it into a stream
                .map(String::strip)                                                            // Strips whitespace from the stream (means that it only strips the edges of the previous elements)
                .collect(Collectors.toList());                                                // Converts the stripped stream back into a list
        System.out.println(tagsInput);


        // Gathering complete

        // Time to do whatever the purpose of this dialog is with the inputted values
        if (questionDetailsPurpose == QuestionManagementController.QuestionDetailsPurpose.Add) {
            // Load the gathered inputs into the constructor
            Question newQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
            // Add the new constructed Question to the list
            questionsObservableList.add(newQuestion);
        }
        else if (questionDetailsPurpose == QuestionManagementController.QuestionDetailsPurpose.Edit) {
            // Create the updated version of the question
            Question updatedQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
            // Find where the old version is kept in the list
            int selectedQuestionIndex = questionsObservableList.indexOf(selectedQuestion);
            // Update the old question with the newly edited question
            questionsObservableList.set(selectedQuestionIndex, updatedQuestion);
        }
        else if (questionDetailsPurpose == QuestionManagementController.QuestionDetailsPurpose.Clone) {     //TODO: Add Cloned question to the same test-bank by default
            // Create the newly made clone of the question
            Question newClonedQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
            // Add the new constructed cloned question into the list
            questionsObservableList.add(newClonedQuestion);
        }

        // Closes this dialog now that the question is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Question> _questionsObservableList) {
        this.questionsObservableList = _questionsObservableList;
    }

    public void setQuestionDetailsPurpose(QuestionManagementController.QuestionDetailsPurpose _questionDetailsPurpose) {
        this.questionDetailsPurpose = _questionDetailsPurpose;
    }

    public void setSelectedQuestion(Question _selectedQuestion) {
        this.selectedQuestion = _selectedQuestion;

        comboBoxQuestionTypeInput.setValue(selectedQuestion.getQuestionType());
        textAreaQuestionInput.setText(selectedQuestion.getQuestion());
        textAreaAnswerInput.setText(selectedQuestion.getCorrectAnswer());
        textFieldAmountMarksInput.setText(String.valueOf(selectedQuestion.getCorrectMarks()));
        textFieldTagsInput.setText(selectedQuestion.getTags().toString());
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }
}
