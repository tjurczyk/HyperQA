package edu.emory.clir.hyperqa.representation;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftDocument extends Document {
    private int questions_length = 4;
    private int answers_length   = 4;

    private Sentence  [] l_questions = new Sentence[questions_length];
    private Sentence[][] l_answers   = new Sentence[questions_length][answers_length];
    private int       [] l_canswers  = new int     [questions_length];

    public Sentence[] get_questions()
    {
        return l_questions;
    }

    public Sentence getQuestion(int index)
    {
        return l_questions[index];
    }

    public void addQuestion(Sentence question, int index)
    {
        this.l_questions[index] = question;
    }

    public int getCorrectAnswer(int index)
    {
        return l_canswers[index];
    }

    public void addCorrectAnswer(int correct_answer, int index)
    {
        this.l_canswers[index] = correct_answer;
    }

    public Sentence getAnswers(int question_index, int answer_index)
    {
        return l_answers[question_index][answer_index];
    }

    public void addAnswer(int question_index, int answer_index, Sentence answer)
    {
        l_answers[question_index][answer_index] = answer;
    }
}
