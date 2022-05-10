package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText addMaKH,addHoKH, addTenKH, addNgaySinh, addGioiTinh,addSDT, addCCCD;
    private Button btnAdd;
    private TextView tv_message;
    private Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
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
                String strMaKH= addMaKH.getText().toString().trim();
                String strHoKH= addHoKH.getText().toString().trim();
                String strTenKH = addTenKH.getText().toString().trim();
                String strNgaySinh = addNgaySinh.getText().toString().trim();
                String strGioiTinh = addGioiTinh.getText().toString().trim();
                String sdt = addSDT.getText().toString().trim();
                String cccd = addCCCD.getText().toString().trim();

                if (strMaKH.isEmpty() || strHoKH.isEmpty() || strTenKH.isEmpty() || strNgaySinh.isEmpty()
                        || strGioiTinh.isEmpty() || sdt.isEmpty() || cccd.isEmpty()){
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message.setVisibility(View.GONE);
                    insertCustomer();
                }
            }
        });


    }

    private void insertCustomer(){
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/insertDataCustomer.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(AddCustomerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Mã khách hàng đã được sử dụng!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddCustomerActivity.this, "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maKH", addMaKH.getText().toString().trim());
                params.put("hoKH", addHoKH.getText().toString().trim());
                params.put("tenKH", addTenKH.getText().toString().trim());
                params.put("ngaySinh", addNgaySinh.getText().toString().trim());
                params.put("gioiTinh", addGioiTinh.getText().toString().trim());
                params.put("sdt", addSDT.getText().toString().trim());
                params.put("cccd", addCCCD.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        addMaKH = findViewById(R.id.add_makh);
        addHoKH = findViewById(R.id.add_hokh);
        addTenKH = findViewById(R.id.add_tenkh);
        addNgaySinh = findViewById(R.id.add_ngaysinhKH);
        addGioiTinh = findViewById(R.id.add_gioitinhKH);
        addSDT = findViewById(R.id.add_sdtKH);
        addCCCD = findViewById(R.id.add_cccdKH);
        btnAdd = findViewById(R.id.btn_add_kh);

        tv_message = findViewById(R.id.tv_message);
        toolbar = findViewById(R.id.toolbar_addCustomer);
    }

}