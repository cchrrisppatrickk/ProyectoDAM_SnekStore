package com.example.snekstorep.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.snekstorep.repository.MainRepository;

public class MainViewModel extends ViewModel {
    private final MainRepository repository = new MainRepository();

}