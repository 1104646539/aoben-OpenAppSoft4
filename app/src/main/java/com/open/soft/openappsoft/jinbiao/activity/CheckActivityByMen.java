package com.open.soft.openappsoft.jinbiao.activity;

import static com.open.soft.openappsoft.multifuction.util.SerialUtils.CardOutOrIn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrcodescan.QRCode;
import com.example.utils.http.Barcode2D;
import com.example.utils.http.CheckPresenter;
import com.example.utils.http.GetQRInfoBean;
import com.example.utils.http.GetQRInfoResultBean;
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
import com.lzy.okgo.callback.StringCallback;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.activity.SearchActivity;
import com.open.soft.openappsoft.bean.AddComBean;
import com.open.soft.openappsoft.dialog.DialogFragmentPaint;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.jinbiao.dialog.DateTimePickerDialog;
import com.open.soft.openappsoft.jinbiao.location.LocationService;
import com.open.soft.openappsoft.jinbiao.model.CardCompanyModel;
import com.open.soft.openappsoft.jinbiao.model.Data;
import com.open.soft.openappsoft.jinbiao.model.JsonRootBean;
import com.open.soft.openappsoft.jinbiao.model.LineModel;
import com.open.soft.openappsoft.jinbiao.model.LinkageData;
import com.open.soft.openappsoft.jinbiao.model.LinkageJsonRootBean;
import com.open.soft.openappsoft.jinbiao.model.NewSampleNameData;
import com.open.soft.openappsoft.jinbiao.model.NewSampleNameDataBean;
import com.open.soft.openappsoft.jinbiao.model.PeopleModel;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.open.soft.openappsoft.jinbiao.model.SampleModel;
import com.open.soft.openappsoft.jinbiao.model.SampleSourceData;
import com.open.soft.openappsoft.jinbiao.model.SampleSourceJsonRootBean;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeData;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeJsonRootBean;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeModel;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.jinbiao.model.ShiJiModel;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean1;
import com.open.soft.openappsoft.jinbiao.util.APPUtils;
import com.open.soft.openappsoft.jinbiao.util.Global;
import com.open.soft.openappsoft.jinbiao.util.SerialUtils;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.open.soft.openappsoft.multifuction.activity.InputMessageDialog;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;
import com.open.soft.openappsoft.util.InterfaceURL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class CheckActivityByMen extends BaseActivity implements OnClickListener, CheckPresenter.CheckInterface, StatusDialog.OnProgressStatusChange, OnItemSelectedListener {

    /**
     * 读取数据超时次数，如果超过3次，则终止此次检测
     */
    private String needCompanyCode;
    private int readTimeOutCount = 0;
    private Spinner companySpinner = null;
    private Spinner persionSpinner = null;
    private Spinner shijiSpinner = null;
    private Spinner sampleSpinner = null;
    private Spinner projectSpinner = null;
    private Spinner typeSpinner = null;
    private Spinner sampleUnitSpinner = null;

    private EditText etJcx = null;
    private EditText etLjz = null;
    private EditText etDr = null;
    private EditText etConcentrate = null;
    private EditText etResult = null;
    private TextView long_tv = null;
    private TextView tv_checkactivity_sampleunit = null;
    private TextView tv_check_b = null;


    private EditText et_Sample_Num = null;
    private TextView et_companyCode;
    private EditText et_SampleTime = null;
    private TableRow llCompany;
    private ImageView ivSearchCheck=null;

    private String[] company_list = null;
    private String[] persion_list = null;
    private String[] shiji_list = null;
    private String[] sample_list = null;
    private String[] project_list = null;
    private String[] type_list = null;
    private String[] sampleUnit_list = null;

    private List<PeopleModel> persionlist = null;
    private List<PeopleModel> companylist = null;
    private List<ShiJiModel> shijilist = null;
    private List<SampleModel> samplelist = null;
    private List<LineModel> projectlist = null;
    private List<SampleTypeModel> typelist = null;
    private List<PeopleModel> sampleUnitList = null;

    private ArrayAdapter<String> company_adater = null;
    private ArrayAdapter<String> persion_adapter = null;
    private ArrayAdapter<String> shiji_adapter = null;
    private ArrayAdapter<String> sample_adapter = null;
    private ArrayAdapter<String> project_adapter = null;
    private ArrayAdapter<String> type_adapter = null;
    private ArrayAdapter<String> sampleUnit_adapter = null;

//    private Spinner sp_yplx;

    private Button upload_data;
    private Button btn_Imm_Check;
    private DbUtils db;

    private Button btn_location;

    private final String TAG = CheckActivity.class.getSimpleName();

    //    private static final String ACTION_USB_PERMISSION = "com.open.soft.openappsoft.jinbiao";
    private Button move_time;
    private final int MSG_USB_GETDATA = 0xCC;

    private boolean isGet = false;

    private int baurt = 9600;

    /**
     * 正在上传的标志，指示是否正在进行上传操作
     */
    private Boolean uploadingFlag = false;

    public LineModel selectedProject = null;
    public PeopleModel company_model = null;
    public PeopleModel persion_model = null;
    public ShiJiModel shiji_model = null;
    public SampleModel sample_model = null;
    public SampleTypeModel type_model = null;
    public PeopleModel sampleUnit_model = null;

    private String source = null;
    String testTime = null;

    String s = null;
    private ResultModel resultModel;
    private StatusDialog statusDialog;

    private CheckPresenter checkPresenter;
    //qrcode
    private Barcode2D barcode2D = null;
    private boolean usbDialogFinish = true;
    /**
     * 每次扫码成功都会返回的 T /r/n 屏蔽掉
     */
    private byte[] T = new byte[]{84, 6, 13, 10};
    /**
     * 每次关闭扫码功能（超时）都会返回的 U /r/n 屏蔽掉
     */
    private byte[] U = new byte[]{85, 6, 13, 10};
    /**
     * 每次扫码成功都会返回的 T /r/n 屏蔽掉
     */
    private byte[] T_Not_N = new byte[]{84, 6};
    /**
     * 每次关闭扫码功能（超时）都会返回的 U /r/n 屏蔽掉
     */
    private byte[] U_Not_N = new byte[]{85, 6};

    //region usb广播具体操作，暂时不用
    private PendingIntent mPermissionIntent = null;
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

    // 混合录入模式
    private Button mode_exchange;

    // 绘图控件
    private LineChart mChart;
    private byte[] imageData;
    private Button btn_paint1;

    private TextView tv_title_mode2;

    //获取组件
    @GT.Annotations.GT_View(R.id.sp1)
    private Spinner sp1;
    @GT.Annotations.GT_View(R.id.sp2)
    private Spinner sp2;
    @GT.Annotations.GT_View(R.id.sp3)
    private Spinner sp3;

    @GT.Annotations.GT_View(R.id.et_jcxxdz)
    private TextView et_jcxxdz;


    //初始化集合
    @GT.Annotations.GT_Collection.GT_List
    private List<LinkageData> provinceList;
    @GT.Annotations.GT_Collection.GT_List
    private List<LinkageData> cityList;
    @GT.Annotations.GT_Collection.GT_List
    private List<LinkageData> areaList;
    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map;


    // 样品类型相关初始化
    @GT.Annotations.GT_View(R.id.sp_yplx_normal)
    private Spinner sp_yplx_normal;
    @GT.Annotations.GT_View(R.id.sp_yplx)
    private Spinner sp_yplx;
    @GT.Annotations.GT_View(R.id.sp_yplx1)
    private Spinner sp_yplx1;
    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map1;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleTypeData> sampletyelist1;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleTypeData> sampletyelist2;

    // 新样品来源相关初始化
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleSourceData> sampleSources1;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleSourceData> sampleSources2;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleSourceData> sampleSources3;

    //省市区的ID
    private String spId1, spId2, spId3;
    // 样品类别Id
    private String yplbId;
    // 样品来源地址
    private EditText et_yplydz;

    // 唐山专用下拉列表
    @GT.Annotations.GT_View(R.id.ll_yplx_ts)
    private LinearLayout ll_yplx_ts;

    // 其他情况
    @GT.Annotations.GT_View(R.id.ll_yplx)
    private LinearLayout ll_yplx;

    public static String sampleId;//样品总分类Id

    /**
     * 扫描参数成功
     *
     * @param temp
     */

    private String QRCode22 = "";


    private void scanParameterSuccess(final String temp) {

        if(temp == null || temp.contains("OK2")){
            CheckActivityByMen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CheckActivityByMen.this,"当前网络不佳，请从新扫描。",Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        QRCode22 = temp;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(CheckActivity.this, "scanParameterSuccess=" + temp, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "scanParameterSuccess temp=" + temp);

                String temp2 = temp.replaceAll("\r", "");
                temp2 = temp2.replaceAll("\n", "");
                temp2 = temp.replaceAll("\\s*", "");
                if (statusDialog != null) {
                    cardCode = temp2;
                    statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_CARD);
                    checkPresenter.GetCardQRInfo(new GetQRInfoBean(cardCode), 1, CheckActivityByMen.this);
                    statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_CARD);
                }
            }
        });
    }


    /**
     * 获得Samp ling信息成功
     *
     * @param result
     * @param requestCode
     */
    @Override
    public void GetSamplingInfoSuccess(Result<GetSamplingInfoResultBean> result, int requestCode) {
        Log.d(TAG, "GetSamplingInfoSuccess result=" + result + " requestCode=" + requestCode);
        statusDialog.setStatus(StatusDialog.STATUS_END_VERITY_QRCODE_SAMPLE);
        setSampleInfo(result);
    }

    private void setSampleInfo(Result<GetSamplingInfoResultBean> result) {
        // TODO
        statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_CARD);//隐藏弹出检测卡对话框扫描
        statusDialog.dismiss();//关闭对话框

        tv_check_sample.setText(result.getData().getSamplingName() + "");
        tv_check_type.setText(result.getData().getSamplingType() + "");
        tv_checkactivity_sampleunit.setText(result.getData().getSamplingSource() + "");
        tv_check_b.setText(result.getData().getSamplingDept() + "");
    }

    /**
     * 获取采样信息失败
     *
     * @param msg
     * @param requestCode
     */
    @Override
    public void GetSamplingInfoFailed(String msg, int requestCode) {
        Log.d(TAG, "GetSamplingInfoFailed msg=" + msg + " requestCode=" + requestCode);
        // TODO 这个之前在外置二维码时是显示的
        APPUtils.showToast(this, "获取样本信息失败：" + msg);
    }

    /**
     * 成功获取CardQR信息
     *
     * @param result
     * @param requestCode
     */
    @Override
    public void GetCardQRInfoSuccess(Result<GetQRInfoResultBean> result, int requestCode) {

        if (result.getData().getQRErrCode() == null || result.getData().getQRErrCode().isEmpty() || !result.getData().getQRErrCode().equals("0")) {
            isStartTest = -1;
            statusDialog.setMessage("验证失败:" + result.getData().getQRErrMsg());
        } else {
            if (result.getData().getRemainTimes() > 0) {
//            if (result.getData().getRemainTimes() == 0) {
                statusDialog.setStatus(StatusDialog.STATUS_END_VERITY_QRCODE_CARD);
                statusDialog.dismiss();
//            APPUtils.showToast(this, "请开始检测");
                //项目
                if (result.getData().getListInfo() == null || result.getData().getListInfo().isEmpty()) {
                    isStartTest = -1;
                    com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "获取项目参数失败:" + result.getErrMsg());
                } else {
                    selectedProject = new LineModel();
                    selectedProject.source = Integer.valueOf(source);
                    for (int i = 0; i < result.getData().getListInfo().size(); i++) {
                        GetQRInfoResultBean.ListInfoBean lif = result.getData().getListInfo().get(i);
                        if (lif.getTitle().equals("检测项目")) {
                            tv_check_project.setText(lif.getValue() + "");
                            selectedProject.name = lif.getValue();
                        }
                    }

                    //赋值 限量值 与 单位
                    List<GetQRInfoResultBean.ListInfoBean> listInfo = result.getData().getListInfo();
                    for (GetQRInfoResultBean.ListInfoBean listInfoBean : listInfo) {
                        /*if ("国家标准".equals(listInfoBean.getTitle())) {
                            etJcx.setText(listInfoBean.getValue() + "");
                            selectedProject.Jcx = listInfoBean.getValue() + "";
                        } else if ("国家标准单位".equals(listInfoBean.getTitle())) {
                            etJcx.setText(etJcx.getText().toString() + listInfoBean.getValue() + "");
//                            selectedProject.Jcx += listInfoBean.getValue() + "";
                        }*/
                        if ("灵敏度".equals(listInfoBean.getTitle())) {
                            String str=listInfoBean.getValue().trim();
                            String str2="";
                            if(str != null && !"".equals(str)) {
                                for (int i = 0; i < str.length(); i++) {
                                    if ((str.charAt(i) >= 48 && str.charAt(i) <= 57) || str.charAt(i) == 46) {
                                        str2 += str.charAt(i);
                                    }
                                }
                            }
                            etJcx.setText(str2 + "");
                            selectedProject.Jcx = str2 + "";
                        } else if ("国家标准单位".equals(listInfoBean.getTitle())) {
                            etJcx.setText(etJcx.getText().toString() + listInfoBean.getValue() + "");
//                            selectedProject.Jcx += listInfoBean.getValue() + "";
                        }
                    }

                    //赋值单位
                    List<GetQRInfoResultBean.ListInfoBean> listInfo2 = result.getData().getListInfo();
                    for (GetQRInfoResultBean.ListInfoBean listInfoBean : listInfo2) {
                        if ("国家标准单位".equals(listInfoBean.getTitle())) {
                            etJcx.setText(etJcx.getText().toString() + listInfoBean.getValue() + "");
//                            selectedProject.Jcx += listInfoBean.getValue() + "";
                        }
                    }



                    for (int i = 0; i < result.getData().getAlgorithmPara().size(); i++) {
                        GetQRInfoResultBean.AlgorithmParaBean lif = result.getData().getAlgorithmPara().get(i);

                        if (lif.getTitle().equals("Limit")) {
                            selectedProject.Ljz = lif.getValue() + "";
                        } else if (lif.getTitle().equals("Critical")) {
                            etLjz.setText(lif.getValue() + "");
                            selectedProject.Ljz = lif.getValue() + "";
                        } else if (lif.getTitle().equals("ScanStart")) {
                            selectedProject.ScanStart = lif.getValue() + "";
                        } else if (lif.getTitle().equals("ScanEnd")) {
                            selectedProject.ScanEnd = lif.getValue() + "";
                        } else if (lif.getTitle().equals("CTWidth")) {
                            selectedProject.CTWidth = lif.getValue() + "";
//                            selectedProject.CTWidth = "120";
                        } else if (lif.getTitle().equals("CTSpacing")) {
                            selectedProject.CTDistance = lif.getValue() + "";
                        } else if (lif.getTitle().equals("a1")) {
                            selectedProject.A1 = lif.getValue() + "";
                            A1 = lif.getValue();
                        } else if (lif.getTitle().equals("a2")) {
                            selectedProject.A2 = lif.getValue() + "";
                            A2 = lif.getValue();
                        } else if (lif.getTitle().equals("x0")) {
                            selectedProject.X0 = lif.getValue() + "";
                            X0 = lif.getValue();
                        } else if (lif.getTitle().equals("p")) {
                            selectedProject.P = lif.getValue() + "";
                            P = lif.getValue();
                        }
                    }

                    //允许继续测试
                    isStartTest = 1;
                }
            } else {
                statusDialog.setMessage("可用检测次数不足1次，当前为" + result.getData().getRemainTimes() + "次");
//                APPUtils.showToast(this, "当前检测次数不足，请更换金标卡或联系管理人员！");

                new AlertDialog.Builder(this)
                        .setMessage("当前检测次数不足，请更换金标卡或联系管理人员！")
                        .setTitle("提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                isStartTest = -1;
            }
        }
//        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_CARD);
    }

    /**
     * 获取CardQR信息失败
     *
     * @param msg
     * @param requestCode
     */
    @Override
    public void GetCardQRInfoFailed(String msg, int requestCode) {
        APPUtils.showToast(this, "获取试剂盒信息失败：" + msg);
//        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_CARD);
    }

    /**
     * 发送结果成功
     *
     * @param msg
     * @param requestCode
     */
    @Override
    public void SendResultSuccess(String msg, int requestCode) {
        APPUtils.showToast(this, "上传成功");
    }

    /**
     * 上传结果失败
     *
     * @param msg
     * @param requestCode
     */
    @Override
    public void SendResultFailed(String msg, int requestCode) {
        APPUtils.showToast(this, "上传测试结果失败：" + msg);
    }

    // 农药残留单项精准分析仪，唐山账号专用
    private String[] sp_yplx_normal_Array;//样品类别字符串
    private String[] sp_yplx_normal_ArrayId;//样品类别ID

    private String[] sp_yplxArray;//样品类别字符串
    private String[] sp_yplxArrayId;//样品类别ID
    private EditText et_CheckedEnterprise;
    private TextView tv_CheckedEnterprise;

    private GT.Hibernate hibernate;

    private SharedPreferences sp;
    private String user;
    private String terrace;
    private String DeptId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_main2);
        GT.getGT().build(this);
        GT.logs("检测 2 号");

        // 百度定位
        Intent intent = new Intent("LocationAction");
        intent.putExtra("locationMessage", "开始定位");
        sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新

        // 注册广播
        locationReceiver = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SendMessage");
        registerReceiver(locationReceiver, intentFilter); //注册广播


        if (!GT.Network.isInternet(this)) {
            GT.toast(this, "请检查网络是否正常");
            finish();
            return;
        }

        locationMsg = MainActivity.gt_sp.query("locationMsg").toString();

        //初始化
        sp1.setOnItemSelectedListener(this);
        sp2.setOnItemSelectedListener(this);
        sp3.setOnItemSelectedListener(this);


        // 隐藏标题
        GT.WindowUtils.hideActionBar(this);
        //初始化第一个下拉组件数据
        if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {
            initSp("0001", 1);
        } else if ("多参数食品安全检测仪".equals(InterfaceURL.oneModule) || "农药残留检测仪".equals(InterfaceURL.oneModule)) {
            initSp1("0001", 1);
        }


        // 初始化样品类型下拉组件
        sp_yplx.setOnItemSelectedListener(this);
        sp_yplx1.setOnItemSelectedListener(this);
        initSampleTypes("", 1);

        // 样品类型下拉框类型控制
        AreaId = com.example.utils.http.Global.admin_pt;
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            ll_yplx.setVisibility(View.GONE);
            ll_yplx_ts.setVisibility(View.VISIBLE);
        } else {
            ll_yplx.setVisibility(View.VISIBLE);
            ll_yplx_ts.setVisibility(View.GONE);
        }

