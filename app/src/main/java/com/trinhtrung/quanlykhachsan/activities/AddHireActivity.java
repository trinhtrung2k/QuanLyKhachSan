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
import com.trinhtrung.quanlykhachsan.models.HireModel;
import com.trinhtrung.quanlykhachsan.models.RoomModel;
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddHireActivity extends AppCompatActivity {

    private EditText addMaDK;
    private Button btnAdd;
    private TextView tv_message, addNgayDen, addGNgayDK,addNgayDi;
    private Toolbar toolbar ;
    private ImageView choose_ngayden,choose_ngaydi,choose_ngaydk;

    private List<CustomerModel> customerModelList = new ArrayList<>();
    private List<RoomModel> roomModelList = new ArrayList<>();
    private SmartMaterialSpinner<String> spMaKH,spSoPhong;
    private List<String> dataStrings = new ArrayList<>();
    private List<String> dataStringSoPhong = new ArrayList<>();
    private String strSelectMaKH;
    private String strSelectSoPhong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hire);
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
        GetAllSoPhong();
        choose_ngayden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(addNgayDen);
            }
        });
        choose_ngaydk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(addGNgayDK);
            }
        });
        choose_ngaydi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog(addNgayDi);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMaDK= addMaDK.getText().toString().trim();

                String strNgayDen = addNgayDen.getText().toString().trim();
                String strNgayDK = addGNgayDK.getText().toString().trim();
                String strNgayDi = addNgayDi.getText().toString().trim();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date startDate = simpleDateFormat.parse(strNgayDen);
                    Date registrationDate = simpleDateFormat.parse(strNgayDK);
                    Date endDate = simpleDateFormat.parse(strNgayDi);

                    if (strMaDK.isEmpty() ||strSelectMaKH == null || strSelectSoPhong == null || strNgayDen.isEmpty()
                            || strNgayDK.isEmpty() || strNgayDi.isEmpty() || strSelectMaKH == null){
                        tv_message.setVisibility(View.VISIBLE);
                        tv_message.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                        tv_message.setTextColor(getResources().getColor(R.color.red));
                    }
                    else if (registrationDate.before(startDate) || endDate.before(registrationDate) || endDate.before(startDate)){
                        tv_message.setVisibility(View.VISIBLE);
                        tv_message.setText("Sai login về thời gian");
                        tv_message.setTextColor(getResources().getColor(R.color.red));
                    }
                    else{
                        tv_message.setVisibility(View.GONE);
                        insertHire();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        });


    }

    private void insertHire(){
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/insertDataHire.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(AddHireActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Mã đăng kí đã được sử dụng!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddHireActivity.this, "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maDK", addMaDK.getText().toString().trim());
                params.put("maKH", strSelectMaKH);
                params.put("soPhong", strSelectSoPhong);
                params.put("ngayDen", addNgayDen.getText().toString().trim());
                params.put("ngayDK", addGNgayDK.getText().toString().trim());
                params.put("ngayDi", addNgayDi.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        addMaDK = findViewById(R.id.add_hire_madk);
        spMaKH = findViewById(R.id.spinnerMaKH_Hire);
        spSoPhong = findViewById(R.id.spinnerSoPhong_Hire);
        addNgayDen = findViewById(R.id.add_hire_ngayden);
        addGNgayDK= findViewById(R.id.add_hire_ngayDK);
        addNgayDi = findViewById(R.id.add_hire_ngayDi);
        choose_ngayden = findViewById(R.id.choose_add_ngayden);
        choose_ngaydk = findViewById(R.id.choose_add_ngaydk);
        choose_ngaydi = findViewById(R.id.choose_add_ngaydi);
        btnAdd = findViewById(R.id.btn_add_hire);

        tv_message = findViewById(R.id.tv_message);
        toolbar = findViewById(R.id.toolbar_addHire);
    }


    private void setEventSpinner() {
        spMaKH.setItem(dataStrings);
        spMaKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectMaKH = spMaKH.getSelectedItem().toString();
                Toast.makeText(AddHireActivity.this, "bạn chọn: " +
                        dataStrings.get(position), Toast.LENGTH_SHORT).show();
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
                    dataStrings.add(customerModelList.get(i).getMaKH());
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

    private void GetAllSoPhong() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllSoPhong.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                roomModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        roomModelList.add(new RoomModel(
                                object.getInt("SoPhong")

                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < roomModelList.size(); i++) {
                    dataStringSoPhong.add(String.valueOf(roomModelList.get(i).getSoPhong()));
                    setEventSpinnerSoPhong();
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

    private void setEventSpinnerSoPhong() {
        spSoPhong.setItem(dataStringSoPhong);
        spSoPhong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectSoPhong = spSoPhong.getSelectedItem().toString();
                Toast.makeText(AddHireActivity.this, "bạn chọn: " +
                        dataStringSoPhong.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}