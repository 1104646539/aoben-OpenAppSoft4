package com.open.soft.openappsoft.activity.adp;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kongzue.baseokhttp.util.JsonMap;
import com.open.soft.openappsoft.R;

public class SearchAdp extends BaseQuickAdapter<JsonMap, BaseViewHolder> {

    public SearchAdp(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, JsonMap jsonMap) {
        TextView it_tv = baseViewHolder.getView(R.id.it_tv);
        TextView it_tv_num = baseViewHolder.getView(R.id.it_tv_num);
        it_tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        it_tv.setText(jsonMap.getString("Key"));
        it_tv_num.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        it_tv_num.setText(jsonMap.getString("Value"));
    }
}
