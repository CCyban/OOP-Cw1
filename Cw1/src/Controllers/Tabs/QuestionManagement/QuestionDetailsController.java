package Controllers.Tabs.QuestionManagement;

import Classes.RegexTextFormatters;
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
import static Classes.Quiz.Question.QuestionType.Arithmetic;
import static Classes.Quiz.Question.QuestionType.MultiChoice;

public class QuestionDetailsController implements Initializable {

    @FXML
    private Button buttonFinishQuestion;

    @FXML
    private ComboBox comboBoxQuestionTypeInput;

    @FXML
    private TextArea textAreaQuestionInput;

    @FXML
    private Label labelQuestionHelpText;

    @FXML
    private TextArea textAreaAnswerInput;

    @FXML
    private Label labelAnswerHelpText;

    @FXML
    private TextField textFieldAmountMarksInput;

    @FXML
    private TextField textFieldTagsInput;

    private ObservableList<Question> questionsObservableList;
    private QuestionManagementController.QuestionDetailsPurpose questionDetailsPurpose;
    private Question selectedQuestion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBoxItemsForInputs();
        initTextFormattersForInputs();
    }

    @FXML
    public void onFinishQuestionClick(ActionEvent event) {
        // The Gathering begins...

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


        // The Gathering is complete

        // Time to do whatever the purpose of this dialog is with the inputted values
        switch (questionDetailsPurpose) {
            case Add:
                // Load the gathered inputs into the constructor
                Question newQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
                // Add the new constructed Question to the list
                questionsObservableList.add(newQuestion);
                break;
            case Edit:
                // Update the question data with the newly edited values
                selectedQuestion.EditQuestion(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
                break;
            case Clone: //TODO: Add Cloned question to the same test-bank by default (meaning that this isn't a duplicate of the Add case)
                // Create the newly made clone of the question
                Question newClonedQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
                // Add the new constructed cloned question into the list
                questionsObservableList.add(newClonedQuestion);
                break;
            default: System.out.println("Unknown QuestionDetailPurpose"); break;
        }

        // Closes this dialog now that the question is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Question> _questionsObservableList) {
        this.questionsObservableList = _questionsObservableList;
    }

    public void setQuestionDetailsPurpose(QuestionManagementController.QuestionDetailsPurpose _questionDetailsPurpose) {
        this.questionDetailsPurpose = _questionDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (questionDetailsPurpose) {
            case Add:
                buttonFinishQuestion.setText("Create Question");
                // Must make the user select a question type first
                textAreaQuestionInput.setDisable(true);
                textAreaAnswerInput.setDisable(true);
                break;
            case Edit:
                buttonFinishQuestion.setText("Update Question");
                break;
            case Clone:
                buttonFinishQuestion.setText("Create Cloned Question");
                comboBoxQuestionTypeInput.setDisable(true); // User is not allowed to change type when cloning according to the requirements
                break;
            default: System.out.println("Unknown QuestionDetailPurpose"); break;
        }
    }

    public void setSelectedQuestion(Question _selectedQuestion) {
        this.selectedQuestion = _selectedQuestion;

        comboBoxQuestionTypeInput.setValue(selectedQuestion.getQuestionType());
        textAreaQuestionInput.setText(selectedQuestion.getQuestion());
        textAreaAnswerInput.setText(selectedQuestion.getCorrectAnswer());
        textFieldAmountMarksInput.setText(String.valueOf(selectedQuestion.getCorrectMarks()));
        textFieldTagsInput.setText(selectedQuestion.getTags().toString().substring(1, selectedQuestion.getTags().toString().length() - 1)); // Using substring to trim the edges by 1 character each so that the []s are removed
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initComboBoxItemsForInputs() {
        // Inputting the questionTypes into the comboBox, any new types can be added here
        ObservableList<QuestionType> questionTypes = FXCollections.observableArrayList(
                Arithmetic, MultiChoice);
        comboBoxQuestionTypeInput.setItems(questionTypes);
    }

    public void initTextFormattersForInputs() {
        // Adding a integer-only TextFormatter to the textField 'Amount of Marks' input
        RegexTextFormatters.set3WholeNumbersOnlyTextFormatter(textFieldAmountMarksInput);

        // Adding a single-line only TextFormatter to the textArea 'Question' input
        RegexTextFormatters.setSingleLineTextFormatter(textAreaQuestionInput);

        // Applying the same single-line only TextFormatter to the textArea 'Answer' input
        RegexTextFormatters.setSingleLineTextFormatter(textAreaAnswerInput);
    }

    @FXML
    public void onQuestionTypeSelect(ActionEvent event) {
        // If one of the inputs is disabled then, enable them now that a question type is selected
        if (textAreaQuestionInput.isDisable()) {
            textAreaQuestionInput.setDisable(false);
            textAreaAnswerInput.setDisable(false);
        }

        // Always clear the text inputs when the user selects a question type so the applied TextFilter can work from the start
        textAreaQuestionInput.clear();
        textAreaAnswerInput.clear();

        updateUIBasedOnNewQuestionType((QuestionType) comboBoxQuestionTypeInput.getValue());
    }

    public void updateUIBasedOnNewQuestionType(QuestionType questionType) {
        switch (questionType) {
            case Arithmetic:
                // Set PromptText values
                labelQuestionHelpText.setText("Example: What is 2+2?");
                labelAnswerHelpText.setText("Example: 4\nOnly numbers and decimals can be used in the answer due to the question type");

                // Set TextFormatter
                RegexTextFormatters.setNumbersOnlyTextFormatter(textAreaAnswerInput);

                // End case
                break;

            case MultiChoice:
                // Set PromptText
                labelQuestionHelpText.setText("Example: What is 2+2?    (1, 2, Three, Orange)\nThe choices are a part of the question and are in a pair of () with each choice separated by ,");
                labelAnswerHelpText.setText("Example: 2");

                // Set TextFormatter to null as one isn't needed for this question type
                if (textAreaAnswerInput.getTextFormatter() != null) {
                    textAreaAnswerInput.setTextFormatter(null); // Overwrite the preexisting text-formatter
                }

                // End case
                break;

            default: System.out.println("questionType value not recognised"); break;
        }
    }
}


// TODO: Add validation to see if the user inputted a correct question format with the MultiChoice and that the answer exists within the inputted options. (Would run at the create question button)