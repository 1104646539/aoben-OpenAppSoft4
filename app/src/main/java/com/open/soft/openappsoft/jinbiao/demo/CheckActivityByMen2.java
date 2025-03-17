package com.open.soft.openappsoft.jinbiao.demo;

import android.os.Bundle;
import android.view.View;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;

public class CheckActivityByMen2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_main2);

    }

    public void ClickBack(View v) {
        CheckActivityByMen2.this.back();
    }
}


