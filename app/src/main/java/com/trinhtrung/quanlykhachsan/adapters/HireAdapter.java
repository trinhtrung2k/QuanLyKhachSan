package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.EditHireActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemHireBinding;
import com.trinhtrung.quanlykhachsan.models.CustomerModel;
import com.trinhtrung.quanlykhachsan.models.HireModel;

import java.util.ArrayList;
import java.util.List;

public class HireAdapter extends RecyclerView.Adapter<HireAdapter.HireViewHolder> {
    Context context;
    List<HireModel> list;

    public HireAdapter(Context context, List<HireModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HireViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hire,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HireViewHolder holder, int position) {
        HireModel hireModel = list.get(position);
        holder.binding.tvHireMaDK.setText(hireModel.getMaDK());
        holder.binding.tvHireMaKH.setText(hireModel.getMaKH());
        holder.binding.tvHireSoPhong.setText(String.valueOf(hireModel.getSoPhong()));

        holder.binding.itemHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditHireActivity.class);
                intent.putExtra("dataHire", hireModel);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HireViewHolder extends RecyclerView.ViewHolder {
        ItemHireBinding binding;
        public HireViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHireBinding.bind(itemView);
        }
    }
    public void filterList(ArrayList<HireModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}
