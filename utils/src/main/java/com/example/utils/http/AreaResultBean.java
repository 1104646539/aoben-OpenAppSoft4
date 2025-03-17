package com.example.utils.http;

public class AreaResultBean {
    //    {"Id":"TianjinManage","Name":"天津监管平台","Order":10}
    public String Id;
    public String Name;
    public long Order;

    @Override
    public String toString() {
        return "AreaResultBean{" +
                "Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", Order=" + Order +
                '}';
    }

    public AreaResultBean(String id, String name, long order) {
        Id = id;
        Name = name;
        Order = order;
    }
}
