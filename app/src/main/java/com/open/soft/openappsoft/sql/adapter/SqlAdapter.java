package com.open.soft.openappsoft.sql.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：King
 * @time：2020/9/23-19:11
 * @moduleName：数据库适配器
 * @businessIntroduction：在数据库查询出数据后进行数据展示
 * @loadLibrary：GT库
 */
public class SqlAdapter extends RecyclerView.Adapter<SqlAdapter.BaseHolder> {

    private static ItemClickListener itemClickListener;
    public Context context;
    private TextView tv_querySum;
    private ProgressDialog progressDialog;
    private static List<DetectionResultBean> list;
    private GT.GT_Date gt_date;
    private String[] liushuihao_list;
    private static boolean isAllSelect = false; // 是否全部选中


    public SqlAdapter(Context context, TextView tv_querySum, ProgressDialog progressDialog) {
        gt_date = new GT.GT_Date();
        this.context = context;
        this.tv_querySum = tv_querySum;
        this.progressDialog = progressDialog;
    }

    public SqlAdapter(Context context, TextView tv_querySum, ProgressDialog progressDialog, ItemClickListener itemClickListener) {
        gt_date = new GT.GT_Date();
        this.context = context;
        this.tv_querySum = tv_querySum;
        this.progressDialog = progressDialog;
        SqlAdapter.itemClickListener = itemClickListener;
    }

    public List<DetectionResultBean> getList() {
        return list;
    }

