package com.open.soft.openappsoft.multifuction.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lidroid.xutils.DbUtils;
import com.open.soft.openappsoft.multifuction.util.Global;

import java.io.File;

/**
 * Created by Administrator on 2017-12-08.
 */

public class DbHelper {

    private DbHelper() {

    }

    private static DbUtils db = null;
    private static Context context;

    public static DbUtils GetInstance() {
        return db;
    }

    /**
     * 初始化数据库
     *
     * @param ctx 上下文
     */
    public static void InitDb(Context ctx) {

        context = ctx;
        File file = context.getDatabasePath(Global.DATA_BASE_NAME);
        boolean isExist = file.exists();
        db = DbUtils.create(context, Global.DATA_BASE_NAME, Global.DATABASE_VERSION,
                new DbUtils.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                        //InitTables(db);
//                        if (oldVersion < newVersion) {
//                            String tableName = TableUtils.getTableName(ProjectModel.class);
//                            //System.out.println("数据库升级了");
//                            try {
//                                db.execNonQuery("alter table " + tableName + " add dilutionRatio FLOAT DEFAULT 0");
//                                db.configAllowTransaction(true);// 开启事务
//                                db.configDebug(false);
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                });

        db.configAllowTransaction(true);
        if (!isExist) {
//            InitTables(db);
        }
    }


    /**
     * 获取 样品号
     *
     * @param goodsName
     * @return
     */
    public static String getSampleNumber(String goodsName) {

//        GT.logs("查询的数据:" + goodsName);
        SQLiteDatabase database = db.getDatabase();
//        GT.logs("database:" + database);
        if(database == null) return null;

        Cursor cursor = database.query("com_open_soft_openappsoft_multifuction_model_SampleName", null, "sampleName = ?", new String[]{goodsName}, null, null, "", "");
//        GT.logs("cursor:" + cursor);
        if(cursor == null) return null;

        int statusNumber = cursor.getCount();
//        GT.logs("返回的操作值:" + statusNumber);

//        GT.logs("cursor.getCount():" + cursor.getCount());

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();//移动到首位
            String sampleNumber = cursor.getString(cursor.getColumnIndex("sampleNumber"));
//            GT.logs("sampleNumber:" + sampleNumber);
            cursor.close();//释放资源
            return sampleNumber;
        }

        return null;

    }

}
