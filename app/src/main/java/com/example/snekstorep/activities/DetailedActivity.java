package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snekstorep.Adapters.SizeAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price;
    Button buyNowBtn;
    ImageButton cartIconBtn;

    // Producto actual
    ProductModel productModel = null;

    // Firebase
    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    // Tallas
    private RecyclerView sizeList;
    private SizeAdapter sizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Obtener producto de la intención
        final Object obj = getIntent().getSerializableExtra("detailed");
        if (obj instanceof ProductModel) {
            productModel = (ProductModel) obj;
        }

        // Inicializar vistas
        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        buyNowBtn = findViewById(R.id.buyNowBtn);
        cartIconBtn = findViewById(R.id.cartIconBtn);
        sizeList = findViewById(R.id.sizeList);

        // Configurar producto
        if (productModel != null) {
            Glide.with(getApplicationContext()).load(productModel.getImg_url()).into(detailedImg);
            name.setText(productModel.getTitle());
            rating.setText(productModel.getRating());
            description.setText(productModel.getDescription());
            price.setText("S/ " + String.valueOf(productModel.getPrice()));
        }

        // Configurar lista de tallas
        sizeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (productModel != null && productModel.getSize() != null && !productModel.getSize().isEmpty()) {
            sizeAdapter = new SizeAdapter(productModel.getSize(), this);
            sizeList.setAdapter(sizeAdapter);
        } else {
            // Ocultar sección de tallas si no hay
            findViewById(R.id.sizeHeaderTxt).setVisibility(View.GONE);
            sizeList.setVisibility(View.GONE);
        }

        // Configurar botón de compra (MANTIENE SU FUNCIÓN ACTUAL)
        buyNowBtn.setOnClickListener(v -> handleBuyButtonClick());

        // Configurar botón del carrito (NUEVO COMPORTAMIENTO)
        cartIconBtn.setOnClickListener(v -> {
            // Redirigir a la actividad del carrito
            startActivity(new Intent(DetailedActivity.this, CartActivity.class));
        });
    }

    private void handleBuyButtonClick() {
        if (productModel == null) return;

        // Verificar si necesita selección de talla
        if (productModel.getSize() != null && !productModel.getSize().isEmpty()) {
            String selectedSize = sizeAdapter != null ? sizeAdapter.getSelectedSize() : null;

            if (selectedSize == null) {
                Toast.makeText(this, "Por favor selecciona una talla", Toast.LENGTH_SHORT).show();
                return;
            }
            addToCart(selectedSize);
        } else {
            addToCart("N/A");
        }
    }

    private void addToCart(String size) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión primero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener fecha y hora actual
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calForDate.getTime());

        // Crear mapa de datos
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", productModel.getTitle());
        cartMap.put("productImage", productModel.getImg_url());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("productDate", saveCurrentDate);
        cartMap.put("totalQuantity", 1); // Cantidad inicial
        cartMap.put("totalPrice", productModel.getPrice()); // Precio total inicial
        cartMap.put("productSize", size);

        // Guardar en Firestore
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .add(cartMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(DetailedActivity.this, "Producto añadido", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailedActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}