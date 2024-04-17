package com.karam.QuizApp.service;

import com.karam.QuizApp.dao.QuestionDao;
import com.karam.QuizApp.dao.QuizDao;
import com.karam.QuizApp.model.Question;
import com.karam.QuizApp.model.QuestionWrapper;
import com.karam.QuizApp.model.Quiz;
import com.karam.QuizApp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findRandomQuestionByCategory(category,numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz created", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
       Optional<Quiz> quiz =  quizDao.findById(id);
       List<Question> questionsFromDB = quiz.get().getQuestions();
       List<QuestionWrapper> questionForUser = new ArrayList<>();
        for (Question question : questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(question.getId(),
                    question.getQuestion_title(),
                    question.getOption1(),
                    question.getOption2(),
                    question.getOption3(),
                    question.getOption4());
            questionForUser.add(qw);
        }

       return new ResponseEntity<>(questionForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(int id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        int right = 0;
        int i = 0;
        for (Response response : responses) {
            if (response.getResponse().equals(questions.get(i).getRight_answer())) {
                right++;
            }
            i++;
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
