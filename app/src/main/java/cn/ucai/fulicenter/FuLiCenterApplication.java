package cn.ucai.fulicenter;

import android.app.Application;

/**
 * Created by sunyu on 2016/10/17.
 */
public class FuLiCenterApplication extends Application{
    public  static FuLiCenterApplication application;
    private static FuLiCenterApplication instance ;
    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FuLiCenterApplication.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        instance =this;
    }

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;
    }
}
