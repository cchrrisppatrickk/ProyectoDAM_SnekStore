package com.example.snekstorep.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.R;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {
    private ArrayList<String> sizes;
    private int selectedPosition = -1;
    private Context context;

    public SizeAdapter(ArrayList<String> sizes, Context context) {
        this.sizes = sizes;
        this.context = context;
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        holder.sizeText.setText(sizes.get(position));

        // Actualizar el fondo según la selección
        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.size_selected_bg);
            holder.sizeText.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.size_unselected_bg);
            holder.sizeText.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged(); // Esto fuerza a redibujar todos los items
        });
    }

    @Override
    public int getItemCount() {
        return sizes != null ? sizes.size() : 0;
    }

    public String getSelectedSize() {
        return (selectedPosition != -1) ? sizes.get(selectedPosition) : null;
    }

    class SizeViewHolder extends RecyclerView.ViewHolder {
        TextView sizeText;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeText = itemView.findViewById(R.id.sizeText);
            itemView.setOnClickListener(v -> {
                selectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}