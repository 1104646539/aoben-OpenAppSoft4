package com.open.soft.openappsoft.bean;

import android.util.Log;

/**
 * created by fish
 * on 2023/10/11 10:28
 */
public class AddComBean {
    public String companyName;
    public String companyNum;


    public AddComBean() {}

    public AddComBean(String companyName, String companyNum) {
        this.companyName = companyName;
        this.companyNum = companyNum;
        Log.i("zdl", "eventBus---AddComBean--" + companyName);
    //再发一次  发了吗
    }
}
