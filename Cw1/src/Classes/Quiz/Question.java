package Classes.Quiz;

import java.util.List;
import java.util.UUID;

public class Question {
    public enum QuestionType { Arithmetic, MultiChoice };

    UUID questionUUID;
    QuestionType questionType;
    String Question;
    String correctAnswer;
    List<String> Tags;
    int correctMarks;

    public Question(String _Question, QuestionType _questionType, String _correctAnswer, int _correctMarks, List<String> _Tags)
    {
        // Generate a UUID for the question
        questionUUID = UUID.randomUUID();

        // Use payload values
        Question = _Question;
        questionType = _questionType;
        correctAnswer = _correctAnswer;
        correctMarks = _correctMarks;
        Tags = _Tags;
    }

    public void EditQuestion(int _correctMarks) {
        // This procedure (maybe) only edits the marks of a question for now
        correctMarks = _correctMarks;
    }

    public UUID getQuestionUUID() {
        return questionUUID;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public String getQuestion() {
        return Question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getCorrectMarks() {
        return correctMarks;
    }

    public List<String> getTags() {
        return Tags;
    }
}