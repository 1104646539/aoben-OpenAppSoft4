package com.open.soft.openappsoft.activity.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.utils.http.ToolUtil;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.multifuction.model.FiltrateModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskListAdapter2 extends BaseAdapter {
    private Context context;
    private List<TaskModel> taskModels = new ArrayList<>();


    public TaskListAdapter2(Context context) {
        this.context = context;
    }

    public void setData(List<TaskModel> sampleSources) {
        this.taskModels.clear();
        if (sampleSources != null) {
            this.taskModels.addAll(sampleSources);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return taskModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TaskListAdapter2.ViewHolder holder = null;
        if (convertView != null) {
            holder = (TaskListAdapter2.ViewHolder) convertView.getTag();
        } else {
            holder = new TaskListAdapter2.ViewHolder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_task_list, parent, false);
            holder.tv_id = convertView.findViewById(R.id.tv_id);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_bjdw = convertView.findViewById(R.id.tv_bjdw);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }
        TaskModel item = taskModels.get(position);
        holder.tv_id.setText(ToolUtil.nullToString(item.getTaskID(), ""));
        holder.tv_name.setText(ToolUtil.nullToString(item.getSampleName(), ""));
        holder.tv_bjdw.setText(ToolUtil.nullToString(item.getCompanyName(), ""));
        holder.tv_time.setText(ToolUtil.nullToString(item.getSamplingTime(), ""));
        return convertView;
    }

    public class ViewHolder {
        TextView tv_id, tv_name, tv_bjdw, tv_time;
    }
}
