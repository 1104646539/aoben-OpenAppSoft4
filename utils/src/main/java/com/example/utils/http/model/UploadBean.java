package com.example.utils.http.model;

public class UploadBean {

    /**
     * id : 689376395033448448
     * qrcode :
     * checkUserId :
     * checkUser : aaa
     * checkItemId : 11
     * checkItemName : itemName
     * checkMothedId : mothedId
     * checkMothedName : methedName
     * checkTime : 2025-03-16 12:30:20
     * checkOrgId :
     * checkOrg : 青岛AA检测站
     * deviceSn : W123
     * longitude :
     * latitude :
     * checkAddress :
     * sampleId :
     * sampleCode :
     * sampleTypeId :
     * sampleType : 肉类
     * sampleSubTypeId :
     * sampleSubType : 猪肉
     * sampleName : 猪肉001
     * companyName : 青岛XXX屠宰场
     * companyCode : 12345678987
     * companyUser :
     * companyPhone :
     * sampleBatch :
     * samplingAddress :
     * sampleSource :
     * samplingOrg : 抽样单位
     * samplingUser : 抽样人
     * samplingTime : 2025-03-15 13:30:20
     * checkLimit :
     * result : 0
     * resultValue : null
     * resultInfo : null
     * customize :
     */

    private String id;
    /**
     * 检测卡上的二维码，没有可以不填
     */
    private String qrcode;
    /**
     * 检测人员ID ，没有可以不填
     */
    private String checkUserId;
    /**
     * 必填 检测人员姓名
     */
    private String checkUser;
    /**
     * 检测项目ID
     */
    private String checkItemId;
    /**
     * 必填 检测项目
     */
    private String checkItemName;
    /**
     * 检测方法ID
     */
    private String checkMothedId;
    /**
     * 必填 检测方法
     */
    private String checkMothedName;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String checkTime;
    /**
     * 检测机构ID
     */
    private String checkOrgId;
    /**
     * 检测机构
     */
    private String checkOrg;
    /**
     * 必填 设备SN
     */
    private String deviceSn;
    /**
     * 位置经度
     */
    private String longitude;
    /**
     * 位置纬度
     */
    private String latitude;
    /**
     * 检测地址
     */
    private String checkAddress;
    /**
     * 样品ID
     */
    private String sampleId;
    /**
     * 样品编号
     */
    private String sampleCode;
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
     * 联系人姓名
     */
    private String companyUser;
    /**
     * 联系人电话
     */
    private String companyPhone;
    /**
     * 样品（产品）批次（生产批次）
     */
    private String sampleBatch;
    /**
     * 抽样地点
     */
    private String samplingAddress;
    /**
     * 样品（产品）来源
     */
    private String sampleSource;
    /**
     * 抽检机构（部门）
     */
    private String samplingOrg;
    /**
     * 抽样人员
     */
    private String samplingUser;
    /**
     * 抽样日期
     */
    private String samplingTime;
    /**
     * 检测限值
     */
    private String checkLimit;
    /**
     * 检测结果 0-阴性  1-阳性  2-疑似阳性  -1-检测失败
     */
    private int result;
    /**
     * 检测数值
     */
    private String resultValue;
    /**
     * 结果说明
     */
    private String resultInfo;
    /**
     * 自定义内容JSON格式key-value
     */
    private String customize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckItemId() {
        return checkItemId;
    }

    public void setCheckItemId(String checkItemId) {
        this.checkItemId = checkItemId;
    }

    public String getCheckItemName() {
        return checkItemName;
    }

    public void setCheckItemName(String checkItemName) {
        this.checkItemName = checkItemName;
    }

    public String getCheckMothedId() {
        return checkMothedId;
    }

    public void setCheckMothedId(String checkMothedId) {
        this.checkMothedId = checkMothedId;
    }

    public String getCheckMothedName() {
        return checkMothedName;
    }

    public void setCheckMothedName(String checkMothedName) {
        this.checkMothedName = checkMothedName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckOrgId() {
        return checkOrgId;
    }

    public void setCheckOrgId(String checkOrgId) {
        this.checkOrgId = checkOrgId;
    }

    public String getCheckOrg() {
        return checkOrg;
    }

    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
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

    public String getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(String companyUser) {
        this.companyUser = companyUser;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getSampleBatch() {
        return sampleBatch;
    }

    public void setSampleBatch(String sampleBatch) {
        this.sampleBatch = sampleBatch;
    }

    public String getSamplingAddress() {
        return samplingAddress;
    }

    public void setSamplingAddress(String samplingAddress) {
        this.samplingAddress = samplingAddress;
    }

    public String getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(String sampleSource) {
        this.sampleSource = sampleSource;
    }

    public String getSamplingOrg() {
        return samplingOrg;
    }

    public void setSamplingOrg(String samplingOrg) {
        this.samplingOrg = samplingOrg;
    }

    public String getSamplingUser() {
        return samplingUser;
    }

    public void setSamplingUser(String samplingUser) {
        this.samplingUser = samplingUser;
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getCheckLimit() {
        return checkLimit;
    }

    public void setCheckLimit(String checkLimit) {
        this.checkLimit = checkLimit;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Object getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public Object getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getCustomize() {
        return customize;
    }

    public void setCustomize(String customize) {
        this.customize = customize;
    }
}
