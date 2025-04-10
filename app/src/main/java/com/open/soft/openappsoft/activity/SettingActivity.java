package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.utils.http.Global;
import com.example.utils.http.RetrofitServiceManager;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.lzy.okgo.callback.StringCallback;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.orderinfo.EditInfoActivity;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoModel;
import com.open.soft.openappsoft.activity.samplename.SampleNameActivity;
import com.open.soft.openappsoft.atp.AtpArgActivity;
import com.open.soft.openappsoft.jinbiao.model.CompanyNameData;
import com.open.soft.openappsoft.jinbiao.model.CompanyNameRootBean;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.multifuction.activity.SystemSettingActivity2;
import com.open.soft.openappsoft.multifuction.dialog.EditURLDialog;
import com.open.soft.openappsoft.multifuction.dialog.SetTitleDialog;
import com.open.soft.openappsoft.multifuction.dialog.UploadingDialog;
import com.open.soft.openappsoft.multifuction.resource.SPResource;
import com.open.soft.openappsoft.multifuction.util.APPUtils;
import com.open.soft.openappsoft.multifuction.util.SerialUtils;
import com.open.soft.openappsoft.util.InterfaceURL;

import java.util.List;
import java.util.Map;

public class SettingActivity extends Activity implements View.OnClickListener {

    private Button btn_open_1;
    private Button btn_open_2;
    private Button btn_open_3;
    private Button btn_open_4;
    private Button btn_open_5;
    private Button btn_pdf;
    private Button btn_video;
    private Button btn_sample_type_main, btn_sample_type_child, btn_bcheck_ori, btn_check_ori, btn_sample;
    private TextView tv_mac_url;
    private TextView tv_title;

    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map;

    @GT.Annotations.GT_Collection.GT_List
    private List<CompanyNameData> companynameList;


    EditURLDialog editURLDialog;
    EditURLDialog editPswDialog;
    SetTitleDialog setTitleDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.getGT().build(this);
        setContentView(R.layout.activity_setting);

        btn_open_1 = (Button) findViewById(R.id.btn_open_1);
        btn_open_2 = (Button) findViewById(R.id.btn_open_2);
        btn_open_3 = (Button) findViewById(R.id.btn_open_3);
        btn_open_4 = (Button) findViewById(R.id.btn_open_4);
        btn_open_5 = (Button) findViewById(R.id.btn_open_5);
        btn_pdf = (Button) findViewById(R.id.btn_pdf);
        btn_video = (Button) findViewById(R.id.btn_video);
        btn_sample_type_main = (Button) findViewById(R.id.btn_sample_type_main);
        btn_sample_type_child = (Button) findViewById(R.id.btn_sample_type_child);
        btn_bcheck_ori = (Button) findViewById(R.id.btn_bcheck_ori);
        btn_check_ori = (Button) findViewById(R.id.btn_check_ori);
        btn_sample = (Button) findViewById(R.id.btn_sample);
        tv_mac_url = (TextView) findViewById(R.id.tv_mac_url);
        tv_title = (TextView) findViewById(R.id.tv_title);


        tv_mac_url.setText("Mac地址：" + MainActivity.mac_url);

