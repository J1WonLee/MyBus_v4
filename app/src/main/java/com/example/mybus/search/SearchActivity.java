package com.example.mybus.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivitySearchBinding;
import com.example.mybus.menu.alarm.AddAlarmFavListActivity;
import com.example.mybus.viewmodel.SearchViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivitySearchBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<String> lists = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private SearchViewModel searchViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setContentView(binding.getRoot());
        tabLayout = binding.searchTab;  viewPager2 = binding.searchResults;
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        connectTab();
        setTabSelected();

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }


    public void connectTab(){
        lists.add("버스");        lists.add("정류장");
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(lists.get(position));
            }
        }).attach();
    }

    public void setTabSelected(){
        tabLayout.setTabTextColors(Color.rgb(0,0,0), Color.rgb(241,170,169));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getBooleanExtra("isAlarm", false)){
            moveAlarm();
        }else{
            moveMain();
        }

    }

    public void moveMain(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    public void moveAlarm(){
        Intent moveAlarm = new Intent(this, AddAlarmFavListActivity.class);
        startActivity(moveAlarm);
        finishAfterTransition();
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