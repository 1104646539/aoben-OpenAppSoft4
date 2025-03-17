package com.open.soft.openappsoft.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.utils.http.Global;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.lzy.okgo.callback.StringCallback;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.SearchActivity;
import com.open.soft.openappsoft.bean.AddComBean;
import com.open.soft.openappsoft.jinbiao.model.SampleSourceData;
import com.open.soft.openappsoft.jinbiao.model.SampleSourceJsonRootBean;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeData;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeJsonRootBean;
import com.open.soft.openappsoft.jinbiao.util.APPUtils;
import com.open.soft.openappsoft.multifuction.activity.PesticideTestActivity2;
import com.open.soft.openappsoft.util.InterfaceURL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

@GT.Annotations.GT_AnnotationDialogFragment(R.layout.dialog_input_message)
//public class DialogFragmentDemo2 extends GT.GT_Fragment.AnnotationDialogFragment implements AdapterView.OnItemSelectedListener {
public class DialogFragmentDemo2 extends GT.GT_Dialog.BaseDialogFragment implements AdapterView.OnItemSelectedListener {

    @GT.Annotations.GT_View(R.id.et_input_ypmc)//样品名称
    private EditText et_input_ypmc;
    @GT.Annotations.GT_View(R.id.et_input_ypbh)//样品编号
    private EditText et_input_ypbh;
    @GT.Annotations.GT_View(R.id.et_input_bjdw)//备检单位
    private EditText et_input_bjdw;

    @GT.Annotations.GT_View(R.id.et_companyCode)//组织机构
    private TextView et_company_code;
    //组织机构是否显示
    @GT.Annotations.GT_View(R.id.ll_company2)//组织机构
    private LinearLayout llCompany;
    @GT.Annotations.GT_View(R.id.iv_ts_search)//搜索
    private ImageView ivTsSearch;

    @GT.Annotations.GT_View(R.id.et_input_bjdw_add)//备检单位不填写
    private TextView et_input_bjdw_add;
    private String needCompanyCode;


    @GT.Annotations.GT_View(R.id.sp_yplx)
    private Spinner sp_yplx;
    @GT.Annotations.GT_View(R.id.sp_yplx1)
    private Spinner sp_yplx1;
    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map1;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleTypeData> sampletyelist1;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleTypeData> sampletyelist2;

    //获取组件
    @GT.Annotations.GT_View(R.id.sp1)
    private Spinner sp1;
    @GT.Annotations.GT_View(R.id.sp2)
    private Spinner sp2;
    @GT.Annotations.GT_View(R.id.sp3)
    private Spinner sp3;

    // 新样品来源相关初始化
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleSourceData> sampleSources1;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleSourceData> sampleSources2;
    @GT.Annotations.GT_Collection.GT_List
    private List<SampleSourceData> sampleSources3;

    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map;


    //省市区的ID
    private String spId1, spId2, spId3;


    private String[] sp_yplxArray;//样品类别字符串
    private String[] sp_yplxArrayId;//样品类别ID
    private String[] sp_subId;//样品类别ID

    private Activity activity;


    private String samplesource = "";
    private String samplename = "";
    private String sampleid = "";
    private String sampletype = "";
    private String samplebcheckedOrganization = "";
    private String yplbId = ""; //样品类别Id
    private String companyCode = "";//组织机构

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public static DialogFragmentDemo2 newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }

        DialogFragmentDemo2 fragment = new DialogFragmentDemo2();
        fragment.setArguments(bundle);


        return fragment;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        build(this, view);//绑定  DialogFragment

        Bundle arguments = getArguments();