//        register();

        initView();

        // 样品名称下拉框初始化
        initSampleNameSpinner();

        sp = getSharedPreferences("userPass", MODE_PRIVATE);
        user = sp.getString("user", "");
        terrace = sp.getString("terrace", "");
        DeptId = sp.getString("DeptId", "");

        et_jcxxdz.setText(LocationAddress);

        // 单金标隐藏胶体金模块定时检测按钮，多参数不隐藏
        if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {
            move_time.setVisibility(View.GONE);
        } else if ("多参数食品安全检测仪".equals(InterfaceURL.oneModule)) {
//            move_time.setVisibility(View.VISIBLE);
            move_time.setVisibility(View.GONE);
        }

        //
        sp_yplx_normal = findViewById(R.id.sp_yplx_normal);

        // 获取参数
        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;
        map1.clear();
        map1.put("AreaId", AreaId);
        map1.put("OperatorId", OperatorId);
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "/Tangshan/GetSamplingTypeForNAD4074",map1, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String string = response;
//                Timber.i("样品类型string:" + string);
                JsonRootBean jsonRootBean = new Gson().fromJson(string, JsonRootBean.class);
                List<Data> data = jsonRootBean.getData();

                sp_yplx_normal_Array = new String[data.size()];
                sp_yplx_normal_ArrayId = new String[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    sp_yplx_normal_Array[i] = data.get(i).getSampleName();
                    sp_yplx_normal_ArrayId[i] = data.get(i).getSampleId();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, sp_yplx_normal_Array);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, sp_yplx_normal_Array);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, sp_yplx_normal_Array);
                        sp_yplx_normal.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
//        new GT.OkGo(InterfaceURL.BASE_URL + "/Tangshan/GetSamplingTypeForNAD4074",map1).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                String string = response.body().toString();
////                Timber.i("样品类型string:" + string);
//                JsonRootBean jsonRootBean = new Gson().fromJson(string, JsonRootBean.class);
//                List<Data> data = jsonRootBean.getData();
//
//                sp_yplx_normal_Array = new String[data.size()];
//                sp_yplx_normal_ArrayId = new String[data.size()];
//                for (int i = 0; i < data.size(); i++) {
//                    sp_yplx_normal_Array[i] = data.get(i).getSampleName();
//                    sp_yplx_normal_ArrayId[i] = data.get(i).getSampleId();
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, sp_yplx_normal_Array);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, sp_yplx_normal_Array);
////                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, sp_yplx_normal_Array);
//                        sp_yplx_normal.setAdapter(adapter);
//                    }
//                });
//            }
//        });

        //sampleId
        sp_yplx_normal.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_check_type.setText(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_yplx.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                tv_check_type.setText(parent.getSelectedItem().toString());
                String select = parent.getSelectedItem().toString();
                for (SampleTypeData data : sampletyelist1) {
                    if (data.getSampleName().equals(select)) {
                        String sampleId = data.getSampleId();
                        CheckActivityByMen.sampleId = sampleId;
                        break;
                    }
                }

                loadSelectSampleTypeData(sampletyelist1, select, 1);//实现样品类型二级联动
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_yplx1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_check_type.setText(parent.getSelectedItem().toString());
                String select = parent.getSelectedItem().toString();
                loadSelectSampleTypeData(sampletyelist2, select, 2);//实现样品类型二级联动
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        registerReceiver();
        checkPresenter = new CheckPresenter();
        db = DbHelper.GetInstance();

        tv_check_company.setText(com.example.utils.http.Global.Dept + "");
        tv_check_persion.setText(com.example.utils.http.Global.NAME + "");

        upload_data.setEnabled(true);
        source = getIntent().getStringExtra("source");

        statusDialog = new StatusDialog(this);
//        statusDialog.show();
        statusDialog.setOnProgressStatus(this);

//         录入方式切换
        mode_exchange = (Button) findViewById(R.id.btn_exchange2);
//         判断是否是混合输入状态
        if (!com.example.utils.http.Global.ismixedentry) {
            mode_exchange.setVisibility(View.GONE);
        } else {
            tv_title_mode2.setText("混合录入样品检测".toString());
        }

        mode_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent inent = new Intent(CheckActivityByMen.this, CheckActivity.class);
                inent.putExtra("source", "2");// 定性
                startActivity(inent);

                progressDialog = new ProgressDialog(CheckActivityByMen.this);
                progressDialog.setMessage("加载中……");
                progressDialog.setCancelable(false);
                progressDialog.show();
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        progressDialog.dismiss();
                        t.cancel();
                        Intent inent = new Intent(CheckActivityByMen.this, CheckActivity.class);
                        inent.putExtra("source", "2");// 定性
                        startActivity(inent);
                    }
                }, 2500);


            }
        });

        //扫描时间
        tv_scanTime = findViewById(R.id.tv_scanTime);

        //进出卡
        findViewById(R.id.btn_outCard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SerialUtils.CardOutOrIn();
//                CardOutOrIn();
            }
        });

        if (!source.equals("1")) {
            etConcentrate.setVisibility(View.GONE);
            long_tv.setVisibility(View.GONE);
        }

        try {
            if (persionlist == null) {
                persionlist = new ArrayList<PeopleModel>();
            }

            if (companylist == null) {
                companylist = new ArrayList<PeopleModel>();
            }

            if (shijilist == null) {
                shijilist = new ArrayList<ShiJiModel>();
            }
            if (samplelist == null) {
                samplelist = new ArrayList<SampleModel>();
            }
            if (typelist == null) {
                typelist = new ArrayList<SampleTypeModel>();
            }
            if (projectlist == null) {
                projectlist = new ArrayList<LineModel>();
            }
            if (sampleUnitList == null) {
                sampleUnitList = new ArrayList<PeopleModel>();
            }

            company_list = new String[companylist.size()];
            persion_list = new String[persionlist.size()];
            shiji_list = new String[shijilist.size()];
            sample_list = new String[samplelist.size()];
            project_list = new String[projectlist.size()];
            type_list = new String[typelist.size()];
            sampleUnit_list = new String[sampleUnitList.size()];
            for (int i = 0; i < persionlist.size(); i++) {
                PeopleModel model = persionlist.get(i);
                persion_list[i] = model.getName();
            }

            for (int i = 0; i < companylist.size(); i++) {
                PeopleModel model = companylist.get(i);
                company_list[i] = model.getName();
            }

//			for (int i = 0; i < shijilist.size(); i++) {
//				ShiJiModel model = shijilist.get(i);
//				shiji_list[i] = model.getName();
//			}
            for (int i = 0; i < samplelist.size(); i++) {
                SampleModel model = samplelist.get(i);
                sample_list[i] = model.getName();
            }

            for (int i = 0; i < projectlist.size(); i++) {
                LineModel model = projectlist.get(i);
                project_list[i] = model.getName();
            }
            for (int i = 0; i < typelist.size(); i++) {
                SampleTypeModel model = typelist.get(i);
                type_list[i] = model.getName();
            }
            for (int i = 0; i < sampleUnitList.size(); i++) {
                PeopleModel model = sampleUnitList.get(i);
                sampleUnit_list[i] = model.getName();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (company_list == null || company_list.length <= 0) {
            company_list = new String[1];
            company_list[0] = "请先添加检测单位";
        }

        if (persion_list == null || persion_list.length <= 0) {
            persion_list = new String[1];
            persion_list[0] = "请先添加检验员";
        }

//		if (shiji_list == null || shiji_list.length <= 0) {
//			shiji_list = new String[1];
//			shiji_list[0] = "请先添加试剂厂商";
//		}
        if (sample_list == null || sample_list.length <= 0) {
            sample_list = new String[1];
            sample_list[0] = "请先添加样品名称";
        }
        if (type_list == null || type_list.length <= 0) {
            type_list = new String[1];
            type_list[0] = "请先添加样品类型";
        }
        if (project_list == null || project_list.length <= 0) {
            project_list = new String[1];
            project_list[0] = "请先添加检测项目";
        }
        if (sampleUnit_list == null || sampleUnit_list.length <= 0) {
            sampleUnit_list = new String[1];
            sampleUnit_list[0] = "请先添加送检单位";
        }


        company_adater = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, company_list);//simple_spinner_item
        persion_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, persion_list);
        shiji_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, shiji_list);
        sample_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, sample_list);
        project_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, project_list);
        type_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, type_list);
        sampleUnit_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, sampleUnit_list);

        companySpinner.setAdapter(company_adater);
        persionSpinner.setAdapter(persion_adapter);
        shijiSpinner.setAdapter(shiji_adapter);
        sampleSpinner.setAdapter(sample_adapter);
        projectSpinner.setAdapter(project_adapter);
        typeSpinner.setAdapter(type_adapter);
        sampleUnitSpinner.setAdapter(sampleUnit_adapter);

        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (typelist.size() > 0) {
                    SampleTypeModel model = typelist.get(position);
                    type_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        projectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (projectlist.size() > 0) {
//                    selectedProject = projectlist.get(arg2);
//                    A1 = selectedProject.getA1();
//                    A2 = selectedProject.getA2();
//                    X0 = selectedProject.getX0();
//                    P = selectedProject.getP();
//                    etJcx.setText(selectedProject.getJcx());
//                    etLjz.setText(selectedProject.getLjz());
//                    clearTestDataShow();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        companySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (companylist.size() > 0) {
                    PeopleModel model = companylist.get(arg2);
                    company_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        sampleUnitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (sampleUnitList.size() > 0) {
                    PeopleModel model = sampleUnitList.get(arg2);
                    sampleUnit_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        persionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (persionlist.size() > 0) {
                    PeopleModel model = persionlist.get(arg2);
                    persion_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        shijiSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (shijilist.size() > 0) {
                    ShiJiModel model = shijilist.get(arg2);
                    shiji_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        sampleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (samplelist.size() > 0) {
                    SampleModel model = samplelist.get(position);
                    sample_model = model;
                }


                select_position = position;
                if(NewSampleNameData_List != null){
                    String data = NewSampleNameData_List.get(position).getObjectName();
//                    Timber.i("设置的样品名称为：" + data);
                    tv_check_sample.setText(data);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(qrCodeBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);

    }


    //弹出对话框
    public void ClickDialog(View view) {


        switch (view.getId()) {
            case R.id.tv_check_sample:
            case R.id.tv_check_type:
            case R.id.tv_checkactivity_sampleunit:
//                if (statusDialog == null) {
//                    statusDialog = new StatusDialog(this);
//                }
//                statusDialog.setStatus(StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE);//隐藏弹出检测卡对话框扫描
//                statusDialog.show();
                break;
        }


    }

    Handler handlerQRCode = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scanParameterSuccess((String) msg.obj);
        }
    };


    QRCodeBroadcastReceiver qrCodeBroadcastReceiver;

    private void registerReceiver() {
        //二维码广播接收机
        qrCodeBroadcastReceiver = new QRCodeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(QRCode.QRCODE_LOCAL_MSG_SAMPLE);
        intentFilter.addAction(QRCode.QRCODE_LOCAL_MSG_CARD);
        registerReceiver(qrCodeBroadcastReceiver, intentFilter);
    }

    /**
     * 关于进度状态更改
     *
     * @param status
     */
    @Override
    public void onProgressStatusChange(int status) {
    }

    @Override
    public void onCancel(int status) {
        statusDialog.dismiss();
//        finish();
    }


    //扫描二维码后点击确定按钮
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
        } else if (number.contains("http")) {//适配旧版本的解析
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String select = parent.getSelectedItem().toString();
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {

            switch (parent.getId()) {

                case R.id.sp1:
                    loadSelectSpData(provinceList, select, 1);
                    break;

                case R.id.sp2:
                    loadSelectSpData(cityList, select, 2);
                    break;

                case R.id.sp3:
                    loadSelectSpData(areaList, select, 3);
                    break;

            }

        } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)|| "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {

            switch (parent.getId()) {
                case R.id.sp1:
                    loadSelectSpData1(sampleSources1, select, 1);
                    break;

                case R.id.sp2:
                    loadSelectSpData1(sampleSources2, select, 2);
                    break;

                case R.id.sp3:
                    loadSelectSpData1(sampleSources3, select, 3);
                    break;

            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 初始化当前联动的SP数据
     */
    private String AreaId;

    private void initSp(String cityId, int level) {

        map.clear();//清空数据后设置请求值
        map.put("cityId", cityId);
        //网络请求
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "/System/GetUnderCity", map, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String body = response;
//                Timber.i("initSp-----body=" + body);
                //解析数据
                LinkageJsonRootBean linkageJsonRootBean = new Gson().fromJson(body, LinkageJsonRootBean.class);

                if("0".equals(linkageJsonRootBean.getErrCode())){
                    if(linkageJsonRootBean.getData() != null){

                        //为Sp设置数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (level) {
                                    case 1:

                                        provinceList = linkageJsonRootBean.getData();
                                        String[] array = new String[provinceList.size()];
                                        for (int i = 0; i < provinceList.size(); i++) {
                                            array[i] = provinceList.get(i).getCityName();
                                        }
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp1.setAdapter(adapter);
                                        break;
                                    case 2:

                                        spId1 = cityId;
                                        cityList = linkageJsonRootBean.getData();
                                        array = new String[cityList.size()];
                                        for (int i = 0; i < cityList.size(); i++) {
                                            array[i] = cityList.get(i).getCityName();
                                        }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp2.setAdapter(adapter);
                                        break;
                                    case 3:
                                        spId2 = cityId;
                                        areaList = linkageJsonRootBean.getData();
                                        array = new String[areaList.size()];
                                        for (int i = 0; i < areaList.size(); i++) {
                                            array[i] = areaList.get(i).getCityName();
                                        }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp3.setAdapter(adapter);
                                        break;
                                    case 4:
                                        spId3 = cityId;
                                        break;
                                }

                            }
                        });
                    }
                } else{
//                    Timber.i("------------3------------------");
                    String data = linkageJsonRootBean.getErrMsg();
//                    Timber.i(data);
                    GT.toast(CheckActivityByMen.this,linkageJsonRootBean.getErrMsg());
                }
            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
