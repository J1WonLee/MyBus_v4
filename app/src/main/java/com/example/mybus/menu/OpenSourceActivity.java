package com.example.mybus.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mybus.databinding.ActivityOpenSourceBinding;

public class OpenSourceActivity extends AppCompatActivity {
    private ActivityOpenSourceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenSourceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}