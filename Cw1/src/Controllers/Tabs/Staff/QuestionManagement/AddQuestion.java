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
import java.util.Collections;
import java.util.ResourceBundle;

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

    // Todo: Add a tags control and pass the tags data into the constructor
    @FXML
    public void onAddNewQuestionClick(ActionEvent event) {

        // Gather user inputs
        QuestionType questionTypeInput = (QuestionType) comboBoxQuestionTypeInput.getValue();
        String questionInput = textAreaQuestionInput.getText();
        String answerInput = textAreaAnswerInput.getText();

        int amountMarksInput = Integer.parseInt(textFieldAmountMarksInput.getText());

        // Load the inputs into the constructor
        Question newQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, Collections.<String>emptyList());

        // Add the new Question to the list
        questionsObservableList.add(newQuestion);

        System.out.println("Question Added");

        // Closes this dialog now that the question is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Question> _questionsObservableList) {
        this.questionsObservableList = _questionsObservableList;
    }
}
