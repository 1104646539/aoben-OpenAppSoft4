package com.open.soft.openappsoft.activity.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utils.http.ToolUtil;
import com.example.utils.http.model.UploadBean;
import com.open.soft.openappsoft.R;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    List<UploadBean> uploadBeans = new ArrayList<>();

    public TaskListAdapter(List<UploadBean> uploadBeans) {
        this.uploadBeans.clear();
        this.uploadBeans.addAll(uploadBeans);
    }

    public void add(UploadBean uploadBean) {
        this.uploadBeans.add(uploadBean);
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        UploadBean item = uploadBeans.get(position);
        holder.tv_id.setText(ToolUtil.nullToString(item.getId(),""));
        holder.tv_name.setText(ToolUtil.nullToString(item.getSampleName(),""));
        holder.tv_bjdw.setText(ToolUtil.nullToString(item.getCompanyName(),""));
        holder.tv_time.setText(ToolUtil.nullToString(item.getSamplingTime(),""));
    }

    @Override
    public int getItemCount() {
        return uploadBeans.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_name, tv_bjdw, tv_time;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_bjdw = itemView.findViewById(R.id.tv_bjdw);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
