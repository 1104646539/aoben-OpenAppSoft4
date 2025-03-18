package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.utils.http.AreaResultBean;
import com.example.utils.http.Global;
import com.example.utils.http.KnowledgeResultBean;
import com.example.utils.http.LoginResultBean;
import com.example.utils.http.OrderPresenter;
import com.example.utils.http.Result;
import com.example.utils.http.RetrofitServiceManager;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.multifuction.util.APPUtils;

import java.util.List;

public class SettingPTActivity extends Activity implements View.OnClickListener, OrderPresenter.OrderInterface {
    private static final String TAG = "SettingPTActivity";
    Spinner spr_pt;
    Button btn_confirm;
    TextView tv_get_list;
    OrderPresenter orderPresenter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pt);
        spr_pt = (Spinner) findViewById(R.id.spr_pt);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_get_list = (TextView) findViewById(R.id.tv_get_list);
        btn_confirm.setOnClickListener(this);
        tv_get_list.setOnClickListener(this);

        RetrofitServiceManager.getInstance();
        orderPresenter = new OrderPresenter();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        getAreaList();

    }

    void getAreaList() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (Network.isNetwork(LoginActivity.this)) {
        progressDialog.show();
//        orderPresenter.GetAreaList(SettingPTActivity.this);
//                } else {
//                    progressDialog.setCancelable(false);
//                    progressDialog.setMessage("请先连接网络");
//                    progressDialog.show();
//                }
//            }
//        }, 1000 * 3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                confirm();
                break;
            case R.id.tv_get_list:
                getAreaList();
                break;
        }
    }

    private void confirm() {
        String pt = "";
        if (result != null && !result.getData().isEmpty()) {
            pt = result.getData().get(spr_pt.getSelectedItemPosition()).Id;
        }
        if (pt == null || pt.isEmpty()) {
            APPUtils.showToast(this, "请选择归属地");
            return;
        }
        Global.admin_pt = pt;
        SharedPreferencesUtil.getDefaultSharedPreferences(this).edit().putString(Global.SP_ADMIN_PT, Global.admin_pt).commit();
        APPUtils.showToast(this, "保存归属地成功");
    }

    Result<List<AreaResultBean>> result;

    @Override
    public void getAreaListSuccess(Result<List<AreaResultBean>> result) {
        this.result = result;
        progressDialog.dismiss();
        if (result.getData() != null) {
            String[] items = new String[result.getData().size()];
            for (int i = 0; i < result.getData().size(); i++) {
                items[i] = result.getData().get(i).Name;
            }

            spr_pt.setAdapter(new ArrayAdapter<String>(this, R.layout.sp_style, R.id.tv_sp_size, items));
        }
        Toast.makeText(this, "获取成功", Toast.LENGTH_SHORT).show();

        if (Global.admin_pt != null && !Global.admin_pt.isEmpty()) {
            for (int i = 0; i < result.getData().size(); i++) {
                Log.d(TAG, "getAreaListSuccess result i =" + result.getData().get(i).Id);
                if (Global.admin_pt.equals(result.getData().get(i).Id)) {
                    spr_pt.setSelection(i);
                }
            }
        }
    }

    @Override
    public void getAreaListFailed(String msg) {
        Log.d(TAG, "getAreaListFailed msg=" + msg);
//        progressDialog.setMessage("请先连接网络");
        progressDialog.dismiss();
        Toast.makeText(this, "获取失败" + msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void logInSuccess(LoginResultBean msg) {

    }

    @Override
    public void logInFailed(String msg) {

    }

    @Override
    public void getKnowledgeSuccess(Result<List<KnowledgeResultBean>> result) {

    }

    @Override
    public void getKnowledgeFailed(String msg) {

    }
}
