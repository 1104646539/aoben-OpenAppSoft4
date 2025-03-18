
package com.open.soft.openappsoft.activity.orderinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.util.APPUtils;

import java.util.ArrayList;
import java.util.List;

public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener, OrderInfoAdapter.OnLongClick {
    OrderInfoAdapter adapter;
    TextView tv_title, tv_add;
    RecyclerView rv_data;
    View ll_root;
    List<OrderInfoModel> orderInfoModels = new ArrayList<>();

    int type = OrderInfoModel.type_bcheck;

    GT.Hibernate hibernate;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.WindowUtils.hideActionBar(this);
        setContentView(R.layout.activity_edit_info);
        hibernate = MainActivity.hibernate;
        type = getIntent().getIntExtra("type", OrderInfoModel.type_bcheck);
        initView();
        setTypeInfo();
        loadData();
    }

    private void setTypeInfo() {
        if (type == OrderInfoModel.type_bcheck) {
            tv_title.setText("受检单位管理");
        } else if (type == OrderInfoModel.type_sample_type_child) {
            tv_title.setText("样本子类管理");
        } else if (type == OrderInfoModel.type_sample_type_main) {
            tv_title.setText("样本主类管理");
        }
    }

    private void loadData() {
        List<OrderInfoModel> temp = hibernate.where("type = ?", "" + type).queryAll(OrderInfoModel.class);
        orderInfoModels.clear();
        orderInfoModels.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    private void addOrSave(boolean add, OrderInfoModel orderInfoModel) {
        String hilt = add ? "插入" : "更新";
        if (add) {
            hibernate.save(orderInfoModel);
        } else {
            hibernate.update(orderInfoModel);
        }
        if (hibernate.isStatus()) {
            loadData();
            adapter.notifyDataSetChanged();
            APPUtils.showToast(this, hilt + "成功");
        } else {
            APPUtils.showToast(this, hilt + "失败");
        }
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_add = findViewById(R.id.tv_add);
        rv_data = findViewById(R.id.rv_data);
        ll_root = findViewById(R.id.ll_root);
        tv_add.setOnClickListener(this);

        adapter = new OrderInfoAdapter(orderInfoModels);
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
                showInfoDialog(EditOrderInfoDialog.ShowMode_details, orderInfoModels.get(longPosition));
                break;
            case R.id.inspected_long_modify:
                showInfoDialog(EditOrderInfoDialog.ShowMode_change, orderInfoModels.get(longPosition));
                break;
            case R.id.inspected_long_delete:
                delete();
                break;
        }
    }

    private void changeDetailsView() {
        popupWindow_long.dismiss();
    }

    private void delete() {
        if (longPosition >= 0 && longPosition < orderInfoModels.size()) {
            OrderInfoModel model = orderInfoModels.get(longPosition);
            hibernate.where("id = ?", "" + model.id).delete(OrderInfoModel.class);
            loadData();
            APPUtils.showToast(this, "删除成功");
        }
        popupWindow_long.dismiss();
    }

    private void showDetailsView() {
        popupWindow_long.dismiss();
    }

    private void showInfoDialog(int showMode, OrderInfoModel temp) {
        EditOrderInfoDialog orderInfoDialog = new EditOrderInfoDialog(this);
        orderInfoDialog.showDialog(showMode, type, temp, new EditOrderInfoDialog.OnCommit() {
            @Override
            public void OnCommit(boolean add, OrderInfoModel model) {
                if (type == OrderInfoModel.type_bcheck) {
                    if (APPUtils.isNull(model.name)) {
                        APPUtils.showToast(EditInfoActivity.this, "请输入受检单位");

                    } else if (APPUtils.isNull(model.code)) {
                        APPUtils.showToast(EditInfoActivity.this, "请输入受检单位代码");

                    } else {
                        orderInfoDialog.dismiss();
                        addOrSave(showMode == EditOrderInfoDialog.ShowMode_add, temp);
                    }
                } else if (type == OrderInfoModel.type_sample_type_child) {
                    if (APPUtils.isNull(model.name)) {
                        APPUtils.showToast(EditInfoActivity.this, "请输入样品子类");

                    } else {
                        orderInfoDialog.dismiss();
                        addOrSave(showMode == EditOrderInfoDialog.ShowMode_add, temp);
                    }
                } else if (type == OrderInfoModel.type_sample_type_main) {
                    if (APPUtils.isNull(model.name)) {
                        APPUtils.showToast(EditInfoActivity.this, "请输入样品主类");

                    } else {
                        orderInfoDialog.dismiss();
                        popupWindow_long.dismiss();
                        addOrSave(showMode == EditOrderInfoDialog.ShowMode_add, temp);
                    }
                }
            }

            @Override
            public void OnCancel() {
                orderInfoDialog.dismiss();
                popupWindow_long.dismiss();
            }
        });
    }

    @Override
    public void OnLongClick(int position) {
        showLongPop(position);
    }
}