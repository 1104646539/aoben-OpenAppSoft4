package com.open.soft.openappsoft.multifuction.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.lzy.okgo.callback.StringCallback;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeData;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeJsonRootBean;
import com.open.soft.openappsoft.util.InterfaceURL;

import java.util.List;
import java.util.Map;

public class InputMessageDialog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    // 样品类型相关初始化
    @GT.Annotations.GT_View(R.id.sp_yplx_normal)
    private Spinner sp_yplx_normal;
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

    //省市区的ID
    private String spId1, spId2, spId3;


    private String[] sp_yplxArray;//样品类别字符串
    private String[] sp_yplxArrayId;//样品类别ID

    private Button btn_confirm;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_message);


        // 初始化样品类型下拉组件
        sp_yplx.setOnItemSelectedListener(this);
        sp_yplx1.setOnItemSelectedListener(this);
        initSampleTypes("", 1);


        sp_yplx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = parent.getSelectedItem().toString();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void initSampleTypes(String parentId, int level) {

        // 获取参数
        String AreaId = com.example.utils.http.Global.admin_pt;
        String OperatorId = com.example.utils.http.Global.ID;

        map1.clear();
        map1.put("AreaId", AreaId);
        map1.put("OperatorId", OperatorId);
        map1.put("ParentId", parentId);

//        EditURLDialog editURLDialog = new EditURLDialog(InputMessageDialog.this);
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "CommonManual/GetSamplingTypeList", map1, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String body = response;
                //解析数据
                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);

                //为Sp设置数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (level) {
                            case 1:

                                sampletyelist1 = sampleTypeJsonRootBean.getData();
                                String[] array = new String[sampletyelist1.size()];
                                for (int i = 0; i < sampletyelist1.size(); i++) {
                                    array[i] = sampletyelist1.get(i).getSampleName();
                                }
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(InputMessageDialog.this, R.layout.simple_list_item_choice_shoudong, array);
                                sp_yplx.setAdapter(adapter);
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
                                adapter = new ArrayAdapter<String>(InputMessageDialog.this, R.layout.simple_list_item_choice_shoudong, array);
                                sp_yplx1.setAdapter(adapter);
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
//                String body = response.body();
//                //解析数据
//                SampleTypeJsonRootBean sampleTypeJsonRootBean = new Gson().fromJson(body, SampleTypeJsonRootBean.class);
//
//                //为Sp设置数据
//                runOnUiThread(new Runnable() {
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
////                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckActivityByMen.this, android.R.layout.simple_list_item_multiple_choice, array);
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(InputMessageDialog.this, R.layout.simple_list_item_choice_shoudong, array);
//                                sp_yplx.setAdapter(adapter);
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
//                                adapter = new ArrayAdapter<String>(InputMessageDialog.this, R.layout.simple_list_item_choice_shoudong, array);
//                                sp_yplx1.setAdapter(adapter);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
