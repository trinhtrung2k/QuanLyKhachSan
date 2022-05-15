package com.trinhtrung.quanlykhachsan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.trinhtrung.quanlykhachsan.activities.AddHireActivity;
import com.trinhtrung.quanlykhachsan.adapters.HireAdapter;
import com.trinhtrung.quanlykhachsan.models.HireModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HireFragment extends Fragment {



    private List<HireModel> hireModelList;
    private HireAdapter hireAdapter;
    private RecyclerView recyclerView;
    private EditText edt_search_hire;
    ScrollView scrollView;
    ProgressBar progressBar;
    private ImageView img_add_hire;



    public HireFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hire, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        img_add_hire= view.findViewById(R.id.img_add_hire);
        edt_search_hire = view.findViewById(R.id.edt_search_hire);
        recyclerView = view.findViewById(R.id.hire_rec);
        scrollView = view.findViewById(R.id.scroll_view_hire);
        progressBar = view.findViewById(R.id.progressbar_hire);

    }

    private void setEvent() {

        img_add_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddHireActivity.class));
            }
        });



        edt_search_hire.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });


        LoadListData();


    }

    private void LoadListData() {


        //table layout
        recyclerView.setHasFixedSize(true);
        //

        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        hireModelList = new ArrayList<>();
        hireAdapter = new HireAdapter(getContext(), hireModelList);
        recyclerView.setAdapter(hireAdapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);



        CheckTrangThaiPhong();
        GetAllHire();

    }

    private void GetAllHire() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllDataHire.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hireModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        hireModelList.add(new HireModel(
                                object.getString("MaDK"),
                                object.getString("MaKH"),
                                object.getInt("SoPhong"),
                                object.getString("NgayDen"),
                                object.getString("NgayDK"),
                                object.getString("NgayDi")

                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                hireAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }

        );
        requestQueue.add(jsonArrayRequest);


    }

    private void filter(String text) {
        ArrayList<HireModel> hireModels = new ArrayList<>();
        for (HireModel item:hireModelList)
        {
            if (item.getMaDK().toLowerCase().contains(text.toLowerCase())){
                hireModels.add(item);
            }

        }
        hireAdapter.filterList(hireModels);

    }

    private void CheckHireExist(String soPhong){
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/CheckHireExist.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                   /* Toast.makeText(AddHireActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();*/
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                   /* tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Phòng đã có người đăng kí vào thời gian nay!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));*/
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("soPhong",soPhong);

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void CheckHireNoExist(String soPhong){
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/CheckHireNoExist.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                   /* Toast.makeText(AddHireActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();*/
                }else {
                    // Toast.makeText(AddWorkerActivity.this, "error", Toast.LENGTH_SHORT).show();
                   /* tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Phòng đã có người đăng kí vào thời gian nay!!!");
                    tv_message.setTextColor(getResources().getColor(R.color.red));*/
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("error", error.toString());
                    }

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("soPhong",soPhong);

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private  void CheckTrangThaiPhong(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String ngayht = LocalDate.now().format(dateFormatter);
        for (int i = 0; i < hireModelList.size();i++){
            try {
                Date dateNgayDK = sdf.parse(hireModelList.get(i).getNgayDK());
                Date dateNgayDi = sdf.parse(hireModelList.get(i).getNgayDi());
                Date dateNgayHT = sdf.parse(ngayht);
                Log.d("dateNgayDK",dateNgayDK.toString());
                Log.d("dateNgayDi",dateNgayDi.toString());
                Log.d("dateNgayHT",dateNgayHT.toString());
                if (dateNgayDK.before(dateNgayHT) && dateNgayDi.after(dateNgayHT) ){
                    CheckHireExist(String.valueOf(hireModelList.get(i).getSoPhong()));
                    Log.d("CHECKHireExit",String.valueOf(hireModelList.get(i).getSoPhong() ));


                }else {
                    CheckHireNoExist(String.valueOf(hireModelList.get(i).getSoPhong()));
                    Log.d("CHECKHireNoExit",String.valueOf(hireModelList.get(i).getSoPhong() ));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

          //  Date dateNgayDK = sdf.parse(hireModelList.get(i).getNgayDK());
        }

    }


    @Override
    public void onResume() {

        super.onResume();
        LoadListData();
    }
}