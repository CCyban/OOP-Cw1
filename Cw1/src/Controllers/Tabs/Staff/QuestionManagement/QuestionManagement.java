package Controllers.Tabs.Staff.QuestionManagement;

import Classes.Quiz.Question;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class QuestionManagement implements Initializable {

    @FXML
    private TableView tableViewQuestions;

    private ObservableList<Question> questionsObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Question q1 = new Question("What is one plus one?", Question.QuestionType.Manual, "two", 5, Arrays.asList("Year 1", "Maths"));
        Question q2 = new Question("What is 1+1? [1, 2, 3, 4]", Question.QuestionType.MultiChoice, "2", 5, Arrays.asList("Year 1", "Maths"));
        Question q3 = new Question("What is 32+23?", Question.QuestionType.Arithmetic, "55", 5, Arrays.asList("Year 3", "Maths"));

        questionsObservableList = FXCollections.observableArrayList(q1, q2, q3);


        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Question, UUID>("questionUUID"));
        idCol.setPrefWidth(100);

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<Question, Question.QuestionType>("questionType"));

        TableColumn questionCol = new TableColumn("Question");
        questionCol.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));

        TableColumn correctAnswerCol = new TableColumn("Correct Answer");
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));

        TableColumn correctMarksCol = new TableColumn("Possible Marks");
        correctMarksCol.setCellValueFactory(new PropertyValueFactory<Question, Integer>("CorrectMarks"));

        TableColumn tagsCol = new TableColumn("Tags");
        tagsCol.setCellValueFactory(new PropertyValueFactory<Question, List<String>>("Tags"));



        tableViewQuestions.setItems(questionsObservableList);
        tableViewQuestions.getColumns().addAll(idCol, typeCol, questionCol, correctAnswerCol, correctMarksCol, tagsCol);
    }

    public void oldAddNewQuestionClick(ActionEvent event) {
        Parent root;
        try {
            // Stage configurations
            root = FXMLLoader.load(getClass().getResource("/Views/Tabs/Staff/QuestionManagement/AddQuestion.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add New Question");
            stage.setScene(new Scene(root, 1000, 600));

            // Can use this event handler to run a procedure that would update the TableView
            stage.setOnHidden(e -> System.out.println("Should Update TableView"));

            // Show the child window 'Add New Question'
            stage.show();

            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void onAddNewQuestionClick(ActionEvent event) throws IOException {
        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/Staff/QuestionManagement/AddQuestion.fxml"));
        Parent parent = fxmlLoader.load();
        AddQuestion dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(questionsObservableList);

        Scene scene = new Scene(parent, 900, 500);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Add New Question");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();
    }
}