//        new GT.OkGo(InterfaceURL.BASE_URL + "/System/GetUnderCity", map).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                String body = response.body();
////                Timber.i("initSp-----body=" + body);
//                //解析数据
//                LinkageJsonRootBean linkageJsonRootBean = new Gson().fromJson(body, LinkageJsonRootBean.class);
//
//                if("0".equals(linkageJsonRootBean.getErrCode())){
//                    if(linkageJsonRootBean.getData() != null){
//
//                        //为Sp设置数据
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                switch (level) {
//                                    case 1:
//
//                                        provinceList = linkageJsonRootBean.getData();
//                                        String[] array = new String[provinceList.size()];
//                                        for (int i = 0; i < provinceList.size(); i++) {
//                                            array[i] = provinceList.get(i).getCityName();
//                                        }
////                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
//                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp1.setAdapter(adapter);
//                                        break;
//                                    case 2:
//
//                                        spId1 = cityId;
//                                        cityList = linkageJsonRootBean.getData();
//                                        array = new String[cityList.size()];
//                                        for (int i = 0; i < cityList.size(); i++) {
//                                            array[i] = cityList.get(i).getCityName();
//                                        }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
//                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp2.setAdapter(adapter);
//                                        break;
//                                    case 3:
//                                        spId2 = cityId;
//                                        areaList = linkageJsonRootBean.getData();
//                                        array = new String[areaList.size()];
//                                        for (int i = 0; i < areaList.size(); i++) {
//                                            array[i] = areaList.get(i).getCityName();
//                                        }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
//                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp3.setAdapter(adapter);
//                                        break;
//                                    case 4:
//                                        spId3 = cityId;
//                                        break;
//                                }
//
//                            }
//                        });
//                    }
//                } else{
////                    Timber.i("------------3------------------");
//                    String data = linkageJsonRootBean.getErrMsg();
////                    Timber.i(data);
//                    GT.toast(CheckActivityByMen.this,linkageJsonRootBean.getErrMsg());
//                }
//
//
//
//
//            }
//        });

    }

    /**
     * 单金标实现三级联动的请求
     */
    private void loadSelectSpData(List<LinkageData> dataList, String selectValue, int level) {

        //如果到第三级了就直接返回不再联动
        if (level > 3) {
            return;
        }
        level++;

        for (LinkageData data : dataList) {
            if (data.getCityName().equals(selectValue)) {
                String cityId = data.getCityId();
                initSp(cityId, level);
            }
        }

    }


    /**
     * 多参数上传数据初始化
     */
    private void initSp1(String cityId, int level) {

        // 获取请求体数据
        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;

        if ("信达安检测".equals(OperatorId)) {
            OperatorId = "TangshanNM";
        }

        String str_level = level + "";

        map.clear();//清空数据后设置请求值

        map.put("AreaId", AreaId);
        map.put("OperatorId", OperatorId);
        map.put("ParentId", cityId);
        map.put("LevelCode", str_level);
        //网络请求
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "CommonManual/GetSamplingSourceList", map, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String body = response;
                //解析数据
                SampleSourceJsonRootBean sampleSourceJsonRootBean = new Gson().fromJson(body, SampleSourceJsonRootBean.class);

                if("0".equals(sampleSourceJsonRootBean.getErrCode())){
                    if(sampleSourceJsonRootBean.getData() != null){
                        //为Sp设置数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (level) {
                                    case 1:

                                        sampleSources1 = sampleSourceJsonRootBean.getData();
                                        String[] array = new String[sampleSources1.size()];
                                        for (int i = 0; i < sampleSources1.size(); i++) {
                                            array[i] = sampleSources1.get(i).getSourceName();
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp1.setAdapter(adapter);
                                        break;
                                    case 2:

                                        spId1 = cityId;
                                        sampleSources2 = sampleSourceJsonRootBean.getData();
                                        array = new String[sampleSources2.size()];
                                        for (int i = 0; i < sampleSources2.size(); i++) {
                                            array[i] = sampleSources2.get(i).getSourceName();
                                        }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp2.setAdapter(adapter);
                                        break;
                                    case 3:
                                        spId2 = cityId;
                                        sampleSources3 = sampleSourceJsonRootBean.getData();
                                        array = new String[sampleSources3.size()];
                                        for (int i = 0; i < sampleSources3.size(); i++) {
                                            array[i] = sampleSources3.get(i).getSourceName();
                                        }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp3.setAdapter(adapter);
                                        break;
                                    case 4:
                                        spId3 = cityId;
                                        break;
                                }

                            }
                        });
                    }
                }
                else{
//                    Timber.i("---------------4------------------");
                    String data = sampleSourceJsonRootBean.getErrMsg();
//                    Timber.i(data);
                    GT.toast(CheckActivityByMen.this, sampleSourceJsonRootBean.getErrMsg());
                }
            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
