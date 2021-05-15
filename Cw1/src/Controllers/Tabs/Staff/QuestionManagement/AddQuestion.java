package Controllers.Tabs.Staff.QuestionManagement;

import Classes.Quiz.Question;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AddQuestion {

    @FXML
    private Button buttonCreateQuestion;

    private ObservableList<Question> questionsObservableList;

    @FXML
    public void onAddNewQuestionClick(ActionEvent event) {
        System.out.println("Question Added");

        // Closes this child window now that the question is added
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Question> _questionsObservableList) {
        this.questionsObservableList = _questionsObservableList;
    }
}
