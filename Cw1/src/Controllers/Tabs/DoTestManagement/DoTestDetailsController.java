package Controllers.Tabs.DoTestManagement;

import Classes.Quiz.Answer;
import Classes.Quiz.Question;
import Classes.Quiz.Result;
import Classes.Quiz.Test;
import Classes.Translating;
import com.sun.javafx.scene.shape.ArcHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static Classes.Quiz.Question.QuestionType.Arithmetic;

public class DoTestDetailsController implements Initializable {

    private Test selectedTest;

    private List<Question> testQuestions;

    @FXML
    private Label labelTestTitle;

    @FXML
    private Label labelTotalTestMarks;

    @FXML
    private Accordion accordionTestQuestions;

    private ArrayList<Object> arrayListGivenAnswers = new ArrayList<>();

    private Result result;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onFinishTestClick(ActionEvent event) {
        System.out.println("clicked");

        //TODO: Create and store result here


        ArrayList<Answer> arrayListAnswers = new ArrayList<Answer>();

        for (int index = 0; index < arrayListGivenAnswers.size(); index++) {
            Question question = testQuestions.get(index);
            Object answer = arrayListGivenAnswers.get(index);
            int marksAchieved = 0;
            if (answer.getClass() == ToggleGroup.class) {

                // Extract the answer given from the selected radio button
                String answerGiven = ((RadioButton) ((ToggleGroup)answer).getSelectedToggle()).getText();

                // If the user got it correct, give the marks
                if (answerGiven.equals(question.getCorrectAnswer())) {
                    marksAchieved = question.getCorrectMarks();
                }

                // Save given answer with its details
                arrayListAnswers.add(new Answer(testQuestions.get(index).getQuestionUUID(), marksAchieved, answerGiven));
            }
            else if (answer.getClass() == TextField.class) {

                // Extract the answer given from the TextField input
                String answerGiven = ((TextField) answer).getText();

                // If the user got it correct, give the marks
                if (answerGiven.equals(question.getCorrectAnswer())) {
                    marksAchieved = question.getCorrectMarks();
                }

                // Save given answer with its details
                arrayListAnswers.add(new Answer(testQuestions.get(index).getQuestionUUID(), marksAchieved, answerGiven));
            }
        }

        result = new Result(selectedTest.getTestUUID(), arrayListAnswers);
        saveResultBank(true);

        // Closes this dialog now that the result is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setSelectedTest(Test _selectedTest) {
        this.selectedTest = _selectedTest;

        this.testQuestions = selectedTest.getQuestions();

        labelTestTitle.setText(selectedTest.getTestTitle());

        // For each question in the test, we make a TitledPane. This code-generated TitlePane is then shown in the question list Accordion control.
        for (int index = 0; index < testQuestions.stream().count(); index++)
        {
            Question question = testQuestions.get(index);
            VBox vbox = new VBox();
            vbox.setSpacing(20);



            switch (question.getQuestionType()) {
                case Arithmetic:
                    // Adding the basic Correct Marks and Question labels to the question VBox
                    vbox.getChildren().add(new Label("Correct Marks: " + question.getCorrectMarks()));
                    vbox.getChildren().add(new Label("Question: " + question.getQuestion()));

                    // Done so we can keep track of the answers
                    TextField textFieldQuestion = new TextField();
                    arrayListGivenAnswers.add(textFieldQuestion);

                    // Creating and adding the answer HBox to the question VBox
                    HBox hbox = new HBox();
                    hbox.setSpacing(50);
                    hbox.getChildren().add(new Label("Answer: "));
                    hbox.getChildren().add(textFieldQuestion);    // TODO: APPLY NUMBERSONLY
                    vbox.getChildren().add(hbox);
                    break;
                case MultiChoice:
                    // Storing a local copy of the question string as it will be modified
                    String entireQuestion = question.getQuestion();

                    // Find the split of the subsections & remove the brackets to clean it up
                    int optionsIndex = entireQuestion.indexOf('(');                     // Finds where initial question ends & the options begin
                    entireQuestion = entireQuestion.replace("(", "");   // Removes the '('
                    entireQuestion = entireQuestion.replace(")", "");   // Removes the ')'

                    // Get subsections
                    String initialQuestion = question.getQuestion().substring(0, optionsIndex); // Splits the entireQuestion into the initial question part
                    String optionsQuestion = entireQuestion.substring(optionsIndex);            // Splits the entireQuestion into the question options part

                    // Can now show a subsection - the initial question part
                    vbox.getChildren().add(new Label("Correct Marks: " + question.getCorrectMarks()));
                    Label labelQuestion = new Label("Question: " + initialQuestion);
                    labelQuestion.setWrapText(true);
                    vbox.getChildren().add(labelQuestion);

                    // Now to begin showing the answer options
                    vbox.getChildren().add(new Label("Answer: "));
                    ToggleGroup toggleGroupQuestion = new ToggleGroup();

                    // Done so we can keep track of the answers
                    arrayListGivenAnswers.add(toggleGroupQuestion);


                    // Now showing the other subsection - the options part
                    // For each option loop
                    for (String option: Arrays.asList(optionsQuestion.split("\\s*,\\s*"))) {
                        RadioButton radioButtonQuestion = new RadioButton(option);  // Creating a radio button for an answer option
                        radioButtonQuestion.setWrapText(true);
                        radioButtonQuestion.setToggleGroup(toggleGroupQuestion);    // Making the radio buttons functional by only letting 1 be selected
                        vbox.getChildren().add(radioButtonQuestion);                // Adding the radio button to the frontend
                    }
                    break;
                default:
                    System.out.println("QuestionType value not recognised");
                    break;
            }

            TitledPane titledPane = new TitledPane("Question " + (index + 1), vbox);
            titledPane.setAnimated(true);
            accordionTestQuestions.getPanes().add(titledPane);
        }


        labelTotalTestMarks.setText(String.valueOf(selectedTest.getTotalMarks()));
    }

    public void saveResultBank(Boolean useDialogResult) {
        List<Result> resultsList = new ArrayList<>();   // Creation of empty result bank

        // Running an attempt to retrieve the data from the resultBank
        List retrievedData = Translating.deserialiseList("resultBank.ser", useDialogResult);
        if (retrievedData != null) {
            resultsList.addAll(retrievedData);  // Gets the current up to date result bank
        }

        resultsList.add(result); // Add result to the result bank

        // Sending the list data to be serialised as a resultBank file
        Translating.serialiseObject(resultsList, "resultBank.ser", useDialogResult);
    }
}
