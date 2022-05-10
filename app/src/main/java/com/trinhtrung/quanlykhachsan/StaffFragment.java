package com.trinhtrung.quanlykhachsan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.trinhtrung.quanlykhachsan.activities.AddStaffActivity;
import com.trinhtrung.quanlykhachsan.adapters.CustomerAdapter;
import com.trinhtrung.quanlykhachsan.adapters.StaffAdapter;
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StaffFragment extends Fragment {


    private List<StaffModel> staffModelList;
    private StaffAdapter staffAdapter;
    private RecyclerView recyclerView;
    private EditText edt_search_staff;
    ScrollView scrollView;
    ProgressBar progressBar;
    private ImageView img_add_staff;





    public StaffFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        img_add_staff= view.findViewById(R.id.img_add_staff);
        edt_search_staff = view.findViewById(R.id.edt_search_staff);
        recyclerView = view.findViewById(R.id.staff_rec);
        scrollView = view.findViewById(R.id.scroll_view_staff);
        progressBar = view.findViewById(R.id.progressbar_staff);
    }

    private void setEvent() {

        img_add_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddStaffActivity.class));
            }
        });

        edt_search_staff.addTextChangedListener(new TextWatcher() {
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
        staffModelList = new ArrayList<>();
        staffAdapter = new StaffAdapter(getContext(), staffModelList);
        recyclerView.setAdapter(staffAdapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        LoadListData();
        GetAllStaff();
    }

    private void GetAllStaff() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllDataStaff.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                staffModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        staffModelList.add(new StaffModel(
                                object.getString("MaNV"),
                                object.getString("HoNV"),
                                object.getString("TenNV"),
                                object.getString("GioiTinh"),
                                object.getString("NgaySinh"),
                                object.getString("DiaChi"),
                                object.getInt("SDT"),
                                object.getInt("CCCD"),
                                object.getString("Hinh"),
                                object.getString("ChucVu")

                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                staffAdapter.notifyDataSetChanged();
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
        ArrayList<StaffModel> staffModels = new ArrayList<>();
        for (StaffModel item:staffModelList)
        {
            if (item.getMaNV().toLowerCase().contains(text.toLowerCase())){
                staffModels.add(item);
            }

        }
        staffAdapter.filterList(staffModels);

    }


    @Override
    public void onStart() {
        super.onStart();
        GetAllStaff();
    }

    @Override
    public void onResume() {
        super.onResume();
     //   GetAllStaff();
        LoadListData();
    }
}