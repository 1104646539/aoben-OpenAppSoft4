package com.open.soft.openappsoft.activity.orderinfo;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utils.http.ToolUtil;
import com.open.soft.openappsoft.R;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderInfoHolder> {
    List<OrderInfoModel> orderInfoModels;
    OnLongClick onLongClick;
    public OrderInfoAdapter(List<OrderInfoModel> orderInfoModels) {
        this.orderInfoModels = orderInfoModels;
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
        holder.tv_name.setText(ToolUtil.nullToString(orderInfoModels.get(position).name, "-"));
        holder.root.setOnLongClickListener(view -> {
            if(onLongClick!=null){
                onLongClick.OnLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return orderInfoModels.size();
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
