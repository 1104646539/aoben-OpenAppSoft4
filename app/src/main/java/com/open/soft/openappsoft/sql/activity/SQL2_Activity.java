package com.open.soft.openappsoft.sql.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.activity.ResultActivity;
import com.open.soft.openappsoft.multifuction.activity.ResultQueryActivity;
import com.open.soft.openappsoft.util.InterfaceURL;

@GT.Annotations.GT_AnnotationActivity(R.layout.activity_sql2)
public class SQL2_Activity extends GT.GT_Activity.AnnotationActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        build(this);//绑定 Activity
        GT.WindowUtils.hideActionBar(this);//隐藏导航栏

        GT.WindowUtils.AutoLandscapeAndPortrait(this,0);
    }

    @Override
    public void loadData() {
        super.loadData();

        //隐藏不需要的模块
        if("农药残留检测仪".equals(InterfaceURL.oneModule)){
            startActivity(new Intent(SQL2_Activity.this, ResultQueryActivity.class));
            finish();
        }else if("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)){
            startActivity(new Intent(SQL2_Activity.this, ResultActivity.class));
            finish();
        }


    }

    //注册单击事件
    @GT.Annotations.GT_Click({R.id.btn_fggd, R.id.btn_jtj, R.id.btn_exit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btn_fggd://分光光度
                startActivity(new Intent(SQL2_Activity.this, ResultQueryActivity.class));
                break;
            case R.id.btn_jtj://胶体金
                startActivity(new Intent(SQL2_Activity.this, ResultActivity.class));
                break;
            case R.id.btn_exit://退出
                finish();
                break;
        }

    }

}
