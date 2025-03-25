package com.open.soft.openappsoft.activity.samplename;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utils.http.ToolUtil;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.orderinfo.OrderInfoModel;
import com.open.soft.openappsoft.multifuction.model.SampleName;

import java.util.List;

public class SampleNameAdapter extends RecyclerView.Adapter<SampleNameAdapter.OrderInfoHolder> {
    List<SampleName> sampleNames;
    OnLongClick onLongClick;
    public SampleNameAdapter(List<SampleName> orderInfoModels) {
        this.sampleNames = orderInfoModels;
    }

    public void setOnLongClick(OnLongClick onLongClick) {
        this.onLongClick = onLongClick;
    }

    @NonNull
    @Override
    public OrderInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderInfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInfoHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(ToolUtil.nullToString(sampleNames.get(position).getSampleName(), "-"));
        holder.root.setOnLongClickListener(view -> {
            if(onLongClick!=null){
                onLongClick.OnLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return sampleNames.size();
    }


    public static class OrderInfoHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        View root;

        public OrderInfoHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface OnLongClick {
        void OnLongClick(int position);
    }
}
