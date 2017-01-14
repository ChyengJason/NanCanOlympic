package com.jscheng.mr_horse.utils;

import android.content.ContentValues;
import android.content.Context;

import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionType;
import com.orhanobut.logger.Logger;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cheng on 17-1-10.
 */
public class QuestionDbUtil {
    private SQLiteDatabase db;
    private QuestionDB questionDB ;

    public QuestionDbUtil(Context context) {
        questionDB = new QuestionDB(context);
    }

    public void open() throws SQLException {
        db = questionDB.getWritableDatabase();
    }

    public void close() {
        if(db.isOpen())
            questionDB.close();
    }

    // 插入数据操作
    public void insert(QuestionModel questionModel) {
        try {
            this.open();
            ContentValues cv = new ContentValues();
            cv.put(questionDB.Question_Num, questionModel.getQuestionNum());
            cv.put(questionDB.Question_Type, questionTypeToInt(questionModel.getQuestionType()));
            cv.put(questionDB.Question_Done_Type, questionDoneTypeToInt(questionModel.getDone()));
            cv.put(questionDB.Question_Catogory, questionModel.getCatogory());
            cv.put(questionDB.Question_Text, questionModel.getQuestion());
            cv.put(questionDB.Option_List, listToString(questionModel.getOptionList(), '#'));
            cv.put(questionDB.Answer_List, listToString(questionModel.getAnswerList(), '#'));
            cv.put(questionDB.isCollect, booleanToInt(questionModel.isCollected()));
            cv.put(questionDB.isWrong, booleanToInt(questionModel.isWrong()));
            db.insert(QuestionDB.TABLE_NAME, null, cv);
        }catch (Exception e){
            Logger.e(e);
        }finally {
            this.close();
        }
    }

    public void insertList(List<QuestionModel> modelList,DbProgressListener listener){
        try {
            this.open();
            db.beginTransaction();
            for(int i=0;i<modelList.size();i++){
                QuestionModel questionModel = modelList.get(i);
                ContentValues cv = new ContentValues();
                cv.put(questionDB.Question_Num, questionModel.getQuestionNum());
                cv.put(questionDB.Question_Type, questionTypeToInt(questionModel.getQuestionType()));
                cv.put(questionDB.Question_Done_Type, questionDoneTypeToInt(questionModel.getDone()));
                cv.put(questionDB.Question_Catogory, questionModel.getCatogory());
                cv.put(questionDB.Question_Text, questionModel.getQuestion());
                cv.put(questionDB.Option_List, listToString(questionModel.getOptionList(), '#'));
                cv.put(questionDB.Answer_List, listToString(questionModel.getAnswerList(), '#'));
                cv.put(questionDB.isCollect, booleanToInt(questionModel.isCollected()));
                cv.put(questionDB.isWrong, booleanToInt(questionModel.isWrong()));
                db.insert(QuestionDB.TABLE_NAME, null, cv);
                if(listener!=null)
                    listener.loadProgress(i);
            }
            db.setTransactionSuccessful();
            Logger.e("已经存入数据库");
        }catch (Exception e){
            Logger.e(e);
        }finally {
            db.endTransaction();
            this.close();
        }
    }

    public interface DbProgressListener{
        void loadProgress(int progress);
    }

    // 删除数据操作
    public boolean delete(int questionNum,String catogory) {
        try {
            this.open();
            String where = QuestionDB.Question_Num + " = ? and " + QuestionDB.Question_Catogory + " = ? ";
            String[] whereValue = {questionNum + ""};
            if (db.delete(QuestionDB.TABLE_NAME, where, whereValue) > 0) {
                this.close();
                return true;
            }
        }catch (Exception e){
            Logger.e(e);
        }finally {
            this.close();
        }
        return false;
    }

    // 更新数据操作
    public boolean update(QuestionModel questionModel) {
        try{
            this.open();
            ContentValues cv = new ContentValues();
            cv.put(questionDB.Question_Type, questionTypeToInt(questionModel.getQuestionType()));
            cv.put(questionDB.Question_Done_Type, questionDoneTypeToInt(questionModel.getDone()));
            cv.put(questionDB.Question_Catogory, questionModel.getCatogory());
            cv.put(questionDB.Question_Text, questionModel.getQuestion());
            cv.put(questionDB.Option_List, listToString(questionModel.getOptionList(),'#'));
            cv.put(questionDB.Answer_List, listToString(questionModel.getAnswerList(),'#'));
            cv.put(questionDB.isCollect, booleanToInt(questionModel.isCollected()));
            cv.put(questionDB.isWrong, booleanToInt(questionModel.isWrong()));

            if (db.update(QuestionDB.TABLE_NAME, cv, QuestionDB.Question_Num + "=? and "+QuestionDB.Question_Catogory+"=? ",
                    new String[]{questionModel.getQuestionNum()+"",questionModel.getCatogory()}) > 0) {
                return true;
            }
        }catch (Exception e){
            Logger.e(e);
        }finally {
            this.close();
        }
        return false;
    }

