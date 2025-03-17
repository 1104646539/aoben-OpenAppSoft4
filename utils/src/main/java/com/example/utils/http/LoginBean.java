package com.example.utils.http;

public class LoginBean {

    /**
     * AreaId : TianjinManage
     * Id : hxchenwei
     * Pwd : 96E79218965EB72C92A549DD5A330112
     */

    private String AreaId;
    private String Id;
    private String Pwd;

    public LoginBean(String areaId, String id, String pwd) {
        AreaId = areaId;
        Id = id;
        Pwd = pwd;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String Pwd) {
        this.Pwd = Pwd;
    }
}
