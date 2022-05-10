package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.trinhtrung.quanlykhachsan.models.RoomModel;

import java.util.HashMap;
import java.util.Map;

public class EditRoomActivity extends AppCompatActivity {

    private EditText edtLoaiPhong, edtGiaPhong;
    private TextView tvSoPhong,tv_message_edt_room;
    private Button btnEdit,btnDelete;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

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

                String strLoaiPhong= edtLoaiPhong.getText().toString().trim();
                String strGiaPhong = edtGiaPhong.getText().toString().trim();

                //   int phanXuong = Integer.parseInt(tempPX);
                if (strLoaiPhong.isEmpty() || strGiaPhong.isEmpty()  ){
                    tv_message_edt_room.setVisibility(View.VISIBLE);
                    tv_message_edt_room.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message_edt_room.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message_edt_room.setVisibility(View.GONE);
                    UpdateRoom();

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strSoPhong =tvSoPhong.getText().toString() ;


                ConfirmDeteleRoom(strSoPhong);
            }
        });



    }

    private void setData() {
        Intent intent = getIntent();
        RoomModel roomModel = (RoomModel) intent.getSerializableExtra("dataRoom");
        tvSoPhong.setText(String.valueOf(roomModel.getSoPhong()));
        edtLoaiPhong.setText(roomModel.getLoaiPhong());
        edtGiaPhong.setText(String.valueOf(roomModel.getGiaPhong()));

    }


    private void initUi() {
        tvSoPhong = findViewById(R.id.tv_soPhongRoom);
        edtLoaiPhong = findViewById(R.id.edt_loaiphongRoom);
        edtGiaPhong = findViewById(R.id.edt_giaPhongRoom);
        btnEdit = findViewById(R.id.btn_edit_room);
        tv_message_edt_room = findViewById(R.id.tv_message_edt_room);
        toolbar = findViewById(R.id.toolbar_edtRoom);
        btnDelete = findViewById(R.id.btn_delete_room);
    }
    private void UpdateRoom(){

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/updateDataRoom.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(EditRoomActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(EditRoomActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditRoomActivity.this, "xảy ra lỗi: " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("soPhong", tvSoPhong.getText().toString().trim());
                params.put("loaiPhong",edtLoaiPhong.getText().toString().trim());
                params.put("giaPhong",edtGiaPhong.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void ConfirmDeteleRoom(String soPhong) {

        Dialog dialog = new Dialog(EditRoomActivity.this);
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
        tvTitle.setText("Bạn thực sự muốn xoá phòng " + soPhong.toString());
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

                    deleteRoom(soPhong);
                    dialog.dismiss();
                }catch (Exception e){
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Xoá không thành công do ràng buộc quan hệ");
                }

            }
        });

        dialog.show();



    }

    public void deleteRoom(String soPhong ){


        String urlDelete = "http://192.168.1.12:8081/QuanLyKhachSan/deleteDataRoom.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(EditRoomActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(EditRoomActivity.this, "Số phòng này đang chứa thông tin trong bảng khác", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditRoomActivity.this, "Xảy ra lỗi : " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("soPhongDelete",soPhong);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    @Override
    public void onResume(){
        super.onResume();
        Log.d("onResume","onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop","onStop");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart","onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPause","onPause");
    }

}