package com.open.soft.openappsoft.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.bean.AddComBean;
import com.open.soft.openappsoft.dialog.DialogFragmentDemo2;
import com.open.soft.openappsoft.dialog.TSDialogFragmentDemo2;
import com.open.soft.openappsoft.jinbiao.base.BaseAty;

import org.greenrobot.eventbus.EventBus;

//新建企业
public class AddCompanySearchActivity extends BaseAty {

    private android.widget.TextView tvAddCommit;
    private android.widget.EditText etAddName;
    private android.widget.EditText etAddUnm;

    @Override
    public void initViews() {
        //去除默认标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        tvAddCommit = findViewById(R.id.tv_add_commit);
        etAddName = findViewById(R.id.et_add_name);
        etAddUnm = findViewById(R.id.et_add_unm);

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void setEvents() {
        tvAddCommit.setOnClickListener(view -> {
            Log.d("zdl", "====etAddName.getText()=================" + etAddName.getText().toString().trim());
            EventBus.getDefault().postSticky(new AddComBean(etAddName.getText().toString().trim(), etAddUnm.getText().toString().trim()));
            finish();
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_company_search;
    }
}
