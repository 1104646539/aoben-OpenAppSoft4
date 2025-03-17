package com.open.soft.openappsoft.sql.bean;

import com.gsls.gt.GT;

/**
 * @author：King
 * @time：2020/9/19-15:15
 * @moduleName：检测结果实体类
 * @businessIntroduction：主要是为“分光光度”与“胶体金”两个模块新建实体类
 * @loadLibrary：GT库
 */
@GT.Hibernate.GT_Bean
public class DetectionResultBean {

    @GT.Hibernate.GT_Key
    private int ID;                             //检测结果ID
    private boolean isSelect;                   //是否选中
    private String SQLType;                     //数据类型
    private int CheckRunningNumber;             //检测流水号
    private String numberSamples;               //样品编号
    private long detectionTime;                 //检测时间
    private String aisle;                       //通道
    private String sampleName;                  //样品名称
    private String specimenType;                //样品类型
    private String limitStandard;               //限量标准
    private String criticalValue;               //临界值
    private String sampleConcentration;         //样品浓度
    private String detectionValue;              //抑制率/检测值
    private String testItem;                    //检测项目
    private String detectionResult;             //检测结果
    private String unitsUnderInspection;        //被检单位
    private String inspector;                   //检测人员
    private String detectionCompany;            //检测单位
    private String weight;                      //重量
    private String commodityPlaceOrigin;        //商品来源
    private String uploadStatus;                //上传状态
    private String QRCode;                      //金标卡上二维码字段
    private String spId1;                       //省级id
    private String spId2;                       //市级id
    private String spId3;                       //县级id
    private String sampleId;                    //样品总分类Id
    private String yplbId;                      //样本类别id
    private String OperatorId;                  //上传数据的OperatorId参数 后增加的字段
    private String AreaId;                      //上传数据的AreaId参数
    private String DeptId;                      //上传数据的DeptId参数
    private String xgd;                      //吸光度
    public String companyCode;               //组织机构代码
    public String objectId;               //组织机构代码


    public DetectionResultBean() {
        super();
    }

    public DetectionResultBean(boolean isSelect, String SQLType, int checkRunningNumber, String numberSamples, long detectionTime, String aisle, String sampleName, String specimenType, String limitStandard, String criticalValue, String sampleConcentration, String detectionValue, String testItem, String detectionResult, String unitsUnderInspection, String inspector, String detectionCompany, String weight, String commodityPlaceOrigin, String uploadStatus, String QRCode, String spId1, String spId2, String spId3, String yplbId, String sampleId, String operatorId, String areaId, String deptId) {
        this.isSelect = isSelect;
        this.SQLType = SQLType;
        CheckRunningNumber = checkRunningNumber;
        this.numberSamples = numberSamples;
        this.detectionTime = detectionTime;
        this.aisle = aisle;
        this.sampleName = sampleName;
        this.specimenType = specimenType;
        this.limitStandard = limitStandard;
        this.criticalValue = criticalValue;
        this.sampleConcentration = sampleConcentration;
        this.detectionValue = detectionValue;
        this.testItem = testItem;
        this.detectionResult = detectionResult;
        this.unitsUnderInspection = unitsUnderInspection;
        this.inspector = inspector;
        this.detectionCompany = detectionCompany;
        this.weight = weight;
        this.commodityPlaceOrigin = commodityPlaceOrigin;
        this.uploadStatus = uploadStatus;
        this.QRCode = QRCode;
        this.spId1 = spId1;
        this.spId2 = spId2;
        this.spId3 = spId3;
        this.yplbId = yplbId;
        this.sampleId = sampleId;
        this.OperatorId = operatorId;
        //OperatorId = operatorId;
        this.AreaId = areaId;
        this.DeptId = deptId;
    }


