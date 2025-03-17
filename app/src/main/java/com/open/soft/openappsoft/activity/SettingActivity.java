package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.utils.http.Global;
import com.example.utils.http.RetrofitServiceManager;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.lzy.okgo.callback.StringCallback;
import com.open.soft.openappsoft.R;
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
    private TextView tv_mac_url;

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
        tv_mac_url = (TextView) findViewById(R.id.tv_mac_url);


        tv_mac_url.setText("Mac地址：" + MainActivity.mac_url);

        btn_open_1.setOnClickListener(this);
        btn_open_2.setOnClickListener(this);
        btn_open_3.setOnClickListener(this);
        btn_open_4.setOnClickListener(this);
        btn_open_5.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_open_1) {
            onUpLoadingSetting();
        } else if (v.getId() == R.id.btn_open_2) {
            onSettingPsw();
        } else if (v.getId() == R.id.btn_open_3) {
            onSettingPT();
        } else if (v.getId() == R.id.btn_open_4) {

            final EditText et_name = new EditText(this);
            final Spinner sp_name = new Spinner(this);

            initCn("M417", sp_name);

            //赋值
            String name = new GT.GT_SharedPreferences(this, "companyName", true).query("name").toString();
            if (!"0".equals(name)) {
                InterfaceURL.companyName = name;//赋值
                et_name.setText(name);
            }

            new AlertDialog.Builder(this).setTitle("请输入维护公司的名称")
                    .setIcon(android.R.drawable.sym_def_app_icon)
                    .setView(sp_name)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //按下确定键后的事件
//                            String name = sp_name.getText().toString();
                            String name = sp_name.getSelectedItem().toString();

                            if (null != name && !"null".equals(name) && name.length() > 0) {

                                // 修改
                                if ("信达安检测技术（天津）有限公司".equals(name)) {
                                    Global.company_name = "Xindaan";
                                } else {
                                    Global.company_name = "Aoben";
                                }

                                new GT.GT_SharedPreferences(SettingActivity.this, "companyName", true).save("name", name);
                                GT.toast(SettingActivity.this, "修改成功！");
                                InterfaceURL.companyName = name;
                            } else {
                                GT.toast(SettingActivity.this, "公司名不能为空！");
                            }
                        }
                    }).setNegativeButton("取消", null).show();


        }else if (v.getId() == R.id.btn_open_5) {
            if (setTitleDialog == null) {
                setTitleDialog = new SetTitleDialog(this);
            }
            setTitleDialog.showDialog("", "", 1);
            setTitleDialog.setOnConfirmListener(new SetTitleDialog.OnConfirmListener() {
                @Override
                public void onConfirmPw(String pw) {
                    LoginActivity.sp_ServiceUrl.save("TitleSet",pw);
                    Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

                Global.BASE_URL = InterfaceURL.BASE_URL = url;
                Global.URL_LOGIN = et_1;
                Global.URL_GetAreaList = et_2;
                Global.URL_GetCardQRInfo = et_3;
                Global.URL_GetSamplingInfo = et_4;
                Global.URL_SendResult = et_5;

                //修改保存到GT_SP中
                LoginActivity.sp_ServiceUrl.save("url_api", url);//修改默认是服务器

                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(SPResource.KEY_UPLOAD_URL, InterfaceURL.BASE_URL).commit();
                Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                RetrofitServiceManager.getInstance().refreshRetrofitServiceManager();

                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_LOGIN, Global.URL_LOGIN).commit();
                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_GetAreaList, Global.URL_GetAreaList).commit();
                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_GetCardQRInfo, Global.URL_GetCardQRInfo).commit();
                SharedPreferencesUtil.getDefaultSharedPreferences(SettingActivity.this).edit().putString(Global.SP_URL_GetSamplingInfo, Global.URL_GetSamplingInfo).commit();
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
