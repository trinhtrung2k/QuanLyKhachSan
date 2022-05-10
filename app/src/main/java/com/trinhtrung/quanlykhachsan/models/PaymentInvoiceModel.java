package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class PaymentInvoiceModel implements Serializable {
    String MaHD;
    String tenHD;
    String maDV;
    String maKH;
    String ngayLapHD;

    public PaymentInvoiceModel() {
    }

    public PaymentInvoiceModel(String maHD, String tenHD, String maDV, String maKH, String ngayLapHD) {
        MaHD = maHD;
        this.tenHD = tenHD;
        this.maDV = maDV;
        this.maKH = maKH;
        this.ngayLapHD = ngayLapHD;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
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

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getNgayLapHD() {
        return ngayLapHD;
    }

    public void setNgayLapHD(String ngayLapHD) {
        this.ngayLapHD = ngayLapHD;
    }
}
