package com.open.soft.openappsoft.activity.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.multifuction.activity.PesticideTestActivity2;

public class TestTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int source_pesticide = 0;
    public final static int source_jinbiao = 1;
    public final static int source_atp = 2;
    Button btn_task_settings;
    Button btn_task_start;

    int source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.WindowUtils.hideActionBar(this);

        setContentView(R.layout.activity_test_task);
        btn_task_settings = findViewById(R.id.btn_task_settings);
        btn_task_start = findViewById(R.id.btn_task_start);
        btn_task_settings.setOnClickListener(this);
        btn_task_start.setOnClickListener(this);

        source = getIntent().getIntExtra("source", source_pesticide);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_task_settings:
                startActivity(new Intent(this, TaskListActivity.class));
                break;
            case R.id.btn_task_start:
                if (source == source_pesticide) {
                    startActivity(new Intent(this, PesticideTestActivity2.class));
                }
                break;
        }
    }
}
