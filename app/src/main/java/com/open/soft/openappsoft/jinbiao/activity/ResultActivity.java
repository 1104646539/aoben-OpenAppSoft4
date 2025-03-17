package com.open.soft.openappsoft.jinbiao.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.http.Global;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.adapter.ResultAdapterT;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.jinbiao.model.CardCompanyModel;
import com.open.soft.openappsoft.jinbiao.model.LineModel;
import com.open.soft.openappsoft.jinbiao.model.PeopleModel;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.open.soft.openappsoft.jinbiao.model.SampleModel;
import com.open.soft.openappsoft.jinbiao.model.SampleTypeModel;
import com.open.soft.openappsoft.jinbiao.model.ShiJiModel;
import com.open.soft.openappsoft.jinbiao.util.APPUtils;
import com.open.soft.openappsoft.jinbiao.util.SerialUtils;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;

import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 胶体金——数据管理
 */
public class ResultActivity extends BaseActivity {

    private ListView listview = null;
    private ResultAdapterT adapter = null;
    private ResultModel selectResultMode;
    private TextView startTime = null;
    private TextView endTime = null;
    private Spinner company_spinner = null;
    private Spinner project_spinner = null;
    private Spinner result_spinner = null;
    private Spinner shijiSpinner = null;
    private Spinner persionSpinner = null;
    private Spinner sampleSpinner = null;
    private Spinner typeSpinner = null;
    private Spinner result_spinner_result = null;
    private List<LineModel> project_list = null;
    private List<PeopleModel> companylist = null;
    private List<PeopleModel> persionlist = null;
    private List<SampleModel> samplelist = null;
    private List<SampleTypeModel> typelist = null;
    private List<ShiJiModel> shijilist = null;
    private ArrayAdapter<String> company_adapter = null;
    private ArrayAdapter<String> project_adapter = null;
    private ArrayAdapter<String> shiji_adapter = null;
    private ArrayAdapter<String> persion_adapter = null;
    private ArrayAdapter<String> sample_adapter = null;
    private ArrayAdapter<String> type_adapter = null;
    private String[] company_list = null;
    private String[] project_name = null;
    private String[] shiji_list = null;
    private String[] persion_list = null;
    private String[] sample_list = null;
    private String[] type_list = null;
    private long starttime_long = 0;
    private long endtime_long = 0;
    private DbUtils db;
    private LineModel linemodel = null;
    private PeopleModel company_model = null;
    public PeopleModel persion_model = null;
    public SampleModel sample_model = null;
    public SampleTypeModel type_model = null;
    public ShiJiModel shiji_model = null;
    private List<ResultModel> result_list = null;
    private CheckBox activity_result_checkbox_name;
    private CheckBox activity_result_checkbox_shiji;
    private CheckBox activity_result_checkbox_company;
    private CheckBox activity_result_checkbox_persion;
    private CheckBox activity_result_checkbox_result;
    private CheckBox activity_result_checkbox_sample;
    private CheckBox activity_result_checkbox_type;
    private ArrayAdapter<String> resultAdapter;


    // 存储已检测数据中的检测项目信息
    private String[] project_name_list = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_main);

        db = DbHelper.GetInstance();

        initView();

        result_list = new ArrayList<ResultModel>();
        adapter = new ResultAdapterT(this, result_list);
        query();
        listview.setAdapter(adapter);

        getData();

