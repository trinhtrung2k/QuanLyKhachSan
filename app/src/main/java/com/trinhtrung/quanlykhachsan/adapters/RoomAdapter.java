package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.EditRoomActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemRoomBinding;
import com.trinhtrung.quanlykhachsan.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{

    Context context;
    List<RoomModel> list;

    public RoomAdapter(Context context, List<RoomModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomModel roomModel = list.get(position);
        holder.binding.tvRoomSoPhong.setText(String.valueOf(roomModel.getSoPhong()));
        holder.binding.tvRoomLoaiPhong.setText(roomModel.getLoaiPhong());
        holder.binding.tvRoomGiaPhong.setText(String.valueOf(roomModel.getGiaPhong()));
        int trangThai = roomModel.isTrangThai();
        if (trangThai == 1){
            holder.binding.tvRoomTrangThai.setText("Phòng đang được thuê");
        }else  {
            holder.binding.tvRoomTrangThai.setText("Phòng còn trống");
        }

        holder.binding.itemRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRoomActivity.class);
                intent.putExtra("dataRoom", roomModel);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        ItemRoomBinding binding;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRoomBinding.bind(itemView);
        }
    }
    public void filterList(ArrayList<RoomModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
