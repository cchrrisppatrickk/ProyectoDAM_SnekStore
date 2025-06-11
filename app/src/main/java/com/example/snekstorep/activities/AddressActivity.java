package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.Adapters.AddressAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.models.AddressModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    // Elementos UI
    private RecyclerView recyclerView;
    private Button addAddressBtn, paymentBtn;
    private Toolbar toolbar; // (Comentado actualmente)

    // Datos y adaptador
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;

    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    // Estado de selección
    private String mAddress = ""; // Almacena la dirección seleccionada

    // Constante para solicitud de actividad
    private static final int REQUEST_CODE_ADD_ADDRESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilita diseño edge-to-edge
        setContentView(R.layout.activity_address);

        // Inicialización de Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Vinculación de vistas
        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        addAddressBtn = findViewById(R.id.add_address_btn);

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, addressModelList, this);
        recyclerView.setAdapter(addressAdapter);

        // Listener para botón de agregar dirección
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad de agregar dirección esperando resultado
                startActivityForResult(
                        new Intent(AddressActivity.this, AddAddressActivity.class),
                        REQUEST_CODE_ADD_ADDRESS
                );
            }
        });

        // Listener para botón de pago (DESCOMENTADO Y MEJORADO)
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddress.isEmpty()) {
                    Toast.makeText(AddressActivity.this,
                            "Selecciona una dirección primero",
                            Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(AddressActivity.this, PaymentActivity.class));
                }
            }
        });

        // Carga inicial de direcciones
        loadAddresses();
    }

    /**
     * Implementación de la interfaz del adaptador.
     * Actualiza la dirección seleccionada cuando el usuario selecciona un ítem.
     * @param address Dirección seleccionada por el usuario
     */
    @Override
    public void setAddress(String address) {
        mAddress = address;
    }

    /**
     * Maneja resultados de actividades hijas.
     * Recarga las direcciones cuando se agrega una nueva exitosamente.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK) {
            loadAddresses(); // Recargar lista después de agregar
        }
    }

    /**
     * Carga las direcciones del usuario desde Firestore.
     * Limpia la lista actual, obtiene los documentos y actualiza el adaptador.
     */
    private void loadAddresses() {
        // Limpiar lista existente
        addressModelList.clear();

        // Consulta a Firestore
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Procesar cada documento
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                if (addressModel != null) {
                                    // Guardar ID del documento para operaciones futuras
                                    addressModel.setDocumentId(doc.getId());
                                    addressModelList.add(addressModel);
                                }
                            }
                            // Notificar al adaptador que los datos cambiaron
                            addressAdapter.notifyDataSetChanged();
                        } else {
                            // Manejar error
                            Toast.makeText(AddressActivity.this,
                                    "Error al cargar direcciones: " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}