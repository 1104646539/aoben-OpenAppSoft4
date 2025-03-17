package com.open.soft.openappsoft.multifuction.model;

import com.lidroid.xutils.db.annotation.Transient;

public abstract class UserInputModel<T extends UserInputModel> extends BaseData<T> {


    @Transient
    public boolean isSelected;

}
