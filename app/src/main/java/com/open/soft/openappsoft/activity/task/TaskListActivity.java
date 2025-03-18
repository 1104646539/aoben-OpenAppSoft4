package com.open.soft.openappsoft.activity.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.utils.http.model.UploadBean;
import com.gsls.gt.GT;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener {
    List<UploadBean> taskList = new ArrayList<>();
    DbUtils db;

    RecyclerView rv_data;
    TextView tv_add_task;
    TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initView();
        db = DbHelper.GetInstance();
        UploadBean ub = new UploadBean();
        ub.setSampleName("123456d");
        try {
            db.save(ub);
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        findData();
    }

    private void initView() {
        GT.WindowUtils.hideActionBar(this);
        rv_data = findViewById(R.id.rv_data);
        tv_add_task = findViewById(R.id.tv_add_task);

        tv_add_task.setOnClickListener(this);

        adapter = new TaskListAdapter(taskList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(manager);
        rv_data.setAdapter(adapter);
    }

    private void findData() {
        try {
            List<UploadBean> temp = DbHelper.GetInstance().findAll(UploadBean.class);
            taskList.clear();
            taskList.addAll(temp);
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_task:
                addTask();
                break;
        }
    }

    private void addTask() {
//        startActivity(new Intent(this,));
    }
}