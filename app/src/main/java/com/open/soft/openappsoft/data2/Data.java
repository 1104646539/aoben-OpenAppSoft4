package com.open.soft.openappsoft.data2;

public class Data {

    private String Title;
    private String Url;
    public void setTitle(String Title) {
        this.Title = Title;
    }
    public String getTitle() {
        return Title;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
    public String getUrl() {
        return Url;
    }

    @Override
    public String toString() {
        return "Data{" +
                "Title='" + Title + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
