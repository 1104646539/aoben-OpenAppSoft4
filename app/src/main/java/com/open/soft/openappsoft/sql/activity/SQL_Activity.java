package com.open.soft.openappsoft.sql.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.lidroid.xutils.DbUtils;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.LoginActivity;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.jinbiao.activity.ResultActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean1;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean2;
import com.open.soft.openappsoft.jinbiao.model.TestDataBean3;
import com.open.soft.openappsoft.jinbiao.util.SerialUtils;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.open.soft.openappsoft.multifuction.activity.ResultQueryActivity;
import com.open.soft.openappsoft.sql.adapter.SqlAdapter;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;
import com.open.soft.openappsoft.util.APPUtils;
import com.open.soft.openappsoft.util.InterfaceURL;
import com.open.soft.openappsoft.util.UploadThread2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * @author：King
 * @time：2020/9/16-11:12
 * @moduleName：数据管理模块
 * @businessIntroduction：主要是为“分光光度”与“胶体金”两个模块的数据表进行管理操作。
 * @loadLibrary：GT库
 */
@GT.Annotations.GT_AnnotationActivity(R.layout.activity_sql)
public class SQL_Activity extends GT.GT_Activity.AnnotationActivity implements AdapterView.OnItemSelectedListener {

    //初始化组件
    @GT.Annotations.GT_View(R.id.tv_querySum)
    private TextView tv_querySum;//查询总条数
    @GT.Annotations.GT_View(R.id.sp_moduleName)
    private Spinner sp_moduleName;//模块名称
    @GT.Annotations.GT_View(R.id.tv_startTime)
    private TextView tv_startTime;//开始时间
    @GT.Annotations.GT_View(R.id.tv_finishTime)
    private TextView tv_finishTime;//结束时间
    @GT.Annotations.GT_View(R.id.sp_detectionItem)
    private Spinner sp_detectionItem;//检测项目
    @GT.Annotations.GT_View(R.id.sp_detectionUnit)
    private Spinner sp_detectionUnit;//检测单位
    @GT.Annotations.GT_View(R.id.sp_inspector)
    private Spinner sp_inspector;//检测人员
    @GT.Annotations.GT_View(R.id.sp_detectionResult)
    private Spinner sp_detectionResult;//检测结果
    @GT.Annotations.GT_View(R.id.sp_specimenType)
    private Spinner sp_specimenType;//样品类型
    @GT.Annotations.GT_View(R.id.sp_sampleName)
    private Spinner sp_sampleName;//样品名称
    @GT.Annotations.GT_View(R.id.sql_rv)
    private RecyclerView sql_rv;
    @GT.Annotations.GT_View(R.id.cb_activity_sql)
    private CheckBox cb_activity_sql;
    @GT.Annotations.GT_View(R.id.ll_moduleName)
    private LinearLayout ll_moduleName;
    @GT.Annotations.GT_View(R.id.ll_detectionItem)
    private View ll_detectionItem;

    //其他属性初始化
    private SqlAdapter sqlAdapter;
    private ProgressDialog progressDialog;
    private GT.Hibernate hibernate;
    private List<DetectionResultBean> detectionResultBeans;//数据库查询出的数据会存储到此
    private List<DetectionResultBean> temporaryData;//筛选过后的临时存储数据

