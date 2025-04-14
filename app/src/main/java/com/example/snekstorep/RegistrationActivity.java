package com.example.snekstorep;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();


        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

    }


    public void registrarse(View view){

        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Ingrese Su Nombre!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this,"Ingrese Su Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Ingrese su Contraseña!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length()<6){

            Toast.makeText(this,"Contraseña muy corta, ingrese minimo 6 caracteres",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));



    }

    public void iniciarsesion(View view){
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));


    }
}