    public DetectionResultBean(boolean isSelect, String SQLType, int checkRunningNumber, String numberSamples, long detectionTime, String aisle, String sampleName, String specimenType, String limitStandard, String criticalValue, String sampleConcentration, String detectionValue, String testItem, String detectionResult, String unitsUnderInspection, String inspector, String detectionCompany, String weight, String commodityPlaceOrigin, String uploadStatus, String QRCode, String spId1, String spId2, String spId3, String yplbId, String sampleId, String operatorId, String areaId, String deptId,String companyCode,String objectId) {
        this.isSelect = isSelect;
        this.SQLType = SQLType;
        CheckRunningNumber = checkRunningNumber;
        this.numberSamples = numberSamples;
        this.detectionTime = detectionTime;
        this.aisle = aisle;
        this.sampleName = sampleName;
        this.specimenType = specimenType;
        this.limitStandard = limitStandard;
        this.criticalValue = criticalValue;
        this.sampleConcentration = sampleConcentration;
        this.detectionValue = detectionValue;
        this.testItem = testItem;
        this.detectionResult = detectionResult;
        this.unitsUnderInspection = unitsUnderInspection;
        this.inspector = inspector;
        this.detectionCompany = detectionCompany;
        this.weight = weight;
        this.commodityPlaceOrigin = commodityPlaceOrigin;
        this.uploadStatus = uploadStatus;
        this.QRCode = QRCode;
        this.spId1 = spId1;
        this.spId2 = spId2;
        this.spId3 = spId3;
        this.yplbId = yplbId;
        this.sampleId = sampleId;
        this.OperatorId = operatorId;
        //OperatorId = operatorId;
        this.AreaId = areaId;
        this.DeptId = deptId;
        this.companyCode = companyCode;
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getID() {
        return ID;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getSQLType() {
        return SQLType;
    }

    public void setSQLType(String SQLType) {
        this.SQLType = SQLType;
    }

    public int getCheckRunningNumber() {
        return CheckRunningNumber;
    }

    public void setCheckRunningNumber(int checkRunningNumber) {
        CheckRunningNumber = checkRunningNumber;
    }

    public String getNumberSamples() {
        return numberSamples;
    }

    public void setNumberSamples(String numberSamples) {
        this.numberSamples = numberSamples;
    }

    public long getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(long detectionTime) {
        this.detectionTime = detectionTime;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    public String getLimitStandard() {
        return limitStandard;
    }

    public void setLimitStandard(String limitStandard) {
        this.limitStandard = limitStandard;
    }

    public String getCriticalValue() {
        return criticalValue;
    }

    public void setCriticalValue(String criticalValue) {
        this.criticalValue = criticalValue;
    }

    public String getSampleConcentration() {
        return sampleConcentration;
    }

    public void setSampleConcentration(String sampleConcentration) {
        this.sampleConcentration = sampleConcentration;
    }

    public String getDetectionValue() {
        return detectionValue;
    }

    public void setDetectionValue(String detectionValue) {
        this.detectionValue = detectionValue;
    }

    public String getTestItem() {
        return testItem;
    }

    public void setTestItem(String testItem) {
        this.testItem = testItem;
    }

    public String getDetectionResult() {
        return detectionResult;
    }

    public void setDetectionResult(String detectionResult) {
        this.detectionResult = detectionResult;
    }

    public String getUnitsUnderInspection() {
        return unitsUnderInspection;
    }

    public void setUnitsUnderInspection(String unitsUnderInspection) {
        this.unitsUnderInspection = unitsUnderInspection;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getDetectionCompany() {
        return detectionCompany;
    }

    public void setDetectionCompany(String detectionCompany) {
        this.detectionCompany = detectionCompany;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCommodityPlaceOrigin() {
        return commodityPlaceOrigin;
    }

    public void setCommodityPlaceOrigin(String commodityPlaceOrigin) {
        this.commodityPlaceOrigin = commodityPlaceOrigin;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getSpId1() {
        return spId1;
    }

    public void setSpId1(String spId1) {
        this.spId1 = spId1;
    }

    public String getSpId2() {
        return spId2;
    }

    public void setSpId2(String spId2) {
        this.spId2 = spId2;
    }

    public String getSpId3() {
        return spId3;
    }

    public void setSpId3(String spId3) {
        this.spId3 = spId3;
    }

    public String getYplbId() {
        return yplbId;
    }

    public void setYplbId(String yplbId) {
        this.yplbId = yplbId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String operatorId) {
        this.OperatorId = operatorId;
        //OperatorId = operatorId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getDeptId() {
        return DeptId;
    }

    public void setDeptId(String deptId) {
        DeptId = deptId;
    }

    public String getXgd() {
        return xgd;
    }

    public void setXgd(String xgd) {
        this.xgd = xgd;
    }

    @Override
    public String toString() {
        return "DetectionResultBean{" +
                "ID=" + ID +
                ", isSelect=" + isSelect +
                ", SQLType='" + SQLType + '\'' +
                ", CheckRunningNumber=" + CheckRunningNumber +
                ", numberSamples='" + numberSamples + '\'' +
                ", detectionTime=" + detectionTime +
                ", aisle='" + aisle + '\'' +
                ", sampleName='" + sampleName + '\'' +
                ", specimenType='" + specimenType + '\'' +
                ", limitStandard='" + limitStandard + '\'' +
                ", criticalValue='" + criticalValue + '\'' +
                ", sampleConcentration='" + sampleConcentration + '\'' +
                ", detectionValue='" + detectionValue + '\'' +
                ", testItem='" + testItem + '\'' +
                ", detectionResult='" + detectionResult + '\'' +
                ", unitsUnderInspection='" + unitsUnderInspection + '\'' +
                ", inspector='" + inspector + '\'' +
                ", detectionCompany='" + detectionCompany + '\'' +
                ", weight='" + weight + '\'' +
                ", commodityPlaceOrigin='" + commodityPlaceOrigin + '\'' +
                ", uploadStatus='" + uploadStatus + '\'' +
                ", QRCode='" + QRCode + '\'' +
                ", spId1='" + spId1 + '\'' +
                ", spId2='" + spId2 + '\'' +
                ", spId3='" + spId3 + '\'' +
                ", sampleId='" + sampleId + '\'' +
                ", yplbId='" + yplbId + '\'' +
                ", OperatorId='" + OperatorId + '\'' +
                ", AreaId='" + AreaId + '\'' +
                ", DeptId='" + DeptId + '\'' +
                ", xgd='" + xgd + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
