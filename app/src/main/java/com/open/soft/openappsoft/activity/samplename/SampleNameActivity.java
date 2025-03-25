
package com.open.soft.openappsoft.activity.samplename;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsls.gt.GT;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.activity.orderinfo.EditOrderInfoDialog;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoAdapter;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoModel;
import com.open.soft.openappsoft.multifuction.db.DbHelper;
import com.open.soft.openappsoft.multifuction.model.SampleName;
import com.open.soft.openappsoft.util.APPUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class SampleNameActivity extends AppCompatActivity implements View.OnClickListener, SampleNameAdapter.OnLongClick {
    SampleNameAdapter adapter;
    TextView tv_title, tv_add;
    RecyclerView rv_data;
    View ll_root;
    List<SampleName> sampleNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.WindowUtils.hideActionBar(this);
        setContentView(R.layout.activity_edit_sample_name);
        initView();
        setTypeInfo();
        loadData();
    }

    private void setTypeInfo() {
        tv_title.setText("样品名称管理");
    }

    private void loadData() {

        List<SampleName> temp = null;
        try {
            temp = DbHelper.GetInstance().findAll(Selector.from(SampleName.class)
                    .orderBy("time", true));
            sampleNames.clear();
            if (temp != null) {
                sampleNames.addAll(temp);
            }
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            Timber.d("加载失败");
            APPUtils.showToast(this, "加载失败");
        }
    }

    private void addOrSave(boolean add, SampleName sampleName) {
        String hilt = add ? "插入" : "更新";
        try {
            if (add) {
                DbHelper.GetInstance().save(sampleName);
            } else {
                DbHelper.GetInstance().update(sampleName);
            }
        } catch (DbException e) {
            APPUtils.showToast(this, hilt + "失败");
            return;
        }
        loadData();
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_add = findViewById(R.id.tv_add);
        rv_data = findViewById(R.id.rv_data);
        ll_root = findViewById(R.id.ll_root);
        tv_add.setOnClickListener(this);

        adapter = new SampleNameAdapter(sampleNames);
        rv_data.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        rv_data.setAdapter(adapter);
        adapter.setOnLongClick(this);
    }

    PopupWindow popupWindow_long;
    int longPosition;

    private void showLongPop(int position) {
        // TODO Auto-generated method stub
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (popupWindow_long == null) {
            View contentView = LayoutInflater.from(
                    this).inflate(
                    R.layout.activity_long_pop, null);
            initPopLongView(contentView);
            int width = (int) getApplicationContext().getResources()
                    .getDimension(R.dimen.activity_main_pop_width);
            int hight = (int) getApplicationContext().getResources()
                    .getDimension(R.dimen.activity_main_pop_hight);

            popupWindow_long = new PopupWindow(contentView, width, hight);
        }
        longPosition = position;
        // 使其聚集
        popupWindow_long.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow_long.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow_long.setBackgroundDrawable(new BitmapDrawable());

        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow_long.getWidth() / 2;
        int yPos = windowManager.getDefaultDisplay().getHeight() / 2
                - popupWindow_long.getHeight() / 2;
        popupWindow_long.showAsDropDown(tv_title, xPos, yPos);

    }

    private TextView inspected_long_show;
    private TextView inspected_long_modify;
    private TextView inspected_long_delete;

    private void initPopLongView(View contentView) {
        // TODO Auto-generated method stub
        inspected_long_show = (TextView) contentView
                .findViewById(R.id.inspected_long_show);
        inspected_long_show.setOnClickListener(this);
        inspected_long_modify = (TextView) contentView
                .findViewById(R.id.inspected_long_modify);
        inspected_long_modify.setOnClickListener(this);
        inspected_long_delete = (TextView) contentView
                .findViewById(R.id.inspected_long_delete);
        inspected_long_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                showInfoDialog(EditOrderInfoDialog.ShowMode_add, null);
                break;
            case R.id.inspected_long_show:
                showInfoDialog(EditOrderInfoDialog.ShowMode_details, sampleNames.get(longPosition));
                break;
            case R.id.inspected_long_modify:
                showInfoDialog(EditOrderInfoDialog.ShowMode_change, sampleNames.get(longPosition));
                break;
            case R.id.inspected_long_delete:
                delete();
                break;
        }
    }


    private void delete() {
        if (longPosition >= 0 && longPosition < sampleNames.size()) {
            SampleName model = sampleNames.get(longPosition);
            try {
                DbHelper.GetInstance().delete(model);
            } catch (DbException e) {
                Timber.d("删除失败");
                popupWindow_long.dismiss();
                APPUtils.showToast(this, "删除失败");
                return;
            }
            loadData();
            APPUtils.showToast(this, "删除成功");
        }
        popupWindow_long.dismiss();
    }


    private void showInfoDialog(int showMode, SampleName temp) {
        EditSampleNameDialog editSampleNameDialog = new EditSampleNameDialog(this);
        editSampleNameDialog.showDialog(showMode, temp, new EditSampleNameDialog.OnCommit() {
            @Override
            public void OnCommit(boolean add, SampleName model) {

                if (APPUtils.isNull(model.sampleName)) {
                    APPUtils.showToast(SampleNameActivity.this, "请输入样品名称");

                } else {
                    editSampleNameDialog.dismiss();
                    model.time = new Date().getTime();
                    addOrSave(showMode == EditOrderInfoDialog.ShowMode_add, model);
                }

            }

            @Override
            public void OnCancel() {
                editSampleNameDialog.dismiss();
                if (popupWindow_long != null)
                    popupWindow_long.dismiss();
            }
        });
    }

    @Override
    public void OnLongClick(int position) {
        showLongPop(position);
    }
}
