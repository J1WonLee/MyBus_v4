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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.GBusStopRouteList;
import com.example.mybus.apisearch.itemList.StopRouteList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.ActivityMainBinding;
import com.example.mybus.databinding.NaviHeaderBinding;
import com.example.mybus.kakaoLogin.KakaoLogin;
import com.example.mybus.mainadapter.MainFavAdapter;
import com.example.mybus.mainadapter.MainFavChildAdapter;
import com.example.mybus.mainadapter.MainStopBusListAdapter;
import com.example.mybus.menu.FireBaseSyncActivity;
import com.example.mybus.menu.HomeEditActivity;
import com.example.mybus.menu.LoginActivity;
import com.example.mybus.menu.MyAlarmActivity;
import com.example.mybus.menu.OpenSourceActivity;
import com.example.mybus.search.SearchActivity;
import com.example.mybus.searchDetail.BusRouteDetailActivity;
import com.example.mybus.searchDetail.BusRouteDetailAdapter;
import com.example.mybus.searchDetail.StopDetailActivity;
import com.example.mybus.viewmodel.MainViewModel;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kakao.sdk.user.UserApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements ActivityAnimate {
    private KakaoLogin kakaoLogin;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navi;
    private MainViewModel mainViewModel;
    private View header;
    private NaviHeaderBinding headerBinding;
    private RecyclerView recyclerView;
    private RecyclerView panelRecyclerView;
    private MainFavAdapter adapter;
    private MainStopBusListAdapter mainStopBusListAdapter;
    private List<DataWithFavStopBus> dataWithFavStopBus;
    private ArrayList<DataWithFavStopBus> dataWithFavStopBusArrayList;
    private List<BusArrivalList> busArrivalList;
    private List<StopUidSchList> stopUidSchList;
    private List<StopRouteList> stopRouteList;
    private List<GBusStopRouteList> gBusStopRouteList;
    private List<LocalFavStopBus> localFavStopBusList;
    private DataWithFavStopBus dataWithFavStopBusOnPanel;
    private View slidingPanel;
    private boolean isUp;
    private boolean isFaved;
    private String lfId;
    private SharedPreferences sharedPreferences;
    private String loginId;
    private long mBackPressTime = 0L;
    private ImageView mainEmtpyImage;
    private FloatingActionButton floatingActionButton;
    private long mLastClickTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.vertical_center, R.anim.none);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        initMenu();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUser();
        chkUser();
        getLoginId();
//        getFavList();
        initRecycler();
        initPanelRecycler();
        getFavBusList();
        if (dataWithFavStopBus != null){
            getFavArrTime();
        }
        setRecyclerListener();
        setSlideDownListener();
