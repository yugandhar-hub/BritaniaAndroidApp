package com.brit.britaniaapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Report implements Parcelable {
    int id;
    String depName;
    String reportName;
    String webUrl;
    boolean isChecked;

    public Report(int id, String depName, String reportName, String webUrl) {
        this.id = id;
        this.depName = depName;
        this.reportName = reportName;
        this.webUrl = webUrl;
    }

    public Report(String reportName, String webUrl) {
        this.id = 0;
        this.depName = "";
        this.reportName = reportName;
        this.webUrl = webUrl;
    }

    protected Report(Parcel in) {
        id = in.readInt();
        depName = in.readString();
        reportName = in.readString();
        webUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(depName);
        dest.writeString(reportName);
        dest.writeString(webUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    @Override
    public String toString() {
        return reportName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
