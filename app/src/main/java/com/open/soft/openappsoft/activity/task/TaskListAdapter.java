package com.open.soft.openappsoft.activity.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    List<TaskModel> taskModels;
    OnItemClick onItemClick;

    boolean showSelected;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TaskListAdapter(List<TaskModel> taskModels, boolean showSelected) {
        this.taskModels = taskModels;
        this.showSelected = showSelected;
    }

    public void add(TaskModel uploadBean) {
        this.taskModels.add(uploadBean);
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false), showSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        TaskModel item = taskModels.get(position);
        holder.tv_id.setText(ToolUtil.nullToString(item.getTaskID(), ""));
        holder.tv_name.setText(ToolUtil.nullToString(item.getSampleName(), ""));
        holder.tv_bjdw.setText(ToolUtil.nullToString(item.getCompanyName(), ""));
        holder.tv_time.setText(ToolUtil.nullToString(item.getSamplingTime(), ""));
//        holder.ll_root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onItemClick!=null){
//                    onItemClick.onItemClick(position);
//                }
//            }
//        });
        holder.iv_selected.setSelected(item.selected);
        if (showSelected) {
            holder.iv_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.selected = !item.selected;
                    taskModels.get(position).selected = item.selected;
                    holder.iv_selected.setSelected(item.selected);
                }
            });
        }
    }

    public List<TaskModel> getSelectedList() {
        List<TaskModel> selectedModel = new ArrayList<>();
        if (taskModels == null || taskModels.isEmpty()) return new ArrayList<>();

        for (int i = 0; i < taskModels.size(); i++) {
            if (taskModels.get(i).selected) {
                selectedModel.add(taskModels.get(i));
            }
        }
        return selectedModel;
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_name, tv_bjdw, tv_time;
        View ll_root;
        ImageView iv_selected;

        public TaskListViewHolder(@NonNull View itemView, boolean showSelected) {
            super(itemView);
            iv_selected = itemView.findViewById(R.id.iv_selected);
            iv_selected.setVisibility(showSelected ? View.VISIBLE : View.GONE);
            ll_root = itemView.findViewById(R.id.ll_root);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_bjdw = itemView.findViewById(R.id.tv_bjdw);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }
}
