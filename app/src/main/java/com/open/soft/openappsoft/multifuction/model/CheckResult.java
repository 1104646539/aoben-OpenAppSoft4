package com.open.soft.openappsoft.multifuction.model;

import com.lidroid.xutils.db.annotation.Transient;

public class CheckResult extends BaseData<CheckResult> {


    public String checkedOrganization = "";//检测单位
    public String bcheckedOrganization = "";//被检测单位
    public String bcheckedOrganizationCode = "";//被检测代码
    public String SamplingTime = "";//抽样时间
    public String taskID = "";//任务ID
    public String projectName;//检测项目
    public String sampleName;//样品名称
    public String sampleNum = "";//样品编号
    public String sampleSource = "";//商品来源
    public String weight = "";//重量
    public String channel;//通道
    public long testTime;//检测时间
    public String testValue;//抑制率/检测值
    public String resultJudge;//检测结果
    public String xlz;//限量值
    public String testStandard;
    public String checker;//检测人员
    public String twh;
    @Transient
    public boolean isSelected;
    public SampleName sn;
    public int uploadId;//上传状态
    public String uploadMsg;//上传成功返回的ID

    // 新增
    public String sampleType; // 样品类型 主类
    public String sampleTypeCode; // 样品类型 主类
    public String sampleTypeChild; // 样品类型 子类
    public String sampleTypeChildCode; // 样品类型 子类

    public String xgd;  //吸光度


    // 录入方式标志位
    public Boolean status = true; // 默认是自动录入方式
    public String companyCode;//组织机构


    public CheckResult() {

    }

    public CheckResult(String checkedOrganization,
                       String bcheckedOrganization,
                       String bcheckedOrganizationCode,
                       String samplingTime,
                       String taskID,
                       String sampleType,
                       String sampleTypeCode,
                       String sampleTypeChild,
                       String sampleTypeChildCode,
                       String projectName,
                       String sampleNum,
                       String sampleName,
                       String sampleSource,
                       String channel,
                       long testTime,
                       String testValue,
                       String resultJudge,
                       String xlz,
                       String testStandard,
                       String checker,
                       String weight,
                       String twh,
                       String xgd,
                       String companyCode) {
        this.sampleTypeChild = sampleTypeChild;
        this.bcheckedOrganizationCode = bcheckedOrganizationCode;
        this.SamplingTime = samplingTime;
        this.taskID = taskID;
        this.sampleType = sampleType;
        this.sampleTypeCode = sampleTypeCode;
        this.sampleTypeChildCode = sampleTypeChildCode;
        this.checkedOrganization = checkedOrganization;
        this.bcheckedOrganization = bcheckedOrganization;
        this.sampleNum = sampleNum;
        this.projectName = projectName;
        this.sampleName = sampleName;
        this.sampleSource = sampleSource;
        this.channel = channel;
        this.testTime = testTime;
        this.testValue = testValue;
        this.resultJudge = resultJudge;
        this.xlz = xlz;
        this.testStandard = testStandard;
        this.checker = checker;
        this.weight = weight;
        this.twh = twh;
        this.xgd = xgd;
        this.companyCode = companyCode;
    }

    @Override
    public String toString() {
        return "CheckResult{" +
                "checkedOrganization='" + checkedOrganization + '\'' +
                ", bcheckedOrganization='" + bcheckedOrganization + '\'' +
                ", projectName='" + projectName + '\'' +
                ", sampleName='" + sampleName + '\'' +
                ", sampleNum='" + sampleNum + '\'' +
                ", sampleSource='" + sampleSource + '\'' +
                ", weight='" + weight + '\'' +
                ", channel='" + channel + '\'' +
                ", testTime=" + testTime +
                ", testValue='" + testValue + '\'' +
                ", resultJudge='" + resultJudge + '\'' +
                ", xlz='" + xlz + '\'' +
                ", testStandard='" + testStandard + '\'' +
                ", checker='" + checker + '\'' +
                ", twh='" + twh + '\'' +
                ", isSelected=" + isSelected +
                ", sn=" + sn +
                ", uploadId=" + uploadId +
                ", sampleType='" + sampleType + '\'' +
                ", xgd='" + xgd + '\'' +
                ", status=" + status +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
}
