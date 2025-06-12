package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    // Componentes de UI
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private Button checkoutBtn;

    // Adaptador y lista de datos
    private MyCartAdapter cartAdapter;
    private List<MyCartModel> cartModelList;

    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Inicialización de Firebase
        initializeFirebase();

        // Configuración de vistas
        setupViews();

        // Configuración del RecyclerView
        setupRecyclerView();

        // Cargar datos del carrito
        loadCartItems();

        // Configurar listeners
        setupListeners();
    }

    /**
     * Inicializa los servicios de Firebase
     */
    private void initializeFirebase() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Configura las vistas principales
     */
    private void setupViews() {
        recyclerView = findViewById(R.id.cartRecyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        checkoutBtn = findViewById(R.id.cartActivityCheckoutBtn);
    }

    /**
     * Configura el RecyclerView y su adaptador
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);
    }

    /**
     * Configura los listeners de interacción
     */
    private void setupListeners() {
        // Listener para el botón de checkout
        checkoutBtn.setOnClickListener(v -> {
            if (cartModelList.isEmpty()) {
                showToast("Tu carrito está vacío");
            } else {
                checkExistingAddresses();
            }
        });

        // Listener para eliminar items del carrito
        cartAdapter.setOnDeleteClickListener(position -> {
            MyCartModel itemToDelete = cartModelList.get(position);
            deleteItemFromFirestore(itemToDelete, position);
        });
    }

    /**
     * Carga los items del carrito desde Firestore
     */
    private void loadCartItems() {
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        processCartItems(task);
                    } else {
                        showToast("Error al cargar el carrito");
                    }
                });
    }

    /**
     * Procesa los items del carrito obtenidos de Firestore
     */
    private void processCartItems(@NonNull Task<QuerySnapshot> task) {
        cartModelList.clear();
        for (DocumentSnapshot document : task.getResult().getDocuments()) {
            MyCartModel cartModel = document.toObject(MyCartModel.class);
            if (cartModel != null) {
                cartModel.setDocumentId(document.getId());
                cartModelList.add(cartModel);
            }
        }
        cartAdapter.notifyDataSetChanged();
        calculateTotal(cartModelList);
    }

    /**
     * Elimina un item del carrito en Firestore
     */
    private void deleteItemFromFirestore(MyCartModel item, int position) {
        if (item.getDocumentId() == null) {
            showToast("Error: ID de documento no encontrado");
            return;
        }

        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .document(item.getDocumentId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        removeItemFromList(position);
                    } else {
                        showToast("Error al eliminar: " + task.getException());
                    }
                });
    }

    /**
     * Elimina un item de la lista local y actualiza la UI
     */
    private void removeItemFromList(int position) {
        cartModelList.remove(position);
        cartAdapter.notifyItemRemoved(position);
        calculateTotal(cartModelList);
        showToast("Producto eliminado");
    }

    /**
     * Calcula el total del carrito y actualiza la vista
     */
    private double calculateTotal(List<MyCartModel> cartModelList) {
        double total = 0.0;
        for (MyCartModel model : cartModelList) {
            total += model.getTotalPrice();
        }
        totalPrice.setText(String.format("S/ %.2f", total));
        return total;
    }

    /**
     * Verifica si el usuario tiene direcciones guardadas
     */
    private void checkExistingAddresses() {
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Address")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        navigateBasedOnAddresses(task);
                    } else {
                        showToast("Error al verificar direcciones");
                    }
                });
    }

    /**
     * Navega a la pantalla correspondiente según si hay direcciones
     */
    private void navigateBasedOnAddresses(@NonNull Task<QuerySnapshot> task) {
        double totalAmount = calculateTotal(cartModelList);
        Intent intent;

        if (!task.getResult().isEmpty()) {
            // Hay direcciones existentes - ir a selección
            intent = new Intent(this, AddressActivity.class);
        } else {
            // No hay direcciones - ir a creación
            intent = new Intent(this, AddAddressActivity.class);
        }

        intent.putExtra("totalAmount", totalAmount);
        startActivity(intent);
    }

    /**
     * Muestra un Toast con el mensaje especificado
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}