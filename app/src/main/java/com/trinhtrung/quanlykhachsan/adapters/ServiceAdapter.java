package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.EditServiceActivity;
import com.trinhtrung.quanlykhachsan.activities.EditStaffActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemServiceBinding;
import com.trinhtrung.quanlykhachsan.models.ServiceModel;
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    Context context;
    List<ServiceModel> list;

    public ServiceAdapter(Context context, List<ServiceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel serviceModel = list.get(position);
        holder.binding.tvServiceMaDV.setText(serviceModel.getMaDV());
        holder.binding.tvServiceTenDV.setText(serviceModel.getTenDV());
        holder.binding.tvServiceGiaDV.setText(String.valueOf(serviceModel.getGiaDV()));
        holder.binding.tvServiceMaNV.setText(serviceModel.getMaNV());

        holder.binding.itemService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditServiceActivity.class);
                intent.putExtra("dataService", serviceModel);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        ItemServiceBinding binding;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemServiceBinding.bind(itemView);
        }
    }

    public void filterList(ArrayList<ServiceModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}
