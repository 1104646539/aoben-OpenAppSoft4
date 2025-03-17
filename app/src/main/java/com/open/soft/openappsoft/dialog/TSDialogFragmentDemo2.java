package com.open.soft.openappsoft.dialog;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.utils.http.Global;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.kongzue.baseokhttp.util.JsonList;
import com.kongzue.baseokhttp.util.JsonMap;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.lzy.okgo.callback.StringCallback;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.SearchActivity;
import com.open.soft.openappsoft.bean.AddComBean;
import com.open.soft.openappsoft.jinbiao.activity.CheckPaintActivity;
import com.open.soft.openappsoft.jinbiao.model.NewSampleNameData;
import com.open.soft.openappsoft.jinbiao.model.SampleSourceData;
import com.open.soft.openappsoft.jinbiao.model.SampleSourceJsonRootBean;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeData;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeJsonRootBean;
import com.open.soft.openappsoft.jinbiao.util.APPUtils;
import com.open.soft.openappsoft.jinbiao.util.DialogUitl;
import com.open.soft.openappsoft.multifuction.activity.PesticideTestActivity2;
import com.open.soft.openappsoft.util.InterfaceURL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


@GT.Annotations.GT_AnnotationDialogFragment(R.layout.fragment_t_s_dialog_demo2)
public class TSDialogFragmentDemo2 extends GT.GT_Dialog.BaseDialogFragment implements AdapterView.OnItemSelectedListener {
    /**
     * 后续修改添加下拉框  测试企业
     */
    private JsonList jsType = new JsonList(), jsSubType = new JsonList();
    private int jsTypePosition, jsSubTypePosition;
    private String jsTypeId, jsSubTypeId, jsSubProductId;


    @GT.Annotations.GT_View(R.id.tv_type_sam)//样品类型
    private TextView tv_type_sam;
    @GT.Annotations.GT_View(R.id.tv_name_sample)//样品类型
    private TextView et_input_ypmc;
    @GT.Annotations.GT_View(R.id.et_input_ypbh)//样品编号
    private EditText et_input_ypbh;
    @GT.Annotations.GT_View(R.id.et_input_bjdw)//备检单位
    private EditText et_input_bjdw;
    @GT.Annotations.GT_View(R.id.iv_ts_search)//搜索
    private ImageView ivTsSearch;
    @GT.Annotations.GT_View(R.id.et_companyCode)//组织机构
    private TextView et_company_code;
    @GT.Annotations.GT_View(R.id.ll_company)//组织机构是否显示
    private LinearLayout llCompany;
    @GT.Annotations.GT_View(R.id.et_input_bjdw_add)//备检单位不填写
    private TextView et_input_bjdw_add;
    private String needCompanyCode;

    //    @GT.Annotations.GT_View(R.id.sp_yplx)
//    private Spinner sp_yplx;
//    @GT.Annotations.GT_View(R.id.sp_yplx1)
//    private Spinner sp_yplx1;
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

    private Activity activity;


