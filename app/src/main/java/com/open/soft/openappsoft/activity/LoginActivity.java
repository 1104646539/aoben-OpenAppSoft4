package com.open.soft.openappsoft.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.utils.http.AreaResultBean;
import com.example.utils.http.Global;
import com.example.utils.http.KnowledgeResultBean;
import com.example.utils.http.LoginBean;
import com.example.utils.http.LoginResultBean;
import com.example.utils.http.OrderPresenter;
import com.example.utils.http.Result;
import com.example.utils.http.RetrofitServiceManager;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.App;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.data2.JiaMiData;
import com.open.soft.openappsoft.data2.JiaMiDataBean;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.open.soft.openappsoft.multifuction.model.CheckResult;
import com.open.soft.openappsoft.multifuction.util.Network;
import com.open.soft.openappsoft.multifuction.util.SerialUtils;
import com.open.soft.openappsoft.util.APPUtils;
import com.open.soft.openappsoft.util.InterfaceURL;
import com.open.soft.openappsoft.util.SharedPreferences2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

public class LoginActivity extends Activity implements OrderPresenter.OrderInterface, View.OnClickListener {
    private static final String TAG = "LoginActivity";
    Spinner spinner;
    EditText et_user, et_psw;
    OrderPresenter orderPresenter;
    ProgressDialog progressDialog;
    Button btn_login;
    Button btn_login_admin;
    private TextView tv_homeLocation;
    public static SharedPreferences2 sp_ServiceUrl;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAreaList();
                    }
                }, 1000 * 3);
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        //App异常打印
//        GT.LOG.initAppErrLogTry(this);
//        GT.LOG.LOG_FILE_TF = true;//开启本地打印

        sp_ServiceUrl = new SharedPreferences2(this, "companyName");

        spinner = findViewById(R.id.spr_pt);
        et_user = findViewById(R.id.et_user);
        et_psw = findViewById(R.id.et_psw);
        btn_login = findViewById(R.id.btn_login);
        btn_login_admin = findViewById(R.id.btn_login_admin);
        btn_login_admin.setOnClickListener(this);
        initSp();
        RetrofitServiceManager.getInstance();
        orderPresenter = new OrderPresenter();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录中，请等待……");
        progressDialog.setCancelable(false);//设置无法在登录的时候取消
//        progressDialog.show();
        btn_login.setOnClickListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        registerReceiver(receiver, filter);

        initUserPass();//登录上次的账号密码
        softKeyboard(); //收起软键盘

        //设置标题
        TextView tv_title = findViewById(R.id.tv_title);
        String title = sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            tv_title.setText(InterfaceURL.oneModule);
        } else {
            tv_title.setText(title);
        }
        //tv_title.setText(InterfaceURL.oneModule);

        //检测App更新
        if (InterfaceURL.isDetectionAppUpdate) {
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在检测新版本");
            progressDialog.setCancelable(false);
            APPUtils.updateApp(this, progressDialog);//检测App更新
        }

        //是否打开测试
        if (!InterfaceURL.isOpenTest) {
            btn_login_admin.setVisibility(View.GONE);
        }

        //长按进入归属地
        /*findViewById(R.id.tv_homeLocation).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(LoginActivity.this, SettingPTActivity.class));
                return true;
            }
        });*/


        if (sp_ServiceUrl != null) {
            Object name1 = sp_ServiceUrl.query("name");
            String name = name1.toString();
            if (!"0".equals(name)) {
                InterfaceURL.companyName = name;//赋值
                // 修改
                if ("北京奥本生物技术有限公司".equals(name)) {
                    Global.company_name = "Aoben";
                } else {
                    Global.company_name = "Xindaan";
                }
            }
        }


        tv_homeLocation = findViewById(R.id.tv_homeLocation);
        String verName = GT.ApplicationUtils.getVerName(this);
        tv_homeLocation.setText(InterfaceURL.companyName + " " + "v" + verName);


        getEncryptionMethod();
        //初始化串口
//        SerialUtils.InitSerialPort(this);
        // 强制竖屏
//        GT.Window.AutoLandscapeAndPortrait(this,2);
        //打印测试

        // 胶体金数据打印[朝外打印]
