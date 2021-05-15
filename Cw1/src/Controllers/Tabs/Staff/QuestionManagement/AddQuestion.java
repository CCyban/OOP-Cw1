package Controllers.Tabs.Staff.QuestionManagement;

import Classes.Quiz.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Classes.Quiz.Question.QuestionType;
import static Classes.Quiz.Question.QuestionType.Manual;
import static Classes.Quiz.Question.QuestionType.Arithmetic;
import static Classes.Quiz.Question.QuestionType.MultiChoice;

public class AddQuestion implements Initializable {

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

        // Load the gathered inputs into the constructor
        Question newQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);

        // Add the new constructed Question to the list
        questionsObservableList.add(newQuestion);

        System.out.println("Question Added");

        // Closes this dialog now that the question is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Question> _questionsObservableList) {
        this.questionsObservableList = _questionsObservableList;
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }
}