//        new GT.OkGo(InterfaceURL.BASE_URL + "CommonManual/GetSamplingSourceList", map).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                String body = response.body();
//                //解析数据
//                SampleSourceJsonRootBean sampleSourceJsonRootBean = new Gson().fromJson(body, SampleSourceJsonRootBean.class);
//
//                if("0".equals(sampleSourceJsonRootBean.getErrCode())){
//                    if(sampleSourceJsonRootBean.getData() != null){
//                        //为Sp设置数据
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                switch (level) {
//                                    case 1:
//
//                                        sampleSources1 = sampleSourceJsonRootBean.getData();
//                                        String[] array = new String[sampleSources1.size()];
//                                        for (int i = 0; i < sampleSources1.size(); i++) {
//                                            array[i] = sampleSources1.get(i).getSourceName();
//                                        }
//                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp1.setAdapter(adapter);
//                                        break;
//                                    case 2:
//
//                                        spId1 = cityId;
//                                        sampleSources2 = sampleSourceJsonRootBean.getData();
//                                        array = new String[sampleSources2.size()];
//                                        for (int i = 0; i < sampleSources2.size(); i++) {
//                                            array[i] = sampleSources2.get(i).getSourceName();
//                                        }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
//                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp2.setAdapter(adapter);
//                                        break;
//                                    case 3:
//                                        spId2 = cityId;
//                                        sampleSources3 = sampleSourceJsonRootBean.getData();
//                                        array = new String[sampleSources3.size()];
//                                        for (int i = 0; i < sampleSources3.size(); i++) {
//                                            array[i] = sampleSources3.get(i).getSourceName();
//                                        }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, R.layout.simple_list_item_choice_shoudong, array);
//                                        adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp3.setAdapter(adapter);
//                                        break;
//                                    case 4:
//                                        spId3 = cityId;
//                                        break;
//                                }
//
//                            }
//                        });
//                    }
//                }
//                else{
////                    Timber.i("---------------4------------------");
//                    String data = sampleSourceJsonRootBean.getErrMsg();
////                    Timber.i(data);
//                    GT.toast(CheckActivityByMen.this, sampleSourceJsonRootBean.getErrMsg());
//                }
//
//            }
//        });

    }

    /**
     * 多参数实现三级联动的请求
     */

    private void loadSelectSpData1(List<SampleSourceData> dataList, String selectValue, int level) {

        //如果到第三级了就直接返回不再联动
        if (level > 3) {
            return;
        }
        level++;

        for (SampleSourceData data : dataList) {
            if (data.getSourceName().equals(selectValue)) {
                String cityId = data.getSourceId();
                initSp1(cityId, level);
            }
        }

    }


    public class QRCodeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handlerQRCode.sendMessage(Message.obtain(handlerQRCode, 1, intent.getStringExtra(QRCode.QRCODE_LOCAL_MSG)));
        }
    }

    private ProgressDialog progressDialog1;
    public void initView() {

        tv_checkactivity_sampleunit = (TextView) findViewById(R.id.tv_checkactivity_sampleunit);
        tv_check_b = (TextView) findViewById(R.id.tv_check_b);
        companySpinner = (Spinner) findViewById(R.id.check_company_spinner);
        persionSpinner = (Spinner) findViewById(R.id.check_persion_spinner);
        shijiSpinner = (Spinner) findViewById(R.id.check_shiji_spinner);
        sampleSpinner = (Spinner) findViewById(R.id.check_sample_spinner);

        projectSpinner = (Spinner) findViewById(R.id.check_project_spinner);
        typeSpinner = (Spinner) findViewById(R.id.check_type_spinner);
        sampleUnitSpinner = (Spinner) findViewById(R.id.checkactivity_sampleunit_spinner);

        tv_check_company = (TextView) findViewById(R.id.tv_check_company);
        tv_check_persion = (TextView) findViewById(R.id.tv_check_persion);
        tv_check_sample = (TextView) findViewById(R.id.tv_check_sample);
        tv_check_type = (TextView) findViewById(R.id.tv_check_type);
        tv_check_project = (TextView) findViewById(R.id.tv_check_project);
        etJcx = (EditText) findViewById(R.id.check_edit_jcx);
        etLjz = (EditText) findViewById(R.id.check_edit_lin);
        etDr = (EditText) findViewById(R.id.check_edit_value);
        etConcentrate = (EditText) findViewById(R.id.check_edit_long);
        long_tv = (TextView) findViewById(R.id.check_edit_tv_long);

        et_Sample_Num = (EditText) findViewById(R.id.checkactivity_et_SampleNum);
        et_companyCode=findViewById(R.id.checkactivity_et_company);
        ivSearchCheck=findViewById(R.id.iv_search_check);
        llCompany=findViewById(R.id.tr_ll);


        et_CheckedEnterprise = (EditText) findViewById(R.id.et_CheckedEnterprise);
        tv_CheckedEnterprise=findViewById(R.id.tv_CheckedEnterprise);

        et_Sample_Num.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        tv_check_sample.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        et_CheckedEnterprise.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        et_SampleTime = (EditText) findViewById(R.id.checkactivity_et_SampleTime);
        et_SampleTime.setOnClickListener(this);
        String time = GetCurrentTime();

        et_SampleTime.setText(time);

        etResult = (EditText) findViewById(R.id.check_edit_result);
        btn_Imm_Check = (Button) findViewById(R.id.btn_Imm_Check);
        move_time = (Button) findViewById(R.id.move_time);
        move_time.setOnClickListener(this);
        upload_data = (Button) findViewById(R.id.upload_data);

        tv_title_mode2 = (TextView) findViewById(R.id.tv_title_mode2);

        et_yplydz = (EditText) findViewById(R.id.et_yplydz);
        et_yplydz.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        sp_yplx = findViewById(R.id.sp_yplx);
        sp_yplx1 = findViewById(R.id.sp_yplx1);

        mChart = (LineChart) findViewById(R.id.move_chart);
        mChart.getDescription().setEnabled(false);
        mChart.setNoDataText("");

        btn_paint1 = (Button) findViewById(R.id.btn_paint1);
        btn_paint1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(v);
            }
        });

        // 重新定位
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                GT.Thread.runAndroid(new Runnable() {
//                    @Override
//                    public void run() {
//                        et_jcxxdz.setText("");
//                    }
//                });
                et_jcxxdz.setText("");

                if (!GT.Network.isInternet(CheckActivityByMen.this)) {

                    new AlertDialog.Builder(CheckActivityByMen.this)
                            .setMessage("当前网络不佳，请换个网络或到网络信号好的地方定位")
                            .setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                    return;
                }
                Location();
            }
        });
        //判断是否需要组织机构代码
        needCompanyCode= com.example.utils.http.Global.NEEDCompanyCode;
        if (needCompanyCode.equals("0")){
            llCompany.setVisibility(View.GONE);
            ivSearchCheck.setVisibility(View.GONE);
            Log.i("lcy", "initView: ----View.GONE---");

        }else {
            et_CheckedEnterprise.setVisibility(View.GONE);
            tv_CheckedEnterprise.setVisibility(View.VISIBLE);
        }
        //图片跳转搜索界面
        ivSearchCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckActivityByMen.this, SearchActivity.class);
                intent.putExtra("ts","CheckActivityByMen");
                startActivityForResult(intent,1001);
            }
        });

        //为按钮设置焦点，防止进入检测界面后立即弹出键盘的行为
//        btn_Imm_Check.setFocusable(true);
//        btn_Imm_Check.setFocusableInTouchMode(true);
//        btn_Imm_Check.requestFocus();
//        btn_Imm_Check.requestFocusFromTouch();

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
            APPUtils.showToast(CheckActivityByMen.this, "请先检测");
            return;
        }
        if (uploadingFlag) {
            Toast.makeText(this, "正在上传数据，请稍后...", Toast.LENGTH_LONG).show();
            return;
        }
        uploadingFlag = true;
        String msg = getUploadData();
        GetDataFrom(msg);
    }
//	{
//		"sample_number":"W123456987",
//		"test_unit_name":"",
//		"test_item":"",
//		"sample_unit":"",
//		"sample_type":"",
//		"test_results":"",
//		"critical_value":"",
//		"test_man":"",
//		" test_time ":"2017-09-23 15:45:12",
//		" sample_time ":"2017-09-23  15:45:12"
//		}

    private String getUploadData() {
        StringBuffer sb = new StringBuffer();
        sb.append("data={");
        sb.append("\"sample_number\":");
        sb.append("\"" + et_Sample_Num.getText().toString().trim() + "\"" + ",");
        sb.append("\"test_unit_name\":");
        sb.append("\"" + companylist.get(companySpinner.getSelectedItemPosition()).getName() + "\"" + ",");
        sb.append("\"test_item\":");
        sb.append("\"" + projectlist.get(projectSpinner.getSelectedItemPosition()).getName() + "\"" + ",");
        sb.append("\"sample_unit\":");
        sb.append("\"" + sampleUnitList.get(sampleUnitSpinner.getSelectedItemPosition()).getName() + "\"" + ",");
        sb.append("\"sample_type\":");
        sb.append("\"" + typelist.get(typeSpinner.getSelectedItemPosition()).getName() + "\"" + ",");
        sb.append("\"test_results\":");
        sb.append("\"" + etResult.getText().toString().trim() + "\"" + ",");
        sb.append("\"critical_value\":");
        if (source.equals("2")) {
            sb.append("\"" + etDr.getText().toString().trim() + "\"" + ",");
        } else {
            sb.append("\"" + etConcentrate.getText().toString().trim() + "\"" + ",");
        }
        sb.append("\"test_man\":");
        sb.append("\"" + persionlist.get(persionSpinner.getSelectedItemPosition()).getName() + "\"" + ",");
        sb.append("\"test_time\":");
        sb.append("\"" + testTime + "\"" + ",");
        sb.append("\"sample_time\":");
        sb.append("\"" + et_SampleTime.getText().toString().trim() + "\"");
        sb.append("}");

        return sb.toString();
    }

    private void GetDataFrom(final String msg) {
        //在子线程中操作网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                //urlConnection请求服务器，验证
                Message message = Message.obtain();
                try {
                    //1：url对象
                    String urlString = " http://www.huaxialj.com/gspt_all_api/api_hltstkj/pda/API/Application/root/Controller/root_antibiotic/Antibiotic_test_results_colloidal.php";
                    Log.i("BASE_URL", urlString);
                    URL url = new URL(urlString);
                    //2;url.openconnection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    byte[] data = msg.getBytes("utf-8");
                    //3
                    conn.setDoInput(true);                  //打开输入流，以便从服务器获取数据
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);               //使用Post方式不能使用缓存
                    conn.setConnectTimeout(10 * 1000);
                    //设置请求体的类型是文本类型
                    conn.setRequestProperty("Content-Type", "application/X0-www-form-urlencoded");
                    //设置请求体的长度
                    conn.setRequestProperty("Content-Length", String.valueOf(data.length));

                    conn.getOutputStream().write(data);
                    //4
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = getStringFromInputStream(is);
                        String[] st = result.split(",");
                        String str = null;
                        if (st.length == 2) {
                            str = st[0];
                            str = str.substring(str.indexOf(':') + 2);
                            str = str.replace("\"", "");
                        }
                        if (str != null) {
                            String s = decodeUnicode(str);
                            System.out.println("=====================服务器返回的信息：" + result);
                            System.out.println("=====================上传结果：" + s);
                            if (s != null) {
                                if (s.contains("成功")) {
                                    message.obj = s;
                                } else {
                                    message.obj = "上传失败";
                                }
                            } else {
                                message.obj = "上传失败";
                            }
                        }
                    } else {
                        message.obj = "上传失败";
                    }
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                    message.obj = "上传失败,请检查网络连接！";
                } catch (IOException ex) {
                    ex.printStackTrace();
                    message.obj = "上传失败,请检查网络连接！";
                } catch (Exception e) {
                    e.printStackTrace();
                    message.obj = "上传失败！";
                } finally {
                    uploadingFlag = false;
                    message.what = 200;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

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
     * 根据输入流返回一个字符串
     *
     * @param is
     * @return
     * @throws
     */
    private static String getStringFromInputStream(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = is.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        is.close();
        String html = baos.toString();
        baos.close();


        return html;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_USB_GETDATA:
                    if (isGet) {
//                        ToolUtils.hiddenHUD();
                        String str = bytes2HexString((byte[]) msg.obj);
                        etResult.setText(str);

                        Toast.makeText(CheckActivityByMen.this, str, Toast.LENGTH_LONG).show();
                    }

                    break;

                case 100:
//                    ToolUtils.hiddenHUD();
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                    if (myTimerTask != null) {
                        myTimerTask.cancel();
                    }

//				HardwareControler.close(devfd);
                    upload_data.setEnabled(true);
                    //getUploadData();
                    //ClickUplaad();
                    break;
                case 200:
                    String result = (String) msg.obj;
                    if (result.contains("成功")) {
                        Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT).show();
                    } else if (result.contains("失败")) {
                        Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

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
            resultModel.project_name = selectedProject.getName();
//            resultModel.sample_unit = sampleUnit_model.getName();
            resultModel.xian = etJcx.getText().toString();
            resultModel.lin = etLjz.getText().toString();
            resultModel.check_value = etDr.getText().toString();
            resultModel.style_long = etConcentrate.getText().toString();
            resultModel.check_result = result;
            resultModel.time = time;
            resultModel.concentrateUnit = selectedProject.ConcentrateUnit;
            resultModel.sample_unit = tv_checkactivity_sampleunit.getText().toString();
            resultModel.shiji = tv_check_b.getText().toString();
            resultModel.companyCode=et_companyCode.getText().toString();
//            resultModel.upload_status = 1;
//            resultModel.sample_id = et_Sample_Num.getText().toString();


            upload_data.setEnabled(true);

            //保存检测结果
            db.save(resultModel);

            //保存新表
            saveNewTable(resultModel);

            // 记录检测项目祥光数据
            save_projectname_to_LineModel();

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
        detectionResultBean.setSQLType("胶体金");
        detectionResultBean.setCheckRunningNumber(resultModel.id);//检测流水号
        detectionResultBean.setDetectionCompany(resultModel.company_name);//检测单位
        detectionResultBean.setInspector(resultModel.persion);//检测人员
        detectionResultBean.setCommodityPlaceOrigin(resultModel.sample_unit);//商品来源
        detectionResultBean.setTestItem(resultModel.project_name);//检测项目
        detectionResultBean.setSampleName(resultModel.sample_name);//样品名称
        detectionResultBean.setSpecimenType(resultModel.sample_type);//样品类型
        detectionResultBean.setLimitStandard(resultModel.xian);//限量标准
//        detectionResultBean.setLimitStandard(resultModel.xian + resultModel.concentrateUnit);//限量标准
        detectionResultBean.setCriticalValue(resultModel.lin);//临界值
        detectionResultBean.setDetectionValue(resultModel.check_value);//检测值
        detectionResultBean.setSampleConcentration(resultModel.style_long);//样品浓度
        detectionResultBean.setDetectionResult(resultModel.check_result);//检测结果
        detectionResultBean.setDetectionTime(resultModel.time);//检测时间

        // 新增
//        detectionResultBean.setUploadStatus("已上传");
        detectionResultBean.setUploadStatus(upload_status == 1 ? "已上传" : "未上传");
        detectionResultBean.setNumberSamples(et_Sample_Num.getText().toString());// 样品编号
        detectionResultBean.setLimitStandard(resultModel.xian); // 限量标准
        detectionResultBean.setUnitsUnderInspection(et_CheckedEnterprise.getText().toString() + ""); // 限量标准
        detectionResultBean.setSpId1(spId1 + ""); // 省级id
        detectionResultBean.setSpId2(spId2 + ""); // 市级id
        detectionResultBean.setSpId3(spId3 + ""); // 县级id
        detectionResultBean.setYplbId(yplbId + ""); // 县级id
        detectionResultBean.setSampleId(sampleId);//样品总分类Id
        detectionResultBean.setQRCode(QRCode22); // 试剂盒二维码字符串
        detectionResultBean.setOperatorId(com.example.utils.http.Global.ID); // 上传数据的OperatorId参数
        detectionResultBean.setAreaId(terrace); // 上传数据的AreaId参数
        detectionResultBean.setDeptId(DeptId); // 上传数据的DeptId参数
        detectionResultBean.companyCode = et_companyCode.getText().toString();


        //保存数据
//        MainActivity.hibernate.save(detectionResultBean);

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


    Timer timerRe = null;
    TimerTask taskTime = null;
    private int recLen = 0;

    Handler handlerTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    move_time.setText("" + recLen + "S后开始检测");
                    if (recLen < 0) {
                        taskTime.cancel();
//                        timerRe.cancel();
//                        timerRe = null;
                        move_time.setText("定时检测");
                        ClickMoveNow();
                    }
                    break;
            }
        }
    };

    private boolean ValidateDataToTest() {


        return true;
    }

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
        etDr.setText("");
        etResult.setText("");
        etConcentrate.setText("");
    }

    // TODO
    /**
     * 检测
     * 0:等待
     * 1:允许
     * -1:不允许
     */
    private int isStartTest = 0;
    private int sleepTime = 20;//等待二维码扫描的时间
    private boolean isTest = false;//是否正在检测中
    public boolean isScanQRExit = false;//是否在扫描二维码时退出扫描等待

    /**
     * 即时检测
     *
     * @param v
     */
    public void ClickStart(View v) {

//        if("2".equals(CheckActivity.out_or_in_status) || "".equals(CheckActivity.out_or_in_status)){
//            APPUtils.showToast(CheckActivityByMen.this, "检测前请保持卡槽在外的状态");
//            return;
//        }

        //如果还在扫描二维码时，不许再点击即时检测
        if (isTest) {
            APPUtils.showToast(CheckActivityByMen.this, "二维码扫描中，请稍后...");
            return;
        }


        //如果样品名称为null 那就弹出对话框
//|| et_CheckedEnterprise.getText().toString().length() == 0
        if (tv_check_sample.getText().toString().length() == 0  || et_Sample_Num.getText().toString().length() == 0 || et_yplydz.getText().toString().length() == 0) {
            String str = "";
            if (tv_check_sample.getText().toString().length() == 0) {
                str = "样品名称不能为空";
            }
//            else if (et_CheckedEnterprise.getText().toString().length() == 0) {
//                str = "被检企业名称不能为空";
//            }
            else if (et_Sample_Num.getText().toString().length() == 0) {
                str = "样品编号不能为空";
            } else if (et_yplydz.getText().toString().length() == 0) {
                str = "样品来源地址不能为空";
            }
            APPUtils.showToast(CheckActivityByMen.this, str);
            return;
        }


        //获取商品来源
        tv_checkactivity_sampleunit.setText(sp1.getSelectedItem().toString() + "," + sp2.getSelectedItem().toString() + "," + sp3.getSelectedItem().toString());

        // TODO 是否允许检测(如果二维码没有获取到，或二维码扫描请求失败 那就不允许检测)
        isStartTest = 0;
        scanInlayQRCode();//扫描内置二维码
        APPUtils.showToast(CheckActivityByMen.this, "扫描二维码中，请稍后...");

        GT.Thread.runJava(new Runnable() {
            @Override
            public void run() {
                int i = sleepTime;
                while (isStartTest == 0) {

                    int finalI = i;
                    GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                        @Override
                        public void run() {
                            if (tv_scanTime != null) {
                                tv_scanTime.setText("扫描时间  " + finalI + "s");
                            }
                        }
                    });

                    //是否在扫描的时候按下返回键退出
                    if (isScanQRExit) {
                        isScanQRExit = false;
                        break;
                    }

                    i--;
                    GT.Thread.sleep(1000);
                    if (i <= 0) {
                        break;
                    }
                }

                if (isStartTest == 0) {
                    APPUtils.showToast(CheckActivityByMen.this, "1请检查金标卡是否插到位与网络是否正常连接：" + isStartTest);
                    isTest = false;


                    GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                        @Override
                        public void run() {
                            if (tv_scanTime != null) {
                                tv_scanTime.setText("未扫描");
                            }
                        }
                    });

//                    CardOutOrIn();

                    SerialUtils.CardOutOrIn();

                    return;
                } else if (isStartTest == -1) {


                    APPUtils.showToast(CheckActivityByMen.this, "该二维码数据有误");

                    isTest = false;


                    GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                        @Override
                        public void run() {
                            if (tv_scanTime != null) {
                                tv_scanTime.setText("二维码数据有误");
                            }
                        }
                    });

