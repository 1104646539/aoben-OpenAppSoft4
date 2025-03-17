package com.open.soft.openappsoft.multifuction.model;

public class CheckDiDingResult {

    public String qrcode; // 试剂盒二维码
    public String num;  //  滴数
    public String sampleid;  // 样品编号
    public String testresult;  // 检测结果
    public String samlename;  // 样品名称
    public String samplesource;  // 样品来源
    public String testedunit;  // 被检测单位
    public String testmethod;  // 检测方法
    public String nationallimit;  // 国家限量
    public long testTime;  // 检测时间
    public String result;  // 检测结果

    public int upload;  // 上传状态


    public CheckDiDingResult(){

    }

    public CheckDiDingResult(
            String qrcode,
            String num,
            String sampleid,
            String testresult,
            String samlename,
            String samplesource,
            String testedunit,
            String testmethod,
            String nationallimit,
            String result,
            long testTime
    ){
        this.qrcode = qrcode;
        this.num = num;
        this.sampleid = sampleid;
        this.testresult = testresult;
        this.samlename = samlename;
        this.samplesource = samplesource;
        this.testedunit = testedunit;
        this.testmethod = testmethod;
        this.nationallimit = nationallimit;
        this.testTime = testTime;
    }

    @Override
    public String toString() {
        return "CheckDiDingResult{" +
                "qrcode='" + qrcode + '\'' +
                ", num='" + num + '\'' +
                ", sampleid='" + sampleid + '\'' +
                ", testresult='" + testresult + '\'' +
                ", samlename='" + samlename + '\'' +
                ", samplesource='" + samplesource + '\'' +
                ", testedunit='" + testedunit + '\'' +
                ", testmethod='" + testmethod + '\'' +
                ", nationallimit='" + nationallimit + '\'' +
                ", testTime=" + testTime +
                '}';
    }
}
