package com.open.soft.openappsoft.jinbiao.model;

public class PdfData {

    private String Url;
    public void setUrl(String Url) {
        this.Url = Url;
    }
    public String getUrl() {
        return Url;
    }

    @Override
    public String toString() {
        return "PdfData{" +
                "Url='" + Url + '\'' +
                '}';
    }
}
