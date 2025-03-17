package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utils.http.AreaResultBean;
import com.example.utils.http.KnowledgeResultBean;
import com.example.utils.http.LoginResultBean;
import com.example.utils.http.OrderPresenter;
import com.example.utils.http.Result;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.data2.Data;
import com.open.soft.openappsoft.data2.JsonRootBean;
import com.open.soft.openappsoft.util.APPUtils;
import com.open.soft.openappsoft.util.InterfaceURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class knowledgeActivity extends Activity {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    //    List<KnowledgeResultBean> knowledgeResultBeans = new ArrayList<>();
    List<Data> knowledgeResultBeans = new ArrayList<>();
    KnowledgeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在读取知识库....");
        progressDialog.show();

        adapter = new KnowledgeAdapter(this, knowledgeResultBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        loadData();

    }


    public void loadData() {

        Map<String, String> map = new HashMap<>();

        if ("多参数食品安全检测仪".equals(InterfaceURL.oneModule)) {
            map.put("type", "M417");
        } else if ("农药残留检测仪".equals(InterfaceURL.oneModule)) {
            map.put("type", "MC4011");
        } else if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {
            map.put("type", "NAD4074");
        }
        // 从本地获取网址
//        EditURLDialog editURLDialog = new EditURLDialog(knowledgeActivity.this);
        new GT.HttpUtil().postRequest(InterfaceURL.BASE_URL + "/Other/GetKnowledge", map, new GT.HttpUtil.OnLoadDataListener() {
            @Override
            public void onSuccess(String response, Object o) {
                String string = response;

                JsonRootBean jsonRootBean = new Gson().fromJson(string, JsonRootBean.class);
                if (jsonRootBean != null) {
                    List<Data> data = jsonRootBean.getData();
                    knowledgeResultBeans.clear();
                    knowledgeResultBeans.addAll(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    });
                }


            }

            @Override
            public void onError(String response, Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        APPUtils.showToast(knowledgeActivity.this, "查找失败");
                    }
                });
            }
        }, false);
//
//        new GT.OkHttp(InterfaceURL.BASE_URL+"/Other/GetKnowledge",map).loadData(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String string = response.body().string();
//
//                JsonRootBean jsonRootBean = new Gson().fromJson(string, JsonRootBean.class);
//                if(jsonRootBean != null){
//                    List<Data> data = jsonRootBean.getData();
//                    knowledgeResultBeans.clear();
//                    knowledgeResultBeans.addAll(data);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.notifyDataSetChanged();
//                            progressDialog.dismiss();
//                        }
//                    });
//                }
//
//
//            }
//        });


    }


}
