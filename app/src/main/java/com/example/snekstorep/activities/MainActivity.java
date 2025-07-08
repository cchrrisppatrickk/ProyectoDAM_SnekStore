package com.example.snekstorep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.snekstorep.fragments.DeliveredOrdersFragment;
import com.example.snekstorep.fragments.OrderHistoryFragment;
import com.example.snekstorep.R;
import com.example.snekstorep.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar; // Ahora es androidx.appcompat.widget.Toolbar
    private Fragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configuración de insets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupToolbar();
        setupNavigationDrawer();

        homeFragment = new HomeFragment();

        if (savedInstanceState == null) {
            loadFragment(homeFragment, R.id.home);
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar); // Asegúrate que en el XML sea androidx.appcompat.widget.Toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.Open, R.string.Close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.navy_blue));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void loadFragment(Fragment fragment, int menuItemId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, fragment);
        transaction.commit();
        navigationView.setCheckedItem(menuItemId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.home) {
            loadFragment(new HomeFragment(), id);
        } else if (id == R.id.order_history) {
            loadFragment(new OrderHistoryFragment(), id);
            Toast.makeText(this, "Historial de pedidos", Toast.LENGTH_SHORT).show();
//        } else if (id == R.id.delivered_orders) {
//            loadFragment(new DeliveredOrdersFragment(), id);
//            Toast.makeText(this, "Pedidos entregados", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.logout) {
            cerrarSesion();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}