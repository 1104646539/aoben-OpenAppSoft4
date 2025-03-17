package com.open.soft.openappsoft.multifuction.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.http.CheckPresenter;
import com.example.utils.http.GetQRInfoBean;
import com.example.utils.http.GetQRInfoResultBean;
import com.example.utils.http.GetSamplingInfoBean;
import com.example.utils.http.GetSamplingInfoResultBean;
import com.example.utils.http.Result;
import com.example.utils.http.StatusDialog;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean1;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean2;
import com.open.soft.openappsoft.jinbiao.util.APPUtils;
import com.open.soft.openappsoft.jinbiao.util.SerialUtils;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.open.soft.openappsoft.multifuction.adapter.TestAdapter;
import com.open.soft.openappsoft.multifuction.model.CheckDiDingResult;
import com.open.soft.openappsoft.multifuction.model.CheckResult;
import com.open.soft.openappsoft.util.InterfaceURL;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TitrationTestActivity extends AppCompatActivity implements StatusDialog.OnProgressStatusChange, CheckPresenter.CheckInterface {

    private TextView tv_number;
    private TextView tv_mode;
    private TextView tv_national_limit;
    private EditText ed_et_SampleNum;
    private TextView tv_sample_name;
    private TextView tv_sample_source;
    private TextView tv_tested_unit;
    private TextView tv_result;
    private TextView tv_scanTime;
    private EditText et_drop_volume;

    private EditText ed_sample_name;
    private EditText ed_sample_source;
    private EditText ed_tested_unit;

    private StatusDialog statusDialog;
    private CheckPresenter checkPresenter;
    private String card_number;
    private List<CheckResult> resultList = new ArrayList<>();
    private TestAdapter testAdapter;
    private static final String TAG = TitrationTestActivity.class.getSimpleName();


    private Spinner input_method;

    private Button btn_test;
    private Button btn_print;
    private Button btn_reset;
    private Button btn_back;

    // 保存检测结果
    private ResultModel resultModel;
    private String source = null;


    // 输入方式
    private ArrayAdapter<String> adapter;
    private static final String[] m = {"信息获取方式", "样品扫码", "手工填写编号", "手工填写样品信息"};


    private LocationReceiver locationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titration_test);

        // 隐藏页面上的标题
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


        statusDialog = new StatusDialog(this);
        statusDialog.setOnProgressStatus(this);

        checkPresenter = new CheckPresenter();

        source = getIntent().getStringExtra("source");

        // 初始化控件
        initcontrol();


        // 设置页面显示信息
        receivedata();


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
                    Toast.makeText(TitrationTestActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    public void initcontrol() {
        tv_number = (TextView) findViewById(R.id.tv_titration_number);
        tv_mode = (TextView) findViewById(R.id.tv_titration_mode);
        tv_national_limit = (TextView) findViewById(R.id.tv_titration_national_limit);
        ed_et_SampleNum = (EditText) findViewById(R.id.ed_titration_et_SampleNum);
        tv_sample_name = (TextView) findViewById(R.id.tv_titration_sample_name);
        tv_sample_source = (TextView) findViewById(R.id.tv_titration_sample_source);
        tv_tested_unit = (TextView) findViewById(R.id.tv_titration_tested_unit);
        tv_result = (TextView) findViewById(R.id.tv_titrationTest_result);
        tv_scanTime = (TextView) findViewById(R.id.tv_titration_scanTime);
        et_drop_volume = (EditText) findViewById(R.id.et_drop_volume);

        ed_sample_name = (EditText) findViewById(R.id.ed_titration_sample_name);
        ed_sample_source = (EditText) findViewById(R.id.ed_titration_sample_source);
        ed_tested_unit = (EditText) findViewById(R.id.ed_titration_tested_unit);

        btn_test = (Button) findViewById(R.id.btn_titration_test);
        btn_print = (Button) findViewById(R.id.btn_titration_print);
        btn_reset = (Button) findViewById(R.id.btn_titration_reset);
        btn_back = (Button) findViewById(R.id.btn_titration_back);

        input_method = (Spinner) findViewById(R.id.spinner_input_methods);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        input_method.setAdapter(adapter);

        //添加事件Spinner事件监听
        input_method.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        input_method.setVisibility(View.VISIBLE);
        Onclick onClick = new Onclick();

        // 不主动获取软件盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_et_SampleNum.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_drop_volume.getWindowToken(), 0);

        ed_et_SampleNum.clearFocus();
        et_drop_volume.clearFocus();

        btn_test.setOnClickListener(onClick);
        btn_print.setOnClickListener(onClick);
        btn_reset.setOnClickListener(onClick);
        btn_back.setOnClickListener(onClick);

        tv_sample_name.setOnClickListener(onClick);
        tv_sample_source.setOnClickListener(onClick);
        tv_tested_unit.setOnClickListener(onClick);
        tv_tested_unit.setOnClickListener(onClick);
        tv_number.setOnClickListener(onClick);


    }

    @Override
    public void onProgressStatusChange(int status) {


    }

    @Override
    public void onCancel(int status) {
        statusDialog.dismiss();
    }

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
//                    try {
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
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
////                        GT.errs("从网上扒数据异常：" + e);
//                    }
                }
            });

            return;
        }

        scanParameterSuccess(number);

    }


    String sample_name;
    String sample_source;
    String tested_unit;
    String sample_type;

    @Override
    public void GetSamplingInfoSuccess(Result<GetSamplingInfoResultBean> result, int requestCode) {

        if (statusDialog != null) {
            statusDialog.dismiss();
        }


        sample_name = result.getData().getSamplingName();
        sample_source = result.getData().getSamplingSource();
        tested_unit = result.getData().getSamplingDept();
        sample_type = result.getData().getSamplingType();

        // 设置页面数据
        tv_sample_name.setText(result.getData().getSamplingName());
        tv_sample_source.setText(result.getData().getSamplingSource());
        tv_tested_unit.setText(result.getData().getSamplingDept());

        // 清空滴数和上一次检测结果
        et_drop_volume.setText("");
        tv_result.setText("");
//        }

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

    @Override
    public void GetCardQRInfoSuccess(Result<GetQRInfoResultBean> result, int requestCode) {


        Bundle bundle = new Bundle();
        bundle.putString("QRCode", result.getData().getQRCode());

        // 获取TestValue和JudgeRegion对应的值
        for (int i = 0; i < result.getData().getAlgorithmPara().size(); i++) {
            GetQRInfoResultBean.AlgorithmParaBean lif = result.getData().getAlgorithmPara().get(i);
            if (lif.getTitle().equals("TestValue")) {
                bundle.putString("TestValue", lif.getValue());
            } else if (lif.getTitle().equals("JudgeRegion")) {
                bundle.putString("JudgeRegion", lif.getValue());
            } else if (lif.getTitle().equals("WaveLength")) {
                bundle.putString("WaveLength", lif.getValue());
            }
        }

        // 获取返回信息
        for (int i = 0; i < result.getData().getListInfo().size(); i++) {
            GetQRInfoResultBean.ListInfoBean lif = result.getData().getListInfo().get(i);
            if (lif.getTitle().equals("检测项目")) {
                bundle.putString("检测项目", lif.getValue());
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
            }
        }

        // 如果不是滴定法就跳转到普通检测页面
        for (int i = 0; i < result.getData().getListInfo().size(); i++) {
            GetQRInfoResultBean.ListInfoBean lif = result.getData().getListInfo().get(i);
            if (lif.getTitle().equals("判读方法")) {
                if (!"滴定法".equals(lif.getValue())) {
                    Intent intent = new Intent(TitrationTestActivity.this, PesticideTestActivity2.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    statusDialog.dismiss();
                } else {
                    Intent intent = new Intent(TitrationTestActivity.this, TitrationTestActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    statusDialog.dismiss();
                }
            }
        }


    }

    @Override
    public void GetCardQRInfoFailed(String msg, int requestCode) {

        statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_CARD);
        com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "验证检测卡/试剂盒编号失败:" + msg);
    }

    boolean isFinishing = false;

    @Override
    public void SendResultSuccess(String msg, int requestCode) {

        if (!isFinishing) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "上传成功");
        }
    }

    @Override
    public void SendResultFailed(String msg, int requestCode) {

        com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "A" + (requestCode + 1) + "上传失败" + msg);
    }

    // 输入模式标志位
    int position1;

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


