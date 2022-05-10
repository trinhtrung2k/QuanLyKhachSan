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
import com.trinhtrung.quanlykhachsan.activities.AddRoomActivity;
import com.trinhtrung.quanlykhachsan.activities.AddServiceActivity;
import com.trinhtrung.quanlykhachsan.adapters.RoomAdapter;
import com.trinhtrung.quanlykhachsan.adapters.ServiceAdapter;
import com.trinhtrung.quanlykhachsan.models.RoomModel;
import com.trinhtrung.quanlykhachsan.models.ServiceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ServiceFragment extends Fragment {


    private List<ServiceModel> serviceModels;
    private ServiceAdapter serviceAdapter;
    private RecyclerView recyclerView;
    private EditText edt_search_service;
    ScrollView scrollView;
    ProgressBar progressBar;
    private ImageView img_add_service;



    public ServiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        img_add_service= view.findViewById(R.id.img_add_service);
        edt_search_service = view.findViewById(R.id.edt_search_service);
        recyclerView = view.findViewById(R.id.service_rec);
        scrollView = view.findViewById(R.id.scroll_view_service);
        progressBar = view.findViewById(R.id.progressbar_service);
    }

    private void setEvent() {

        img_add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddServiceActivity.class));
            }
        });

        edt_search_service.addTextChangedListener(new TextWatcher() {
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
        serviceModels = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(getContext(), serviceModels);
        recyclerView.setAdapter(serviceAdapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        GetAllService();
    }

    private void GetAllService() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllDataService.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                serviceModels.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        serviceModels.add(new ServiceModel(
                                object.getString("MaDV"),
                                object.getString("TenDV"),
                                object.getInt("GiaDV"),
                                object.getString("MaNV")


                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                serviceAdapter.notifyDataSetChanged();
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
        ArrayList<ServiceModel> serviceModel = new ArrayList<>();
        for (ServiceModel item:serviceModels)
        {
            if (item.getMaDV().toLowerCase().contains(text.toLowerCase())){
                serviceModel.add(item);
            }

        }
        serviceAdapter.filterList(serviceModel);

    }




    @Override
    public void onResume() {

        super.onResume();
        LoadListData();
    }
}