    private String samplesource = "";
    private String samplename = "";
    private String sampleid = "";
    private String sampletype = "";
    private String samplebcheckedOrganization = "";
    private String yplbId = ""; //样品类别Id
    private String companyCode = "";//组织机构
    private int aRequest = 0;//判断数据请求几次

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public static TSDialogFragmentDemo2 newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }

        TSDialogFragmentDemo2 fragment = new TSDialogFragmentDemo2();
        fragment.setArguments(bundle);


        return fragment;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        build(this, view);//绑定  DialogFragment

        Bundle arguments = getArguments();

        setClickExternalNoHideDialog();//点击外部不会消息
        // 限制用户输入长度
        et_input_ypmc.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        et_input_ypbh.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        et_input_bjdw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        initSampleTypes("", 1);

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
                intent.putExtra("ts", "tsdemo");
                startActivityForResult(intent, 1001);
            }
        });

        //初始化
        sp1.setOnItemSelectedListener(this);
        sp2.setOnItemSelectedListener(this);
        sp3.setOnItemSelectedListener(this);
        List<String> listValue = new ArrayList();
        //初始化第一个下拉组件数据
        initSp("0001", 1);
        tv_type_sam.setOnClickListener(view1 -> {
            if (jsType.size() > 0) {
                Log.i("lcy", "initView: --------走大--");
                listValue.clear();
                for (int i = 0; i < jsType.size(); i++) {
                    listValue.add(jsType.getJsonMap(i).getString("sampleName"));
                }
                DialogUitl.TsListDialog(view1, listValue, position -> {
                    jsTypePosition = position;
                    tv_type_sam.setText(jsType.getJsonMap(jsTypePosition).getString("sampleName"));
                    jsTypeId = jsType.getJsonMap(jsTypePosition).getString("sampleId");

                    //为PesticideTestActivity2设置总分类id
                    if (PesticideTestActivity2.sampleId == null) {
                        PesticideTestActivity2.sampleId = jsTypeId;
                    }

                    Log.i("lcy", "onClick: ----jsTypeId-----" + jsTypeId);
                    if (jsType.size() > 0) {
                        // 样品名称下拉框初始化
                        initSampleNameSpinner();
                    }
                    et_input_ypmc.setText("请选择");
                });
            } else {
                if (aRequest < 3) {
                    initSampleTypes("", 1);
                    Toast.makeText(activity, "数据重新请求中", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "数据请求失败", Toast.LENGTH_SHORT).show();
                }
                Log.i("lcy", "initView: --------走小--");
            }

        });
        et_input_ypmc.setOnClickListener(view12 -> {
            if (tv_type_sam.getText().equals("请选择")) {
                Toast.makeText(activity, "请先选择样品类型", Toast.LENGTH_SHORT).show();
            } else {
                List<String> list = new ArrayList();
                for (int i = 0; i < jsSubType.size(); i++) {
                    list.add(jsSubType.getJsonMap(i).getString("objectName"));
                }
                DialogUitl.TsListDialog(view12, list, position -> {
                    jsSubTypePosition = position;
                    et_input_ypmc.setText(jsSubType.getJsonMap(jsSubTypePosition).getString("objectName"));
                    jsSubTypeId = jsSubType.getJsonMap(jsSubTypePosition).getString("objectId");
                    jsSubProductId = jsSubType.getJsonMap(jsSubTypePosition).getString("productId");
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("lcy", "onResume: ---onResume------");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("lcy", "onResume: ---onPause------");
    }


    @GT.Annotations.GT_Click({R.id.btn_confirm, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:

                if ("".equals(et_input_ypmc.getText().toString()) || et_input_ypmc.getText().toString() == "请选择") {
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
                    sampletype = tv_type_sam.getText().toString();
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
                    //被检单位判断填写
                    if (needCompanyCode.equals("0")) {
                        samplebcheckedOrganization = et_input_bjdw.getText().toString();

                    } else {
                        samplebcheckedOrganization = et_input_bjdw_add.getText().toString();
                    }

                    companyCode = et_company_code.getText().toString();

                    Intent intent = new Intent();
                    intent.putExtra("samplename", samplename);//样品名称
                    intent.putExtra("sampleid", sampleid);//样品编号
                    intent.putExtra("sampletype", sampletype);//样品类别
                    intent.putExtra("samplesource", samplesource);//样品来源省市区拼接
                    intent.putExtra("samplebcheckedOrganization", samplebcheckedOrganization);//被检单位
                    intent.putExtra("yplbId", jsSubTypeId);//样品类别id
                    intent.putExtra("jsSubProductId", jsSubProductId);//样品类别productid
                    intent.putExtra("spId1", spId1);//省
                    intent.putExtra("spId2", spId2);//市
                    intent.putExtra("spId3", spId3);//区
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

        Timber.i("监听到返回");

        return true;
    }

    private void initSampleTypes(String parentId, int level) {
        aRequest++;
        // 获取参数
        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;
        map1.clear();
        map1.put("AreaId", AreaId);
        map1.put("OperatorId", OperatorId);
        map1.put("ParentId", parentId);
//        EditURLDialog editURLDialog = new EditURLDialog(getContext());

        WaitDialog.show("数据加载中");
        //网络请求
        //样品种类
//        new GT.OkGo(InterfaceURL.BASE_URL + "CommonManual/GetSamplingTypeList", map1).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                WaitDialog.dismiss();
//                String body = response.body();
//                //解析数据
//                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);
//                if ("0".equals(sampleTypeJsonRootBean.getErrCode())) {
//                    JsonMap data = JsonMap.parse(response.body());
//                    jsType = data.getList("Data");
//                    WaitDialog.dismiss();
//                } else {
//                    WaitDialog.dismiss();
//                    String data = sampleTypeJsonRootBean.getErrMsg();
//                    APPUtils.showToast(activity,data);
//                }
//
//            }
//        });

        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "CommonManual/GetSamplingTypeList", map1, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                WaitDialog.dismiss();
                String body = response;
                //解析数据
                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);
                if ("0".equals(sampleTypeJsonRootBean.getErrCode())) {
                    JsonMap data = JsonMap.parse(response);
                    jsType = data.getList("Data");
                    WaitDialog.dismiss();
                } else {
                    WaitDialog.dismiss();
                    String data = sampleTypeJsonRootBean.getErrMsg();
                    APPUtils.showToast(activity, data);
                }

            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
    }

    @GT.Annotations.GT_Collection.GT_Map
    private Map<String, String> map2;
    private List<NewSampleNameData> NewSampleNameData_List;

    private void initSampleNameSpinner() {

        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;
        map2.clear();
        map2.put("AreaId", AreaId);
        map2.put("OperatorId", OperatorId);
        NewSampleNameData_List = new ArrayList<>();
        map2.put("ProductType", jsTypeId);
        // http://www.shionda.com:7011/api//CommonManual/GetProductList
//        new GT.OkGo(InterfaceURL.BASE_URL + "/CommonManual/GetProductList", map2).loadDataPost(new StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                JsonMap data = JsonMap.parse(response.body());
//                if (data.getString("ErrCode").equals("0")) {
//                    jsSubType = data.getList("Data");
//                    Log.i("lcy", "onSuccess: ------" + jsSubType);
//                } else {
//                    APPUtils.showToast(activity, data.getString("ErrMsg"));
//                }
//            }
//        });


        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "/CommonManual/GetProductList", map2, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                JsonMap data = JsonMap.parse(response);
                if (data.getString("ErrCode").equals("0")) {
                    jsSubType = data.getList("Data");
                    Log.i("lcy", "onSuccess: ------" + jsSubType);
                } else {
                    APPUtils.showToast(activity, data.getString("ErrMsg"));
                }
            }

            @Override
            public void onError(String response, Object o) {

            }
        }, false);
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
//
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
//                    }
//                });
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
        String companyName = addComBean.companyName;
        String companyNum = addComBean.companyNum;
        et_company_code.setText(companyNum);
        et_input_bjdw_add.setText(companyName);
        //每次用完需要移除
        EventBus.getDefault().removeStickyEvent(addComBean);
        Log.i("lcy", "onEventMainThread: ---TSDialogFragmentDemo2拿到了值--" + addComBean.companyName);
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
