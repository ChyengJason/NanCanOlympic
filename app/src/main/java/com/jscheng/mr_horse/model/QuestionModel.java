package com.jscheng.mr_horse.model;

import android.content.Intent;

import java.util.List;

/**
 * Created by cheng on 17-1-7.
 */
public class QuestionModel {
    private int questionNum;
    private QuestionType questionType;
    private String question;
    private int optionCount;
    private List<String> optionList;
    private List<Integer> answerList;
    private Boolean done;//是否做了

    public QuestionModel(int questionNum,QuestionType questionType, String question, List<String> optionList, List<Integer> answerList){
        this.questionNum = questionNum;
        this.questionType = questionType;
        this.question = question;
        this.optionList = optionList;
        this.optionCount = optionList!=null?optionList.size():0;
        this.answerList = answerList;
        this.done = false;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }

    public List<Integer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Integer> answerList) {
        this.answerList = answerList;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }
}
