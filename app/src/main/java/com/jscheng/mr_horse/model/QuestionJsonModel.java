package com.jscheng.mr_horse.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cheng on 17-1-8.
 */
public class QuestionJsonModel implements Serializable{
    private Integer questionNum;
    private Integer questionType;
    private String question;
    private List<String> options;
    private List<Integer> answer;

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return questionNum+" "+question;
    }

    public QuestionModel toQuestionModel(String catogory){
        QuestionType type = null;
        switch (questionType){
            case 1:
                type = QuestionType.SINGLE;
                break;
            case 2:
                type = QuestionType.JUDGE;
                break;
            case 3:
                type = QuestionType.MULTIPLE;
                break;
            default:
                type = QuestionType.SINGLE;
                break;
        }
        return new QuestionModel(questionNum,type,catogory,question,options,answer);
    }
}
