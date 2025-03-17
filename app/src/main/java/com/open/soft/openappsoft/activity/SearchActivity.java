package com.open.soft.openappsoft.activity;


import static java.util.Objects.isNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utils.http.ToolUtil;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonList;
import com.kongzue.baseokhttp.util.JsonMap;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.open.soft.openappsoft.App;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.adp.SearchAdp;
import com.open.soft.openappsoft.activity.api.HttpDataRequest;
import com.open.soft.openappsoft.dialog.DialogFragmentDemo2;
import com.open.soft.openappsoft.dialog.TSDialogFragmentDemo2;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivityByMen;
import com.open.soft.openappsoft.jinbiao.activity.TSCheckActivity;
import com.open.soft.openappsoft.jinbiao.base.BaseAty;
import com.open.soft.openappsoft.multifuction.util.ToolUtils;
import com.open.soft.openappsoft.util.APPUtils;
import com.open.soft.openappsoft.util.stacktable.StackLabel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseAty {

    private StackLabel stackLabel;
    private ImageView image_black;
    private EditText editText;
    private TextView text_search, no_history_tip;
    private LinearLayout ll_gone_lable;
    private String searchLabe;
    private ImageView image_delete;
    private String tagId;
    private List<String> list;
    private SearchAdp searchAdp;
    private JsonList dataMsg;
    private TextView textAddCompany;

    @Override
    public void initViews() {
        //去除默认标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        list = new ArrayList<>();
        RecyclerView recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        searchAdp = new SearchAdp(R.layout.item_search);
        recycler_view.setAdapter(searchAdp);
        textAddCompany = findViewById(R.id.text_add_company);
        no_history_tip = findViewById(R.id.no_history_tip);
        text_search = findViewById(R.id.text_search);
        editText = findViewById(R.id.editText);
        image_black = findViewById(R.id.image_black);
        stackLabel = findViewById(R.id.stackLabel);
        image_delete = findViewById(R.id.image_delete);
        searchLabe = App.defaultSP.getString("lab", "searchLabe");
        ll_gone_lable = findViewById(R.id.ll_gone_lable);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    public void initDatas() {

        getHttp(editText.getText().toString());
        if (isNull(searchLabe)) {
            no_history_tip.setVisibility(View.VISIBLE);
            ll_gone_lable.setVisibility(View.GONE);
        } else {
            String[] str = searchLabe.split(",");
            list.clear();
            for (int i = 0; i < str.length; i++) {
                list.add(str[i]);
            }
            stackLabel.setLabels(str);
            no_history_tip.setVisibility(View.GONE);
            ll_gone_lable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEvents() {
        searchAdp.setOnItemClickListener((adapter, view, position) -> {

            Intent in = getIntent();
            String ts = in.getStringExtra("ts");
            Intent intent;
            if (ts.equals("tsdemo")) {
                intent = new Intent(this, TSDialogFragmentDemo2.class);
            } else if (ts.equals("TSCheckActivity")) {
                intent = new Intent(this, TSCheckActivity.class);
            } else if (ts.equals("CheckActivityByMen")) {
                intent = new Intent(this, CheckActivityByMen.class);
            } else {
                intent = new Intent(this, DialogFragmentDemo2.class);
            }
            intent.putExtra("companyName", dataMsg.getJsonMap(position).getString("Key"));
            intent.putExtra("companyNum", dataMsg.getJsonMap(position).getString("Value"));
            //这个值对应
            setResult(502, intent);

            finish();
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("zdl", "==beforeTextChanged====");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("zdl", "===onTextChanged===");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("zdl", "===afterTextChanged===");
                ll_gone_lable.setVisibility(View.VISIBLE);
            }
        });

        text_search.setOnClickListener(view -> {
//            if (editText.getText().toString().trim() == null || editText.getText().toString().trim().equals("")) {
//                toast("搜索框不能为空");
//                return;
//            }
            if (isNull(searchLabe)) {
                App.defaultSP.edit().putString("lab", editText.getText().toString()).commit();
            } else {
                boolean listSwitch = true;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(editText.getText().toString().trim())) {
                        listSwitch = false;
                    }
                }
                if (listSwitch) {
                    App.defaultSP.edit().putString("lab", searchLabe + "," + editText.getText().toString()).commit();
                }
            }
            getHttp(editText.getText().toString());
        });
        stackLabel.setOnLabelClickListener((index, v, s) -> getHttp(s));
        image_black.setOnClickListener(v -> {
            finish();
        });
        image_delete.setOnClickListener(view -> {
            MessageDialog.show("确认删除全部历史记录？",
                    "",
                    "确定",
                    "取消"
            ).setOkButton((baseDialog, v) -> {
                App.defaultSP.edit().putString("lab", "");
                APPUtils.showToast(this, "清空成功");
                searchLabe = App.defaultSP.getString("lab", "searchLabe");
                no_history_tip.setVisibility(View.VISIBLE);
                ll_gone_lable.setVisibility(View.GONE);
                return false;
            });
        });

        textAddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, AddCompanySearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void getHttp(String name) {

        HttpDataRequest.getInstance().getContext(SearchActivity.this).GetSampleCategoryName(name, new JsonResponseListener() {
            @Override
            public void onResponse(JsonMap main, Exception error) {

                if (main.getString("ErrCode").equals("0")) {
                    JsonList data = main.getList("Data");
                    dataMsg = data;
                    no_history_tip.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
                    no_history_tip.setText(data.size() == 0 ? "未搜索到点击右上角新建企业" : "还没搜索记录");
//                    Log.i("lcy", "onResponse: ----data--------"+data);
                    searchAdp.setNewData(data);
                } else {
                    no_history_tip.setVisibility(View.VISIBLE);
                    no_history_tip.setText(main.getString("ErrMsg"));
                }
            }
        });
    }
}