    /**
     * 初始化界面
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        build(this);//绑定 Activity
        GT.WindowUtils.hideActionBar(this);//隐藏导航栏
        hibernate = MainActivity.hibernate;//赋值数据库对象
        GT.WindowUtils.AutoLandscapeAndPortrait(this, 0);// 强行横屏
        //创建加载条
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载数据，请等待……");
        progressDialog.setCancelable(true);
        sql_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 获取数据库数据
        sqlAdapter = new SqlAdapter(this, tv_querySum, progressDialog);
        sql_rv.setAdapter(sqlAdapter);
        loadData();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        Timber.i("查询出所有的数据");
        updateSQLData();//更新数据库数据
        //初始化 SP 数据
        initSpData();
        // 从本地获取定位信息
        set_dingwei_info();
        // 弹出时间
        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog(0);
            }
        });

        tv_finishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog(1);
            }
        });

        cb_activity_sql.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                for (int i = 0; i < temporaryData.size(); i++) {
                    DetectionResultBean detectionResultBean = temporaryData.get(i);
                    detectionResultBean.setSelect(isChecked);
                    temporaryData.set(i, detectionResultBean);
                }
                sqlAdapter.setDetectionProjectBeanList(temporaryData);
            }
        });
        //查询模块
        sp_moduleName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String moduleName = parent.getSelectedItem().toString();//获取模块名称

                //初始化SP数据
                String[] arrayString;
                ArrayAdapter<String> stringArrayAdapter;

                if ("分光光度".equals(moduleName)) {
                    arrayString = new String[]{"全部", "合格", "不合格", "无效"};
                    stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                    sp_detectionResult.setAdapter(stringArrayAdapter);
                } else if ("胶体金".equals(moduleName)) {
                    arrayString = new String[]{"全部", "阳性", "阴性", "无效"};
                    stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                    sp_detectionResult.setAdapter(stringArrayAdapter);
                } else if ("ATP".equals(moduleName)) {
                    arrayString = new String[]{"全部", "合格", "不合格", "无效"};
                    stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                    sp_detectionResult.setAdapter(stringArrayAdapter);
                } else if ("全部".equals(moduleName)) {
                    arrayString = new String[]{"全部", "合格", "不合格", "阳性", "阴性", "无效"};
                    stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                    sp_detectionResult.setAdapter(stringArrayAdapter);
                }

                progressDialog.show();//显示加载条

                if (detectionResultBeans == null) {
                    detectionResultBeans = new ArrayList<>();
                } else {
                    detectionResultBeans.clear();
                }

                //模块名称
                if ("全部".equals(moduleName)) {
                    Timber.i("loadData 加载数据 单金标或分光光度");
                    updateSQLData();//更新数据库数据
                } else {
                    Timber.i("模块下拉触发 单金标或分光光度");
                    detectionResultBeans = hibernate.flashback("ID").where("SQLType = ?", moduleName).queryAll(DetectionResultBean.class);//模块下拉触发 单金标或分光光度
                }
                sqlAdapter.setList(detectionResultBeans);//适配器设置数据

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //更新数据库数据
    private void updateSQLData() {
        progressDialog.show();
//        if ("多参数食品安全检测仪".equals(InterfaceURL.oneModule)) {
            detectionResultBeans = hibernate.flashback("ID").queryAll(DetectionResultBean.class);//loadData 加载数据 全部
//        } else if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {
//            detectionResultBeans = hibernate.flashback("ID").where("SQLType = ?", "胶体金").queryAll(DetectionResultBean.class);//loadData 加载数据 单金标或分光光度
//        } else if ("农药残留检测仪".equals(InterfaceURL.oneModule)) {
//            detectionResultBeans = hibernate.flashback("ID").where("SQLType = ?", "分光光度").queryAll(DetectionResultBean.class);//loadData 加载数据 单金标或分光光度
//        }
        sqlAdapter.setList(detectionResultBeans);//设置数据
        progressDialog.dismiss();
    }

    /**
     * 初始化 SP 数据
     */
    private void initSpData() {

        //临时变量
        ArrayAdapter<String> stringArrayAdapter;
        String[] arrayString;

        //是否隐藏模块选择
        if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule) || "农药残留检测仪".equals(InterfaceURL.oneModule)) {
            ll_moduleName.setVisibility(View.GONE);
        }

        //是否隐藏检测项目
        if ("农药残留检测仪".equals(InterfaceURL.oneModule)) {
            ll_detectionItem.setVisibility(View.GONE);
        }


        //模块名称
        arrayString = new String[]{"全部", "分光光度", "胶体金","ATP"};
        stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
        sp_moduleName.setAdapter(stringArrayAdapter);

        //检测项目
        try {
            if (detectionResultBeans != null && detectionResultBeans.size() > 0) {
                arrayString = new String[]{"全部"};
                for (int i = 0; i < detectionResultBeans.size(); i++) {
                    if (detectionResultBeans.get(i).getTestItem() != null) {
                        arrayString = insert(arrayString, detectionResultBeans.get(i).getTestItem());
                    }
                }

                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_detectionItem.setAdapter(stringArrayAdapter);
                sp_detectionItem.setOnItemSelectedListener(this);
            } else {
                arrayString = new String[]{"全部"};
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_detectionItem.setAdapter(stringArrayAdapter);
                sp_detectionItem.setOnItemSelectedListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //检测单位
        try {
            if (detectionResultBeans != null && detectionResultBeans.size() > 0) {
                arrayString = new String[]{"全部"};
                for (int i = 0; i < detectionResultBeans.size(); i++) {
                    if (detectionResultBeans.get(i).getDetectionCompany() != null) {
                        arrayString = insert(arrayString, detectionResultBeans.get(i).getDetectionCompany());
                    }
                }
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_detectionUnit.setAdapter(stringArrayAdapter);
                sp_detectionUnit.setOnItemSelectedListener(this);

            } else {
                arrayString = new String[]{"全部"};
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_detectionUnit.setAdapter(stringArrayAdapter);
                sp_detectionUnit.setOnItemSelectedListener(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //检测人员
        try {
            if (detectionResultBeans != null && detectionResultBeans.size() > 0) {

                arrayString = new String[]{"全部"};
                for (int i = 0; i < detectionResultBeans.size(); i++) {
                    if (detectionResultBeans.get(i).getInspector() != null) {
                        arrayString = insert(arrayString, detectionResultBeans.get(i).getInspector());
                    }
                }
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_inspector.setAdapter(stringArrayAdapter);
                sp_inspector.setOnItemSelectedListener(this);
            } else {
                arrayString = new String[]{"全部"};
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_inspector.setAdapter(stringArrayAdapter);
                sp_inspector.setOnItemSelectedListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //检测结果
        if ("多参数食品安全检测仪".equals(InterfaceURL.oneModule)) {
            arrayString = new String[]{"全部", "合格", "不合格", "阳性", "阴性", "无效"};
            stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
            sp_detectionResult.setAdapter(stringArrayAdapter);
            sp_detectionResult.setOnItemSelectedListener(this);
        } else if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {
            arrayString = new String[]{"全部", "阳性", "阴性", "无效"};
            stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
            sp_detectionResult.setAdapter(stringArrayAdapter);
            sp_detectionResult.setOnItemSelectedListener(this);
        } else if ("农药残留检测仪".equals(InterfaceURL.oneModule)) {
            arrayString = new String[]{"全部", "合格", "不合格", "无效"};
            stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
            sp_detectionResult.setAdapter(stringArrayAdapter);
            sp_detectionResult.setOnItemSelectedListener(this);
        }

        //样品类型
        try {
            if (detectionResultBeans != null && detectionResultBeans.size() > 0) {

                arrayString = new String[]{"全部"};
                for (int i = 0; i < detectionResultBeans.size(); i++) {
                    if (detectionResultBeans.get(i).getSpecimenType() != null) {
                        arrayString = insert(arrayString, detectionResultBeans.get(i).getSpecimenType());
                    }
                }
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_specimenType.setAdapter(stringArrayAdapter);
                sp_specimenType.setOnItemSelectedListener(this);
            } else {
                arrayString = new String[]{"全部"};
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_specimenType.setAdapter(stringArrayAdapter);
                sp_specimenType.setOnItemSelectedListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //样品名称 sp_sampleName
        try {
            if (detectionResultBeans != null && detectionResultBeans.size() > 0) {
                arrayString = new String[]{"全部"};
                for (int i = 0; i < detectionResultBeans.size(); i++) {
                    if (detectionResultBeans.get(i).getSampleName() != null) {
                        arrayString = insert(arrayString, detectionResultBeans.get(i).getSampleName());
                    }
                }

                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_sampleName.setAdapter(stringArrayAdapter);
                sp_sampleName.setOnItemSelectedListener(this);
            } else {
                arrayString = new String[]{"全部"};
                stringArrayAdapter = new ArrayAdapter<>(SQL_Activity.this, R.layout.sp_style, R.id.tv_sp_size, arrayString);
                sp_sampleName.setAdapter(stringArrayAdapter);
                sp_sampleName.setOnItemSelectedListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GT.Annotations.GT_Click({R.id.btn_clear, R.id.btn_delete, R.id.btn_print, R.id.btn_uploading, R.id.btn_export, R.id.btn_return, R.id.btn_fggb, R.id.btn_jtj, R.id.cb_activity_sql, R.id.tv_select_all})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                //清空
                ClickClear(view);
                break;
            case R.id.btn_delete:
                //删除
                ClickDelete(view);
                break;
            case R.id.btn_print:
                //打印
                PrintInfo(view);
                break;
            case R.id.btn_uploading:
                //上传
                if (!press_status) {
                    uploadData();
                }
                break;
            case R.id.btn_export:
                //导出
                showWaitDialog();
                exportData2();
                while (true) {
                    if (export_status != 0) {
                        break;
                    }
                    GT.Thread.sleep(500);
                }
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            case R.id.btn_return:
                finish();
                //返回
                break;
            case R.id.btn_fggb:
                //分光
                startActivity(new Intent(SQL_Activity.this, ResultQueryActivity.class));
                break;
            case R.id.btn_jtj:
                //胶体金
                startActivity(new Intent(SQL_Activity.this, ResultActivity.class));
                break;
        }
    }

    //从已查询出的SQL数据中筛选数据
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //将数据库真实数据赋值给与筛选集合
        if (temporaryData == null) {
            temporaryData = new ArrayList<>();
        } else {
            temporaryData.clear();
        }
        if (detectionResultBeans == null) {
            updateSQLData();//更新数据库数据
        }
        //添加所有数据
        temporaryData.addAll(detectionResultBeans);
        //获取筛选条件
        String detectionResult = sp_detectionResult.getSelectedItem().toString();//检测结果
        String detectionUnit = sp_detectionUnit.getSelectedItem().toString();//检测单位
        String inspector = sp_inspector.getSelectedItem().toString();//检测人员
        String detectionItem = sp_detectionItem.getSelectedItem().toString();//检测项目
        String specimenType = sp_specimenType.getSelectedItem().toString();//样品类型
        String sampleName = sp_sampleName.getSelectedItem().toString();//样品名称
        //检测结果
        if (!"全部".equals(detectionResult)) {
            for (int i = 0; i < temporaryData.size(); i++) {
                String detectionResult1 = temporaryData.get(i).getDetectionResult();
                if (!detectionResult.equals(detectionResult1)) {
                    temporaryData.remove(i--);
                }
            }
        }
        //检测单位
        if (!"全部".equals(detectionUnit)) {
            for (int i = 0; i < temporaryData.size(); i++) {
                if (!detectionUnit.equals(temporaryData.get(i).getDetectionCompany())) {
                    temporaryData.remove(i--);
                }
            }
        }
        //检测人员
        if (!"全部".equals(inspector)) {
            for (int i = 0; i < temporaryData.size(); i++) {
                if (!inspector.equals(temporaryData.get(i).getInspector())) {
                    temporaryData.remove(i--);
                }
            }
        }
        //检测项目
        if (!"全部".equals(detectionItem)) {
            for (int i = 0; i < temporaryData.size(); i++) {
                if (!detectionItem.equals(temporaryData.get(i).getTestItem())) {
                    temporaryData.remove(i--);
                }
            }
        }
        //样品类型
        if (!"全部".equals(specimenType)) {
            for (int i = 0; i < temporaryData.size(); i++) {
                if (!specimenType.equals(temporaryData.get(i).getSpecimenType())) {
                    temporaryData.remove(i--);
                }
            }
        }
        //样品名称
        if (!"全部".equals(sampleName)) {
            for (int i = 0; i < temporaryData.size(); i++) {
                if (!sampleName.equals(temporaryData.get(i).getSampleName())) {
                    temporaryData.remove(i--);
                }
            }
        }
        sqlAdapter.setList(temporaryData);//设置数据
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // 删除数据
    public void ClickDelete(View view) {
        // 是否存在选中item标志位
        boolean have_checked = false;
        // 存储选中item的列表
        int[] ischecked_list = new int[]{};

        for (int i = 0; i < sql_rv.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) sql_rv.getChildAt(i);
            while (true) {
                View childAt = viewGroup.getChildAt(0);
                if (childAt instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) childAt;
                    if (checkBox.isChecked()) {
                        ischecked_list = insertInt(ischecked_list, i);
                        have_checked = true;
                    }
                    break;
                }
                viewGroup = (ViewGroup) childAt;
            }
        }

        if (!have_checked) {
            APPUtils.showToast(this, "请先选择要删除的数据");
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    SQL_Activity.this);
            builder.setTitle("提示");
            builder.setMessage("确定删除数据?");
            int[] finalIschecked_list = ischecked_list;
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    for (int i = 0; i < finalIschecked_list.length; i++) {
                        DetectionResultBean detectionResultBean = sqlAdapter.getList().get(finalIschecked_list[i]);
                        hibernate.delete(DetectionResultBean.class, detectionResultBean.getID());
                        com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(SQL_Activity.this, "删除成功");
                        arg0.dismiss();
                        // 刷新页面的显示
                        loadData();
                    }
                }
            });

            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            builder.create().show();
        }

    }

    // 查询时间
    public void showCalendarDialog(final int index) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthYear = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (arg0, arg1, arg2, arg3) -> {
            if (index == 0) {
                /*String time = arg1 + "-" + String.format("%02d", arg2 + 1)
                        + "-" + String.format("%02d", arg3) + " 00:00";*/
                String time = arg1 + "-" + String.format("%02d", arg2 + 1)
                        + "-" + String.format("%02d", arg3);
                long t = 0;
                try {
//                        t = stringToLong(time, "yyyy-MM-dd HH:mm");
                    t = stringToLong(time, "yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_startTime.setText(time);
            } else {
                /*String time = arg1 + "-" + String.format("%02d", arg2 + 1)
                        + "-" + String.format("%02d", arg3) + " 23:59";*/
                String time = arg1 + "-" + String.format("%02d", arg2 + 1)
                        + "-" + String.format("%02d", arg3);
                long t = 0;
                try {
//                        t = stringToLong(time, "yyyy-MM-dd HH:mm");
                    t = stringToLong(time, "yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_finishTime.setText(time);
            }

        }, year, monthYear, day).show();
    }


    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }


    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    // 清空
    public void ClickClear(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定删除全部数据?");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // 删除DetectionResultBean表中的所有数据
                hibernate.deleteAll(DetectionResultBean.class);

                com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(SQL_Activity.this, "删除成功");
                arg0.dismiss();
                // 刷新页面的显示
                loadData();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        builder.create().show();
    }

    // 打印
    public void PrintInfo(View v) {

        String printData = null;

        // 是否存在选中标志为
        boolean have_chenked = false;

        List<DetectionResultBean> list = sqlAdapter.getSelectedList();
        if (list.size() == 0) {
            APPUtils.showToast(this, "请先选择要打印的数据");
        }
        for (int i = 0; i < list.size(); i++) {
            DetectionResultBean detectionResultBean = list.get(i);
            Timber.i("打印detectionResultBean:" + detectionResultBean);

            if ("胶体金".equals(detectionResultBean.getSQLType())) {
                // 朝外打印
                printData = GetPrintInfo1(detectionResultBean, this);

                // 朝内打印
//                            printData = GetPrintInfo3(detectionResultBean, this);
            } else if ("分光光度".equals(detectionResultBean.getSQLType())) {
                // 朝外打印
                printData = GetPrintInfo2(detectionResultBean, this);

                // 朝内打印
//                            printData = GetPrintInfo4(detectionResultBean, this);
            }

            APPUtils.showToast(this, printData);
            byte[] data = printData.getBytes(Charset.forName("gb2312"));


            if (!SerialUtils.COM4_SendData(data)) {
                APPUtils.showToast(this, "打印数据发送失败");
            } else {
                APPUtils.showToast(this, "打印数据发送成功");
            }
        }
        //getChildCount()返回数量
        /*for (int i = 0; i < sql_rv.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) sql_rv.getChildAt(i);
            while (true) {
                View childAt = viewGroup.getChildAt(0);
                if (childAt instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) childAt;
                    if (checkBox.isChecked()) {
                        have_chenked = true;
                        DetectionResultBean detectionResultBean = sqlAdapter.getList().get(i);
                        logs("打印detectionResultBean:" + detectionResultBean);

                        if ("胶体金".equals(detectionResultBean.getSQLType())) {
                            // 朝外打印
                            printData = GetPrintInfo1(detectionResultBean, this);

                            // 朝内打印
//                            printData = GetPrintInfo3(detectionResultBean, this);
                        } else if ("分光光度".equals(detectionResultBean.getSQLType())) {
                            // 朝外打印
                            printData = GetPrintInfo2(detectionResultBean, this);

                            // 朝内打印
//                            printData = GetPrintInfo4(detectionResultBean, this);
                        }

                        APPUtils.showToast(this, printData);
                        byte[] data = printData.getBytes(Charset.forName("gb2312"));


                        if (!SerialUtils.COM4_SendData(data)) {
                            APPUtils.showToast(this, "打印数据发送失败");
                        } else {
                            APPUtils.showToast(this, "打印数据发送成功");
                        }
                    }
                    break;
                }

                viewGroup = (ViewGroup) childAt;
            }
        }

        if (!have_chenked) {
            APPUtils.showToast(this, "请先选择要打印的数据");
        }*/

    }

    // 上传情况标志位
    private int upload_status;
    // 上传错误信息
    private String errmessage;
    private ProgressDialog progressDialog1;
    private DbUtils dbtest;
    private boolean press_status = false;

    private List<DetectionResultBean> list_upload;

    // 上传按钮
    public void uploadData() {
        press_status = true;
        list_upload = new ArrayList<DetectionResultBean>();
        final boolean[] have_chenked = {false};
        dbtest = DbHelper.GetInstance();
        final int[] times = {0};

        String[] index_list = new String[sql_rv.getChildCount()];
        boolean[] upload_list = new boolean[sql_rv.getChildCount()];
        String[] errmessage_list = new String[sql_rv.getChildCount()];
        int count1 = 0;
        for (int i = 0; i < sql_rv.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) sql_rv.getChildAt(i);
            while (true) {
                View childAt = viewGroup.getChildAt(0);
//                View liushui = viewGroup.getChildAt(2);
                if (childAt instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) childAt;
                    if (checkBox.isChecked()) {

                        String number = "";
                        ViewParent parent = checkBox.getParent().getParent();
                        if (parent instanceof LinearLayout) {
                            LinearLayout linearLayout = (LinearLayout) parent;
                            View childAt1 = linearLayout.getChildAt(2);
                            if (childAt1 instanceof TextView) {
                                TextView tv_number = (TextView) childAt1;
                                number = tv_number.getText().toString();
                            }
                        }

                        index_list[count1] = number;
                        have_chenked[0] = true;

                        times[0]++;

                        int index = Integer.parseInt(number) - 1;

                        DetectionResultBean detectionResultBean = temporaryData.get(index);
                        list_upload.add(count1, detectionResultBean);
                        count1++;
                    }
                    break;
                }
                viewGroup = (ViewGroup) childAt;
            }
        }
        if (list_upload.size() > 0) {
            for (int i = 0; i < list_upload.size(); i++) {
                DetectionResultBean detectionResultBean = list_upload.get(i);

                String uploadStatus = detectionResultBean.getUploadStatus();
                if ("已上传".equals(uploadStatus)) {
                    APPUtils.showToast(SQL_Activity.this, "已上传过不能重复上传");
                    press_status = false;
                    return;
                }
            }
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在上传");
        progressDialog.show();
        GT.Thread.runJava(() -> {

            UploadThread2 uploadThread2 = new UploadThread2(this, list_upload, new UploadThread2.onUploadListener() {
                @Override
                public void onUploadSuccess(int position, String msg) {
                    list_upload.get(position).setUploadID(msg);
                    list_upload.get(position).setUploadStatus("已上传");
                    list_upload.get(position).setSelect(false);
                    hibernate.update(list_upload.get(position));
                    sqlAdapter.notifyDataSetChanged();
                }

                @Override
                public void onUploadFail(int position, String failInfo) {

                }

                @Override
                public void onUploadFinish(int count, int successCount, int failedCount) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        MessageDialog.show("提示", "上传完成,共上传" + count + "条" + "，成功" + successCount + "条" + "，失败" + failedCount + "条", "确定");
                    });
                    press_status = false;
                }
            });
            uploadThread2.start();


//            String success = "";
//            String fail = "";
//            for (int i = 0; i < list_upload.size(); i++) {
//                if (upload_list[i]) {
////                        success += index_list[i] + ",";
//
//                    success += "检测流水号为" + index_list[i] + "上传成功" + "\n";
//                } else {
////                        fail += index_list[i] + ",";
//                    fail += "检测流水号为" + index_list[i] + "上传失败，失败原因：" + errmessage_list[i] + "\n";
//                }
//            }
//
//
//            String finalSuccess = success;
//            String finalFail = fail;
//
//            if (SQL_Activity.this != null) {
//                try {
//                    SQL_Activity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new AlertDialog.Builder(SQL_Activity.this).setTitle("提示")
//                                    .setMessage(finalSuccess + finalFail)
//                                    .setCancelable(false)
//                                    .setNeutralButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            press_status = false;
//                                        }
//                                    }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            press_status = false;
//                                        }
//                                    }).create().show();
//                        }
//                    });
//                } catch (Exception e) {
//                    Timber.e("窗口异常:" + e);
//                }
//
//            }


        });
        if (list_upload.size() == 0) {
            press_status = false;
            APPUtils.showToast(this, "请先选择要上传的数据");
        }
    }


    // 胶体金自动录入检测试记录的上传
    public void uploadData1(DetectionResultBean drb) {
        String user = drb.getOperatorId() + "";
        String terrace = drb.getAreaId() + "";
        String deptId = drb.getDeptId() + "";
        //将具体检测文字 转为 编码
        String result = "";
        if ("阴性".equals(drb.getDetectionResult())) {
            result = "0";
        } else if ("阳性".equals(drb.getDetectionResult())) {
            result = "1";
        } else {
            result = "-1";
        }
        String str_api = "";
        // 真实数据data
        String data = "";
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "QR/SendResultList";

            data = "[{\"QRCode\":\"" + drb.getQRCode().replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + drb.getDetectionResult() + "\",\n" +
                    "\"SamplingNumber\":\"" + drb.getNumberSamples() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "    [\n" +
                    "        {\n" +
                    "        \"Title\":\"检测项目\",\n" +
                    "        \"Id\":\"projectName\",\n" +
                    "        \"Value\":\"" + drb.getTestItem() + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"Title\":\"限量值\",\n" +
                    "            \"Id\":\"xlz\",\n" +
                    "            \"Value\":\"" + drb.getLimitStandard() + "\"\n" +
                    "            }\n" +
                    "            ,{\n" +
                    "             \"Title\":\"样品来源\",\n" +
                    "             \"Id\":\"sampleSource\",\n" +
                    "             \"Value\":\"" + drb.getCommodityPlaceOrigin() + "\"\n" +
                    "             },\n" +
                    "            ,{\n" +
                    "             \"Title\":\"设备编号\",\n" +
                    "             \"Id\":\"deviceSn\",\n" +
                    "             \"Value\":\"" + MainActivity.mac_url + "\"\n" +
                    "             },\n" +
                    "{\n" +
                    "    \"Title\":\"被检测单位\",\n" +
                    "    \"Id\":\"samplingDept\",\n" +
                    "    \"Value\":\"" + drb.getUnitsUnderInspection() + "\"\n" +
                    "    }]}\n" +
                    "\n" +
                    "]";


        } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "QR/SendResult";
            data = "{\"QRCode\":\"" + drb.getQRCode().replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + drb.getDetectionResult() + "\",\n" +
                    "\"SamplingNumber\":\"" + drb.getNumberSamples() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "    [\n" +
                    "        {\n" +
                    "        \"Title\":\"检测项目\",\n" +
                    "        \"Id\":\"projectName\",\n" +
                    "        \"Value\":\"" + drb.getTestItem() + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "         \"Title\":\"限量值\",\n" +
                    "         \"Id\":\"xlz\",\n" +
                    "         \"Value\":\"" + drb.getLimitStandard() + "\"\n" +
                    "         }\n" +
                    "         ,{\n" +
                    "          \"Title\":\"样品来源\",\n" +
                    "          \"Id\":\"sampleSource\",\n" +
                    "          \"Value\":\"" + drb.getCommodityPlaceOrigin() + "\"\n" +
                    "          },\n" +
                    "         ,{\n" +
                    "          \"Title\":\"设备编号\",\n" +
                    "          \"Id\":\"deviceSn\",\n" +
                    "          \"Value\":\"" + MainActivity.mac_url + "\"\n" +
                    "          },\n" +
                    "{\n" +
                    "    \"Title\":\"被检测单位\",\n" +
                    "    \"Id\":\"samplingDept\",\n" +
                    "    \"Value\":\"" + drb.getUnitsUnderInspection() + "\"\n" +
                    "    }]" +
                    "}";

        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                upload_status = 3;
                GT.Thread.runJava(() -> {

                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean1 testDataBean1 = null;
                TestDataBean3 testDataBean3 = null;
                if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
                    testDataBean1 = new Gson().fromJson(ret, TestDataBean1.class);
                    if (testDataBean1 != null) {
                        List<TestDataBean2> data1 = testDataBean1.getData();
                        if (data1 != null) {
                            TestDataBean2 testDataBean2 = data1.get(0);
                            if (testDataBean2 != null) {
                                SQL_Activity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        Toast.makeText(SQL_Activity.this, testDataBean2.getErrMsg(), Toast.LENGTH_LONG).show();
                                        errmessage = testDataBean2.getErrMsg();
                                        if ("0".equals(testDataBean2.getErrCode())) {
                                            upload_status = 1;
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("uploadStatus", "已上传");
                                            hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
                                            loadData();
                                        } else {
                                            upload_status = 2;
                                        }
                                    }
                                });
                            }
                        }

                    }
                } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
                    testDataBean3 = new Gson().fromJson(ret, TestDataBean3.class);

                    if (testDataBean3 != null) {
                        TestDataBean2 data1 = testDataBean3.getData();
                        if (data1 != null) {
                            SQL_Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SQL_Activity.this, data1.getErrMsg(), Toast.LENGTH_LONG).show();
                                    errmessage = data1.getErrMsg();
                                    if ("0".equals(data1.getErrCode())) {
                                        upload_status = 1;
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("uploadStatus", "已上传");
                                        hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
                                        loadData();
                                    } else {
                                        upload_status = 2;
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//
//                TestDataBean1 testDataBean1 = null;
//                TestDataBean3 testDataBean3 = null;
//                if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
//                    testDataBean1 = new Gson().fromJson(response, TestDataBean1.class);
//                    if (testDataBean1 != null) {
//                        List<TestDataBean2> data1 = testDataBean1.getData();
//                        if (data1 != null) {
//                            TestDataBean2 testDataBean2 = data1.get(0);
//                            if (testDataBean2 != null) {
//                                SQL_Activity.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
////                                        Toast.makeText(SQL_Activity.this, testDataBean2.getErrMsg(), Toast.LENGTH_LONG).show();
//                                        errmessage = testDataBean2.getErrMsg();
//                                        if ("0".equals(testDataBean2.getErrCode())) {
//                                            upload_status = 1;
//                                            ContentValues contentValues = new ContentValues();
//                                            contentValues.put("uploadStatus", "已上传");
//                                            hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
//                                            loadData();
//                                        } else {
//                                            upload_status = 2;
//                                        }
//                                    }
//                                });
//                            }
//                        }
//
//                    }
//                } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
//                    testDataBean3 = new Gson().fromJson(response, TestDataBean3.class);
//
//                    if (testDataBean3 != null) {
//                        TestDataBean2 data1 = testDataBean3.getData();
//                        if (data1 != null) {
//                            SQL_Activity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(SQL_Activity.this, data1.getErrMsg(), Toast.LENGTH_LONG).show();
//                                    errmessage = data1.getErrMsg();
//                                    if ("0".equals(data1.getErrCode())) {
//                                        upload_status = 1;
//                                        ContentValues contentValues = new ContentValues();
//                                        contentValues.put("uploadStatus", "已上传");
//                                        hibernate.where( "id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
//                                        loadData();
//                                    } else {
//                                        upload_status = 2;
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onError(String response) {
//                upload_status = 3;
//                GT.Thread.runJava(() -> {
//
//                });
//            }
//        });
    }

    private String AreaId;

    // 胶体金手动录入检测记录的上传
    public void uploadData2(DetectionResultBean drb) {
        String AreaId = com.example.utils.http.Global.admin_pt;
        //获取必要上传
        SharedPreferences sp = getSharedPreferences("userPass", MODE_PRIVATE);
        /*String user = sp.getString("user", "");
        String terrace = sp.getString("terrace", "");
        String deptId = sp.getString("DeptId", "");*/

        String user = drb.getOperatorId() + "";
        String terrace = drb.getAreaId() + "";
        String deptId = drb.getDeptId() + "";

        //将具体检测文字 转为 编码
        String result = "";
        if ("阴性".equals(drb.getDetectionResult())) {
            result = "0";
        } else if ("阳性".equals(drb.getDetectionResult())) {
            result = "1";
        } else {
            result = "-1";
        }
        String data = "";
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            data = "[{\"QRCode\":\"" + drb.getQRCode().replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + drb.getDetectionResult() + "\",\n" +
                    "\"SamplingNumber\":\"" + drb.getNumberSamples() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "[\n" +
                    "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                    "{\"Title\":\"省Id\",\"Id\":\"province\",\"Value\":\"" + drb.getSpId1() + "\"},\n" +
                    "{\"Title\":\"城市Id\",\"Id\":\"city\",\"Value\":\"" + drb.getSpId2() + "\"},\n" +
                    "{\"Title\":\"区县\",\"Id\":\"district\",\"Value\":\"" + drb.getSpId3() + "\"},\n" +
                    "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + drb.getYplbId() + "\"},\n" +
                    "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + drb.getSampleName() + "\"},\n" +
                    "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + drb.getUnitsUnderInspection() + "\"},\n" +
                    "{\"Title\":\"产品Id\",\"Id\":\"productId\",\"Value\":\"" + drb.getYplbId() + "\"},\n" +
                    "{\"Title\":\"对象Id\",\"Id\":\"objectId\",\"Value\":\"" + drb.objectId + "\"},\n" +
                    "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                    "{\"Title\":\"产地详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + drb.getCommodityPlaceOrigin() + "\"}\n" +
                    "]}\n" +
                    "]";

        } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            data = "[{\"QRCode\":\"" + drb.getQRCode().replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + drb.getDetectionResult() + "\",\n" +
                    "\"SamplingNumber\":\"" + drb.getNumberSamples() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "[\n" +
                    "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                    "{\"Title\":\"样品来源地1级\",\"Id\":\"source1\",\"Value\":\"" + drb.getSpId1() + "\"},\n" +
                    "{\"Title\":\"样品来源地2级\",\"Id\":\"source2\",\"Value\":\"" + drb.getSpId2() + "\"},\n" +
                    "{\"Title\":\"样品来源地3级\",\"Id\":\"source3\",\"Value\":\"" + drb.getSpId3() + "\"},\n" +
                    "{\"Title\":\"样品总分类Id\",\"Id\":\"sampleClassId\",\"Value\":\"" + drb.getSampleId() + "\"},\n" +
                    "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + drb.getSampleId() + "\"},\n" +
                    "{\"Title\":\"产品Id\",\"Id\":\"productId\",\"Value\":\"" + drb.getYplbId() + "\"},\n" +
                    "{\"Title\":\"对象Id\",\"Id\":\"objectId\",\"Value\":\"" + drb.objectId + "\"},\n" +
                    "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + drb.getSampleName() + "\"},\n" +
                    "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + drb.getUnitsUnderInspection() + "\"},\n" +
                    "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                    "{\"Title\":\"样品详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + drb.getCommodityPlaceOrigin() + "\"}\n" +
                    "]}\n" +
                    "]";
        }


        String str_api = "";

        // 只有单金标且登录用户是唐山的用户才使用 "Tangshan/SendForNAD4074" api
        // 其他的都是 "CommonManual/GetSamplingTypeList" api
        if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) && "TangshanNM".equals(AreaId)) {
            str_api = "Tangshan/SendForNAD4074";

        } else if ("农药残留单项精准分析仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule) && !"TangshanNM".equals(AreaId)) {
            str_api = "Tangshan/SendForNAD4074";

        } else if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "CommonManual/UpdateManualSampling";
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                upload_status = 3;
                GT.Thread.runJava(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean1 uploadData1 = new Gson().fromJson(ret, TestDataBean1.class);
                if (uploadData1 != null) {
                    String errMsg = uploadData1.getData().get(0).getErrMsg();
                    String errCode = uploadData1.getData().get(0).getErrCode();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errmessage = errMsg;
                            if ("0".equals(errCode)) {
                                upload_status = 1;
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("uploadStatus", "已上传");
                                hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
                                loadData();
                            } else {
                                upload_status = 2;
                            }

                        }
                    });
                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                TestDataBean1 uploadData1 = new Gson().fromJson(response, TestDataBean1.class);
//                if (uploadData1 != null) {
//                    String errMsg = uploadData1.getData().get(0).getErrMsg();
//                    String errCode = uploadData1.getData().get(0).getErrCode();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            errmessage = errMsg;
//                            if ("0".equals(errCode)) {
//                                upload_status = 1;
//                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("uploadStatus", "已上传");
//                                hibernate.update("DetectionResultBean", contentValues, "id = ?", new String[]{drb.getID() + ""});
//                                loadData();
//                            } else {
//                                upload_status = 2;
//                            }
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onError(String response) {
//                upload_status = 3;
//                GT.Thread.runJava(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        });


    }

    // 分光光度自动录入检测记录的上传
    public void uploadData3(DetectionResultBean drb) {
        //获取必要上传
        String user = drb.getOperatorId() + "";
        String terrace = drb.getAreaId() + "";
        String deptId = drb.getDeptId() + "";
        String str_api = "";
        // 只有单金标且登录用户是唐山的用户才使用 "Tangshan/SendForNAD4074" api
        // 其他的都是 "CommonManual/GetSamplingTypeList" api
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "QR/SendResult";
        }
        String result = "";
        if ("合格".equals(drb.getDetectionResult())) {
            result = "0";
        } else if ("不合格".equals(drb.getDetectionResult())) {
            result = "1";
        } else {
            result = "-1";
        }
        String data = "";
        // 新的上传
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            data = "{\"QRCode\":\"" + drb.getQRCode().replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + drb.getDetectionResult() + "\",\n" +
                    "\"SamplingNumber\":\"" + drb.getNumberSamples() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "    [\n" +
                    "        {\n" +
                    "        \"Title\":\"样品编号\",\n" +
                    "        \"Id\":\"projectName\",\n" +
                    "        \"Value\":\"" + drb.getNumberSamples() + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"Title\":\"样品名称\",\n" +
                    "            \"Id\":\"xlz\",\n" +
                    "            \"Value\":\"" + drb.getSampleName() + "\"\n" +
                    "            }\n" +
                    "            ,{\n" +
                    "                \"Title\":\"被检单位\",\n" +
                    "                \"Id\":\"sampleSource\",\n" +
                    "                \"Value\":\"" + drb.getDetectionCompany() + "\"\n" +
                    "                },\n" +
                    "{\n" +
                    "    \"Title\":\"商品来源\",\n" +
                    "    \"Id\":\"samplingDept\",\n" +
                    "    \"Value\":\"" + drb.getCommodityPlaceOrigin() + "\"\n" +
                    "    }]}";

        }
        Log.i("lcy", "uploadData3: ----自动分光---" + data);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                upload_status = 3;

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean3 testDataBean3 = null;
                testDataBean3 = new Gson().fromJson(ret, TestDataBean3.class);

                if (testDataBean3 != null) {
                    TestDataBean2 data1 = testDataBean3.getData();
                    if (data1 != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errmessage = data1.getErrMsg();
                                if ("0".equals(data1.getErrCode())) {
                                    upload_status = 1;
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("uploadStatus", "已上传");
                                    hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
                                    loadData();
                                } else {
                                    upload_status = 2;
                                }
                            }
                        });
                    }
                }
            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                TestDataBean3 testDataBean3 = null;
