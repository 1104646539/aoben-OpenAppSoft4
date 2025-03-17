package com.open.soft.openappsoft.multifuction.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodescan.QRCode;
import com.example.utils.http.CheckPresenter;
import com.example.utils.http.GetQRInfoBean;
import com.example.utils.http.GetQRInfoResultBean;
import com.example.utils.http.GetSamplingInfoBean;
import com.example.utils.http.GetSamplingInfoResultBean;
import com.example.utils.http.Result;
import com.example.utils.http.StatusDialog;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.App;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.jinbiao.model.CardCompanyModel;
import com.open.soft.openappsoft.jinbiao.model.LineModel;
import com.open.soft.openappsoft.jinbiao.model.PeopleModel;
import com.open.soft.openappsoft.jinbiao.model.SampleModel;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeModel;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean1;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean2;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean3;
import com.open.soft.openappsoft.multifuction.adapter.TestAdapter;
import com.open.soft.openappsoft.multifuction.model.CheckResult;
import com.open.soft.openappsoft.multifuction.model.Project;
import com.open.soft.openappsoft.multifuction.resource.SPResource;
import com.open.soft.openappsoft.multifuction.util.APPUtils;
import com.open.soft.openappsoft.multifuction.util.DensityUtil;
import com.open.soft.openappsoft.multifuction.util.Global;
import com.open.soft.openappsoft.multifuction.util.SerialUtils;
import com.open.soft.openappsoft.multifuction.util.ToolUtils;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;
import com.open.soft.openappsoft.util.InterfaceURL;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * 卡片检测
 */
public class PesticideTestActivity2 extends TestActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener, StatusDialog.OnProgressStatusChange, CheckPresenter.CheckInterface, TestAdapter.OnShowSampleDialog {
    private String errMsg;
    private static final int BS_START = 6;
    private static final float COMPARE_MIN_VALUE = 0.2f;
    private static final String TAG = PesticideTestActivity2.class.getSimpleName();
    private String jsSubProductId = "";

    //添加未保存本地的数据
    List SamplNumlist = new ArrayList();
    List typelist = new ArrayList();
    List locId = new ArrayList();
    List locsp1Id = new ArrayList();
    List locsp2Id = new ArrayList();
    List locsp3Id = new ArrayList();
    private List subProductId = new ArrayList();
    private List yplblist = new ArrayList();


    private Button btnReturn;
    private Button btnTest;
    private ImageView cbSelectAll;
    private LinearLayout llSelectAll;

//    private TextView tvCurrentTemp;

    private Timer tempTimer;
    private Timer reactionTimer;

    private Button btnPrint;

    private static final int TEST_START = 0;
    private static final int TEST_END = 2;
    private static final int COMPARE_START = 1;
    private static final int COMPARE_END = 7;

    private static final int OPEN_CLOSE_LIB = 3;
    private static final int ENTER_OUT_CARD = 4;
    private static final int GET_TEMP = 5;
    private CountDownLatch mCountDownLatch;
    private float ac1;
    private float Ac;
    private Button btnCompare;
    private boolean isComparing;
    private boolean isTesting = false;
    private TextView tvCompareValue;
    private float[] as1List;
    private float[] as2List;
    private List<Double> AsList;
    private double COMPARE_FACTOR = 0.01;
    private SharedPreferences sp;
    private TextView tv_status;
    private TextView tv_alis;
    private Button btnUpload;
    private RecyclerView rv_data;
    private TestAdapter testAdapter;
    private List<CheckResult> resultList = new ArrayList<>();
    private long countDownDelay = 6500;//倒计时结束后延时处理
    private List<Project> projects = new ArrayList<>();
    private Spinner spn_project;
    private Project mProject;
    private TextView tv_yzl;

    private TextView tv_hint, tv_card_number;
    private LinearLayout ll_card_number;
    private CheckPresenter checkPresenter;
    private StatusDialog statusDialog;
    private String card_number;
    private int card_number_i;
    QRCodeBroadcastReceiver qrCodeBroadcastReceiver;
    private TextView tv_project;

    private boolean isOneStart = true;
    // 定义三个数组，存储扫描试剂盒后，后台返回的检测项目，试剂盒编号和剩余可用次数三个数据
    private List list_project_name = new ArrayList();
    private List list_card_qrcode = new ArrayList();
    private List list_using_times = new ArrayList();


    String check_company = com.example.utils.http.Global.Dept + "";
    String check_persion = com.example.utils.http.Global.NAME + "";


    // 分光光度页面的切换模式标志位
    public static int change_method = 1;

    private Button btn_changemethod;

    // 限量标准
    private String limit_standard = "";

    // 临界值
    private String critical_value = "";

    // 检测项目
    private String jiance_xiangmu = "";

    private DbUtils db;

    // 设置数组进行手动和自动录入的判断
    private Boolean[] mode_statu_list = new Boolean[]{true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true};

    public static String sampleId;//样品总分类Id
    public static String sampleName;//样品总分类名称
    public static String subSampleId;//样品总分类名称


    @Override
    protected void onStop() {
        super.onStop();
        if (qrCodeBroadcastReceiver != null) {
            try {
                unregisterReceiver(qrCodeBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registerReceiver() {
        qrCodeBroadcastReceiver = new QRCodeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QRCode.QRCODE_LOCAL_MSG_SAMPLE);
        intentFilter.addAction(QRCode.QRCODE_LOCAL_MSG_CARD);
        registerReceiver(qrCodeBroadcastReceiver, intentFilter);
    }

    private int clickPosition = -1;

    @Override
    public void onShowSampleDialog(int position) {
        clickPosition = position;
        if (com.example.utils.http.Global.isVoluntarily && !com.example.utils.http.Global.ismixedentry) {
            statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
            statusDialog.show();
        } else if (com.example.utils.http.Global.ismixedentry && change_method % 2 == 1) {
            statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
            statusDialog.show();
        }

//        if(com.example.utils.http.Global.isVoluntarily){
//            statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
//            statusDialog.show();
//        }
//        statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
//        statusDialog.show();

    }

    public class QRCodeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            APPUtils.showToast(PesticideTestActivity2.this, intent.getStringExtra(QRCode.QRCODE_LOCAL_MSG));
            mHandler.sendMessage(Message.obtain(mHandler, 111, intent.getStringExtra(QRCode.QRCODE_LOCAL_MSG)));
        }
    }

    private LocationReceiver locationReceiver;

    private String user = "";
    private String terrace = "";
    private String DeptId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticide_test2);
        GT.WindowUtils.hideActionBar(this);

        // 百度定位
        Intent intent = new Intent("LocationAction");
        intent.putExtra("locationMessage", "开始定位");
        sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新

        // 注册广播
        locationReceiver = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SendMessage");
        registerReceiver(locationReceiver, intentFilter); //注册广播


        checkPresenter = new CheckPresenter();
        registerReceiver();
        statusDialog = new StatusDialog(this, this);
        statusDialog.setOnProgressStatus(this);
        initView();

        receivedata();

        sp = getSharedPreferences("userPass", MODE_PRIVATE);

        if (!com.example.utils.http.Global.isAdimin) {
            user = sp.getString("user", "");
            terrace = sp.getString("terrace", "");
            DeptId = sp.getString("DeptId", "");
        }

        if (list_card_qrcode.size() > 0 && list_project_name.size() > 0 && list_using_times.size() > 0) {
            String card_qrcode = list_card_qrcode.get(list_card_qrcode.size() - 1).toString();
            String project_name = list_project_name.get(list_project_name.size() - 1).toString();
            String using_times = list_using_times.get(list_using_times.size() - 1).toString();

            tv_project.setText(project_name);
            tv_card_number.setText(card_qrcode);
            tv_hint.setText(using_times);
            findViewById(R.id.tv_projectName).setVisibility(View.GONE);
        }
        act = this;
//        tempTimer = new Timer();
//        tempTimer.schedule(tempTask, 0, 10 * 1000);
        initSp();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //如果是农药残留，就直接不给选择
        if (!com.example.utils.http.Global.isAdimin) {
            spn_project.setVisibility(View.GONE);
            if (!"".equals(qrcode) && qrcode != null) {
                findViewById(R.id.tv_projectName).setVisibility(View.GONE);
            } else {
//                findViewById(R.id.tv_projectName).setVisibility(View.VISIBLE);
                openLight(410);
            }

        } else {
            spn_project.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_projectName).setVisibility(View.GONE);
        }


    }


    // 接收定位成功发送消息
    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                LocationX = intent.getStringExtra("LocationX");
                LocationY = intent.getStringExtra("LocationY");
                LocationAddress = intent.getStringExtra("LocationAddress");

                if (!"".equals(LocationX) && !"".equals(LocationY) && !"".equals(LocationAddress)) {
                } else {
                    // 配置定位信息
//                    set_dingwei_info();
                    Toast.makeText(PesticideTestActivity2.this, "定位失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    @SuppressLint("SetTextI18n")
    private void initSp() {
        //初始化
//        tvCompareValue.setText("对照值:0.000");
//                    btnTest.setEnabled(false);
        //对照值保存在本地
        sp = getSharedPreferences(SPResource.FILE_NAME, MODE_PRIVATE);
        Ac = sp.getFloat(SPResource.KEY_COMPARE_VALUE, 0);
        if (Ac <= COMPARE_MIN_VALUE) {
            tvCompareValue.setText(getResources().getString(R.string.contrastValue));
        } else {
            tvCompareValue.setText(getResources().getString(R.string.contrastValue) + df.format(Ac));
            if (Ac >= COMPARE_MIN_VALUE)
                btnTest.setEnabled(true);
        }

    }


    private void initView() {
        projects = new Project().findAll();
        Log.d(TAG, "projects=" + projects.size());
        tv_status = (TextView) findViewById(R.id.tv_status);
        tvCompareValue = (TextView) findViewById(R.id.tv_compare_value);
        btnPrint = (Button) findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);
        cbSelectAll = (ImageView) findViewById(R.id.cb_select_all);
        tv_project = (TextView) findViewById(R.id.tv_project);
        llSelectAll = (LinearLayout) findViewById(R.id.ll_select_all);
        tv_alis = (TextView) findViewById(R.id.tv_alis);
        tv_yzl = (TextView) findViewById(R.id.tv_yzl);
        llSelectAll.setOnClickListener(this);
        tv_alis.setOnClickListener(this);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnCompare = (Button) findViewById(R.id.btn_compare);
        btnCompare.setOnClickListener(this);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        btn_changemethod = (Button) findViewById(R.id.btn_changemethod);


        btn_changemethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_method++;
                if (change_method % 2 == 0) {
                    APPUtils.showToast(PesticideTestActivity2.this, "手动录入模式");
                } else {
                    APPUtils.showToast(PesticideTestActivity2.this, "自动录入模式");
                }
            }
        });
        btnUpload.setOnClickListener(this);
        rv_data = (RecyclerView) findViewById(R.id.rv_data);

        tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_card_number = (TextView) findViewById(R.id.tv_card_number);
        ll_card_number = (LinearLayout) findViewById(R.id.ll_card_number);
        tv_hint.setOnClickListener(this);
        tv_card_number.setOnClickListener(this);
        ll_card_number.setOnClickListener(this);
        spn_project = (Spinner) findViewById(R.id.spn_project);
        if (com.example.utils.http.Global.isAdimin) {
            if (projects != null && !projects.isEmpty()) {
                ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(this, R.layout.item_select_project, R.id.tv_project_name, projects);
                adapter.setDropDownViewResource(R.layout.item_select_project_drop);
                spn_project.setAdapter(adapter);
                spn_project.setSelection(projects.size() - 1);
                mProject = projects.get(projects.size() - 1);
                spn_project.setOnItemSelectedListener(this);
            }
            openLight(mProject.bochang);

            // 波长

            spn_project.setVisibility(View.VISIBLE);
            tv_project.setVisibility(View.GONE);
        } else {
            spn_project.setVisibility(View.GONE);
            tv_project.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        testAdapter = new TestAdapter(this);
        testAdapter.setOnAllSelectListener(new TestAdapter.OnAllSelectListener() {
            @Override
            public void onAllSelect(boolean isAllSelect) {
                tv_alis.setSelected(isAllSelect);
            }
        });
        rv_data.setLayoutManager(manager);
        rv_data.setAdapter(testAdapter);
        testAdapter.setOnShowSampleDialog(this);

        //修改通道号
        int stateIndex = 0;
        String[] arrayState = {"A", "B", "C", "D"};
        int idnex = 1;

        for (int i = 0; i < Global.CHANNEL_COUNT; i++) {
            CheckResult checkResult = new CheckResult();

            if (i == 5) {
                stateIndex = 1;
            } else if (i == 10) {
                stateIndex = 2;
            } else if (i == 15) {
                stateIndex = 3;
            }
            checkResult.channel = arrayState[stateIndex] + idnex++;

            if (idnex > 5) {
                idnex = 1;
            }

            resultList.add(checkResult);
        }
        testAdapter.setData(resultList);


        if (Global.uploadModel == 1) {
            btnUpload.setVisibility(View.GONE);
        }
        btnReturn = (Button) findViewById(R.id.btn_return);

        btnTest.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        if (com.example.utils.http.Global.ismixedentry) {
            btn_changemethod.setVisibility(View.VISIBLE);
        } else {
            btn_changemethod.setVisibility(View.GONE);
        }

//        tvCurrentTemp = findViewById(R.id.tv_current_temp);
    }

    private void openLight(int bochang) {
        SerialUtils.COM3_SendData(("Light" + bochang).getBytes());
    }

    Random random = new Random();

    //region 定时刷新温度任务