//        setChildAdapterListener();
        setRefreshBtnListener();

    }

    public void initMenu(){
        kakaoLogin = new KakaoLogin();
        toolbar = binding.toolbar;
        floatingActionButton = binding.mainRefershBtn;
        slidingPanel = binding.slidingPanel;
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
                            moveAnimate();
                        }else{
                            // 로그인 된 상태
                            mainViewModel.delete();
                            UserApiClient.getInstance().logout(errors ->{
                                return null;
                            });
                            Intent intent3 = new Intent(this, LoginActivity.class);
                            startActivity(intent3);
                            finish();
                            moveAnimate();
                        }
                        return null;
                    });
                    break;
                case R.id.move_my_alarm:
                    Intent goAlarm = new Intent(this, MyAlarmActivity.class);
                    startActivity(goAlarm);
                    moveAnimate();
                    break;
                case R.id.move_home_edit:
                    // 홈 화면 편집으로 이동
                    Intent goHomeEdit = new Intent(this, HomeEditActivity.class);
                    goHomeEdit.putExtra("favlists",  dataWithFavStopBusArrayList);
                    startActivity(goHomeEdit);
                    moveAnimate();
                    break;
                case R.id.move_open_source:
                    // 오픈소스 정보로 이동
                    Intent goOpenSource = new Intent(this, OpenSourceActivity.class);
                    startActivity(goOpenSource);
                    moveAnimate();
                    break;
                case R.id.move_fb_sync:
                    // 파이어베이스 데이터 받아오기로 이동
                    if (loginId != null){
                        Intent goFbSync = new Intent(this, FireBaseSyncActivity.class);
                        startActivity(goFbSync);
                        moveAnimate();
                        finish();
                    }else{
                        Toast.makeText(this, "로그인 후 이용 가능 합니다", Toast.LENGTH_SHORT).show();
                    }
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
                    Glide.with(MainActivity.this)
                            .load(user.getUser_thunbnail())
                            .placeholder(R.drawable.ic_baseline_dehaze_24)
                            .error(R.drawable.ic_baseline_dehaze_24)
                            .override(170,170)
                            .into(img);
                }else{
                    TextView txt = header.findViewById(R.id.name);
                    txt.setText("비회원");

                    ImageView img = header.findViewById(R.id.profile);
                    Glide.with(MainActivity.this)
                            .load(R.drawable.ic_baseline_home_24)
                            .placeholder(R.drawable.ic_baseline_dehaze_24)
                            .error(R.drawable.ic_baseline_dehaze_24)
                            .override(250,250)
                            .into(img);
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
                moveAnimate();
                break;

            case R.id.toolbar_home_edit:
                // 홈 화면 편집으로 이동한다.
                Intent intent2 = new Intent(this, HomeEditActivity.class);
                intent2.putExtra("favlists",  dataWithFavStopBusArrayList);
                startActivity(intent2);
                moveAnimate();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if (System.currentTimeMillis() > mBackPressTime + 2000){
                mBackPressTime = System.currentTimeMillis();
                Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
            }else if (System.currentTimeMillis() <=mBackPressTime+2000){
                finish();
            }
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

    public void initPanelRecycler(){
        panelRecyclerView = binding.panelRecycler;
        mainStopBusListAdapter = new MainStopBusListAdapter();
        panelRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        panelRecyclerView.setAdapter(mainStopBusListAdapter);
    }

    public void getFavBusList(){
        mainViewModel.getFavStopBus();
        mainViewModel.localFavStopBusLists.observe(this, new Observer<List<DataWithFavStopBus>>() {
            @Override
            public void onChanged(List<DataWithFavStopBus> dataWithFavStopBuses) {
                Log.d("MainActivity", ":::::::::::::::::: getFavBusList onchanged!!");
                if (dataWithFavStopBuses.size() > 0){
                    dataWithFavStopBus = dataWithFavStopBuses;
                    dataWithFavStopBusArrayList = (ArrayList<DataWithFavStopBus>) dataWithFavStopBuses;
                    getFavArrTime();
                    getGbusFavArrTime();
//                adapter.updateDataWithFavStopBusList(dataWithFavStopBuses);
                }else{
                    mainEmtpyImage = binding.mainEmtpyImg;
                    mainEmtpyImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getFavArrTime(){
        Log.d("MainActivity", "called getFavArrTime");
        if (dataWithFavStopBus!=null){
            mainViewModel.getFavArrTime(dataWithFavStopBus);
            mainViewModel.stopUidSchList.observe(this, new Observer<List<StopUidSchList>>() {
                @Override
                public void onChanged(List<StopUidSchList> stopUidSchLists) {
                    if (stopUidSchLists != null){
                        adapter.updateDataWithFavStopBusList(dataWithFavStopBus, stopUidSchLists);
                        stopUidSchList = stopUidSchLists;
                    } else {
                        adapter.updateDataWithFavStopBus(dataWithFavStopBus);
                    }
                }
            });
//            mainViewModel.getGbusFavArrTime(dataWithFavStopBus);
//            mainViewModel.busArrivalList.observe(this, new Observer<List<BusArrivalList>>() {
//                @Override
//                public void onChanged(List<BusArrivalList> busArrivalLists) {
//                    if (busArrivalLists != null){
//                       busArrivalList = busArrivalLists;
//                       adapter.updateDataWithGBusFavStopBusList(dataWithFavStopBus, busArrivalList);
//                    }
//                }
//            });
        }
    }

    public void getGbusFavArrTime(){
        Log.d("MainActivity", "called getGbusFavArrTime");
        if (dataWithFavStopBus!=null){
            Log.d("MainActivity", "called getGbusFavArrTime in if");
            mainViewModel.getGbusFavArrTime(dataWithFavStopBus);
            mainViewModel.busArrivalList.observe(this, new Observer<List<BusArrivalList>>() {
                @Override
                public void onChanged(List<BusArrivalList> busArrivalLists) {
                    if (busArrivalLists != null){
                       busArrivalList = busArrivalLists;
                       adapter.updateDataWithGBusFavStopBusList(dataWithFavStopBus, busArrivalList);
                    }else {
                        Log.d("MainActivity", "busArrivalLists is null");
                        adapter.updateDataWithFavStopBus(dataWithFavStopBus);
                    }
                }
            });
        }
    }

    public void setRecyclerListener(){
        adapter.setOnItemClickListener(new MainFavAdapter.OnItemClickListener() {
            // 해당 정류소 버스 노출 여부
            @Override
            public void onBtnClick(View v, int position) {
                if (isUp){
                    // 숨길때는 해당 정류장 목록 보여주는 어뎁터에서 숨겨줘야 함.
//                    slideDown();
                }else{
                    floatingActionButton.setVisibility(View.GONE);
                    slideUp(dataWithFavStopBus.get(position));
                }
                isUp = !isUp;
            }

            // 정류장 혹은 버스 이름 클릭시 해당 상세보기로 이동
            @Override
            public void onTitleClick(View v, int position) {
                if (dataWithFavStopBus != null){
                    Intent intent;
                    Bundle args = new Bundle();
                    if (dataWithFavStopBus.get(position).localFav.getLf_isBus() == 0){
                        // 버스 일 경우 , 노선 이름 번호 담아간다.
                        BusSchList busSchList = new BusSchList();
                        busSchList.setBusRouteId(dataWithFavStopBus.get(position).localFav.getLf_id());
                        busSchList.setBusRouteNm(dataWithFavStopBus.get(position).localFav.getLf_name());
                        busSchList.setCorpNm(dataWithFavStopBus.get(position).localFav.getLf_desc());
                        intent = new Intent(v.getContext(), BusRouteDetailActivity.class);
                        intent.setAction("com.example.mybus.fromMain");
                        args.putParcelable("busList", busSchList);
                        intent.putExtras(args);
                        startActivity(intent);
                        moveAnimate();
                    }else if (dataWithFavStopBus.get(position).localFav.getLf_isBus() == 1){
                        // 정류장 일 경우 ,
                        StopSchList stopSchList = new StopSchList();
                        stopSchList.setStId(dataWithFavStopBus.get(position).localFav.getSt_id());
                        stopSchList.setArsId(dataWithFavStopBus.get(position).localFav.getLf_id());
                        stopSchList.setNextDir(dataWithFavStopBus.get(position).localFav.getLf_desc());
                        stopSchList.setStNm(dataWithFavStopBus.get(position).localFav.getLf_name());
                        intent = new Intent(v.getContext(), StopDetailActivity.class);
                        intent.setAction("com.example.mybus.fromMain");
                        args.putParcelable("stopList", stopSchList);
                        intent.putExtras(args);
                        startActivity(intent);
                        moveAnimate();
                    }
                }
            }
        });
    }

    public void slideUp(DataWithFavStopBus dataWithFavStopBus){
//        dataWithFavStopBusOnPanel = dataWithFavStopBus;
        localFavStopBusList = dataWithFavStopBus.localFavStopBusList;
        setSlideUpTexts(dataWithFavStopBus);
        lfId = dataWithFavStopBus.localFav.getLf_id();
        slidingPanel.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                slidingPanel.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        if (lfId.length() <= 5){
        // 서울 시 정류장 인 경우
            mainViewModel.getStopRouteList(lfId);
            mainViewModel.stopRouteList.observe(this, new Observer<List<StopRouteList>>() {
                @Override
                public void onChanged(List<StopRouteList> stopRouteLists) {
                    if (stopRouteLists != null){
                        stopRouteList = stopRouteLists;
                        mainStopBusListAdapter.updateStopRouteList(stopRouteLists, dataWithFavStopBus.localFavStopBusList);
                    }
                }
            });
        }else{
        // 경기도 정류장 인 경우
            mainViewModel.getGBusStopRouteList(lfId);
            mainViewModel.gbusStopRouteList.observe(this, new Observer<List<GBusStopRouteList>>() {
                @Override
                public void onChanged(List<GBusStopRouteList> gBusStopRouteLists) {
                    if (gBusStopRouteLists != null){
                        gBusStopRouteList = gBusStopRouteLists;
                        stopRouteList=null;
                        mainStopBusListAdapter.updateGBusStopRouteList(gBusStopRouteList, dataWithFavStopBus.localFavStopBusList);
                    }
                }
            });
        }
        slidingPanel.startAnimation(animate);
        setMainStopBusListClickListener();
    }

    public void slideDown(){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                slidingPanel.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(false);
        slidingPanel.startAnimation(animate);
        slidingPanel.setVisibility(View.GONE);
        mainViewModel.getFavStopBus();
    }

    public void setSlideDownListener(){
        binding.mainPanelShutBtn.setOnClickListener(view -> {
            slideDown();
            isUp = !isUp;
            floatingActionButton.setVisibility(View.VISIBLE);
        });
    }

    public void setSlideUpTexts(DataWithFavStopBus dataWithFavStopBus){
        binding.mainStationNm.setText(dataWithFavStopBus.localFav.getLf_name());
    }

    // 즐겨찾기 추가 제거 리스너
    public void setMainStopBusListClickListener(){
        mainStopBusListAdapter.setOnItemClickListener(new MainStopBusListAdapter.OnItemClickListener() {
            @Override
            public void onBtnClick(View v, int position) {
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                if (stopRouteList != null){
                    LocalFavStopBus localFavStopBus = new LocalFavStopBus(lfId
                            , date
                            , stopRouteList.get(position).getBusRouteId()
                            , stopRouteList.get(position).getBusRouteNm()
//                          , stopRouteList.get(position).getBusRouteNm());
                            , adapter.getStopBusOrd(stopRouteList.get(position))
                            , adapter.getStopId(stopRouteList.get(position)));
                    // 즐겨찾기 목록에 추가 혹은 제거
                    if (stopRouteList.get(position).isFlag()) {
                        stopRouteList.get(position).setFlag(false);
                        // 삭제
                        mainViewModel.deleteFbStopFav(lfId, stopRouteList.get(position).getBusRouteId() ,loginId);
                    } else {
                        stopRouteList.get(position).setFlag(true);
                        // 추가
                        mainViewModel.insertFbStopFavFromMain(localFavStopBus, loginId);
                    }
                    mainViewModel.insertFabFv(localFavStopBus);
//                    mainStopBusListAdapter.notifyItemChanged(position);
                    mainStopBusListAdapter.isClicked = true;
//                    mainStopBusListAdapter.updateStopRouteList(stopRouteList, dataWithFavStopBusOnPanel.localFavStopBusList);
                    mainStopBusListAdapter.updateStopRouteList(stopRouteList, localFavStopBusList);
                }else if (gBusStopRouteList != null){
                    LocalFavStopBus localFavStopBus = new LocalFavStopBus(lfId
                            , date
                            , gBusStopRouteList.get(position).getRouteId()
                            , gBusStopRouteList.get(position).getRouteName()
                            , gBusStopRouteList.get(position).getStaOrder()
                            , lfId);
                    if (gBusStopRouteList.get(position).isFlag()) {
                        gBusStopRouteList.get(position).setFlag(false);
                        // 삭제
                        mainViewModel.deleteFbStopFav(lfId, gBusStopRouteList.get(position).getRouteId(), loginId);
                    } else {
                        gBusStopRouteList.get(position).setFlag(true);
                        // 추가
                        mainViewModel.insertFbStopFavFromMain(localFavStopBus, loginId);
                    }
                    mainViewModel.insertFabFv(localFavStopBus);
//                    mainStopBusListAdapter.notifyItemChanged(position);
                    mainStopBusListAdapter.isClicked = true;
                    mainStopBusListAdapter.updateGBusStopRouteList(gBusStopRouteList, localFavStopBusList);
                }
            }
        });
        mainStopBusListAdapter.isClicked = false;
    }

    public void setRefreshBtnListener(){
        floatingActionButton.setOnClickListener(view -> {
            try {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 5000) {
                    Intent intent = getIntent();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
            catch (Exception e){
                Log.d("MainActivity", "setRefreshBtnListener error : " + e.getMessage());
            }
        });
    }

    // 사용자 로그인 정보 받아온다.
    public void getLoginId(){
        sharedPreferences = getSharedPreferences(LoginActivity.sharedId, MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", null);
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


  /* repository.delete();
        UserApiClient.getInstance().logout(error ->{
            return null;
        });

       */