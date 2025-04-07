package com.open.soft.openappsoft.activity.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.open.soft.openappsoft.R;

import java.util.List;

public class MySpinnerAdapter<T> extends ArrayAdapter<T> {
    private int layoutID;
    private int nonLayoutId;

    public MySpinnerAdapter(Context context, int layoutID,  int nonLayoutId, List<T> items) {
        super(context, layoutID, items);
        this.layoutID = layoutID;
        this.nonLayoutId = nonLayoutId;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }

    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = (TextView) LayoutInflater.from(getContext()).inflate(nonLayoutId, null);
        view.setText("请选择");
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller);
        view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        TextView row = (TextView) (convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(layoutID, parent, false));

        position = position - 1; // Adjust for initial selection item
        T item = getItem(position);
        row.setText(item.toString());
        return row;
    }

}