//    TimerTask tempTask = new TimerTask() {
//        @Override
//        public void run() {
//            sendData(GET_TEMP);
//        }
//    };
    //endregion

    public double nextDouble(final double min, final double max) throws Exception {
        if (max < min) {
            throw new Exception("min > max");
        }
        if (min == max) {
            return min;
        }
        return min + ((max - min) * random.nextDouble());
    }

    private int reactionTime = Global.cardWarmTime;

    @Override
    public void onClick(View v) {

        int i = v.getId();//全选
        if (i == R.id.btn_test) {
            if (card_number == null || card_number.isEmpty()) {
                APPUtils.showToast(this, "检测卡编号未输入");
                return;
            }
            if (remainTimes < 1 || remainTimes < testAdapter.getSelectedCount()) {
                APPUtils.showToast(this, "检测卡编号可用次数不足" + testAdapter.getSelectedCount() + "次");
                return;
            }
            if (isNc()) {
                if (Ac < COMPARE_MIN_VALUE) {
                    APPUtils.showToast(this, "请先对照");
                    return;
                }
            }
            if (isTesting || isComparing) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("正在检测，请等待").create().show();
                return;
            }
            test();
        } else if (i == R.id.tv_alis) {
            if (isTesting || isComparing) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("正在检测，请等待").create().show();
                return;
            }
            tv_alis.setSelected(!tv_alis.isSelected());
            testAdapter.setAllSelect(tv_alis.isSelected());
        } else if (i == R.id.btn_return) {
            onBackPressed();
        } else if (i == R.id.btn_print) {
            if (isTesting || isComparing) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("正在检测，请等待").create().show();
                return;
            }

            print();
        } else if (i == R.id.btn_compare) {//                Ac = 20;
//                saveAc2Sp();
            if (isTesting || isComparing) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("正在检测，请等待").create().show();
                return;
            }

            compare();
        } else if (i == R.id.btn_upload) {
            if (isTesting || isComparing) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("正在检测，请等待").create().show();
                return;
            }

            upload(false);
        } else if (i == R.id.ll_select_all) {
            if (isTesting || isComparing) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("正在检测，请等待").create().show();
                return;
            }
            cbSelectAll.setSelected(!cbSelectAll.isSelected());
            testAdapter.setAllSelect(cbSelectAll.isSelected());
        } else if (i == R.id.tv_hint) {
            changeCardNumber();
        } else if (i == R.id.tv_card_number) {
            changeCardNumber();
        } else if (i == R.id.ll_card_number) {
            changeCardNumber();
        }
    }

    //单击文本框 请求扫描二维码
    private void changeCardNumber() {
//        checkPresenter.GetCardQRInfo(new GetQRInfoBean(card_number));
        statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_CARD);
        statusDialog.show();
    }

    private void upload(boolean autoUpload) {

    }

    private void updateUploadState2Db(List<CheckResult> list) {

        new CheckResult().updateAll(list, new String[]{"uploadId"});
    }

    private int compareChannelIndex = 0;

    private void compare() {
        if (testAdapter.getSelectedCount() == 0
                || testAdapter.getSelectedCount() > 1) {
            APPUtils.showToast(this, "请选择一个通道进行对照");
            return;
        }
        compareChannelIndex = testAdapter.getSelectedArray()[0] - 1;
        Log.d(TAG, "compareChannelIndex:" + compareChannelIndex);
        if (!validateBusyOrNot()) {
            return;
        }
        isComparing = true;
//        reactionTime = Global.cardWarmTime * 60;//new
//        if (Global.DEBUG) {
//            reactionTime = Global.cardWarmTime;
//        }

        startReaction();
    }


    private void print() {
        if (resultList == null || resultList.isEmpty()) {
            APPUtils.showToast(act, "请先检测");
            return;
        }

        List<CheckResult> printList = testAdapter.getSelectedList();

        if (printList == null || printList.size() == 0) {
            APPUtils.showToast(act, "请先选择数据");
        } else {
            byte[] data = ToolUtils.assemblePrintCheck(printList, this);
            Log.d(TAG, "data:" + new String(data));
            if (SerialUtils.COM4_SendData(data)) {
                APPUtils.showToast(act, "打印数据发送成功");
            } else {
                APPUtils.showToast(act, "打印失败");
            }
        }

    }

    //region 测试专用
    private void t1() {

        int selectedCount = testAdapter.getSelectedCount();
        if (selectedCount == 0) {
            APPUtils.showToast(this, "请勾选检测通道");
            return;
        }
        selectedChannels = new int[selectedCount];

        String result = "205,155,55|255,255,255|255,255,255|255,255,255|5,255,25|255,255,255|5,25,155|255,255,255|255,255,255|255,255,255|255,255,255|255,255,255";
        String[] st = result.split("\\|");
        if (st.length != Global.CHANNEL_COUNT) {
            mHandler.sendEmptyMessage(ToolUtils.test_fail);
            return;
        }

        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < st.length; i++) {
            String[] s = st[i].split(",");
            if (s.length != 3) {
                mHandler.sendEmptyMessage(ToolUtils.test_fail);
                return;
            }
            list.add(s);
        }

        List<Integer> intList = new ArrayList<>();
        try {
            int sum;
            int index = 0;
            for (int j = 0; j < list.size(); j++) {
                sum = 0;
                if (index == selectedChannels.length) break;
                if (selectedChannels[index] == j + 1) {
                    index++;
                    for (int i = 0; i < list.get(j).length; i++) {
                        sum += Integer.parseInt(list.get(j)[i]);
                    }
                    intList.add(sum);
                }
            }
            mHandler.obtainMessage(ToolUtils.test_success, intList).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(ToolUtils.test_fail);
            return;
        }
    }
    //endregion

    private void showOperateDialog() {

        View view = View.inflate(this, R.layout.dialog_operation_help, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
//        view.findViewById(R.id.btn_ok).setOnClickListener((v) -> dialog.dismiss());
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 600));
    }

    private boolean validateBusyOrNot() {
        if (isTesting) {
            APPUtils.showToast(this, "正在检测...");
            return false;
        }
        if (isComparing) {
            APPUtils.showToast(this, "正在对照...");
            return false;
        }
        return true;
    }

    int[] selectedChannels = null;

    private void test() {

        if (testAdapter == null) return;

//        if (!validateBusyOrNot() || !validateCommonDataIsComplete() || !validateDataIsComplete())
//            return;

        int selectedCount = testAdapter.getSelectedCount();
        if (selectedCount == 0) {
            APPUtils.showToast(this, "请勾选检测通道");
            return;
        } else if (!testAdapter.verification()) {
            return;
        }

        isTesting = true;
        selectedChannels = new int[selectedCount];
        selectedChannels = testAdapter.getSelectedArray();
//        reactionTime = Global.cardWarmTime;//new
//        if (Global.DEBUG) {
//            reactionTime = Global.cardWarmTime;
//        }
        startReaction();
    }


    Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ToolUtils.test_fail:
                    failHandle();
                    break;
                case ToolUtils.update_countdown:
                    int i = (int) msg.obj;
                    tv_status.setText("检测中: " + i + "s");
                    break;
                case ToolUtils.test_success:
                    isTesting = false;
                    showTestResult();
//                    if (Global.uploadModel == 1) {
//                        upload(true);
//                    }
                    tv_status.setText("检测成功");
                    cbSelectAll.setSelected(false);
//                    spn_project.setEnabled(true);
                    break;
                case ToolUtils.testing:
                    tv_status.setText("检测中...");
                    break;
                case ToolUtils.compare_fail:
                    failHandle();
                    break;
                case ToolUtils.compare_success:
                    isComparing = false;
