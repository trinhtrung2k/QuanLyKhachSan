package com.trinhtrung.quanlykhachsan.models;

import java.io.Serializable;

public class RoomModel implements Serializable {
    int soPhong;
    String loaiPhong;
    int giaPhong;
    int trangThai;

    public RoomModel() {
    }

    public RoomModel(int soPhong) {
        this.soPhong = soPhong;
    }

    public RoomModel(int soPhong, String loaiPhong, int giaPhong) {
        this.soPhong = soPhong;
        this.loaiPhong = loaiPhong;
        this.giaPhong = giaPhong;
    }

    public RoomModel(int soPhong, String loaiPhong, int giaPhong, int trangThai) {
        this.soPhong = soPhong;
        this.loaiPhong = loaiPhong;
        this.giaPhong = giaPhong;
        this.trangThai = trangThai;
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

    public int isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
