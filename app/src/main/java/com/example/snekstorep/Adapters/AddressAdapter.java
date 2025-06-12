package com.example.snekstorep.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.R;
import com.example.snekstorep.activities.AddressActivity;
import com.example.snekstorep.models.AddressModel;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {


    Context context;
    List<AddressModel> addressModelList;
    SelectedAddress selectedAddress;
    private RadioButton selectedRadioBtn;

    public AddressAdapter(Context context, List<AddressModel> addressModelList, SelectedAddress selectedAddress) {
        this.context = context;
        this.addressModelList = addressModelList;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.address_item, parent, false));
    }


    // Modificar onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.address.setText(addressModelList.get(position).getUserAddress());
        holder.radioButton.setChecked(addressModelList.get(position).isSelected());

        holder.radioButton.setOnClickListener(v -> {
            // Desmarcar el botón de radio previamente seleccionado
            if (selectedRadioBtn != null && selectedRadioBtn != holder.radioButton) {
                selectedRadioBtn.setChecked(false);
            }

            // Actualizar selección
            for (AddressModel address : addressModelList) {
                address.setSelected(false);
            }
            addressModelList.get(position).setSelected(true);

            selectedRadioBtn = holder.radioButton;
            selectedAddress.setAddress(addressModelList.get(position).getUserAddress());
        });
    }


    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    // Agregar método para actualizar datos
    // En la clase AddressAdapter
    public void updateAddresses(List<AddressModel> newList) {
        addressModelList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address_add);
            radioButton = itemView.findViewById(R.id.select_address);
        }
    }

    public interface SelectedAddress {
        void setAddress(String address);
    }


}