    public void updateList(List<QuestionModel> modelList){
        try {
            this.open();
            db.beginTransaction();
            for(int i=0;i<modelList.size();i++){
                QuestionModel questionModel = modelList.get(i);
                ContentValues cv = new ContentValues();
                cv.put(questionDB.Question_Num, questionModel.getQuestionNum());
                cv.put(questionDB.Question_Type, questionTypeToInt(questionModel.getQuestionType()));
                cv.put(questionDB.Question_Done_Type, questionDoneTypeToInt(questionModel.getDone()));
                cv.put(questionDB.Question_Catogory, questionModel.getCatogory());
                cv.put(questionDB.Question_Text, questionModel.getQuestion());
                cv.put(questionDB.Option_List, listToString(questionModel.getOptionList(), '#'));
                cv.put(questionDB.Answer_List, listToString(questionModel.getAnswerList(), '#'));
                cv.put(questionDB.isCollect, booleanToInt(questionModel.isCollected()));
                cv.put(questionDB.isWrong, booleanToInt(questionModel.isWrong()));
                db.update(QuestionDB.TABLE_NAME, cv, QuestionDB.Question_Num + "=? and "+QuestionDB.Question_Catogory+"=? ",
                    new String[]{questionModel.getQuestionNum()+"",questionModel.getCatogory()});
            }
            db.setTransactionSuccessful();
            Logger.e("更改数据库完成");
        }catch (Exception e){
            Logger.e(e);
        }finally {
            db.endTransaction();
            this.close();
        }
    }

