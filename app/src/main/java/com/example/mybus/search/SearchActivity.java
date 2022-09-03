package com.example.mybus.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mybus.R;
import com.example.mybus.databinding.ActivitySearchBinding;
import com.example.mybus.viewmodel.SearchViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchActivity extends AppCompatActivity {
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
        tabLayout = binding.searchTab;  viewPager2 = binding.searchResults; viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        connectTab();

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
}