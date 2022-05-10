package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class ServiceModel implements Serializable {
        String maDV;
        String tenDV;
        int giaDV;
        String MaNV;

    public ServiceModel() {
    }

    public ServiceModel(String maDV) {
        this.maDV = maDV;
    }

    public ServiceModel(String maDV, String tenDV, int giaDV, String maNV) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.giaDV = giaDV;
        MaNV = maNV;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public int getGiaDV() {
        return giaDV;
    }

    public void setGiaDV(int giaDV) {
        this.giaDV = giaDV;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }
}
