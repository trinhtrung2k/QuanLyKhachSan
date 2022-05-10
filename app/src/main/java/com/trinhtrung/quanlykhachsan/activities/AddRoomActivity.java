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

public class AddRoomActivity extends AppCompatActivity {
    private EditText addSoPhong,addLoaiPhong, addGiaPhong;
    private Button btnAdd;
    private TextView tv_message;
    private Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
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
                String strSoPhong= addSoPhong.getText().toString().trim();
                String strLoaiPhong= addLoaiPhong.getText().toString().trim();
                String strGiaPhong = addGiaPhong.getText().toString().trim();

                if (strSoPhong.isEmpty() || strLoaiPhong.isEmpty() || strGiaPhong.isEmpty() ){
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message.setVisibility(View.GONE);
                    insertRoom();
                }
            }
        });


    }

    private void insertRoom(){
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/insertDataRoom.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(AddRoomActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Mã phòng đã được sử dụng!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddRoomActivity.this, "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("soPhong", addSoPhong.getText().toString().trim());
                params.put("loaiPhong", addLoaiPhong.getText().toString().trim());
                params.put("giaPhong", addGiaPhong.getText().toString().trim());
                params.put("trangThai", String.valueOf(0));

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        addSoPhong = findViewById(R.id.add_room_soPhong);
        addLoaiPhong = findViewById(R.id.add_room_loaiphong);
        addGiaPhong = findViewById(R.id.add_room_giaphong);

        btnAdd = findViewById(R.id.btn_add_room);

        tv_message = findViewById(R.id.tv_message_room);
        toolbar = findViewById(R.id.toolbar_addRoom);
    }

}