//                    spn_project.setEnabled(true);

                    if (isNc()) {

                        //是否打开测试
                        /*if (InterfaceURL.isOpenTest) {
                            if (Ac < COMPARE_MIN_VALUE) {
                                if (InterfaceURL.isOpenTest) {
                                    // TODO 测试专用
                                    Ac = 80f;
                                }
                            }
                        }*/

                        if (Ac < COMPARE_MIN_VALUE) {
//                            APPUtils.showToast(PesticideTestActivity2.this,"ac="+Ac);
                            tv_status.setText("对照失败,请重新对照");
                            showFailedHintDialog();
                        } else {
                            tv_status.setText("对照成功");
                            tvCompareValue.setText(getResources().getString(R.string.contrastValue) + df.format(Ac));
                            btnTest.setEnabled(true);
                            App.defaultSP.edit().putString("pes", tvCompareValue.getText().toString()).commit();
//                            Preferences.getInstance().commit(PesticideTestActivity2.this, "pes", "compareValue", tvCompareValue.getText().toString());
                            Log.i("lcy", "handleMessage: --对照成功1----" + tvCompareValue.getText().toString());
                        }
                    } else {
                        tv_status.setText("对照成功");
//                        Preferences.getInstance().commit(PesticideTestActivity2.this, "pes", "compareValue", tvCompareValue.getText().toString());
                        App.defaultSP.edit().putString("pes", tvCompareValue.getText().toString()).commit();

                        Log.i("lcy", "handleMessage: ----对照成功-2-" + tvCompareValue.getText().toString());
//                            tvCompareValue.setText(getResources().
//                                    getString(R.string.contrastValue) +
//                                    df.format(Ac * COMPARE_FACTOR));
                        btnTest.setEnabled(true);
                    }
                    break;
                case 111:
                    scanParameterSuccess((String) msg.obj);
                    msg.obj = "";
                    break;
            }
        }
    };
    String sampleCode;
    List sampleCodelist = new ArrayList();

    //扫描二维码后，点击对话框上的确定按钮后执行的方法
    private void scanParameterSuccess(final String temp) {

        if (temp == null || temp.contains("OK2")) {
            PesticideTestActivity2.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PesticideTestActivity2.this, "当前网络不佳，请从新扫描。", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        // todo 修改
        findViewById(R.id.tv_projectName).setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String temp2 = temp.replaceAll("\r", "");
                temp2 = temp2.replaceAll("\n", "");
                if (statusDialog != null) {

                    if (statusDialog.getStatus() == StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE
                            || statusDialog.getStatus() == StatusDialog.STATUS_END_SCAN_QRCODE_SAMPLE
                            || statusDialog.getStatus() == StatusDialog.STATUS_START_VERITY_QRCODE_SAMPLE
                            || statusDialog.getStatus() == StatusDialog.STATUS_END_VERITY_QRCODE_SAMPLE) {


                        sampleCode = temp2;
                        sampleCodelist.add(sampleCode);
                        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_SAMPLE);
                        checkPresenter.GetSamplingInfo(new GetSamplingInfoBean(temp, com.example.utils.http.Global.admin_pt), clickPosition, PesticideTestActivity2.this);
                        statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_SAMPLE);
//                        et_Sample_Num.setText(temp);
                    } else if (statusDialog.getStatus() == StatusDialog.STATUS_START_SCAN_QRCODE_CARD
                            || statusDialog.getStatus() == StatusDialog.STATUS_END_SCAN_QRCODE_CARD
                            || statusDialog.getStatus() == StatusDialog.STATUS_START_VERITY_QRCODE_CARD
                            || statusDialog.getStatus() == StatusDialog.STATUS_END_VERITY_QRCODE_CARD) {

                        card_number = temp2;

                        //
                        list_card_qrcode.add(temp2);

                        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_CARD);
                        checkPresenter.GetCardQRInfo(new GetQRInfoBean(temp2), 1, PesticideTestActivity2.this);//扫描二维码，向后台请求数据
                        statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_CARD);
                    }
                }

            }
        });
    }

    AlertDialog failedHintDialog;

    /**
     *
     */
    private void showFailedHintDialog() {
        if (failedHintDialog == null) {
            failedHintDialog = new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("对照失败，请重新对照")
                    .create();
        }
        failedHintDialog.show();
    }

    /**
     * 恢复倒计时
     */
    public void countDownClear() {

        if (isNc()) {//現在是否是检测农药残留項目
            reactionTime = Global.cardWarmTime;
        } else {
            reactionTime = 3;
        }

    }

    List<CheckResult> savaDatas;

    //2019.6.14 before
    private void showTestResult() {

        //修改通道号
        int stateIndex = 0;
        String[] arrayState = {"A", "B", "C", "D"};
        int idnex = 1;

        savaDatas = new ArrayList<>();
        int index = 0;
//        double factor = 50.0 / Global.project.cardXlz;
        List<CheckResult> crs = testAdapter.getData();
        for (int i = 0; i < Global.CHANNEL_COUNT; i++) {
            if (selectedChannels.length > index && selectedChannels[index] == i + 1) {
                double xlz = getXLZ(i);//限量标准值

                CheckResult cr = crs.get(i);
                if (isNc()) {
                    xlz = 50;
                    double value = AsList.get(i) * 100;
                    value = value > 100 ? 100 : value;
                    value = value < 0 ? 0 : value;
                    value = Double.parseDouble(df.format(value));

//                    APPUtils.showToast(this, "value=" + value + " xlz=" + xlz);
                    Log.d(TAG, "i=" + i + " value=" + value + " xlz=" + xlz);

                    if (i == 5) {
                        stateIndex = 1;
                    } else if (i == 10) {
                        stateIndex = 2;
                    } else if (i == 15) {
                        stateIndex = 3;
                    }

                    Timber.i("限量值：" + xlz);
                    CheckResult tempResult = new CheckResult(
                            com.example.utils.http.Global.Dept == null ? "无" : com.example.utils.http.Global.Dept,//检测单位
                            cr.bcheckedOrganization,//被检测单位
                            mProject.projectName,//检测项目
                            cr.sampleNum,//样本编号
                            cr.sampleName,//样本名称
                            cr.sampleSource,
                            arrayState[stateIndex] + idnex++,//通道
                            System.currentTimeMillis(),//检测时间
                            value + "%",//抑制率
                            value <= xlz ? "合格" : "不合格",//检测结果
                            xlz + "%",//限量值
                            "",
                            com.example.utils.http.Global.NAME == null ? "无" : com.example.utils.http.Global.NAME,//用户名
                            cr.weight,//重量
                            new Random().nextInt(100000) + "",
                            AsList.get(i) + "",
                            cr.companyCode);//组织机构


                    // 修改
                    if (!mode_statu_list[i]) {
                        tempResult.status = false;
                    }
                    tempResult.sampleType = cr.sampleType;

                    tempResult.isSelected = cr.isSelected;
                    Log.d(TAG, "i=" + i + "sn=" + cr.sn);
                    tempResult.sn = cr.sn;

                    if (idnex > 5) {
                        idnex = 1;
                    }


                    resultList.set(i, tempResult);
                    savaDatas.add(tempResult);
                    index++;
                } else {
//                    float logresult = Math.abs(log((ac1/as1List[i]), 10));

                    /*float logresult = 0;
                    if(as1List[i] > ac1){
                        logresult = (float) Math.log10(as1List[i]) - (float) Math.log10(ac1);
                    }else{
                        logresult = (float) Math.log10(ac1) - (float) Math.log10(as1List[i]);
                    }*/


                    //修改算法
                    Timber.i(" mProject.k:" + mProject.k);
                    Timber.i(" mProject.b:" + mProject.b);

                    double value1 = mProject.k * ((float) Math.log10(d[i])) + mProject.b;
                    double value2 = mProject.k * ((float) Math.log10(as1List[i])) + mProject.b;
                    Timber.i("value1:" + value1);
                    Timber.i("value2:" + value2);

                    BigDecimal b1 = new BigDecimal(value1);
                    BigDecimal b2 = new BigDecimal(value2);

                    Timber.i("b1:" + b1);
                    Timber.i("b2:" + b2);
                    value1 = b1.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    value2 = b2.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Timber.i("value1:" + value1);
                    Timber.i("value2:" + value2);
                    double value = 0;
                    float logresult = 0;

                    if (value1 != value2) {
                        logresult = (float) Math.log10(d[i]) - (float) Math.log10(as1List[i]);
                        value = mProject.k * (logresult) + mProject.b;
                    }

                    Timber.i("value:" + value);
                    value = Double.parseDouble(df.format(value));
                    double jcx = mProject.cardXlz;//检出限

                    /*if (value < jcx) {
                        value = 0;
                    }*/
                    if (value < 0) {
                        value = 0;
                    }

                    if (i == 5) {
                        stateIndex = 1;
                    } else if (i == 10) {
                        stateIndex = 2;
                    } else if (i == 15) {
                        stateIndex = 3;
                    }

                    Timber.i("限量值：" + xlz);

                    //开始保存数据
                    CheckResult tempResult = new CheckResult(
                            com.example.utils.http.Global.Dept == null ? "无" : com.example.utils.http.Global.Dept,
                            cr.bcheckedOrganization,
                            mProject.projectName,
                            cr.sampleNum,
                            cr.sampleName,
                            cr.sampleSource,
                            arrayState[stateIndex] + idnex++,
                            System.currentTimeMillis(),
                            value == 0 ? "0" : df.format(value) + "",
                            value <= xlz ? "合格" : "不合格",
                            df.format(xlz) + "",
                            "",
                            com.example.utils.http.Global.NAME == null ? "无" : com.example.utils.http.Global.NAME,
                            cr.weight, new Random().nextInt(100000) + "", logresult + "", companyCode);
                    tempResult.isSelected = cr.isSelected;
                    tempResult.sn = cr.sn;
                    tempResult.checker = check_persion;
                    tempResult.checkedOrganization = check_company;

                    // 修改
                    if (com.example.utils.http.Global.isAdimin) {
                        tempResult.checkedOrganization = "";
                    }

                    // 修改
                    if (!mode_statu_list[i]) {
                        tempResult.status = false;
                    }

                    tempResult.sampleType = cr.sampleType;

                    if (idnex > 5) {
                        idnex = 1;
                    }

                    resultList.set(i, tempResult);
                    savaDatas.add(tempResult);

                    //
//                    saveNewTable1(tempResult);
//                    }


                    index++;
                }
                if (!com.example.utils.http.Global.isAdimin) {
//                    uploadResult(i);
                }
            } else {

                if (i == 5) {
                    stateIndex = 1;
                } else if (i == 10) {
                    stateIndex = 2;
                } else if (i == 15) {
                    stateIndex = 3;
                }

                CheckResult cr = new CheckResult();
                cr.channel = arrayState[stateIndex] + idnex++;

                if (idnex > 5) {
                    idnex = 1;
                }

                resultList.set(i, cr);
            }

        }
        Log.d(TAG, "resultList=" + resultList.toString());
        testAdapter.setData(resultList);
        boolean isSuccess = new CheckResult().saveBindingIdAll(savaDatas);

        //保存新表
        saveNewTable(savaDatas);
        Log.i("lcy", "showTestResult: -11-----" + savaDatas.toString());

    }


    /**
     * 保存新表
     *
     * @param savaDatas
     */

    //第一步记录一个下标 每次上传成功之后 加一 然后自己调用自己
    int currentIndex = 0;


    private void saveNewTable(List<CheckResult> savaDatas) {
        currentIndex = 0;
        upDateShuju();
    }


    public void upDateShuju() {
        int[] SelectArray = testAdapter.getSelectedArray();
        for (int k = 0; k < SelectArray.length; k++) {
            SamplNumlist.add(sampleidlist[SelectArray[k] - 1]);
            yplblist.add(yplbIdlist[SelectArray[k] - 1]);
            typelist.add(sampletypelist[SelectArray[k] - 1]);
            locId.add(locListId[SelectArray[k] - 1]);
            locsp1Id.add(locSp1Id[SelectArray[k] - 1]);
            locsp2Id.add(locSp2Id[SelectArray[k] - 1]);
            locsp3Id.add(locSp3Id[SelectArray[k] - 1]);
            subProductId.add(sublistProductId[SelectArray[k] - 1]);

        }
        if (savaDatas == null || savaDatas.size() == 0) return;
        if (currentIndex < savaDatas.size()) {
            //上传数据
            detectionResultBean = new DetectionResultBean();
            CheckResult checkResult = savaDatas.get(currentIndex);
            detectionResultBean.setSQLType("分光光度");//设置当前数据的模块名称(防止以后需求改动的需要)
            detectionResultBean.setNumberSamples(checkResult.sampleNum);//样品编号
            detectionResultBean.setDetectionTime(checkResult.testTime);//检测时间
            detectionResultBean.setAisle(checkResult.channel);//通道
            detectionResultBean.setSampleName(checkResult.sampleName);//样品名称
            detectionResultBean.setDetectionValue(checkResult.testValue);//抑制率/检测值
            detectionResultBean.setDetectionResult(checkResult.resultJudge);//检测结果
            detectionResultBean.setUnitsUnderInspection(checkResult.bcheckedOrganization);//被检测单位
            detectionResultBean.setInspector(checkResult.checker);//检测人员
            detectionResultBean.setDetectionCompany(checkResult.checkedOrganization);//检测单位
            detectionResultBean.setWeight(checkResult.weight);//重量
            detectionResultBean.setCommodityPlaceOrigin(checkResult.sampleSource);//商品来源
            detectionResultBean.setUploadStatus("未上传");
            detectionResultBean.setSpecimenType(checkResult.sampleType); // 样品类型
            detectionResultBean.setLimitStandard(limit_standard); // 限量标准
            detectionResultBean.setCriticalValue(critical_value); // 临界值
            detectionResultBean.setTestItem(jiance_xiangmu); // 检测项目
            detectionResultBean.setQRCode(card_number); // 试剂盒二维码字符串
            detectionResultBean.setOperatorId(com.example.utils.http.Global.ID); // 上传数据的OperatorId参数
            detectionResultBean.setAreaId(terrace); // 上传数据的AreaId参数
            detectionResultBean.setDeptId(DeptId); // 上传数据的DeptId参数
            detectionResultBean.setXgd(checkResult.xgd);//吸光度
            detectionResultBean.companyCode = checkResult.companyCode; //组织代码
            if (locsp1Id.get(currentIndex) == null) {
                detectionResultBean.setSpId1("");
            } else {
                detectionResultBean.setSpId1(locsp1Id.get(currentIndex).toString());//省
            }
            if (locsp2Id.get(currentIndex) == null) {
                detectionResultBean.setSpId2("");
            } else {
                detectionResultBean.setSpId2(locsp2Id.get(currentIndex).toString());
            }
            if (locsp3Id.get(currentIndex) == null) {
                detectionResultBean.setSpId3("");
            } else {
                detectionResultBean.setSpId3(locsp3Id.get(currentIndex).toString());
            }

            detectionResultBean.setSampleId(sampleId);//总分类
            if (subProductId.get(currentIndex) == null) {
                detectionResultBean.setYplbId("");
            } else {
                detectionResultBean.setYplbId(subProductId.get(currentIndex).toString());
            }
            if (yplblist.get(currentIndex) == null) {
                detectionResultBean.setObjectId("");
            } else {
                detectionResultBean.setObjectId(yplblist.get(currentIndex).toString());

            }
            Log.d("zdl", "===开始上传===" + currentIndex);
            Log.d("zdl", "start=====:" + detectionResultBean.toString());
            if (!com.example.utils.http.Global.isAdimin) {
                //走的这个
                uploadResult(currentIndex);
            } else {
                MainActivity.hibernate.save(detectionResultBean);
            }
        } else {
            Toast.makeText(act, "数据上传完成", Toast.LENGTH_LONG).show();
            MessageDialog.show("提示", "数据上传完成" + errMsg, "确定");
        }
    }

    private DetectionResultBean detectionResultBean;


    private void saveNewTable1(CheckResult checkResult) {
        detectionResultBean = new DetectionResultBean();
        detectionResultBean.setSQLType("分光光度");//设置当前数据的模块名称(防止以后需求改动的需要)
        detectionResultBean.setNumberSamples(checkResult.sampleNum);//样品编号
        detectionResultBean.setDetectionTime(checkResult.testTime);//检测时间
        detectionResultBean.setAisle(checkResult.channel);//通道
        detectionResultBean.setSampleName(checkResult.sampleName);//样品名称
        detectionResultBean.setDetectionValue(checkResult.testValue);//抑制率/检测值
        detectionResultBean.setDetectionResult(checkResult.resultJudge);//检测结果
        detectionResultBean.setUnitsUnderInspection(checkResult.bcheckedOrganization);//被检测单位
        detectionResultBean.setInspector(checkResult.checker);//检测人员
        detectionResultBean.setDetectionCompany(checkResult.checkedOrganization);//检测单位
        detectionResultBean.setWeight(checkResult.weight);//重量
        detectionResultBean.setCommodityPlaceOrigin(checkResult.sampleSource);//商品来源
//            detectionResultBean.setUploadStatus("未上传");//上传状态
//            detectionResultBean.setUploadStatus("已上传");//上传状态
        detectionResultBean.setUploadStatus("未上传");
//            detectionResultBean.setUploadStatus(upload_status == 1 ? "已上传" : "未上传");//上传状态
        detectionResultBean.setSampleId(sampleId);//样品总分类Id
        detectionResultBean.setSpecimenType(checkResult.sampleType); // 样品类型
        detectionResultBean.setLimitStandard(limit_standard); // 限量标准
        detectionResultBean.setCriticalValue(critical_value); // 临界值
        detectionResultBean.setTestItem(jiance_xiangmu); // 检测项目
        detectionResultBean.setQRCode(card_number); // 试剂盒二维码字符串

//        MainActivity.hibernate.save(detectionResultBean);
    }


    private void uploadResult(int position) {
        CheckResult checkResult = savaDatas.get(position);
        if (checkResult.status) {//true
            uploadResult2(position);
            checkResult.uploadId = 1;
        } else { //false
            //走的手动
            uploadResult1(position);
            checkResult.uploadId = 1;

        }

    }

    public static float log(float value, float base) {
        return (float) ((float) Math.log(value) / (float) Math.log(base));
    }

    private double getXLZ(int index) {
        double jcx = 0;
        Log.d(TAG, "getXLZ index=" + index);
        List<CheckResult> crs = testAdapter.getData();
        CheckResult checkResult = crs.get(index);
        jcx = mProject.cardXlz;
        return jcx;
    }

    protected boolean validateCommonDataIsComplete() {

        if (TextUtils.isEmpty(((CheckResult) testAdapter.getData()
                .get(compareChannelIndex)).bcheckedOrganization)) {
            APPUtils.showToast(act, "被检单位");
            return false;
        }
        if (TextUtils.isEmpty(((CheckResult) testAdapter.getData()
                .get(compareChannelIndex)).sampleSource)) {
            APPUtils.showToast(act, "请输入商品来源");
            return false;
        }
        return true;
    }

    private void failHandle() {
//        spn_project.setEnabled(true);
        if (isTesting) {
            isTesting = false;
            cancelTimer();
            tv_status.setText("检测失败");
        } else {
            isComparing = false;
            cancelTimer();
            tv_status.setText("对照失败");
            showFailedHintDialog();
        }
    }

    private void startCountDown() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        timerTask = new BSCountDownTask();
        mTimer = new Timer();
        //curDownTime = Global.cardWarmTime * 60;//old
        curDownTime = Global.cardReactionTime;//new
        if (Global.DEBUG) {
            curDownTime = Global.cardReactionTime;
        }
