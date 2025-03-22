package com.open.soft.openappsoft.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.example.qrcodescan.QRCode;
import com.example.utils.http.Global;
import com.example.utils.http.ToolUtil;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoModel;
import com.open.soft.openappsoft.activity.task.TaskListActivity;
import com.open.soft.openappsoft.activity.task.TestTaskActivity;
import com.open.soft.openappsoft.data2.JingWeiDataBean;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivity;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivityByMen;
import com.open.soft.openappsoft.jinbiao.activity.TSCheckActivity;
import com.open.soft.openappsoft.jinbiao.location.LocationService;
import com.open.soft.openappsoft.jinbiao.model.CompanyNameData;
import com.open.soft.openappsoft.jinbiao.model.LineModel;
import com.open.soft.openappsoft.jinbiao.model.PdfRootBean;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.multifuction.model.CheckOrg;
import com.open.soft.openappsoft.multifuction.model.Print;
import com.open.soft.openappsoft.multifuction.model.Project;
import com.open.soft.openappsoft.multifuction.model.SampleName;
import com.open.soft.openappsoft.multifuction.resource.SPResource;
import com.open.soft.openappsoft.multifuction.util.DownloadUtil;
import com.open.soft.openappsoft.multifuction.util.SPUtils;
import com.open.soft.openappsoft.multifuction.util.SerialUtils;
import com.open.soft.openappsoft.multifuction.util.ToolUtils;
import com.open.soft.openappsoft.sql.activity.SQL_Activity;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;
import com.open.soft.openappsoft.util.AESUtil;
import com.open.soft.openappsoft.util.InterfaceURL;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import jxl.Sheet;
import jxl.Workbook;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "MainActivity";
    private View btn_open_2;// 比色法模块
    private View btn_open_3;// 金标卡模块
    private View btn_open_4;// atp模块
    private View ll_sql;// 金标卡模块
    private View btn_open_login;//
    private View btn_open_book;//
    private View btn_open_setting;//
    private View btn_open_know;//

    /*private int count = 3;//定位次数
    private String locationMsg = "";//定位消息
    private LocationService locationService;//定位服务*/


    // 百度定位次数
    public int count = 3;//定位次数
    public String locationMsg = "";//定位消息
    public LocationService locationService;//定位服务

    public int count1 = 0; // 定位次数（高德）


    public static String mac_url; // mac地址

    //    String samplingMode  = com.example.utils.http.Global.SamplingMode;

    QRCode qrCode;
    SharedPreferences sp;

    //构建数据库对象
    @GT.Hibernate.Build(setSqlVersion = 7)
    public static GT.Hibernate hibernate;

    public static boolean isFirst = false;


    final static int COUNTS = 4;// 点击次数
    final static long DURATION = 1000;// 规定有效时间
    long[] mHits = new long[COUNTS];

    @Override
    protected void onStart() {
        super.onStart();

        String name = LoginActivity.sp_ServiceUrl.query("name").toString();
//        if (!"0".equals(name)) {
//            InterfaceURL.companyName = name;//赋值
//        }

        TextView tv_homeLocation = findViewById(R.id.tv_homeLocation);
        String verName = GT.ApplicationUtils.getVerName(this);
        tv_homeLocation.setText("v" + verName);

        //动态标题
        TextView tv_title = findViewById(R.id.tv_title);
        String title = LoginActivity.sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            tv_title.setText(InterfaceURL.oneModule);
        } else {
            tv_title.setText(title);
        }
        //内测渠道
        tv_homeLocation.setOnLongClickListener(v -> {
            final EditText inputServer = new EditText(MainActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("测试专用，暂不开放").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String input = inputServer.getText().toString();

                    if ("test".equals(input)) {
                        Intent intent = new Intent(MainActivity.this, com.open.soft.openappsoft.jinbiao.activity.CheckPaintActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtra("source", "2");// 定性
                        startActivity(intent);
                    }
                }
            });
            builder.show();


            return false;
        });

    }

    private void continuousClick(int count, long time) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
