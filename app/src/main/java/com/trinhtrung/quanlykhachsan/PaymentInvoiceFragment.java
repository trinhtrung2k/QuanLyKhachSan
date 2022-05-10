package com.trinhtrung.quanlykhachsan;

import android.content.Intent;
import android.os.Bundle;

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
import com.trinhtrung.quanlykhachsan.activities.AddPaymentInvoiceActivity;
import com.trinhtrung.quanlykhachsan.adapters.PaymentInvoiceAdapter;
import com.trinhtrung.quanlykhachsan.models.BillServiceModel;
import com.trinhtrung.quanlykhachsan.models.PaymentInvoiceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PaymentInvoiceFragment extends Fragment {



    private List<BillServiceModel> billServiceModels;
    private PaymentInvoiceAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edt_search_payment;
    ScrollView scrollView;
    ProgressBar progressBar;
    private ImageView img_add_payment;



    public PaymentInvoiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_invoice, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        img_add_payment= view.findViewById(R.id.img_add_payment);
        edt_search_payment = view.findViewById(R.id.edt_search_payment);
        recyclerView = view.findViewById(R.id.payment_rec);
        scrollView = view.findViewById(R.id.scroll_view_payment);
        progressBar = view.findViewById(R.id.progressbar_payment);
    }

    private void setEvent() {

        img_add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddPaymentInvoiceActivity.class));
            }
        });

        edt_search_payment.addTextChangedListener(new TextWatcher() {
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
        billServiceModels = new ArrayList<>();
        adapter = new PaymentInvoiceAdapter(getActivity(), billServiceModels);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        GetAllService();
    }

    private void GetAllService() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllBillService.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                billServiceModels.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        billServiceModels.add(new BillServiceModel(
                                object.getString("MaHD"),
                                object.getString("TenHD"),
                                object.getString("MaDV"),
                                object.getString("TenDV"),
                                object.getString("MaKH"),
                                object.getString("HoKH"),
                                object.getString("TenKH"),
                                object.getString("HoNV"),
                                object.getString("TenNV"),
                                object.getString("NgayLapHD"),
                                object.getInt("GiaDV")


                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
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
        ArrayList<BillServiceModel> billServiceModels1 = new ArrayList<>();
        for (BillServiceModel item: billServiceModels)
        {
            if (item.getMaDV().toLowerCase().contains(text.toLowerCase())){
                billServiceModels1.add(item);
            }

        }
        adapter.filterList(billServiceModels1);

    }




    @Override
    public void onResume() {

        super.onResume();
        LoadListData();
    }
}