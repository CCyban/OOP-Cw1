package Classes.Quiz;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Test {

    UUID testUUID;
    String testTitle;
    List<Question> Questions;

    public Test(String _testTitle, List<Question> _Questions) {
        // Generate a UUID for the test
        testUUID = UUID.randomUUID();

        // Use payload values
        testTitle = _testTitle;
        Questions = _Questions;
    }

    public void addQuestion(Question question) {
        Questions.add(question);
    }

    public void removeQuestion(Question question) {
        Questions.remove(question);
    }

    public UUID getTestUUID() {
        return testUUID;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String _testTitle) {
        this.testTitle = _testTitle;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public List<Question> searchQuestionsByTag(String search) { // Todo: A possible task
        return Collections.EMPTY_LIST;
    }
}
