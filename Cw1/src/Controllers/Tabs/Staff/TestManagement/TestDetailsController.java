package Controllers.Tabs.Staff.TestManagement;

import Classes.Quiz.Test;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class TestDetailsController implements Initializable {

    @FXML
    private Button buttonFinishTest;

    @FXML
    private TextField textFieldTestTitleInput;

    private ObservableList<Test> testObservableList;
    private TestManagementController.TestDetailsPurpose testDetailsPurpose;
    private Test selectedTest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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


        //TODO: GATHER OBLIST FROM THE TEST TABLES
        /*
        // Gather a list of Tags
        List<String> tagsInput = Arrays.stream(textFieldTagsInput.getText().split(","))  // Makes a list with each new element after a comma, then converts it into a stream
                .map(String::strip)                                                            // Strips whitespace from the stream (means that it only strips the edges of the previous elements)
                .collect(Collectors.toList());                                                // Converts the stripped stream back into a list

        */


        // The Gathering is complete

        // Time to do whatever the purpose of this dialog is with the inputted values
        switch (testDetailsPurpose) {
            case Add:
                // Load the gathered inputs into the constructor
                Test newTest = new Test(testTitleInput, Collections.EMPTY_LIST);
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

        textFieldTestTitleInput.setText(selectedTest.getTestTitle());
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }
}
