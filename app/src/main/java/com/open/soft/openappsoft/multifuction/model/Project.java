package com.open.soft.openappsoft.multifuction.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Transient;

public class Project extends BaseData<Project> implements FiltrateModel, Parcelable {


    public String checker;

    public String projectName;

    public String testStandard;
    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
    @Override
    public boolean isSelect() {
        return isSelect;
    }
    public float cardXlz;
    public float k;
    public float b;

    public int bochang;

    @Transient
    public boolean isSelect;

    public Project(){}

    public Project(String checker, String projectName, String testStandard, float cardXlz, float k, float b, int bochang) {
        this.checker = checker;
        this.projectName = projectName;
        this.testStandard = testStandard;
        this.cardXlz = cardXlz;
        this.k = k;
        this.b = b;
        this.bochang = bochang;
    }

    public String toString2() {
        return "Project{" +
                "checker='" + checker + '\'' +
                ", projectName='" + projectName + '\'' +
                ", testStandard='" + testStandard + '\'' +
                ", cardXlz=" + cardXlz +
                ", k=" + k +
                ", b=" + b +
                ", bochang=" + bochang +
                ", isSelect=" + isSelect +
                '}';
    }
    @Override
    public String toString() {
        return projectName;
    }
//    public Project(String checker, String projectName, String testStandard, float cardXlz) {
//        this.checker = checker;
//        this.projectName = projectName;
//        this.testStandard = testStandard;
//        this.cardXlz = cardXlz;
//    }

    @Override
    public String getName() {
        return projectName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.checker);
        dest.writeString(this.projectName);
        dest.writeString(this.testStandard);
        dest.writeDouble(this.cardXlz);
        dest.writeDouble(this.k);
        dest.writeDouble(this.b);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected Project(Parcel in) {
        this.checker = in.readString();
        this.projectName = in.readString();
        this.testStandard = in.readString();
        this.cardXlz = in.readFloat();
        this.k = in.readFloat();
        this.b = in.readFloat();
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