//                    CardOutOrIn();
                    SerialUtils.CardOutOrIn();

                    return;

                }

                if (selectedProject == null) {
                    isTest = false;
                    return;
                }


                if (tv_check_company.getText() == null || tv_check_company.getText().toString().isEmpty()) {
                    APPUtils.showToast(CheckActivityByMen.this, "请先选择检测单位");
                    tv_scanTime.setText("请先选择检测单位");
                    isTest = false;
                    return;
                }

                if (tv_check_persion.getText() == null || tv_check_persion.getText().toString().isEmpty()) {
                    APPUtils.showToast(CheckActivityByMen.this, "请先选择检验员");
                    tv_scanTime.setText("请先选择检验员");
                    isTest = false;
                    return;
                }

                if (tv_check_sample.getText() == null || tv_check_sample.getText().toString().isEmpty()) {
                    APPUtils.showToast(CheckActivityByMen.this, "请先选样品名称");
                    tv_scanTime.setText("请先选样品名称");
                    isTest = false;
                    return;
                }
                if (tv_check_type.getText() == null || tv_check_type.getText().toString().isEmpty()) {
                    APPUtils.showToast(CheckActivityByMen.this, "请先选样品类型");
                    tv_scanTime.setText("请先选样品类型");
                    isTest = false;
                    return;
                }

                if (et_yplydz.getText() == null || et_yplydz.getText().toString().isEmpty()) {
                    APPUtils.showToast(CheckActivityByMen.this, "请输入样品详细地址");
                    tv_scanTime.setText("请输入样品详细地址");
                    isTest = false;
                    return;
                }

                if (new Integer("0").equals(selectedProject.ScanEnd)
                        || new Integer("0").equals(selectedProject.ScanStart)
                        || new Integer("0").equals(selectedProject.CTWidth)
                        || new Integer("0").equals(selectedProject.CTDistance)) {
                    APPUtils.showToast(CheckActivityByMen.this, "该检测项目参数不完整或不正确，请转至项目管理并重新编辑");
                    isTest = false;
                    return;
                }

                GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                    @Override
                    public void run() {

                        isTest = false;
                        GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                            @Override
                            public void run() {
                                if (tv_scanTime != null) {
                                    tv_scanTime.setText("扫描成功");
                                }
                            }
                        });


                        //清空测试数据显示
                        clearTestDataShow();

                        /*//发送检测命令
                        String message = getTestInstruction();
                        readTimeOutCount = 0;
                        SendData(message);*/

                        // 发送绘图命令
                        ClickDraw1(v);
                    }
                });

            }
        });

    }

    public static int REQUEST_CODE = 0;

    //扫描内置二维码
    private void scanInlayQRCode() {


        isTest = true;//设置检测二维码中标识
        String message2 = "Card_Skip";
        byte[] data = message2.getBytes(Charset.forName("gb2312"));
        if (!SerialUtils.COM3_SendData(data)) {
            APPUtils.showToast(this, "数据发送失败");
            return;
        } else {
            APPUtils.showToast(this, "数据发送成功");
        }

        buf = new byte[BUFSIZE];
        strResultBuffer = new StringBuilder(256 * 200);


        Message message = new Message();
        message.what = 2;
        handlerMess.sendMessage(message);


    }

    private String getTestInstruction() {

        int sou = selectedProject.source;
        if (sou == 3) sou = 2;
        return "SendData" + "," + sou + ","
                + selectedProject.getScanStart() + "," + selectedProject.getScanEnd() + ","
                + selectedProject.getCTWidth() + "," + selectedProject.getCTDistance() + ","
                + selectedProject.getLjz() + "," + "1" + "," + "1" + "," + "1"
                + "," + "1";
    }

    private String A1;
    private String A2;
    private String X0;
    private String P;

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
        printData = ToolUtils.GetPrintInfo1(resultModel, this, source);
        APPUtils.showToast(this, printData);
        byte[] data = printData.getBytes(Charset.forName("gb2312"));
        if (!SerialUtils.COM4_SendData(data)) {
            APPUtils.showToast(this, "打印数据发送失败");
        }
    }

    private String devName = "/dev/ttyAMA3";
    private int speed = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int devfd = -1;
    private Timer timer = null;
    private String dateCheck = "";
    private String timeCheck = "";

    /**
     * 发送数据
     *
     * @param message
     */
    private void SendData(String message) {
        SimpleDateFormat formatterData = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        dateCheck = formatterData.format(curDate);
        timeCheck = formatterTime.format(curDate);

        if (buf != null && buf.length > 0) {
            buf = null;
        }

        if (strResultBuffer != null && strResultBuffer.length() > 0) {
            strResultBuffer = null;
        }

        buf = new byte[BUFSIZE];
        strResultBuffer = new StringBuilder(256 * 200);

        //向下位机发送数据
        byte[] data = message.getBytes(Charset.forName("gb2312"));
        if (!SerialUtils.COM3_SendData(data)) {
            APPUtils.showToast(this, "数据发送失败");
            return;
        } else {
            APPUtils.showToast(this, "数据发送成功");
        }
        ToolUtils.showHUD(CheckActivityByMen.this, "请稍等...");

        timer = new Timer();
        if (myTimerTask != null) {
            myTimerTask.cancel();
        }
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 4000, 2000);
    }

    private MyTimerTask myTimerTask;

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (readTimeOutCount++ > 3) {
                handler.sendEmptyMessage(100);
                APPUtils.showToast(CheckActivityByMen.this, "接收数据失败");
                Log.d(TAG, "接收数据超时了，结束了本次数据接收");
            }
            Message message = new Message();
            message.what = 1;
            handlerMess.sendMessage(message);
        }
    }


    private final int BUFSIZE = 1024;
    private byte[] buf;
    private StringBuilder strResultBuffer = null;
    private String strResult = null;
    public boolean isScanSuccess = false;
    private Handler handlerMess = new Handler() {
        @Override
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:


                    int i = HardwareControler.select(Global.DEV_COM3, 0, 0);

                    if (i == 1) {


                        int retSize = HardwareControler.read(Global.DEV_COM3, buf, BUFSIZE);
                        if (retSize > 0) {
                            String strResultOld;
                            strResultOld = new String(buf, 0, retSize, Charset.forName("gbk"));
                            strResultBuffer.append(strResultOld);
                            String raw_data = strResultBuffer.toString();
                            raw_data = raw_data.replace("\n", ",").replace("OK", ",");
                            raw_data = raw_data + "\n";

                            if (!raw_data.endsWith("\n")) {
                                return;
                            }

                            raw_data = raw_data.replace("\n", "");

                            String[] raw = raw_data.split(",");
                            String raw_data1 = "";
                            for (int z = 0; z < raw.length; z++) {
                                raw_data1 += raw[z];
                            }

                            strResult = raw[1] + "," + raw[2];
                            String[] data = strResult.split(",");

                            if (data.length != 2) {  //如果数据格式错误
                                APPUtils.showToast(CheckActivityByMen.this, "数据格式错误，接收数据失败");
                                handler.sendEmptyMessage(100);
                                return;
                            }
                            CalcAndShowTestResult(data);
                        }
                    }

                    break;

                case 2:

                    //读取下位机传过来的数据
                    try {
                        GT.Thread.runJava(new Runnable() {
                            @SuppressLint("HandlerLeak")
                            @Override
                            public void run() {
                                int i = 0;
                                isScanSuccess = false;
                                while (true) {
                                    i++;
                                    GT.Thread.sleep(1000);

                                    GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                                        @Override
                                        public void run() {

                                            if (buf == null) return;
                                            int index = HardwareControler.select(Global.DEV_COM3, 0, 0);
                                            if (index == 1) {
                                                int retSize = HardwareControler.read(Global.DEV_COM3, buf, BUFSIZE);
                                                if (retSize > 0) {
                                                    String strResultOld;
                                                    strResultOld = new String(buf, 0, retSize, Charset.forName("gbk"));
                                                    if (handlerQRCode == null) return;

                                                    if (!"".equals(strResultOld) && strResultOld != null) {
                                                        if (strResultOld.length() % 2 == 0) {
                                                            String str1 = "";
                                                            String str2 = "";
                                                            str1 = strResultOld.substring(0, strResultOld.length() / 2);
                                                            str2 = strResultOld.substring(strResultOld.length() / 2, strResultOld.length());
                                                            if (str1.equals(str2)) {
                                                                strResultOld = str1;
                                                            }
                                                        }
                                                    }


                                                    //发送给后台请求的内置二维码信息
                                                    handlerQRCode.sendMessage(Message.obtain(handlerQRCode, 1, strResultOld));
                                                    isScanSuccess = true;


                                                }
                                            }

                                        }
                                    });

                                    if (isScanSuccess) {
                                        break;
                                    }

                                    if (i >= sleepTime) {
                                        break;
                                    }

                                }

                            }
                        });

                    } catch (Exception e) {
                    }

                    break;
            }
        }
    };

    /**
     * 计算并显示检测结果
     *
     * @param data 检测值和检测结果组成的长度为2的字符串数组
     */
    private void CalcAndShowTestResult(String[] data) {

        etDr.setText(data[0]);
        if ("no signal".equals(data[0].toLowerCase())) {  //无效的情况
            if (source.equals("1")) {  //定量
                etConcentrate.setText("0.000");
            }
            etResult.setText(data[1]);
            handler.sendEmptyMessage(100);//关闭定时器
            APPUtils.showToast(this, "结果无效，请重新检测");

            /*//保存
            saveCheck_ResultData(etResult.getText().toString());*/


            // 结束加载画面
            ToolUtils.hiddenHUD();

            //TODO 上传
            uploadResult();
            return;
        }
        float dr = 0.0f;
        try {
            dr = Float.parseFloat(data[0]);
        } catch (NumberFormatException ex) {
            APPUtils.showToast(CheckActivityByMen.this, "数据格式错误，接收数据失败");//几乎不可能异常，除非下位机出现问题
            handler.sendEmptyMessage(100);//关闭定时器


            // 结束加载画面
            ToolUtils.hiddenHUD();

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (source.equals("1")) {  //定量
                double concentrate = 0.0;
                if (!TextUtils.isEmpty(A1) && !TextUtils.isEmpty(A2) && !TextUtils.isEmpty(X0) && !TextUtils.isEmpty(P)) {

                    concentrate = Double.parseDouble(X0) * (Math.pow((((Double.parseDouble(A2) - Double.parseDouble(A1)) / (Double
                            .parseDouble(A2) - dr)) - 1), (1 / Double.parseDouble(P))));
                    concentrate = concentrate < 0 ? 0.0 : concentrate;  //如果计算得到的浓度值为负数，则令其=0
                    DecimalFormat df = new DecimalFormat("#.000");
                    String s = df.format(concentrate);
                    concentrate = Double.parseDouble(s);
                    etConcentrate.setText(concentrate + "");
                    if (Double.parseDouble(selectedProject.Jcx) > concentrate) {
                        etResult.setText("合格");
                    } else {
                        etResult.setText("不合格");
                    }
                } else {  //定量检测中用户对进行检测的项目没有设置四参数的情况，该情况下，检测结果显示下位机收到的阴阳性结果
                    etResult.setText(data[1]);
                }
            } else {  //定性
                etResult.setText(data[1]);
            }


        } catch (NumberFormatException e) {
            APPUtils.showToast(CheckActivityByMen.this, "计算错误");
            e.printStackTrace();

            // 结束加载画面
            ToolUtils.hiddenHUD();
        }


        //上传
        uploadResult();

        //保存
        saveCheck_ResultData(etResult.getText().toString());
        handler.sendEmptyMessage(100);//关闭定时器


    }

    // 获取定位信息
    private void set_dingwei_info() {
        try {
            locationMsg = MainActivity.gt_sp.query("locationMsg").toString();
            MainActivity.gt_sp.save("locationMsg", locationMsg);

            String[] split_something = locationMsg.split(",");

            LocationX = split_something[0];
            LocationY = split_something[1];
            LocationAddress = split_something[2];
        } catch (Exception e) {
            e.printStackTrace();
            LocationX = "";
            LocationY = "";
            LocationAddress = "";
        }
    }

    private String locationMsg = "";
    private String LocationX = "";
    private String LocationY = "";
    private String LocationAddress = "";


    // 上传成功状态位
    private int upload_status = 0;

    private void uploadResult() {

        //获取必要上传
        SharedPreferences sp = getSharedPreferences("userPass", MODE_PRIVATE);
        String user = sp.getString("user", "");
        String terrace = sp.getString("terrace", "");
        String deptId = sp.getString("DeptId", "");
        String setWithModel = getStringSN();//获取唯一标识


        String str_api = "";

        // 只有单金标且登录用户是唐山的用户才使用 "Tangshan/SendForNAD4074" api
        // 其他的都是 "CommonManual/GetSamplingTypeList" api
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) && "TangshanNM".equals(AreaId)) {
            str_api = "Tangshan/SendForNAD4074";
            for (int i = 0; i < sp_yplx_normal_Array.length; i++) {
                if (sp_yplx_normal.getSelectedItem().toString().equals(sp_yplx_normal_Array[i])) {
                    yplbId = sp_yplx_normal_ArrayId[i];
                    break;
                }
            }

        } else if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) && !"TangshanNM".equals(AreaId)) {
            str_api = "Tangshan/SendForNAD4074";
            for (int i = 0; i < sp_yplx_normal_Array.length; i++) {
                if (sp_yplx_normal.getSelectedItem().toString().equals(sp_yplx_normal_Array[i])) {
                    yplbId = sp_yplx_normal_ArrayId[i];
                    break;
                }
            }
        } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)|| "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "CommonManual/UpdateManualSampling";
            for (int i = 0; i < sp_yplxArray.length; i++) {
                if (sp_yplx1.getSelectedItem().toString().equals(sp_yplxArray[i])) {
                    yplbId = sp_yplxArrayId[i];
                    break;
                }
            }
        }


        String ipAddress = GT.Network.getIPAddress(this);

        //将具体检测文字 转为 编码
        String result = "";
        if ("阴性".equals(etResult.getText().toString())) {
            result = "0";
        } else if ("阳性".equals(etResult.getText().toString())) {
            result = "1";
        } else {
            result = "-1";
        }

        String data = "";

        String productId = "";
        String objectId = "";
        if(select_position != -1){
            if(NewSampleNameData_List != null){
                productId = NewSampleNameData_List.get(select_position).getProductId();
                objectId = NewSampleNameData_List.get(select_position).getObjectId()+"";
            }

        }


        // 新的上传
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            String checkedEnterprise="";
            if (needCompanyCode.equals("0")){
                checkedEnterprise=et_CheckedEnterprise.getText().toString();

                Log.i("lcy", et_companyCode.getText().toString() +"======uploadResult: ---走了-tv_CheckedEnterprise-"+et_CheckedEnterprise.getText().toString()+"-------------"+tv_CheckedEnterprise.getText().toString());



            }else {
                checkedEnterprise=tv_CheckedEnterprise.getText().toString();

                Log.i("lcy", et_companyCode.getText().toString() +"----uploadResult: ---走了-et_CheckedEnterprise-"+et_CheckedEnterprise.getText().toString()+"-------------"+tv_CheckedEnterprise.getText().toString());

            }

            data = "[{\"QRCode\":\"" + QRCode22.replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + etDr.getText().toString() + "\",\n" +
                    "\"SamplingNumber\":\"" + et_Sample_Num.getText().toString() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "[\n" +
                    "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                    "{\"Title\":\"省Id\",\"Id\":\"province\",\"Value\":\"" + spId1 + "\"},\n" +
                    "{\"Title\":\"城市Id\",\"Id\":\"city\",\"Value\":\"" + spId2 + "\"},\n" +
                    "{\"Title\":\"区县\",\"Id\":\"district\",\"Value\":\"" + spId3 + "\"},\n" +
                    "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + yplbId + "\"},\n" +
                    "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + tv_check_sample.getText().toString() + "\"},\n" +
                    "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + checkedEnterprise + "\"},\n" +
                    "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                    "{\"Title\":\"组织机构代码\",\"Id\":\"companyCode\",\"Value\":\"" + et_companyCode.getText().toString() + "\"},\n" +
                    "{\"Title\":\"产地详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + et_yplydz.getText().toString() + "\"},\n" +
                    "{\"Title\":\"产品Id\",\"Id\":\"productId\",\"Value\":\""+ productId +"\"},\n" +
                    "{\"Title\":\"对象Id\",\"Id\":\"objectId\",\"Value\":\""+ objectId +"\"},\n" +
                    "{\"Title\":\"图像Base64(可选)\",\"Id\":\"picBase64\",\"Value\":\"" + image_base64 + "\"}\n" +
                    "]}\n" +
                    "]";

        } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)|| "农药残留检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {

            String input = tv_check_sample.getText().toString();
            String regex = "\\\\u000f";
            String replacement = " ";
            String output = input.replaceAll(regex, replacement);

                Log.d("zdl","=============et_companyCode=================="+et_companyCode.getText().toString());
                Log.d("zdl","================et_CheckedEnterprise==============="+et_CheckedEnterprise.getText().toString());
                Log.d("zdl","=============tv_CheckedEnterprise=================="+tv_CheckedEnterprise.getText().toString());

            //判断被检企业添加哪个
            String checkedEnterprise="";
            if (needCompanyCode.equals("0")){
                checkedEnterprise=et_CheckedEnterprise.getText().toString();

            }else {
                checkedEnterprise=tv_CheckedEnterprise.getText().toString();

            }
            data = "[{\"QRCode\":\"" + QRCode22.replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + etDr.getText().toString() + "\",\n" +
                    "\"SamplingNumber\":\"" + et_Sample_Num.getText().toString() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "[\n" +
                    "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                    "{\"Title\":\"样品来源地1级\",\"Id\":\"source1\",\"Value\":\"" + spId1 + "\"},\n" +
                    "{\"Title\":\"样品来源地2级\",\"Id\":\"source2\",\"Value\":\"" + spId2 + "\"},\n" +
                    "{\"Title\":\"样品来源地3级\",\"Id\":\"source3\",\"Value\":\"" + spId3 + "\"},\n" +
                    "{\"Title\":\"样品总分类Id\",\"Id\":\"sampleClassId\",\"Value\":\"" + sampleId + "\"},\n" +
                    "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + yplbId + "\"},\n" +
                    "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + output + "\"},\n" +
                    "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + checkedEnterprise + "\"},\n" +
                    "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                    "{\"Title\":\"组织机构代码\",\"Id\":\"companyCode\",\"Value\":\"" + et_companyCode.getText().toString() + "\"},\n" +
                    "{\"Title\":\"样品详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + et_yplydz.getText().toString() + "\"},\n" +
                    "{\"Title\":\"产品Id\",\"Id\":\"productId\",\"Value\":\""+ productId +"\"},\n" +
                    "{\"Title\":\"对象Id\",\"Id\":\"objectId\",\"Value\":\""+ objectId +"\"},\n" +
                    "{\"Title\":\"图像Base64(可选)\",\"Id\":\"picBase64\",\"Value\":\"" + image_base64 + "\"}\n" +
                    "]}\n" +
                    "]";

        }

        Timber.i("data手动====" + data);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (detectionResultBean != null) {
                    MainActivity.hibernate.save(detectionResultBean);
                }
                GT.Thread.runJava(new Runnable() {
                    @Override
                    public void run() {

                        CheckActivityByMen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GT.Thread.runJava(new Runnable() {
                                    @Override
                                    public void run() {
                                        GT.Thread.sleep(4000);
                                        ToolUtils.hiddenHUD();
                                    }
                                });
                            }
                        });

                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean1 uploadData1 = new Gson().fromJson(ret, TestDataBean1.class);
                if (uploadData1 != null) {

                    String errMsg = uploadData1.getData().get(0).getErrMsg();
                    String errCode = uploadData1.getData().get(0).getErrCode();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(CheckActivityByMen.this, errMsg, Toast.LENGTH_LONG).show();
                            if ("0".equals(errCode)) {
                                if (detectionResultBean != null) {
                                    detectionResultBean.setUploadStatus("已上传");
                                    MainActivity.hibernate.save(detectionResultBean);
                                }
                            } else {
                                if (detectionResultBean != null) {
                                    MainActivity.hibernate.save(detectionResultBean);
                                }
//                                Timber.i("上传-------------------");
                                GT.toast(CheckActivityByMen.this,errMsg);
                            }

                            GT.Thread.runJava(new Runnable() {
                                @Override
                                public void run() {

                                    CheckActivityByMen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            GT.Thread.runJava(new Runnable() {
                                                @Override
                                                public void run() {
                                                    GT.Thread.sleep(4000);
                                                    ToolUtils.hiddenHUD();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//
//                TestDataBean1 uploadData1 = new Gson().fromJson(response, TestDataBean1.class);
//                if (uploadData1 != null) {
//
//                    String errMsg = uploadData1.getData().get(0).getErrMsg();
//                    String errCode = uploadData1.getData().get(0).getErrCode();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Toast.makeText(CheckActivityByMen.this, errMsg, Toast.LENGTH_LONG).show();
//                            if ("0".equals(errCode)) {
//                                if (detectionResultBean != null) {
//                                    detectionResultBean.setUploadStatus("已上传");
//                                    MainActivity.hibernate.save(detectionResultBean);
//                                }
//                            } else {
//                                if (detectionResultBean != null) {
//                                    MainActivity.hibernate.save(detectionResultBean);
//                                }
////                                Timber.i("上传-------------------");
//                                GT.toast(CheckActivityByMen.this,errMsg);
//                            }
//
//                            GT.Thread.runJava(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    CheckActivityByMen.this.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            GT.Thread.runJava(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    GT.Thread.sleep(4000);
//                                                    ToolUtils.hiddenHUD();
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onError(String response) {
//
//
//                if (detectionResultBean != null) {
//                    MainActivity.hibernate.save(detectionResultBean);
//                }
//                GT.Thread.runJava(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        CheckActivityByMen.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                GT.Thread.runJava(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        GT.Thread.sleep(4000);
//                                        ToolUtils.hiddenHUD();
//                                    }
//                                });
//                            }
//                        });
//
//                    }
//                });
//            }
//        });

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

        if (isTest) {
            new AlertDialog.Builder(this)
                    .setMessage("正在扫描二维码中，确定要关闭扫描吗？")
                    .setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isScanQRExit = true;
                            CheckActivityByMen.this.back();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        } else {
            CheckActivityByMen.this.back();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (isTest) {
            new AlertDialog.Builder(this)
                    .setMessage("正在扫描二维码中，确定要关闭扫描吗？")
                    .setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isScanQRExit = true;
                            CheckActivityByMen.this.back();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        } else {
            CheckActivityByMen.this.back();
        }
    }

    // [ScanStart] bytes2HexString byte与16进制字符串的互相转换
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
            ShowSampleTimeDialog();
        } else if (v.getId() == R.id.move_time) {
            timeCheck();
        }

    }

    /**
     * 定时检测
     */
    private void timeCheck() {
        recLen = SharedPreferencesUtil.getTime(getApplicationContext(), "time") * 60;
        taskTime = new MytaskTime();
        timer = new Timer();
        timer.schedule(taskTime, 0, 1000);
    }

    /**
     * 显示抽样时间dialog
     */

    private void ShowSampleTimeDialog() {

        final Calendar c = Calendar.getInstance();
        // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
        new DateTimePickerDialog(CheckActivityByMen.this, 0,
                new DateTimePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker,
                                          int year, int monthOfYear,
                                          int dayOfMonth, TimePicker timePicker,
                                          int hour, int minute) {
                        String textString = GetFormatTime(year, monthOfYear, dayOfMonth, hour, minute, c.get(Calendar.SECOND));
                        et_SampleTime.setText(textString);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private String GetFormatTime(int year, int monthOfYear, int dayOfMonth, int hour, int minute, int second) {

        String textString = String.format("%d-%d-%d %02d:%02d:%02d", year, monthOfYear + 1, dayOfMonth, hour, minute, second);
        return textString;
    }

    private String GetCurrentTime() {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式

        return dateFormat.format(now);
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

    private void initSampleTypes(String parentId, int level) {

        // 获取参数
        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;

        map1.clear();
        map1.put("AreaId", AreaId);
        map1.put("OperatorId", OperatorId);
        map1.put("ParentId", parentId);

        //网络请求
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "/CommonManual/GetSamplingTypeList", map1, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String body = response;
//                Timber.i("body=" + body);

                //解析数据
                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);

                if("0".equals(sampleTypeJsonRootBean.getErrCode())){

                    if(sampleTypeJsonRootBean.getData() != null){
                        //为Sp设置数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (level) {
                                    case 1:
                                        sampletyelist1 = sampleTypeJsonRootBean.getData();
                                        String[] array = new String[sampletyelist1.size()];
                                        for (int i = 0; i < sampletyelist1.size(); i++) {
                                            array[i] = sampletyelist1.get(i).getSampleName();
                                        }
                                        //样品类型
                                        for (SampleTypeData sampleTypeData : sampletyelist1) {
                                            if (sampleId == null) {
                                                sampleId = sampleTypeData.getSampleId();
                                            }
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp_yplx.setAdapter(adapter);
                                        break;
                                    case 2:
                                        sampletyelist2 = sampleTypeJsonRootBean.getData();
                                        array = new String[sampletyelist2.size()];
                                        sp_yplxArray = new String[sampletyelist2.size()];
                                        sp_yplxArrayId = new String[sampletyelist2.size()];
                                        for (int i = 0; i < sampletyelist2.size(); i++) {
                                            array[i] = sampletyelist2.get(i).getSampleName();
                                            sp_yplxArrayId[i] = sampletyelist2.get(i).getSampleId();
                                            sp_yplxArray[i] = sampletyelist2.get(i).getSampleName();
                                        }
                                        adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
                                        sp_yplx1.setAdapter(adapter);
                                        break;
                                    case 3:
                                        break;
                                }

                            }
                        });
                    }
                } else{
//                    Timber.i("-----------1---------------------");
                    String data = sampleTypeJsonRootBean.getErrMsg();
//                    Timber.i(data);
                    GT.toast(CheckActivityByMen.this, data);
                }

            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
//        new GT.OkGo(InterfaceURL.BASE_URL + "/CommonManual/GetSamplingTypeList", map1).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                String body = response.body();
////                Timber.i("body=" + body);
//
//                //解析数据
//                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);
//
//                if("0".equals(sampleTypeJsonRootBean.getErrCode())){
//
//                    if(sampleTypeJsonRootBean.getData() != null){
//                        //为Sp设置数据
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                switch (level) {
//                                    case 1:
//                                        sampletyelist1 = sampleTypeJsonRootBean.getData();
//                                        String[] array = new String[sampletyelist1.size()];
//                                        for (int i = 0; i < sampletyelist1.size(); i++) {
//                                            array[i] = sampletyelist1.get(i).getSampleName();
//                                        }
//                                        //样品类型
//                                        for (SampleTypeData sampleTypeData : sampletyelist1) {
//                                            if (sampleId == null) {
//                                                sampleId = sampleTypeData.getSampleId();
//                                            }
//                                        }
//
//                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp_yplx.setAdapter(adapter);
//                                        break;
//                                    case 2:
//                                        sampletyelist2 = sampleTypeJsonRootBean.getData();
//                                        array = new String[sampletyelist2.size()];
//                                        sp_yplxArray = new String[sampletyelist2.size()];
//                                        sp_yplxArrayId = new String[sampletyelist2.size()];
//                                        for (int i = 0; i < sampletyelist2.size(); i++) {
//                                            array[i] = sampletyelist2.get(i).getSampleName();
//                                            sp_yplxArrayId[i] = sampletyelist2.get(i).getSampleId();
//                                            sp_yplxArray[i] = sampletyelist2.get(i).getSampleName();
//                                        }
//                                        adapter = new ArrayAdapter<String>(CheckActivityByMen.this, android.R.layout.simple_spinner_dropdown_item, array);
//                                        sp_yplx1.setAdapter(adapter);
//                                        break;
//                                    case 3:
//                                        break;
//                                }
//
//                            }
//                        });
//                    }
//                } else{
////                    Timber.i("-----------1---------------------");
//                    String data = sampleTypeJsonRootBean.getErrMsg();
////                    Timber.i(data);
//                    GT.toast(CheckActivityByMen.this, data);
//                }
//
//
//            }
//        });

    }

    /**
     * 实现样品类型二级联动
     *
     * @param dataList
     * @param selectValue
     * @param level
     */
    private void loadSelectSampleTypeData(List<SampleTypeData> dataList, String selectValue, int level) {

        //如果到第三级了就直接返回不再联动
        if (level > 2) {
            return;
        }
        level++;

        for (SampleTypeData data : dataList) {
            if (data.getSampleName().equals(selectValue)) {
                String sampleId = data.getSampleId();
                initSampleTypes(sampleId, level);
            }
        }

    }

    // 向LineModel数据表中存储数据
    private List<ResultModel> result_list = null;

    // 存储已检测数据中的检测项目信息
    private String[] project_name_list = new String[0];

    private void save_projectname_to_LineModel() {
        result_list = new ArrayList<ResultModel>();
        query_add();
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

        if (result_list.size() > 0 && project_name_list.length == 0) {
            project_name_list = new String[1];
            project_name_list[0] = result_list.get(0).project_name;
        }


//        CardCompanyModel cardModel = new CardCompanyModel("奥本", "320", "920", "140", "320");
        CardCompanyModel cardModel = new CardCompanyModel("奥本", selectedProject.getScanStart(), selectedProject.getScanEnd(), selectedProject.getCTWidth(), selectedProject.getCTDistance());


        // 向数据库保存检测项目的数据
        for (int i = 0; i < result_list.size(); i++) {
            project_name_list = insert(project_name_list, result_list.get(i).project_name);
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

    private static String[] insert(String[] arr, String str) {

        int size = arr.length; // 获取原数组长度
        int newSize = size + 1; // 原数组长度加上追加的数据的总长度
        // 新建临时字符串数组
        String[] tmp = new String[newSize];

        for (int j = 0; j < size; j++) {
            tmp[j] = arr[j];
        }


        for (int i = 0; i < size; i++) {
            if (tmp[i].equals(str)) {
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

    // 获取ResultModel数据表中的数据添加到result_list数组中
    public void query_add() {

        WhereBuilder whereBuilder = WhereBuilder.b();
        try {
            result_list.clear();
            List<ResultModel> list = db.findAll(Selector.from(ResultModel.class).where(whereBuilder));
            result_list.addAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /*
        单金标数据管理页面样品名称[数据库存储]
     */

    // 存储检测记录中有的数据信息
    private String[] sample_name_list = new String[0];

    private void save_samplename_to_SampleModel() {
        result_list = new ArrayList<ResultModel>();
        query_add();
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

        if (result_list.size() > 0 && sample_name_list.length == 0) {
            sample_name_list = new String[1];
            sample_name_list[0] = result_list.get(0).sample_name;
        }


        // 向数据库保存检测项目的数据
        for (int i = 0; i < result_list.size(); i++) {
            sample_name_list = insert(sample_name_list, result_list.get(i).sample_name);
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


    /*
        修改数据管理页面样品类型[数据库存储数据]
     */

    // 存储检测记录中有的数据信息

    private String[] sample_type_list = new String[0];

    private void save_sampletype_to_SampleTypeModel() {
        result_list = new ArrayList<ResultModel>();
        query_add();
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

        if (result_list.size() > 0 && sample_type_list.length == 0) {
            sample_type_list = new String[1];
            sample_type_list[0] = result_list.get(0).sample_type;
        }


        // 向数据库保存检测项目的数据
        for (int i = 0; i < result_list.size(); i++) {
            sample_type_list = insert(sample_type_list, result_list.get(i).sample_type);
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


    /**
     * 数据管理模块中的检测单位和检测人员的保存
     */


    private String[] test_unit_list = new String[0];
    private String[] test_person_list = new String[0];

    private void save_test_unit_SampleTypeModel() {
        result_list = new ArrayList<ResultModel>();
        query_add();
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
//            e.printStackTrace();
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

        if (result_list.size() > 0 && test_unit_list.length == 0) {
            test_unit_list = new String[1];
            test_unit_list[0] = result_list.get(0).company_name;
        }
        if (result_list.size() > 0 && test_person_list.length == 0) {
            test_person_list = new String[1];
            test_person_list[0] = result_list.get(0).persion;

        }

        // 向数据库保存检测项目的数据
        for (int i = 0; i < result_list.size(); i++) {
            test_unit_list = insert(test_unit_list, result_list.get(i).company_name);
        }

        for (int i = 0; i < result_list.size(); i++) {
            test_person_list = insert(test_person_list, result_list.get(i).persion);
        }

        if (test_unit_list.length > list2.size()) {
            list1 = new ArrayList<>();
            // 每次添加最后一条数据
            list1.add(new PeopleModel(test_unit_list[test_unit_list.length - 1], 1));

            try {
                db.saveAll(list1);
            } catch (DbException e) {
//                e.printStackTrace();
            }

            try {
                list = db.findAll(Selector.from(PeopleModel.class).where(whereBuilder));
            } catch (Exception e) {
//                e.printStackTrace();
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
//                e.printStackTrace();
            }

            try {
                list = db.findAll(Selector.from(PeopleModel.class).where(whereBuilder));
            } catch (Exception e) {
//                e.printStackTrace();
            }
        } else {
        }
    }


    /**
     * 重新定位
     */

    private int count = 3;//定位次数
    private LocationService locationService;//定位服务

    private LocationReceiver locationReceiver;


    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                LocationX = intent.getStringExtra("LocationX");
                LocationY = intent.getStringExtra("LocationY");
                LocationAddress = intent.getStringExtra("LocationAddress");

                GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                    @Override
                    public void run() {
                        if (!"".equals(LocationAddress) && !"".equals(LocationX) && !"".equals(LocationY)) {
                            if (isLocation) {
                                APPUtils.showToast(CheckActivityByMen.this, "定位成功");
                            }
                            isLocation = false;
                            et_jcxxdz.setText(LocationAddress);
                        } else {
                            if (isLocation) {
                                APPUtils.showToast(CheckActivityByMen.this, "定位失败");
                            }
                            isLocation = false;
                            et_jcxxdz.setText("");

                            // 配置定位信息
//                            set_dingwei_info();
                        }

                    }
                });
            }


        }

    }

    private boolean isLocation = false;

    private void Location() {

        isLocation = true;
        if (locationReceiver != null) {
            unregisterReceiver(locationReceiver);
        }

        Intent intent = new Intent("LocationAction");
        intent.putExtra("locationMessage", "开始定位");
        sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新

        // 注册广播
        locationReceiver = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SendMessage");
        registerReceiver(locationReceiver, intentFilter); //注册广播
    }


    public void ClickDraw1(View v) {

        if (isTest) {
            APPUtils.showToast(this, "正在获取图像，请稍后...");
            return;
        }


        int sou = selectedProject.source;
        if (sou == 3) sou = 2;

        final String message = "GetData" + "," + sou + ","
                + selectedProject.ScanStart + "," + selectedProject.ScanEnd + ","
                + selectedProject.CTWidth + "," + selectedProject.CTDistance + ","
                + selectedProject.getLjz();

        if(mChart != null){
            mChart.clear();
        }

        byte[] data = message.getBytes(Charset.forName("gb2312"));
        if (!SerialUtils.COM3_SendData(data)) {
            isTest = false;
            APPUtils.showToast(this, "数据发送失败");
            return;
        }
        ToolUtils.showHUD(CheckActivityByMen.this, "请稍等...");
        new Thread() {

            @Override
            public void run() {
                SystemClock.sleep(5000);
                int minDataLenght = (Integer.parseInt(selectedProject.ScanEnd) - Integer.parseInt(selectedProject.ScanStart)) * 2 + 6; //  +5是因为dr值至少为形如0.123，所以至少要+6
                if (isFinishing()) {
                    return;
                }
                imageData = null;
                imageData = RecImageData(minDataLenght);
                if (imageData == null) {
                    APPUtils.showToast(CheckActivityByMen.this, "图像数据获取失败");
                    return;
                }
                handlerMess1.sendEmptyMessage(100);
            }
        }.start();

    }

    private byte[] buffer1;
    private int len1;

    private byte[] RecImageData(int minDataLength) {
        byte[] response = new byte[4096];
//		int select = HardwareControler.select(ToolUtils.devfd, 2, 20);
//		if(select == 0){
//			return null;
//		}
        int currentDataLength = 0;
        int errorCount = 0;
        byte[] buffer = new byte[1024];
        while (errorCount < 100) {
            int len = HardwareControler.read(Global.DEV_COM3, buffer, buffer.length);
            len1 = len;
            if (len < 1) {
                errorCount++;
            } else {
                buffer1 = buffer;
                System.arraycopy(buffer, 0, response, currentDataLength, len);
                Array.setByte(buffer, 0, (byte) '\0');
            }
            currentDataLength += len;
            if (currentDataLength >= minDataLength) {
                break;
            }
            SystemClock.sleep(500);
        }
        if (errorCount >= 100) {
            return null;
        }
        int index = -1;
        for (int i = 0; i < currentDataLength; i++) {
            if (response[i] == ',') {
                index = i;
                break;
            }
        }
        if (index == -1) {   //如果不包含逗号，则说明没有dr值，数据不完整
            return null;
        }

        String linjie_str = etLjz.getText().toString();

        float linjie_num = Float.parseFloat(linjie_str);


        final String drValue = new String(response, 0, index + 1, Charset.forName("gbk")).replace("OK", "").replace(",", "");
        GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
            @Override
            public void run() {
                if (etDr != null) {
                    etDr.setText(drValue);
                }
            }
        });

        float jiance_num = -10f;

//        try {
//            if (!"No signal".equals(drValue)) {
//                jiance_num = Float.parseFloat(drValue);
//            } else {
//                jiance_num = -10f;
//            }
//        } catch (NumberFormatException e) {
//            jiance_num = -10f;
//        }

        try {
            if ("No signal".equals(drValue)) {
                jiance_num = -10f;

            } else if(drValue.endsWith("error")){
                jiance_num = -5f;
            } else {
                jiance_num = Float.parseFloat(drValue);
            }
        } catch (NumberFormatException e) {
            jiance_num = -10f;
        }



        if (jiance_num == -10f) {
            GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                @Override
                public void run() {
                    if (etResult != null) {
                        etResult.setText("无效值");
                    }
                }
            });
        } else if(jiance_num == -5f) {
            GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                @Override
                public void run() {
                    if (etResult != null) {
                        etResult.setText("结果异常");
                    }
                }
            });
        } else {
            if (jiance_num <= linjie_num) {
                GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                    @Override
                    public void run() {
                        if (etResult != null) {
                            etResult.setText("阳性");
                        }
                    }
                });
            } else {
                GT.Thread.runAndroid(CheckActivityByMen.this, new Runnable() {
                    @Override
                    public void run() {
                        if (etResult != null) {
                            etResult.setText("阴性");
                        }
                    }
                });
            }
        }

        byte[] data = new byte[currentDataLength - index - 1];
        System.arraycopy(response, index + 1, data, 0, data.length);


        return data;
    }


    private Handler handlerMess1 = new Handler() {
        @Override
        @SuppressLint("HandlerLeak")//
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    int[] ints = ToolUtils.byte2Int(imageData);

                    new_ints = ints;

                    Intent intent = new Intent();
                    intent.putExtra("ints", ints);
                    (CheckActivityByMen.this).onActivityResult(CheckActivityByMen.REQUEST_CODE, Activity.RESULT_OK, intent);

                    String ints_data = "";
                    for (int i = 0; i < ints.length; i++) {
                        ints_data += ints[i] + ",";
                    }
                    int scanStart = Integer.parseInt("300");
                    InitChart();
                    try {
                        setData(ints, scanStart);
                        savaImage();

                        // 获取图片路径
                        String imgaepath = getImagePath();

                        // 转base64编码
                        image_base64 = imageToBase64(imgaepath);
                        image_base64 = image_base64.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");

                        // 数据上传
                        uploadResult();

                        // 保存新表
                        saveCheck_ResultData(etResult.getText().toString());
                        handler.sendEmptyMessage(100);//关闭定时器
                    } catch (ClassCastException e1) {
                        APPUtils.showToast(CheckActivityByMen.this, "接收数据错误");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        ToolUtils.hiddenHUD();
                        isTest = false;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private void InitChart() {


        mChart.getDescription().setEnabled(false);// no description text

        mChart.setTouchEnabled(true);// enable touch gestures

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(6000);
        leftAxis.setAxisMinimum(0);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(10);
        // if disabled, scaling can be done on X0- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.getAxisRight().setEnabled(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMaximum(6000);
        rightAxis.setAxisMinimum(0);
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        rightAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(10);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

    }


    public void setData(int[] imageData, int scanStart) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMaximum(scanStart + imageData.length);
        xAxis.setAxisMinimum(scanStart);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        // if disabled, scaling can be done on X0- and y-axis separately
        mChart.setPinchZoom(true);
        xAxis.setLabelCount(20);


        ArrayList<Entry> yVals = new ArrayList<Entry>();
        String sum = "";
        for (int a : imageData) {
            sum += a + ",";
        }
        for (int i = 0; i < imageData.length; i++) {
            yVals.add(new Entry(scanStart + i, imageData[i]));
        }

        LineDataSet set1;

        set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.5f);
        set1.setDrawCircles(false);
        set1.setLineWidth(3.0f);
        set1.setCircleRadius(40f);
        set1.setCircleColor(Color.WHITE);
//			set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.RED);
        set1.setDrawFilled(false);
//			set1.setFillColor(Color.WHITE);
//			set1.setFillAlpha(100);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTypeface(Typeface.DEFAULT);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        // set data
        mChart.setData(data);

        mChart.getLegend().setEnabled(false);

//			mChart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        mChart.invalidate();
    }


    public void savaImage() {

        // 根目录路径
        String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        // 保存图片
        GT.ApplicationUtils.saveImage(CheckActivityByMen.this, mChart, appSavePath, "曲线.png");

    }


    public String getImagePath() {
        String image_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "曲线.png";
        return image_Path;
    }


    // 图片的base64编码字符串
    public String image_base64 = "";

    /**
     * 图片转base64编码
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * base64编码转图片
     */
    public static boolean base64StrToImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        byte[] b = Base64.decode(imgStr, 1);
        try {
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //文件夹不存在则自动创建
            File tempFile = new File(path);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 展示绘图曲线
     */

    private int[] new_ints;

    public void showEditDialog(View view) {

        if (new_ints == null) {
            GT.toast(CheckActivityByMen.this, "还没有检测数据，无法查看绘图");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("samplename", "");
        bundle.putIntArray("ints", new_ints);
//        DialogFragmentPaint.newInstance(bundle).show(CheckPaintActivity.this.getSupportFragmentManager(), "");
        DialogFragmentPaint.newInstance(bundle).show(CheckActivityByMen.this.getSupportFragmentManager(), "");


    }

    @Override
    protected void onResume() {

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        MyApplication.keepScreenOn(getApplicationContext(), true);
        super.onResume();
    }


    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map2;

    /**
     * 获取样品名称接口数据
     */

    // "ProductType":"animal","ProductType":"produce","ProductType":"fishery"

    private List<NewSampleNameData> NewSampleNameData_List;
    private int request_count = 0;
    private int select_position = -1;

    private void initSampleNameSpinner(){

        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;

//        String[] ProductTypes = new String[]{"produce","animal","fishery"};
        String[] ProductTypes = new String[]{"produce"};

        map2.clear();
        map2.put("AreaId", AreaId);
        map2.put("OperatorId", OperatorId);

        NewSampleNameData_List = new ArrayList<>();

        for(int i=0;i<ProductTypes.length;i++){
            map2.put("ProductType",ProductTypes[i]);

            // http://www.shionda.com:7011/api//CommonManual/GetProductList
            new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "/CommonManual/GetProductList", map2, new GT.HttpUtil.OnLoadDataListener() {
                @Override
                public void onSuccess(String response, Object o) {
                    String body = response;
//                    Timber.i("body=" + body);

                    NewSampleNameDataBean newSampleNameDataBean = new Gson().fromJson(body, NewSampleNameDataBean.class);

                    if("0".equals(newSampleNameDataBean.getErrCode())){
                        if(newSampleNameDataBean != null){
                            List<NewSampleNameData> list1 = newSampleNameDataBean.getData();
                            if(list1 != null){
                                NewSampleNameData_List.addAll(list1);

                                request_count++;
                            }
                            if(request_count == 1){
                                if(NewSampleNameData_List != null){
                                    if(NewSampleNameData_List.size()>0){

                                        sampleSpinner.setVisibility(View.VISIBLE);
                                        tv_check_sample.setVisibility(View.GONE);
                                        String[] sampleNameList = new String[NewSampleNameData_List.size()];
                                        for(int j=0;j<NewSampleNameData_List.size();j++){
                                            sampleNameList[j] = NewSampleNameData_List.get(j).getObjectName();
                                        }
                                        sample_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, sampleNameList);
                                        sampleSpinner.setAdapter(sample_adapter);
                                    }else{
                                        sampleSpinner.setVisibility(View.GONE);
                                    }
                                }
                                request_count = 0;
                            }
                        }
                    } else{
//                        Timber.i("-----------2---------------------");
                        String data = newSampleNameDataBean.getErrMsg();
//                        Timber.i(data);
                        GT.toast(CheckActivityByMen.this, newSampleNameDataBean.getErrMsg());
                    }

                }

                @Override
                public void onError(String response, Object o) {

                }
            }, false);
//            new GT.OkGo(InterfaceURL.BASE_URL + "/CommonManual/GetProductList", map2).loadDataPost(new StringCallback() {
//                @Override
//                public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                    String body = response.body();
////                    Timber.i("body=" + body);
//
//                    NewSampleNameDataBean newSampleNameDataBean = new Gson().fromJson(body, NewSampleNameDataBean.class);
//
//                    if("0".equals(newSampleNameDataBean.getErrCode())){
//                        if(newSampleNameDataBean != null){
//                            List<NewSampleNameData> list1 = newSampleNameDataBean.getData();
//                            if(list1 != null){
//                                NewSampleNameData_List.addAll(list1);
//
//                                request_count++;
//                            }
//                            if(request_count == 1){
//                                if(NewSampleNameData_List != null){
//                                    if(NewSampleNameData_List.size()>0){
//
//                                        sampleSpinner.setVisibility(View.VISIBLE);
//                                        tv_check_sample.setVisibility(View.GONE);
//                                        String[] sampleNameList = new String[NewSampleNameData_List.size()];
//                                        for(int j=0;j<NewSampleNameData_List.size();j++){
//                                            sampleNameList[j] = NewSampleNameData_List.get(j).getObjectName();
//                                        }
//                                        sample_adapter = new ArrayAdapter<String>(CheckActivityByMen.this, R.layout.item_simple_spiner, sampleNameList);
//                                        sampleSpinner.setAdapter(sample_adapter);
//                                    }else{
//                                        sampleSpinner.setVisibility(View.GONE);
//                                    }
//                                }
//                                request_count = 0;
//                            }
//                        }
//                    } else{
////                        Timber.i("-----------2---------------------");
//                        String data = newSampleNameDataBean.getErrMsg();
////                        Timber.i(data);
//                        GT.toast(CheckActivityByMen.this, newSampleNameDataBean.getErrMsg());
//                    }
//
//                }
//            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("zdl","=========onActivityResult========requestCode====="+requestCode);
        Log.d("zdl","=========onActivityResult=======resultCode======"+resultCode);
        if (data != null){
            //这边 resultCode 这个值   如果你要返回到这个页面并且穿参数  就需要写不同的code
            if (requestCode == 1001 && resultCode == 502){
                String companyName = data.getStringExtra("companyName");
                String companyNum = data.getStringExtra("companyNum");
                et_companyCode.setText(companyNum);
                tv_CheckedEnterprise.setText(companyName);
                Log.d("zdl","=========onActivityResult=======companyName======"+companyName);
            }
        }
    }
    //接收添加企业传过来的消息
    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void onEventMainThread(AddComBean addComBean) {
        String companyName = addComBean.companyName;
        String companyNum = addComBean.companyNum;
        et_companyCode.setText(companyNum);
        tv_CheckedEnterprise.setText(companyName);
        //每次用完需要移除
        EventBus.getDefault().removeStickyEvent(addComBean);
        Log.i("lcy", "onEventMainThread: ---CheckActivityByMen拿到了值--"+addComBean.companyName);
    }

}