//        result_list = new ArrayList<ResultModel>();
//        adapter = new ResultAdapterT(this, result_list);
//        listview.setAdapter(adapter);
//        query();
//        change_projectname_list();
    }

    // 修改数据管理中的检测项目下拉列表
    private void change_projectname_list(){
        String concentrateUnit = "μg/kg";
        ArrayList<LineModel> list = new ArrayList<>();
        CardCompanyModel cardModel = new CardCompanyModel("奥本", "200", "800", "120", "290");

        // 向数据库保存检测项目的数据
        for(int i=0;i<result_list.size();i++){
            project_name_list = insert(project_name_list,result_list.get(i).project_name);
        }

        for (int i = 0; i < project_name_list.length; i++) {
            list.add(new LineModel(2, project_name_list[i], cardModel.name, cardModel.ScanStart, cardModel.ScanEnd, cardModel.CTPeakWidth, cardModel.CTPeakDistance, "1", "0.1", concentrateUnit));
        }
        try {
            db.saveAll(list);
            db.save(cardModel);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static String[] insert(String[] arr, String str) {

        int size = arr.length; // 获取原数组长度
        int newSize = size+1; // 原数组长度加上追加的数据的总长度

        // 新建临时字符串数组
        String[] tmp = new String[newSize];

        if(size == 0){
            tmp[0] = str;
            return tmp;
        }

        for (int j = 0; j < size; j++) {
            tmp[j] = arr[j];
        }


        for(int i=0;i<size;i++){
            if(tmp[i] != null && tmp[i].equals(str)){
                tmp = new String[size];
                for (int j = 0; j < size; j++) {
                    tmp[j] = arr[j];
                }
                break;
            }else{
                tmp[size] = str;
            }
        }

        return tmp; // 返回拼接完成的字符串数组
    }

    private void showDeleteDialog(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定删除此记录？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResultModel model = result_list.get(position);
                try {
                    db.delete(model);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                result_list.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void getData() {


        project_name = new String[]{};
        sample_list = new String[]{};
        type_list = new String[]{};

        for(int i=0;i<result_list.size();i++){
            project_name = insert(project_name,result_list.get(i).project_name);
            sample_list = insert(sample_list,result_list.get(i).sample_name);
            type_list = insert(type_list,result_list.get(i).sample_type);

        }
        try {

            companylist = db.findAll(Selector.from(PeopleModel.class).where("source", "=", 1));
            persionlist = db.findAll(Selector.from(PeopleModel.class).where("source", "=", 2));
//            shijilist = db.findAll(Selector.from(ShiJiModel.class));
            samplelist = db.findAll(Selector.from(SampleModel.class));
            //样品类型
            typelist = db.findAll(Selector.from(SampleTypeModel.class));
            project_list = db.findAll(Selector.from(LineModel.class));
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (companylist == null) {
            companylist = new ArrayList<PeopleModel>();
        }

        /*if (project_list == null) {
            project_list = new ArrayList<LineModel>();
        }*/
        if (shijilist == null) {
            shijilist = new ArrayList<ShiJiModel>();
        }
        if (persionlist == null) {
            persionlist = new ArrayList<PeopleModel>();
        }

        company_list = new String[companylist.size()];
        shiji_list = new String[shijilist.size()];
//        project_name = new String[project_list.size()];
        persion_list = new String[persionlist.size()];
//        sample_list = new String[samplelist.size()];
//        type_list = new String[typelist.size()];

        for (int i = 0; i < persionlist.size(); i++) {
            PeopleModel model = persionlist.get(i);
            persion_list[i] = model.getName();
        }

        // 样品类型
        /*for (int i = 0; i < samplelist.size(); i++) {
            SampleModel model = samplelist.get(i);
            sample_list[i] = model.getName();
        }*/

        // 样品类型
        /*for (int i = 0; i < typelist.size(); i++) {
            SampleTypeModel model = typelist.get(i);
            type_list[i] = model.getName();
        }*/

        for (int i = 0; i < companylist.size(); i++) {
            PeopleModel model = companylist.get(i);
            company_list[i] = model.getName();
        }

        // 检测项目
        /*for (int i = 0; i < project_list.size(); i++) {
            LineModel model = project_list.get(i);
            project_name[i] = model.getName();
        }*/

        for (int i = 0; i < shijilist.size(); i++) {
            ShiJiModel model = shijilist.get(i);
            shiji_list[i] = model.getName();
        }

        if (company_list == null || company_list.length <= 0) {
            company_list = new String[1];
            company_list[0] = "请先添加检测单位";
        }
        if (shiji_list == null || shiji_list.length <= 0) {
            shiji_list = new String[1];
            shiji_list[0] = "请先添加试剂厂商";
        }
        if (persion_list == null || persion_list.length <= 0) {
            persion_list = new String[1];
            persion_list[0] = "请先添加检验员";
        }
        if (sample_list == null || sample_list.length <= 0) {
            sample_list = new String[1];
            sample_list[0] = "请先添加样品名称";
        }
        if (type_list == null || type_list.length <= 0) {
            type_list = new String[1];
            type_list[0] = "请先添加样品类型";
        }

        if (project_name == null || project_name.length <= 0) {
            project_name = new String[1];
            project_name[0] = "请先添加检测项目";
        }

        company_adapter = new ArrayAdapter<String>(ResultActivity.this,	R.layout.item_simple_spiner, company_list);

        project_adapter = new ArrayAdapter<String>(ResultActivity.this,	R.layout.item_simple_spiner, project_name);
        shiji_adapter = new ArrayAdapter<String>(ResultActivity.this,R.layout.item_simple_spiner, shiji_list);
        persion_adapter = new ArrayAdapter<String>(ResultActivity.this,	R.layout.item_simple_spiner, persion_list);
        sample_adapter = new ArrayAdapter<String>(ResultActivity.this, R.layout.item_simple_spiner, sample_list);

        //样品类型 如果是手动输入的那就换成接口中的值
        if(Global.isVoluntarily){
            //自动录入
            type_adapter = new ArrayAdapter<String>(ResultActivity.this, R.layout.item_simple_spiner, type_list);
            typeSpinner.setAdapter(type_adapter);
        }else{
            //手动录入

            type_adapter = new ArrayAdapter<String>(ResultActivity.this, R.layout.item_simple_spiner, type_list);
            typeSpinner.setAdapter(type_adapter);


        }



        resultAdapter = new ArrayAdapter<String>(ResultActivity.this, R.layout.item_simple_spiner, getResources().getStringArray(R.array.check_company_name));
        company_spinner.setAdapter(company_adapter);
        project_spinner.setAdapter(project_adapter);
        shijiSpinner.setAdapter(shiji_adapter);
        persionSpinner.setAdapter(persion_adapter);
        sampleSpinner.setAdapter(sample_adapter);
        result_spinner.setAdapter(resultAdapter);
        project_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (project_list.size() > 0) {
                    LineModel model = project_list.get(arg2);
                    linemodel = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        company_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (companylist.size() > 0) {
                    PeopleModel model = companylist.get(arg2);
                    company_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        persionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (persionlist.size() > 0) {
                    PeopleModel model = persionlist.get(arg2);
                    persion_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        sampleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int arg2, long id) {
                // TODO Auto-generated method stub
                if (samplelist.size() > 0) {
                    SampleModel model = samplelist.get(arg2);
                    sample_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int arg2, long id) {
                try{
                    if(typelist == null) return;
                    if (typelist.size() > 0) {
                        SampleTypeModel model = typelist.get(arg2);
                        type_model = model;
                    }
                }catch (IndexOutOfBoundsException e){
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        shijiSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if(shijilist == null) return;
                if (shijilist.size() > 0) {
                    ShiJiModel model = shijilist.get(arg2);
                    shiji_model = model;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    public void query() {

        WhereBuilder whereBuilder = WhereBuilder.b();
        if (!GetQueryCondition(whereBuilder)) return;
        try {
            result_list.clear();
            List<ResultModel> list = db.findAll(Selector.from(ResultModel.class).where(whereBuilder).orderBy("id",true));
            result_list.addAll(list);

        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 获取查询条件
     * @param whereBuilder
     * @return
     */
    private boolean GetQueryCondition(WhereBuilder whereBuilder) {

        Boolean name_check = activity_result_checkbox_name.isChecked();
        Boolean shiji_check = activity_result_checkbox_shiji.isChecked();
        Boolean company_check = activity_result_checkbox_company.isChecked();
        Boolean persion_check = activity_result_checkbox_persion.isChecked();
        Boolean result_check = activity_result_checkbox_result.isChecked();
        Boolean sample_check = activity_result_checkbox_sample.isChecked();
        Boolean type_check = activity_result_checkbox_type.isChecked();



        if (starttime_long > 0 && endtime_long <= 0) {
            Toast.makeText(ResultActivity.this, "请输入结束日期", Toast.LENGTH_LONG).show();
            return false;
        }

        if (endtime_long > 0 && starttime_long <= 0) {
            Toast.makeText(ResultActivity.this, "请输入起始日期", Toast.LENGTH_LONG).show();
            return false;
        }
        if (starttime_long > 0 && endtime_long > 0) {
            whereBuilder.and("time", ">=", starttime_long);
            whereBuilder.and("time", "<=", endtime_long);
        }

        if (name_check) {
            whereBuilder.and("project_name", "=", project_spinner.getSelectedItem().toString());
        }
        if (shiji_check) {
            whereBuilder.and("shiji", "=", shijiSpinner.getSelectedItem().toString());
        }
        if (company_check) {
            whereBuilder.and("company_name", "=", company_spinner.getSelectedItem().toString());
        }
        if (persion_check) {
            whereBuilder.and("persion", "=", persionSpinner.getSelectedItem().toString());
        }
        if (result_check) {
            whereBuilder.and("check_result", "=", result_spinner_result.getSelectedItem().toString());
        }
        if (sample_check) {
            whereBuilder.and("sample_name", "=", sampleSpinner.getSelectedItem().toString());
        }
        if (type_check) {
            whereBuilder.and("sample_type", "=", typeSpinner.getSelectedItem().toString());

        }
        return true;
    }

    public void PrintInfo(View v) {

        if (selectResultMode == null) {
            Toast.makeText(ResultActivity.this, "请先选择要打印的数据!", Toast.LENGTH_LONG).show();
            return;
        }
        String id = selectResultMode.id + "";
        String printData = ToolUtils.GetPrintInfo(selectResultMode, this);
        APPUtils.showToast(this, printData);
        byte[] data = printData.getBytes(Charset.forName("gb2312"));
        if(!SerialUtils.COM4_SendData(data)){
            APPUtils.showToast(this, "打印数据发送失败");
        }
    }

    public void initView() {
        listview = (ListView) findViewById(R.id.result_listview);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                adapter.setSeclection(arg2);
                adapter.notifyDataSetChanged();
                selectResultMode = result_list.get(arg2);
            }
        });
        startTime = (TextView) findViewById(R.id.result_start_time);
        activity_result_checkbox_name = (CheckBox) findViewById(R.id.activity_result_checkbox_name);
        activity_result_checkbox_shiji = (CheckBox) findViewById(R.id.activity_result_checkbox_shiji);
        activity_result_checkbox_company = (CheckBox) findViewById(R.id.activity_result_checkbox_company);
        activity_result_checkbox_persion = (CheckBox) findViewById(R.id.activity_result_checkbox_persion);
        activity_result_checkbox_result = (CheckBox) findViewById(R.id.activity_result_checkbox_result);
        activity_result_checkbox_sample = (CheckBox) findViewById(R.id.activity_result_checkbox_sample);
        activity_result_checkbox_type = (CheckBox) findViewById(R.id.activity_result_checkbox_type);


        startTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showCalendarDialog(0);
            }
        });

        endTime = (TextView) findViewById(R.id.result_end_time);
        endTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showCalendarDialog(1);
            }
        });
        company_spinner = (Spinner) findViewById(R.id.result_spinner_company);
        project_spinner = (Spinner) findViewById(R.id.result_spinner_name);
        result_spinner = (Spinner) findViewById(R.id.result_spinner_result);
        persionSpinner = (Spinner) findViewById(R.id.result_persion_spinner);
        sampleSpinner = (Spinner) findViewById(R.id.result_spinner_sample);
        typeSpinner = (Spinner) findViewById(R.id.result_spinner_type);
        shijiSpinner = (Spinner) findViewById(R.id.result_shiji_spinner);
        result_spinner_result = (Spinner) findViewById(R.id.result_spinner_result);
    }

    public void ClickQuery(View v) {
        query();
    }

    public void ClickClear(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                ResultActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定删除全部数据?");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                for (int i = 0; i < result_list.size(); i++) {
                    ResultModel resultModel = result_list.get(i);
                    try {
                        db.delete(resultModel);// 自定义sql查询
                    } catch (DbException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                result_list.clear();
                adapter.notifyDataSetChanged();
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

    public void ClickUploadData(View v) {

    }

    public void ClickBack(View v) {
        this.back();
    }

    public void showCalendarDialog(final int index) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthYear = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

                if (index == 0) {
                    String time = arg1 + "-" + String.format("%02d", arg2 + 1)
                            + "-" + String.format("%02d", arg3) + " 00:00";
                    long t = 0;
                    try {
                        t = stringToLong(time, "yyyy-MM-dd HH:mm");
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    starttime_long = t;
                    startTime.setText(time);
                } else {
                    String time = arg1 + "-" + String.format("%02d", arg2 + 1)
                            + "-" + String.format("%02d", arg3) + " 23:59";
                    long t = 0;
                    try {
                        t = stringToLong(time, "yyyy-MM-dd HH:mm");
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    endtime_long = t;
                    endTime.setText(time);
                }

            }
        }, year, monthYear, day).show();

    }

    public void ClickDelete(View view){

        if (selectResultMode == null) {
            com.open.soft.openappsoft.multifuction.util.APPUtils.showToast(this, "请先选中数据");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ResultActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定删除数据?");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                try {
                    db.delete(selectResultMode);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                result_list.remove(selectResultMode);
                adapter.notifyDataSetChanged();
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

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
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

}
