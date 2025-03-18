package com.open.soft.openappsoft.activity.task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.WindowUtils.hideActionBar(this);
        setContentView(R.layout.activity_add_task);
    }
}