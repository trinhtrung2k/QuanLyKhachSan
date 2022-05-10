package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.BillRoomDetailActivity;
import com.trinhtrung.quanlykhachsan.activities.BillServiceDetailActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemBillRoomBinding;
import com.trinhtrung.quanlykhachsan.models.BillRoomModel;

import java.util.ArrayList;
import java.util.List;

public class BillRoomAdapter extends RecyclerView.Adapter<BillRoomAdapter.BillServiceViewHolder> {
    Context context;
    List<BillRoomModel> list;

    public BillRoomAdapter(Context context, List<BillRoomModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BillServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_room,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillServiceViewHolder holder, int position) {
        BillRoomModel model = list.get(position);
        holder.binding.txtBillroomMaKH.setText(model.getMaKH());
        holder.binding.txtBillroomTenKH.setText(model.getTenKH());
        holder.binding.txtBillroomSophong.setText(String.valueOf(model.getSoPhong()));
        holder.binding.txtBillroomLp.setText(model.getLoaiPhong());
        holder.binding.txtBillroomGiaPhong.setText(String.valueOf(model.getGiaPhong()));
        holder.binding.itemBillroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillRoomDetailActivity.class);
                intent.putExtra("dataBillRoom",model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BillServiceViewHolder extends RecyclerView.ViewHolder {
        ItemBillRoomBinding binding;
        public BillServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemBillRoomBinding.bind(itemView);
        }
    }


    public void filterList(ArrayList<BillRoomModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}
