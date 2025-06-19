package com.example.snekstorep.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snekstorep.R;
import com.example.snekstorep.models.MyCartModel;
import com.example.snekstorep.models.PurchaseHistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountTv;
    private Button payBtn;
    private double totalAmount;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private String purchaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        totalAmountTv = findViewById(R.id.total_amt);
        payBtn = findViewById(R.id.pay_btn);

        totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        totalAmountTv.setText(String.format("S/ %.2f", totalAmount));

        payBtn.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        Toast.makeText(this, "Procesando pago...", Toast.LENGTH_SHORT).show();

        // Primero obtenemos los items del carrito
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<MyCartModel> cartItems = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            MyCartModel cartModel = document.toObject(MyCartModel.class);
                            if (cartModel != null) {
                                cartModel.setDocumentId(document.getId());
                                cartItems.add(cartModel);
                            }
                        }

                        // Guardamos el historial antes de limpiar el carrito
                        savePurchaseHistory(cartItems);
                    } else {
                        Toast.makeText(this, "Error al obtener items del carrito", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void savePurchaseHistory(List<MyCartModel> cartItems) {
        // Generar ID único para la compra
        String purchaseId = firestore.collection("PurchaseHistory").document().getId();

        // Obtener fecha actual
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        // Crear objeto de historial
        PurchaseHistoryModel purchase = new PurchaseHistoryModel(
                purchaseId,
                auth.getCurrentUser().getUid(),
                currentDate,
                totalAmount,
                cartItems,
                "completado" // Estado por defecto al crear la compra
        );

        // Guardar en Firestore
        firestore.collection("PurchaseHistory")
                .document(auth.getCurrentUser().getUid())
                .collection("UserPurchases")
                .document(purchaseId)
                .set(purchase)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Una vez guardado el historial, limpiar el carrito
                        clearCart();
                        showSuccessDialog();
                    } else {
                        Toast.makeText(this, "Error al guardar historial de compra", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearCart() {
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            document.getReference().delete();
                        }
                    } else {
                        Toast.makeText(this, "Error al limpiar el carrito", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_purchase_success, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CircularDialogTheme);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.7f);
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            window.getDecorView().setPadding(0, 0, 0, 0);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    280,
                    getResources().getDisplayMetrics()
            );
            layoutParams.height = layoutParams.width;
            window.setAttributes(layoutParams);
        }

        dialog.show();

        Button btnContinueShopping = dialogView.findViewById(R.id.btn_continue_shopping);
        btnContinueShopping.setOnClickListener(v -> {

            // Cerrar actividades anteriores
            Intent mainIntent = new Intent(PaymentActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);

            dialog.dismiss();
            finish();
        });
    }
}