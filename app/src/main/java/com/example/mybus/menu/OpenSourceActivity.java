package com.example.mybus.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivityOpenSourceBinding;

import java.util.ArrayList;
import java.util.List;

public class OpenSourceActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivityOpenSourceBinding binding;
    private List<String> openSourceLists = new ArrayList<>();
    private RecyclerView recycler;
    private OpenSourceAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenSourceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initRecycler();
        setLists();
    }

    public void initView(){
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" 오픈소스 정보 ");
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
        switch (item.getItemId()){
            case R.id.action_home:          // 홈 버튼
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            case android.R.id.home:     // 뒤로 가기 버튼
                startActivity(intent);
                finish();
                exitAnimate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRecycler(){
        recycler = binding.opensourceRecycler;
        adapter = new OpenSourceAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }


    public void setLists(){
        openSourceLists.add("rxjava3, https://github.com/ReactiveX/RxJava");
        openSourceLists.add("retrofit2, https://square.github.io/retrofit/");
        openSourceLists.add("glide, https://github.com/bumptech/glide");
        openSourceLists.add("kakaoLogin, https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api");
        openSourceLists.add("dagger & hilt, https://dagger.dev/");
        openSourceLists.add("tikxml, https://github.com/Tickaroo/tikxml");
        openSourceLists.add("material, https://material.io/develop/android");
        openSourceLists.add("spinKit, https://github.com/ybq/Android-SpinKit");
        openSourceLists.add("firebase, https://firebase.google.com/?hl=ko");
        openSourceLists.add("공공데이터, https://www.data.go.kr/");

        adapter.updateopenSourceLists(openSourceLists);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void moveAnimate() {
        overridePendingTransition(R.anim.vertical_center, R.anim.none);
    }

    @Override
    public void exitAnimate() {
        overridePendingTransition(R.anim.none, R.anim.vertical_exit);
    }
}