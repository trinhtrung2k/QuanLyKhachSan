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
import com.trinhtrung.quanlykhachsan.models.CustomerModel;

import java.util.HashMap;
import java.util.Map;

public class EditCustomerActivity extends AppCompatActivity {
    private EditText edtHoKH, edtTenKH, edtNgaySinh,edtGioiTinh,edtSdt,edtCCCD;
    private TextView edtMaKH,tv_message_edt_customer;
    private Button btnEdit,btnDelete;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

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
                // String strMaCN = edtMaCN.getText().toString().trim();
                String strHoKH= edtHoKH.getText().toString().trim();
                String strTenKH = edtTenKH.getText().toString().trim();
                String strNgaySinh = edtNgaySinh.getText().toString().trim();
                String strGioiTinh = edtGioiTinh.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String cccd = edtCCCD.getText().toString().trim();
             //   int phanXuong = Integer.parseInt(tempPX);
                if (strHoKH.isEmpty() || strTenKH.isEmpty() || strNgaySinh.isEmpty() || strGioiTinh.isEmpty()
                || sdt.isEmpty() || cccd.isEmpty()){
                    tv_message_edt_customer.setVisibility(View.VISIBLE);
                    tv_message_edt_customer.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message_edt_customer.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message_edt_customer.setVisibility(View.GONE);
                    UpdateCustomer();

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTenKH =edtHoKH.getText().toString() + edtTenKH.getText().toString();
                String strMaKH =edtMaKH.getText().toString();

                ConfirmDeteleCongNhan(strTenKH,strMaKH);
            }
        });



    }

    private void setData() {
        Intent intent = getIntent();
        CustomerModel customerModel = (CustomerModel) intent.getSerializableExtra("dataCustomer");
        edtMaKH.setText(customerModel.getMaKH());
        edtHoKH.setText(customerModel.getHoKH());
        edtTenKH.setText(customerModel.getTenKH());
        edtNgaySinh.setText(customerModel.getNgaySinh());
        edtGioiTinh.setText(customerModel.getGioiTinh());
        edtSdt.setText(String.valueOf(customerModel.getSDT()));
        edtCCCD.setText(String.valueOf(customerModel.getCCCD()));
    }


    private void initUi() {
        edtMaKH = findViewById(R.id.tv_makh);
        edtHoKH = findViewById(R.id.edt_hokh);
        edtTenKH = findViewById(R.id.edt_tenkh);
        edtNgaySinh = findViewById(R.id.edt_ngaysinhKH);
        edtGioiTinh = findViewById(R.id.edt_gioitinhKH);
        edtSdt = findViewById(R.id.edt_sdtKH);
        edtCCCD = findViewById(R.id.edt_ccdKH);

        btnEdit = findViewById(R.id.btn_edit_kh);
        tv_message_edt_customer = findViewById(R.id.tv_message_edt_customer);
        toolbar = findViewById(R.id.toolbar_edtCustomer);
        btnDelete = findViewById(R.id.btn_delete_kh);
    }
    private void UpdateCustomer(){

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/updateDataCustomer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(EditCustomerActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(EditCustomerActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCustomerActivity.this, "xảy ra lỗi: " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maKH", edtMaKH.getText().toString().trim());
                params.put("hoKH",edtHoKH.getText().toString().trim());
                params.put("tenKH",edtTenKH.getText().toString().trim());
                params.put("ngaySinh",edtNgaySinh.getText().toString().trim());
                params.put("gioiTinh",edtGioiTinh.getText().toString().trim());
                params.put("sdt",edtSdt.getText().toString().trim());
                params.put("cccd",edtCCCD.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void ConfirmDeteleCongNhan(String tenKH, String maKH) {

        Dialog dialog = new Dialog(EditCustomerActivity.this);
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

        TextView tvTitle = dialog.findViewById(R.id.tv_title_pdf);
        TextView tvMessage = dialog.findViewById(R.id.tv_add_Fail_pdf);
        tvTitle.setText("Bạn thực sự muốn xoá khách hàng " + tenKH.toString());
        Button btnOk = dialog.findViewById(R.id.btn_dialog_OK_pdf);
        Button btnCancel = dialog.findViewById(R.id.btn_dialog_Cancel_pdf);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCancel.isSelected();
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnOk.isSelected();
                    deleteCustomer(String.valueOf(maKH));
                    dialog.dismiss();
                }catch (Exception e){
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Xoá không thành công do ràng buộc quan hệ");
                }

            }
        });

        dialog.show();



    }

    public void deleteCustomer( String maKH ){


        String urlDelete = "http://192.168.1.12:8081/QuanLyKhachSan/deleteDataCustomer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(EditCustomerActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(EditCustomerActivity.this, "Khách hàng này đang chứa thông tin trong bảng khác", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCustomerActivity.this, "Xảy ra lỗi : " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("maKHDelete",maKH);
                return map;
            }
        };
        requestQueue.add(stringRequest);





    }

}