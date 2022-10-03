package com.example.mybus.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivityHomeEditBinding;
import com.example.mybus.homeedit.HomeEditAdapter;
import com.example.mybus.homeedit.ItemTouchHelperCallback;
import com.example.mybus.viewmodel.HomeEditViewModel;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeEditActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivityHomeEditBinding binding;
    private Toolbar toolbar;
    private List<DataWithFavStopBus> dataWithFavStopBusList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomeEditAdapter homeEditAdapter;
    private ItemTouchHelper itemTouchHelper;
    private HomeEditViewModel homeEditViewModel;
    private SharedPreferences sharedPreferences;
    private String loginId;
    private Switch recentSchSwitch;
    private boolean isRecentChk;
    private ImageView deleteImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        homeEditViewModel = new ViewModelProvider(this).get(HomeEditViewModel.class);
        getPreferences();
        getDataFromIntent();
//        getLoginId();
        initView();
        setSwitchListener();
        initRecycler();
        setImageListener();
    }

    public void initView(){
        deleteImg = binding.deleteRecenetImg;
        recentSchSwitch = binding.switch1;
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(" 홈화면 편집 ");
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
        Log.d("HomeEditActivity", "getItemId :::::::::::::::" + item.getItemId());
        switch (item.getItemId()){
            case R.id.action_home:          // 홈 버튼
                chkIsRemoved();
                homeEditViewModel.updateAll(homeEditAdapter.getLocalFav());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case android.R.id.home:     // 뒤로 가기 버튼
                chkIsRemoved();
                homeEditViewModel.updateAll(homeEditAdapter.getLocalFav());
//                finishAfterTransition();
                moveIntent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSwitchListener(){
        if (isRecentChk) {
            Log.d("HomeEditActivity", ":::::::::::::::" + isRecentChk);
            recentSchSwitch.setChecked(true);
        }
        else{
            Log.d("HomeEditActivity", ":::::::::::::::" + isRecentChk);
            recentSchSwitch.setChecked(false);
        }

        recentSchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // 최근 검색어 저장 on
                    Log.d("HomeEditActivity", "::::::::::::::: checkchangelistener" + isChecked);
                    editor.putBoolean("recentSch", true);
                }else{
                    // 최근 검색어 저장 off
                    Log.d("HomeEditActivity", "::::::::::::::: checkchangelistener" + isChecked);
                    editor.putBoolean("recentSch", false);
                }
                editor.apply();
            }
        });
    }

    public void setImageListener(){
        deleteImg.setOnClickListener(view ->{
            Toast.makeText(this," 최근 검색 기록을 삭제했습니다", Toast.LENGTH_SHORT).show();
            homeEditViewModel.deleteRecentSch();
        });
    }

    public void initRecycler(){
        recyclerView = binding.recyclerView;
        homeEditAdapter = new HomeEditAdapter(dataWithFavStopBusList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(homeEditAdapter);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(homeEditAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void getDataFromIntent(){
        dataWithFavStopBusList = (ArrayList<DataWithFavStopBus>) getIntent().getSerializableExtra("favlists");
    }

    public void chkIsRemoved(){
        if(homeEditAdapter.getRemoveList().size()>0){
            homeEditViewModel.deleteFavList(homeEditAdapter.getRemoveList());
        }
    }

    public void updateFbFav(List<LocalFav> localFavList){
        if(localFavList.size()>0){
            homeEditViewModel.updateFbFav(homeEditAdapter.getLocalFav(), loginId);
        }
    }

    public void chkIsRemovedFb(List<LocalFav> removedList){
        if (removedList.size()>0){
            homeEditViewModel.deleteFbFav(removedList, loginId);
        }
    }

    public void getLoginId(){
//        sharedPreferences = getSharedPreferences(LoginActivity.sharedId, MODE_PRIVATE);
//        loginId = sharedPreferences.getString("loginId", null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveIntent();
    }

    public void moveIntent(){
        if(getIntent().getBooleanExtra("fromSetting", false)){
            finishAfterTransition();
        }else{
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    public void getPreferences(){
        sharedPreferences = getSharedPreferences(LoginActivity.sharedId, MODE_PRIVATE);
        isRecentChk  = sharedPreferences.getBoolean("recentSch", true);
        loginId = sharedPreferences.getString("loginId", null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 삭제 작업
        chkIsRemoved();
        // db 수정 작업.
        homeEditViewModel.updateAll(homeEditAdapter.getLocalFav());
        chkIsRemovedFb(homeEditAdapter.getRemoveList());
        updateFbFav(homeEditAdapter.getLocalFav());
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