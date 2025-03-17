package com.open.soft.openappsoft.jinbiao.model;

public class LinkageData {

    private String cityName;
    private int Zone;
    private String pid;
    private String CityId;
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityName() {
        return cityName;
    }

    public void setZone(int Zone) {
        this.Zone = Zone;
    }
    public int getZone() {
        return Zone;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getPid() {
        return pid;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }
    public String getCityId() {
        return CityId;
    }

    @Override
    public String toString() {
        return "Data{" +
                "cityName='" + cityName + '\'' +
                ", Zone=" + Zone +
                ", pid='" + pid + '\'' +
                ", CityId='" + CityId + '\'' +
                '}';
    }

}
