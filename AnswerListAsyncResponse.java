package com.example.simpleapiquiz.data;

import com.example.simpleapiquiz.model.Questions;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Questions> questionsArrayList);
}
