package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    String maKH;
    String hoKH;
    String tenKH;
    String ngaySinh;
    String gioiTinh;
    int SDT;
    int CCCD;

    public CustomerModel() {
    }

    public CustomerModel(String maKH) {
        this.maKH = maKH;
    }

    public CustomerModel(String maKH, String hoKH, String tenKH, String ngaySinh, String gioiTinh, int SDT, int CCCD) {
        this.maKH = maKH;
        this.hoKH = hoKH;
        this.tenKH = tenKH;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.SDT = SDT;
        this.CCCD = CCCD;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoKH() {
        return hoKH;
    }

    public void setHoKH(String hoKH) {
        this.hoKH = hoKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getSDT() {
        return SDT;
    }

    public void setSDT(int SDT) {
        this.SDT = SDT;
    }

    public int getCCCD() {
        return CCCD;
    }

    public void setCCCD(int CCCD) {
        this.CCCD = CCCD;
    }
}
