package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.trinhtrung.quanlykhachsan.models.ServiceModel;

import java.util.HashMap;
import java.util.Map;

public class EditServiceActivity extends AppCompatActivity {
    private EditText edtTenDV, edtGiaDV;
    private TextView tvMaDV, tvMaNV,tv_message_edt_service;
    private Button btnEdit,btnDelete;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        initUi();
        setData();
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



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strTenDV= edtTenDV.getText().toString().trim();
                String strGiaDV = edtGiaDV.getText().toString().trim();


                if (strTenDV.isEmpty() || strGiaDV.isEmpty()  ){
                    tv_message_edt_service.setVisibility(View.VISIBLE);
                    tv_message_edt_service.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message_edt_service.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message_edt_service.setVisibility(View.GONE);
                    UpdateService();

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strMaDV =tvMaDV.getText().toString() ;
                String strTenDV =edtTenDV.getText().toString() ;
                String strMaNV =tvMaNV.getText().toString() ;


                ConfirmDeteleService(strMaDV, strTenDV, strMaNV);
            }
        });



    }

    private void setData() {
        Intent intent = getIntent();
        ServiceModel serviceModel = (ServiceModel) intent.getSerializableExtra("dataService");
        tvMaDV.setText(serviceModel.getMaDV());
        edtTenDV.setText(serviceModel.getTenDV());
        edtGiaDV.setText(String.valueOf(serviceModel.getGiaDV()));
        tvMaNV.setText(serviceModel.getMaNV());

    }


    private void initUi() {
        tvMaDV = findViewById(R.id.tv_service_maDV);
        edtTenDV = findViewById(R.id.edt_service_tenDV);
        edtGiaDV = findViewById(R.id.edt_service_giaDV);
        tvMaNV = findViewById(R.id.tv_service_maNV);
        btnEdit = findViewById(R.id.btn_edit_service);
        tv_message_edt_service = findViewById(R.id.tv_message_edt_service);
        toolbar = findViewById(R.id.toolbar_edtService);
        btnDelete = findViewById(R.id.btn_delete_service);
    }
    private void UpdateService(){

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/updateDataService.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(EditServiceActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(EditServiceActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditServiceActivity.this, "xảy ra lỗi: " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maDV", tvMaDV.getText().toString().trim());
                params.put("tenDV",edtTenDV.getText().toString().trim());
                params.put("giaDV",edtGiaDV.getText().toString().trim());
                params.put("MaNV",tvMaNV.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void ConfirmDeteleService(String maDV, String tenDV, String maNV) {

        Dialog dialog = new Dialog(EditServiceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_feeback);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes  =window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        //khi click ra ngoai thi se thoat
        if (Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        TextView tvMessage = dialog.findViewById(R.id.tv_add_Fail);
        tvTitle.setText("Bạn thực sự muốn xoá dịch vụ " + tenDV);
        Button btnOk = dialog.findViewById(R.id.btn_dialog_OK);
        Button btnCancel = dialog.findViewById(R.id.btn_dialog_Cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    deleteService(maDV, maNV);
                    dialog.dismiss();
                }catch (Exception e){
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Xoá không thành công do ràng buộc quan hệ");
                }

            }
        });

        dialog.show();



    }

    public void deleteService(String maDV, String maNV ){


        String urlDelete = "http://192.168.1.12:8081/QuanLyKhachSan/deleteDataService.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(EditServiceActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(EditServiceActivity.this, "Dịch vụ này đang chứa thông tin trong bảng khác", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditServiceActivity.this, "Xảy ra lỗi : " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("maDVDelete",maDV);
                map.put("maNVDelete",maNV);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}