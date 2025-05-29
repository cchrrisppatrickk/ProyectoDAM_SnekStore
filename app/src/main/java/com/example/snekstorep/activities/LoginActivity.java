package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snekstorep.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        // >>> SOLO PARA PRUEBAS - FORZAR LOGOUT AL INICIAR <<<
//        FirebaseAuth.getInstance().signOut();
//        Log.d("DEBUG_AUTH", "Sesión cerrada forzadamente para pruebas");
//       // >>> ELIMINAR ESTO EN PRODUCCIÓN <<<

        auth = FirebaseAuth.getInstance();

        // Verificar si el usuario ya está autenticado
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Log.d("DEBUG_AUTH", "Usuario ya autenticado: " + currentUser.getEmail());
            goToMainActivity();
            return;
        } else {
            Log.d("DEBUG_AUTH", "No hay usuario autenticado");
        }

        // Inicializar vistas
        etEmail = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnIngresar);
        tvRegister = findViewById(R.id.Lblregistrar);

        // Configurar listeners
        btnLogin.setOnClickListener(v -> signIn());
        tvRegister.setOnClickListener(v -> signUp());
    }

    private void signIn() {
        String userEmail = etEmail.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();

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

        btnLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {
                        Log.d("DEBUG_AUTH", "Inicio de sesión exitoso");
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Log.e("DEBUG_AUTH", "Error en login", task.getException());
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}