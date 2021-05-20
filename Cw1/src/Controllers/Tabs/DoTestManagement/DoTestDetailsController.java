package Controllers.Tabs.DoTestManagement;

import Classes.Quiz.Question;
import Classes.Quiz.Test;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DoTestDetailsController implements Initializable {

    private Test selectedTest;

    private List<Question> testQuestions;

    @FXML
    private Label labelTestTitle;

    @FXML
    private Label labelTotalTestMarks;

    @FXML
    private Accordion accordionTestQuestions;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onFinishTestClick(ActionEvent event) {
        System.out.println("clicked");

        //TODO: Create and store result here

        // Closes this dialog now that the result is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setSelectedTest(Test _selectedTest) {
        this.selectedTest = _selectedTest;

        this.testQuestions = selectedTest.getQuestions();

        labelTestTitle.setText(selectedTest.getTestTitle());

        //tableViewTestQuestions.setItems(testQuestionsObservableList);
        /*
        for (Question question: testQuestions) {
            VBox vbox = new VBox();
            vbox.getChildren().add(new Label("Question: " + question.getQuestion()));
            vbox.getChildren().add(new Label("Max Marks: " + question.getCorrectMarks()));
            vbox.getChildren().add(new Label("Question Type: " + question.getQuestionType()));

            TitledPane titledPane = new TitledPane(question.getQuestion(), vbox);
            titledPane.setAnimated(false);
            accordionTestQuestions.getPanes().add(titledPane);
        }

         */
        for (int index = 0; index < testQuestions.stream().count(); index++)
        {
            Question question = testQuestions.get(index);
            VBox vbox = new VBox();
            vbox.getChildren().add(new Label("Question: " + question.getQuestion()));
            vbox.getChildren().add(new Label("Max Marks: " + question.getCorrectMarks()));
            vbox.getChildren().add(new Label("Question Type: " + question.getQuestionType()));

            HBox hbox = new HBox();
            hbox.getChildren().add(new Label("Answer: "));
            hbox.getChildren().add(new TextField());
            vbox.getChildren().add(hbox);

            TitledPane titledPane = new TitledPane((index + 1) + ". " + question.getQuestion(), vbox);
            titledPane.setAnimated(false);
            accordionTestQuestions.getPanes().add(titledPane);
        }


        labelTotalTestMarks.setText(String.valueOf(selectedTest.getTotalMarks()));
    }
}
