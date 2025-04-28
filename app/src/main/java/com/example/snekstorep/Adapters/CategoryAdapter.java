package com.example.snekstorep.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snekstorep.R;
import com.example.snekstorep.databinding.CategoryListBinding;
import com.example.snekstorep.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<CategoryModel> list;
    private int selectedPosition = -1; // Para controlar el ítem seleccionado

    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryListBinding binding = CategoryListBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel category = list.get(position);

        // Cargar imagen
        Glide.with(context)
                .load(category.getPicUrl())
                .into(holder.binding.catImg);

        holder.binding.catName.setText(category.getTitle());

        // Manejar selección
        if (selectedPosition == position) {
            // Ítem seleccionado
            holder.binding.catImg.setBackgroundResource(R.drawable.purple_bg);
            ImageViewCompat.setImageTintList(
                    holder.binding.catImg,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            );
            holder.binding.catName.setVisibility(View.VISIBLE);
            holder.binding.catName.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            // Ítem no seleccionado
            holder.binding.catImg.setBackgroundResource(R.drawable.grey_bg);
            ImageViewCompat.setImageTintList(
                    holder.binding.catImg,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
            );
            holder.binding.catName.setVisibility(View.GONE);
        }

        // Evento click
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastSelectedPosition = selectedPosition;
                selectedPosition = position;

                if (lastSelectedPosition != -1) {
                    notifyItemChanged(lastSelectedPosition);
                }
                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CategoryListBinding binding;

        public ViewHolder(CategoryListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
