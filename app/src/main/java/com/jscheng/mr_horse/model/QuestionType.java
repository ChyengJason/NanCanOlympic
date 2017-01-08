package com.jscheng.mr_horse.model;

/**
 * Created by cheng on 17-1-7.
 * 问题类型
 * 单选
 * 多选
 * 判断
 */
public enum QuestionType {
    SINGLE,MULTIPLE,JUDGE;

    @Override
    public String toString() {
        if(super.toString().equals("SINGLE"))
            return "单选题";
        if(super.toString().equals("MULTIPLE"))
            return "多选题";
        else
            return "判断题";
    }
}