//            Toast.makeText(this, "连续点击了4次", Toast.LENGTH_LONG).show();

            final EditText inputServer = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("测试专用，暂不开放").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String input = inputServer.getText().toString();

                    if ("test".equals(input)) {
                        Intent intent = new Intent(MainActivity.this, com.open.soft.openappsoft.jinbiao.activity.CheckPaintActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtra("source", "2");// 定性
                        startActivity(intent);
                    }
                }
            });
            builder.show();
        }
    }

    public static GT.GT_SharedPreferences gt_sp;


    // 百度定位
    private LocationReceiver locationReceiver;


    //声明AMapLocationClient类对象(高德)
    public AMapLocationClient mLocationClient = null;//高德定位广播

    private LocationReceiver1 locationReceiver1;//自定义内部类广播 服务于 定位


    String[] projectNames = new String[]{
            "甲醛", "吊白块", "二氧化硫", "亚硝酸盐", "双氧水", "硼砂", "甲醇", "硫酸铝钾", "重金属铅", "山梨酸钾", "溴酸钾", "糖精钠", "组胺",
            "挥发性盐基氮", "丙二醛", "蛋白质", "氨基酸态氮", "硫氰酸盐", "过氧化苯甲酰", "谷氨酸钠", "碘酸钾", "食醋总酸", "硫酸镁", "甜蜜素",
            "苯甲酸钠", "羟甲基糠醛", "果糖和葡萄糖", "重金属铬", "重金属镉", "蜂蜜中蔗糖", "钾离子", "柠檬黄", "日落黄", "胭脂红", "苋菜红",
            "亮蓝", "靛蓝", "尿素", "硫化钠", "亚铁氰化钾", "硫酸铜", "余氯", "氯离子", "硝酸盐", "酱油总酸", "过氧化值", "茶多酚", "酸价", "食用油酸价"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //黑屏问题解决办法
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        // 初始化
        initModel();
        initView();
//        checkIsadmin();
        GT.WindowUtils.hideActionBar(this);
        GT.getGT().build(this);//绑定当前Activity


//        Timber.i(hibernate.getSQLAllTableName());//获取 DemoSQL数据库所有表名称
//        Timber.i(hibernate.getTableAllValue(DetectionResultBean.class));//获取 DemoBean 表所有字段名称


        // 百度定位
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(mListener);
        locationService.start();

        // 注册广播(百度)
        locationReceiver = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LocationAction");
        registerReceiver(locationReceiver, intentFilter); //注册广播

        mac_url = getMacDefault(this) + "";
        mac_url = handleMacUrl(mac_url);

        gt_sp = new GT.GT_SharedPreferences(this, getClass().getName(), true);
        qrCode = new QRCode(this);
        qrCode.register();
        qrCode.startScanListener();
        qrCode.setoScanParameterSuccess(new QRCode.OnScanParameterSuccess() {
            @Override
            public void oScanParameterSuccess(String msg) {
                Log.d("MainActivity", "MainActivity msg=" + msg);
                String qrCode = ToolUtil.getSampleQrCode(msg);
                Log.d("MainActivity", "MainActivity qrCode1=" + qrCode + " msg=" + msg);
                Intent intent = new Intent();
                //不是样品的,默认为是检测卡的
                if (qrCode == null || qrCode.isEmpty()) {
                    Log.d("MainActivity", "QRCODE_LOCAL_MSG_CARD qrCode2=" + qrCode);
                    intent.setAction(QRCode.QRCODE_LOCAL_MSG_CARD);
                    intent.putExtra(QRCode.QRCODE_LOCAL_MSG, msg);
                } else {
                    Log.d("MainActivity", "QRCODE_LOCAL_MSG_SAMPLE msg=" + msg);
                    intent.setAction(QRCode.QRCODE_LOCAL_MSG_SAMPLE);
                    intent.putExtra(QRCode.QRCODE_LOCAL_MSG, qrCode);
                }
                Log.d("MainActivity", "MainActivity qrCode2=" + qrCode);
                sendBroadcast(intent);
            }
        });

        sp = getSharedPreferences(SPResource.FILE_NAME, Context.MODE_PRIVATE);
        String str1 = sp.getString(SPResource.KEY_CARD_WARM_TIME, "180s");
        com.open.soft.openappsoft.multifuction.util.Global.cardWarmTime = ToolUtils.replenishInt(str1, "s");

        String sdPath = Environment.getExternalStorageDirectory().getPath();

        // 获取pdfname
        String pdf_name = Global.companyCode_value + "_" + Global.deviceType_value + "_" + Global.samplingMode_value + ".pdf";
        Global.URI_MULT = sdPath + pdf_name;
        if (SharedPreferencesUtil.getDefaultSharedPreferences(this).getBoolean("isFirst", true)) {
            Project project = new Project("",
                    "农药残留",
                    "GB/T 5009.199", 50f, 1f, 0f, 410);
            project.save(project);
            for (int i = 0; i < projectNames.length; i++) {
                Project project2 = new Project("",
                        projectNames[i],
                        "GB/T 5009.199", 1f, 1f, 0f, 410);
                project.save(project2);
            }

//            projects = (ArrayList<Project>) new Project().findAll();
            initBook();
            initXlzMap();
        }

        //是否模拟器测试
        if (!InterfaceURL.isSimulatorTest) {
            initMult();
            initJinBiao();
        }

        /*//动态标题
        TextView tv_title = findViewById(R.id.tv_title);

        String title = gt_sp.query("TitleSet").toString();
        if(title.isEmpty() || title.equals("0")){
            tv_title.setText(InterfaceURL.oneModule);
        }else{
            tv_title.setText(title);
        }*/

        //隐藏不需要的模块
//        if ("农药残留检测仪".equals(InterfaceURL.oneModule)) {
//            findViewById(R.id.btn_open_3).setVisibility(View.GONE);
//            ((TextView) findViewById(R.id.tv_fggd_Name)).setText("检测");
//        } else if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {
//            findViewById(R.id.btn_open_2).setVisibility(View.GONE);
//            ((TextView) findViewById(R.id.tv_jtj_Name)).setText("检测");
//        }
//        test();

    }

    private void test() {
        String testStr = "{\"id\":\"689376395033448448\",\"qrcode\":\"\",\"checkUserId\":\"\",\"checkUser\":\"aaa\",\"checkItemId\":\"11\",\"checkItemName\":\"itemName\",\"checkMothedId\":\"mothedId\",\"checkMothedName\":\"methedName\",\"checkTime\":\"2025-03-16 12:30:20\",\"checkOrgId\":\"\",\"checkOrg\":\"青岛AA检测站\",\"deviceSn\":\"W123\",\"longitude\":\"\",\"latitude\":\"\",\"checkAddress\":\"\",\"sampleId\":\"\",\"sampleCode\":\"\",\"sampleTypeId\":\"\",\"sampleType\":\"肉类\",\"sampleSubTypeId\":\"\",\"sampleSubType\":\"猪肉\",\"sampleName\":\"猪肉001\",\"companyName\":\"青岛XXX屠宰场\",\"companyCode\":\"12345678987\",\"companyUser\":\"\",\"companyPhone\":\"\",\"sampleBatch\":\"\",\"samplingAddress\":\"\",\"sampleSource\":\"\",\"samplingOrg\":\"抽样单位\",\"samplingUser\":\"抽样人\",\"samplingTime\":\"2025-03-15 13:30:20\",\"checkLimit\":\"\",\"result\":0,\"resultValue\":null,\"resultInfo\":null,\"customize\":\"\"}";
        String key = "e2fd36a50c8c4759a5aaf1f094543a17";
        String iv = "0wIY0bAfkif1";
        String data = AESUtil.encrypt(testStr, key, iv);
        String ret = AESUtil.decrypt(data, key, iv);
        Log.d("MainActivity test", "data=" + data);
        Log.d("MainActivity test", "ret=" + ret);
    }

    // 监听其他页面发送的消息（百度定位）
    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String locationMessage = intent.getStringExtra("locationMessage");
                if ("开始定位".equals(locationMessage)) {
                    //启动定位
                    locationService.start();
                } else if ("停止定位".equals(locationMessage)) {
                    //停止定位
                    locationService.stop();
                }
            }

        }

    }


    // 监听其他页面发送的消息（高德定位）
    private class LocationReceiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String locationMessage = intent.getStringExtra("locationMessage");
                if ("开始定位".equals(locationMessage)) {
                    //启动定位
                    if (count1 > 0) {
                        mLocationClient.startLocation();
                    }
                } else if ("停止定位".equals(locationMessage)) {
                    //停止定位
                    if (count1 > 0) {
                        mLocationClient.stopLocation();
                    }
                }
            }

        }

    }

    private void initJinBiao() {
        com.open.soft.openappsoft.jinbiao.util.SerialUtils.InitSerialPort(this);
    }

    private void initMult() {
        SerialUtils.InitSerialPort(this);
    }

    public static void openPDFInNative(Context context, String FILE_NAME) {
        Intent intent = new Intent(context, FileActivity.class);
        intent.putExtra("isFile", true);
        intent.putExtra("url", FILE_NAME);
        intent.putExtra("fileType", "pdf");
        intent.putExtra("title", "操作说明");
        context.startActivity(intent);
    }

    private void initBook() {


        // 获取pdfname
        String pdf_name = Global.companyCode_value + "_" + Global.deviceType_value + "_" + Global.samplingMode_value + ".pdf";
        Log.d(TAG, "initBook");
        new Thread() {
            @Override
            public void run() {
                super.run();
                //
                AssetManager am = MainActivity.this.getAssets();
                InputStream is = null;
                try {
                    is = am.open(pdf_name);
                    // 获取SD卡根路径
                    FileOutputStream fos = new FileOutputStream(com.example.utils.http.Global.URI_MULT);
                    byte[] buff = new byte[512];
                    int count = is.read(buff);
                    while (count != -1) {
                        fos.write(buff);
                        count = is.read(buff);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        btn_open_2 = findViewById(R.id.btn_open_2);
        btn_open_3 = findViewById(R.id.btn_open_3);
        btn_open_4 = findViewById(R.id.btn_open_4);
        ll_sql = findViewById(R.id.ll_sql);
        btn_open_setting = findViewById(R.id.btn_open_setting);
        btn_open_login = findViewById(R.id.btn_open_login);
        btn_open_know = findViewById(R.id.btn_open_know);
        btn_open_book = findViewById(R.id.btn_open_book);
        btn_open_2.setOnClickListener(this);
        btn_open_3.setOnClickListener(this);
        btn_open_4.setOnClickListener(this);
        ll_sql.setOnClickListener(this);
        btn_open_setting.setOnClickListener(this);
        btn_open_login.setOnClickListener(this);
        btn_open_know.setOnClickListener(this);
        btn_open_book.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_open_2:
                //分光光度
//                if (!Global.isAdimin) {
//                    Intent intent1 = new Intent(this, com.open.soft.openappsoft.multifuction.activity.PesticideTestActivity2.class);
//                    startActivity(intent1);
//                } else {
                Intent intent1 = new Intent(this, com.open.soft.openappsoft.multifuction.activity.MainActivity.class);
                startActivity(intent1);
//                }
                break;
            case R.id.btn_open_4:
                //ATP
                Intent intentAtp = new Intent(this, TestTaskActivity.class);
                intentAtp.putExtra("source", TestTaskActivity.source_atp);
                startActivity(intentAtp);
                break;
            case R.id.btn_open_3:

                /*Intent intent10 = new Intent(this, CheckActivityByMen.class);
                intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent10.putExtra("source", "2");// 定性
                startActivity(intent10);*/

                //胶体金
//                if (!Global.isAdimin) {
//                    // 获取参数  归属地 本地判断是否是唐山界面进行样本名称选择
//                    String AreaId = com.example.utils.http.Global.admin_pt;
//                    if (AreaId.equals("TangshanNMEnterprise")) {
//                        Intent intent3 = new Intent(this, TSCheckActivity.class);
//                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                        intent3.putExtra("source", "2");// 定性
//                        startActivity(intent3);
//                    } else if (AreaId.equals("XAEnterprise")) {
//                        Intent intent3 = new Intent(this, TSCheckActivity.class);
//                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                        intent3.putExtra("source", "2");// 定性
//                        startActivity(intent3);
//                    } else {
//                        if ((Global.isVoluntarily && !Global.ismixedentry) || (Global.isVoluntarily && Global.ismixedentry)) {
//                            //自动录入
//                            Intent intent2 = new Intent(this, CheckActivity.class);
//                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            intent2.putExtra("source", "2");// 定性
//                            startActivity(intent2);
//                        } else if (!Global.isVoluntarily && !Global.ismixedentry) {
//                            //手动录入
//                            Intent intent10 = new Intent(this, CheckActivityByMen.class);
//                            intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            intent10.putExtra("source", "2");// 定性
//                            startActivity(intent10);
//                        }
//                    }
//
//
//                } else {
                Intent intent2 = new Intent(this, com.open.soft.openappsoft.jinbiao.activity.MainActivity.class);
                startActivity(intent2);
//                }
                break;

            case R.id.ll_sql:
                //数据管理

               /* if(!InterfaceURL.isDetectionAppUpdate){
                    startActivity(new Intent(MainActivity.this, SQL_Activity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, SQL2_Activity.class));
                }*/

//                Object obj = GT.AppDataPool.Interior.queryDataPool(DialogFragmentDemo.class, "name");
//                String name = obj.toString();

                // 旧版数据库
//                 startActivity(new Intent(MainActivity.this, SQL2_Activity.class));

                // 新版数据库
                startActivity(new Intent(MainActivity.this, SQL_Activity.class));

                break;

            case R.id.btn_open_book:

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在检测PDF中，请等待……");
                progressDialog.setCancelable(false);

                // 获取pdfname

                if (Global.company_name == "") {
                    Global.companyCode_value = "Xindaan";
                } else {
                    Global.companyCode_value = Global.company_name;
                }

                if ("多参数食品安全检测仪".equals(interfaceURL.oneModule)) {
                    Global.deviceType_value = "M417";
                } else if ("农药残留检测仪".equals(interfaceURL.oneModule)) {
                    Global.deviceType_value = "MC4011";
                } else if ("农药残留单项精准分析仪".equals(interfaceURL.oneModule)) {
                    Global.deviceType_value = "NAD4074";
                }

                if (Global.isVoluntarily) {
                    Global.samplingMode_value = "scan";
                } else {
                    Global.samplingMode_value = "input";
                }
                String pdf_name = Global.companyCode_value + "_" + Global.deviceType_value + "_" + Global.samplingMode_value + ".pdf";

//                String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";


                if (new File(appSavePath + pdf_name).exists()) {


                    //操作手册
                    Global.URI_MULT = appSavePath + pdf_name;
                    openPDFInNative(MainActivity.this, Global.URI_MULT);
//
                } else {

                    try {
                        if (!GT.Network.isInternet(MainActivity.this)) {
                            Toast.makeText(this, "当前没有网络，请检查网络是否连接正常", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                    } catch (Exception e) {
                        Timber.i("异常:" + e);
                    }


                    getpdfurl(appSavePath);

                }


                break;

            case R.id.btn_open_know:
                //知识库
                Intent intent5 = new Intent(MainActivity.this, knowledgeActivity.class);
                startActivity(intent5);
                break;

            case R.id.btn_open_setting:
                Intent intent3 = new Intent(this, SettingActivity.class);
                startActivity(intent3);

                break;

            case R.id.btn_open_login:

                Global.isAdimin = false;
                Intent intent4 = new Intent(this, LoginActivity.class);
                startActivity(intent4);
                finish();

                //定位 服务初始化
//                GT.toast(MainActivity.this,"点击开始定位");
//                locationService.start();// 定位SDK

                break;

        }


    }

    // 访问操作手册接口，返回pdf地址
//    {
//        "companyCode":"Xindaan",
//            "deviceType":"M417",
//            "samplingMode":"scan"
//    }

    String url_pdf;
    CompanyNameData companyName = new CompanyNameData();
    InterfaceURL interfaceURL = new InterfaceURL();


    public void getpdfurl(String appSavePath) {


        // 获取必要参数值
        if ("".equals(Global.company_name)) {
            Global.companyCode_value = "Aoben";
        } else {
            Global.companyCode_value = Global.company_name;
        }

        if ("多参数食品安全检测仪".equals(interfaceURL.oneModule)) {
            Global.deviceType_value = "M417";
        } else if ("农药残留检测仪".equals(interfaceURL.oneModule)) {
            Global.deviceType_value = "MC4011";
        } else if ("农药残留单项精准分析仪".equals(interfaceURL.oneModule)) {
            Global.deviceType_value = "NAD4074";

        }

        if (Global.isVoluntarily) {
            Global.samplingMode_value = "scan";
        } else {
            Global.samplingMode_value = "input";
        }

        String data = "{\n" +
                "\"companyCode\":\"" + Global.companyCode_value + "\",\n" +
                "\"deviceType\":\"" + Global.deviceType_value + "\",\n" +
                "\"samplingMode\":\"" + Global.samplingMode_value + "\"\n" +
                "}";

        String pdf_name = Global.companyCode_value + "_" + Global.deviceType_value + "_" + Global.samplingMode_value + ".pdf";

//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + "Other/GetManual", data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                PdfRootBean pdfRootBean = new Gson().fromJson(response, PdfRootBean.class);
//                if (pdfRootBean != null) {
//                    if (pdfRootBean.getData() != null) {
//                        url_pdf = pdfRootBean.getData().getUrl();
//                        new DownloadUtil().download(url_pdf, appSavePath, pdf_name, new DownloadUtil.OnDownloadListener() {
//                            @Override
//                            public void onDownloadSuccess(File file) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        progressDialog.dismiss();
//                                        //操作手册
//                                        String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
//                                        Global.URI_MULT = appSavePath + pdf_name;
//                                        openPDFInNative(MainActivity.this, Global.URI_MULT);
//                                    }
//                                });
//
//                            }
//
//                            @Override
//                            public void onDownloading(int progress) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        progressDialog.setMessage("正在更新最新PDF，请稍等...已完成 " + progress + "%");
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onDownloadFailed(Exception e) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        progressDialog.dismiss();
//                                    }
//                                });
//
//                            }
//                        });
//                    } else {
//                        String errMsg = pdfRootBean.getErrMsg();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "暂无此操作手册，敬请等待更新。。。", Toast.LENGTH_LONG).show();
//                                progressDialog.dismiss();
//                            }
//                        });
//                    }
//
//
//                }
//
//            }
//
//            @Override
//            public void onError(String response) {
//                Toast.makeText(MainActivity.this, "暂无此操作手册，敬请等待更新。。。", Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//            }
//        });
        HashMap dataMap = new HashMap();
        dataMap.put("companyCode", Global.companyCode_value);
        dataMap.put("deviceType", Global.deviceType_value);
        dataMap.put("samplingMode", Global.samplingMode_value);
        progressDialog.show();
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "Other/GetManual", dataMap, new GT.HttpUtil.OnLoadData() {
            @Override
            public void onSuccess(String response, Object o) {
                PdfRootBean pdfRootBean = new Gson().fromJson(response, PdfRootBean.class);
                if (pdfRootBean != null) {
                    if (pdfRootBean.getData() != null) {
                        url_pdf = pdfRootBean.getData().getUrl();
                        new DownloadUtil().download(url_pdf, appSavePath, pdf_name, new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        //操作手册
                                        String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                                        Global.URI_MULT = appSavePath + pdf_name;
                                        openPDFInNative(MainActivity.this, Global.URI_MULT);
                                    }
                                });

                            }

                            @Override
                            public void onDownloading(int progress) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setMessage("正在更新最新PDF，请稍等...已完成 " + progress + "%");
                                    }
                                });
                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                Timber.i(e);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });

                            }
                        });
                    } else {
                        String errMsg = pdfRootBean.getErrMsg();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "暂无此操作手册，敬请等待更新。。。", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(String response, Object o) {
                super.onError(response, o);
                Toast.makeText(MainActivity.this, "暂无此操作手册，敬请等待更新。。。", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        }, false);

    }

    // 添加功能：根据登录账户选择参数设置显示或隐藏
    public void checkIsadmin() {
        if (!Global.isAdimin) {
            // 隐藏
            btn_open_setting.setVisibility(View.GONE);
        }
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    ProgressDialog progressDialog;

    //数据库初始化进度条
    private int schedule = 1;

    private void initXlzMap() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在初始化，请等待……");
        progressDialog.setCancelable(false);
        progressDialog.show();
        schedule = 1;
        new Thread() {
            @Override
            public void run() {

                initDB();

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
                            String sampleName = sheet.getCell(2, i).getContents();
                            if (!TextUtils.isEmpty(sampleName)) {
                                String sampleNumber = sheet.getCell(1, i).getContents();
                                String sampleType = sheet.getCell(2, i).getContents();
                                SampleName sn = new SampleName(sampleName, sampleType, sampleNumber);
                                SampleName.ProjectList<SampleName.Project> snps = new SampleName.ProjectList<>();
                                schedule += 1;
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
                                CheckOrg checkOrg = new CheckOrg();
                                checkOrg.co_id = i;
                                sn.projects = snps;
                                new SampleName.Project().saveAll(snps);
                                list.add(sn);
                                GT.Thread.runAndroid(MainActivity.this, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (schedule < 1345) {
                                            progressDialog.setMessage("正在初始化数据，请等待……" + schedule + "/1348");
                                        } else {
                                            progressDialog.setMessage("初始化完成！");
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
//                                log("i=" + i + "sn=" + sn.toString());
                            }
                        }
                    }
                    new SampleName().saveAll(list);
                    SharedPreferencesUtil.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean("isFirst", false).apply();
                    if (com.open.soft.openappsoft.multifuction.util.Global.DEBUG)
                        progressDialog.dismiss();
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }.start();
    }

    String[] initName1 = {"畜禽产品类", "农产品类", "水产品类", "加工制成类", "餐饮器具类"};
    String[] initCode1 = {"A", "C", "F", "P", "T",};
    String[] initName2 = {"畜肉类", "禽肉类", "禽蛋类", "餐具类", "专间类", "熟食类", "豆制品类", "腌制品类", "粮油类", "加工类", "水果类", "蔬菜类", "乳制品类", "干货类", "酒类", "水产品类", "调味品类", "其它"};
    String[] initCode2 = {"A001", "A002", "A003", "T001", "T002", "P001", "P002", "P003", "C001", "P004", "C002", "C003", "P005", "C004", "P006", "F001", "P007", "P999"};
    String[] initName3 = {"农贸市场1", "农贸市场2"};
    String[] initCode3 = {"00100001", "222200000"};
    String[] initName4 = {"检测机构1", "检测机构2"};

    private void initDB() {

        for (int i = 0; i < initName1.length; i++) {
            OrderInfoModel orderInfoModel = new OrderInfoModel(initName1[i], initCode1[i], OrderInfoModel.type_sample_type_main);
            hibernate.save(orderInfoModel);
        }
        for (int i = 0; i < initName2.length; i++) {
            OrderInfoModel orderInfoModel = new OrderInfoModel(initName2[i], initCode2[i], OrderInfoModel.type_sample_type_child);
            hibernate.save(orderInfoModel);
        }
        for (int i = 0; i < initName3.length; i++) {
            OrderInfoModel orderInfoModel = new OrderInfoModel(initName3[i], initCode3[i], OrderInfoModel.type_bcheck);
            hibernate.save(orderInfoModel);
        }
        for (int i = 0; i < initName4.length; i++) {
            OrderInfoModel orderInfoModel = new OrderInfoModel(initName4[i], "", OrderInfoModel.type_check);
            hibernate.save(orderInfoModel);
        }
    }


    private String LocationX = "";
    private String LocationY = "";
    private String LocationAddress = "";


    //定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {

            locationMsg = "";
            LocationX = "";
            LocationY = "";
            LocationAddress = "";

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());

                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                if (!"null".equals(String.valueOf(location.getLatitude()))) {
                    locationMsg += location.getLatitude() + ",";
                }
                sb.append("\nlongtitude : ");// 经度
                sb.append(location.getLongitude());

                if (!"null".equals(String.valueOf(location.getLongitude()))) {
                    locationMsg += location.getLongitude() + ",";
                }

                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nProvince : ");// 获取省份
                sb.append(location.getProvince());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nTown : ");// 获取镇信息
                sb.append(location.getTown());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());

                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());

                if (!"null".equals(String.valueOf(location.getAddrStr()))) {
                    locationMsg += location.getAddrStr();
                }


                sb.append("\nStreetNumber : ");// 获取街道号码
                sb.append(location.getStreetNumber());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append("poiName:");
                        sb.append(poi.getName() + ", ");
                        sb.append("poiTag:");
                        sb.append(poi.getTags() + "\n");
                    }
                }
                if (location.getPoiRegion() != null) {
                    sb.append("PoiRegion: ");// 返回定位位置相对poi的位置关系，仅在开发者设置需要POI信息时才会返回，在网络不通或无法获取时有可能返回null
                    PoiRegion poiRegion = location.getPoiRegion();
                    sb.append("DerectionDesc:"); // 获取POIREGION的位置关系，ex:"内"
                    sb.append(poiRegion.getDerectionDesc() + "; ");
                    sb.append("Name:"); // 获取POIREGION的名字字符串
                    sb.append(poiRegion.getName() + "; ");
                    sb.append("Tags:"); // 获取POIREGION的类型
                    sb.append(poiRegion.getTags() + "; ");
                    sb.append("\nSDK版本: ");
                }
                sb.append(locationService.getSDKVersion()); // 获取SDK版本
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                if ("".equals(locationMsg)) {
                    count--;
                    // 切换定位方法

                    count1 = 0;
                    if (count <= 0 && "".equals(locationMsg)) {
                        locationService.stop();
                        count1 = 3;
                        changeLocation();
                    }

                } else if (locationMsg.contains("-")) {
                    count--;
                    if (count <= 0 && locationMsg.contains("-")) {
                        locationService.stop();
                        count1 = 3;
                        changeLocation();
                    }
                }

                // 获取到定位数据的情况
                else {
                    count--;
                    String[] split = locationMsg.split(",");
                    if (count <= 0) {
                        locationService.stop();//停止获取定位信息
                        count = 3;
//                        locationService.unregisterListener(mListener); //注销掉监听
                    }

                    if (split.length == 3) {
                        locationService.stop();//停止获取定位信息
                        count = 3;
//                        locationService.unregisterListener(mListener); //注销掉监听
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.gt_sp.save("locationMsg", locationMsg);
                                String[] split_something = locationMsg.split(",");

                                LocationY = split_something[0];
                                LocationX = split_something[1];
                                LocationAddress = split_something[2];

                                Intent intent = new Intent("SendMessage");
                                intent.putExtra("LocationX", LocationX);
                                intent.putExtra("LocationY", LocationY);
                                intent.putExtra("LocationAddress", LocationAddress);
                                sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新

                            }
                        });
                    }

                }

            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * @param locType 当前定位类型
         * @param diagnosticType 诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
            StringBuffer sb = new StringBuffer(256);
            sb.append("诊断结果: ");
            if (locType == BDLocation.TypeNetWorkLocation) {
                if (diagnosticType == 1) {
                    sb.append("网络定位成功，没有开启GPS，建议打开GPS会更好");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 2) {
                    sb.append("网络定位成功，没有开启Wi-Fi，建议打开Wi-Fi会更好");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeOffLineLocationFail) {
                if (diagnosticType == 3) {
                    sb.append("定位失败，请您检查您的网络状态");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeCriteriaException) {
                if (diagnosticType == 4) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 5) {
                    sb.append("定位失败，无法获取有效定位依据，请检查运营商网络或者Wi-Fi网络是否正常开启，尝试重新请求定位");
                    sb.append(diagnosticMessage);
                } else if (diagnosticType == 6) {
                    sb.append("定位失败，无法获取有效定位依据，请尝试插入一张sim卡或打开Wi-Fi重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 7) {
                    sb.append("定位失败，飞行模式下无法获取有效定位依据，请关闭飞行模式重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 9) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeServerError) {
                if (diagnosticType == 8) {
                    sb.append("定位失败，请确认您定位的开关打开状态，是否赋予APP定位权限");
                    sb.append("\n" + diagnosticMessage);
                }
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Global.isAdimin = false;//取消登录状态
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 注销高德监听
        if (locationReceiver1 != null) {
            try {
                unregisterReceiver(locationReceiver1);
            } catch (Exception e) {
            }
        }

        // 注销百度监听
        if (locationReceiver != null) {
            try {
                unregisterReceiver(locationReceiver);
            } catch (Exception e) {
            }
        }


        qrCode.unregisterReceiverUsb(this);//销毁广播
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     *
     * @param context * @return
     */
    public static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
//        WifiManager wifi = (WifiManager)context.applicationContext.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * 对mac地址进行处理
     */
    private String handleMacUrl(String str) {

        String result = "";
        result = str.replaceAll(":", "_");
        return result;
    }


    private List<Print> prints_check;
    private List<Print> prints_data_manager;
    SPUtils spUtils;

    private void initModel() {

        sp = getSharedPreferences(SPResource.FILE_NAME, Context.MODE_PRIVATE);
        spUtils = new SPUtils(sp);
        isFirst = sp.getBoolean(SPResource.KEY_FIRST_ENTER, true);

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
//            prints_check.add(new Print("样品编号", true, false, false));
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
//            prints_data_manager.add(new Print("商品来源", true, false, false));
            prints_data_manager.add(new Print("样品编号", true, false, false));
            prints_data_manager.add(new Print("重量", true, false, false));
            prints_data_manager.add(new Print("限量标准", true, true, false));
//            prints_data_manager.add(new Print("限量标准", true, true, false));


            spUtils.setDataList(SPResource.KEY_PRINT_CHECK_DATA, prints_check);
            spUtils.setDataList(SPResource.KEY_PRINT_DATA_MANAGER_DATA, prints_data_manager);
        }
    }


    /**
     * 获取外网IP
     */

    private static String[] platforms = {
            "http://pv.sohu.com/cityjson",
            "http://pv.sohu.com/cityjson?ie=utf-8",
            "http://ip.chinaz.com/getip.aspx"
    };

    public String getOutNetIP(Context context, int index) {
        if (index < platforms.length) {
            BufferedReader buff = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(platforms[index]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);//读取超时
                urlConnection.setConnectTimeout(5000);//连接超时
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {//找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                    InputStream is = urlConnection.getInputStream();
                    buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        builder.append(line);
                    }

                    buff.close();//内部会关闭 InputStream
                    urlConnection.disconnect();

                    Log.e("xiaoman", builder.toString());
                    if (index == 0 || index == 1) {
                        //截取字符串
                        int satrtIndex = builder.indexOf("{");//包含[
                        int endIndex = builder.indexOf("}");//包含]
                        String json = builder.substring(satrtIndex, endIndex + 1);//包含[satrtIndex,endIndex)
                        JSONObject jo = new JSONObject(json);
                        String ip = jo.getString("cip");

                        return ip;
                    } else if (index == 2) {
                        JSONObject jo = new JSONObject(builder.toString());
                        return jo.getString("ip");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return getInNetIp(context);
        }
        return getOutNetIP(context, ++index);
    }


    public static String getInNetIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);

        return ip;
    }

    //这段是转换成点分式IP的码
    private static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
    }


    // 获取本地的外网ip
    public void IpLocation(String url) {

//        new GT.OkHttp(url).loadData(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String string = response.body().string();
//
//                JingWeiDataBean jingWeiDataBean = new Gson().fromJson(string, JingWeiDataBean.class);
//
//                // 外网ip
//                String ip = jingWeiDataBean.getQuery();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//
//            }
//        });
    }

    public void getAddress(String url) {

//        if (!"".equals(url)) {
//            new GT.OkHttp(url).loadData(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String string = response.body().string();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//
//                }
//            });
//        }
    }


    /**
     * 跳转高德定位
     */
    private void changeLocation() {

        if (count <= 0) {
            // 高德定位  初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());

            //设置定位回调监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null) {
                        count1--;
                        if (aMapLocation.getErrorCode() == 0) {

                            if (!"".equals(aMapLocation.getAddress()) && !"".equals(aMapLocation.getLongitude() + "") && !"".equals(aMapLocation.getLatitude() + "")) {

                                LocationX = aMapLocation.getLongitude() + "";
                                LocationY = aMapLocation.getLatitude() + "";
                                LocationAddress = aMapLocation.getAddress();

                                String locationMsg_gaode = LocationY + LocationX + LocationAddress;

                                MainActivity.gt_sp.save("locationMsg", locationMsg_gaode);
                                Intent intent = new Intent("SendMessage");
                                intent.putExtra("LocationX", LocationX);
                                intent.putExtra("LocationY", LocationY);
                                intent.putExtra("LocationAddress", LocationAddress);
                                sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新
                            }

                            mLocationClient.stopLocation();
                            count = 3;
                        }
                        if (count1 <= 0) {
                            mLocationClient.stopLocation();
                            count = 3;
                        }
                    }
                }
            });
            // 启动定位
            mLocationClient.startLocation();

            //注册广播（高德）
            locationReceiver1 = new LocationReceiver1();
            //实例化过滤器并设置要过滤的广播
            IntentFilter intentFilter1 = new IntentFilter();
            intentFilter1.addAction("LocationAction");
            registerReceiver(locationReceiver1, intentFilter1); //注册广播
        }
    }
}
