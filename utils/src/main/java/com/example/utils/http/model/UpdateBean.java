package com.example.utils.http.model;

public class UpdateBean {

    /**
     * app : qingdao001
     * name : 青岛仪器
     * version : 20
     * info : V1.0.0Test
     * url : http://api.shionda.com/file/app/690399304300498944.jpg
     */

    private String app;
    private String name;
    private String version;
    private String info;
    private String url;

    public UpdateBean() {
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
