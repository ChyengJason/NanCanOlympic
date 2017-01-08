package com.jscheng.mr_horse.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cheng on 17-1-8.
 */
public class JsonUtil {
    public static String inputStreamToStr(InputStream inputStream)throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        return outputStream.toString();
    }

    public static Object getInstanceByJson(Class<?> clz,String jsonString){
        Object obj = null;
        Gson gson = new Gson();
        obj = gson.fromJson(jsonString,clz);
        return obj;
    }

    public static <T> List<T> jsonToList(Class<T[]> clz,String jsonString){
        Gson gson = new Gson();
        T[] array = gson.fromJson(jsonString,clz);
        return Arrays.asList(array);
    }

    public static <T> ArrayList<T> jsonToArrayList(Class<T> clz, String jsonString){
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(jsonString,type);
        ArrayList<T> arrayList =new ArrayList<>();
        for(JsonObject jsonObject:jsonObjects){
            arrayList.add(new Gson().fromJson(jsonObject,clz));
        }
        return arrayList;
    }
}
