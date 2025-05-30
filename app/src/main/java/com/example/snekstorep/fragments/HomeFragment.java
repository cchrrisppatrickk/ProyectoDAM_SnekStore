    package com.example.snekstorep.fragments;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.denzcoskun.imageslider.ImageSlider;
    import com.denzcoskun.imageslider.constants.ScaleTypes;
    import com.denzcoskun.imageslider.models.SlideModel;
    import com.example.snekstorep.Adapters.CategoryAdapter;
    import com.example.snekstorep.Adapters.NewProductAdapter;
    import com.example.snekstorep.R;
    import com.example.snekstorep.activities.CartActivity;
    import com.example.snekstorep.activities.LoginActivity;
    import com.example.snekstorep.activities.RegistrationActivity;
    import com.example.snekstorep.activities.ShowAllActivity;
    import com.example.snekstorep.models.CategoryModel;
    import com.example.snekstorep.models.ProductModel;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.firebase.firestore.QuerySnapshot;

    import java.util.ArrayList;
    import java.util.List;

    public class HomeFragment extends Fragment {


        TextView catShowAll, popularShowAll, newProductShowAll;



        RecyclerView catRecyclerview, newProductRecyclerview;

        //Category
        CategoryAdapter categoryAdapter;
        List<CategoryModel> categoryModelList;



        // Agrega estas variables
        private NewProductAdapter newProductAdapter;
        private List<ProductModel> productModelList;


        FirebaseFirestore db;

        // Añade esta variable
        private FirebaseAuth auth;

        public HomeFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflar el diseño para este fragmento
            View root = inflater.inflate(R.layout.fragment_home, container, false);

            // Inicializa Firebase Auth
            auth = FirebaseAuth.getInstance();

            // Configura el botón de logout (reemplaza el antiguo userIcon)
            ImageButton logoutBtn = root.findViewById(R.id.logoutBtn);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarSesion();
                }
            });


            // Dentro de onCreateView, después de inflar la vista (View root = ...)
            ImageButton cartIconBtn = root.findViewById(R.id.cartIconBtn);
            cartIconBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CartActivity.class);
                    startActivity(intent);
                }
            });

            catRecyclerview = root.findViewById(R.id.rec_category);
            newProductRecyclerview = root.findViewById(R.id.new_product_rec);




            /// ShowAll ///

//            catShowAll = root.findViewById(R.id.category_see_all);
            popularShowAll = root.findViewById(R.id.popular_see_all);
            newProductShowAll = root.findViewById(R.id.newProducts_see_all);

//            catShowAll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), ShowAllActivity.class);
//                    startActivity(intent);
//                }
//            });

            popularShowAll.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
           });

            newProductShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ShowAllActivity.class);
                    startActivity(intent);
                }
            });


            /// End ShowAll ///



            // En HomeFragment reemplaza:
            db = FirebaseFirestore.getInstance();


            // Inicializar el ImageSlider
            ImageSlider imageSlider = root.findViewById(R.id.image_slider);
            List<SlideModel> slideModels = new ArrayList<>();

            // Agregar imágenes al slider
            slideModels.add(new SlideModel(R.drawable.banner1, "", ScaleTypes.CENTER_CROP));
            slideModels.add(new SlideModel(R.drawable.banner2, "", ScaleTypes.CENTER_CROP));
            slideModels.add(new SlideModel(R.drawable.banner3, "", ScaleTypes.CENTER_CROP));

            imageSlider.setImageList(slideModels);


            // categoria
            catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            categoryModelList = new ArrayList<>();
            categoryAdapter = new CategoryAdapter(getContext(), categoryModelList);
            catRecyclerview.setAdapter(categoryAdapter);

            db.collection("Category")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            categoryModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                            }
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });


            // new products
            newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            productModelList = new ArrayList<>();
            newProductAdapter = new NewProductAdapter(getContext(),productModelList);
            newProductRecyclerview.setAdapter(newProductAdapter);

            db.collection("Products")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ProductModel newProductsModel = document.toObject(ProductModel.class);
                                    productModelList.add(newProductsModel);
                                    newProductAdapter.notifyDataSetChanged();
                                }
                            } else {

                                Toast.makeText(getActivity(), ""+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




            return root;
        }

        // Método para cerrar sesión
        private void cerrarSesion() {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }


    }

