package cn.ucai.fulicenter.dao;

import android.content.Context;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by sunyu on 2016/10/24.
 */

public class DBManager {
    private static DBOpenHelper mHelper ;
    private static  DBManager dbMgr = new DBManager();
    public DBManager() {
    }
    public static synchronized DBManager getInstance(){
        return dbMgr;
    }

    public static DBManager onInit(Context context){
        if (mHelper == null) {
            mHelper = DBOpenHelper.onInit(context);
        }
        return dbMgr;
    }
    public boolean saveUser(User user) {
        return false;
    }

    public User getUser(String username) {
        return null ;
    }

    public boolean updateUser(User user) {
        return false;
    }
    public static void closeDB(){
        if (mHelper != null) {
            mHelper.close();
        }
    }
}