//
        mTimer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    boolean isFinishing = false;

    @Override
    public void onBackPressed() {
        if (isTesting || isComparing) {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("正在检测，是否终止检测?")
                    .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isFinishing = true;
                            dialog.dismiss();
                            countDownLatch();
                            finish();
                        }
                    }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            countDownLatch();

            finish();
        }
    }

    @Override
    protected void onDestroy() {

        // 注销定位监听
        try {
            if (locationReceiver != null) {
                unregisterReceiver(locationReceiver);
            }
        } catch (Exception e) {
        }


        if (mTimer != null) mTimer.cancel();
        if (reactionTimer != null) reactionTimer.cancel();
        if (tempTimer != null) tempTimer.cancel();
        if (timerTask != null) timerTask.cancel();
        countDownLatch();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void cancelTimer() {

        if (mTimer != null) {
            mTimer.cancel();
        }
        if (reactionTimer != null) {
            reactionTimer.cancel();
        }
    }

    Timer mTimer = null;

    int curDownTime = 0;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_in_out_card) {
            if (validateBusyOrNot()) {
                new Thread() {
                    @Override
                    public void run() {
                        sendData(ENTER_OUT_CARD);
                    }
                }.start();
            }
        } else if (i == R.id.action_open_close_lib) {
            if (validateBusyOrNot()) {
                new Thread() {
                    @Override
                    public void run() {
                        sendData(OPEN_CLOSE_LIB);
                    }
                }.start();
            }
        } else if (i == R.id.action_operate_procedure) {
            showOperateDialog();
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        mProject = projects.get(position);
        //是否是农残
        if (isNc()) {
            tv_yzl.setText("抑制率");
//            tv_yzl.setText("吸光度");
            tvCompareValue.setVisibility(View.VISIBLE);
            btnCompare.setText("对照");

            initSp();
        } else {
            Ac = 0;
            tv_yzl.setText("检测值");
//            APPUtils.showToast(this, "请先进行调零");
            tvCompareValue.setVisibility(View.GONE);
//            btnCompare.setText("调零");
            btnCompare.setText("对照");

            btnTest.setEnabled(false);
        }
        Timber.i("选中的项目：" + mProject);

        countDownClear();
        openLight(mProject.bochang);

        // 波长


        if (isOneStart) {
            isOneStart = false;

            GT.Thread.runJava(new Runnable() {
                @Override
                public void run() {
                    GT.Thread.sleep(1000);

                    GT.Thread.runAndroid(PesticideTestActivity2.this, new Runnable() {
                        @Override
                        public void run() {

                            mProject = new Project();
                            mProject.checker = "";
                            mProject.projectName = "农药残留";
                            mProject.testStandard = "GB/T 5009.199";
                            mProject.cardXlz = 50.0f;
                            mProject.k = 1.0f;
                            mProject.b = 0.0f;
                            mProject.bochang = 410;
                            mProject.isSelect = false;

                            Log.d("波长", mProject.projectName);

                            if (isNc()) {
                                tv_yzl.setText("抑制率");
                                tvCompareValue.setVisibility(View.VISIBLE);
                                btnCompare.setText("对照");

                                initSp();
                            } else {
                                Ac = 0;
                                tv_yzl.setText("检测值");
                                APPUtils.showToast(PesticideTestActivity2.this, "请先进行调零");
                                tvCompareValue.setVisibility(View.GONE);
                                btnCompare.setText("调零");

                                btnTest.setEnabled(false);
                            }
                            countDownClear();
                            openLight(mProject.bochang);

                            // 波长

                        }
                    });

                }
            });


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressStatusChange(int status) {

    }

    @Override
    public void onCancel(int status) {
        statusDialog.dismiss();
    }

    //扫码枪点击确定
    @Override
    public void onConfirm(String number) {

        if (number.contains("SampleNumber") || number.contains("SAMPLENUMBER")) {
            new GT.Thread().runJava(new Runnable() {
                @Override
                public void run() {
                    try {
                        String htmlData = "";
                        if (number.contains("SampleNumber")) {
                            htmlData = number.substring(number.indexOf("SampleNumber") + 13);
                        } else if (number.contains("SAMPLENUMBER")) {
                            htmlData = number.substring(number.indexOf("SAMPLENUMBER") + 13);
                        }
                        scanParameterSuccess(htmlData.replaceAll("\\s*", ""));
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return;
        } else if (number.contains("http")) {
            GT.Thread.runJava(new Runnable() {
                @Override
                public void run() {
                    try {

                        GT.WebViewUtils.getHtmlData(number, new GT.WebViewUtils.OnGetHtmlCodeListener() {
                            @Override
                            public void onGetStart(String url) {

                            }

                            @Override
                            public void onGetProgress(int progress) {

                            }

                            @Override
                            public void onGetClose(String url, String htmlData, long htmlSize) {
                                int indexOf1, indexOf2;
                                indexOf1 = htmlData.indexOf("样品编号");
                                indexOf2 = htmlData.indexOf("采样时间");
                                htmlData = htmlData.substring(indexOf1, indexOf2);
                                indexOf1 = htmlData.indexOf("neirong\">");
                                indexOf2 = htmlData.indexOf("<div class=\"left2\"></div>");
                                htmlData = htmlData.substring(indexOf1, indexOf2);
                                indexOf1 = htmlData.indexOf(">");
                                indexOf2 = htmlData.indexOf("<");
                                htmlData = htmlData.substring(indexOf1 + 1, indexOf2).replaceAll("\\s", "");
                                scanParameterSuccess(htmlData.replaceAll("\\s*", ""));

                            }

                            @Override
                            public void onGetError(String url, Object errorMessage) {

                            }
                        });
//                        String htmlData = GT.WebApi.getHtmlData(number);
//                        int indexOf1, indexOf2;
//                        indexOf1 = htmlData.indexOf("样品编号");
//                        indexOf2 = htmlData.indexOf("采样时间");
//                        htmlData = htmlData.substring(indexOf1, indexOf2);
//                        indexOf1 = htmlData.indexOf("neirong\">");
//                        indexOf2 = htmlData.indexOf("<div class=\"left2\"></div>");
//                        htmlData = htmlData.substring(indexOf1, indexOf2);
//                        indexOf1 = htmlData.indexOf(">");
//                        indexOf2 = htmlData.indexOf("<");
//                        htmlData = htmlData.substring(indexOf1 + 1, indexOf2).replaceAll("\\s", "");
//
//                        scanParameterSuccess(htmlData.replaceAll("\\s*", ""));
//                        return;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            new GT.Thread().runJava(new Runnable() {
                @Override
                public void run() {
                    try {
//                        String htmlData = GT.WebApi.getHtmlData(number);
//                        int indexOf1, indexOf2;
//                        indexOf1 = htmlData.indexOf("样品编号");
//                        indexOf2 = htmlData.indexOf("采样时间");
//                        htmlData = htmlData.substring(indexOf1, indexOf2);
//                        indexOf1 = htmlData.indexOf("neirong\">");
//                        indexOf2 = htmlData.indexOf("<div class=\"left2\"></div>");
//                        htmlData = htmlData.substring(indexOf1, indexOf2);
//                        indexOf1 = htmlData.indexOf(">");
//                        indexOf2 = htmlData.indexOf("<");
//                        htmlData = htmlData.substring(indexOf1 + 1, indexOf2).replaceAll("\\s", "");
//
//                        scanParameterSuccess(htmlData.replaceAll("\\s*", ""));
                        return;

                    } catch (Exception e) {
                        e.printStackTrace();
//                        GT.errs("从网上扒数据异常：" + e);
                    }
                }
            });

            return;
        }


        scanParameterSuccess(number);
    }

    //扫描二维码后，向后台请求数据成功后，后台反馈的数据
    @Override
    public void GetSamplingInfoSuccess(Result<GetSamplingInfoResultBean> result, int requestCode) {


        if (clickPosition == requestCode) {
            if (statusDialog != null) {
                statusDialog.dismiss();
            }
            resultList.get(clickPosition).sampleName = result.getData().getSamplingName();
            resultList.get(clickPosition).sampleNum = sampleCode;
            resultList.get(clickPosition).sampleSource = result.getData().getSamplingSource();
            resultList.get(clickPosition).bcheckedOrganization = result.getData().getSamplingDept();
            resultList.get(clickPosition).sampleType = result.getData().getSamplingType();

            testAdapter.notifyItemChanged(clickPosition);
        }
    }

    @Override
    public void GetSamplingInfoFailed(String msg, int requestCode) {
        Log.d(TAG, "GetSamplingInfoFailed msg=" + msg);
//        if (statusDialog != null) {
//            statusDialog.dismiss();
//        }
        statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
//   TODO     APPUtils.showToast(this, "验证样本编号失败:" + msg);
    }

    int remainTimes;

    // 设置的标志位
    String TestValue_1 = "";

    //向后台请求数据后，在这里输入数据
    @Override
    public void GetCardQRInfoSuccess(Result<GetQRInfoResultBean> result, int requestCode) {


        //
        Bundle bundle = new Bundle();
        bundle.putString("QRCode", result.getData().getQRCode());

        // 获取TestValue和JudgeRegion对应的值
        for (int i = 0; i < result.getData().getAlgorithmPara().size(); i++) {
            GetQRInfoResultBean.AlgorithmParaBean lif = result.getData().getAlgorithmPara().get(i);
            if (lif.getTitle().equals("TestValue")) {
                bundle.putString("TestValue", lif.getValue());
            } else if (lif.getTitle().equals("JudgeRegion")) {
                bundle.putString("JudgeRegion", lif.getValue());
            } else if (lif.getTitle().equals("Limit")) {
                limit_standard = lif.getValue();
            } else if (lif.getTitle().equals("Critical")) {
                critical_value = lif.getValue();
            }
        }

        // 获取返回信息
        for (int i = 0; i < result.getData().getListInfo().size(); i++) {
            GetQRInfoResultBean.ListInfoBean lif = result.getData().getListInfo().get(i);
            if (lif.getTitle().equals("检测项目")) {
                bundle.putString("检测项目", lif.getValue());
                list_project_name.add(lif.getValue());
                jiance_xiangmu = lif.getValue();
            } else if (lif.getTitle().equals("判读方法")) {
                bundle.putString("判读方法", lif.getValue());
            } else if (lif.getTitle().equals("检测方法")) {
                bundle.putString("检测方法", lif.getValue());
            } else if (lif.getTitle().equals("国家标准")) {
                bundle.putString("国家标准", lif.getValue());
            } else if (lif.getTitle().equals("国家标准单位")) {
                bundle.putString("国家标准单位", lif.getValue());
            } else if (lif.getTitle().equals("剩余最大使用次数")) {
                bundle.putString("剩余最大使用次数", lif.getValue());
                list_using_times.add("剩余最大使用次数" + lif.getValue() + "次");
            }
        }

        // 滴定法跳转页面
        for (int i = 0; i < result.getData().getListInfo().size(); i++) {
            GetQRInfoResultBean.ListInfoBean lif = result.getData().getListInfo().get(i);
            if (lif.getTitle().equals("判读方法")) {
                if ("滴定法".equals(lif.getValue())) {

                    try {
                        unregisterReceiver(locationReceiver);
                    } catch (Exception e) {
                    }

                    Intent intent = new Intent(PesticideTestActivity2.this, TitrationTestActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    if (!com.example.utils.http.Global.isAdimin) {
//                        findViewById(R.id.tv_projectName).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tv_projectName).setVisibility(View.GONE);
                    }

                    statusDialog.dismiss();
                    if (
                            list_card_qrcode.size() > 0 && list_project_name.size() > 0 && list_using_times.size() > 0
                    ) {

                        list_card_qrcode.remove(list_card_qrcode.size() - 1);
                        list_project_name.remove(list_project_name.size() - 1);
                        list_using_times.remove(list_using_times.size() - 1);
                    }

                }
            }
        }


        if (result.getData().getQRErrCode() == null || result.getData().getQRErrCode().isEmpty() || !result.getData().getQRErrCode().equals("0")) {
            statusDialog.setMessage("验证失败:" + result.getData().getQRErrMsg());
        } else {


            findViewById(R.id.tv_projectName).setVisibility(View.GONE);

            tv_card_number.setText(list_card_qrcode.get(list_card_qrcode.size() - 1).toString());
            remainTimes = result.getData().getRemainTimes();
            tv_hint.setText(list_using_times.get(list_using_times.size() - 1).toString());
            statusDialog.dismiss();

//            tv_card_number.setText(card_number);
//            remainTimes = result.getData().getRemainTimes();
//            tv_hint.setText(String.format("剩余可用次数%d次", remainTimes));
//            statusDialog.dismiss();


            //非管理员模式才需要获取服务器项目参数
            if (!com.example.utils.http.Global.isAdimin) {
                //项目
                if (result.getData().getListInfo() == null || result.getData().getListInfo().isEmpty()) {
                    APPUtils.showToast(this, "获取项目参数失败:" + result.getErrMsg());
                } else {
                    mProject = new Project();
                    for (int i = 0; i < result.getData().getListInfo().size(); i++) {
                        GetQRInfoResultBean.ListInfoBean lif = result.getData().getListInfo().get(i);
                        if (lif.getTitle().equals("检测项目")) {
//                            tv_project.setText(lif.getValue() + "");

                            // 修改
                            tv_project.setText(list_project_name.get(list_project_name.size() - 1).toString());

                            mProject.projectName = lif.getValue();
                        } else if (lif.getTitle().equals("国家标准")) {
                            tv_yzl.setText(lif.getValue() + "");
//                            mProject.cardXlz = Float.valueOf(lif.getValue());
                        }
                    }
                    for (int i = 0; i < result.getData().getAlgorithmPara().size(); i++) {
                        GetQRInfoResultBean.AlgorithmParaBean lif = result.getData().getAlgorithmPara().get(i);
                        if (lif.getTitle().equals("k")) {
                            mProject.k = Float.valueOf(lif.getValue());
                        } else if (lif.getTitle().equals("b")) {
                            mProject.b = Float.valueOf(lif.getValue());
                        } else if (lif.getTitle().contains("WaveLength")) {
                            mProject.bochang = Integer.valueOf(lif.getValue());
                        } else if ("Limit".equals(lif.getTitle())) {
//                            tv_yzl.setText(lif.getValue() + "");
                            mProject.cardXlz = Float.valueOf(lif.getValue());
                        }
                    }
                }
                if (isNc()) {
                    tv_yzl.setText("抑制率");
                    btnCompare.setText("对照");

                    tvCompareValue.setVisibility(View.VISIBLE);
                    initSp();
                } else {
                    Ac = 0;


                    tv_yzl.setText("检测值");
                    btnCompare.setText("调零");

                    APPUtils.showToast(this, "请先进行调零");
                    tvCompareValue.setVisibility(View.GONE);
                    btnTest.setEnabled(false);
                }
                countDownClear();
//            mProject.bochang = 410;
                openLight(mProject.bochang);

                // 波长


                Log.d(TAG, "GetCardQRInfoSuccess mProject=" + mProject.toString2());
            } else {
                findViewById(R.id.tv_projectName).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void GetCardQRInfoFailed(String msg, int requestCode) {
        Log.d(TAG, "GetCardQRInfoFailed msg=" + msg);
        statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_CARD);
//        APPUtils.showToast(this, "验证检测卡/试剂盒编号失败:" + msg);
    }

    @Override
    public void SendResultSuccess(String msg, int requestCode) {
        Log.d(TAG, "SendResultSuccess result=" + msg);
        if (!isFinishing) {
            APPUtils.showToast(this, "上传成功");
        }
    }

    @Override
    public void SendResultFailed(String msg, int requestCode) {
        Log.d(TAG, "SendResultFailed msg=" + msg);
        APPUtils.showToast(this, "A" + (requestCode + 1) + "上传失败" + msg);
    }

    class BSCountDownTask extends TimerTask {

        @Override
        public void run() {
            if (isFinishing()) return;
            mHandler.obtainMessage(ToolUtils.update_countdown, --curDownTime)
                    .sendToTarget();
            if (curDownTime <= 0) {
                mTimer.cancel();
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                Log.d(TAG, "BSCountDownTask testing=" + isTesting);
                mHandler.sendEmptyMessage(ToolUtils.testing);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isTesting) {
                            Log.d(TAG, "BSCountDownTask TEST_END=");
                            sendData(TEST_END);
                        } else {
                            Log.d(TAG, "BSCountDownTask COMPARE_END=");
                            sendData(COMPARE_END);
                        }
                    }
                });

            }

        }
    }

    private void startReaction() {

        countDownClear();//恢复倒计时
        final int time = reactionTime;

        if (reactionTimer != null) {
            reactionTimer.cancel();
            reactionTimer = null;
        }
        reactionTimer = new Timer();

        reactionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (reactionTime == time) {
                            tv_status.setText("检测中:" + reactionTime + "s");
                            if (isNc()) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        if (isTesting) {
                                            sendData(TEST_START);
                                        } else {
                                            sendData(COMPARE_START);
                                        }
                                    }
                                }.start();
                            }
                        } else if (reactionTime == 0) {
                            mHandler.sendEmptyMessage(ToolUtils.testing);
                            if (!isNc()) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isTesting) {
                                            sendData(TEST_START);
                                        } else {
                                            sendData(COMPARE_START);
                                        }
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isTesting) {
                                            sendData(TEST_END);
                                        } else {
                                            sendData(COMPARE_END);
                                        }
                                    }
                                });
                            }


                            if (reactionTimer != null) {
                                reactionTimer.cancel();
                                reactionTimer = null;
                            }

                        } else {
                            tv_status.setText("检测中:" + reactionTime + "s");
                        }

                        reactionTime--;
                    }
                });


            }
        }, 0, 1000);

    }

    private void executeTest(final int flag) {
        long delTime = 3000;
        /*if (isNc()) {
            delTime = 3000;
        } else {
            delTime = 3000;
        }*/
        if (Global.DEBUG) Log.i(TAG, "发送数据====" + new String(Global.GETALLDATA));
        if (!SerialUtils.COM3_SendData(Global.GETALLDATA)) {
            sendEmptyMessage(false);
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    byte[] response = SerialUtils.COM3_RevData();
                    if (response == null || response.length == 0) {
                        sendEmptyMessage(false);
                    } else {
//                        Log.i(TAG, "接收到的检测数据======" + new String(response));
                        Timber.i("接收到的检测数据======" + new String(response));
                        float[] fList = dealTestData(response);
                        if (fList == null) {
                            sendEmptyMessage(false);
                        } else {
                            parseTest(fList, flag);

                        }
                    }
                    countDownLatch();
                }
            }, delTime);
        }
    }

    /**
     * 解析结果
     *
     * @param fList
     * @param flag
     */
    private void parseTest(float[] fList, int flag) {
        if (flag == TEST_START) {
            as1List = fList;
            if (!isNc()) {
                mHandler.sendEmptyMessage(ToolUtils.test_success);
            }
            return;
        } else if (flag == TEST_END) {
            as2List = fList;
            float[] tempAcList = new float[Global.CHANNEL_COUNT];
            for (int i = 0; i < as2List.length; i++) {
                tempAcList[i] = (float) Math.log10(as1List[i] / as2List[i]);//这个是 AS
                Log.d(TAG, "as1List[i]=" + as1List[i] + "as2List[i]=" + as2List[i]);
            }
            AsList = new ArrayList<>();
            for (int i = 0; i < as2List.length; i++) {
                double temp = 0;
                if (Ac - tempAcList[i] < 0) {
                    temp = 0;
                } else {
                    temp = (Ac - tempAcList[i]) / Ac;
                }
                Log.d(TAG, "temp=" + temp);//算出的 抑制率
                AsList.add(temp > 1 ? 1 : temp);
            }
            mHandler.sendEmptyMessage(ToolUtils.test_success);
        }
    }

    private float[] dealTestData(byte[] response) {
        float[] ds = null;
        String result = new String(response).replace("OK", "").replace("\n", "");
        String[] st = result.split(",");
        if (st.length != Global.CHANNEL_COUNT) {
            return null;
        }

        ds = new float[Global.CHANNEL_COUNT];

        for (int i = 0; i < Global.CHANNEL_COUNT; i++) {
            try {
                ds[i] = Float.valueOf(st[i]);
            } catch (Exception e) {
                ds[i] = 0;
            }

        }
        return ds;
    }


    private float[] computeAc(byte[] response) {

        float[] ds = null;
        String result = new String(response).replace("OK", "").replace("\n", "");
        String[] st = result.split(",");
        if (st.length != Global.CHANNEL_COUNT) {
            return null;
        }
        ds = new float[Global.CHANNEL_COUNT];
        for (int i = 0; i < Global.CHANNEL_COUNT; i++) {
            ds[i] = Float.valueOf(st[i]);
        }
        return ds;
    }

    private void sendEmptyMessage(boolean isSucc) {

        if (isSucc) {
            if (isComparing) {
                mHandler.sendEmptyMessage(ToolUtils.compare_success);
            } else {
                mHandler.sendEmptyMessage(ToolUtils.test_success);
            }
        } else {
            if (isComparing) {
                mHandler.sendEmptyMessage(ToolUtils.compare_fail);
            } else {
                mHandler.sendEmptyMessage(ToolUtils.test_fail);
            }
        }
    }

    private synchronized void sendData(int flag) {
        if (isFinishing) return;
        initLatch();
        SystemClock.sleep(100);

        if (isFinishing) {
            return;
        }
        if (Global.DEBUG) {
            if (flag == COMPARE_START || flag == COMPARE_END) {
                float[] d = new float[20];
                for (int i = 0; i < 20; i++) {
                    if (flag == COMPARE_START) {
                        d[i] = 0.2f;
                    } else {
                        d[i] = 0.5f;
                    }
                }
                parseCompare(d, flag);
            } else if (flag == TEST_START || flag == TEST_END) {
                float[] d = new float[20];
                for (int i = 0; i < 20; i++) {
                    if (flag == TEST_START) {
                        d[i] = 0.8f;
                    } else {
                        d[i] = 0.5f;
                    }
                }
                parseTest(d, flag);
            }
        } else {
            switch (flag) {
                case COMPARE_START://1
                    compareRecData(flag);
                    break;
                case COMPARE_END://7
                    compareRecData(flag);
                    break;
                case TEST_START://0
                    if (DEBUG) Log.i(TAG, "===开始检测第一次取值");
                    executeTest(flag);
                    break;
                case TEST_END://2
                    if (DEBUG) Log.i(TAG, "===开始检测第2次取值");
                    executeTest(flag);
                    break;
            }
        }
//        waitCountDownLatch();
    }

    private static float[] d;

    private void compareRecData(final int flag) {
        if (Global.DEBUG) Log.i(TAG, "发送数据====" + new String(Global.GETALLDATA));
        if (!SerialUtils.COM3_SendData(Global.GETALLDATA)) {
            sendEmptyMessage(false);
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    byte[] response = SerialUtils.COM3_RevData();
                    Timber.i("接收到的检测数据======" + new String(response));
                    if (response == null || response.length == 0) {
                        sendEmptyMessage(false);
                    } else {
                        if (Global.DEBUG) {
                            APPUtils.showToast(act, new String(response), true);
                        }
                        d = computeAc(response);

                        if (d == null) {
                            sendEmptyMessage(false);
                        } else {
                            for (int i = 0; i < d.length; i++) {
                            }
                            if (Global.DEBUG) Log.i(TAG, "Ac======" + (d != null ? d[0] : 0));

                            parseCompare(d, flag);

                        }
                    }
                    countDownLatch();
                    if (Global.DEBUG) Log.i(TAG, "countDownLatch().......");
                }
            }, 3500);
        }
    }

    /**
     * 解析对照结果
     *
     * @param d
     * @param flag
     */
    private void parseCompare(float[] d, int flag) {
        if (flag == COMPARE_START) {
            ac1 = d[compareChannelIndex];
            if (!isNc()) {//多功能只需取一次值便对照完成
                mHandler.sendEmptyMessage(ToolUtils.compare_success);
            }
            return;
        } else {
            Ac = (float) Math.log10(ac1) - (float) Math.log10(d[compareChannelIndex]);  //计算对照值
            mHandler.sendEmptyMessage(ToolUtils.compare_success);
            saveAc2Sp();
            if (Global.DEBUG) Log.i(TAG, "AC2======" + Ac);
        }
    }

    private void saveAc2Sp() {
        sp.edit().putFloat(SPResource.KEY_COMPARE_VALUE, Ac).apply();
    }


    private void postForAction(long delayMills) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownLatch();
            }
        }, delayMills);
    }

    private void countDownLatch() {

//        Log.i(TAG, "置零...");
        if (mCountDownLatch != null) {
            mCountDownLatch.countDown();
        }
    }

    private void initLatch() {

        if (mCountDownLatch != null) {
            mCountDownLatch.countDown();
            mCountDownLatch = null;
        }
        mCountDownLatch = new CountDownLatch(1);
    }


    private void waitCountDownLatch() {
        try {
//            Log.i(TAG, "阻塞...");
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 現在是否是检测农药残留項目
     *
     * @return
     */
    private boolean isNc() {

        if (mProject != null && "农药残留".equals(mProject.getName())) {
            return true;
        } else if (mProject != null && "有机磷和氨基甲酸酯类".equals(mProject.getName())) {
            return true;
        }
        return false;
    }

    private void executeBSData(byte[] data) {

        if (!SerialUtils.COM3_SendData(data)) {
            countDownLatch();
            mHandler.sendEmptyMessage(ToolUtils.test_fail);
            return;
        }
        if (Global.DEBUG) Log.i(TAG, "发送数据====" + new String(data));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownLatch();
                byte[] response = SerialUtils.COM3_RevData();
                if (response == null || response.length != 3) {
                    mHandler.sendEmptyMessage(ToolUtils.test_fail);
                } else {
                    String result = new String(response);
                    if (Global.DEBUG) Log.i(TAG, "接收数据====" + result);
                    if (response[0] == 'O' && response[1] == 'K' && response[2] == '\n') {

                    } else {
                        mHandler.sendEmptyMessage(ToolUtils.test_fail);
                    }
                }
            }
        }, 4500);
    }

    private BSCountDownTask timerTask;

    String qrcode;

    private void receivedata() {
        Intent intent = getIntent();
        qrcode = intent.getStringExtra("QRCode");
        String testproject = intent.getStringExtra("检测项目");
        String maxtimes = intent.getStringExtra("剩余最大使用次数");
        String WaveLength = intent.getStringExtra("WaveLength");

        if (("".equals(qrcode) || qrcode == null) || ("".equals(testproject) || testproject == null) || ("".equals(maxtimes) || maxtimes == null)) {
            return;
        } else {
            tv_card_number.setText(qrcode);
            tv_hint.setText("剩余可用次数" + maxtimes + "次");
            tv_project.setText(testproject);
            findViewById(R.id.tv_projectName).setVisibility(View.GONE);

            if (!"".equals(WaveLength) && WaveLength != null) {
                try {
                    Integer num1 = new Integer(WaveLength);
                    openLight(num1);
                } catch (Exception e) {
                }
            }

        }
    }

    private String locationMsg = "";
    private String LocationX = "";
    private String LocationY = "";
    private String LocationAddress = "";

    // 获取定位信息
    private void set_dingwei_info() {
        try {
            locationMsg = MainActivity.gt_sp.query("locationMsg").toString();
            MainActivity.gt_sp.save("locationMsg", locationMsg);

            String[] split_something = locationMsg.split(",");

            LocationY = split_something[0];
            LocationX = split_something[1];
            LocationAddress = split_something[2];

        } catch (Exception e) {
            e.printStackTrace();

            LocationX = "";
            LocationY = "";
            LocationAddress = "";
        }

    }


    private String samplename = "";
    private String sampleid = "";
    private String sampletype = "";
    private String samplesource = "";
    private String samplebcheckedOrganization = "";
    private String companyCode = "";//组织机构

    public static int REQUEST_CODE = 0;

    String[] yplbIdlist = new String[20];
    String[] sampletypelist = new String[20];
    String[] sampleidlist = new String[20];
    String[] sublistProductId = new String[20];
    String[] locListId = new String[20];
    String[] locSp1Id = new String[20];
    String[] locSp2Id = new String[20];
    String[] locSp3Id = new String[20];

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            samplename = data.getStringExtra("samplename");
            sampleid = data.getStringExtra("sampleid");
            sampletype = data.getStringExtra("sampletype");
            samplesource = data.getStringExtra("samplesource");
            samplebcheckedOrganization = data.getStringExtra("samplebcheckedOrganization");
            yplbId = data.getStringExtra("yplbId");
            jsSubProductId = data.getStringExtra("jsSubProductId");
            spId1 = data.getStringExtra("spId1");
            spId2 = data.getStringExtra("spId2");
            spId3 = data.getStringExtra("spId3");
            companyCode = data.getStringExtra("companyCode");
