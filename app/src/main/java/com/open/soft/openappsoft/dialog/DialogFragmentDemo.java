package com.open.soft.openappsoft.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;

public class DialogFragmentDemo extends DialogFragment {

    public static DialogFragmentDemo newInstance() {
        Bundle args = new Bundle();
        DialogFragmentDemo fragment = new DialogFragmentDemo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_input_message,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GT.getGT().build(this,view);

//        GT.AppDataPool.Interior.saveDataPool(DialogFragmentDemo.class,"name","张三");
    }


}
