package com.jscheng.mr_horse.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jscheng.mr_horse.App;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cheng on 17-1-10.
 */
public class QuestionDB extends SQLiteOpenHelper {
    // 数据库名
    public final static String DATABASE_NAME = "Mr_Horse.db";
    // 数据库版本
    public static int DATABASE_VERSION = 1;
    // 表名
    public final static String TABLE_NAME = "question_model";
    // 表中的字段
    public final static String _id = "_id";
    public final static String Question_Num = "question_num";
    public final static String Question_Type = "question_type";
    public final static String Question_Done_Type = "question_done_type";
    public final static String Question_Catogory = "question_catogory";
    public final static String Question_Text = "question_text";
    public final static String Option_List = "option_list";
    public final static String Answer_List = "answer_list";
    public final static String isCollect = "is_collect";
    public final static String isWrong = "is_wrong";

    public QuestionDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _id               + " integer PRIMARY KEY autoincrement , "
                + Question_Num      + " integer, "
                + Question_Type     + " integer, "
                + Question_Done_Type+ " integer, "
                + Question_Catogory + " text, "
                + Question_Text     + " text, "
                + Option_List       + " text, "
                + Answer_List       + " text, "
                + isCollect         + " integer, "
                + isWrong           + " integer);";
        db.execSQL(sql);
    }

    /**
     * 数据库升级时调用 删除数据库中原有的user表，并重新创建新user表
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    // 复制和加载区域数据库中的数据
    public static String  CopySqliteFileFromRawToDatabases(Context context,String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        File dir = new File("data/data/" + App.getInstance().getPackageName() + "/databases");
        Logger.e("!dir.exists()=" + !dir.exists());
        Logger.e("!dir.isDirectory()=" + !dir.isDirectory());

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file= new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream =null;

        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();
//                inputStream =App.getInstance().getClass().getClassLoader().getResourceAsStream("assets/" + );
                inputStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);
                Logger.e(file.getName()+inputStream.read());
                byte[] buffer = new byte[1024];
                int len ;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            finally {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return file.getPath();

    }
}
