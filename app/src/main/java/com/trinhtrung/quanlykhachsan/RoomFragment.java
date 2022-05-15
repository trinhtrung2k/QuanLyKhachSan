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
import com.trinhtrung.quanlykhachsan.activities.AddCustomerActivity;
import com.trinhtrung.quanlykhachsan.activities.AddRoomActivity;
import com.trinhtrung.quanlykhachsan.adapters.CustomerAdapter;
import com.trinhtrung.quanlykhachsan.adapters.RoomAdapter;
import com.trinhtrung.quanlykhachsan.models.CustomerModel;
import com.trinhtrung.quanlykhachsan.models.RoomModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RoomFragment extends Fragment {


    private List<RoomModel> roomModelList;
    private RoomAdapter roomAdapter;
    private RecyclerView recyclerView;
    private EditText edt_search_room;
    ScrollView scrollView;
    ProgressBar progressBar;
    private ImageView img_add_room;



    public RoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        initUi(view);

        setEvent();

        return view ;
    }

    private void initUi(View view) {
        img_add_room= view.findViewById(R.id.img_add_room);
        edt_search_room = view.findViewById(R.id.edt_search_room);
        recyclerView = view.findViewById(R.id.room_rec);
        scrollView = view.findViewById(R.id.scroll_view_room);
        progressBar = view.findViewById(R.id.progressbar_room);
    }

    private void setEvent() {

        img_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddRoomActivity.class));
            }
        });

        edt_search_room.addTextChangedListener(new TextWatcher() {
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


     /*   //table layout
        recyclerView.setHasFixedSize(true);
        //

        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);*/

        LoadListData();




    }

    private void LoadListData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        roomModelList = new ArrayList<>();
        roomAdapter = new RoomAdapter(getContext(), roomModelList);
        recyclerView.setAdapter(roomAdapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        GetAllCustomer();
    }

    private void GetAllCustomer() {

        String url = "http://192.168.1.12:8081/QuanLyKhachSan/getAllDataRoom.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                roomModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        roomModelList.add(new RoomModel(
                                object.getInt("SoPhong"),
                                object.getString("LoaiPhong"),
                                object.getInt("GiaPhong"),
                                object.getInt("TrangThai")


                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                roomAdapter.notifyDataSetChanged();
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
        ArrayList<RoomModel> roomModels = new ArrayList<>();
        for (RoomModel item:roomModelList)
        {
            if (String.valueOf(item.getSoPhong()).toLowerCase().contains(text.toLowerCase())){
                roomModels.add(item);
            }

        }
        roomAdapter.filterList(roomModels);

    }




//    @Override
//    public void onResume() {
//
//        super.onResume();
//        GetAllCustomer();
//    }
//

    @Override
    public void onResume(){
        super.onResume();
        Log.d("onnnnResumeFragment","onResume");
        LoadListData();
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onnnnDestroyFragment","onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onnnnStopFragment","onStop");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onnnnStartFragment","onStart");
       // GetAllCustomer();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onnnnPauseFragment","onPause");
    }*/
}