/**
 * Copyright 2020 bejson.com
 */
package com.open.soft.openappsoft.bean;

/**
 * Auto-generated: 2020-09-16 15:14:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AppUpdateBean {

    private String projectName;
    private String versions;
    private String filePath;

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getVersions() {
        return versions;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "AppUpdateBean{" +
                "projectName='" + projectName + '\'' +
                ", versions='" + versions + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}