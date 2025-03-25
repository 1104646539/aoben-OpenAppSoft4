package com.open.soft.openappsoft.activity.samplename;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoModel;
import com.open.soft.openappsoft.multifuction.model.SampleName;

public class EditSampleNameDialog extends Dialog {
    Context context;
    View view;

    TextView tv_title, tv_label_name, tv_label_id;
    EditText et_name, et_id;
    TextView tv_add, tv_cancel;
    LinearLayout ll_bottom, ll_id;
    /**
     * 0添加 1修改 2显示
     */
    int showMode = 0;
    public static int ShowMode_add = 0;
    public static int ShowMode_change = 1;
    public static int ShowMode_details = 2;

    SampleName model;

    public EditSampleNameDialog(@NonNull Context context) {
        this(context, 0);
    }

    public EditSampleNameDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_order_info, null, false);
        tv_title = view.findViewById(R.id.tv_title);
        tv_label_name = view.findViewById(R.id.tv_label_name);
        tv_label_id = view.findViewById(R.id.tv_label_id);
        et_name = view.findViewById(R.id.et_name);
        et_id = view.findViewById(R.id.et_id);
        tv_add = view.findViewById(R.id.tv_add);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        ll_bottom = view.findViewById(R.id.ll_bottom);
        ll_id = view.findViewById(R.id.ll_id);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public void showDialog(int showMode, SampleName temp, OnCommit onAdd) {
        if (isShowing()) {
            dismiss();
            return;
        }
        this.model = temp;
        this.showMode = showMode;
        String hilt = "";
        if (showMode == ShowMode_add) {
            hilt = "添加";
            tv_add.setText("添加");
        } else if (showMode == ShowMode_change) {
            ll_id.setVisibility(View.GONE);
            hilt = "修改";
            tv_add.setText("修改");
            et_name.setText(temp.sampleName);
        } else if (showMode == ShowMode_details) {
            hilt = "详情";
            tv_add.setVisibility(View.GONE);
            et_name.setText(temp.sampleName);
            et_name.setEnabled(false);
            et_id.setEnabled(false);
        }

        ll_id.setVisibility(View.GONE);
        tv_title.setText("样品名称" + hilt);
        tv_label_name.setText("样品名称");

        tv_cancel.setOnClickListener(view -> {
            if (onAdd != null) {
                onAdd.OnCancel();
            }
        });
        tv_add.setOnClickListener(view -> {
            if (this.model == null) {
                this.model = new SampleName();
            }
            model.sampleName = et_name.getText().toString();
            onAdd.OnCommit(showMode == ShowMode_add, model);
        });
        setContentView(view);
        show();
    }

    public interface OnCommit {
        void OnCommit(boolean add, SampleName model);

        void OnCancel();

    }
}
