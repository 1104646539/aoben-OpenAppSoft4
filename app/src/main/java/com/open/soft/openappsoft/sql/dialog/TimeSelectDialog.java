package com.open.soft.openappsoft.sql.dialog;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;

/**
 * @author：King
 * @time：2020/9/29-19:41
 * @moduleName：时间选择对话框
 * @businessIntroduction：用于实现，查询中的“开始查询时间”与“结束查询时间”
 * @loadLibrary：GT库
 */

@GT.Annotations.GT_AnnotationDialogFragment(R.layout.dialog_time_select)
public class TimeSelectDialog extends GT.GT_Dialog.BaseDialogFragment {

    @GT.Annotations.GT_View(R.id.tv_title)
    private TextView tv_title;
    @GT.Annotations.GT_View(R.id.dp_date)
    private DatePicker dp_date;
    @GT.Annotations.GT_View(R.id.tp_time)
    private TimePicker tp_time;

    /**
     * 创建 TimeSelectDialog
     *
     * @param bundle
     * @return
     */
    public static TimeSelectDialog newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        TimeSelectDialog fragment = new TimeSelectDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        build(this, view);//绑定 DialogFragment

        //设置时间选择的标题
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title", "时间选择");
            tv_title.setText(title);
        }
        loadData();
    }

//    @SuppressLint("NewApi")
//    @Override
     public void loadData() {
        super.loadData();

        //日期选择
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             dp_date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                 @Override
                 public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                 }
             });
         }

         //时间选择
        tp_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });

    }

    //单击事件
    @GT.Annotations.GT_Click(R.id.btn_closeSelectTime)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_closeSelectTime:
                finish();
                break;
        }
    }

    @Override
    protected boolean onBackPressed() {
        //劫持返回时事件
        return true;
    }

}

