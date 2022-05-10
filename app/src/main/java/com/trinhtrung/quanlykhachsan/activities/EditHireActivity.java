package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.trinhtrung.quanlykhachsan.models.HireModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditHireActivity extends AppCompatActivity {
    private TextView edtNgayDen, edtNgayDK, edtNgayDi;
    private TextView tvMaDK, tvMaKH, tvSoPhong, tv_message_edt_hire;
    private Button btnEdit,btnDelete;
    private Toolbar toolbar ;
    private ImageView choose_ngayden,choose_ngaydi,choose_ngaydk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hire);

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

  choose_ngayden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(edtNgayDen);
            }
        });
        choose_ngaydk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(edtNgayDK);
            }
        });
        choose_ngaydi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(edtNgayDi);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String strMaCN = edtMaCN.getText().toString().trim();
                String strNgayDen= edtNgayDen.getText().toString().trim();
                String strNgayDK = edtNgayDK.getText().toString().trim();
                String strNgayDi = edtNgayDi.getText().toString().trim();

                //   int phanXuong = Integer.parseInt(tempPX);
                if (strNgayDen.isEmpty() || strNgayDK.isEmpty() || strNgayDi.isEmpty()){
                    tv_message_edt_hire.setVisibility(View.VISIBLE);
                    tv_message_edt_hire.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message_edt_hire.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message_edt_hire.setVisibility(View.GONE);
                    UpdateHire();

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String strTenKH =edtHoKH.getText().toString() + edtTenKH.getText().toString();
                String strMaDK =tvMaDK.getText().toString();
                String strMaKH =tvMaKH.getText().toString();
                String strSoPhong =tvSoPhong.getText().toString();

                ConfirmDeteleHire(strMaDK,strMaKH, strSoPhong);
            }
        });



    }

    private void setData() {
        Intent intent = getIntent();
        HireModel hireModel = (HireModel) intent.getSerializableExtra("dataHire");
        tvMaDK.setText(hireModel.getMaDK());
        tvMaKH.setText(hireModel.getMaKH());
        tvSoPhong.setText(String.valueOf(hireModel.getSoPhong()));
        edtNgayDen.setText(hireModel.getNgayDen());
        edtNgayDK.setText(hireModel.getNgayDK());
        edtNgayDi.setText(String.valueOf(hireModel.getNgayDi()));


    }


    private void initUi() {
        tvMaDK = findViewById(R.id.tv_hire_maDK);
        tvMaKH = findViewById(R.id.tv_hire_MaKH);
        tvSoPhong = findViewById(R.id.tv_hire_SoPhong);
        edtNgayDen = findViewById(R.id.edt_hire_ngayden);
        edtNgayDK = findViewById(R.id.edt_hire_ngaydk);
        edtNgayDi = findViewById(R.id.edt_hire_ngaydi);

        choose_ngayden = findViewById(R.id.choose_ngayden);
        choose_ngaydk = findViewById(R.id.choose_ngaydk);
        choose_ngaydi = findViewById(R.id.choose_ngaydi);
        btnEdit = findViewById(R.id.btn_edit_hire);
        tv_message_edt_hire = findViewById(R.id.tv_message_edt_hire);
        toolbar = findViewById(R.id.toolbar_edtHire);
        btnDelete = findViewById(R.id.btn_delete_hire);
    }
    private void UpdateHire(){

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/updateDataHire.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(EditHireActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(EditHireActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditHireActivity.this, "xảy ra lỗi: " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maDK", tvMaDK.getText().toString().trim());
                params.put("maKH",tvMaKH.getText().toString().trim());
                params.put("soPhong",tvSoPhong.getText().toString().trim());
                params.put("ngayDen",edtNgayDen.getText().toString().trim());
                params.put("ngayDK",edtNgayDK.getText().toString().trim());
                params.put("ngayDi",edtNgayDi.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void ConfirmDeteleHire(String maDK,String maKH,String soPhong) {

        Dialog dialog = new Dialog(EditHireActivity.this);
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
        tvTitle.setText("Bạn thực sự muốn xoá thông tin mã thuê " + maDK.toString());
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

                    deleteHire(maDK,maKH,soPhong);
                    dialog.dismiss();
                }catch (Exception e){
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Xoá không thành công do ràng buộc quan hệ");
                }

            }
        });

        dialog.show();



    }

    public void deleteHire(String maDK,String maKH,String soPhong ){


        String urlDelete = "http://192.168.1.12:8081/QuanLyKhachSan/deleteDataHire.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(EditHireActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(EditHireActivity.this, "thông tin thuê này đang chứa thông tin trong bảng khác", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditHireActivity.this, "Xảy ra lỗi : " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("maDKDelete",maDK);
                map.put("maKHDelete",maKH);
                map.put("soPhongDelete",soPhong);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void showDatePickerDailog(TextView textView) {

        Calendar calendar = Calendar.getInstance();
        int day1 = calendar.get(calendar.DATE);
        int month1 = calendar.get(calendar.MONTH);
        int year1 = calendar.get(calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                textView.setText(simpleDateFormat.format(calendar.getTime()));


            }
        }, year1, month1, day1);

        datePickerDialog.show();
    }
}