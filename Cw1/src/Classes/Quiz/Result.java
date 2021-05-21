package Classes.Quiz;

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

    public ArrayList<Answer> getResultData() {
        return resultData;
    }
}
