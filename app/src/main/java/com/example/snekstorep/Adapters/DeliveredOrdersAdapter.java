package com.example.snekstorep.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.R;
import com.example.snekstorep.models.MyCartModel;
import com.example.snekstorep.models.PurchaseHistoryModel;

import java.util.List;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersAdapter.ViewHolder> {
    private List<PurchaseHistoryModel> orders;

    public DeliveredOrdersAdapter(List<PurchaseHistoryModel> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivered_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseHistoryModel order = orders.get(position);

        holder.dateText.setText("Fecha: " + order.getDate());
        holder.totalText.setText("Total: S/ " + order.getTotalAmount());

        StringBuilder itemsText = new StringBuilder();
        for (MyCartModel item : order.getCartItems()) {
            itemsText.append(item.getProductName())
                    .append(" x")
                    .append(item.getTotalQuantity())
                    .append("\n");
        }
        holder.itemsText.setText(itemsText.toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, totalText, itemsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.order_date);
            totalText = itemView.findViewById(R.id.order_total);
            itemsText = itemView.findViewById(R.id.order_items);
        }
    }
}