//        setClickExternalNoHideDialog();//点击外部不会消息

        // 限制用户输入长度
        et_input_ypmc.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        et_input_ypbh.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        et_input_bjdw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});


        // 初始化样品类型下拉组件
        sp_yplx.setOnItemSelectedListener(this);
        sp_yplx1.setOnItemSelectedListener(this);
        initSampleTypes("", 1);


        //初始化
        sp1.setOnItemSelectedListener(this);
        sp2.setOnItemSelectedListener(this);
        sp3.setOnItemSelectedListener(this);

        //初始化第一个下拉组件数据
        initSp("0001", 1);


        sp_yplx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                tv_check_type.setText(parent.getSelectedItem().toString());
                String select = parent.getSelectedItem().toString();

                for (SampleTypeData data : sampletyelist1) {
                    if (data.getSampleName().equals(select)) {
                        String sampleId = data.getSampleId();
                        String sample_Name = data.getSampleName();
                        PesticideTestActivity2.sampleId = sampleId;
                        PesticideTestActivity2.sampleName = sample_Name;
                        break;
                    }
                }

                loadSelectSampleTypeData(sampletyelist1, select, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_yplx1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = parent.getSelectedItem().toString();
                loadSelectSampleTypeData(sampletyelist2, select, 2);
                et_input_ypmc.setText(select);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //判断是否需要组织机构代码
        needCompanyCode = Global.NEEDCompanyCode;

        if (needCompanyCode.equals("0")) {
            llCompany.setVisibility(View.GONE);
            ivTsSearch.setVisibility(View.GONE);
            Log.i("lcy", "initView: ----View.GONE---");

        } else {
            et_input_bjdw.setVisibility(View.GONE);
            et_input_bjdw_add.setVisibility(View.VISIBLE);
        }

        //图片跳转搜索界面
        ivTsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("ts", "other");
                startActivityForResult(intent, 1001);
            }
        });

    }

    @GT.Annotations.GT_Click({R.id.btn_confirm, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:

                if ("".equals(et_input_ypmc.getText().toString()) || et_input_ypmc.getText().toString() == null) {
                    APPUtils.showToast(activity, "请先输入样品名称");
                } else if ("".equals(et_input_ypbh.getText().toString()) || et_input_ypbh.getText().toString() == null) {
                    APPUtils.showToast(activity, "请先输入样品编号");
                }
//                else if ("".equals(et_input_bjdw.getText().toString()) || et_input_bjdw.getText().toString() == null) {
//                    APPUtils.showToast(activity, "请先输入被检单位");
//                }
                else {
                    samplename = et_input_ypmc.getText().toString();
                    sampleid = et_input_ypbh.getText().toString();
                    sampletype = sp_yplx1.getSelectedItem().toString();
                    String str1 = "";
                    String str2 = "";
                    String str3 = "";
                    try {
                        sp1.getSelectedItem().toString();
                        samplesource += sp1.getSelectedItem().toString();
                    } catch (Exception e) {
                        samplesource += "";
                    }
                    try {
                        sp2.getSelectedItem().toString();
                        samplesource += sp2.getSelectedItem().toString();
                    } catch (Exception e) {
                        samplesource += "";
                    }
                    try {
                        sp3.getSelectedItem().toString();
                    } catch (Exception e) {
                        samplesource += "";
                    }
                    //被检单位判断填写  这里拿到的
                    if (needCompanyCode.equals("0")) {
                        samplebcheckedOrganization = et_input_bjdw.getText().toString();

                    } else {
                        samplebcheckedOrganization = et_input_bjdw_add.getText().toString();
                    }

                    // 样品类别id
                    for (int i = 0; i < sp_yplxArray.length; i++) {
                        if (sp_yplx1.getSelectedItem().toString().equals(sp_yplxArray[i])) {
                            yplbId = sp_yplxArrayId[i];
                            break;
                        }
                    }
                    companyCode = et_company_code.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("samplename", samplename);//样品类型
                    intent.putExtra("sampleid", sampleid);//样品编号
                    intent.putExtra("sampletype", sampletype);//样品名称
                    intent.putExtra("samplesource", samplesource);//样品来源
                    intent.putExtra("samplebcheckedOrganization", samplebcheckedOrganization);
                    intent.putExtra("yplbId", yplbId);//样品类别id
                    intent.putExtra("spId1", spId1);
                    intent.putExtra("spId2", spId2);
                    intent.putExtra("spId3", spId3);
                    intent.putExtra("companyCode", companyCode);//组织机构
                    ((PesticideTestActivity2) getActivity()).onActivityResult(PesticideTestActivity2.REQUEST_CODE, Activity.RESULT_OK, intent);
                    finish();

                }

                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    protected boolean onBackPressed() {

        Timber.e("监听到返回");

        return true;
    }


    private void initSampleTypes(String parentId, int level) {

        // 获取参数
        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;
        map1.clear();
        map1.put("AreaId", AreaId);
        map1.put("OperatorId", OperatorId);
        map1.put("ParentId", parentId);
//        EditURLDialog editURLDialog = new EditURLDialog(getContext());

        WaitDialog.show("数据加载中");
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "CommonManual/GetSamplingTypeList", map1, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                WaitDialog.dismiss();
                String body = response;
                //解析数据
                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);

                //为Sp设置数据
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (level) {
                            case 1:

                                sampletyelist1 = sampleTypeJsonRootBean.getData();
                                String[] array = new String[sampletyelist1.size()];
                                for (int i = 0; i < sampletyelist1.size(); i++) {
                                    array[i] = sampletyelist1.get(i).getSampleName();
                                }
                                ArrayAdapter<String> adapter;
                                try {
                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
                                    sp_yplx.setAdapter(adapter);
                                } catch (Exception e) {
                                    APPUtils.showToast(activity, "数据还未加载完成");
                                }

                                //样品类型
                                for (SampleTypeData sampleTypeData : sampletyelist1) {
                                    if (PesticideTestActivity2.sampleId == null) {
                                        PesticideTestActivity2.sampleId = sampleTypeData.getSampleId();
                                    }
                                }

                                break;
                            case 2:

                                spId1 = parentId;
                                sampletyelist2 = sampleTypeJsonRootBean.getData();
                                array = new String[sampletyelist2.size()];
                                sp_yplxArray = new String[sampletyelist2.size()];
                                sp_yplxArrayId = new String[sampletyelist2.size()];
                                for (int i = 0; i < sampletyelist2.size(); i++) {
                                    array[i] = sampletyelist2.get(i).getSampleName();
                                    sp_yplxArrayId[i] = sampletyelist2.get(i).getSampleId();
                                    sp_yplxArray[i] = sampletyelist2.get(i).getSampleName();

                                }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
                                try {
                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
                                    sp_yplx1.setAdapter(adapter);
                                } catch (Exception e) {
                                    APPUtils.showToast(activity, "数据还未加载完成");
                                }

                                break;
                            case 3:
                                spId2 = parentId;
                                break;
                        }

                    }
                });
            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
        //网络请求
