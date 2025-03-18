package com.open.soft.openappsoft.multifuction.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.task.TaskListActivity;
import com.open.soft.openappsoft.activity.task.TestTaskActivity;
import com.open.soft.openappsoft.multifuction.db.DbHelper;
import com.open.soft.openappsoft.multifuction.model.BCheckOrg;
import com.open.soft.openappsoft.multifuction.model.CheckOrg;
import com.open.soft.openappsoft.multifuction.model.Inspector;
import com.open.soft.openappsoft.multifuction.model.Print;
import com.open.soft.openappsoft.multifuction.model.Project;
import com.open.soft.openappsoft.multifuction.model.SampleName;
import com.open.soft.openappsoft.multifuction.model.SampleSource;
import com.open.soft.openappsoft.multifuction.resource.SPResource;
import com.open.soft.openappsoft.multifuction.util.Global;
import com.open.soft.openappsoft.multifuction.util.SPUtils;
import com.open.soft.openappsoft.multifuction.util.SerialUtils;
import com.open.soft.openappsoft.multifuction.util.ToolUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btnPesticideTest;
    private Button btnDataManage;
    private Button btnSysSetting;

    private long lastExitTime;
    private Timer mTimer;
    private SharedPreferences sp;
    public static boolean isFirst = false;
    private List<Print> prints_check;
    private List<Print> prints_data_manager;
    SPUtils spUtils;
    TextView tv_title;
    ArrayList<Project> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_multifuction);

        btnPesticideTest = (Button) findViewById(R.id.btn_pesticide_test);
        btnDataManage = (Button) findViewById(R.id.btn_data_manage);
        btnSysSetting = (Button) findViewById(R.id.btn_sys_setting);
        tv_title = (TextView) findViewById(R.id.tv_title);

        btnPesticideTest.setOnClickListener(this);
        btnDataManage.setOnClickListener(this);
        btnSysSetting.setOnClickListener(this);


        initSP();
        initTestData();
//        startHold2Server();
        initData();
        initProject();

