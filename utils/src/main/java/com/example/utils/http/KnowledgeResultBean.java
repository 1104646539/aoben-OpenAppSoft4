package com.example.utils.http;

public class KnowledgeResultBean {

    /**
     * Title : 吊白块，一种有毒的“食品添加剂”
     * Url : http://www.shionda.com:7007
     */

    private String Title;
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }


    @Override
    public String toString() {
        return "KnowledgeResultBean{" +
                "Title='" + Title + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