//                testDataBean3 = new Gson().fromJson(response, TestDataBean3.class);
//
//                if (testDataBean3 != null) {
//                    TestDataBean2 data1 = testDataBean3.getData();
//                    if (data1 != null) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                errmessage = data1.getErrMsg();
//                                if ("0".equals(data1.getErrCode())) {
//                                    upload_status = 1;
//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put("uploadStatus", "已上传");
//                                    hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
//                                    loadData();
//                                } else {
//                                    upload_status = 2;
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onError(String response) {
//                upload_status = 3;
//            }
//        });
    }

    // 分光光度手动录入检测记录的上传
    public void uploadData4(DetectionResultBean drb) {
        String user = drb.getOperatorId() + "";
        String terrace = drb.getAreaId() + "";
        String deptId = drb.getDeptId() + "";
        String str_api = "";
        // 只有单金标且登录用户是唐山的用户才使用 "Tangshan/SendForNAD4074" api
        // 其他的都是 "CommonManual/GetSamplingTypeList" api
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {
            str_api = "CommonManual/UpdateManualSampling";
        }
        String result = "";
        if ("合格".equals(drb.getDetectionResult())) {
            result = "0";
        } else if ("不合格".equals(drb.getDetectionResult())) {
            result = "1";
        } else {
            result = "-1";
        }
        if ("".equals(LocationX) && "".equals(LocationY) && "".equals(LocationAddress)) {
            locationMsg = MainActivity.gt_sp.query("locationMsg").toString();
            MainActivity.gt_sp.save("locationMsg", locationMsg);

            String[] split_something = locationMsg.split(",");

            LocationX = split_something[0];
            LocationY = split_something[1];
            LocationAddress = split_something[2];
        }
        String data = "";
        // 新的上传
        if ("多参数食品安全检测仪".equals(com.open.soft.openappsoft.util.InterfaceURL.oneModule)) {

            data = "[{\"QRCode\":\"" + drb.getQRCode().replaceAll("\\s*", "") + "\",\n" +
                    "\"LocationX\":\"" + LocationX + "\",\n" +
                    "\"LocationY\":\"" + LocationY + "\",\n" +
                    "\"LocationAddress\":\"" + LocationAddress + "\",\n" +
                    "\"Result\":\"" + result + "\",\n" +
                    "\"ResultValue\":\"" + drb.getDetectionResult() + "\",\n" +
                    "\"SamplingNumber\":\"" + drb.getNumberSamples() + "\",\n" +
                    "\"AreaId\":\"" + terrace + "\",\n" +
                    "\"OperatorId\":\"" + com.example.utils.http.Global.ID + "\",\n" +
                    "\"ResultData\":\n" +
                    "[\n" +
                    "{\"Title\":\"部门id\",\"Id\":\"deptId\",\"Value\":\"" + deptId + "\"},\n" +
                    "{\"Title\":\"样品来源地1级\",\"Id\":\"source1\",\"Value\":\"" + drb.getSpId1() + "\"},\n" +
                    "{\"Title\":\"样品来源地2级\",\"Id\":\"source2\",\"Value\":\"" + drb.getSpId2() + "\"},\n" +
                    "{\"Title\":\"样品来源地3级\",\"Id\":\"source3\",\"Value\":\"" + drb.getSpId3() + "\"},\n" +
                    "{\"Title\":\"样品总分类Id\",\"Id\":\"sampleClassId\",\"Value\":\"" + drb.getSampleId() + "\"},\n" +
                    "{\"Title\":\"样本类别Id\",\"Id\":\"sampleTypeId\",\"Value\":\"" + "\"},\n" +
                    "{\"Title\":\"检测对象名称\",\"Id\":\"objectName\",\"Value\":\"" + drb.getSpecimenType() + "\"},\n" +
                    "{\"Title\":\"被检企业名称\",\"Id\":\"companyName\",\"Value\":\"" + drb.getDetectionCompany() + "\"},\n" +
                    "{\"Title\":\"产品Id\",\"Id\":\"productId\",\"Value\":\"" + drb.getYplbId() + "\"},\n" +
                    "{\"Title\":\"对象Id\",\"Id\":\"objectId\",\"Value\":\"" + drb.objectId + "\"},\n" +
                    "{\"Title\":\"设备编号\",\"Id\":\"deviceSn\",\"Value\":\"" + MainActivity.mac_url + "\"},\n" +
                    "{\"Title\":\"设备型号\",\"Id\":\"deviceType\",\"Value\":\"" + "MD_417" + "\"},\n" +
                    "{\"Title\":\"样品详细地址\",\"Id\":\"sampleAddress\",\"Value\":\"" + drb.getCommodityPlaceOrigin() + "]}\n" +
                    "]";

        }
        Timber.i("上传数据2号：" + data);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(InterfaceURL.BASE_URL + str_api)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                upload_status = 3;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret = response.body().string();
                TestDataBean1 uploadData1 = new Gson().fromJson(ret, TestDataBean1.class);
                if (uploadData1 != null) {
                    String errMsg = uploadData1.getData().get(0).getErrMsg();
                    String errCode = uploadData1.getData().get(0).getErrCode();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errmessage = errMsg;
                            if ("0".equals(errCode)) {
                                upload_status = 1;
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("uploadStatus", "已上传");
                                hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
                                loadData();
                            } else {
                                upload_status = 2;
                            }
                        }
                    });
                }

            }
        });
