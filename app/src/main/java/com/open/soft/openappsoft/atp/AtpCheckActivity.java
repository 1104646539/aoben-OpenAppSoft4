package com.open.soft.openappsoft.atp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qrcodescan.QRCode;
import com.example.utils.http.Barcode2D;
import com.example.utils.http.CheckPresenter;
import com.example.utils.http.GetQRInfoBean;
import com.example.utils.http.GetQRInfoResultBean;
import com.example.utils.http.GetSamplingInfoBean;
import com.example.utils.http.GetSamplingInfoResultBean;
import com.example.utils.http.Result;
import com.example.utils.http.StatusDialog;
import com.friendlyarm.AndroidSDK.HardwareControler;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.activity.task.TaskListAdapter2;
import com.open.soft.openappsoft.activity.task.TaskModel;
import com.open.soft.openappsoft.dialog.DialogFragmentPaint;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivityByMen;
import com.open.soft.openappsoft.jinbiao.activity.CheckPaintActivity;
import com.open.soft.openappsoft.jinbiao.activity.MyApplication;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.jinbiao.dialog.DateTimePickerDialog;
import com.open.soft.openappsoft.jinbiao.model.CardCompanyModel;
import com.open.soft.openappsoft.jinbiao.model.LineModel;
import com.open.soft.openappsoft.jinbiao.model.PeopleModel;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.open.soft.openappsoft.jinbiao.model.SampleModel;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeModel;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.jinbiao.model.ShiJiModel;
import com.open.soft.openappsoft.jinbiao.util.APPUtils;
import com.open.soft.openappsoft.jinbiao.util.SerialUtils;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.open.soft.openappsoft.multifuction.util.Global;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;
import com.open.soft.openappsoft.util.UploadThread2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class AtpCheckActivity extends BaseActivity implements OnClickListener {

    /**
     * 读取数据超时次数，如果超过3次，则终止此次检测新的
     */
    private int readTimeOutCount = 0;
    private EditText etJcx = null;
    private EditText etLjz = null;
    private EditText etConcentrate = null;
    private EditText etResult = null;
    private TextView long_tv = null;
    private TextView tv_check_b = null;

    private EditText et_Sample_Num = null;
    private EditText et_SampleTime = null;
    private EditText et_companyCode = null;
    private TableRow llCompany;

    private Button upload_data;
    private Button btn_Imm_Check;
    private DbUtils db;
    /**
     * 反应时间
     */
    int reactionTime = 20;

    private final String TAG = AtpCheckActivity.class.getSimpleName();

    /**
     * 正在上传的标志，指示是否正在进行上传操作
     */
    private Boolean uploadingFlag = false;

    private String source = null;
    String testTime = null;

    String s = null;
    private ResultModel resultModel;
    private static final String ACTION_USB_PERMISSION_BASE = "com.tnd.USB_PERMISSION.";
    private final String ACTION_USB_PERMISSION = ACTION_USB_PERMISSION_BASE + hashCode();
    private TextView tv_check_company;
    private TextView tv_check_persion;
    private TextView tv_check_sample;
    private TextView tv_check_project;
    private TextView tv_check_type;
    public String sampleCode = "";
    public String cardCode = "";
    private TextView tv_scanTime;//扫描时间

    // 样品编号
    private String sample_number;

    private GT.Hibernate hibernate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atp_check_main);

        if (!GT.Network.isInternet(this)) {
            GT.toast(this, "请检查网络是否正常");
            finish();
            return;
        }

        db = DbHelper.GetInstance();
        initView();

        // 隐藏标题
        GT.WindowUtils.hideActionBar(this);

        tv_check_persion.setText(com.example.utils.http.Global.NAME + "");

        upload_data.setEnabled(true);
        showTaskDialog();

        //扫描时间
        tv_scanTime = findViewById(R.id.tv_scanTime);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //弹出对话框
    public void ClickDialog(View view) {
        switch (view.getId()) {
            case R.id.tv_check_sample:
            case R.id.tv_check_type:
            case R.id.tv_checkactivity_sampleunit:
                showTaskDialog();
                break;
        }
    }

    private void loadTaskModel() {
        List<TaskModel> temp = hibernate.where("jcx != ?", "").queryAll(TaskModel.class);
        taskModels.clear();
        if (temp != null) {
            taskModels.addAll(temp);
        }
    }

    Dialog dialog_task_list;
    TaskListAdapter2 taskListAdapter;

    ListView task_list;
    List<TaskModel> taskModels = new ArrayList<>();
    TaskModel taskModel = null;

    private void showTaskDialog() {
        if (dialog_task_list == null) {
            loadTaskModel();
            dialog_task_list = new Dialog(this);

            View dialogContentView = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_show_task_list, null, false);
            task_list = dialogContentView.findViewById(R.id.lv_task_list);
            taskListAdapter = new TaskListAdapter2(this);
            taskListAdapter.setData(taskModels);

            dialog_task_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_task_list.setContentView(dialogContentView);

            task_list.setAdapter(taskListAdapter);
            task_list.setOnItemClickListener((parent, view, position, id) -> {
                TaskModel taskModel = taskModels.get(position);
                Timber.i("选择的任务=" + new Gson().toJson(taskModel));
                this.taskModel = taskModel;
                etJcx.setText(taskModel.getJcx());
                tv_check_sample.setText(taskModel.getSampleName());
//                resultList.get(clickPosition).taskID = taskModel.getTaskID();
                tv_check_b.setText(taskModel.getCompanyName());
//                resultList.get(clickPosition).bcheckedOrganizationCode = taskModel.getCompanyCode();
                tv_check_type.setText(taskModel.getSampleType());
//                resultList.get(clickPosition).sampleTypeCode = taskModel.getSampleTypeId();
//                resultList.get(clickPosition).sampleTypeChild = taskModel.getSampleSubType();
//                resultList.get(clickPosition).sampleTypeChildCode = taskModel.getSampleSubTypeId();
//                resultList.get(clickPosition).SamplingTime = taskModel.getSamplingTime();
//                resultList.get(clickPosition).checkedOrganization = taskModel.getJcdw();
                tv_check_company.setText(taskModel.getJcdw());
//
//                testAdapter.notifyItemChanged(clickPosition);
                dialog_task_list.dismiss();
            });
        }

        dialog_task_list.show();
        dialog_task_list.setCancelable(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    public void initView() {

        hibernate = MainActivity.hibernate;
        tv_check_b = (TextView) findViewById(R.id.tv_check_b);

        tv_check_company = (TextView) findViewById(R.id.tv_check_company);
        tv_check_persion = (TextView) findViewById(R.id.tv_check_persion);
        tv_check_sample = (TextView) findViewById(R.id.tv_check_sample);


        tv_check_type = (TextView) findViewById(R.id.tv_check_type);
        tv_check_project = (TextView) findViewById(R.id.tv_check_project);
        etJcx = (EditText) findViewById(R.id.check_edit_jcx);
        etConcentrate = (EditText) findViewById(R.id.check_edit_long);
        long_tv = (TextView) findViewById(R.id.check_edit_tv_long);

        et_Sample_Num = (EditText) findViewById(R.id.checkactivity_et_SampleNum);
        et_companyCode = findViewById(R.id.checkactivity_et_company);
        llCompany = findViewById(R.id.tr_ll);
        //判断是否需要组织机构代码
        String needCompanyCode = com.example.utils.http.Global.NEEDCompanyCode;
        if (needCompanyCode.equals("0")) {
            llCompany.setVisibility(View.GONE);
        }


        et_SampleTime = (EditText) findViewById(R.id.checkactivity_et_SampleTime);
        et_SampleTime.setOnClickListener(this);
        String time = GetCurrentTime();

        et_SampleTime.setText(time);

        etResult = (EditText) findViewById(R.id.check_edit_result);
        btn_Imm_Check = (Button) findViewById(R.id.btn_Imm_Check);
        upload_data = (Button) findViewById(R.id.upload_data);

        //为按钮设置焦点，防止进入检测界面后立即弹出键盘的行为
        btn_Imm_Check.setFocusable(true);
        btn_Imm_Check.setFocusableInTouchMode(true);
        btn_Imm_Check.requestFocus();
        btn_Imm_Check.requestFocusFromTouch();


        //上传数据按钮事件
        upload_data.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ClickUpload();
            }
        });

    }

    private void ClickUpload() {
        if (resultModel == null) {
            APPUtils.showToast(AtpCheckActivity.this, "请先检测");
            return;
        }
        if (uploadingFlag) {
            Toast.makeText(this, "正在上传数据，请稍后...", Toast.LENGTH_LONG).show();
            return;
        }
        uploadingFlag = true;

        // 新的上传
        uploadResult();
    }

    private String locationMsg = "";

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();

        try {
            while (start > -1) {
                end = dataStr.indexOf("\\u", start + 2);
                String charStr = "";
                if (end == -1) {
                    charStr = dataStr.substring(start + 2, dataStr.length());
                } else {
                    charStr = dataStr.substring(start + 2, end);
                }
                char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
                buffer.append(new Character(letter).toString());
                start = end;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 保存检测结果
     *
     * @param result
     */
    public void saveCheck_ResultData(String result) {
        try {
            long time = new Date().getTime();// new Date()为获取当前系统时间
            resultModel = new ResultModel();
            testTime = GetCurrentTime();
            resultModel.number = testTime;
            resultModel.company_name = tv_check_company.getText().toString();
            resultModel.persion = tv_check_persion.getText().toString();
//			resultModel.shiji = shiji_model.getName();
            resultModel.sample_name = tv_check_sample.getText().toString();
            resultModel.sample_number = et_Sample_Num.getText().toString();
            resultModel.sample_type = tv_check_type.getText().toString();
            resultModel.project_name = "ATP";
//            resultModel.sample_unit = sampleUnit_model.getName();
            resultModel.xian = etJcx.getText().toString();
            resultModel.lin = etJcx.getText().toString();
            resultModel.check_value = etConcentrate.getText().toString();
            resultModel.style_long = etConcentrate.getText().toString();
            resultModel.check_result = result;
            resultModel.time = time;
            resultModel.concentrateUnit = "";
            resultModel.sample_unit = taskModel.getCompanyName();
            resultModel.shiji = tv_check_b.getText().toString();
            resultModel.companyCode = et_companyCode.getText().toString();
//            resultModel.sample_id = et_Sample_Num.getText().toString();
//            resultModel.upload_status = 1;
            upload_data.setEnabled(true);
            //保存检测结果
            db.save(resultModel);
            //保存新表
            saveNewTable(resultModel);
            // 添加检测项目
//            save_projectname_to_LineModel();


        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存新表
     *
     * @param resultModel
     */
    private DetectionResultBean detectionResultBean;

    private void saveNewTable(ResultModel resultModel) {
        if (resultModel == null) {
            return;
        }
        //赋值新表
        detectionResultBean = new DetectionResultBean();
        detectionResultBean.setSQLType("ATP");
        detectionResultBean.setCheckRunningNumber(resultModel.id);//检测流水号
        detectionResultBean.setDetectionCompany(resultModel.company_name);//检测单位
        detectionResultBean.setInspector(resultModel.persion);//检测人员
        detectionResultBean.setCommodityPlaceOrigin(resultModel.sample_unit);//商品来源
        detectionResultBean.setTestItem(resultModel.project_name);//检测项目
        detectionResultBean.setSampleName(resultModel.sample_name);//样品名称
        detectionResultBean.setSpecimenType(resultModel.sample_type);//样品类型
        detectionResultBean.setSpecimenTypeCode(taskModel.getSampleTypeId());//样品类型
        detectionResultBean.setSpecimenTypeChild(taskModel.getSampleSubType());//样品类型
        detectionResultBean.setSpecimenTypeChildCode(taskModel.getSampleSubTypeId());//样品类型
        detectionResultBean.setLimitStandard(resultModel.xian);//限量标准
//        detectionResultBean.setLimitStandard(resultModel.xian + resultModel.concentrateUnit);//限量标准
//        detectionResultBean.setLimitStandard(limit_standard);//限量标准
        detectionResultBean.setCriticalValue(resultModel.lin);//临界值
        detectionResultBean.setDetectionValue(resultModel.check_value);//检测值
        detectionResultBean.setSampleConcentration(resultModel.style_long);//样品浓度
        detectionResultBean.setDetectionResult(resultModel.check_result);//检测结果
        detectionResultBean.setDetectionTime(resultModel.time);//检测时间

        // 新增
        detectionResultBean.setNumberSamples(sample_number);// 样品编号
        detectionResultBean.setUploadStatus("未上传");

        detectionResultBean.setUnitsUnderInspection(taskModel.getCompanyName()); // 被检单位
        detectionResultBean.setUnitsUnderInspectionCode(taskModel.getCompanyCode()); // 被检单位
        detectionResultBean.setAisle(""); // 通道
        detectionResultBean.setWeight(""); // 重量
        detectionResultBean.setSampleConcentration(""); // 浓度
        detectionResultBean.setOperatorId(com.example.utils.http.Global.ID); // 上传数据的OperatorId参数
        detectionResultBean.companyCode = resultModel.companyCode;
        detectionResultBean.samplingDate = taskModel.getSamplingTime();


        Log.d("zdl", "===============companyCode================" + detectionResultBean.companyCode);

//        List<DetectionResultBean> detectionResultBeans = MainActivity.hibernate.queryAll(DetectionResultBean.class);

        MainActivity.hibernate.save(detectionResultBean);
        int id = hibernate.getStatus();
        detectionResultBean.setID(id);

    }


    class MytaskTime extends TimerTask {
        @Override
        public void run() {
            recLen--;
            Message message = new Message();
            message.what = 1;
            handlerTime.sendMessage(message);
        }
    }


    TimerTask taskTime = null;
    private int recLen = 0;

    Handler handlerTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_scanTime.setText("请等待" + recLen + "S");
                    if (recLen <= 0) {
                        taskTime.cancel();
                        if (Global.DEBUG) {
                            showResult(temp);
                        } else {
                            recAndDealData();
                        }
                        //时间到了，检测
                        saveCheck_ResultData(etResult.getText().toString());
                        uploadResult();
                    }
                    break;
            }
        }
    };


    /**
     * 及时检测
     */
    public void ClickMoveNow() {
        ClickStart(null);
    }

    /**
     * 清空测试数据
     */
    private void clearTestDataShow() {
        etResult.setText("");
        etConcentrate.setText("");
    }

    private boolean isTest = false;//是否正在检测中

    /**
     * 即时检测
     *
     * @param v
     */
    public void ClickStart(View v) {

        //如果还在扫描二维码时，不许再点击即时检测
        if (isTest) {
            APPUtils.showToast(AtpCheckActivity.this, "检测中，请稍后...");
            return;
        }
        //如果样品名称为null 那就弹出对话框

        if (tv_check_sample == null || tv_check_sample.getText().toString().length() == 0) {
            showTaskDialog();
            return;
        }

        if (tv_check_company.getText() == null || tv_check_company.getText().toString().isEmpty()) {
            APPUtils.showToast(AtpCheckActivity.this, "请先选择检测单位");
            return;
        }

        if (tv_check_company.getText() == null || tv_check_company.getText().toString().isEmpty()) {
            APPUtils.showToast(AtpCheckActivity.this, "请先选择检测单位");
            tv_scanTime.setText("请先选择检测单位");
            isTest = false;
            return;
        }

        if (tv_check_persion.getText() == null || tv_check_persion.getText().toString().isEmpty()) {
            APPUtils.showToast(AtpCheckActivity.this, "请先选择检验员");
            tv_scanTime.setText("请先选择检验员");
            isTest = false;
            return;
        }

        if (tv_check_sample.getText() == null || tv_check_sample.getText().toString().isEmpty()) {
            APPUtils.showToast(AtpCheckActivity.this, "请先选样品名称");
            tv_scanTime.setText("请先选样品名称");
            isTest = false;
            return;
        }
        if (tv_check_type.getText() == null || tv_check_type.getText().toString().isEmpty()) {
            APPUtils.showToast(AtpCheckActivity.this, "请先选样品类型");
            tv_scanTime.setText("请先选样品类型");
            isTest = false;
            return;
        }

        GT.Thread.runAndroid(AtpCheckActivity.this, new Runnable() {
            @Override
            public void run() {
                isTest = false;
                //清空测试数据显示
                clearTestDataShow();

                // 发送绘图命令
                ClickDraw1();
            }
        });
    }

    public static int REQUEST_CODE = 0;

    /**
     * 打印数据
     *
     * @param v
     */
    public void PrintInfo(View v) {

        if (resultModel == null) {
            Toast.makeText(this, "请先检测", Toast.LENGTH_LONG).show();
            return;
        }

        String message = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String printData = null;
        printData = ToolUtils.GetPrintInfo3(resultModel);
//        APPUtils.showToast(this, printData);
        byte[] data = printData.getBytes(Charset.forName("gb2312"));
        Timber.i("data="+new String(data,Charset.forName("gbk")));
        if (!SerialUtils.COM4_SendData(data)) {
            APPUtils.showToast(this, "打印数据发送失败");
        }
    }

    private Timer timer = null;


    private final int BUFSIZE = 1024;
    private StringBuilder strResultBuffer = null;
    private String strResult = null;
    public boolean isScanSuccess = false;


    private void uploadResult() {
        if (detectionResultBean != null) {
            List<DetectionResultBean> list_upload = new ArrayList<>();
            list_upload.add(detectionResultBean);
            UploadThread2 uploadThread2 = new UploadThread2(this, list_upload, new UploadThread2.onUploadListener() {
                @Override
                public void onUploadSuccess(int position, String msg) {
                    list_upload.get(position).setUploadID(msg);
                    list_upload.get(position).setUploadStatus("已上传");
                    list_upload.get(position).setSelect(false);
                    hibernate.update(list_upload.get(position));
                    runOnUiThread(() -> {
                        com.open.soft.openappsoft.util.APPUtils.showToast(AtpCheckActivity.this, "上传成功");
                    });
                }

                @Override
                public void onUploadFail(int position, String failInfo) {
                    runOnUiThread(() -> {
                        com.open.soft.openappsoft.util.APPUtils.showToast(AtpCheckActivity.this, "上传失败：" + failInfo);
                    });
                }

                @Override
                public void onUploadFinish(int count, int successCount, int failedCount) {
//                    runOnUiThread(() -> {
//                        MessageDialog.show("提示", "上传完成,共上传" + count + "条" + "，成功" + successCount + "条" + "，失败" + failedCount + "条", "确定");
//                    });
                }
            });
            uploadThread2.start();
        } else {
            com.open.soft.openappsoft.util.APPUtils.showToast(this, "未检测");
        }
    }

    public boolean isNumeric(String str) {

        {
            char c = str.charAt(0);
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                return true;
            } else {
                return false;
            }
        }
    }

    private ProgressDialog progressDialog;

    public void ClickBack(View v) {
        showBack();
    }

    private void showBack() {
        if (isTest) {
            new AlertDialog.Builder(this)
                    .setMessage("正在检测中，确定要取消检测吗？")
                    .setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AtpCheckActivity.this.back();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        } else {
            AtpCheckActivity.this.back();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        showBack();
    }


    public String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.checkactivity_et_SampleTime) {
            showTaskDialog();
        } else if (v.getId() == R.id.move_time) {
            timeCheck();
        }
    }

    /**
     * 定时检测
     */
    private void timeCheck() {
        recLen = Global.DEBUG ? 3 : reactionTime;
        taskTime = new MytaskTime();
        timer = new Timer();
        timer.schedule(taskTime, 0, 1000);
    }


    private String GetCurrentTime() {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式

        return dateFormat.format(now);
    }

    String temp = "OK0.3\n";

    public void ClickDraw1() {
        if (isTest) {
            APPUtils.showToast(this, "正在检测，请稍后...");
            return;
        }

        final String message = "GetATPData";

        if (com.open.soft.openappsoft.multifuction.util.Global.DEBUG) {
            //测试
            recLen = 3;
            timeCheck();
            return;
        }
        byte[] data = message.getBytes(Charset.forName("gb2312"));
        if (!SerialUtils.COM3_SendData(data)) {
            isTest = false;
            APPUtils.showToast(this, "数据发送失败");
            return;
        }
    }

    private final static int what_test_success = 700;
    private final static int what_test_fail = 600;

    private void showResult(String str) {
        Timber.d("showResult str=" + str);
        String drValue = str.replace("OK", "").replace("\n", "");
        String finalDrValue = drValue;
        String jcx = etJcx.getText().toString();
        runOnUiThread(() -> {
            if (Float.parseFloat(finalDrValue) > Float.parseFloat(jcx)) {
                etConcentrate.setText(finalDrValue);
                etResult.setText("不合格");
            } else {
                etConcentrate.setText(finalDrValue);
                etResult.setText("合格");
            }
        });
    }


    private Handler handlerMess1 = new Handler() {
        @Override
        @SuppressLint("HandlerLeak")//
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case what_test_success:
                    tv_scanTime.setText("检测成功");
                    isTest = false;
                    upload_data.setEnabled(true);
                    break;
                case what_test_fail:
                    tv_scanTime.setText("检测失败");
                    isTest = false;
                    upload_data.setEnabled(false);
                    break;
            }
        }
    };

    private void recAndDealData() {
        byte[] rec = SerialUtils.COM3_RevData();
        if (rec == null || rec.length == 0 || rec[rec.length - 1] != '\n') {
            handlerMess1.sendEmptyMessage(what_test_fail);
        } else {
            String result = new String(rec);
            try {
                final String finalResult = result;
                showResult(finalResult);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                handlerMess1.sendEmptyMessage(what_test_fail);
            }
        }
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        MyApplication.keepScreenOn(getApplicationContext(), true);
        super.onResume();
    }
}
