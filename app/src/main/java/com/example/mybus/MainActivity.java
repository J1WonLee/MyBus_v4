package com.example.mybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.ActivityMainBinding;
import com.example.mybus.databinding.NaviHeaderBinding;
import com.example.mybus.kakaoLogin.KakaoLogin;
import com.example.mybus.mainadapter.MainFavAdapter;
import com.example.mybus.menu.HomeEditActivity;
import com.example.mybus.menu.LoginActivity;
import com.example.mybus.menu.MyAlarmActivity;
import com.example.mybus.menu.OpenSourceActivity;
import com.example.mybus.search.SearchActivity;
import com.example.mybus.viewmodel.MainViewModel;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.User;
import com.google.android.material.navigation.NavigationView;
import com.kakao.sdk.user.UserApiClient;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private KakaoLogin kakaoLogin;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navi;
    private MainViewModel mainViewModel;
    private View header;
    private NaviHeaderBinding headerBinding;
    private RecyclerView recyclerView;
    private MainFavAdapter adapter;
    private List<DataWithFavStopBus> dataWithFavStopBus;
    private List<BusArrivalList> busArrivalList;
    private List<StopUidSchList> stopUidSchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        initMenu();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUser();
        chkUser();
//        getFavList();
        initRecycler();
        getFavBusList();
        if (dataWithFavStopBus != null){
            getFavArrTime();
        }


    }

    public void initMenu(){
        kakaoLogin = new KakaoLogin();
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
        drawerLayout = binding.drawer;
        navi = binding.navigation;
        header = navi.getHeaderView(0);
        headerBinding = NaviHeaderBinding.bind(header);

        // 네비게이션 아이템 클릭 이벤트
        navi.setNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.move_login:
                    UserApiClient.getInstance().me((user, error) -> {
                        if (error != null){
                            // 로그인 안 된 상태
                            Intent intent2 = new Intent(this, LoginActivity.class);
                            startActivity(intent2);
                            finish();
                        }else{
                            // 로그인 된 상태
                            mainViewModel.delete();
                            UserApiClient.getInstance().logout(errors ->{
                                return null;
                            });
                            Intent intent3 = new Intent(this, LoginActivity.class);
                            startActivity(intent3);
                            finish();
                        }
                        return null;
                    });
                    break;
                case R.id.move_my_alarm:
                    Intent goAlarm = new Intent(this, MyAlarmActivity.class);
                    startActivity(goAlarm);
                    break;
                case R.id.move_home_edit:
                    // 홈 화면 편집으로 이동
                    Intent goHomeEdit = new Intent(this, HomeEditActivity.class);
                    startActivity(goHomeEdit);
                    break;
                case R.id.move_open_source:
                    // 오픈소스 정보로 이동
                    Intent goOpenSource = new Intent(this, OpenSourceActivity.class);
                    startActivity(goOpenSource);
                    break;
            }
            return false;
        });
    }

    public void chkUser(){

        mainViewModel.mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    Log.d("kkang", "logined user :" + user.getUser_name());
                    TextView txt = header.findViewById(R.id.name);
                    txt.setText(user.getUser_name());

                    ImageView img = header.findViewById(R.id.profile);
                    Glide.with(MainActivity.this).load(user.getUser_thunbnail()).placeholder(R.drawable.ic_baseline_dehaze_24).error(R.drawable.ic_baseline_dehaze_24).into(img);
                }else{
                    TextView txt = header.findViewById(R.id.name);
                    txt.setText("비회원");

                    ImageView img = header.findViewById(R.id.profile);
                    Glide.with(MainActivity.this).load(R.drawable.ic_baseline_home_24).placeholder(R.drawable.ic_baseline_dehaze_24).error(R.drawable.ic_baseline_dehaze_24).into(img);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.toolbar_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);

            case R.id.toolbar_home_edit:
                // 홈 화면 편집으로 이동한다.
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public void initRecycler(){
        recyclerView = binding.mainRecycler;
        adapter = new MainFavAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void getFavList(){
        mainViewModel.getFavList();
        mainViewModel.localFavLists.observe(this, new Observer<List<LocalFav>>() {
            @Override
            public void onChanged(List<LocalFav> localFavs) {
                Log.d("MainActivity", localFavs.size() +" ");
            }
        });
    }

    public void getFavBusList(){
        mainViewModel.getFavStopBus();
        mainViewModel.localFavStopBusLists.observe(this, new Observer<List<DataWithFavStopBus>>() {
            @Override
            public void onChanged(List<DataWithFavStopBus> dataWithFavStopBuses) {
                Log.d("MainActivity", "size is : " + dataWithFavStopBuses.size() +" ");
                dataWithFavStopBus = dataWithFavStopBuses;
                getFavArrTime();
//                adapter.updateDataWithFavStopBusList(dataWithFavStopBuses);
            }
        });
    }

    public void getFavArrTime(){
        Log.d("MainActivity", "called getfavtime");
        if (dataWithFavStopBus!=null){
            mainViewModel.getFavArrTime(dataWithFavStopBus);
            mainViewModel.stopUidSchList.observe(this, new Observer<List<StopUidSchList>>() {
                @Override
                public void onChanged(List<StopUidSchList> stopUidSchLists) {
                    if (stopUidSchLists != null){
                        adapter.updateDataWithFavStopBusList(dataWithFavStopBus, stopUidSchLists);
                        stopUidSchList = stopUidSchLists;
                    }
                }
            });

            mainViewModel.getGbusFavArrTime(dataWithFavStopBus);
            mainViewModel.busArrivalList.observe(this, new Observer<List<BusArrivalList>>() {
                @Override
                public void onChanged(List<BusArrivalList> busArrivalLists) {
                    if (busArrivalLists != null){
                       busArrivalList = busArrivalLists;
                        adapter.updateDataWithFavStopBusList(dataWithFavStopBus, stopUidSchList, busArrivalList);
                    }
                }
            });

        }

    }
}


  /* repository.delete();
        UserApiClient.getInstance().logout(error ->{
            return null;
        });

       */