package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddServiceActivity extends AppCompatActivity {

    private EditText addMaDV,addTenDV, addGiaDV, addMaNV;
    private Button btnAdd;
    private TextView tv_message;
    private Toolbar toolbar ;


    private List<StaffModel> staffModelList = new ArrayList<>();
    private SmartMaterialSpinner<String> spMaNV;
    private List<String> dataStrings = new ArrayList<>();
    private String strSelectMaNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
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
        GetAllMaNV();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMaDV= addMaDV.getText().toString().trim();
                String strTenDV= addTenDV.getText().toString().trim();
                String strGiaDV = addGiaDV.getText().toString().trim();

                if (strMaDV.isEmpty() || strTenDV.isEmpty() || strGiaDV.isEmpty() || strSelectMaNV == null ){
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
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/insertDataService.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(AddServiceActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddServiceActivity.this, "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maDV", addMaDV.getText().toString().trim());
                params.put("tenDV", addTenDV.getText().toString().trim());
                params.put("giaDV", addGiaDV.getText().toString().trim());
                params.put("maNV", strSelectMaNV);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        addMaDV = findViewById(R.id.add_service_maDV);
        addTenDV = findViewById(R.id.add_service_tenDV);
        addGiaDV = findViewById(R.id.add_service_giaDV);
        spMaNV = findViewById(R.id.spinnerMaNV_service);

        btnAdd = findViewById(R.id.btn_add_service);

        tv_message = findViewById(R.id.tv_message_service);
        toolbar = findViewById(R.id.toolbar_addService);
    }


    private void setEventSpinner() {
        spMaNV.setItem(dataStrings);
        spMaNV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectMaNV = spMaNV.getSelectedItem().toString();
                Toast.makeText(AddServiceActivity.this, "Bạn chọn: " +
                        dataStrings.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void GetAllMaNV() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllMaNV.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                staffModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        staffModelList.add(new StaffModel(
                                object.getString("MaNV")

                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < staffModelList.size(); i++) {
                    dataStrings.add(staffModelList.get(i).getMaNV());
                    setEventSpinner();
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


}