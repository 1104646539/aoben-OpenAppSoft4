package com.open.soft.openappsoft.multifuction.interfaces;


import com.open.soft.openappsoft.multifuction.model.UserInputModel;

import java.util.List;

public interface onItemSelectedListener<T extends UserInputModel> {

    int getSelectedCount();

    List<T> getSelectedList();
}
