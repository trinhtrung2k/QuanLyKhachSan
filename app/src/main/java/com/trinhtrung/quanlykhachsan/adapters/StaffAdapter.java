package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.EditStaffActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemStaffBinding;
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    Context context;
    List<StaffModel> list;

    public StaffAdapter(Context context, List<StaffModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StaffViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        StaffModel staffModel = list.get(position);
        holder.binding.tvStaffMaNV.setText(staffModel.getMaNV());
        holder.binding.tvStaffGioitinh.setText(staffModel.getGioiTinh());
        String hoTenNV = staffModel.getHoNV() + " "+ staffModel.getTenNV();
        holder.binding.tvStaffHotenNV.setText(hoTenNV);

        holder.binding.itemStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditStaffActivity.class);
                intent.putExtra("dataStaff", staffModel);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder {
        ItemStaffBinding binding;
        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStaffBinding.bind(itemView);
        }
    }
    public void filterList(ArrayList<StaffModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }


}