//            if (!"".equals(samplename) && !"".equals(sampleid) && !"".equals(sampletype) && !"".equals(samplesource) && !"".equals(samplebcheckedOrganization)) {
            if (!"".equals(samplename) && !"".equals(sampleid) && !"".equals(sampletype) && !"".equals(samplesource)) {
                // 设置录入方式
                mode_statu_list[clickPosition] = false;
                resultList.get(clickPosition).sampleName = samplename;
                resultList.get(clickPosition).sampleNum = sampleid;
                resultList.get(clickPosition).sampleSource = samplesource;
                resultList.get(clickPosition).bcheckedOrganization = samplebcheckedOrganization;
                resultList.get(clickPosition).sampleType = sampletype;
                resultList.get(clickPosition).companyCode = companyCode;//组织机构   之前是这个页面给组织机构赋值

                sampleidlist[clickPosition] = sampleid;
                yplbIdlist[clickPosition] = yplbId;
                sampletypelist[clickPosition] = sampletype;
                sublistProductId[clickPosition] = jsSubProductId;
                locListId[clickPosition] = samplesource;
                locSp1Id[clickPosition] = spId1;
                locSp2Id[clickPosition] = spId2;
                locSp3Id[clickPosition] = spId3;


                testAdapter.notifyItemChanged(clickPosition);
            }
        }


    }


    private String yplbId = "";//样品类别Id
    private String spId1 = "";//样品来源1级地址
    private String spId2 = "";//样品来源2级地址
    private String spId3 = "";//样品来源3级地址
    private String ResultValue = "";//检测值
    private String Result = "";//检测结果

    private GT.Hibernate hibernate;


    // 上传成功状态位
    private static int upload_status = 0;

    // 自动录入上传方式
    private void uploadResult2(int index) {

        //获取必要上传
        SharedPreferences sp = getSharedPreferences("userPass", MODE_PRIVATE);
        String user = sp.getString("user", "");
        String terrace = sp.getString("terrace", "");
        String deptId = sp.getString("DeptId", "");
        String setWithModel = getStringSN();//获取唯一标识


        String str_api = "";

        // 只有单金标且登录用户是唐山的用户才使用 "Tangshan/SendForNAD4074" api
        // 其他的都是 "CommonManual/GetSamplingTypeList" api
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) || "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "QR/SendResult";
        }

        String data = "";


        Result = "";
        ResultValue = "";
        Result = savaDatas.get(currentIndex).resultJudge;
        //Result = resultList.get(clickPosition).resultJudge;
        if ("合格".equals(Result)) {
            ResultValue = "0";
        } else if ("不合格".equals(Result)) {
            ResultValue = "1";
        } else {
            ResultValue = "-1";
        }

        // 新的上传
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) || "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {

            data = "{\"QRCode\":\"" + card_number.replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + ResultValue + "\",\n" +
                    "\"ResultValue\":\"" + savaDatas.get(currentIndex).testValue + "\",\n" +
                    "\"SamplingNumber\":\"" + savaDatas.get(currentIndex).sampleNum + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "    [\n" +
                    "        {\n" +
                    "        \"Title\":\"检测项目\",\n" +
                    "        \"Id\":\"projectName\",\n" +
                    "        \"Value\":\"" + savaDatas.get(currentIndex).projectName + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"Title\":\"限量值\",\n" +
                    "            \"Id\":\"xlz\",\n" +
                    "            \"Value\":\"" + savaDatas.get(currentIndex).xlz + "\"\n" +
                    "            }\n" +
                    "            ,{\n" +
                    "                \"Title\":\"被检测单位\",\n" +
                    "                \"Id\":\"sampleSource\",\n" +
                    "                \"Value\":\"" + savaDatas.get(currentIndex).bcheckedOrganization + "\"\n" +
                    "                },\n" +
                    "            ,{\n" +
                    "                \"Title\":\"设备编号\",\n" +
                    "                \"Id\":\"deviceSn\",\n" +
                    "                \"Value\":\"" + MainActivity.mac_url + "\"\n" +
                    "                },\n" +
                    "            ,{\n" +
                    "                \"Title\":\"组织机构代码\",\n" +
                    "                \"Id\":\"companyCode\",\n" +
                    "                \"Value\":\"" + savaDatas.get(currentIndex).companyCode + "\"\n" +
                    "                },\n" +
                    "{\n" +
                    "    \"Title\":\"商品来源\",\n" +
                    "    \"Id\":\"samplingDept\",\n" +
                    "    \"Value\":\"" + savaDatas.get(currentIndex).sampleSource + "\"\n" +
                    "    }]}";

        }

        Timber.i("自动录入方式data:" + data);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                upload_status = 3;
                MainActivity.hibernate.save(detectionResultBean);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean3 testDataBean3 = null;
                testDataBean3 = new Gson().fromJson(ret, TestDataBean3.class);

                if (testDataBean3 != null) {
                    Log.i("lcy", "onSuccess: ----testDataBean3 自动---" + testDataBean3);
                    errMsg = testDataBean3.getData().getErrMsg();
                    TestDataBean2 data1 = testDataBean3.getData();
                    if (data1 != null) {
                        PesticideTestActivity2.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("0".equals(data1.getErrCode())) {
                                    upload_status = 1;
                                    detectionResultBean.setUploadStatus("已上传");
                                    MainActivity.hibernate.save(detectionResultBean);
                                } else {
                                    upload_status = 2;
                                    MainActivity.hibernate.save(detectionResultBean);
                                }
                                currentIndex++;
                                upDateShuju();
                            }
                        });
                    }
                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                TestDataBean3 testDataBean3 = null;
