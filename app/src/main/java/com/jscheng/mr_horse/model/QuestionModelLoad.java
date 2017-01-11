package com.jscheng.mr_horse.model;

import android.content.Context;

import com.jscheng.mr_horse.utils.JsonUtil;
import com.jscheng.mr_horse.utils.QuestionDbUtil;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 17-1-9.
 */
public class QuestionModelLoad {

    public static List<QuestionJsonModel> getQuestionJsonModels(Context mContext,String filename)throws IOException {
        InputStream inputstream = mContext.getClass().getClassLoader().getResourceAsStream("assets/" + filename);
        ArrayList<QuestionJsonModel> jsonModelList = null;
        if (inputstream != null) {
            String jsonString = JsonUtil.inputStreamToStr(inputstream);
            jsonModelList = JsonUtil.jsonToArrayList(QuestionJsonModel.class, jsonString);
        }
        return jsonModelList;
    }

    public static int getQuestionDoneNum(Context mContext,String catogory){
        String key = catogory+"_done_num";
        int num = (Integer) SharedPreferencesUtil.getParam(mContext,key,0);
//        Logger.e(num+"");
        return num;
    }

    public static void saveQuestionDoneNum(Context mContext,String catogory,Integer num){
        String key = catogory+"_done_num";
        SharedPreferencesUtil.setParam(mContext,key,num);
    }

    public static void saveQuestionModelToDB(Context mContext,List<QuestionModel> modelList) {
        QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
        questionDbUtil.insertList(modelList);
    }

    public static List<QuestionModel> getQuestionModelsfromDB(Context mContext,String catogory) {
        try {
            QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
            return questionDbUtil.getAllData(catogory);
        }catch (Exception e){
            Logger.e(e);
        }
        return new ArrayList<>();
    }

    public static void setQuestionModelDone(Context mContext,QuestionModel questionModel){
        try{
            QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
            questionDbUtil.update(questionModel);
        }catch (Exception e){
            Logger.e(e);
        }
    }

    public static void setQuestionModelToCollect(Context mContext,QuestionModel questionModel){
        try{
            questionModel.setCollected(true);
            QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
            questionDbUtil.update(questionModel);
        }catch (Exception e){
            Logger.e(e);
        }
    }

    public static void setQuestionModelOutCollect(Context mContext,QuestionModel questionModel){
        try{
            questionModel.setCollected(false);
            QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
            questionDbUtil.update(questionModel);
        }catch (Exception e){
            Logger.e(e);
        }
    }

    public static void setQuestionModelToWrongCollect(Context mContext,QuestionModel questionModel){
        try{
            questionModel.setWrong(true);
            QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
            questionDbUtil.update(questionModel);
        }catch (Exception e){
            Logger.e(e);
        }
    }

    public static void setQuestionModelOutWrongCollect(Context mContext,QuestionModel questionModel){
        try{
            questionModel.setWrong(false);
            QuestionDbUtil questionDbUtil = new QuestionDbUtil(mContext);
            questionDbUtil.update(questionModel);
        }catch (Exception e){
            Logger.e(e);
        }
    }
}
