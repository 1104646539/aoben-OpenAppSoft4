package com.example.utils.http;

public class LoginResultBean {

    /**
     * LogId : hxchenwei
     * AreaId : TianjinManage
     * UserName : 陈伟
     * DeptName : 河西检测中心
     */

    private String LogId;
    private String AreaId;
    private String UserName;
    private String DeptName;
    private String DeptId;
    private String SamplingMode;
    private String NeedCompanyCode;

    public LoginResultBean() {
        super();
    }

    public LoginResultBean(String logId, String areaId, String userName, String deptName, String deptId, String samplingMode) {
        LogId = logId;
        AreaId = areaId;
        UserName = userName;
        DeptName = deptName;
        DeptId = deptId;
        SamplingMode = samplingMode;
    }

    public String getNeedCompanyCode() {
        return NeedCompanyCode;
    }

    public void setNeedCompanyCode(String needCompanyCode) {
        NeedCompanyCode = needCompanyCode;
    }

    public LoginResultBean(String areaId, String logId, String userName, String deptName, String deptId, String samplingMode, String NeedCompanyCode) {
        AreaId = areaId;
        LogId = logId;
        UserName = userName;
        DeptName = deptName;
        DeptId = deptId;
        SamplingMode = samplingMode;
        this.NeedCompanyCode = NeedCompanyCode;
    }

    public String getLogId() {
        return LogId;
    }

    public void setLogId(String logId) {
        LogId = logId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getDeptId() {
        return DeptId;
    }

    public void setDeptId(String deptId) {
        DeptId = deptId;
    }

    public String getSamplingMode() {
        return SamplingMode;
    }

    public void setSamplingMode(String samplingMode) {
        SamplingMode = samplingMode;
    }

    @Override
    public String toString() {
        return "LoginResultBean{" +
                "LogId='" + LogId + '\'' +
                ", AreaId='" + AreaId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", DeptName='" + DeptName + '\'' +
                ", DeptId='" + DeptId + '\'' +
                ", SamplingMode='" + SamplingMode + '\'' +
                '}';
    }
}