//        StringBuffer sb = new StringBuffer("\n");
//        for (int i=0;i<2;i++) {
//            sb.append("检测时间：");
//            sb.append("2023-05-31 09:27:39" + "\n");
//
//            sb.append("检测结果：");
//            sb.append("阴性" + "\n");
//
//            sb.append("检 测 值：");
//            sb.append("2.103" + "\n");
//
//
//            sb.append("检 测 限：");
//            sb.append("5.000ppb" + "\n");
//
//            sb.append("样品编号：");
//            sb.append("t" + "\n");
//
//            sb.append("样品来源：");
//            sb.append("北京市，市辖区，东城区" + "\n");
//
//
//            sb.append("检测项目：");
//            sb.append("黄曲霉毒素B1" + "\n");
//
//            sb.append("样品名称：");
//            sb.append("f" + "\n");
//
//            sb.append("样品类型：");
//            sb.append("果蔬" + "\n");
//
//            sb.append("检 验 员：");
//            sb.append("信达安测试用户" + "\n");
//
//            sb.append("检测单位：");
//            sb.append("信达安" + "\n");
//
//            sb.append("多参数食品安全检测仪" + "\n\n");
//
//        sb.append("\n\n");
//        }

        // 分光光度数据打印[朝外打印]
//        StringBuffer sb = new StringBuffer();
//        for (int j = 0; j < 2; j++) {
//        sb.append("\n\n");
//        sb.append("检测时间：");
//        sb.append("qwe" + "\n");
//
//        sb.append("检测结果：");
//        sb.append("qwe" + "\n");
//
//        sb.append("抑 制 率：");
//        sb.append("qwe" + "\n");
//
//        sb.append("检 测 限：");
//        sb.append("qwe" + "\n");
//
//        sb.append("样品编号：");
//        sb.append("qwe" + "\n");
//
//            sb.append("样品来源：");
//            sb.append("qwe" + "\n");
//
//        sb.append("检测项目：");
//        sb.append("qwe" + "\n");
//
//        sb.append("样品名称：");
//        sb.append("qwe" + "\n");
//
//        sb.append("样品类型：");
//        sb.append("qwe" + "\n");
//
//        sb.append("检 验 员：");
//        sb.append("qwe" + "\n");
//
//            sb.append("qwe" + "\n");
//            sb.append("检测单位：");
//
//        sb.append("通 道 号：");
//        sb.append("qwe" + "\n");
//
//        sb.append(title + "\n\n\n\n\n");
//    }
//        sb.append("\n\n");

//            byte[] data  = sb.toString().getBytes(Charset.forName("GBK"));
//
//            Log.d(TAG, "data:" + new String(data));
//            SerialUtils.COM4_SendData(data);
//
    }


    //初始化 上次登录过的账号密码
    private void initUserPass() {

        //保存登录成功的账号密码
        SharedPreferences sp = getSharedPreferences("userPass", MODE_PRIVATE);
        String user = sp.getString("user", "");
        String pass = sp.getString("pass", "");
        if (user.length() > 0 && pass.length() > 0) {
            et_user.setText(user);
            et_psw.setText(pass);
        } else {
            et_user.setText("admin");
            et_psw.setText("123456");
        }
    }

    //收起软键盘
    private void softKeyboard() {

        new Thread(() -> {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //收起软键盘
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(et_user.getWindowToken(), 0);

        }).start();

    }

    private void initSp() {

        String url_api = sp_ServiceUrl.query("url_api", Global.BASE_URL).toString();
        GT.logs("查询出来的URL:" + url_api);
//        if (!"0".equals(url_api)) {
//            Global.BASE_URL = InterfaceURL.BASE_URL = url_api;
//        } else {
//            Global.BASE_URL = InterfaceURL.BASE_URL;
//            sp_ServiceUrl.save("url_api", InterfaceURL.BASE_URL);//默认是测试服务器
//        }
        Global.BASE_URL = url_api;
//        Global.BASE_URL = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(SPResource.KEY_UPLOAD_URL, com.example.utils.http.Global.BASE_URL);
        Global.admin_psw = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_ADMIN_PSW, Global.admin_psw);
        Global.admin_pt = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_ADMIN_PT, Global.admin_pt);
        Global.URL_LOGIN = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_URL_LOGIN, Global.URL_LOGIN);
//        Global.URL_GetAreaList = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_URL_GetAreaList, Global.URL_GetAreaList);
//        Global.URL_GetCardQRInfo = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_URL_GetCardQRInfo, Global.URL_GetCardQRInfo);
//        Global.URL_GetSamplingInfo = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_URL_GetSamplingInfo, Global.URL_GetSamplingInfo);
        Global.URL_SendResult = SharedPreferencesUtil.getDefaultSharedPreferences(this).getString(Global.SP_URL_SendResult, Global.URL_SendResult);
