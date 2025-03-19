package com.open.soft.openappsoft.activity.task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gsls.gt.GT;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener {
    List<TaskModel> taskList = new ArrayList<>();

    RecyclerView rv_data;
    TextView tv_add_task;
    TaskListAdapter adapter;

    GT.Hibernate hibernate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        hibernate = MainActivity.hibernate;
        initView();
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
        List<TaskModel> temp = hibernate.queryAll(TaskModel.class);
        taskList.clear();
        if (temp != null) {
            taskList.addAll(temp);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_task:
                addTask();
                break;
        }
    }

    int request_add = 12000;

    private void addTask() {
        startActivityForResult(new Intent(this, AddTaskActivity.class), request_add);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_add && resultCode == RESULT_OK) {
            findData();
        }
    }
}
