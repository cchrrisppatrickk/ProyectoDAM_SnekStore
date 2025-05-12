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
    private int selectedPosition = -1;

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
            // Ítem seleccionado - mostrar nombre y cambiar estilo
            holder.binding.getRoot().setBackgroundResource(R.drawable.purple_bg);
            ImageViewCompat.setImageTintList(
                    holder.binding.catImg,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            );
            holder.binding.catName.setVisibility(View.VISIBLE);
            holder.binding.catName.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            // Ítem no seleccionado - ocultar nombre y estilo normal
            holder.binding.getRoot().setBackgroundResource(R.drawable.grey_bg);
            ImageViewCompat.setImageTintList(
                    holder.binding.catImg,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
            );
            holder.binding.catName.setVisibility(View.GONE);
        }

        // Evento click
        holder.binding.getRoot().setOnClickListener(v -> {
            int lastSelected = selectedPosition;
            selectedPosition = position;

            // Notificar cambios solo en los items afectados
            if (lastSelected != -1) {
                notifyItemChanged(lastSelected);
            }
            notifyItemChanged(selectedPosition);
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

    // Método para obtener la categoría seleccionada
    public CategoryModel getSelectedCategory() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }
}