        btn_open_1.setOnClickListener(this);
        btn_open_2.setOnClickListener(this);
        btn_open_3.setOnClickListener(this);
        btn_open_4.setOnClickListener(this);
        btn_open_5.setOnClickListener(this);
        btn_bcheck_ori.setOnClickListener(this);
        btn_check_ori.setOnClickListener(this);
        btn_sample_type_main.setOnClickListener(this);
        btn_sample_type_child.setOnClickListener(this);
        tv_title.setOnClickListener(this);
        btn_sample.setOnClickListener(this);
        btn_pdf.setOnClickListener(this);
        btn_video.setOnClickListener(this);

    }

    int clickSettings = 0;
    int What_Click_atp = 100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == What_Click_atp) {
                clickSettings = 0;
            }
        }
    };

    private void startAtpArg() {
        clickSettings++;
        if (clickSettings > 5) {
            clickSettings = 0;
            startActivity(new Intent(this, AtpArgActivity.class));
        } else {
            handler.removeMessages(What_Click_atp);
            handler.sendEmptyMessageDelayed(What_Click_atp, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_open_1) {
            onUpLoadingSetting();
        } else if (v.getId() == R.id.tv_title) {
            startAtpArg();
        } else if (v.getId() == R.id.btn_open_2) {
            onSettingPsw();
        } else if (v.getId() == R.id.btn_open_3) {
            onSettingPT();
        } else if (v.getId() == R.id.btn_open_4) {

            final EditText et_name = new EditText(this);
            final Spinner sp_name = new Spinner(this);

            initCn("M417", sp_name);

            et_name.setText(InterfaceURL.companyName);
            new AlertDialog.Builder(this).setTitle("请输入公司名")
                    .setView(et_name)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        //按下确定键后的事件
                        String name = et_name.getText().toString();

                        if (null != name && !"null".equals(name) && name.length() > 0) {
                            SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString("companyName", name).commit();
                            GT.toast(SettingActivity.this, "修改成功！");
                            InterfaceURL.companyName = name;
                        } else {
                            GT.toast(SettingActivity.this, "公司名不能为空！");
                        }
                    }).setNegativeButton("取消", null).show();


        } else if (v.getId() == R.id.btn_open_5) {
            if (setTitleDialog == null) {
                setTitleDialog = new SetTitleDialog(this);
            }
            setTitleDialog.showDialog("", InterfaceURL.oneModule, 1);
            setTitleDialog.setOnConfirmListener(new SetTitleDialog.OnConfirmListener() {
                @Override
                public void onConfirmPw(String pw) {
                    InterfaceURL.oneModule = pw;
                    SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString("oneModule", pw).commit();
                    Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (v.getId() == R.id.btn_sample_type_main) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("type", OrderInfoModel.type_sample_type_main);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sample_type_child) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("type", OrderInfoModel.type_sample_type_child);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bcheck_ori) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("type", OrderInfoModel.type_bcheck);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_check_ori) {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("type", OrderInfoModel.type_check);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sample) {
            Intent intent = new Intent(this, SampleNameActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_video) {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("title", "操作视频");
            startActivity(intent);
        } else if (v.getId() == R.id.btn_pdf) {
            String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            String pdf_name = "test2.pdf";
            Global.URI_MULT = appSavePath + pdf_name;
            openPDFInNative(this, Global.URI_MULT);
        }
    }

    public static void openPDFInNative(Context context, String FILE_NAME) {
        Intent intent = new Intent(context, FileActivity.class);
        intent.putExtra("isFile", true);
        intent.putExtra("url", FILE_NAME);
        intent.putExtra("fileType", "pdf");
        intent.putExtra("title", "厂家信息");
        context.startActivity(intent);
    }

    private void onSettingPT() {
        Intent intent = new Intent(this, SettingPTActivity.class);
        startActivity(intent);
    }

    private void onSettingPsw() {
        if (editPswDialog == null) {
            editPswDialog = new EditURLDialog(this);
        }
        editPswDialog.showDilaog(new EditURLDialog.OnOrderSave() {
            @Override
            public void onOrderSave(String msg) {
                Global.admin_psw = msg;
                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_ADMIN_PSW, Global.admin_psw).commit();
                Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                Log.d("onSettingPsw", "onSettingPsw onUrlSave admin_psw=" + Global.admin_psw);
            }
        }, "密码设置", "旧密码", "新密码", "再次输入密码");
    }


    /**
     * 上传设置
     *
     * @param
     */
    public void onUpLoadingSetting() {
        if (editURLDialog == null) {
            editURLDialog = new EditURLDialog(this);
        }
        editURLDialog.showDilaog(new EditURLDialog.OnUrlSave() {
            @Override
            public void onUrlSave(String url, String et_1, String et_2, String et_3, String et_4, String et_5) {
                if (et_1.isEmpty() || et_2.isEmpty() || et_3.isEmpty() || et_4.isEmpty() || et_5.isEmpty()) {
                    APPUtils.showToast(SettingActivity.this, "请输入");
                    return;
                }

                Global.BASE_URL = url;
                Global.URL_LOGIN = et_1;
//                Global.URL_GetAreaList = et_2;
//                Global.URL_GetCardQRInfo = et_3;
//                Global.URL_GetSamplingInfo = et_4;
                Global.URL_SendResult = et_5;

                //修改保存到GT_SP中
                LoginActivity.sp_ServiceUrl.save("url_api", url);//修改默认是服务器

                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(SPResource.KEY_UPLOAD_URL, InterfaceURL.BASE_URL).commit();
                Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                RetrofitServiceManager.getInstance().refreshRetrofitServiceManager();

                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_LOGIN, Global.URL_LOGIN).commit();
//                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_GetAreaList, Global.URL_GetAreaList).commit();
//                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_GetCardQRInfo, Global.URL_GetCardQRInfo).commit();
//                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_GetSamplingInfo, Global.URL_GetSamplingInfo).commit();
                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_SendResult, Global.URL_SendResult).commit();
            }

        });
    }

    private void initCn(String type, Spinner sp_name) {

        map.clear();
        map.put("type", type);


        // 企业名称数据
//        new GT.OkGo(InterfaceURL.BASE_URL + "/Other/GetCompanyNameList", map).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                String body = response.body();
//
//                //解析数据
//                CompanyNameRootBean companyNameRootBean = new Gson().fromJson(body, CompanyNameRootBean.class);
//
//                //为Sp设置数据
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        companynameList = companyNameRootBean.getData();
//                        String[] array = new String[companynameList.size()];
//                        for (int i = 0; i < companynameList.size(); i++) {
//                            array[i] = companynameList.get(i).getCompnayName();
//                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingActivity.this, android.R.layout.simple_list_item_multiple_choice, array);
//                        sp_name.setAdapter(adapter);
//
//                    }
//                });
//
//            }
//        });

        new GT.HttpUtil().getRequest(InterfaceURL.BASE_URL + "/Other/GetCompanyNameList", map, new GT.HttpUtil.OnLoadData() {
            @Override
            public void onSuccess(String response, Object o) {
                super.onSuccess(response, o);
                String body = response;

                //解析数据
                CompanyNameRootBean companyNameRootBean = new Gson().fromJson(body, CompanyNameRootBean.class);

                //为Sp设置数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        companynameList = companyNameRootBean.getData();
                        String[] array = new String[companynameList.size()];
                        for (int i = 0; i < companynameList.size(); i++) {
                            array[i] = companynameList.get(i).getCompnayName();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingActivity.this, android.R.layout.simple_list_item_multiple_choice, array);
                        sp_name.setAdapter(adapter);

                    }
                });
            }

            @Override
            public void onError(String response, Object o) {
                super.onError(response, o);
            }
        });
    }


}
