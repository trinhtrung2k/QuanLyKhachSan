package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class BillRoomModel implements Serializable {
    String maKH;
    String hoKH;
    String tenKH;
    int soPhong;
    String loaiPhong;
    int giaPhong;
    String ngayDK;
    String ngayDi;

    public BillRoomModel() {
    }

    public BillRoomModel(String maKH, String hoKH, String tenKH, int soPhong, String loaiPhong, int giaPhong, String ngayDK, String ngayDi) {
        this.maKH = maKH;
        this.hoKH = hoKH;
        this.tenKH = tenKH;
        this.soPhong = soPhong;
        this.loaiPhong = loaiPhong;
        this.giaPhong = giaPhong;
        this.ngayDK = ngayDK;
        this.ngayDi = ngayDi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public int getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(int giaPhong) {
        this.giaPhong = giaPhong;
    }

    public String getHoKH() {
        return hoKH;
    }

    public void setHoKH(String hoKH) {
        this.hoKH = hoKH;
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
