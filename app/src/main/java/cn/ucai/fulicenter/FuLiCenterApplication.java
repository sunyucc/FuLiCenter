package cn.ucai.fulicenter;

import android.app.Application;

/**
 * Created by sunyu on 2016/10/17.
 */
public class FuLiCenterApplication extends Application{
    private static FuLiCenterApplication instance ;
    public  static FuLiCenterApplication application;
    public FuLiCenterApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        application=this;
    }

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;
    }
}