//            try {
//                //以下三行代码是解决问题所在
//                Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
//                field.setAccessible(true);	//设置mOldSelectedPosition可访问
//                field.setInt(input_method, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            position1 = position;
            switch (position) {
                case 1:
                    // 直接调用检测页面的二维码扫描方法

                    tv_sample_name.setVisibility(View.VISIBLE);
                    tv_sample_source.setVisibility(View.VISIBLE);
                    tv_tested_unit.setVisibility(View.VISIBLE);

                    ed_sample_name.setVisibility(View.GONE);
                    ed_sample_source.setVisibility(View.GONE);
                    ed_tested_unit.setVisibility(View.GONE);

                    changeCardNumber(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);

                    break;
                case 2:
                    tv_sample_name.setVisibility(View.VISIBLE);
                    tv_sample_source.setVisibility(View.VISIBLE);
                    tv_tested_unit.setVisibility(View.VISIBLE);

                    ed_sample_name.setVisibility(View.GONE);
                    ed_sample_source.setVisibility(View.GONE);
                    ed_tested_unit.setVisibility(View.GONE);


                    break;
                case 3:
                    tv_sample_name.setVisibility(View.GONE);
                    ed_sample_name.setVisibility(View.VISIBLE);

                    tv_sample_source.setVisibility(View.GONE);
                    ed_sample_source.setVisibility(View.VISIBLE);

                    tv_tested_unit.setVisibility(View.GONE);
                    ed_tested_unit.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_titration_test:
                    test();
                    break;
                case R.id.btn_titration_print:
                    print();
                    break;
                case R.id.btn_titration_reset:
                    reset();
                    break;
                case R.id.btn_titration_back:
                    back();
                    break;

                // 调用扫描二维码窗口
                case R.id.tv_titration_sample_name:
                    changeCardNumber(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
                    break;
                case R.id.tv_titration_sample_source:
                    changeCardNumber(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
                    break;
                case R.id.tv_titration_tested_unit:
                    changeCardNumber(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);
                    break;
                case R.id.tv_titration_number:
                    changeCardNumber(StatusDialog.STATUS_START_SCAN_QRCODE_CARD);
                    break;
            }
        }
    }


    String qrcode;
    String num;
    String sampleid;
    String testresult;
    String samlename;
    String samplesource;
    String testedunit;
    String result;

    // 检测
    public void test() {

        qrcode = tv_number.getText().toString();
        num = et_drop_volume.getText().toString();
        sampleid = ed_et_SampleNum.getText().toString();
        testresult = tv_result.getText().toString();

        // 不同获取信息方式下的操作
        if (position1 == 0 || position1 == 1 || position1 == 2) {
            samlename = tv_sample_name.getText().toString();
            samplesource = tv_sample_source.getText().toString();
            testedunit = tv_tested_unit.getText().toString();
        } else {
            samlename = ed_sample_name.getText().toString();
            samplesource = ed_sample_source.getText().toString();
            testedunit = ed_tested_unit.getText().toString();
        }

        // 空值判断
        if ("".equals(sampleid) || sampleid == null) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "请输入样品编号");
            return;
        } else if ("".equals(samlename) || samlename == null) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "请输入样品名称");
            return;
        } else if ("".equals(samplesource) || samplesource == null) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "请输入样品来源");
            return;
        } else if ("".equals(testedunit) || testedunit == null) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "请输入被检测单位");
            return;
        } else if ("".equals(num) || num == null) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "请输入滴加试剂总量");
            return;
        }

        // 判定合格/不合格
        try {
            Integer num1 = new Integer(num);

            // 判断是否合格
            String objStr = JudgeRegion.substring(JudgeRegion.indexOf("(") + 1, JudgeRegion.indexOf(")"));

            if (objStr.endsWith(",")) {

                objStr = objStr.trim();
                String str2 = "";
                if (objStr != null && !"".equals(objStr)) {
                    for (int i = 0; i < objStr.length(); i++) {
                        if (objStr.charAt(i) >= 48 && objStr.charAt(i) <= 57) {
                            str2 += objStr.charAt(i);
                        }
                    }
                }

                Integer m1 = new Integer(String.valueOf(str2));

                if (num1 <= m1) {

                    tv_result.setText("不合格");
                    result = "不合格";
                } else {
                    tv_result.setText("合格");
                    result = "合格";
                }
            } else if (objStr.startsWith(",")) {

                objStr = objStr.trim();
                String str2 = "";
                if (objStr != null && !"".equals(objStr)) {
                    for (int i = 0; i < objStr.length(); i++) {
                        if (objStr.charAt(i) >= 48 && objStr.charAt(i) <= 57) {
                            str2 += objStr.charAt(i);
                        }
                    }
                }

                Integer m2 = new Integer(String.valueOf(str2));

                if (num1 < m2) {
                    tv_result.setText("合格");
                    result = "合格";
                } else {
                    tv_result.setText("不合格");
                    result = "不合格";
                }
            }
        } catch (Exception e) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "输入滴数数据格式有误");
        }

        getUploadData2();
    }

    // 打印
    public void print() {


        // 打印前冲重新获取页面信息
        String qrcode1 = tv_number.getText().toString();
        String num1 = et_drop_volume.getText().toString();
        String sampleid1 = ed_et_SampleNum.getText().toString();
        String testresult1 = tv_result.getText().toString();


        String samlename1;
        String samplesource1;
        String testedunit1;

        if (position1 == 0 || position1 == 1 || position1 == 2) {
            samlename1 = tv_sample_name.getText().toString();
            samplesource1 = tv_sample_source.getText().toString();
            testedunit1 = tv_tested_unit.getText().toString();
        } else {
            samlename1 = ed_sample_name.getText().toString();
            samplesource1 = ed_sample_source.getText().toString();
            testedunit1 = ed_tested_unit.getText().toString();
        }

        if (result == null || "".equals(result) || "".equals(testresult1) || testedunit1 == null) {
            APPUtils.showToast(this, "请先检测");
            return;
        }

        // 将打印前获取的数据和检测之后的数据做比较，如果有改变，就再检测，如果没有变化就直接打印

        if (
                sampleid1.equals(sampleid) && testresult1.equals(result) && samlename1.equals(samlename)
                        && samplesource1.equals(samplesource) && testedunit1.equals(testedunit)
                        && num1.equals(num)
        ) {

            CheckDiDingResult checkDiDingResult = new CheckDiDingResult();
            checkDiDingResult.qrcode = qrcode;
            checkDiDingResult.testmethod = judgment_method;
            checkDiDingResult.nationallimit = nationallimit;
            checkDiDingResult.sampleid = sampleid;
            checkDiDingResult.samlename = samlename;
            checkDiDingResult.samplesource = samplesource;
            checkDiDingResult.testedunit = testedunit;
            checkDiDingResult.num = num;
            checkDiDingResult.result = result;
            checkDiDingResult.testTime = System.currentTimeMillis();


            String message = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间

            String printData = null;
            printData = GetPrintInfo1(checkDiDingResult, this, source);

            APPUtils.showToast(this, printData);
            byte[] data = printData.getBytes(Charset.forName("gb2312"));
            if (!SerialUtils.COM4_SendData(data)) {
                APPUtils.showToast(this, "打印数据发送失败");
            } else {
                APPUtils.showToast(this, "打印数据发送成功");
            }
        } else {
            APPUtils.showToast(this, "数据已修改，请先检测");

            return;
        }


    }

    // 重置
    public void reset() {
        ed_et_SampleNum.setText("");
        tv_sample_name.setText("");
        tv_sample_source.setText("");
        tv_tested_unit.setText("");
        tv_result.setText("");
        et_drop_volume.setText("");

        ed_sample_name.setText("");
        ed_sample_source.setText("");
        ed_tested_unit.setText("");
    }

    // 返回
    public void back() {
        finish();
    }


    String TestValue;
    String JudgeRegion;
    String judgment_method;
    String nationallimit;


    // 接收分光光度页面跳转到滴定法页面发送的数据并设置在页面中
    public void receivedata() {
        Intent intent = getIntent();
//        Intent intent = getIntent().getExtras();
        judgment_method = intent.getStringExtra("判读方法");
        String national_standard = intent.getStringExtra("国家标准");
        String national_standard_unit = intent.getStringExtra("国家标准单位");
        String test_items = intent.getStringExtra("检测项目");
        String test_method = intent.getStringExtra("检测方法");
        String QRCode = intent.getStringExtra("QRCode");
        TestValue = intent.getStringExtra("TestValue");
        JudgeRegion = intent.getStringExtra("JudgeRegion");
        nationallimit = national_standard + national_standard_unit;

        tv_scanTime.setText(test_items);
        tv_number.setText(QRCode);
        tv_mode.setText(judgment_method);
        tv_national_limit.setText(national_standard + national_standard_unit);

    }

    private int clickPosition = -1;

    //单击文本框 请求扫描二维码
    private void changeCardNumber(int num) {
        statusDialog.setStatus(num);
        statusDialog.show();
    }

    String sampleCode;

    //扫描二维码后，点击对话框上的确定按钮后执行的方法
    private void scanParameterSuccess(final String temp) {

        if (temp == null || temp.contains("OK2")) {
            TitrationTestActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TitrationTestActivity.this, "当前网络不佳，请从新扫描。", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

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


                        // 设置编号
                        if (position1 == 1 || position1 == 0) {
                            ed_et_SampleNum.setText(temp2);
                        }


                        sampleCode = temp2;
                        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_SAMPLE);
                        checkPresenter.GetSamplingInfo(new GetSamplingInfoBean(temp,com.example.utils.http.Global.admin_pt), clickPosition, TitrationTestActivity.this);
                        statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_SAMPLE);
