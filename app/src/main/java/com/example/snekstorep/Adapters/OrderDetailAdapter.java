package com.example.snekstorep.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.R;
import com.example.snekstorep.models.MyCartModel;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<MyCartModel> itemList;

    public OrderDetailAdapter(List<MyCartModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCartModel item = itemList.get(position);
        holder.nameText.setText(item.getProductName());
        holder.quantityText.setText("Cantidad: " + item.getTotalQuantity());
        holder.priceText.setText("Precio: S/ " + item.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, quantityText, priceText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_detail_name);
            quantityText = itemView.findViewById(R.id.item_detail_quantity);
            priceText = itemView.findViewById(R.id.item_detail_price);
        }
    }
}