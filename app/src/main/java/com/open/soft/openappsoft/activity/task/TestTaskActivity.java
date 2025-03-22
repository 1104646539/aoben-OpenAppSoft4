package com.open.soft.openappsoft.activity.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.atp.AtpCheckActivity;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivity;
import com.open.soft.openappsoft.multifuction.activity.PesticideTestActivity2;

public class TestTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String Key_type = "source";
    public final static int source_pesticide = 0;
    public final static int source_jinbiao = 1;
    public final static int source_atp = 2;
    Button btn_task_settings;
    Button btn_task_start;

    int source;
    //金标的类型，定性，定量
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.WindowUtils.hideActionBar(this);

        setContentView(R.layout.activity_test_task);
        btn_task_settings = findViewById(R.id.btn_task_settings);
        btn_task_start = findViewById(R.id.btn_task_start);
        btn_task_settings.setOnClickListener(this);
        btn_task_start.setOnClickListener(this);

        source = getIntent().getIntExtra(Key_type, source_pesticide);
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_task_settings:
                Intent intent = new Intent(this, TaskListActivity.class);
                intent.putExtra(Key_type,source);
                startActivity(intent);
                break;
            case R.id.btn_task_start:
                if (source == source_pesticide) {
                    startActivity(new Intent(this, PesticideTestActivity2.class));
                } else if (source == source_jinbiao) {
                    Intent intent1 = new Intent(this, CheckActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent1.putExtra("source", type);// 定量
                    startActivity(intent1);
                }else if (source == source_atp) {
                    Intent intent1 = new Intent(this, AtpCheckActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent1);
                }
                break;
        }
    }
}
