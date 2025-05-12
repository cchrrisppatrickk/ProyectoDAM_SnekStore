package com.example.snekstorep.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snekstorep.Adapters.SizeAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.models.ProductModel;
import com.example.snekstorep.models.ShowAllModel;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;

    //New Products
    ProductModel productModel = null;


    //Show Alll
    ShowAllModel showAllModel  = null;

    //SIze
    private RecyclerView sizeList;
    private SizeAdapter sizeAdapter;


    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        firestore = FirebaseFirestore.getInstance();

        final Object obj = getIntent().getSerializableExtra( "detailed");

        if (obj instanceof ProductModel){
            productModel = (ProductModel) obj;
        }else if (obj instanceof ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);

//        addToCart = findViewById(R.id.add_to_cart);
//        buyNow = findViewById(R.id.buy_now);
//
//        addItems = findViewById(R.id.add_item);
//        removeItems = findViewById(R.id.remove_item);

        // New Products
        if (productModel != null) {
            Glide.with(getApplicationContext()).load(productModel.getImg_url()).into(detailedImg);
            name.setText(productModel.getTitle());
            rating.setText(productModel.getRating());
            description.setText(productModel.getDescription());
            price.setText("S/ " + String.valueOf(productModel.getPrice()));
            name.setText(productModel.getTitle());
        }
        // Mostrar todos los productos
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());
        }


        sizeList = findViewById(R.id.sizeList);
        sizeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (productModel != null && productModel.getSize() != null) {
            sizeAdapter = new SizeAdapter(productModel.getSize(), this);
            sizeList.setAdapter(sizeAdapter);

            // Opcional: Manejar selección de talla
            Button buyNow = findViewById(R.id.addToCartBtn);
            buyNow.setOnClickListener(v -> {
                String selectedSize = sizeAdapter.getSelectedSize();
                if (selectedSize != null) {
                    // lógica cuando hay talla seleccionada
                    Toast.makeText(this, "Talla seleccionada: " + selectedSize, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Selecciona una talla", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no hay tallas, ocultar la sección
            findViewById(R.id.sizeHeaderTxt).setVisibility(View.GONE);
            sizeList.setVisibility(View.GONE);
        }

    }




}