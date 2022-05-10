package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class BillServiceModel implements Serializable {
    String maHD;
    String tenHD;
    String maDV;
    String tenDV;
    String maKH;
    String hoKH;
    String tenKH;
    String hoNV;
    String tenNV;
    String ngayLapHD;
    int giaDV;
    int thanhtien;

    public BillServiceModel() {
    }

    public BillServiceModel(String maHD, String tenHD, String maDV, String tenDV, String maKH, String hoKH, String tenKH, String hoNV, String tenNV, String ngayLapHD, int giaDV) {
        this.maHD = maHD;
        this.tenHD = tenHD;
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.maKH = maKH;
        this.hoKH = hoKH;
        this.tenKH = tenKH;
        this.hoNV = hoNV;
        this.tenNV = tenNV;
        this.ngayLapHD = ngayLapHD;
        this.giaDV = giaDV;

    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getTenHD() {
        return tenHD;
    }

    public void setTenHD(String tenHD) {
        this.tenHD = tenHD;
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

    public String getHoNV() {
        return hoNV;
    }

    public void setHoNV(String hoNV) {
        this.hoNV = hoNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getNgayLapHD() {
        return ngayLapHD;
    }

    public void setNgayLapHD(String ngayLapHD) {
        this.ngayLapHD = ngayLapHD;
    }

    public int getGiaDV() {
        return giaDV;
    }

    public void setGiaDV(int giaDV) {
        this.giaDV = giaDV;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }
}