//                testDataBean3 = new Gson().fromJson(response, TestDataBean3.class);
//
//                if (testDataBean3 != null) {
//                    Log.i("lcy", "onSuccess: ----testDataBean3 自动---"+testDataBean3);
//                    errMsg= testDataBean3.getData().getErrMsg();
//                    TestDataBean2 data1 = testDataBean3.getData();
//                    if (data1 != null) {
//                        PesticideTestActivity2.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if ("0".equals(data1.getErrCode())) {
//                                    upload_status = 1;
//                                    detectionResultBean.setUploadStatus("已上传");
//                                    MainActivity.hibernate.save(detectionResultBean);
//                                } else {
//                                    upload_status = 2;
//                                    MainActivity.hibernate.save(detectionResultBean);
//                                }
//                                currentIndex++;
//                                upDateShuju();
//                            }
//                        });
//                    }
//                }
//
//            }
//
//            @Override
//            public void onError(String response) {
//                upload_status = 3;
//                MainActivity.hibernate.save(detectionResultBean);
//            }
//        });

    }


    // 手动录入上传方式
    private void uploadResult1(int index) {
        //获取必要上传
        SharedPreferences sp = getSharedPreferences("userPass", MODE_PRIVATE);
//        String user = sp.getString("user", "");
        String user = com.example.utils.http.Global.ID;
        String terrace = sp.getString("terrace", "");
        String deptId = sp.getString("DeptId", "");
        String setWithModel = getStringSN();//获取唯一标识
        String str_api = "";

        // 只有单金标且登录用户是唐山的用户才使用 "Tangshan/SendForNAD4074" api
        // 其他的都是 "CommonManual/GetSamplingTypeList" api
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) || "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "CommonManual/UpdateManualSampling";
        }
        if ("".equals(LocationX) && "".equals(LocationY) && "".equals(LocationAddress)) {
            locationMsg = MainActivity.gt_sp.query("locationMsg").toString();
            MainActivity.gt_sp.save("locationMsg", locationMsg);

            String[] split_something = locationMsg.split(",");

            LocationX = split_something[0];
            LocationY = split_something[1];
            LocationAddress = split_something[2];
        }
        String data = "";
        Result = "";
        ResultValue = "";
        Result = savaDatas.get(currentIndex).resultJudge;
        //Result = resultList.get(clickPosition-1).resultJudge;
        if ("合格".equals(Result)) {
            ResultValue = "0";
        } else if ("不合格".equals(Result)) {
            ResultValue = "1";
        } else {
            ResultValue = "-1";
        }


        // 获取参数  归属地 本地判断是否是唐山界面进行样本名称选择
        String AreaId = com.example.utils.http.Global.admin_pt;
        if (AreaId.equals("TangshanNMEnterprise")) {
            if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) || "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
                data = "[{\"QRCode\":\"" + card_number.replaceAll("\\s*", "") + "\",\n" +
                        "\"LocationX\":\"" + LocationX + "\",\n" +
                        "\"LocationY\":\"" + LocationY + "\",\n" +
                        "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                        "\"Result\":\"" + ResultValue + "\",\n" +
                        "\"ResultValue\":\"" + savaDatas.get(currentIndex).testValue + "\",\n" +
                        "\"SamplingNumber\":\"" + SamplNumlist.get(currentIndex) + "\",\n" +
                        "\"AreaId\":\"" + terrace + "\",\n" +
                        "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                        "\"ResultData\":\n" +
                        "[\n" +
                        "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                        "{\"Title\":\"样品来源地1级\",\"Id\":\"source1\",\"Value\":\"" + locsp1Id.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"样品来源地2级\",\"Id\":\"source2\",\"Value\":\"" + locsp2Id.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"样品来源地3级\",\"Id\":\"source3\",\"Value\":\"" + locsp3Id.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"样品总分类Id\",\"Id\":\"sampleClassId\",\"Value\":\"" + sampleId + "\"},\n" +
                        "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + "\"},\n" +
                        "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + typelist.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + detectionResultBean.getUnitsUnderInspection() + "\"},\n" +
                        "{\"Title\":\"样品详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + locId.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                        "{\"Title\":\"设备型号\",\"Id\":\"deviceType\",\"Value\":\"" + "MD_417" + "\"},\n" +
                        "{\"Title\":\"组织机构代码\",\"Id\":\"companyCode\",\"Value\":\"" + detectionResultBean.companyCode + "\"},\n" +
                        "{\"Title\":\"产 品Id\",\"Id\":\"productId\",\"Value\":\"" + subProductId.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"对象 Id\",\"Id\":\"objectId\",\"Value\":\"" + yplblist.get(currentIndex) + "\"}\n" +
                        "]}\n" +
                        "]";
            }

        } else {
            if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) || "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
                data = "[{\"QRCode\":\"" + card_number.replaceAll("\\s*", "") + "\",\n" +
                        "\"LocationX\":\"" + LocationX + "\",\n" +
                        "\"LocationY\":\"" + LocationY + "\",\n" +
                        "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                        "\"Result\":\"" + ResultValue + "\",\n" +
                        "\"ResultValue\":\"" + savaDatas.get(currentIndex).testValue + "\",\n" +
                        "\"SamplingNumber\":\"" + SamplNumlist.get(currentIndex) + "\",\n" +
                        "\"AreaId\":\"" + terrace + "\",\n" +
                        "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                        "\"ResultData\":\n" +
                        "[\n" +
                        "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                        "{\"Title\":\"样品来源地1级\",\"Id\":\"source1\",\"Value\":\"" + locsp1Id.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"样品来源地2级\",\"Id\":\"source2\",\"Value\":\"" + locsp2Id.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"样品来源地3级\",\"Id\":\"source3\",\"Value\":\"" + locsp3Id.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"样品总分类Id\",\"Id\":\"sampleClassId\",\"Value\":\"" + sampleId + "\"},\n" +
                        "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + yplblist.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + typelist.get(currentIndex) + "\"},\n" +
                        "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + detectionResultBean.getUnitsUnderInspection() + "\"},\n" +
                        "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                        "{\"Title\":\"组织机构代码\",\"Id\":\"companyCode\",\"Value\":\"" + detectionResultBean.companyCode + "\"},\n" +
                        "{\"Title\":\"样品详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + locId.get(currentIndex) + "\"}\n" +
                        "]}\n" +
                        "]";
            }

        }
        // 新的上传
        Timber.i("手动录入上传方式data:" + data);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MainActivity.hibernate.save(detectionResultBean);
                Log.i("lcy", "run: ---3------" + detectionResultBean.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean1 uploadData1 = new Gson().fromJson(ret, TestDataBean1.class);
                if (uploadData1 != null) {
                    Timber.i("手动录入上传方式走了:" + response);
                    Log.i("lcy", "onSuccess: --uploadData1----" + uploadData1.toString());
                    errMsg = uploadData1.getData().get(0).getErrMsg();
                    String errCode = uploadData1.getData().get(0).getErrCode();
                    runOnUiThread(() -> {
                        Log.i("lcy", "onSuccess: --uploadData1----" + uploadData1);
                        Log.d("zdl", "=========success=============" + response);
                        Log.d("zdl", "end:========" + detectionResultBean.getSampleName() + ":");
                        //0 是成功 进入这个回调肯定就是上传成功了
                        if ("0".equals(errCode)) {
                            detectionResultBean.setUploadStatus("已上传");
                            MainActivity.hibernate.save(detectionResultBean);
                        } else {
//                            MessageDialog.show("提示", errMsg, "确定");
                            MainActivity.hibernate.save(detectionResultBean);
                        }
                        //只有进到这个里面才会开始上传下一个
                        currentIndex++;
                        upDateShuju();
                    });
                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                TestDataBean1 uploadData1 = new Gson().fromJson(response, TestDataBean1.class);
//                if (uploadData1 != null) {
//                    Timber.i("手动录入上传方式走了:"+response);
//                    Log.i("lcy", "onSuccess: --uploadData1----" +uploadData1.toString());
//                    errMsg= uploadData1.getData().get(0).getErrMsg();
//                    String errCode = uploadData1.getData().get(0).getErrCode();
//                    runOnUiThread(() -> {
//                        Log.i("lcy", "onSuccess: --uploadData1----" +uploadData1);
//                        Log.d("zdl", "=========success=============" + response);
//                        Log.d("zdl", "end:========" + detectionResultBean.getSampleName() + ":");
//                        //0 是成功 进入这个回调肯定就是上传成功了
//                        if ("0".equals(errCode)) {
//                            detectionResultBean.setUploadStatus("已上传");
//                            MainActivity.hibernate.save(detectionResultBean);
//                        } else {
////                            MessageDialog.show("提示", errMsg, "确定");
//                            MainActivity.hibernate.save(detectionResultBean);
//                        }
//                        //只有进到这个里面才会开始上传下一个
//                        currentIndex++;
//                        upDateShuju();
//                    });
//                }
//            }
//
//            @Override
//            public void onError(String response) {
//                MainActivity.hibernate.save(detectionResultBean);
//                Log.i("lcy", "run: ---3------" + detectionResultBean.toString());
//            }
//        });

    }


    /**
     * 获取设备编号
     */
    String[] propertys = {"ro.boot.serialno", "ro.serialno"};

    public String getStringSN() {
        for (String key : propertys) {
            String v = getAndroidOsSystemProperties(key);
            if (v != null) {
                return v;
            }
        }
        return "null";
    }


    private String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }


    /**
     * 数据管理模块中的检测单位和检测人员的保存
     */


    private String[] test_unit_list = new String[0];
    private String[] test_person_list = new String[0];

    private void save_test_unit_SampleTypeModel() {
        db = DbHelper.GetInstance();
        change_testunit();
    }

    private void change_testunit() {
        List<PeopleModel> list = new ArrayList<>();
        List<PeopleModel> list1;
        List<PeopleModel> list2 = new ArrayList<>();
        List<PeopleModel> list3 = new ArrayList<>();
        WhereBuilder whereBuilder = WhereBuilder.b();
        try {
            list = db.findAll(Selector.from(PeopleModel.class).where(whereBuilder));
        } catch (Exception e) {
        }
        if (list == null) {
            list = new ArrayList<>();
        }

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSource() == 1) {
                    list2.add(list.get(i));
                } else if (list.get(i).getSource() == 2) {
                    list3.add(list.get(i));
                }
            }
        }
        if (list2.size() > 0) {
            test_unit_list = new String[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                test_unit_list[i] = list2.get(i).getName();
            }
        } else if (list3.size() > 0) {
            test_person_list = new String[list3.size()];
            for (int i = 0; i < list3.size(); i++) {
                test_person_list[i] = list3.get(i).getName();
            }
        }

        if (savaDatas.size() > 0 && test_unit_list.length == 0) {
            test_unit_list = new String[1];
            test_unit_list[0] = savaDatas.get(0).checkedOrganization;
        }
        if (savaDatas.size() > 0 && test_person_list.length == 0) {
            test_person_list = new String[1];
            test_person_list[0] = savaDatas.get(0).checker;

        }

        // 向数据库保存检测项目的数据
        for (int i = 0; i < savaDatas.size(); i++) {
            test_unit_list = insert(test_unit_list, savaDatas.get(i).checkedOrganization);
        }

        for (int i = 0; i < savaDatas.size(); i++) {
            test_person_list = insert(test_person_list, savaDatas.get(i).checker);
        }

        if (test_unit_list.length > list2.size()) {
            list1 = new ArrayList<>();

            // 每次添加最后一条数据
            list1.add(new PeopleModel(test_unit_list[test_unit_list.length - 1], 1));

            try {
                db.saveAll(list1);
            } catch (DbException e) {
            }

            try {
                list = db.findAll(Selector.from(PeopleModel.class).where(whereBuilder));
            } catch (Exception e) {
            }
        } else {
        }

        if (test_person_list.length > list3.size()) {
            list1 = new ArrayList<>();

            // 每次添加最后一条数据
            list1.add(new PeopleModel(test_person_list[test_person_list.length - 1], 2));

            try {
                db.saveAll(list1);
            } catch (DbException e) {
            }

            try {
                list = db.findAll(Selector.from(PeopleModel.class).where(whereBuilder));
            } catch (Exception e) {
            }
        } else {
        }
    }


    private static String[] insert(String[] arr, String str) {

        int size = arr.length; // 获取原数组长度
        int newSize = size + 1; // 原数组长度加上追加的数据的总长度
        // 新建临时字符串数组
        String[] tmp = new String[newSize];

        for (int j = 0; j < size; j++) {
            tmp[j] = arr[j];
        }


        for (int i = 0; i < size; i++) {
            if (tmp[i] != null && tmp[i].equals(str)) {
                tmp = new String[size];
                for (int j = 0; j < size; j++) {
                    tmp[j] = arr[j];
                }
                break;
            } else {
                tmp[size] = str;
            }
        }

        return tmp; // 返回拼接完成的字符串数组
    }



    /*
        修改数据管理页面样品类型[数据库存储数据]
     */

    // 存储检测记录中有的数据信息

    private String[] sample_type_list = new String[0];

    private void save_sampletype_to_SampleTypeModel() {
        change_sampletype_list();
    }


    private void change_sampletype_list() {
        List<SampleTypeModel> list = new ArrayList<>();
        List<SampleTypeModel> list1 = new ArrayList<>();
        WhereBuilder whereBuilder = WhereBuilder.b();
        try {
            list = db.findAll(Selector.from(SampleTypeModel.class).where(whereBuilder));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        if (list.size() > 0) {
            sample_type_list = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                sample_type_list[i] = list.get(i).getName();
            }
        }

        if (savaDatas.size() > 0 && sample_type_list.length == 0) {
            sample_type_list = new String[1];
            sample_type_list[0] = savaDatas.get(0).sampleType;
        }


        // 向数据库保存检测项目的数据
        for (int i = 0; i < savaDatas.size(); i++) {
            sample_type_list = insert(sample_type_list, savaDatas.get(i).sampleType);
        }

        if (sample_type_list.length > list.size()) {
            // 每次添加最后一条数据
            list1.add(new SampleTypeModel(sample_type_list[sample_type_list.length - 1]));

            try {
                db.saveAll(list1);
            } catch (DbException e) {
                e.printStackTrace();
            }

            try {
                list = db.findAll(Selector.from(SampleTypeModel.class).where(whereBuilder));
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
        }


    }


    /*
        单金标数据管理页面样品名称[数据库存储]
     */

    // 存储检测记录中有的数据信息
    private String[] sample_name_list = new String[0];

    private void save_samplename_to_SampleModel() {
        change_samplename_list();
    }


    private void change_samplename_list() {
        List<SampleModel> list = new ArrayList<>();
        List<SampleModel> list1 = new ArrayList<>();
        WhereBuilder whereBuilder = WhereBuilder.b();
        try {
            list = db.findAll(Selector.from(SampleModel.class).where(whereBuilder));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            sample_name_list = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                sample_name_list[i] = list.get(i).getName();
            }
        }

        if (savaDatas.size() > 0 && sample_name_list.length == 0) {
            sample_name_list = new String[1];
            sample_name_list[0] = savaDatas.get(0).sampleName;
        }


        // 向数据库保存检测项目的数据
        for (int i = 0; i < savaDatas.size(); i++) {
            sample_name_list = insert(sample_name_list, savaDatas.get(i).sampleName);
        }

        if (sample_name_list.length > list.size()) {

            // 每次添加最后一条数据
            list1.add(new SampleModel(sample_name_list[sample_name_list.length - 1]));

            try {
                db.saveAll(list1);
            } catch (DbException e) {
                e.printStackTrace();
            }

            try {
                list = db.findAll(Selector.from(SampleModel.class).where(whereBuilder));
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
        }
    }


    /**
     * 向LineModel数据表中添加数据
     */

    // 存储已检测数据中的检测项目信息
    private String[] project_name_list = new String[0];

    private void save_projectname_to_LineModel() {
        change_projectname_list();
    }


    private void change_projectname_list() {
        String concentrateUnit = "μg/kg";
        List<LineModel> list = new ArrayList<>();
        List<LineModel> list1 = new ArrayList<>();
        WhereBuilder whereBuilder = WhereBuilder.b();
        try {
            list = db.findAll(Selector.from(LineModel.class).where(whereBuilder));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            project_name_list = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                project_name_list[i] = list.get(i).getName();
            }
        }

        if (savaDatas.size() > 0 && project_name_list.length == 0) {
            project_name_list = new String[1];
            project_name_list[0] = savaDatas.get(0).projectName;
        }


        CardCompanyModel cardModel = new CardCompanyModel("奥本", "320", "920", "140", "320");

        // 向数据库保存检测项目的数据
        for (int i = 0; i < savaDatas.size(); i++) {
            project_name_list = insert(project_name_list, savaDatas.get(i).projectName);
        }

        if (project_name_list.length > list.size()) {
            // 每次添加最后一条数据
            list1.add(new LineModel(2, project_name_list[project_name_list.length - 1], cardModel.name, cardModel.ScanStart, cardModel.ScanEnd, cardModel.CTPeakWidth, cardModel.CTPeakDistance, "1", "0.1", concentrateUnit));

            try {
                db.saveAll(list1);
                db.save(cardModel);
            } catch (DbException e) {
                e.printStackTrace();
            }

            try {
                list = db.findAll(Selector.from(LineModel.class).where(whereBuilder));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver();
    }
}
