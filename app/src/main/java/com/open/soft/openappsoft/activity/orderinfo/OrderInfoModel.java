package com.open.soft.openappsoft.activity.orderinfo;

import androidx.annotation.NonNull;

import com.example.utils.http.ToolUtil;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.util.APPUtils;

@GT.Hibernate.GT_Bean
public class OrderInfoModel {
    @GT.Hibernate.GT_Key
    public int id;
    public boolean selected;
    public String name;
    public String code;
    public int type;

    public OrderInfoModel() {
    }

    public OrderInfoModel(String name, String code, int type) {
        this.id = id;
        this.selected = selected;
        this.name = name;
        this.code = code;
        this.type = type;
    }

    /**
     * 受检单位
     * 受检单位代码（个人就是身份证号）
     */
    public static int type_bcheck = 1;
    /**
     * 样品主类名称（类型）
     * 样品主类ID（类型）
     */
    public static int type_sample_type_main = 2;
    /**
     * 样品子类名称（类型）
     * 样品子类ID（类型）
     */
    public static int type_sample_type_child = 3;


    @NonNull
    @Override
    public String toString() {
        return ToolUtil.nullToString(name, "");
    }
}
