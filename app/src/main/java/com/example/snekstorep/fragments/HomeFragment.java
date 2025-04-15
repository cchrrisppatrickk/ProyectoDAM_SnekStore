package com.example.snekstorep.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.snekstorep.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar el ImageSlider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        // Agregar imágenes al slider
        slideModels.add(new SlideModel(R.drawable.banner1, "Mensaje ####### Mensaje #######", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, "Mensaje2 ####### Mensaje2 #######", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, "Mensaje3 ####### Mensaje3 #######", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        return root;
    }
}