//                        et_Sample_Num.setText(temp);
                    } else if (statusDialog.getStatus() == StatusDialog.STATUS_START_SCAN_QRCODE_CARD
                            || statusDialog.getStatus() == StatusDialog.STATUS_END_SCAN_QRCODE_CARD
                            || statusDialog.getStatus() == StatusDialog.STATUS_START_VERITY_QRCODE_CARD
                            || statusDialog.getStatus() == StatusDialog.STATUS_END_VERITY_QRCODE_CARD) {


                        card_number = temp2;
                        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_CARD);
                        checkPresenter.GetCardQRInfo(new GetQRInfoBean(temp2), 1, TitrationTestActivity.this);//扫描二维码，向后台请求数据
                        statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_CARD);
                    }
                }

            }
        });
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

    // 数据上传
    private void getUploadData2() {

        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;

        // 如果选择手动输入模式输入数据，要上传的数据
        if (position1 == 3) {
            sample_name = ed_sample_name.getText().toString();
            sample_source = ed_sample_source.getText().toString();
            tested_unit = ed_tested_unit.getText().toString();
        }

        //将具体检测文字 转为 编码
        String result = "";
        if ("合格".equals(tv_result.getText().toString())) {
            result = "0";
        } else {
            result = "2";
        }

        String data = "{\n" +
                "\"QRCode\":\"" + qrcode + "\",\n" +
                "\"LocationX\":\"" + LocationX + "\",\n" +
                "\"LocationY\":\"" + LocationY + "\",\n" +
                "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                "\"Result\":\"" + result + "\",\n" +
                "\"ResultValue\":\"" + num + "\",\n" +
                "\"SamplingNumber\":\"" + sampleid + "\",\n" +
                "\"AreaId\":\"" + AreaId + "\",\n" +
                "\"OperatorId\":\"" + OperatorId + "\",\n" +
                "\"ResultData\":\n" +
                "[\n" +
                "{\"Title\":\"" + "样品名称" + "\",\"Id\":\"" + "SamplingName" + "\",\"Value\":\"" + sample_name + "\"},\n" +
                "{\"Title\":\"" + "被检测单位" + "\",\"Id\":\"" + "SamplingDept" + "\",\"Value\":\"" + tested_unit + "\"},\n" +
                "{\"Title\":\"" + "样品来源" + "\",\"Id\":\"" + "SamplingSource" + "\",\"Value\":\"" + sample_source + "\"}\n" +
                "]\n" +
                "}";

//        EditURLDialog editURLDialog = new EditURLDialog(TitrationTestActivity.this);

        // 从本地获取网址
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + "QR/SendResult")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(TitrationTestActivity.this, "上传失败");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(TitrationTestActivity.this, "上传成功");
                TestDataBean1 testDataBean1 = new Gson().fromJson(ret, TestDataBean1.class);
                if (testDataBean1 != null) {
                    List<TestDataBean2> data1 = testDataBean1.getData();
                    if (data1 != null) {
                        TestDataBean2 testDataBean2 = data1.get(0);
                        if (testDataBean2 != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TitrationTestActivity.this, testDataBean2.getErrMsg(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + "QR/SendResult", data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(TitrationTestActivity.this, "上传成功");
//                TestDataBean1 testDataBean1 = new Gson().fromJson(response, TestDataBean1.class);
//                if (testDataBean1 != null) {
//                    List<TestDataBean2> data1 = testDataBean1.getData();
//                    if (data1 != null) {
//                        TestDataBean2 testDataBean2 = data1.get(0);
//                        if (testDataBean2 != null) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(TitrationTestActivity.this, testDataBean2.getErrMsg(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                    }
//
//                }
//
//
//            }
//
//            @Override
//            public void onError(String response) {
//                com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(TitrationTestActivity.this, "上传失败");
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 注销定位监听
        try {
            unregisterReceiver(locationReceiver);
        } catch (Exception e) {
        }
    }

    public static String GetPrintInfo1(CheckDiDingResult checkDiDingResult, Context context, String testType) {
        StringBuffer sb = new StringBuffer("\n\n\n\n\n" + "         滴定法检测报告" + "\n\n");

        sb.append("试剂盒编号：");
        sb.append(checkDiDingResult.qrcode + "\n");
        sb.append("检测方法：  ");
        sb.append(checkDiDingResult.testmethod + "\n");
        sb.append("国家限量：  ");
        sb.append(checkDiDingResult.nationallimit + "\n");
        sb.append("样品编号：  ");
        sb.append(checkDiDingResult.sampleid + "\n");
        sb.append("样品名称：  ");
        sb.append(checkDiDingResult.samlename + "\n");
        sb.append("样品来源：  ");
        sb.append(checkDiDingResult.samplesource + "\n");
        sb.append("被检测单位：");
        sb.append(checkDiDingResult.testedunit + "\n");
        sb.append("试剂添加量：");
        sb.append(checkDiDingResult.num + "滴" + "\n");
        sb.append("检测结果：  ");
        sb.append(checkDiDingResult.result + "\n");
        sb.append("检测时间：  ");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                checkDiDingResult.testTime, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");
        sb.append("\n\n\n\n\n");

        return sb.toString();
    }


}