    public void setList(List<DetectionResultBean> list) {
        SqlAdapter.list = list;
        tv_querySum.setText("共查询到(" + list.size() + ")条数据");//设置查询个数

        liushuihao_list = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            liushuihao_list[i] = i + 1 + "";
        }
        progressDialog.dismiss();//关闭加载对话框
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_sql_data, parent, false));
    }

    public void setDetectionProjectBeanList(List<DetectionResultBean> detectionProjectBeanList) {
        SqlAdapter.list = detectionProjectBeanList;
        notifyDataSetChanged(); //刷新
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        DetectionResultBean detectionResultBean = list.get(position);
//        int id = detectionResultBean.getID();
        // 设置流水号
//        detectionResultBean.setCheckRunningNumber(position);
//        holder.detectionResultBean1 = list.get(position);
        //设置数据
        holder.tv_SQLType.setText(detectionResultBean.getSQLType());
//        holder.tv_CheckRunningNumber.setText(String.valueOf(detectionResultBean.getCheckRunningNumber()));
//        holder.tv_CheckRunningNumber.setText(String.valueOf(liushuihao_list[position]));
        holder.tv_CheckRunningNumber.setText(String.valueOf(position + 1));
        holder.tv_numberSamples.setText(detectionResultBean.getNumberSamples());

        //时间戳 转北京时间
        String timestamp = String.valueOf(detectionResultBean.getDetectionTime());
        try {
            timestamp = timestamp.substring(0, timestamp.length() - 3);
            holder.tv_detectionTime.setText(gt_date.toTime(timestamp, "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
        }

        holder.tv_aisle.setText(detectionResultBean.getAisle());
        holder.tv_sampleName.setText(detectionResultBean.getSampleName());
        holder.tv_specimenType.setText(detectionResultBean.getSpecimenType());
        holder.tv_limitStandard.setText(detectionResultBean.getLimitStandard());
        holder.tv_criticalValue.setText(detectionResultBean.getCriticalValue());
        holder.tv_sampleConcentration.setText(detectionResultBean.getSampleConcentration());
        holder.tv_detectionValue.setText(detectionResultBean.getDetectionValue());
        holder.tv_xgd.setText(detectionResultBean.getXgd());
        holder.tv_testItem.setText(detectionResultBean.getTestItem());
        holder.tv_detectionResult.setText(detectionResultBean.getDetectionResult());
        holder.tv_unitsUnderInspection.setText(detectionResultBean.getUnitsUnderInspection());
        holder.tv_inspector.setText(detectionResultBean.getInspector());
        holder.tv_detectionCompany.setText(detectionResultBean.getDetectionCompany());
        holder.tv_weight.setText(detectionResultBean.getWeight());
        holder.tv_commodityPlaceOrigin.setText(detectionResultBean.getCommodityPlaceOrigin());
        holder.tv_uploadStatus.setText(detectionResultBean.getUploadStatus());
        holder.tv_companyCode.setText(detectionResultBean.companyCode == null ? "暂未添加" : detectionResultBean.companyCode);
        Boolean checked = holder.cb_item_sql.isChecked();
        //如果当前数据是最后一条，那么久显示该View
        if (list.size() == (position + 1)) {
            holder.view.setVisibility(View.VISIBLE);
        }

        if (detectionResultBean.isSelect()) {
            //选中逻辑
            holder.cb_item_sql.setChecked(true);
//            holder.ll_item.setBackgroundColor(Color.parseColor("#fff22fff"));;
            holder.tv_SQLType.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_CheckRunningNumber.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_numberSamples.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionTime.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_aisle.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_sampleName.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_specimenType.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_limitStandard.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_criticalValue.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_sampleConcentration.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionValue.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_xgd.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_testItem.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionResult.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_unitsUnderInspection.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_inspector.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionCompany.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_weight.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_commodityPlaceOrigin.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_uploadStatus.setBackgroundColor(Color.parseColor("#fff22fff"));

        } else {
            //未选中逻辑
            holder.cb_item_sql.setChecked(false);
//            holder.ll_item.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_SQLType.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_CheckRunningNumber.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_numberSamples.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionTime.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_aisle.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_sampleName.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_specimenType.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_limitStandard.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_criticalValue.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_sampleConcentration.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionValue.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_xgd.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_testItem.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionResult.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_unitsUnderInspection.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_inspector.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionCompany.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_weight.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_commodityPlaceOrigin.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_uploadStatus.setBackgroundColor(Color.parseColor("#F6F4F4"));

        }

        /*if (isAllSelect) {
            holder.cb_item_sql.setChecked(true);
            holder.tv_SQLType.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_CheckRunningNumber.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_numberSamples.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionTime.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_aisle.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_sampleName.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_specimenType.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_limitStandard.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_criticalValue.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_sampleConcentration.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionValue.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_testItem.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionResult.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_unitsUnderInspection.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_inspector.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_detectionCompany.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_weight.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_commodityPlaceOrigin.setBackgroundColor(Color.parseColor("#fff22fff"));
            holder.tv_uploadStatus.setBackgroundColor(Color.parseColor("#fff22fff"));

//            detectionResultBean.isSelect = true;
        } else {
            holder.cb_item_sql.setChecked(false);
            holder.tv_SQLType.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_CheckRunningNumber.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_numberSamples.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionTime.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_aisle.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_sampleName.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_specimenType.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_limitStandard.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_criticalValue.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_sampleConcentration.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionValue.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_testItem.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionResult.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_unitsUnderInspection.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_inspector.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_detectionCompany.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_weight.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_commodityPlaceOrigin.setBackgroundColor(Color.parseColor("#F6F4F4"));
            holder.tv_uploadStatus.setBackgroundColor(Color.parseColor("#F6F4F4"));
//            detectionResultBean.isSelect = true;
        }
        holder.isSelect = isAllSelect;*/
    }

    //一共选择了的通道列表
    public List<DetectionResultBean> getSelectedList() {
        List<DetectionResultBean> cr = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelect()) {
                cr.add(list.get(i));
            }
        }
        return cr;
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class BaseHolder extends RecyclerView.ViewHolder {
        @GT.Annotations.GT_View(R.id.tv_SQLType)
        private TextView tv_SQLType;
        @GT.Annotations.GT_View(R.id.tv_CheckRunningNumber)
        private TextView tv_CheckRunningNumber;
        @GT.Annotations.GT_View(R.id.tv_numberSamples)
        private TextView tv_numberSamples;
        @GT.Annotations.GT_View(R.id.tv_detectionTime)
        private TextView tv_detectionTime;
        @GT.Annotations.GT_View(R.id.tv_aisle)
        private TextView tv_aisle;
        @GT.Annotations.GT_View(R.id.tv_sampleName)
        private TextView tv_sampleName;
        @GT.Annotations.GT_View(R.id.tv_specimenType)
        private TextView tv_specimenType;
        @GT.Annotations.GT_View(R.id.tv_limitStandard)
        private TextView tv_limitStandard;
        @GT.Annotations.GT_View(R.id.tv_criticalValue)
        private TextView tv_criticalValue;
        @GT.Annotations.GT_View(R.id.tv_sampleConcentration)
        private TextView tv_sampleConcentration;
        @GT.Annotations.GT_View(R.id.tv_detectionValue)
        private TextView tv_detectionValue;
        @GT.Annotations.GT_View(R.id.tv_xgd)
        private TextView tv_xgd;
        @GT.Annotations.GT_View(R.id.tv_testItem)
        private TextView tv_testItem;
        @GT.Annotations.GT_View(R.id.tv_detectionResult)
        private TextView tv_detectionResult;
        @GT.Annotations.GT_View(R.id.tv_unitsUnderInspection)
        private TextView tv_unitsUnderInspection;
        @GT.Annotations.GT_View(R.id.tv_inspector)
        private TextView tv_inspector;
        @GT.Annotations.GT_View(R.id.tv_detectionCompany)
        private TextView tv_detectionCompany;
        @GT.Annotations.GT_View(R.id.tv_weight)
        private TextView tv_weight;
        @GT.Annotations.GT_View(R.id.tv_commodityPlaceOrigin)
        private TextView tv_commodityPlaceOrigin;
        @GT.Annotations.GT_View(R.id.tv_uploadStatus)
        private TextView tv_uploadStatus;
        @GT.Annotations.GT_View(R.id.tv_companyCode)
        private TextView tv_companyCode;
        @GT.Annotations.GT_View(R.id.view)
        private View view;
        @GT.Annotations.GT_View(R.id.cb_item_sql)
        private CheckBox cb_item_sql;
        @GT.Annotations.GT_View(R.id.ll_item)
        private LinearLayout ll_item;
        private boolean isSelect = false;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
            GT.getGT().build(this, itemView);
            itemView.setOnClickListener(v -> {
                //是否选中逻辑
                int number = Integer.parseInt(tv_CheckRunningNumber.getText().toString()) - 1;
                DetectionResultBean detectionResultBean = list.get(number);
                if (cb_item_sql.isChecked()) {
                    cb_item_sql.setChecked(false);
                    detectionResultBean.setSelect(false);
                    list.set(number, detectionResultBean);
//                        ll_item.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_SQLType.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_CheckRunningNumber.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_numberSamples.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_detectionTime.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_aisle.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_sampleName.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_specimenType.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_limitStandard.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_criticalValue.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_sampleConcentration.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_detectionValue.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_xgd.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_testItem.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_detectionResult.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_unitsUnderInspection.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_inspector.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_detectionCompany.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_weight.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_commodityPlaceOrigin.setBackgroundColor(Color.parseColor("#F6F4F4"));
                    tv_uploadStatus.setBackgroundColor(Color.parseColor("#F6F4F4"));
                } else {
                    cb_item_sql.setChecked(true);
                    detectionResultBean.setSelect(true);
                    list.set(number, detectionResultBean);
//                        ll_item.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_SQLType.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_CheckRunningNumber.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_numberSamples.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_detectionTime.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_aisle.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_sampleName.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_specimenType.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_limitStandard.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_criticalValue.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_sampleConcentration.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_detectionValue.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_xgd.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_testItem.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_detectionResult.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_unitsUnderInspection.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_inspector.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_detectionCompany.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_weight.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_commodityPlaceOrigin.setBackgroundColor(Color.parseColor("#fff22fff"));
                    tv_uploadStatus.setBackgroundColor(Color.parseColor("#fff22fff"));
                }
            });
        }
    }

    public interface ItemClickListener {
        void ItemClick(View v, Object object, DetectionResultBean d, int posotion);

        void ItemLongClick(View v, Object object);
    }

    private int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    private ItemClickListener getListener;

    public void setGetListener(ItemClickListener getListener) {
        this.getListener = getListener;
    }

}
