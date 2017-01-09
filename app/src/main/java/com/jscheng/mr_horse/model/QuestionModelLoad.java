package com.jscheng.mr_horse.model;

import android.content.Context;

import com.jscheng.mr_horse.utils.JsonUtil;
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
//        Logger.e(num+"");
        SharedPreferencesUtil.setParam(mContext,key,num);
    }
}
