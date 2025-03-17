package com.open.soft.openappsoft.multifuction.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.multifuction.model.CheckResult;
import com.open.soft.openappsoft.multifuction.util.ToolUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckResultAdapter extends BaseAdapter {

    private onSelectedItemChangedListener listener;
    private List<CheckResult> resultList;
    private Context context;

    public CheckResultAdapter(List<CheckResult> resultList, Context context, onSelectedItemChangedListener listener) {
        this.resultList = resultList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return resultList == null || resultList.size() == 0 ? 0 : resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList == null || resultList.size() == 0 ? null : resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_result, null);
            vh.ll = (LinearLayout) convertView.findViewById(R.id.ll);
            vh.tvID = (TextView) convertView.findViewById(R.id.tv_id);
            vh.tvChannel = (TextView) convertView.findViewById(R.id.tv_channel);
            vh.tvValue = (TextView) convertView.findViewById(R.id.tv_test_value);
            vh.tvResultJudge = (TextView) convertView.findViewById(R.id.tv_result_judge);
            vh.tvProject = (TextView) convertView.findViewById(R.id.tv_project);
            vh.tvXlz = (TextView) convertView.findViewById(R.id.tv_xlz);
            vh.tvCheckedOrg = (TextView) convertView.findViewById(R.id.tv_check_org);
            vh.tv_bchecked_org = (TextView) convertView.findViewById(R.id.tv_bchecked_org);
            vh.tvSampleName = (TextView) convertView.findViewById(R.id.tv_sample_name);
            vh.tvSampleNum = (TextView) convertView.findViewById(R.id.tv_single_xlz);
            vh.tvSampleSource = (TextView) convertView.findViewById(R.id.tv_sample_source);
            vh.tvStandard = (TextView) convertView.findViewById(R.id.tv_standard);
            vh.tvChecker = (TextView) convertView.findViewById(R.id.tv_checker);
            vh.tvUploadState = (TextView) convertView.findViewById(R.id.tv_upload_state);
            vh.tvTestTime = (TextView) convertView.findViewById(R.id.tv_test_time);
            vh.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            vh.tv_sample_number = (TextView) convertView.findViewById(R.id.tv_sample_number);
            vh.tv_xgd = (TextView) convertView.findViewById(R.id.tv_xgd);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        final CheckResult result = resultList.get(position);
        final ViewHolder finalVh = vh;
        vh.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(result, finalVh, position);
            }
        });
        vh.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener!=null){
                listener.onSelectedItemEdit(result,position);
                }
                return false;
            }
        });
        boolean state = result.isSelected;
        if (state) {
            finalVh.ll.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            finalVh.ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        vh.tv_sample_number.setText(result.sampleNum + "");
        vh.tvID.setText(result.id + "");
        vh.tvUploadState.setText(result.uploadId > 0 ? "已上传" : "未上传");
        vh.tvChannel.setText(result.channel);
        vh.tvValue.setText(result.testValue);
        vh.tvChecker.setText(result.checker);
        vh.tvXlz.setText(result.xlz);
        vh.tvResultJudge.setText(result.resultJudge);
        vh.tvProject.setText(result.projectName);
        vh.tvCheckedOrg.setText(result.checkedOrganization);
        vh.tvSampleName.setText(result.sampleName);
//        vh.tvSampleNum.setText(result.sampleNum);
        vh.tvSampleSource.setText(result.sampleSource);
        vh.tvStandard.setText(result.testStandard);
        vh.tvTestTime.setText(ToolUtils.long2String(result.testTime, "yyyy-MM-dd HH:mm:ss"));
        vh.tv_weight.setText(result.weight);
        vh.tv_bchecked_org.setText(result.bcheckedOrganization);
        vh.tv_xgd.setText(result.xgd);
        return convertView;
    }

    int selectPosition = -1;

    private void setState(CheckResult result, ViewHolder finalVh, int position) {
        boolean state = !result.isSelected;
//        resultList.get(position).isSelected = state;
//        if (listener != null) listener.onSelectedItemChanged(position);
//        finalVh.ll.setBackgroundColor(state ? context.getResources().getColor(R.color.colorPrimary) :
//                context.getResources().getColor(R.color.white));
        if (state) {
            if (selectPosition != -1) {
                resultList.get(selectPosition).isSelected = false;
            }
            selectPosition = position;
        }
        resultList.get(position).isSelected = state;
        if (listener != null) listener.onSelectedItemChanged(position);
        notifyDataSetChanged();
    }

    /**
     * 获取全部选择的列表
     *
     * @return
     */
    public List<CheckResult> getSelectList() {
        List<CheckResult> selectList = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).isSelected) {
                selectList.add(resultList.get(i));
            }
        }
        return selectList;
    }

    static class ViewHolder {

        LinearLayout ll;
        TextView tvXlz;
        TextView tvID;
        TextView tvChannel;
        TextView tvValue;
        TextView tvResultJudge;
        TextView tvProject;
        TextView tvCheckedOrg;
        TextView tvSampleName;
        TextView tvSampleNum;
        TextView tvSampleSource;
        TextView tvStandard;
        TextView tvTestTime;
        TextView tvChecker;
        TextView tvUploadState;
        TextView tv_sample_number;
        TextView tv_weight;
        TextView tv_bchecked_org;
        TextView tv_xgd;

    }

    public interface onSelectedItemChangedListener {

        void onSelectedItemChanged(int position);

        void onSelectedItemEdit(CheckResult result, int position);
    }
}
