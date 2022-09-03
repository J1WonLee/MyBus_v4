package com.example.mybus.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mybus.databinding.ActivityHomeEditBinding;

public class HomeEditActivity extends AppCompatActivity {
    ActivityHomeEditBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}