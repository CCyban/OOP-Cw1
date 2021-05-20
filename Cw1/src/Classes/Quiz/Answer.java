package Classes.Quiz;

import java.util.UUID;

public class Answer {
    UUID questionUUID;
    int marksAchieved;
    String givenAnswer;

    public Answer(UUID _questionUUID, int _marksAchieved, String _givenAnswer) {
        questionUUID = _questionUUID;
        marksAchieved = _marksAchieved;
        givenAnswer = _givenAnswer;
    }
}
