package com.karam.QuizApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.karam.QuizApp.model.Quiz;
public interface QuizDao extends JpaRepository<Quiz,Integer> {
    
}
