package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snekstorep.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        // Inicializar vistas
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.button);
        tvLogin = findViewById(R.id.textView2);

        // Configurar listeners
        btnRegister.setOnClickListener(v -> signUp());
        tvLogin.setOnClickListener(v -> signIn());
    }

    private void signUp() {
        String userName = etName.getText().toString().trim();
        String userEmail = etEmail.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            etName.setError("Ingresa tu nombre");
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            etEmail.setError("Ingresa un correo electrónico");
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            etPassword.setError("Ingresa una contraseña");
            return;
        }

        if (userPassword.length() < 6) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        btnRegister.setEnabled(false);

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    btnRegister.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}