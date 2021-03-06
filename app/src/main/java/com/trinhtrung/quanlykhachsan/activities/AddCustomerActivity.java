package com.trinhtrung.quanlykhachsan.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText addMaKH,addHoKH, addTenKH, addNgaySinh,addSDT, addCCCD;
    private Button btnAdd;
    private TextView tv_message;
    private Toolbar toolbar ;
    private Spinner spgender;
    private ArrayList<String> dataGender = new ArrayList<>();
    private String strSelectGender;
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

        setEventSpiner();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMaKH= addMaKH.getText().toString().trim();
                String strHoKH= addHoKH.getText().toString().trim();
                String strTenKH = addTenKH.getText().toString().trim();
                String strNgaySinh = addNgaySinh.getText().toString().trim();
            //    String strGioiTinh = spgender.getText().toString().trim();
                String sdt = addSDT.getText().toString().trim();
                String cccd = addCCCD.getText().toString().trim();

                if (strMaKH.isEmpty() || strHoKH.isEmpty() || strTenKH.isEmpty() || strNgaySinh.isEmpty()
                        || strSelectGender == null || sdt.isEmpty() || cccd.isEmpty()){
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Vui L??ng nh???p ????? th??ng tin, kh??ng ???????c ????? tr???ng");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message.setVisibility(View.GONE);
                    insertCustomer();
                }
            }
        });


    }

    private void setEventSpiner() {
        KhoiTao();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataGender);
        spgender.setAdapter(adapter);
        spgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectGender = spgender.getSelectedItem().toString();
                Toast.makeText(AddCustomerActivity.this, "b???n ch???n gi???i t??nh " + dataGender.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void KhoiTao() {
        dataGender.add("Nam");
        dataGender.add("N???");

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
                    tv_message.setText("M?? kh??ch h??ng ???? ???????c s??? d???ng!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddCustomerActivity.this, "x???y ra l???i", Toast.LENGTH_SHORT).show();
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
                params.put("gioiTinh",strSelectGender);
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
        spgender = findViewById(R.id.add_gioitinhKH);
        addSDT = findViewById(R.id.add_sdtKH);
        addCCCD = findViewById(R.id.add_cccdKH);
        btnAdd = findViewById(R.id.btn_add_kh);

        tv_message = findViewById(R.id.tv_message);
        toolbar = findViewById(R.id.toolbar_addCustomer);
    }

}