//        GT.HttpUtil.postRequest(InterfaceURL.BASE_URL + str_api, data, new GT.HttpUtil.OnLoadData() {
//            @Override
//            public void onSuccess(String response) {
//                TestDataBean1 uploadData1 = new Gson().fromJson(response, TestDataBean1.class);
//                if (uploadData1 != null) {
//                    String errMsg = uploadData1.getData().get(0).getErrMsg();
//                    String errCode = uploadData1.getData().get(0).getErrCode();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            errmessage = errMsg;
//                            if ("0".equals(errCode)) {
//                                upload_status = 1;
//                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("uploadStatus", "已上传");
//                                hibernate.where("id = ?", new String[]{drb.getID() + ""}).update("DetectionResultBean", contentValues);
//                                loadData();
//                            } else {
//                                upload_status = 2;
//                            }
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onError(String response) {
//                upload_status = 3;
//            }
//        });
    }


    // 胶体金数据打印[朝外打印]
    public static String GetPrintInfo1(DetectionResultBean detectionResultBean, Context context) {
        String title = LoginActivity.sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            title = InterfaceURL.oneModule;
        }
        StringBuffer sb = new StringBuffer("\n");

        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        detectionResultBean.getDetectionTime(), "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("检测结果：");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("检 测 值：");
        sb.append(detectionResultBean.getDetectionValue() + "\n");


        sb.append("检 测 限：");
        sb.append(detectionResultBean.getLimitStandard() + "\n");

        sb.append("样品编号：");
        sb.append(detectionResultBean.getNumberSamples() + "\n");


        List list1 = getStrList1(detectionResultBean.getCommodityPlaceOrigin());
        Log.d("list1", list1.toString());
        Log.d("list1.size() ", "" + list1.size() + "");

        if (0 < detectionResultBean.getCommodityPlaceOrigin().length() && detectionResultBean.getCommodityPlaceOrigin().length() <= 11) {
            sb.append("样品来源：");
            sb.append(list1.get(0) + "\n");
        } else if (11 < detectionResultBean.getCommodityPlaceOrigin().length() && detectionResultBean.getCommodityPlaceOrigin().length() <= 27) {
            sb.append(list1.get(1) + "\n");
            sb.append("样品来源：");
            sb.append(list1.get(0) + "\n");
        } else if (27 < detectionResultBean.getCommodityPlaceOrigin().length() && detectionResultBean.getCommodityPlaceOrigin().length() <= 43) {
            sb.append(list1.get(0) + "\n");
            sb.append("样品来源：");
            sb.append(list1.get(1) + "\n");
            sb.append(list1.get(2) + "\n");
        }

        sb.append("检测项目：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("样品名称：");
        sb.append(detectionResultBean.getSampleName() + "\n");

        sb.append("样品类型：");
        sb.append(detectionResultBean.getSpecimenType() + "\n");
//        sb.append("检 验 员：");
//        sb.append(detectionResultBean.getInspector() + "\n");
        if (detectionResultBean.getInspector().length() > 11) {
            sb.append(detectionResultBean.getInspector().substring(11) + "\n");
            sb.append("检 验 员：");
            sb.append(detectionResultBean.getInspector().substring(0, 11) + "\n");
        } else {
            sb.append("检 验 员：");
            sb.append(detectionResultBean.getInspector() + "\n");
        }


        if (detectionResultBean.getDetectionCompany().length() > 11) {
            sb.append(detectionResultBean.getDetectionCompany().substring(11) + "\n");
            sb.append("检测单位：");
            sb.append(detectionResultBean.getDetectionCompany().substring(0, 11) + "\n");
        } else {
            sb.append("检测单位：");
            sb.append(detectionResultBean.getDetectionCompany() + "\n");
        }

        sb.append("\n" + title + "\n");
        sb.append("\n\n\n\n");
//        sb.append("检 测 值：");
//        sb.append(detectionResultBean.getDetectionValue() + "\n");
        // 分割商品来源字符串
//		List list1 = getStrList(model.sample_unit,11);
        //sb.append("检测流水号：");
        // sb.append((detectionResultBean.getCheckRunningNumber() + 1) + "\n");
//        sb.append(context.getResources().getString(R.string.app_name) + "\n\n\n\n\n");
        return sb.toString();
    }


    // 胶体金打印[朝内打印]
    public static String GetPrintInfo3(DetectionResultBean detectionResultBean, Context context) {
//        StringBuffer sb = new StringBuffer("\n\n\n\n\n" + context.getResources().getString(R.string.app_name) + "\n\n");
        StringBuffer sb = new StringBuffer("\n\n\n\n\n" + InterfaceURL.oneModule + "\n\n");
        sb.append("检测流水号：");
        sb.append((detectionResultBean.getCheckRunningNumber() + 1) + "\n");

        sb.append("检测单位：");
        sb.append(detectionResultBean.getDetectionCompany() + "\n");

        sb.append("检 验 员：");
        sb.append(detectionResultBean.getInspector() + "\n");


        sb.append("样品名称：");
        sb.append(detectionResultBean.getSampleName() + "\n");

        sb.append("检测项目：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("样品类型：");
        sb.append(detectionResultBean.getSpecimenType() + "\n");

        sb.append("商品来源：");
        sb.append(detectionResultBean.getCommodityPlaceOrigin() + "\n");

        sb.append("样品编号：");
        sb.append(detectionResultBean.getNumberSamples() + "\n");

        sb.append("检 测 限：");
        sb.append(detectionResultBean.getLimitStandard() + "\n");

        sb.append("检 测 值：");
        sb.append(detectionResultBean.getDetectionValue() + "\n");

        sb.append("检测结果：");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        detectionResultBean.getDetectionTime(), "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");
        sb.append("\n\n\n\n\n");
        return sb.toString();
    }

    // 地址字符串分割
    public static List<String> getStrList1(String inputString) {
        List<String> list = new ArrayList<String>();

        if (0 < inputString.length() && inputString.length() <= 11) {
            list.add(inputString);
        } else if (11 < inputString.length() && inputString.length() <= 27) {
            list.add(inputString.substring(0, 11));
            list.add(inputString.substring(11, inputString.length()));
        } else if (27 < inputString.length() && inputString.length() <= 43) {
            list.add(inputString.substring(0, 11));
            list.add(inputString.substring(11, 27));
            list.add(inputString.substring(27, inputString.length()));
        }
        return list;
    }

    // 分光光度数据打印[朝外打印]
    public static String GetPrintInfo2(DetectionResultBean detectionResultBean, Context context) {
        StringBuffer sb = new StringBuffer("\n\n");

        String title = LoginActivity.sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            title = InterfaceURL.oneModule;
        }
        /*sb.append("检测单位：");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("检测人员：");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("样本类型：");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("样品名称：");
        sb.append(detectionResultBean.getSampleName() + "\n");

        sb.append("检测项目：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("样本来源：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("样品编号：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("限量标准：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("检测值：");
        sb.append(detectionResultBean.getDetectionValue() + "\n");

        sb.append("判定结果：  ");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                detectionResultBean.getDetectionTime(), "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("\n\n\n\n\n");*/
        sb.append("\n\n");
        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        detectionResultBean.getDetectionTime(), "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("检测结果：");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("抑 制 率：");
        sb.append(detectionResultBean.getDetectionValue() + "\n");

        sb.append("检 测 限：");
        sb.append(detectionResultBean.getLimitStandard() + "\n");

        sb.append("样品编号：");
        sb.append(detectionResultBean.getNumberSamples() + "\n");


        List list1 = getStrList1(detectionResultBean.getCommodityPlaceOrigin());
        Log.d("list1", list1.toString());
        Log.d("list1.size() ", "" + list1.size() + "");

        if (0 < detectionResultBean.getCommodityPlaceOrigin().length() && detectionResultBean.getCommodityPlaceOrigin().length() <= 11) {
            sb.append("样品来源：");
            sb.append(list1.get(0) + "\n");
        } else if (11 < detectionResultBean.getCommodityPlaceOrigin().length() && detectionResultBean.getCommodityPlaceOrigin().length() <= 27) {
            sb.append(list1.get(1) + "\n");
            sb.append("样品来源：");
            sb.append(list1.get(0) + "\n");
        } else if (27 < detectionResultBean.getCommodityPlaceOrigin().length() && detectionResultBean.getCommodityPlaceOrigin().length() <= 43) {
            sb.append(list1.get(0) + "\n");
            sb.append("样品来源：");
            sb.append(list1.get(1) + "\n");
            sb.append(list1.get(2) + "\n");
        }

        sb.append("检测项目：");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("样品名称：");
        sb.append(detectionResultBean.getSampleName() + "\n");

        sb.append("样品类型：");
        sb.append(detectionResultBean.getSpecimenType() + "\n");

