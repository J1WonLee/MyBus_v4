package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivityAlarmArriveBinding;

// 도착 알람 설정
public class AlarmArriveActivity extends AppCompatActivity {
    private ActivityAlarmArriveBinding binding;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmArriveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    public void initView(){
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(" 도착 알람 ");
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
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}