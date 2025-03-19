package com.open.soft.openappsoft.activity.task;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.utils.http.Global;
import com.example.utils.http.ToolUtil;
import com.gsls.gt.GT;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoModel;
import com.open.soft.openappsoft.multifuction.adapter.FiltrateAdapter;
import com.open.soft.openappsoft.multifuction.db.DbHelper;
import com.open.soft.openappsoft.multifuction.model.Project;
import com.open.soft.openappsoft.multifuction.model.SampleName;
import com.open.soft.openappsoft.util.APPUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spn_sample_type_main, spn_sample_type_child, spn_bjcdw_name, spn_jcdw_name;
    TextView tv_sampling_date;
    TextView tv_commit, tv_cancel;
    TextView tv_sample_name;
    TextView tv_sample_type_main, tv_sample_type_child, tv_bjcdw_name, tv_jcdw_name;
    EditText et_id;

    List<OrderInfoModel> bjcdws;
    List<OrderInfoModel> jcdws;
    List<OrderInfoModel> sample_type_mains;
    List<OrderInfoModel> sample_type_childs;

    GT.Hibernate hibernate;


    OrderInfoModel jcdw;
    OrderInfoModel bjcdw;
    OrderInfoModel sample_type_main;
    OrderInfoModel sample_type_child;

    Dialog dialog_sample_name;
    List<SampleName> sample_names;
    FiltrateAdapter filtrateAdapter;
    EditText et_content;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GT.WindowUtils.hideActionBar(this);
        setContentView(R.layout.activity_add_task);
        hibernate = MainActivity.hibernate;
        initData();
        initView();

    }

    private void initData() {
        bjcdws = hibernate.where("type = ?", "" + OrderInfoModel.type_bcheck).queryAll(OrderInfoModel.class);
        sample_type_childs = hibernate.where("type = ?", "" + OrderInfoModel.type_sample_type_child).queryAll(OrderInfoModel.class);
        sample_type_mains = hibernate.where("type = ?", "" + OrderInfoModel.type_sample_type_main).queryAll(OrderInfoModel.class);
        jcdws = hibernate.where("type = ?", "" + OrderInfoModel.type_check).queryAll(OrderInfoModel.class);

        if (jcdws == null) {
            jcdws = new ArrayList<>();
        }
        if (bjcdws == null) {
            bjcdws = new ArrayList<>();
        }
        if (sample_type_childs == null) {
            sample_type_childs = new ArrayList<>();
        }
        if (sample_type_mains == null) {
            sample_type_mains = new ArrayList<>();
        }
        try {
            sample_names = DbHelper.GetInstance().findAll(Selector.from(SampleName.class)
                    .orderBy("time", true));
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
    }

    private void initView() {
        spn_sample_type_main = findViewById(R.id.spn_sample_type_main);
        spn_sample_type_child = findViewById(R.id.spn_sample_type_child);
        spn_bjcdw_name = findViewById(R.id.spn_bjcdw_name);
        spn_jcdw_name = findViewById(R.id.spn_jcdw_name);
        tv_sampling_date = findViewById(R.id.tv_sampling_date);
        et_id = findViewById(R.id.et_id);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_commit = findViewById(R.id.tv_commit);
        tv_sample_name = findViewById(R.id.tv_sample_name);
        tv_sample_type_main = findViewById(R.id.tv_sample_type_main);
        tv_sample_type_child = findViewById(R.id.tv_sample_type_child);
        tv_bjcdw_name = findViewById(R.id.tv_bjcdw_name);
        tv_jcdw_name = findViewById(R.id.tv_jcdw_name);

        tv_cancel.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        tv_sample_name.setOnClickListener(this);

        ArrayAdapter<OrderInfoModel> adapter_bjcdw = new ArrayAdapter(this, R.layout.item_select_project, R.id.tv_project_name, bjcdws);
        adapter_bjcdw.setDropDownViewResource(R.layout.item_select_project_drop);
        spn_bjcdw_name.setAdapter(adapter_bjcdw);
        if (!bjcdws.isEmpty()) {
            spn_bjcdw_name.setSelection(bjcdws.size() - 1);
            bjcdw = bjcdws.get(bjcdws.size() - 1);
        }
        spn_bjcdw_name.setOnItemSelectedListener(this);

        ArrayAdapter<OrderInfoModel> adapter_jcdw = new ArrayAdapter(this, R.layout.item_select_project, R.id.tv_project_name, bjcdws);
        adapter_jcdw.setDropDownViewResource(R.layout.item_select_project_drop);
        spn_jcdw_name.setAdapter(adapter_jcdw);
        if (!jcdws.isEmpty()) {
            spn_jcdw_name.setSelection(jcdws.size() - 1);
            jcdw = jcdws.get(jcdws.size() - 1);
        }
        spn_jcdw_name.setOnItemSelectedListener(this);

        ArrayAdapter<OrderInfoModel> adapter_sample_type_child = new ArrayAdapter(this, R.layout.item_select_project, R.id.tv_project_name, sample_type_childs);
        adapter_sample_type_child.setDropDownViewResource(R.layout.item_select_project_drop);
        spn_sample_type_child.setAdapter(adapter_sample_type_child);
        if (!sample_type_childs.isEmpty()) {
            spn_sample_type_child.setSelection(sample_type_childs.size() - 1);
            sample_type_child = sample_type_childs.get(sample_type_childs.size() - 1);
        }
        spn_sample_type_child.setOnItemSelectedListener(this);

        ArrayAdapter<OrderInfoModel> adapter_sample_type_main = new ArrayAdapter(this, R.layout.item_select_project, R.id.tv_project_name, sample_type_mains);
        adapter_sample_type_main.setDropDownViewResource(R.layout.item_select_project_drop);
        spn_sample_type_main.setAdapter(adapter_sample_type_main);
        if (!sample_type_mains.isEmpty()) {
            spn_sample_type_main.setSelection(sample_type_mains.size() - 1);
            sample_type_main = sample_type_mains.get(sample_type_mains.size() - 1);
        }
        spn_sample_type_main.setOnItemSelectedListener(this);

        tv_sampling_date.setOnClickListener(this);

        et_id.setText(getRandomTaskId());

        spn_sample_type_main.setVisibility(sample_type_mains.isEmpty() ? View.GONE : View.VISIBLE);
        tv_sample_type_main.setVisibility(!sample_type_mains.isEmpty() ? View.GONE : View.VISIBLE);

        spn_sample_type_child.setVisibility(sample_type_childs.isEmpty() ? View.GONE : View.VISIBLE);
        tv_sample_type_child.setVisibility(!sample_type_childs.isEmpty() ? View.GONE : View.VISIBLE);

        spn_bjcdw_name.setVisibility(bjcdws.isEmpty() ? View.GONE : View.VISIBLE);
        tv_bjcdw_name.setVisibility(!bjcdws.isEmpty() ? View.GONE : View.VISIBLE);
        spn_jcdw_name.setVisibility(jcdws.isEmpty() ? View.GONE : View.VISIBLE);
        tv_jcdw_name.setVisibility(!jcdws.isEmpty() ? View.GONE : View.VISIBLE);

    }

    private String getRandomTaskId() {
        return ToolUtil.dateToString(new Date(), ToolUtil.DateTime1) + ((int) (new Random().nextFloat() * 1000));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spn_bjcdw_name) {
            bjcdw = bjcdws.get(position);
        } else if (parent.getId() == R.id.spn_sample_type_main) {
            sample_type_main = sample_type_mains.get(position);
        } else if (parent.getId() == R.id.spn_sample_type_child) {
            sample_type_child = sample_type_childs.get(position);
        } else if (parent.getId() == R.id.spn_jcdw_name) {
            jcdw = jcdws.get(position);
        }
    }

    private void showSampleNameDialog() {
        if (dialog_sample_name == null) {
            dialog_sample_name = new Dialog(this);

            View dialogContentView = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_filtrate_select, null, false);
            lv = dialogContentView.findViewById(R.id.lv);
            et_content = dialogContentView.findViewById(R.id.et_content);
            filtrateAdapter = new FiltrateAdapter(this);

            dialog_sample_name.requestWindowFeature(Window.FEATURE_NO_TITLE);
            filtrateAdapter.setData(sample_names);

            et_content.setSingleLine();
            et_content.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            dialog_sample_name.setContentView(dialogContentView);

            lv.setAdapter(filtrateAdapter);
            lv.setOnItemClickListener((parent, view, position, id) -> {
                tv_sample_name.setText(sample_names.get(position).sampleName);
                //将最近选择的排列在最前面
                sample_names.get(position).setTime(new Date().getTime());
                new SampleName().saveOrUpdate(sample_names.get(position));
                SampleName sn = sample_names.get(position);
                if (position < sample_names.size()) {
                    sample_names.remove(sn);
                    sample_names.add(0, sn);
                }
//                sample_names.remove(position);
//                sample_names.add(0, sn);
//                    Log.d(TAG, "选择了样品名是=" + sampleNames.get(sposition).sampleName + "position=" + sposition);
                if (dialog_sample_name != null && dialog_sample_name.isShowing()) {
                    dialog_sample_name.dismiss();
                }
            });
            et_content.setOnEditorActionListener((tv, actionId, event) -> {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    tv_sample_name.setText(et_content.getText());
                    handled = true;
                    dialog_sample_name.dismiss();
                    et_content.setText("");
                }
                return handled;
            });
        }
        dialog_sample_name.show();
        et_content.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.spn_bjcdw_name) {
            bjcdw = null;
        } else if (parent.getId() == R.id.spn_sample_type_main) {
            sample_type_main = null;
        } else if (parent.getId() == R.id.spn_sample_type_child) {
            sample_type_child = null;
        } else if (parent.getId() == R.id.spn_jcdw_name) {
            jcdw = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sampling_date:
                showSamplingDate();
                break;
            case R.id.tv_commit:
                commit();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_sample_name:
                showSampleNameDialog();
                break;
        }
    }

    private void commit() {
        if (!verify()) return;
        String samplingDate = tv_sampling_date.getText().toString();
        String sampleName = tv_sample_name.getText().toString();
        TaskModel taskModel = new TaskModel(et_id.getText().toString(), jcdw.name, sample_type_main.name, sample_type_main.code,
                sample_type_child.name, sample_type_child.code, sampleName, bjcdw.name, bjcdw.code, samplingDate, Global.NAME);
        hibernate.save(taskModel);
        if (hibernate.isStatus()) {
            APPUtils.showToast(this, "插入成功");
            setResult(RESULT_OK);
            finish();
        } else {
            APPUtils.showToast(this, "插入失败");
        }
    }

    private boolean verify() {
        if (APPUtils.isNull(tv_sample_name.getText().toString())) {
            APPUtils.showToast(this, "请选择样品名");
            return false;
        } else if (APPUtils.isNull(et_id.getText().toString())) {
            APPUtils.showToast(this, "请输入任务ID");
            return false;
        } else if (bjcdw == null) {
            APPUtils.showToast(this, "请选择受检单位");
            return false;
        } else if (jcdw == null) {
            APPUtils.showToast(this, "请选择检测机构");
            return false;
        } else if (sample_type_main == null) {
            APPUtils.showToast(this, "请选择样品主类");
            return false;
        } else if (sample_type_child == null) {
            APPUtils.showToast(this, "请选择样品子类");
            return false;
        } else if (APPUtils.isNull(tv_sampling_date.getText().toString())) {
            APPUtils.showToast(this, "请选择抽样时间");
            return false;
        }

        return true;
    }

    private void showSamplingDate() {
        Date now = new Date();
        ;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String s = String.format("%d/%d/%d", year, month, dayOfMonth);
            tv_sampling_date.setText(s);
        }, now.getYear() + 1900, now.getMonth() + 1, now.getDay());
        datePickerDialog.show();
    }
}
