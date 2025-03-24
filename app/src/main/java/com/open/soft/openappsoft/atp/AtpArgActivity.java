package com.open.soft.openappsoft.atp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.utils.http.ToolUtil;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.multifuction.util.Global;
import com.open.soft.openappsoft.util.APPUtils;

import org.jsoup.helper.StringUtil;

public class AtpArgActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_k, et_b;
    SharedPreferences sharedPreferences;

    TextView tv_commit, tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atp_arg);
        GT.WindowUtils.hideActionBar(this);
        sharedPreferences = SharedPreferencesUtil.getDefaultSharedPreferences(this);
        initView();


    }

    private void initView() {
        et_k = findViewById(R.id.et_k);
        et_b = findViewById(R.id.et_b);

        tv_commit = findViewById(R.id.tv_commit);
        tv_cancel = findViewById(R.id.tv_cancel);

        tv_commit.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        et_k.setText("" + Global.ATP_K);
        et_b.setText("" + Global.ATP_B);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                commit();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    private void commit() {
        String k = et_k.getText().toString();
        String b = et_b.getText().toString();
        if (APPUtils.isNull(k) || APPUtils.isNull(b)) {
            APPUtils.showToast(this, "请输入K,B");
            return;
        }

        Global.ATP_K = Float.valueOf(k);
        Global.ATP_B = Float.valueOf(b);
        sharedPreferences.edit().putFloat("atp_k", Global.ATP_K).commit();
        sharedPreferences.edit().putFloat("atp_b", Global.ATP_B).commit();
        APPUtils.showToast(this, "保存成功");
        finish();
    }
}