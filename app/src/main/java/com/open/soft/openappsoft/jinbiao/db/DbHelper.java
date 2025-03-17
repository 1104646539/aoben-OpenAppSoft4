package com.open.soft.openappsoft.jinbiao.db;

import android.content.Context;

import com.open.soft.openappsoft.jinbiao.model.CardCompanyModel;
import com.open.soft.openappsoft.jinbiao.model.LineModel;
import com.open.soft.openappsoft.jinbiao.model.PeopleModel;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.open.soft.openappsoft.jinbiao.model.ResultPhotoImgModel;
import com.open.soft.openappsoft.jinbiao.model.SampleModel;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeModel;
import com.open.soft.openappsoft.jinbiao.model.SampleUnitModel;
import com.open.soft.openappsoft.jinbiao.model.ShiJiModel;
import com.open.soft.openappsoft.jinbiao.util.Global;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        File file = context.getDatabasePath(ToolUtils.PERSION_AND_COMPANY_DB_NAME);
        boolean isExist = file.exists();
        db = DbUtils.create(context, ToolUtils.PERSION_AND_COMPANY_DB_NAME, Global.DATABASE_VERSION,
                new DbUtils.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
//                        InitTables(db);
                        if (oldVersion < newVersion) {
                            //2019.3.19
                            if (oldVersion == 2 && newVersion == 3) {
                                try {
                                    db.deleteAll(LineModel.class);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                InitTables(db);
                            }
                            String s = ResultModel.class.toString().replace("class", "").replace(".", "_").trim();
                            System.out.println(s);
                            System.out.println("数据库升级了");
                            try {
                                db.execNonQuery("alter table " + s + " add sample_number TEXT DEFAULT \"\"");
                                db.execNonQuery("alter table " + s + " add sample_unit TEXT DEFAULT \"\"");
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        db.configAllowTransaction(true);// 开启事务
        db.configDebug(false);

        if (!isExist) {
            InitTables(db);
        }
    }

    /**
     * 初始化数据表
     *
     * @param dbUtil
     */
    private static void InitTables(DbUtils dbUtil) {

        try {
            dbUtil.createTableIfNotExist(ResultModel.class);
            dbUtil.createTableIfNotExist(PeopleModel.class);
            dbUtil.createTableIfNotExist(LineModel.class);
            dbUtil.createTableIfNotExist(CardCompanyModel.class);
            dbUtil.createTableIfNotExist(ResultPhotoImgModel.class);
            dbUtil.createTableIfNotExist(SampleModel.class);
            dbUtil.createTableIfNotExist(SampleUnitModel.class);
            dbUtil.createTableIfNotExist(ShiJiModel.class);
//            System.out.println("表初始化完毕！");
            InitDbData(dbUtil);
        } catch (DbException e1) {
            e1.printStackTrace();
        }
    }

    private static void InitDbData(DbUtils db) {

        String concentrateUnit = "μg/kg";
        CardCompanyModel cardModel = new CardCompanyModel("奥本", "200", "800", "120", "290");
        //初始化检测项目
        ArrayList<LineModel> list = new ArrayList<>();
//        String[] projectNames1 = new String[]{"氯霉素", "呋喃唑酮", "呋喃他酮",
//            "呋喃西林", "呋喃妥因", "孔雀石绿",
//            "氟苯尼考", "盐酸克伦特罗", "莱克多巴胺",
//            "沙丁胺醇",
//            "黄曲霉毒素B1", "呕吐毒素", "三聚氰胺"
//        };
//        String[] projectNames1 = new String[]{"盐酸克伦特罗（组织）快速检测",
//                "莱克多巴胺（组织）快速检测",
//                "沙丁胺醇（组织）快速检测",
//                "喹诺酮（组织）快速检测（稀释)",
//                "链霉素（组织）检测",
//                "甲砜毒素（组织）残留检测",
//                "氟苯尼考（组织）残留检测",
//                "孔雀石绿（组织）快速检测",
//                "呋喃唑酮（组织）快速检测",
//                "呋喃妥因（组织）快速检测",
//                "呋喃西林（组织）快速检测",
//                "呋喃它酮（组织）快速检测",
//                "喹乙醇（组织）快速检测"
//        };



//        String[] projectNames1 = new String[]{
//                "盐酸克伦特罗",
//                "沙丁胺醇",
//                "莱克多巴胺",
//                "三聚氰胺",
//                "氯霉素"
//        };


        String[] projectNames1 = new String[]{
        };




        for (int i = 0; i < projectNames1.length; i++) {
            list.add(new LineModel(2, projectNames1[i], cardModel.name, cardModel.ScanStart, cardModel.ScanEnd, cardModel.CTPeakWidth, cardModel.CTPeakDistance, "1", "0.1", concentrateUnit));
        }
        try {
            db.saveAll(list);
            db.save(cardModel);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
//        1、	检测单位：检测站
            {
//                PeopleModel checkDep = new PeopleModel("检测站", 1);
//                db.save(checkDep);

                List<PeopleModel> checkDep = new ArrayList<>();
                db.saveAll(checkDep);
            }

            {
//        2、	检验员：admin
//                PeopleModel checker = new PeopleModel("admin", 2);
                List<PeopleModel> peoplelist = new ArrayList<>();
//                peoplelist.add(new PeopleModel("admin", 2));
//                peoplelist.add(new PeopleModel("test", 2));
//                peoplelist.add(new PeopleModel("tsnmtest", 2));
                db.saveAll(peoplelist);
            }
//        3、	试剂厂商：浩景
            {
                ShiJiModel reagentCompany = new ShiJiModel();
                reagentCompany.code = "";
                reagentCompany.name = "奥本";
                db.save(reagentCompany);
            }
//        4、	样品类型：食用油、乳及乳制品、谷物
            {
                List<SampleTypeModel> sampleTypeList = new ArrayList<>();
//                sampleTypeList.add(new SampleTypeModel("食用油"));
//                sampleTypeList.add(new SampleTypeModel("乳及乳制品"));
//                sampleTypeList.add(new SampleTypeModel("谷物"));

//                sampleTypeList.add(new SampleTypeModel("果蔬"));
//                sampleTypeList.add(new SampleTypeModel("粮食"));
                db.saveAll(sampleTypeList);
            }
//        5、	样品名称：花生油、牛奶、大米、玉米、谷物饲料
            {
                List<SampleModel> sampleList = new ArrayList<>();
//                sampleList.add(new SampleModel("花生油"));
//                sampleList.add(new SampleModel("牛奶"));
//                sampleList.add(new SampleModel("大米"));
//                sampleList.add(new SampleModel("玉米"));
//                sampleList.add(new SampleModel("谷物饲料"));
                db.saveAll(sampleList);
            }
//        6、	送检单位：超市、市场、其他
            {
                List<PeopleModel> checkedOrgList = new ArrayList<>();
                checkedOrgList.add(new PeopleModel("超市", 3));
                checkedOrgList.add(new PeopleModel("市场", 3));
                checkedOrgList.add(new PeopleModel("其他", 3));
                db.saveAll(checkedOrgList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
