package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.EditCustomerActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemCustomerBinding;
import com.trinhtrung.quanlykhachsan.models.CustomerModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    Context context;
    List<CustomerModel> list;

    public CustomerAdapter(Context context, List<CustomerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer,parent,false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        CustomerModel customerModel = list.get(position);
      holder.binding.tvCustomerMaKH.setText(customerModel.getMaKH());
      String hoTenKH = customerModel.getHoKH() + " "+ customerModel.getTenKH();
      holder.binding.tvCustomerHoTen.setText(hoTenKH);

        holder.binding.itemCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditCustomerActivity.class);
                intent.putExtra("dataCustomer", customerModel);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        ItemCustomerBinding binding;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCustomerBinding.bind(itemView);
        }
    }
    public void filterList(ArrayList<CustomerModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