//        new GT.OkGo(InterfaceURL.BASE_URL + "CommonManual/GetSamplingTypeList", map1).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                WaitDialog.dismiss();
//                String body = response.body();
//                //解析数据
//                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);
//
//                //为Sp设置数据
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        switch (level) {
//                            case 1:
//
//                                sampletyelist1 = sampleTypeJsonRootBean.getData();
//                                String[] array = new String[sampletyelist1.size()];
//                                for (int i = 0; i < sampletyelist1.size(); i++) {
//                                    array[i] = sampletyelist1.get(i).getSampleName();
//                                }
//                                ArrayAdapter<String> adapter;
//                                try {
//                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
//                                    sp_yplx.setAdapter(adapter);
//                                } catch (Exception e) {
//                                    APPUtils.showToast(activity, "数据还未加载完成");
//                                }
//
//                                //样品类型
//                                for (SampleTypeData sampleTypeData : sampletyelist1) {
//                                    if (PesticideTestActivity2.sampleId == null) {
//                                        PesticideTestActivity2.sampleId = sampleTypeData.getSampleId();
//                                    }
//                                }
//
//                                break;
//                            case 2:
//
//                                spId1 = parentId;
//                                sampletyelist2 = sampleTypeJsonRootBean.getData();
//                                array = new String[sampletyelist2.size()];
//                                sp_yplxArray = new String[sampletyelist2.size()];
//                                sp_yplxArrayId = new String[sampletyelist2.size()];
//                                for (int i = 0; i < sampletyelist2.size(); i++) {
//                                    array[i] = sampletyelist2.get(i).getSampleName();
//                                    sp_yplxArrayId[i] = sampletyelist2.get(i).getSampleId();
//                                    sp_yplxArray[i] = sampletyelist2.get(i).getSampleName();
//
//                                }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
//                                try {
//                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
//                                    sp_yplx1.setAdapter(adapter);
//                                } catch (Exception e) {
//                                    APPUtils.showToast(activity, "数据还未加载完成");
//                                }
//
//                                break;
//                            case 3:
//                                spId2 = parentId;
//                                break;
//                        }
//
//                    }
//                });
//
//            }
//        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String select = parent.getSelectedItem().toString();
        switch (parent.getId()) {
            case R.id.sp1:
                loadSelectSpData(sampleSources1, select, 1);
                break;

            case R.id.sp2:
                loadSelectSpData(sampleSources2, select, 2);
                break;

            case R.id.sp3:
                loadSelectSpData(sampleSources3, select, 3);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
    实现样品类型二级联动
     */
    private void loadSelectSampleTypeData(List<SampleTypeData> dataList, String selectValue, int level) {

        //如果到第三级了就直接返回不再联动
        if (level > 2) {
            return;
        }
        level++;

        for (SampleTypeData data : dataList) {
            if (data.getSampleName().equals(selectValue)) {
                String sampleId = data.getSampleId();
                initSampleTypes(sampleId, level);
            }
        }

    }


    private String AreaId;

    private void initSp(String cityId, int level) {

        // 获取请求体数据
        AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;

        if ("信达安检测".equals(OperatorId)) {
            OperatorId = "TangshanNM";
        }

        String str_level = level + "";

        map.clear();//清空数据后设置请求值


        map.put("AreaId", AreaId);
        map.put("OperatorId", OperatorId);
        map.put("ParentId", cityId);
        map.put("LevelCode", str_level);

        // 获取本地存储的地址信息

        // 原始的api /System/GetUnderCity
        // 多参数 CommonManual/GetSamplingSourceList
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "CommonManual/GetSamplingSourceList", map, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String body = response;
                //解析数据
                SampleSourceJsonRootBean sampleSourceJsonRootBean = new Gson().fromJson(body, SampleSourceJsonRootBean.class);

                //为Sp设置数据
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (level) {
                            case 1:

                                sampleSources1 = sampleSourceJsonRootBean.getData();
                                String[] array = new String[sampleSources1.size()];
                                for (int i = 0; i < sampleSources1.size(); i++) {
                                    array[i] = sampleSources1.get(i).getSourceName();
                                }
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
                                ArrayAdapter<String> adapter;
                                try {
                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
                                    sp1.setAdapter(adapter);
                                    for (int j = 0; j < array.length; j++) {
                                        if (array[j].equals("测试学校")) {
                                            sp1.setSelection(j, true);
                                        }
                                    }
                                } catch (Exception e) {
                                    APPUtils.showToast(activity, "数据还未加载完成");
                                }

                                //样品类型
                                for (SampleTypeData sampleTypeData : sampletyelist1) {
                                    if (PesticideTestActivity2.sampleId == null) {
                                        PesticideTestActivity2.sampleId = sampleTypeData.getSampleId();
                                    }
                                }

                                break;
                            case 2:

                                spId1 = cityId;
                                sampleSources2 = sampleSourceJsonRootBean.getData();
                                array = new String[sampleSources2.size()];
                                for (int i = 0; i < sampleSources2.size(); i++) {
                                    array[i] = sampleSources2.get(i).getSourceName();
                                }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
                                try {
                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
                                    sp2.setAdapter(adapter);
                                } catch (Exception e) {
                                    APPUtils.showToast(activity, "数据还未加载完成");
                                }

                                break;
                            case 3:
                                spId2 = cityId;
                                sampleSources3 = sampleSourceJsonRootBean.getData();
                                array = new String[sampleSources3.size()];
                                for (int i = 0; i < sampleSources3.size(); i++) {
                                    array[i] = sampleSources3.get(i).getSourceName();
                                }
//                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
                                try {
                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
                                    sp3.setAdapter(adapter);
                                } catch (Exception e) {
                                    APPUtils.showToast(activity, "数据还未加载完成");
                                }

                                break;
                            case 4:
                                spId3 = cityId;
                                break;
                        }

                    }
                });
            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
        //网络请求
