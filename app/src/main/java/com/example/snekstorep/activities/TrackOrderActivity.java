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
    private TextView destinationTextView;
    private Button confirmDeliveryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        // Inicializar Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Obtener referencias de las vistas
        destinationTextView = findViewById(R.id.destination);
        confirmDeliveryButton = findViewById(R.id.confirmDeliveryButton);

        // Configurar el listener del botón
        confirmDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para volver a la actividad principal
                Intent intent = new Intent(TrackOrderActivity.this, MainActivity.class);

                // Limpiar la pila de actividades para que MainActivity sea la única
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // Iniciar la actividad
                startActivity(intent);

                // Finalizar la actividad actual
                finish();
            }
        });

        // Obtener y mostrar la dirección
        loadShippingAddress();

        // También puedes obtener el estado del envío desde Firestore
        // y actualizar los círculos de progreso según corresponda
    }

    private void loadShippingAddress() {
        // 1. Obtener la dirección seleccionada (la marcada como isSelected = true)
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Address")
                .whereEqualTo("isSelected", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Tomar la primera dirección seleccionada
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String address = document.getString("userAddress");

                        // Mostrar la dirección en el TextView
                        if (address != null && !address.isEmpty()) {
                            destinationTextView.setText("Destino: " + address);
                        } else {
                            destinationTextView.setText("Dirección no especificada");
                        }
                    } else {
                        // Si no hay dirección seleccionada, mostrar la última usada o mensaje
                        destinationTextView.setText("No se encontró dirección de envío");
                        Toast.makeText(this, "No se encontró dirección seleccionada", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}