//        Log.d("onSettingPsw", "initSp onUrlSave admin_psw=" + Global.admin_psw);
    }

    void getAreaList() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (Network.isNetwork(LoginActivity.this)) {
        progressDialog.show();
//        orderPresenter.GetAreaList(LoginActivity.this);
//                } else {
//                    progressDialog.setCancelable(false);
//                    progressDialog.setMessage("请先连接网络");
//                    progressDialog.show();
//                }
//            }
//        }, 1000 * 3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    NetworkReceiver receiver;

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "intent============>>>>" + intent.toString());
//            if (!isSuccess) {
//                handler.sendEmptyMessage(1);
//            }
        }
    }

    Result<List<AreaResultBean>> result;
    boolean isSuccess = false;

    @Override
    public void getAreaListSuccess(Result<List<AreaResultBean>> result) {
        isSuccess = true;
        this.result = result;
        progressDialog.dismiss();
        if (result.getData() != null) {
            String[] items = new String[result.getData().size()];
            for (int i = 0; i < result.getData().size(); i++) {
                items[i] = result.getData().get(i).Name;
            }
            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items));
        }
//        Toast.makeText(this, "获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getAreaListFailed(String msg) {
        isSuccess = false;
//        progressDialog.setMessage("请先连接网络");
        progressDialog.dismiss();
        Toast.makeText(this, "获取失败" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logInSuccess(LoginResultBean resultBean) {

//        if ("input".equals(resultBean.getSamplingMode())) {
//            Global.isVoluntarily = false;//不将自动录入信息
//            Global.ismixedentry = false; // 不是混合录入
//        } else if ("scan".equals(resultBean.getSamplingMode())) {
//            Global.isVoluntarily = true;//自动录入信息
//            Global.ismixedentry = false; // 不是混合录入
//        } else {
//            Global.ismixedentry = true;
//        }
        Global.NAME = user;
        SharedPreferences sp = this.getSharedPreferences("userPass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();   //获取编辑器
//        editor.putString("DeptId", resultBean.getDeptId()); //存入部门ID
        editor.putString("salt", resultBean.getSalt()); //存入salt秘钥
        editor.apply();                //提交修改，否则不生效
        Global.SALT = resultBean.getSalt();
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//        Global.NAME = resultBean
//        Global.NAME = resultBean.getUserName();
//        Global.Dept = resultBean.getDeptName();
//        Global.ID = resultBean.getLogId();
//        Global.SamplingMode = resultBean.getSamplingMode();
//        Global.NEEDCompanyCode = resultBean.getNeedCompanyCode();
        progressDialog.dismiss();
        Intent start = new Intent(this, MainActivity.class);
        startActivity(start);
        finish();
    }

    @Override
    public void logInFailed(String msg) {
        progressDialog.dismiss();
        Toast.makeText(this, "登录失败" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getKnowledgeSuccess(Result<List<KnowledgeResultBean>> result) {
    }

    @Override
    public void getKnowledgeFailed(String msg) {
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //判断是否为第一次 Admin 登录
                if ("Admin".equals(et_user.getText().toString()) || "admin".equals(et_user.getText().toString()) || "ADMIN".equals(et_user.getText().toString())) {
                    startAdmin();
                    return;
                }

                if (verify()) {
                    getEncryptionMethod();
                    login();
                }
                break;
            case R.id.tv_get_list:
                isSuccess = false;
                getAreaList();
                break;
            case R.id.btn_login_admin:
                startAdmin();
                break;
        }
    }

    private void startAdmin() {
        Global.isAdimin = true;
        Global.NAME = "admin";
        // 修改
        Global.isVoluntarily = true;
        Intent start = new Intent(this, MainActivity.class);
        startActivity(start);
        finish();
    }

    String user;
    String psw; // 原本密码
    String jiamipsw; // 加密后的密码

    private boolean verify() {
        user = et_user.getText().toString();
        psw = et_psw.getText().toString();
//        if (result != null && !result.getData().isEmpty()) {
//            pt = result.getData().get(spinner.getSelectedItemPosition()).Id;
//        }

        if (user == null || user.isEmpty()) {
            APPUtils.showToast(this, "请输入帐号");
            return false;
        } else if (psw == null || psw.isEmpty()) {
            APPUtils.showToast(this, "请输入密码");
            return false;
        }

        if (Global.admin_user.equals(user) && Global.admin_psw.equals(psw)) {
            //管理员模式开启
            Global.isAdimin = true;
        }
        Log.d("onSettingPsw", "initSp onUrlSave admin_psw=" + Global.admin_psw + " psw=" + psw + " Global.admin_pt=" + Global.admin_pt);
        if (!Global.isAdimin) {
//            if (Global.admin_pt == null || Global.admin_pt.isEmpty()) {
//                APPUtils.showToast(this, "请先进入管理员帐户选择归属地");
//                return false;
//            }
            if (!Network.isNetwork(this)) {
                APPUtils.showToast(this, "请先联网后登录");
                return false;
            }

        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NewApi")
    private void login() {
        if (Global.isAdimin) {
            startAdmin();
            return;
        }
        String pass = psw;//存储真实密码
        Timber.i("存储真实账号user:" + user);
        Timber.i("存储真实密码psw:" + psw);


        int count = 0;

        while (true) {
            count++;
            if (reslut[0]) {
                break;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (count == 12) {
                break;
            }
        }

        //存储
        SharedPreferences sp = this.getSharedPreferences("userPass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();   //获取编辑器
        editor.putString("terrace", Global.admin_pt); //存入String型数据
        editor.apply();                //提交修改，否则不生效
        progressDialog.show();

        orderPresenter.login(new LoginBean(Global.SN, user, psw, "2", Global.KEY), this, this, pass, progressDialog);
    }

    public static String getMD5Str(String str) {
//        byte[] digest = null;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("md5");
//            digest = md5.digest(str.getBytes("utf-8"));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        //16是表示转换为16进制数
//        String md5Str = new BigInteger(1, digest).toString(16);
//        return md5Str;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int c = b & 0xff; //负数转换成正数
                String result = Integer.toHexString(c); //把十进制的数转换成十六进制的书
                if (result.length() < 2) {
                    sb.append(0); //让十六进制全部都是两位数
                }
                sb.append(result);
            }
            return sb.toString(); //返回加密后的密文
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public boolean[] reslut;

    public void getEncryptionMethod() {

        psw = et_psw.getText().toString();
        Timber.i("获取到的密码://" + psw + "//");
//        if ("".equals(psw) || psw == null) {
//            psw = et_psw.getText().toString();
//        }

        final boolean[] is_complete = {false};
        reslut = new boolean[1];
        new GT.HttpUtil().getRequest(InterfaceURL.BASE_URL + "System/GetAreaList", new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String string = response;
                Timber.i("string:" + string);
                JiaMiDataBean jiaMiDataBean = new Gson().fromJson(string, JiaMiDataBean.class);
                List<JiaMiData> data = jiaMiDataBean.getData();

                Timber.i("Global.admin_pt:" + Global.admin_pt);
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId().equals(Global.admin_pt)) {
                        if ("MD5".equals(data.get(i).getEncryptionType())) {
//                            jiamipsw = getMD5Str(psw).toUpperCase();
                            jiamipsw = getMD5Str(psw);
                            Timber.i("MD5加密后未处理的的密码://" + jiamipsw + "//");
                            jiamipsw = jiamipsw.trim();
                            Timber.i("MD5加密后的处理过的密码://" + jiamipsw + "//");
                            is_complete[0] = true;
                            reslut[0] = is_complete[0];
                            break;
                        } else if ("base64".equals(data.get(i).getEncryptionType())) {
                            jiamipsw = Base64.encodeToString(psw.getBytes(), Base64.DEFAULT);
                            Timber.i("base64加密后未处理的密码://" + jiamipsw + "//");
                            jiamipsw = jiamipsw.trim();
                            Timber.i("base64加密后的处理过的密码://" + jiamipsw + "//");
                            is_complete[0] = true;
                            reslut[0] = is_complete[0];
                            break;
                        }
                    }
                }
            }

            @Override
            public void onError(String response, Object o) {

            }
        });
//        new GT.OkHttp(InterfaceURL.BASE_URL + "System/GetAreaList").loadData(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String string = response.body().string();
//                Timber.i("string:" + string);
//                JiaMiDataBean jiaMiDataBean = new Gson().fromJson(string, JiaMiDataBean.class);
//                List<JiaMiData> data = jiaMiDataBean.getData();
//
//                Timber.i("Global.admin_pt:" + Global.admin_pt);
//                for (int i = 0; i < data.size(); i++) {
//                    if (data.get(i).getId().equals(Global.admin_pt)) {
//                        if ("MD5".equals(data.get(i).getEncryptionType())) {
////                            jiamipsw = getMD5Str(psw).toUpperCase();
//                            jiamipsw = getMD5Str(psw);
//                            Timber.i("MD5加密后未处理的的密码://" + jiamipsw + "//");
//                            jiamipsw = jiamipsw.trim();
//                            Timber.i("MD5加密后的处理过的密码://" + jiamipsw + "//");
//                            is_complete[0] = true;
//                            reslut[0] = is_complete[0];
//                            break;
//                        } else if ("base64".equals(data.get(i).getEncryptionType())) {
//                            jiamipsw = Base64.encodeToString(psw.getBytes(), Base64.DEFAULT);
//                            Timber.i("base64加密后未处理的密码://" + jiamipsw + "//");
//                            jiamipsw = jiamipsw.trim();
//                            Timber.i("base64加密后的处理过的密码://" + jiamipsw + "//");
//                            is_complete[0] = true;
//                            reslut[0] = is_complete[0];
//                            break;
//                        }
//                    }
//                }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getEncryptionMethod();
    }
}
