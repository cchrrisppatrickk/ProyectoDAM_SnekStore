package com.example.snekstorep.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snekstorep.Adapters.ShowAllAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.models.ProductModel;
import com.example.snekstorep.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ProductModel> productModelList;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_rec);

        // Configurar RecyclerView con GridLayout de 2 columnas
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Configurar RecyclerView con GridLayout de 1 columnas
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, productModelList);
        recyclerView.setAdapter(showAllAdapter);

        // Obtener todos los productos de Firestore
        firestore.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                ProductModel productModel = doc.toObject(ProductModel.class);
                                productModelList.add(productModel);
                            }
                            showAllAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ShowAllActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}