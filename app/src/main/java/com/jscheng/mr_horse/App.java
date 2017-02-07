package com.jscheng.mr_horse;

import android.app.Application;
import android.os.Environment;

import com.jscheng.mr_horse.utils.Configs;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * Created by cheng on 17-1-8.
 */
public class App extends Application{
    private static App instance;
    private static int dayNightTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dayNightTheme= (int)SharedPreferencesUtil.getParam(this, Constants.THEME,R.style.SunAppTheme);
        initBugly();
    }

    public static App getInstance(){
        return instance;
    }

    public static int getDayNightTheme() { return dayNightTheme; }

    public static void setDayNightTheme(int theme) {dayNightTheme = theme; }

    private void initBugly(){

        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = (boolean)SharedPreferencesUtil.getParam(this, Constants.AUTO_CHECK_UPGRADE,true);

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为5s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 5 * 1000;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.icon_night;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.largeIconId = R.mipmap.icon_night;

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.largeIconId = R.mipmap.icon_night;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;

        Beta.storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
         **/

        Bugly.init(getApplicationContext(), Configs.BUGLY_APP_ID, true);//最后一个参数代表是否是debug模式

    }

    public static void checkUpgrade(){
        Beta.checkUpgrade();
    }
}
