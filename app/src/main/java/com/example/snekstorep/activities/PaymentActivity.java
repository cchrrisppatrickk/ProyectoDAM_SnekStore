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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountTv;
    private Button payBtn;
    private double totalAmount;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Inicializar Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Obtener referencias de vistas
        totalAmountTv = findViewById(R.id.total_amt);
        payBtn = findViewById(R.id.pay_btn);

        // Obtener el total del intent
        totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        totalAmountTv.setText(String.format("S/ %.2f", totalAmount));

        // Configurar botón de pago
        payBtn.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        // Simular procesamiento de pago
        Toast.makeText(this, "Procesando pago...", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Limpiar el carrito después del pago
            clearCart();

            // Mostrar diálogo de éxito
            showSuccessDialog();
        }, 1500);
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
                        // Ya no llamamos a finish() aquí
                    } else {
                        Toast.makeText(this, "Error al limpiar el carrito", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_purchase_success, null);

        // Usa el tema que definimos
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CircularDialogTheme);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        // Configuración crucial
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.7f);

            // Eliminar márgenes internos del diálogo
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            window.getDecorView().setPadding(0, 0, 0, 0);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    280,
                    getResources().getDisplayMetrics()
            );
            layoutParams.height = layoutParams.width; // Mismo valor para mantener círculo
            window.setAttributes(layoutParams);
        }

        dialog.show();

        Button btnContinueShopping = dialogView.findViewById(R.id.btn_continue_shopping);
        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            dialog.dismiss();
            finish();
        });
    }
}