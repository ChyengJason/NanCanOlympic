package com.jscheng.mr_horse.utils;

/**
 * Created by cheng on 2017/3/27.
 */
public class QuestionCatagoryUtil {
    public static String getShortName(String catagory){
        switch (catagory){
            case Constants.FLJC:
                return Constants.FLJC_SHORT_NAME;
            case Constants.MKSZY:
                return Constants.MKSZY_SHORT_NAME;
            case Constants.MZDSX:
                return Constants.MKSZY_SHORT_NAME;
            case Constants.SXDD:
                return Constants.SXDD_SHORT_NAME;
            case Constants.ZGJDS:
                return Constants.ZGJDS_SHORT_NAME;
        }
        return Constants.UNKNOWN;
    }

    public static String getName(String catagory){
        switch (catagory){
            case Constants.FLJC:
                return Constants.FLJC_NAME;
            case Constants.MKSZY:
                return Constants.MKSZY_NAME;
            case Constants.MZDSX:
                return Constants.MKSZY_NAME;
            case Constants.SXDD:
                return Constants.SXDD_NAME;
            case Constants.ZGJDS:
                return Constants.ZGJDS_NAME;
        }
        return Constants.UNKNOWN;
    }
}