    // 获取所有数据
    public ArrayList<QuestionModel> getAllData(String catagory) {
        ArrayList<QuestionModel> items = new ArrayList<QuestionModel>();
        Cursor cursor = null;
        try {
            this.open();
             cursor = db.rawQuery("select * from " + QuestionDB.TABLE_NAME + " where " + QuestionDB.Question_Catogory + " = '" + catagory + "'", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int question_num = cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Num));
                    QuestionType question_type = intToQuestionType(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Type)));
                    QuestionDoneType question_done_type = intToQuestionDoneType(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Done_Type)));
                    String question_catogory = cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Question_Catogory));
                    String question_text = cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Question_Text));

                    List<String> option_list = stringToStringList(cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Option_List)), '#');
                    List<Integer> answer_list = stringToList(cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Answer_List)), '#');
                    boolean is_collect = intToBoolean(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.isCollect)));
                    boolean is_wrong = intToBoolean(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.isWrong)));

                    QuestionModel item = new QuestionModel(question_num, question_type, question_done_type, question_catogory, question_text, option_list, answer_list, is_collect, is_wrong);
                    items.add(item);
                    cursor.moveToNext();
                }
            }
            if(cursor!=null && !cursor.isClosed()) cursor.close();
        }catch (Exception e){
            Logger.e(e);
        }finally {
            this.close();
            if(cursor!=null && !cursor.isClosed())
                cursor.close();
        }
        return items;
    }

    // 获取收藏数据
    public ArrayList<QuestionModel> getCollectedData(String catagory) {
        ArrayList<QuestionModel> items = new ArrayList<QuestionModel>();
        Cursor cursor = null;
        try {
            this.open();
            cursor = db.rawQuery("select * from " + QuestionDB.TABLE_NAME + " where " + QuestionDB.Question_Catogory + " = '" + catagory + "' and " + QuestionDB.isCollect +" = 1", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int question_num = cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Num));
                    QuestionType question_type = intToQuestionType(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Type)));
                    QuestionDoneType question_done_type = intToQuestionDoneType(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Done_Type)));
                    String question_catogory = cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Question_Catogory));
                    String question_text = cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Question_Text));

                    List<String> option_list = stringToStringList(cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Option_List)), '#');
                    List<Integer> answer_list = stringToList(cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Answer_List)), '#');
                    boolean is_collect = intToBoolean(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.isCollect)));
                    boolean is_wrong = intToBoolean(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.isWrong)));

                    QuestionModel item = new QuestionModel(question_num, question_type, question_done_type, question_catogory, question_text, option_list, answer_list, is_collect, is_wrong);
                    items.add(item);
                    cursor.moveToNext();
                }
            }
            if(cursor!=null && !cursor.isClosed()) cursor.close();
        }catch (Exception e){
            Logger.e(e);
        }finally {
            this.close();
            if(cursor!=null && !cursor.isClosed())
                cursor.close();
        }
        return items;
    }

    public void removeCollectQuestion(String catogory){
        try {
            this.open();
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(questionDB.isCollect, 0);
            db.update(QuestionDB.TABLE_NAME, cv, QuestionDB.Question_Catogory+"=? ", new String[]{catogory});
            db.setTransactionSuccessful();
            Logger.e("移除收藏完成");
        }catch (Exception e){
            Logger.e(e);
        }finally {
            db.endTransaction();
            this.close();
        }
    }

    public void removeWrongQuestion(String catogory){
        try {
            this.open();
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(questionDB.isWrong, 0);
            db.update(QuestionDB.TABLE_NAME, cv, QuestionDB.Question_Catogory+"=? ", new String[]{catogory});
            db.setTransactionSuccessful();
            Logger.e("移除错题完成");
        }catch (Exception e){
            Logger.e(e);
        }finally {
            db.endTransaction();
            this.close();
        }
    }

    // 获取错误数据
    public ArrayList<QuestionModel> getWrongData(String catagory) {
        ArrayList<QuestionModel> items = new ArrayList<QuestionModel>();
        Cursor cursor = null;
        try {
            this.open();
            cursor = db.rawQuery("select * from " + QuestionDB.TABLE_NAME + " where " + QuestionDB.Question_Catogory + " = '" + catagory + "' and " + QuestionDB.isWrong +" = 1", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int question_num = cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Num));
                    QuestionType question_type = intToQuestionType(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Type)));
                    QuestionDoneType question_done_type = intToQuestionDoneType(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.Question_Done_Type)));
                    String question_catogory = cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Question_Catogory));
                    String question_text = cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Question_Text));

                    List<String> option_list = stringToStringList(cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Option_List)), '#');
                    List<Integer> answer_list = stringToList(cursor.getString(cursor
                            .getColumnIndex(QuestionDB.Answer_List)), '#');
                    boolean is_collect = intToBoolean(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.isCollect)));
                    boolean is_wrong = intToBoolean(cursor.getInt(cursor
                            .getColumnIndex(QuestionDB.isWrong)));

                    QuestionModel item = new QuestionModel(question_num, question_type, question_done_type, question_catogory, question_text, option_list, answer_list, is_collect, is_wrong);
                    items.add(item);
                    cursor.moveToNext();
                }
            }
            if(cursor!=null && !cursor.isClosed()) cursor.close();
        }catch (Exception e){
            Logger.e(e);
        }finally {
            this.close();
            if(cursor!=null && !cursor.isClosed())
                cursor.close();
        }
        return items;
    }

    private int booleanToInt(boolean b){
        if (b==true)
            return 1;
        return 0;
    }

    private boolean intToBoolean(int b){
        if(b==1)
            return true;
        return false;
    }

    private int questionTypeToInt(QuestionType type){
        if (type==QuestionType.SINGLE)
            return 0;
        if (type==QuestionType.JUDGE)
            return 1;
        return 2;
    }

    private QuestionType intToQuestionType(int type){
        if (type==0)
            return QuestionType.SINGLE;
        if (type==1)
            return QuestionType.JUDGE;
        return QuestionType.MULTIPLE;
    }

    private int questionDoneTypeToInt(QuestionDoneType type){
        if (type == QuestionDoneType.NOT_DONE)
            return 0;
        if(type==QuestionDoneType.DONE_RIGHT)
            return 1;
        return 2;
    }

    private QuestionDoneType intToQuestionDoneType(int type){
        if(type == 0)
            return QuestionDoneType.NOT_DONE;
        if(type == 1)
            return QuestionDoneType.DONE_RIGHT;

        return QuestionDoneType.DONE_WRONG;
    }


    public  String listToString(List list, char separator){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

    public List<String> stringToStringList(String str,char separator){
        List<String> list  = Arrays.asList(str.split(separator + ""));
        return list;
    }

    public List<Integer> stringToList(String str, char separator) {
        List<String> list  = Arrays.asList(str.split(separator + ""));
        List<Integer> integerList = new ArrayList<>();
        for (String s : list) {
            integerList.add(Integer.parseInt(s));
        }
        return integerList;
    }

}
