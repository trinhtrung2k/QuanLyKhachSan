package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.models.CustomerModel;
import com.trinhtrung.quanlykhachsan.models.RoomModel;
import com.trinhtrung.quanlykhachsan.models.ServiceModel;
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPaymentInvoiceActivity extends AppCompatActivity {

    private EditText addMaHD, addTenHD;
    private Button btnAdd;
    private TextView tv_message, addMaDV, addGMaKH,addNgayLapHD;
    private Toolbar toolbar ;
    private ImageView choose_ngayLapHD;

    private List<ServiceModel> serviceModelList = new ArrayList<>();
    private List<CustomerModel> customerModelList = new ArrayList<>();
    private SmartMaterialSpinner<String> spMaDV,spMaKH;
    private List<String> dataStringMaDV = new ArrayList<>();
    private List<String> dataStringMaKH = new ArrayList<>();
    private String strSelectMaDV;
    private String strSelectMaKH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_invoice);
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
        GetAllMaKH();
        GetAllMaDV();

        choose_ngayLapHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(addNgayLapHD);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMaHD= addMaHD.getText().toString().trim();
                String strTenHD= addTenHD.getText().toString().trim();

                String strNgayLapHD = addNgayLapHD.getText().toString().trim();


                if (strMaHD.isEmpty() || strTenHD.isEmpty() || strNgayLapHD.isEmpty() || strSelectMaKH == null || strNgayLapHD == null){
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message.setVisibility(View.GONE);
                    insertHire();
                }
            }
        });


    }

    private void insertHire(){
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/insertDataPaymentInVoice.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(AddPaymentInvoiceActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Mã hoá đơn đã được sử dụng!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPaymentInvoiceActivity.this, "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maHD", addMaHD.getText().toString().trim());
                params.put("tenHD", addTenHD.getText().toString().trim());
                params.put("maDV", strSelectMaDV);
                params.put("maKH", strSelectMaKH);
                params.put("ngayLapHD", addNgayLapHD.getText().toString().trim());


                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        addMaHD = findViewById(R.id.add_payment_mahd);
        addTenHD = findViewById(R.id.add_payment_tenhd);
        spMaDV = findViewById(R.id.spinnerMaDV_Payment);
        spMaKH = findViewById(R.id.spinnerMaKH_Payment);

        addNgayLapHD = findViewById(R.id.add_payment_ngayLapHD);
        choose_ngayLapHD = findViewById(R.id.choose_add_ngaylaphd);

        btnAdd = findViewById(R.id.btn_add_payment);

        tv_message = findViewById(R.id.tv_message);
        toolbar = findViewById(R.id.toolbar_addPayment);
    }


    private void setEventSpinnerMaDV() {
        spMaDV.setItem(dataStringMaDV);
        spMaDV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectMaDV = spMaDV.getSelectedItem().toString();
                Toast.makeText(AddPaymentInvoiceActivity.this, "bạn chọn: " +
                        dataStringMaDV.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void GetAllMaKH() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllMaKH.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                customerModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        customerModelList.add(new CustomerModel(
                                object.getString("MaKH")

                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < customerModelList.size(); i++) {
                    dataStringMaKH.add(customerModelList.get(i).getMaKH());
                    setEventSpinnerMaKH();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }

        );
        requestQueue.add(jsonArrayRequest);


    }

    private void GetAllMaDV() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllMaDV.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                serviceModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        serviceModelList.add(new ServiceModel(
                                object.getString("MaDV")

                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < serviceModelList.size(); i++) {
                    dataStringMaDV.add(String.valueOf(serviceModelList.get(i).getMaDV()));
                    setEventSpinnerMaDV();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }

        );
        requestQueue.add(jsonArrayRequest);


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

    private void setEventSpinnerMaKH() {
        spMaKH.setItem(dataStringMaKH);
        spMaKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectMaKH = spMaKH.getSelectedItem().toString();
                Toast.makeText(AddPaymentInvoiceActivity.this, "bạn chọn: " +
                        dataStringMaKH.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}