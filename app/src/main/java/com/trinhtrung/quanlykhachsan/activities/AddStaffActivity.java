package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.trinhtrung.quanlykhachsan.R;


import java.util.HashMap;
import java.util.Map;


public class AddStaffActivity extends AppCompatActivity {


    private EditText addMaNV,addHoNV, addTenNV, addGioiTinh, addNgaySinh,addDiaChi, addSDT, addCCCD, addAvatar, addChucVu;
    private Button btnAdd;
    private TextView tv_message;
    private Toolbar toolbar ;

    String url = "http://192.168.1.12:8081/QuanLyKhachSan/insertDataStaff.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        initUi();
        setEvent();

    }

    private void setEvent() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMaNV= addMaNV.getText().toString().trim();
                String strHoNV= addHoNV.getText().toString().trim();
                String strTenNV = addTenNV.getText().toString().trim();
                String strNgaySinh = addNgaySinh.getText().toString().trim();
                String strGioiTinh = addGioiTinh.getText().toString().trim();
                String strDiaChi = addDiaChi.getText().toString().trim();
                String sdt = addSDT.getText().toString().trim();
                String cccd = addCCCD.getText().toString().trim();
              //  String strHinh = addAvatar.getText().toString().trim();
                String strChucVu = addChucVu.getText().toString().trim();


                if (strMaNV.isEmpty() || strHoNV.isEmpty() || strTenNV.isEmpty() || strNgaySinh.isEmpty()
                        || strGioiTinh.isEmpty() || sdt.isEmpty() || cccd.isEmpty()
                        || strDiaChi.isEmpty()  || strChucVu.isEmpty()){
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message.setVisibility(View.GONE);
                    insertStaff(url);
                }
            }
        });


    }

    private void insertStaff(String url){


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(AddStaffActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Mã nhân viên đã được sử dụng!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddStaffActivity.this, "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maNV", addMaNV.getText().toString().trim());
                params.put("hoNV", addHoNV.getText().toString().trim());
                params.put("tenNV", addTenNV.getText().toString().trim());
                params.put("gioiTinh", addGioiTinh.getText().toString().trim());
                params.put("ngaySinh", addNgaySinh.getText().toString().trim());
                params.put("diaChi", addDiaChi.getText().toString().trim());
                params.put("sdt", addSDT.getText().toString().trim());
                params.put("cccd", addCCCD.getText().toString().trim());
               params.put("hinh", "http://192.168.20.1:8081/QuanLyKhachSan/upload/imagesprofile.png");
                params.put("chucVu", addChucVu.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        addMaNV = findViewById(R.id.add_maNV);
        addHoNV = findViewById(R.id.add_hoNV);
        addTenNV = findViewById(R.id.add_tenNV);

        addGioiTinh = findViewById(R.id.add_gioitinhNV);
        addNgaySinh = findViewById(R.id.add_namsinhNV);
        addDiaChi = findViewById(R.id.add_diachiNV);
        addSDT = findViewById(R.id.add_sdtnv);
        addCCCD = findViewById(R.id.add_cccdNV);
        addChucVu = findViewById(R.id.add_chucvuNV);



        btnAdd = findViewById(R.id.btn_add_nv);

        tv_message = findViewById(R.id.tv_message);
        toolbar = findViewById(R.id.toolbar_addStaff);
    }



}