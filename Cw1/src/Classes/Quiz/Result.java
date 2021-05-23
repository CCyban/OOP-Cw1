package Classes.Quiz;

import Classes.Banks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.UUID;

public class Result implements java.io.Serializable {
    UUID resultUUID;
    UUID testUUID;
    ArrayList<Answer> resultData;

    public Result(UUID _testUUID, ArrayList<Answer> _resultData) {
        // Generate a UUID for the result
        resultUUID = UUID.randomUUID();

        // Use payload values
        testUUID = _testUUID;
        resultData = _resultData;
    }

    public UUID getResultUUID() {
        return resultUUID;
    }

    public UUID getTestUUID() {
        return testUUID;
    }

    public String getTestTitle() {
        ObservableList testBankObservableList = FXCollections.observableArrayList();
        Banks.loadTestBank(false, true, testBankObservableList);

        String testTitle = ((Test) testBankObservableList.stream()
                .filter(test -> testUUID.equals(((Test)test).getTestUUID()))
                .findFirst()
                .orElse(null)).getTestTitle();

        return testTitle;
    }

    public ArrayList<Answer> getResultData() {
        return resultData;
    }

    public int getTotalMarksAchieved() {
        int totalMarksAchieved = 0;
        for (Answer answer: resultData) {
            totalMarksAchieved += answer.marksAchieved;
        }
        return totalMarksAchieved;
    }

    public void setResultData(ArrayList<Answer> _resultData) {
        resultData = _resultData;
    }
}