//        if (Global.DEBUG) {
//            Global.cardWarmTime = 30;
//            Global.cardReactionTime = 30;
//        }
        tv_title.setText("农药残留检测");

        if (getIntent().getBooleanExtra("isBoot", false)) {//每次开机都清零
            sp.edit().putFloat(SPResource.KEY_COMPARE_VALUE, 0).apply();
        }


        String str = getJson("test2.json", this);

        try {
            Log.d(TAG, "json=" + new String(str.getBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void initTestData() {
        if (!isFirst) {
            return;
        }
        if (!Global.DEBUG) {
            return;
        }
        //检测单位
        List<CheckOrg> checkOrgs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CheckOrg co = new CheckOrg("检测单位" + (i + 1));
            checkOrgs.add(co);
        }
        try {
            DbHelper.GetInstance().saveAll(checkOrgs);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //被检测单位
        List<BCheckOrg> bCheckOrgs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BCheckOrg co = new BCheckOrg("被检测单位" + (i + 1));
            bCheckOrgs.add(co);
        }
        try {
            DbHelper.GetInstance().saveAll(bCheckOrgs);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //商品来源
        List<SampleSource> sampleSources = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SampleSource ss = new SampleSource("产地" + (i + 1));
            sampleSources.add(ss);
        }
        try {
            DbHelper.GetInstance().saveAll(sampleSources);
        } catch (DbException e) {
            e.printStackTrace();
        }
//        //样品名称
//        List<SampleName> sampleNames = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            SampleName ss = new SampleName("名称" + (i + 1));
//            sampleNames.add(ss);
//        }
//        try {
//            DbHelper.GetInstance().saveAll(sampleNames);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        //检测人员
        List<Inspector> inspectors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Inspector ss = new Inspector("人员" + (i + 1));
            inspectors.add(ss);
        }
        try {
            DbHelper.GetInstance().saveAll(inspectors);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    ProgressDialog progressDialog;

    private void initData() {
        if (isFirst) {

//            initXlzMap();
        } else {
            //获取第一行的所有项目为可检测的项目
//        List<SampleName> sns = new SampleName().findAll();
//        Log.d(TAG,"initData sns="+sns.size());
//            if (sns!=null&&sns.size()>0){
//                Log.d(TAG,"initData sns.size="+sns.size());
//                SampleName sn = sns.get(0);
//                Log.d(TAG,"initData sn="+sn);
////                Log.d(TAG,"initData sn="+sn.projects.size());
//                projects.clear();
//                try {
//                    projects.addAll(sn.getProjects());
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//            }
            projects = (ArrayList<Project>) new Project().findAll();
        }
        //设置打印机为打印方向为正向   0x1b, 0x32, 0x01 则为反向
//        SerialUtils.COM4_SendData(new byte[]{0x1b, 0x09});
//        SerialUtils.COM4_SendData(new byte[]{0x1b, 0x32, 0x00});
//        SerialUtils.COM4_SendData(new byte[]{0x1b, 0x15});
    }

    /**
     * 每隔1分钟向服务器发送一次数据，以表明仪器在线
     */
    private void startHold2Server() {

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ToolUtils.connect2Server();
            }
        }, 0, 60 * 1000);

    }

    private void initProject() {
        List<Project> list = new Project().findAll();
        if (list != null && list.size() > 0) {
            Global.project = list.get(0);
        }
    }

    private void initSP() {

        sp = getSharedPreferences(SPResource.FILE_NAME, Context.MODE_PRIVATE);
        spUtils = new SPUtils(sp);
        isFirst = sp.getBoolean(SPResource.KEY_FIRST_ENTER, true);
        Global.check_department = ToolUtils.getSPValue(SPResource.KEY_CHECK_ORGANIZATION, this);
        Global.check_state_no = ToolUtils.getSPValue(SPResource.KEY_CHECK_STATION_NUMBER, this);
//        Global.cardWarmTime = sp.getInt(SPResource.KEY_CARD_WARM_TIME, 3 * 60);
//        Global.fenguangReactionTime = sp.getInt(SPResource.KEY_FENGUANG_REACTION_TIME, 3 * 60);
//        Global.cardReactionTime = sp.getInt(SPResource.KEY_CARD_REACTION_TIME, 10 * 60);
        String str1 = sp.getString(SPResource.KEY_CARD_WARM_TIME, "180s");
        Global.cardWarmTime = ToolUtils.replenishInt(str1, "s");
//        String str2 = sp.getString(SPResource.KEY_FENGUANG_REACTION_TIME, "180s");
//        Global.fenguangReactionTime = ToolUtils.replenishInt(str2, "s");
//        String str3 = sp.getString(SPResource.KEY_CARD_REACTION_TIME, "15s");
//        Global.cardReactionTime = ToolUtils.replenishInt(str3, "s");

//        com.example.utils.http.Global.BASE_URL = sp.getString(SPResource.KEY_UPLOAD_URL, "http://112.5.17.83:8081/get.aspx");
//        Global.TESTING_UNIT_NAME = sp.getString(SPResource.KEY_UPLOAD_USERNAME, "0109001");
//        Global.TESTING_UNIT_NUMBER = sp.getString(SPResource.KEY_UPLOAD_PASSWORD, "0109001");
//        Global.device_id = sp.getString(SPResource.KEY_COMPARE_VALUE, "");
//        Global.uploadModel = sp.getInt(SPResource.KEY_UPLOAD_MODE, 1);
//        Global.limitValue = sp.getInt(SPResource.KEY_CARD_TEST_LIMIT_VALUE, 400);
        List<CheckOrg> checkOrgs = new CheckOrg().findAll(new Selector(CheckOrg.class)
                .where("isSelect", "=", true));
        if (checkOrgs != null && checkOrgs.size() > 0) {
            Global.checkOrg = checkOrgs.get(0).getName();
        }
        List<Inspector> inspectors = new Inspector().findAll(new Selector(Inspector.class)
                .where("isSelect", "=", true));
        if (inspectors != null && inspectors.size() > 0) {
            Global.inspector = inspectors.get(0).getName();
        }

        if (isFirst) {
            prints_check = new ArrayList<>();
            prints_data_manager = new ArrayList<>();
            Log.d("initData", "prints_check=" + prints_check.toString()
                    + "prints_data_manager=" + prints_data_manager.toString());
            prints_check.add(new Print("检测时间", true, true, true));
            prints_check.add(new Print("样品名称", true, true, true));
            prints_check.add(new Print("吸光度", true, true, true));
            prints_check.add(new Print("判定结果", true, true, true));
            prints_check.add(new Print("通道号", true, true, true));
            prints_check.add(new Print("样品编号", true, false, false));
            prints_check.add(new Print("被检测单位", true, false, false));
            prints_check.add(new Print("重量", true, false, false));
            prints_check.add(new Print("商品来源", true, false, false));
            prints_check.add(new Print("限量标准", true, false, false));
            prints_check.add(new Print("检测单位", true, false, false));
            prints_check.add(new Print("检测人员", true, false, false));
//            prints_check.add(new Print("限量标准", true, true, false));

            prints_data_manager.add(new Print("检测时间", true, true, true));
            prints_data_manager.add(new Print("样品名称", true, true, true));
            prints_data_manager.add(new Print("吸光度", true, true, true));
            prints_data_manager.add(new Print("判定结果", true, true, true));
            prints_data_manager.add(new Print("通道号", true, true, true));
            prints_data_manager.add(new Print("被检测单位", true, false, false));
            prints_data_manager.add(new Print("检测单位", true, true, false));
            prints_data_manager.add(new Print("检测人员", true, true, false));
            prints_data_manager.add(new Print("商品来源", true, false, false));
            prints_data_manager.add(new Print("样品编号", true, false, false));
            prints_data_manager.add(new Print("重量", true, false, false));
            prints_data_manager.add(new Print("限量标准", true, true, false));
//            prints_data_manager.add(new Print("限量标准", true, true, false));


            spUtils.setDataList(SPResource.KEY_PRINT_CHECK_DATA, prints_check);
            spUtils.setDataList(SPResource.KEY_PRINT_DATA_MANAGER_DATA, prints_data_manager);
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        //多功能检测跳转
        if (i == R.id.btn_pesticide_test) {//                printTest(true);
//            Intent startTest = new Intent(this, PesticideTestActivity2.class);
//                startTest.putParcelableArrayListExtra("project",projects);
//            startActivity(startTest);
            Intent intent = new Intent(this, TestTaskActivity.class);
            intent.putExtra("source", 0);
            startActivity(intent);
            Log.d(TAG, "projects=" + projects.size());
        } else if (i == R.id.btn_data_manage) {
            startActivity(new Intent(this, ResultQueryActivity.class));
        } else if (i == R.id.btn_sys_setting) {
            startActivity(new Intent(this, SystemSettingActivity2.class));
        }
    }

    public static void openPDFInNative(Context context, String FILE_NAME) {
        Log.d(TAG, "openPDFInNative FILE_NAME=" + FILE_NAME);
//        File file = new File(context.getExternalCacheDir(),FILE_NAME);
        File file = new File(FILE_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/pdf");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w("URLSpan", "Activity was not found for intent, " + intent.toString());
        }
    }

    private void printTest(boolean b) {
        //1b 09
        //1b 32 00  正面
        //1b 15
        //1b 09
        //1b 32 01  反面
        //1b 15


//        SerialUtils.COM4_SendData(new byte[]{0x1b, 0x09});
//
//        SerialUtils.COM4_SendData(new byte[]{0x1b, 0x32, 0x00});
//
//        SerialUtils.COM4_SendData(new byte[]{0x1b, 0x15});

        if (b) {
            StringBuilder sb = new StringBuilder("\n\n\n");
            sb.append("检测单位1" + "\n");
            sb.append("检测单位2" + "\n");
            sb.append("检测单位3" + "\n");
            try {
                SerialUtils.COM4_SendData(sb.toString().getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            String msg = new String("\n\n\n");
            msg += "检测单位1" + "\n" + "检测单位2" + "\n" + "检测单位3" + "\n";
            try {
                SerialUtils.COM4_SendData(msg.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) mTimer.cancel();
        try {
            //sp.edit().remove(SPResource.KEY_FENGUANG_AC).commit();
            //sp.edit().remove(SPResource.KEY_COMPARE_VALUE).commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
//        if (System.currentTimeMillis() - lastExitTime > 2000) {
//            lastExitTime = System.currentTimeMillis();
//            APPUtils.showToast(this, "再按一次退出程序");
//        } else {
        super.onBackPressed();
//        }
    }

    private void initXlzMap() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在初始化，请等待……");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = getAssets().open(getResources().getString(R.string.excel_name));

                    File tempFile = new File(getCacheDir(), "test.xls");//临时文件，第二个参数为文件名字，可随便取
                    fos = new FileOutputStream(tempFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) {//while循环进行读取
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();

                    Workbook book = Workbook.getWorkbook(tempFile);//用读取到的表格文件来实例化工作簿对象（符合常理，我们所希望操作的就是Excel工作簿文件）
                    Sheet[] sheets = book.getSheets(); //得到所有的工作表
                    List<SampleName> list = new ArrayList<>();
                    if (sheets.length > 0) {
                        Sheet sheet = book.getSheet(0);
                        for (int i = 1; i < sheet.getRows(); i++) {
//                            String sampleNum = sheet.getCell(0, i).getContents();
                            String sampleName = sheet.getCell(2, i).getContents();
                            if (!TextUtils.isEmpty(sampleName)) {
                                String sampleNumber = sheet.getCell(1, i).getContents();
                                String sampleType = sheet.getCell(2, i).getContents();
                                SampleName sn = new SampleName(sampleName, sampleType, sampleNumber);
                                SampleName.ProjectList<SampleName.Project> snps = new SampleName.ProjectList<>();
                                for (int j = 4; j < sheet.getColumns(); j++) {
                                    String projectName = sheet.getCell(j, 0).getContents();
                                    String projectJcx = sheet.getCell(j, i).getContents();
//                                    Log.d(TAG,"column="+j+"sampleName="+sampleName+"projectName="+projectName);
                                    if (projectName != null && projectJcx != null && !projectJcx.equals("")) {
                                        SampleName.Project snp = new SampleName.Project();
                                        snp.projectName = projectName;
                                        snp.jcx = Float.valueOf(projectJcx);
                                        snp.parent_id = sampleName;
                                        snps.add(snp);
                                    }
                                }
//                                if (i ==1 && snps!=null&&snps.size()>0){
//                                    projects.clear();
//                                    projects.addAll(snps);
//                                }
                                CheckOrg checkOrg = new CheckOrg();
                                checkOrg.co_id = i;
                                sn.projects = snps;
                                new SampleName.Project().saveAll(snps);
                                list.add(sn);
                                Log.d(TAG, "i=" + i + "sn=" + sn.toString());
                            }
                        }
                    }
                    Log.d(TAG, "SampleName().saveAll(list)=" + list.size());
//                    new SampleName().save(list.get(0));
                    new SampleName().saveAll(list);
                    sp.edit().putBoolean(SPResource.KEY_FIRST_ENTER, false).apply();
                    if (Global.DEBUG) Log.i(TAG, "数据初始化完毕");
                    progressDialog.dismiss();
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            Log.i(TAG, "数据初始化失败");
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }.start();
    }
}
