package com.example.snekstorep.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.Adapters.MyCartAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    TextView totalPrice;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Inicializar Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.cartRecyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);

        // Configurar listener para eliminar items
        cartAdapter.setOnDeleteClickListener(position -> {
            MyCartModel itemToDelete = cartModelList.get(position);
            deleteItemFromFirestore(itemToDelete, position);
        });


        // Configurar listener para eliminar items
        cartAdapter.setOnDeleteClickListener(position -> {
            MyCartModel itemToDelete = cartModelList.get(position);
            deleteItemFromFirestore(itemToDelete, position);
        });


        // Obtener productos del carrito (ACTUALIZADO para guardar documentId)
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            MyCartModel cartModel = document.toObject(MyCartModel.class);
                            if (cartModel != null) {
                                cartModel.setDocumentId(document.getId()); // GUARDAR ID DEL DOCUMENTO
                                cartModelList.add(cartModel);
                            }
                        }
                        cartAdapter.notifyDataSetChanged();
                        calculateTotal(cartModelList);
                    }
                });
    }

    private void deleteItemFromFirestore(MyCartModel item, int position) {
        if (item.getDocumentId() != null) {
            firestore.collection("AddToCart")
                    .document(auth.getCurrentUser().getUid())
                    .collection("User")
                    .document(item.getDocumentId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Eliminar de la lista local
                            cartModelList.remove(position);
                            cartAdapter.notifyItemRemoved(position);
                            calculateTotal(cartModelList);
                            Toast.makeText(CartActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CartActivity.this, "Error al eliminar: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Error: ID de documento no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateTotal(List<MyCartModel> cartModelList) {
        double total = 0.0;
        for(MyCartModel model : cartModelList) {
            total += model.getTotalPrice();
        }
        totalPrice.setText("S/ " + total);
    }
}