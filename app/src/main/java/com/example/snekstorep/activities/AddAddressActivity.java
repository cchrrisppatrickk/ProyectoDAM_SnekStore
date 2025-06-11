package com.example.snekstorep.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.snekstorep.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    EditText name, address, city, postalCode, phoneNumber;
    Toolbar toolbar;
    Button addAddressBtn;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

//        // Configurar toolbar
//        Toolbar toolbar = findViewById(R.id.add_address_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Inicializar vistas
        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        phoneNumber = findViewById(R.id.ad_phone);
        postalCode = findViewById(R.id.ad_code);
        addAddressBtn = findViewById(R.id.ad_add_address); // ¡IMPORTANTE! Inicializar el botón

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();

                // Construir dirección formateada
                StringBuilder direccionFinal = new StringBuilder();
                if (!userName.isEmpty()) direccionFinal.append(userName).append("\n");
                if (!userAddress.isEmpty()) direccionFinal.append(userAddress).append("\n");
                if (!userCity.isEmpty()) direccionFinal.append(userCity).append(", ");
                if (!userCode.isEmpty()) direccionFinal.append(userCode).append("\n");
                if (!userNumber.isEmpty()) direccionFinal.append("Tel: ").append(userNumber);

                // Validar campos
                if (userName.isEmpty() || userAddress.isEmpty() ||
                        userCity.isEmpty() || userCode.isEmpty() || userNumber.isEmpty()) {

                    Toast.makeText(AddAddressActivity.this,
                            "Por favor complete todos los campos",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Crear objeto para Firestore
                    Map<String, Object> direccionMap = new HashMap<>();
                    direccionMap.put("userAddress", direccionFinal.toString());
                    direccionMap.put("isSelected", false); // Campo requerido

                    // Guardar en Firestore
                    firestore.collection("CurrentUser")
                            .document(auth.getCurrentUser().getUid())
                            .collection("Address")
                            .add(direccionMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddAddressActivity.this,
                                                "Dirección agregada!",
                                                Toast.LENGTH_SHORT).show();
                                        finish(); // Cerrar actividad
                                    } else {
                                        Toast.makeText(AddAddressActivity.this,
                                                "Error: " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}