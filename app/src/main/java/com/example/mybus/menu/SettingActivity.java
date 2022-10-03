package com.example.mybus.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivitySettingBinding;
import com.example.mybus.vo.DataWithFavStopBus;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private MaterialToolbar toolbar;
    private TextView tvgoAlarm;
    private TextView tvHomeEdit;
    private ArrayList<DataWithFavStopBus> dataWithFavStopBusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        getDataFromIntent();
        setClickListener();
    }

    public void initView(){
        toolbar = binding.toolbar;
        tvgoAlarm = binding.tvAlarm;
        tvHomeEdit = binding.tvHomeEdit;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_arrive_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("HomeEditActivity", "getItemId :::::::::::::::" + item.getItemId());
        switch (item.getItemId()){
            case R.id.action_home:          // 홈 버튼
            case android.R.id.home:     // 뒤로 가기 버튼
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setClickListener(){
        tvgoAlarm.setOnClickListener(view -> {
            Intent goAlarm = new Intent(this, MyAlarmActivity.class);
            startActivity(goAlarm, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        tvHomeEdit.setOnClickListener(view -> {
            Intent goHomeEdit = new Intent(this, HomeEditActivity.class);
            goHomeEdit.putExtra("favlists",  dataWithFavStopBusList);
            goHomeEdit.putExtra("fromSetting", true);
            startActivity(goHomeEdit, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
    }

    public void getDataFromIntent(){
        dataWithFavStopBusList = (ArrayList<DataWithFavStopBus>) getIntent().getSerializableExtra("favlists");
    }


}