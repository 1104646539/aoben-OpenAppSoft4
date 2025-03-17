package com.open.soft.openappsoft.jinbiao.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.open.soft.openappsoft.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ando.widget.pickerview.adapter.ArrayWheelAdapter;
import ando.widget.pickerview.builder.OptionsPickerBuilder;
import ando.widget.pickerview.listener.OnOptionsSelectListener;
import ando.widget.pickerview.view.OptionsPickerView;
import ando.widget.wheelview.WheelView;

/**
 * Created by cxf on 2017/8/8.
 */
public class DialogUitl {

    //时间选择器
    public static void timeDialog(Context context, StringInterface stringInterface) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -5);
        Calendar endDate = Calendar.getInstance();
//        TimePickerView pvTime = new TimePickerBuilder(context, (OnTimeSelectListener) (date, v) -> {
//            stringInterface.getDataTime(getTime(date));
//        })
//                .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
//                .setSubmitText("确定")
//                .setCancelText("取消")
//                .setTextXOffset(0, 0, 0, 0, 0, 0)
//                .setCancelColor(context.getResources().getColor(R.color.sky_blue))
//                .setBgColor(context.getResources().getColor(R.color.white))
//                .setDate(selectedDate)
//                .setSubmitColor(ContextCompat.getColor(context, R.color.sky_blue))
//                .setRangDate(startDate, endDate)
//                .setType(new boolean[]{true, true, true, false, false, false})
//                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
//                .addOnCancelClickListener((View.OnClickListener) view -> Log.i("pvTime", "onCancelClickListener"))
//                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
//                .setLineSpacingMultiplier(2.0f)
//                .isAlphaGradient(false)
//                .build();
//
//        Dialog mDialog = pvTime.getDialog();
//        if (mDialog != null) {
//
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    Gravity.BOTTOM);
//
//            params.leftMargin = 0;
//            params.rightMargin = 0;
//            pvTime.getDialogContainerLayout().setLayoutParams(params);
//
//            Window dialogWindow = mDialog.getWindow();
//            if (dialogWindow != null) {
//                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
//                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
//                dialogWindow.setDimAmount(0.3f);
//            }
//            pvTime.show();
//        }
    }


    private static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //自定义数据选择器
    public static void listDialog(Context context, List list, IntInterface intInterface) {
        OptionsPickerView build = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                intInterface.getIntPosition(options1);
            }
        }).setContentTextSize(20).setSubmitText("确定").setCancelText("取消").build();
        build.setPicker(list);

        Dialog mDialog = build.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            build.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
        build.show();
    }

    //自定义数据选择器
    @SuppressLint("ResourceType")
    public static void TsListDialog(View view, List<String> list, IntInterface intInterface) {

        Dialog bottomDialog = new Dialog(view.getContext(), R.style.BottomDialog);
        View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_bottom_list, null);
        bottomDialog.setContentView(contentView);

        WheelView wheelView = bottomDialog.findViewById(R.id.wheel_view);

        wheelView.setItemsVisibleCount(3);
        wheelView.setAlphaGradient(true);
        wheelView.setTextSize(20f);
        wheelView.setCyclic(false);
        wheelView.setDividerColor(Color.parseColor("#444444"));
        wheelView.setDividerType(WheelView.DividerType.FILL);
        wheelView.setLineSpacingMultiplier(1.8f);
        wheelView.setTextColorOut(Color.parseColor("#CCCCCC"));
        wheelView.setTextColorCenter(Color.parseColor("#000000"));
        wheelView.isCenterLabel(false);
        wheelView.setCurrentItem(0);
        wheelView.setAdapter(new ArrayWheelAdapter(list));

        bottomDialog.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> bottomDialog.dismiss());
        bottomDialog.findViewById(R.id.text_sure).setOnClickListener(view1 -> {
            int index = wheelView.getCurrentItem();
            Log.d("bottomSheetDialog", "====index================" + index);
            bottomDialog.dismiss();
            if (intInterface != null) {
                intInterface.getIntPosition(index);
            }
        });

        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = view.getContext().getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    //自定义数据选择器
    public static void list2Dialog(Context context, List<String> list, List<List<String>> list2, IntTwoInterface intTwoInterface) {
        OptionsPickerView build = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                intTwoInterface.getIntPosition(options1, options2);
            }
        }).setCancelText("取消").build();
        build.setPicker(list, list2);
        Dialog mDialog = build.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            build.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
        build.show();
    }

    public interface StringInterface {
        void getDataTime(String string);
    }

    public interface IntInterface {
        void getIntPosition(int position);
    }

    public interface IntTwoInterface {
        void getIntPosition(int position, int position2);
    }

    /**
     * 日期选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText(+year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


}
