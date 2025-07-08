package com.example.snekstorep.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.R;
import com.example.snekstorep.models.MyCartModel;
import com.example.snekstorep.models.PurchaseHistoryModel;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<PurchaseHistoryModel> purchaseList;
    private OnTrackOrderClickListener trackOrderClickListener;





    private OnDetailClickListener detailClickListener;

    // Nueva interfaz para el clic de detalle
    // Interface para el click
    public interface OnDetailClickListener {
        void onDetailClick(PurchaseHistoryModel purchase);
    }
    // Método para asignar el listener
    public void setOnDetailClickListener(OnDetailClickListener listener) {
        this.detailClickListener = listener;
    }
    public interface OnTrackOrderClickListener {
        void onTrackOrderClick(PurchaseHistoryModel purchase);
    }

    public OrderHistoryAdapter(List<PurchaseHistoryModel> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public void setOnTrackOrderClickListener(OnTrackOrderClickListener listener) {
        this.trackOrderClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseHistoryModel purchase = purchaseList.get(position);

        // Configurar vistas...
        holder.dateText.setText("Fecha: " + purchase.getDate());
        holder.totalText.setText("Total: S/ " + purchase.getTotalAmount());
        holder.statusText.setText("Estado: " + purchase.getSaleStatus());

        // Configurar color según estado...
        int statusColor;
        switch (purchase.getSaleStatus().toLowerCase()) {
            case "cancelado":
                statusColor = Color.GREEN;
                break;
            case "entregado":
                statusColor = Color.RED;
                break;
            default:
                statusColor = Color.GRAY;
        }
        holder.statusText.setTextColor(statusColor);

        // Configurar items...
        StringBuilder itemsText = new StringBuilder();
        for (MyCartModel item : purchase.getCartItems()) {
            itemsText.append(item.getProductName())
                    .append(" x")
                    .append(item.getTotalQuantity())
                    .append("\n");
        }
        holder.itemsText.setText(itemsText.toString());

        // Configurar botón (solo una vez)
        holder.trackButton.setOnClickListener(v -> {
            if (trackOrderClickListener != null) {
                trackOrderClickListener.onTrackOrderClick(purchase);
            }
        });

        // Controlar visibilidad de botones según el estado
        if ("Entregado".equalsIgnoreCase(purchase.getSaleStatus())) {
            holder.trackButton.setVisibility(View.GONE);      // Ocultar Rastrear
            holder.viewDetailButton.setVisibility(View.VISIBLE); // Mostrar Ver Detalle
        } else {
            holder.trackButton.setVisibility(View.VISIBLE);   // Mostrar Rastrear
            holder.viewDetailButton.setVisibility(View.GONE);   // Ocultar Ver Detalle
        }


        // Listener para botón de rastreo (existente)
        holder.trackButton.setOnClickListener(v -> {
            if (trackOrderClickListener != null) {
                trackOrderClickListener.onTrackOrderClick(purchase);
            }
        });

        // Nuevo listener para botón de Ver Detalle
        holder.viewDetailButton.setOnClickListener(v -> {
            if (detailClickListener != null) {
                detailClickListener.onDetailClick(purchase);
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, totalText, itemsText, statusText;
        Button trackButton, viewDetailButton; // Nuevo campo

        Button btnDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.order_date);
            totalText = itemView.findViewById(R.id.order_total);
            itemsText = itemView.findViewById(R.id.order_items);
            statusText = itemView.findViewById(R.id.order_status);
            trackButton = itemView.findViewById(R.id.track_button);
            viewDetailButton = itemView.findViewById(R.id.view_detail_btn); // Referencia al nuevo botón


        }
    }
}