//        new GT.OkGo(InterfaceURL.BASE_URL + "CommonManual/GetSamplingSourceList", map).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                String body = response.body();
//                //解析数据
//                SampleSourceJsonRootBean sampleSourceJsonRootBean = new Gson().fromJson(body, SampleSourceJsonRootBean.class);
//
//                //为Sp设置数据
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        switch (level) {
//                            case 1:
//
//                                sampleSources1 = sampleSourceJsonRootBean.getData();
//                                String[] array = new String[sampleSources1.size()];
//                                for (int i = 0; i < sampleSources1.size(); i++) {
//                                    array[i] = sampleSources1.get(i).getSourceName();
//                                }
////                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
//                                ArrayAdapter<String> adapter;
//                                try {
//                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
//                                    sp1.setAdapter(adapter);
//                                    for (int j = 0; j < array.length; j++) {
//                                        if (array[j].equals("测试学校")) {
//                                            sp1.setSelection(j, true);
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    APPUtils.showToast(activity, "数据还未加载完成");
//                                }
//
//                                //样品类型
//                                for (SampleTypeData sampleTypeData : sampletyelist1) {
//                                    if (PesticideTestActivity2.sampleId == null) {
//                                        PesticideTestActivity2.sampleId = sampleTypeData.getSampleId();
//                                    }
//                                }
//
//                                break;
//                            case 2:
//
//                                spId1 = cityId;
//                                sampleSources2 = sampleSourceJsonRootBean.getData();
//                                array = new String[sampleSources2.size()];
//                                for (int i = 0; i < sampleSources2.size(); i++) {
//                                    array[i] = sampleSources2.get(i).getSourceName();
//                                }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
//                                try {
//                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
//                                    sp2.setAdapter(adapter);
//                                } catch (Exception e) {
//                                    APPUtils.showToast(activity, "数据还未加载完成");
//                                }
//
//                                break;
//                            case 3:
//                                spId2 = cityId;
//                                sampleSources3 = sampleSourceJsonRootBean.getData();
//                                array = new String[sampleSources3.size()];
//                                for (int i = 0; i < sampleSources3.size(); i++) {
//                                    array[i] = sampleSources3.get(i).getSourceName();
//                                }
////                                adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
//                                try {
//                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
//                                    sp3.setAdapter(adapter);
//                                } catch (Exception e) {
//                                    APPUtils.showToast(activity, "数据还未加载完成");
//                                }
//
//                                break;
//                            case 4:
//                                spId3 = cityId;
//                                break;
//                        }
//
//                    }
//                });
//
//            }
//        });

    }

    /**
     * 实现三级联动的请求
     */
    private void loadSelectSpData(List<SampleSourceData> dataList, String selectValue, int level) {

        //如果到第三级了就直接返回不再联动
        if (level > 3) {
            return;
        }
        level++;

        for (SampleSourceData data : dataList) {
            if (data.getSourceName().equals(selectValue)) {
                String cityId = data.getSourceId();
                initSp(cityId, level);
            }
        }

    }

    public interface GetData {
        public void getdata(Bundle bundle);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1001 && resultCode == 502) {
                String companyName = data.getStringExtra("companyName");
                String companyNum = data.getStringExtra("companyNum");
                et_company_code.setText(companyNum);
                et_input_bjdw_add.setText(companyName);
            }
        }

    }

    //接收添加企业传过来的消息
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEventMainThread(AddComBean addComBean) {
        Log.i("zdl", "eventbus拿到的值:" + addComBean.companyName);
        et_company_code.setText(addComBean.companyNum);
        et_input_bjdw_add.setText(addComBean.companyName);
        //每次用完需要移除
        EventBus.getDefault().removeStickyEvent(addComBean);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
