package com.trinhtrung.quanlykhachsan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.activities.BillServiceDetailActivity;
import com.trinhtrung.quanlykhachsan.databinding.ItemPaymentBinding;
import com.trinhtrung.quanlykhachsan.models.BillServiceModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentInvoiceAdapter extends RecyclerView.Adapter<PaymentInvoiceAdapter.PaymentInvoiceViewHolder> {
    Context context;
    List<BillServiceModel> list;

    public PaymentInvoiceAdapter(Context context, List<BillServiceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentInvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentInvoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentInvoiceViewHolder holder, int position) {
        BillServiceModel model = list.get(position);
        holder.binding.txtPaymentMahd.setText(model.getMaHD());
        holder.binding.txtPaymentTenhd.setText(model.getTenHD());
        holder.binding.txtPaymentMadv.setText(model.getMaDV());
        holder.binding.txtPaymentMakh.setText(model.getMaKH());
        holder.binding.txtPaymentNgaylap.setText(model.getNgayLapHD());
        holder.binding.itemPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillServiceDetailActivity.class);
                intent.putExtra("dataBillService",model);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentInvoiceViewHolder extends RecyclerView.ViewHolder {

        ItemPaymentBinding binding;
        public PaymentInvoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemPaymentBinding.bind(itemView);
        }
    }

    public void filterList(ArrayList<BillServiceModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
