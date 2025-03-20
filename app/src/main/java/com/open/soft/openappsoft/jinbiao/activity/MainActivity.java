package com.open.soft.openappsoft.jinbiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;
import com.open.soft.openappsoft.jinbiao.util.SerialUtils;

public class MainActivity extends BaseActivity {

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jinbiao);
//        DbHelper.InitDb(getApplicationContext());
//        SerialUtils.InitSerialPort(this);
//        int time = SharedPreferencesUtil.getTime(this, "time");o'n'C'l
//        if (time == 1) {
//            SharedPreferencesUtil.setTime(this, "time", 1);
//        }
    }

    public void ClickLin(View v) {
        Intent intent1 = new Intent(this, CheckActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent1.putExtra("source", "1");// 定量
        startActivity(intent1);
    }

    public void ClickDinXin(View v) {
        Intent intent2 = new Intent(this, CheckActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent2.putExtra("source", "2");// 定性
        startActivity(intent2);
    }
    public void ClickCheck(View v) { //样品检测
        Intent intent = new Intent(this, CheckSelectProjectActivity.class);
        startActivity(intent);
    }

    public void ClickProject(View v) { //项目管理
        Intent intent = new Intent(this, ProjectManagerActivity.class);
        startActivity(intent);
    }

    public void ClickSelect(View v) {  //功能选项

        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }

    public void ClickExit(View v) {   //进出卡
        SerialUtils.CardOutOrIn();
//		ToolUtils.OpenSerialPort(getApplicationContext(), "Card_OutorIn",3);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        if (System.currentTimeMillis() - exitTime > 2000) {
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
        finish();
//        }
    }

}
