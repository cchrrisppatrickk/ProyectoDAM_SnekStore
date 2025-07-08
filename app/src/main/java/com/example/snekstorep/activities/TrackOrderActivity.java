package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snekstorep.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrackOrderActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private TextView destinationTextView, orderStatusTextView;
    private Button confirmDeliveryButton;
    private String orderId;
    private String currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        // Inicializar TODAS las vistas
        destinationTextView = findViewById(R.id.destination);
        orderStatusTextView = findViewById(R.id.order_status); // Asegúrate de que este ID existe en tu layout
        confirmDeliveryButton = findViewById(R.id.confirmDeliveryButton);

        // Obtener datos del intent
        orderId = getIntent().getStringExtra("order_id");
        currentStatus = getIntent().getStringExtra("current_status");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Mostrar estado actual
        if (currentStatus != null) {
            orderStatusTextView.setText("Estado actual: " + currentStatus);

            // Configurar el botón según el estado actual
            if ("Entregado".equalsIgnoreCase(currentStatus)) {
                confirmDeliveryButton.setText("Pedido Entregado");
                confirmDeliveryButton.setEnabled(false);
                confirmDeliveryButton.setVisibility(View.VISIBLE);
            } else {
                // Para cualquier otro estado (incluyendo Cancelado)
                confirmDeliveryButton.setText("Marcar como Entregado");
                confirmDeliveryButton.setEnabled(true);
                confirmDeliveryButton.setVisibility(View.VISIBLE);
            }
        } else {
            confirmDeliveryButton.setVisibility(View.GONE);
        }

        confirmDeliveryButton.setOnClickListener(v -> {
            if (orderId != null) {
                updateOrderStatus();
            }
        });

        loadShippingAddress();
    }

    private void updateOrderStatus() {
        String userId = auth.getCurrentUser().getUid();

        // 1. Actualizar en PurchaseHistory
        firestore.collection("PurchaseHistory")
                .document(userId)
                .collection("UserPurchases")
                .document(orderId)
                .update("saleStatus", "Entregado")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 2. Actualizar en MyOrder (si existe)
                        firestore.collection("CurrentUser")
                                .document(userId)
                                .collection("MyOrder")
                                .document(orderId)
                                .update("saleStatus", "Entregado")
                                .addOnCompleteListener(task2 -> {
                                    Toast.makeText(this, "Estado actualizado a Entregado", Toast.LENGTH_SHORT).show();

                                    // Devolver resultado a OrderHistoryFragment
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("updated_order_id", orderId);

                                    // Añadir información adicional para optimizar la actualización
                                    resultIntent.putExtra("new_status", "Entregado");
                                    resultIntent.putExtra("position", -1); // Por si necesitas posición específica

                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                });
                    } else {
                        Toast.makeText(this, "Error al actualizar estado", Toast.LENGTH_SHORT).show();

                        // Notificar error
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
    }

    private void loadShippingAddress() {
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Address")
                .whereEqualTo("isSelected", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String address = document.getString("userAddress");

                        if (address != null && !address.isEmpty()) {
                            destinationTextView.setText("Destino: " + address);
                        } else {
                            destinationTextView.setText("Dirección no especificada");
                        }
                    } else {
                        destinationTextView.setText("No se encontró dirección de envío");
                        Toast.makeText(this, "No se encontró dirección seleccionada", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}