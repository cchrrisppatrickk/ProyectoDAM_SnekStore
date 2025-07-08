package com.example.snekstorep.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.snekstorep.Adapters.OrderHistoryAdapter;
import com.example.snekstorep.R;
import com.example.snekstorep.activities.TrackOrderActivity;
import com.example.snekstorep.models.PurchaseHistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment implements OrderHistoryAdapter.OnTrackOrderClickListener {
    private FirebaseFirestore firestore;

    private RecyclerView orderHistoryRecycler;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<PurchaseHistoryModel> purchaseList = new ArrayList<>();

    private static final int TRACK_ORDER_REQUEST_CODE = 1001; // Añade esta línea

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        // Inicializa Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Configura RecyclerView
        recyclerView = view.findViewById(R.id.order_history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa la lista y el adaptador
        purchaseList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(purchaseList);
        adapter.setOnTrackOrderClickListener(this); // Establece el listener
        recyclerView.setAdapter(adapter);

        // Carga los datos
        loadPurchaseHistory();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPurchaseHistory(); // Recargar datos cada vez que el fragment se vuelva visible
    }

    @Override
    public void onTrackOrderClick(PurchaseHistoryModel purchase) {
        Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
        intent.putExtra("order_id", purchase.getDocumentId());
        intent.putExtra("current_status", purchase.getSaleStatus());
        startActivityForResult(intent, TRACK_ORDER_REQUEST_CODE);
    }

    private void loadPurchaseHistory() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("PurchaseHistory")
                .document(userId)
                .collection("UserPurchases")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        purchaseList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
                            PurchaseHistoryModel purchase = doc.toObject(PurchaseHistoryModel.class);
                            if (purchase != null) {
                                purchase.setDocumentId(doc.getId()); // Establece el ID del documento
                                purchaseList.add(purchase);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (purchaseList.isEmpty()) {
                            showEmptyState(true); // Muestra mensaje si no hay compras
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al cargar historial: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyState(boolean show) {
        // Implementa un TextView o ImageView para mostrar "No hay compras registradas"
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TRACK_ORDER_REQUEST_CODE && resultCode == RESULT_OK) {
            String updatedOrderId = data.getStringExtra("updated_order_id");

            // Actualizar solo el pedido modificado
            for (int i = 0; i < purchaseList.size(); i++) {
                PurchaseHistoryModel order = purchaseList.get(i);
                if (order.getDocumentId().equals(updatedOrderId)) {
                    order.setSaleStatus("Entregado"); // Actualizar estado localmente
                    adapter.notifyItemChanged(i); // Actualizar solo este ítem
                    break;
                }
            }
        }
    }
}