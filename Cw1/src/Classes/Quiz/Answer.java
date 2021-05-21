package Classes.Quiz;

import java.util.UUID;

public class Answer implements java.io.Serializable {
    UUID questionUUID;
    int marksAchieved;
    String givenAnswer;

    public Answer(UUID _questionUUID, int _marksAchieved, String _givenAnswer) {
        questionUUID = _questionUUID;
        marksAchieved = _marksAchieved;
        givenAnswer = _givenAnswer;
    }
}
