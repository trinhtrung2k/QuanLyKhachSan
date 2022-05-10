package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class HireModel implements Serializable {
    String maDK;
    String maKH;
    int soPhong;
    String ngayDen;
    String ngayDK;
    String ngayDi;

    public HireModel() {
    }

    public HireModel(String maDK, String maKH, int soPhong, String ngayDen, String ngayDK, String ngayDi) {
        this.maDK = maDK;
        this.maKH = maKH;
        this.soPhong = soPhong;
        this.ngayDen = ngayDen;
        this.ngayDK = ngayDK;
        this.ngayDi = ngayDi;
    }

    public String getMaDK() {
        return maDK;
    }

    public void setMaDK(String maDK) {
        this.maDK = maDK;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public String getNgayDen() {
        return ngayDen;
    }

    public void setNgayDen(String ngayDen) {
        this.ngayDen = ngayDen;
    }

    public String getNgayDK() {
        return ngayDK;
    }

    public void setNgayDK(String ngayDK) {
        this.ngayDK = ngayDK;
    }

    public String getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(String ngayDi) {
        this.ngayDi = ngayDi;
    }
}
