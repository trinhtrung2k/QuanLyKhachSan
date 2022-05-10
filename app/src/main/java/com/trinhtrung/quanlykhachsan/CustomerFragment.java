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
import com.trinhtrung.quanlykhachsan.activities.AddCustomerActivity;
import com.trinhtrung.quanlykhachsan.adapters.CustomerAdapter;
import com.trinhtrung.quanlykhachsan.models.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {


    private List<CustomerModel> customerModelList;
    private CustomerAdapter customerAdapter;
    private RecyclerView recyclerView;
    private EditText edt_search_customer;
    ScrollView scrollView;
    ProgressBar progressBar;
    private ImageView img_add_customer;





    public CustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        img_add_customer= view.findViewById(R.id.img_add_customer);
        edt_search_customer = view.findViewById(R.id.edt_search_customer);
        recyclerView = view.findViewById(R.id.customer_rec);
        scrollView = view.findViewById(R.id.scroll_view);
        progressBar = view.findViewById(R.id.progressbar);
    }

    private void setEvent() {

        img_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCustomerActivity.class));
            }
        });

        edt_search_customer.addTextChangedListener(new TextWatcher() {
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
        customerModelList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(getContext(), customerModelList);
        recyclerView.setAdapter(customerAdapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        GetAllCustomer();
    }

    private void GetAllCustomer() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllDataCustomer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                customerModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        customerModelList.add(new CustomerModel(
                                object.getString("MaKH"),
                                object.getString("HoKH"),
                                object.getString("TenKH"),
                                object.getString("NgaySinh"),
                                object.getString("GioiTinh"),
                                object.getInt("SDT"),
                                object.getInt("CCCD")

                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                customerAdapter.notifyDataSetChanged();
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
        ArrayList<CustomerModel> customerModels = new ArrayList<>();
        for (CustomerModel item:customerModelList)
        {
            if (item.getMaKH().toLowerCase().contains(text.toLowerCase())){
                customerModels.add(item);
            }

        }
        customerAdapter.filterList(customerModels);

    }




    @Override
    public void onResume() {
        super.onResume();
        LoadListData();
    }
}