//        sb.append("检 验 员：");
//        sb.append(detectionResultBean.getInspector() + "\n");
        if (detectionResultBean.getInspector().length() > 11) {
            sb.append(detectionResultBean.getInspector().substring(11) + "\n");
            sb.append("检 验 员：");
            sb.append(detectionResultBean.getInspector().substring(0, 11) + "\n");
        } else {
            sb.append("检 验 员：");
            sb.append(detectionResultBean.getInspector() + "\n");
        }

        if (detectionResultBean.getDetectionCompany().length() > 11) {
            sb.append(detectionResultBean.getDetectionCompany().substring(11) + "\n");
            sb.append("检测单位：");
            sb.append(detectionResultBean.getDetectionCompany().substring(0, 11) + "\n");
        } else {
            sb.append("检测单位：");
            sb.append(detectionResultBean.getDetectionCompany() + "\n");
        }

        sb.append("通 道 号：");
        sb.append(detectionResultBean.getAisle() + "\n");
        sb.append("\n" + title + "\n\n\n\n");
        sb.append("\n\n");

        return sb.toString();
    }

    // 分光光度数据打印[朝内打印]
    public static String GetPrintInfo4(DetectionResultBean detectionResultBean, Context context) {
        StringBuffer sb = new StringBuffer("\n\n\n\n\n");

        sb.append("检测流水号：");
        sb.append((detectionResultBean.getCheckRunningNumber() + 1) + "\n");

        sb.append("判定结果：  ");
        sb.append(detectionResultBean.getDetectionResult() + "\n");

        sb.append("检测值：");
        sb.append(detectionResultBean.getDetectionValue() + "\n");

        sb.append("检测项目：  ");
        sb.append(detectionResultBean.getTestItem() + "\n");

        sb.append("样品名称：  ");
        sb.append(detectionResultBean.getSampleName() + "\n");

        sb.append("检测时间：  ");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        detectionResultBean.getDetectionTime(), "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("\n\n\n\n\n");

        return sb.toString();
    }

    /**
     * 数据去重并添加至数组
     */
    private static String[] insert(String[] arr, String str) {

        int size = arr.length; // 获取原数组长度
        int newSize = size + 1; // 原数组长度加上追加的数据的总长度

        // 新建临时字符串数组
        String[] tmp = new String[newSize];

        if (size == 0) {
            tmp[0] = str;
            return tmp;
        }

        for (int j = 0; j < size; j++) {
            tmp[j] = arr[j];
        }


        for (int i = 0; i < size; i++) {
            if (tmp[i] != null && tmp[i].equals(str)) {
                tmp = new String[size];
                for (int j = 0; j < size; j++) {
                    tmp[j] = arr[j];
                }
                break;
            } else {
                tmp[size] = str;
            }
        }

        return tmp; // 返回拼接完成的字符串数组
    }

    /**
     * 数据添加int数组
     */

    private static int[] insertInt(int[] arr, int str) {

        int size = arr.length; // 获取原数组长度
        int newSize = size + 1; // 原数组长度加上追加的数据的总长度

        // 新建临时字符串数组
        int[] tmp = new int[newSize];

        if (size == 0) {
            tmp[0] = str;
            return tmp;
        }

        for (int j = 0; j < size; j++) {
            tmp[j] = arr[j];
        }


        for (int i = 0; i < size; i++) {
            tmp[size] = str;
        }

        return tmp; // 返回拼接完成的int数组
    }


    private String locationMsg = "";
    private String LocationX = "";
    private String LocationY = "";
    private String LocationAddress = "";

    // 获取定位信息
    private void set_dingwei_info() {
        // 如果第一次定位失败且本地未存储定位成功信息就设置为空字符串

        try {
            locationMsg = MainActivity.gt_sp.query("locationMsg").toString();
            MainActivity.gt_sp.save("locationMsg", locationMsg);

            String[] split_something = locationMsg.split(",");

            LocationY = split_something[0];
            LocationX = split_something[1];
            LocationAddress = split_something[2];

        } catch (Exception e) {
            e.printStackTrace();
            LocationX = "";
            LocationY = "";
            LocationAddress = "";
        }
    }


    // 导出
    private final static String EXPORT_DIR = "/检测记录/";
    //    private final static String[] EXCEL_HEADER = {"经营户/负责人", "商品名", "检测项目名", "检测值", "检测单位名称", "检测结果"};
    private final static String[] EXCEL_HEADER = {"数据类型", "检测流水号", "样品编号", "检测时间", "通道", "样品名称", "样品类型", "限量标准", "临界值", "样品浓度", "T/C值", "检测项目", "检测结果", "被检测单位", "检测人员", "检测单位", "重量", "商品来源", "上传状态"};
    private List<DetectionResultBean> list_exportdata;
    private int export_status = 0;

    private void exportData2() {

        // 存储流水号
        String[] index_list = new String[sql_rv.getChildCount()];
        // 存储选中的数据
//        list_exportdata = new ArrayList<DetectionResultBean>();
        // 导出的状态
        export_status = 0;
        // 判断是否有选中的数据
        boolean[] have_chenked = {false};
        list_exportdata = new ArrayList<DetectionResultBean>();
        int count1 = 0;
        for (int i = 0; i < sql_rv.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) sql_rv.getChildAt(i);
            while (true) {
                View childAt = viewGroup.getChildAt(0);
                if (childAt instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) childAt;
                    if (checkBox.isChecked()) {

                        String number = "";
                        ViewParent parent = checkBox.getParent().getParent();
                        if (parent instanceof LinearLayout) {
                            LinearLayout linearLayout = (LinearLayout) parent;
                            View childAt1 = linearLayout.getChildAt(2);
                            if (childAt1 instanceof TextView) {
                                TextView tv_number = (TextView) childAt1;
                                number = tv_number.getText().toString();
                            }
                        }

                        index_list[count1] = number;
                        have_chenked[0] = true;
                        int index = Integer.parseInt(number) - 1;
                        DetectionResultBean detectionResultBean = temporaryData.get(index);
                        if (detectionResultBean != null) {
                            list_exportdata.add(count1, detectionResultBean);
                        }
                        count1++;
                    }
                    break;
                }

                viewGroup = (ViewGroup) childAt;
            }
        }
        // 确保有数据
        if (list_exportdata == null || list_exportdata.isEmpty()) {
            Log.e("lcy", "没有数据");
            return;
        }
        Log.e("lcy", "------" + list_exportdata + "==============");
        String rootPath = Environment.getExternalStorageDirectory() + EXPORT_DIR;
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
            Uri uri = Uri.parse("file://" + file.getAbsolutePath() + "/");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_STARTED, uri));
        }

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        // 输出的excel的路径
        String filePath = rootPath + df.format(new Date()) + ".xls";
        // 创建Excel工作薄
        WritableWorkbook wwb;
        // 新建立一个jxl文件,即在SDcard盘下生成test.xls
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("数据", 0);
            /*for (int i = 0; i < HEADERS.length; i++) {
                sheet.addCell(new Label(i, 0, HEADERS[i]));
            }*/

            //set column width
            for (int i = 0; i < EXCEL_HEADER.length; i++) {
                String header = EXCEL_HEADER[i];
                sheet.setColumnView(i, header.getBytes().length + 2);
            }
            //set row height
            sheet.setRowView(0, 600);

            //write header
            WritableFont defaultFont = new WritableFont(WritableFont.ARIAL, 11);
            WritableCellFormat defaultCellFormat = new WritableCellFormat(defaultFont);
            defaultCellFormat.setAlignment(Alignment.CENTRE);
            defaultCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont defaultHeaderFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
            WritableFont mainHeaderFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            WritableCellFormat mainHeaderCellFormat = new WritableCellFormat(mainHeaderFont);
            mainHeaderCellFormat.setAlignment(Alignment.CENTRE);
            mainHeaderCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            mainHeaderCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            sheet.addCell(new Label(0, 0, "检测信息导入", mainHeaderCellFormat));
            sheet.mergeCells(0, 0, 18, 0);


            WritableCellFormat headerCellFormat = new WritableCellFormat(defaultHeaderFont);
            headerCellFormat.setAlignment(Alignment.CENTRE);
            headerCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            sheet.addCell(new Label(0, 1, "检测时间", headerCellFormat));
            sheet.addCell(new Label(1, 1, com.open.soft.openappsoft.multifuction.util.Global.getCurrentDate(), defaultCellFormat));
            sheet.addCell(new Label(2, 1, "", defaultCellFormat));
            sheet.mergeCells(2, 1, 18, 1);

            WritableFont subMainHeaderFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD);
            WritableCellFormat subMainHeaderCellFormat = new WritableCellFormat(subMainHeaderFont);
            subMainHeaderCellFormat.setAlignment(Alignment.CENTRE);
            subMainHeaderCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            sheet.addCell(new Label(0, 2, "经营户信息详情", subMainHeaderCellFormat));
            sheet.mergeCells(0, 2, 18, 2);

            for (int i = 0; i < EXCEL_HEADER.length; i++) {
                sheet.addCell(new Label(i, 3, EXCEL_HEADER[i], headerCellFormat));
            }

            if (list_exportdata != null) {
                for (int i = 0; i < list_exportdata.size(); i++) {
                    DetectionResultBean drb = list_exportdata.get(i);
                    int lineIdx = 4 + i;
                    sheet.addCell(new Label(0, lineIdx, drb.getSQLType(), defaultCellFormat)); //数据类型
                    sheet.addCell(new Label(1, lineIdx, index_list[i] + "", defaultCellFormat)); //检测流水号
                    sheet.addCell(new Label(2, lineIdx, drb.getNumberSamples(), defaultCellFormat)); //样品编号
                    sheet.addCell(new Label(3, lineIdx, drb.getDetectionTime() + "", defaultCellFormat)); //检测时间
                    sheet.addCell(new Label(4, lineIdx, drb.getAisle(), defaultCellFormat)); //通道
                    sheet.addCell(new Label(5, lineIdx, drb.getSampleName(), defaultCellFormat)); //样品名称
                    sheet.addCell(new Label(6, lineIdx, drb.getSpecimenType(), defaultCellFormat)); //样品类型
                    sheet.addCell(new Label(7, lineIdx, drb.getLimitStandard(), defaultCellFormat)); //限量标准
                    sheet.addCell(new Label(8, lineIdx, drb.getCriticalValue(), defaultCellFormat)); //临界值
                    sheet.addCell(new Label(9, lineIdx, drb.getSampleConcentration(), defaultCellFormat)); //样品浓度
                    sheet.addCell(new Label(10, lineIdx, drb.getDetectionValue(), defaultCellFormat)); //抑制率/检测值
                    sheet.addCell(new Label(11, lineIdx, drb.getTestItem(), defaultCellFormat)); //检测项目
                    sheet.addCell(new Label(12, lineIdx, drb.getDetectionResult(), defaultCellFormat)); //检测结果
                    sheet.addCell(new Label(13, lineIdx, drb.getUnitsUnderInspection(), defaultCellFormat)); //被检单位
                    sheet.addCell(new Label(14, lineIdx, drb.getInspector(), defaultCellFormat)); //检测人员
                    sheet.addCell(new Label(15, lineIdx, drb.getDetectionCompany(), defaultCellFormat)); //检测单位
                    sheet.addCell(new Label(16, lineIdx, drb.getInspector(), defaultCellFormat)); //重量
                    sheet.addCell(new Label(17, lineIdx, drb.getCommodityPlaceOrigin(), defaultCellFormat)); //商品来源
                    sheet.addCell(new Label(18, lineIdx, drb.getUploadStatus(), defaultCellFormat)); //上传状态
                    //sheet.addCell(new Label(11, i + 1, ToolUtils.long2String(result.testTime, "yyyy-MM-dd HH:mm:ss")));
                }
            }
            ((FileOutputStream) os).getFD().sync();
            wwb.write();
            wwb.close();

            File saveFile = new File(filePath);

            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "数据导出成功,存储位置：" + saveFile.getAbsolutePath(), true);
            sendMediaScanIntent(filePath);

            export_status = 1;
            //APPUtils.showToast(this, "数据导出成功，导出路径为：" + filePath);
        } catch (Exception e) {
            export_status = 2;
            e.printStackTrace();
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "数据导出失败");
        }

    }


    private void sendMediaScanIntent(String fileName) {
        File file = new File(fileName);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        sendBroadcast(intent);
    }


    private ProgressDialog waitDialog;

    private void showWaitDialog() {
        waitDialog = new ProgressDialog(this);
        waitDialog.setMessage("正在导出,请稍后...");
        waitDialog.setCancelable(false);
        waitDialog.show();
    }
}
