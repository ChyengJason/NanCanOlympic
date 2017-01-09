package com.jscheng.mr_horse.model;

import android.content.Context;

import com.jscheng.mr_horse.utils.JsonUtil;

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
}
