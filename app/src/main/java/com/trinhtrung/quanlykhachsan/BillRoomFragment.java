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
import com.trinhtrung.quanlykhachsan.activities.AddPaymentInvoiceActivity;
import com.trinhtrung.quanlykhachsan.activities.AddRoomActivity;
import com.trinhtrung.quanlykhachsan.adapters.BillRoomAdapter;
import com.trinhtrung.quanlykhachsan.adapters.PaymentInvoiceAdapter;
import com.trinhtrung.quanlykhachsan.adapters.RoomAdapter;
import com.trinhtrung.quanlykhachsan.models.BillRoomModel;
import com.trinhtrung.quanlykhachsan.models.BillServiceModel;
import com.trinhtrung.quanlykhachsan.models.RoomModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BillRoomFragment extends Fragment {


    private List<BillRoomModel> billRoomModelList;
    private BillRoomAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edt_search_billroom;
    ScrollView scrollView;
    ProgressBar progressBar;




    public BillRoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        edt_search_billroom = view.findViewById(R.id.edt_search_billroom);
        recyclerView = view.findViewById(R.id.billroom_rec);
        scrollView = view.findViewById(R.id.scroll_view_billroom);
        progressBar = view.findViewById(R.id.progressbar_billrom);
    }

    private void setEvent() {



        edt_search_billroom.addTextChangedListener(new TextWatcher() {
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
        billRoomModelList = new ArrayList<>();
        adapter = new BillRoomAdapter(getContext(), billRoomModelList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        GetAllService();
    }

    private void GetAllService() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllBillRoom.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                billRoomModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        billRoomModelList.add(new BillRoomModel(
                                object.getString("MaKH"),
                                object.getString("HoKH"),
                                object.getString("TenKH"),
                                object.getInt("SoPhong"),
                                object.getString("LoaiPhong"),
                                object.getInt("GiaPhong")


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
        ArrayList<BillRoomModel> billRoomModels1 = new ArrayList<>();
        for (BillRoomModel item: billRoomModelList)
        {
            if (item.getMaKH().toLowerCase().contains(text.toLowerCase())){
                billRoomModels1.add(item);
            }

        }
        adapter.filterList(billRoomModels1);

    }




    @Override
    public void onResume() {

        super.onResume();
        LoadListData();
    }
}
