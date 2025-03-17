package com.open.soft.openappsoft.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.data2.Data;

import java.util.List;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.KnowledgeViewHolder> {
    private Context context;
    List<Data> knowledgeResultBeans;

    public KnowledgeAdapter(Context context, List<Data> knowledgeResultBeans) {
        this.context = context;
        this.knowledgeResultBeans = knowledgeResultBeans;
    }

    @Override
    public KnowledgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KnowledgeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_know, parent, false));
    }

    @Override
    public void onBindViewHolder(KnowledgeViewHolder holder, int position) {
        holder.tv_title.setText(knowledgeResultBeans.get(position).getTitle());
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override   
            public void onClick(View v) {
                Intent start = new Intent(context, WebActivity.class);
                start.putExtra("url", knowledgeResultBeans.get(position).getUrl());
                start.putExtra("title", knowledgeResultBeans.get(position).getTitle());
                context.startActivity(start);
            }
        });
    }

    @Override
    public int getItemCount() {
        return knowledgeResultBeans.size();
    }

    public class KnowledgeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;

        public KnowledgeViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
