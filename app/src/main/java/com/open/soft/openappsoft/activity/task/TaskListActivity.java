package com.open.soft.openappsoft.activity.task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsls.gt.GT;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.atp.AtpCheckActivity;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.multifuction.activity.PesticideTestActivity2;
import com.open.soft.openappsoft.util.APPUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener {
    List<TaskModel> taskList = new ArrayList<>();

    RecyclerView rv_data;
    Button btn_add_task, btn_remove_task;
    TaskListAdapter adapter;
    ImageView iv_selected;
    Button btn_start;
    GT.Hibernate hibernate;
    int source;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        hibernate = MainActivity.hibernate;
        source = getIntent().getIntExtra(TestTaskActivity.Key_type, TestTaskActivity.source_pesticide);
        type = getIntent().getStringExtra("type");
        initView();
        findData();
    }

    private void initView() {
        GT.WindowUtils.hideActionBar(this);
        rv_data = findViewById(R.id.rv_data);
        btn_add_task = findViewById(R.id.btn_add_task);
        btn_remove_task = findViewById(R.id.btn_remove_task);
        iv_selected = findViewById(R.id.iv_selected);
        btn_start = findViewById(R.id.btn_start);

        btn_start.setOnClickListener(this);
        btn_remove_task.setOnClickListener(this);
        btn_add_task.setOnClickListener(this);
        iv_selected.setOnClickListener(this);
        iv_selected.setVisibility(View.VISIBLE);

        adapter = new TaskListAdapter(taskList, true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(manager);
        rv_data.setAdapter(adapter);
    }

    private void findData() {
        List<TaskModel> temp;
        if (source == TestTaskActivity.source_atp) {
            temp = hibernate.flashback("id").where("jcx != ?", "").queryAll(TaskModel.class);
        } else {
            temp = hibernate.flashback("id").where("jcx = ?","").queryAll(TaskModel.class);
        }
        taskList.clear();
        if (temp != null) {
            taskList.addAll(temp);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_task:
                addTask();
                break;
            case R.id.btn_remove_task:
                removeTask();
                break;
            case R.id.iv_selected:
                selectedAllTask();
                break;
            case R.id.btn_start:
                start();
                finish();
                break;
        }
    }

    private void start() {
        if (source == TestTaskActivity.source_pesticide) {
            startActivity(new Intent(this, PesticideTestActivity2.class));
        } else if (source == TestTaskActivity.source_jinbiao) {
            Intent intent1 = new Intent(this, CheckActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent1.putExtra("source", type);// 定量
            startActivity(intent1);
        } else if (source == TestTaskActivity.source_atp) {
            Intent intent1 = new Intent(this, AtpCheckActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent1);
        }
    }

    boolean isSelectedAll = false;

    private void selectedAllTask() {
        if (taskList != null && !taskList.isEmpty()) {
            isSelectedAll = !isSelectedAll;
            iv_selected.setSelected(isSelectedAll);
            for (int i = 0; i < taskList.size(); i++) {
                TaskModel tm = taskList.get(i);
                tm.selected = isSelectedAll;
                taskList.set(i, tm);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void removeTask() {
        if (taskList != null && !taskList.isEmpty()) {
            List<TaskModel> selectedList = adapter.getSelectedList();
            for (int i = 0; i < selectedList.size(); i++) {
                MainActivity.hibernate.delete(TaskModel.class, selectedList.get(i).id);
            }
            findData();
//            APPUtils.showToast(this, "删除" + (MainActivity.hibernate.isStatus() ? "成功" : "失败"));
        }
    }

    int request_add = 12000;

    private void addTask() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(TestTaskActivity.Key_type, source);
        startActivityForResult(intent, request_add);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_add && resultCode == RESULT_OK) {
            findData();
        }
    }
}
