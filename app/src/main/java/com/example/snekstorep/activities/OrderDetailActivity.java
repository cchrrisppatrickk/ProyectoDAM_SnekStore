package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.Adapters.OrderDetailAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.models.PurchaseHistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView dateText, totalText, statusText;
    private RecyclerView itemsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Inicializar vistas
        dateText = findViewById(R.id.order_detail_date);
        totalText = findViewById(R.id.order_detail_total);
        statusText = findViewById(R.id.order_detail_status);
        itemsRecycler = findViewById(R.id.order_detail_items);

        // Configurar RecyclerView
        itemsRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Obtener datos del intent
        Intent intent = getIntent();
        if(intent != null) {
            dateText.setText("Fecha: " + intent.getStringExtra("order_date"));
            totalText.setText("Total: S/ " + intent.getDoubleExtra("order_total", 0.0));
            statusText.setText("Estado: " + intent.getStringExtra("order_status"));

            // Aquí deberías cargar los ítems del pedido usando el order_id
            String orderId = intent.getStringExtra("order_id");
            loadOrderItems(orderId);
            if (orderId == null || orderId.isEmpty()) {
                Toast.makeText(this, "Error: ID de pedido inválido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }

    private void loadOrderItems(String orderId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("PurchaseHistory")
                .document(userId)
                .collection("UserPurchases")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    PurchaseHistoryModel order = documentSnapshot.toObject(PurchaseHistoryModel.class);
                    if (order != null && order.getCartItems() != null) {
                        OrderDetailAdapter adapter = new OrderDetailAdapter(order.getCartItems());
                        itemsRecycler.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar los ítems", Toast.LENGTH_SHORT).show();
                });
    }
}