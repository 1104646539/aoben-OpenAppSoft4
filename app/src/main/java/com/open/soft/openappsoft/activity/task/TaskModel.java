package com.open.soft.openappsoft.activity.task;

import com.gsls.gt.GT;

@GT.Hibernate.GT_Bean
public class TaskModel {
    @GT.Hibernate.GT_Key
    public int id;
    public String taskID;

    /**
     * 样品主类ID（类型）
     */
    private String sampleTypeId;
    /**
     * 必填 样品主类名称（类型）
     */
    private String sampleType;
    /**
     * 样品子类ID（类型）
     */
    private String sampleSubTypeId;
    /**
     * 必填 样品子类（类型）
     */
    private String sampleSubType;
    /**
     * 必填 样品名称
     */
    private String sampleName;
    /**
     * 必填 受检单位
     */
    private String companyName;
    /**
     * 必填 受检单位代码
     */
    private String companyCode;
    /**
     * 抽样日期
     */
    private String samplingTime;
    /**
     * 必填 检测人员姓名
     */
    private String checkUser;
    /**
     * 必填 检测单位
     */
    private String jcdw;

    public TaskModel() {
    }

    public TaskModel(String taskID, String jcdw, String sampleTypeId, String sampleType, String sampleSubTypeId, String sampleSubType, String sampleName, String companyName, String companyCode, String samplingTime, String checkUser) {
        this.taskID = taskID;
        this.jcdw = jcdw;
        this.sampleTypeId = sampleTypeId;
        this.sampleType = sampleType;
        this.sampleSubTypeId = sampleSubTypeId;
        this.sampleSubType = sampleSubType;
        this.sampleName = sampleName;
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.samplingTime = samplingTime;
        this.checkUser = checkUser;
    }

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(String sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSampleSubTypeId() {
        return sampleSubTypeId;
    }

    public void setSampleSubTypeId(String sampleSubTypeId) {
        this.sampleSubTypeId = sampleSubTypeId;
    }

    public String getSampleSubType() {
        return sampleSubType;
    }

    public void setSampleSubType(String sampleSubType) {
        this.sampleSubType = sampleSubType;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }
}
