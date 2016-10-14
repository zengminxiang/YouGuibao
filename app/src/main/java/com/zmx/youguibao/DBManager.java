package com.zmx.youguibao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import greenDao.DaoMaster;

/**
 *作者：胖胖祥
 *时间：2016/10/13 0013 下午 3:50
 *功能模块：数据库管理者单例
 */
public class DBManager {

    private final static String dbName = "youguibao";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

}
