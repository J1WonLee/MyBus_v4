package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivityAddAlarmFavListBinding;
import com.example.mybus.menu.MyAlarmActivity;
import com.example.mybus.viewmodel.AddAlarmListViewModel;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddAlarmFavListActivity extends AppCompatActivity {
    private ActivityAddAlarmFavListBinding binding;
    private AddAlarmListViewModel addAlarmListViewModel;
    private List<DataWithFavStopBus> dataWithFavStopBusList;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private AddAlarmFavListAdapter adapter;
    private Bundle bundle;
    private SchAlarmInfo schAlarmInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmFavListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initRecycler();
        getDataFromIntent();
        addAlarmListViewModel = new ViewModelProvider(this).get(AddAlarmListViewModel.class);
        getFavLists();

    }

    public void initView(){
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" 정류장 + 노선 선택 ");
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
        Intent intent = new Intent(this, AddAlarmActivity.class);
        switch (item.getItemId()){
            case R.id.action_home:          // 홈 버튼
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:     // 뒤로 가기 버튼
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromIntent(){
        bundle = getIntent().getExtras();
        if (bundle != null){
            schAlarmInfo = bundle.getParcelable("updateAlarm");
        }
    }

    public void initRecycler(){
        recyclerView = binding.addAlarmRecycler;
        adapter = new AddAlarmFavListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void getFavLists(){
        addAlarmListViewModel.getFavStopBus();
        addAlarmListViewModel.localFavStopBusLists.observe(this, new Observer<List<DataWithFavStopBus>>() {
            @Override
            public void onChanged(List<DataWithFavStopBus> dataWithFavStopBuses) {
                if (dataWithFavStopBuses != null){
                    dataWithFavStopBusList = dataWithFavStopBuses;
                    if (schAlarmInfo != null){
                        adapter.updateDataWithFavStopBusList(dataWithFavStopBusList, schAlarmInfo);
                    }else{
                        adapter.updateDataWithFavStopBusList(dataWithFavStopBusList);
                    }

                }
            }
        });
    }
}