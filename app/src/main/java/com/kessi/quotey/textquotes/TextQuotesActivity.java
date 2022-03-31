package com.kessi.quotey.textquotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.fragment.TCategoryFrag;
import com.kessi.quotey.textquotes.fragment.TFavouriteFrag;
import com.kessi.quotey.textquotes.fragment.TQuotesFrag;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.KSUtil;
import com.kessi.quotey.util.Render;

import java.util.ArrayList;
import java.util.List;

public class TextQuotesActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_quotes);

        backIV = findViewById(R.id.back);
        backIV.setOnClickListener(v -> onBackPressed());

        viewPager = findViewById(R.id.pagerdiet);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tab_layoutdiet);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabViewUn(i));
        }

        setupTabIcons();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabView(tab.getPosition()));

                if (tab.getPosition() == 2) {
                    ((TFavouriteFrag) TextQuotesActivity.this.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + tab.getPosition())).new getDownAsync().execute();
                }

                startActivityes(null,0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabViewUn(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(TextQuotesActivity.this);
//            AdManager.loadBannerAd(TextQuotesActivity.this, adContainer);
            AdManager.loadInterAd(TextQuotesActivity.this);
        } else {
            //Fb banner Ads
//            AdManager.fbBannerAd(TextQuotesActivity.this, adContainer);
            AdManager.loadFbInterAd(TextQuotesActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    void startActivityes(Intent intent, int reqCode) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(TextQuotesActivity.this, intent,reqCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(TextQuotesActivity.this, intent,reqCode);
        }
    }

    private void setupTabIcons() {
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(imagePress[0]);
        FrameLayout.LayoutParams tabp = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 350 / 1080,
                getResources().getDisplayMetrics().heightPixels * 120 / 1920);
        img.setLayoutParams(tabp);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(null);
        tab.setCustomView(v);
    }

    int[] imagePress = new int[]{R.drawable.home_press, R.drawable.categories_press, R.drawable.download_press};

    public View getTabView(int position) {
        View v = LayoutInflater.from(TextQuotesActivity.this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(imagePress[position]);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 350 / 1080,
                getResources().getDisplayMetrics().heightPixels * 120 / 1920);
        img.setLayoutParams(tab);
        Render render = new Render(TextQuotesActivity.this);
        render.setAnimation(KSUtil.Tada(img));
        render.start();
        return v;
    }

    int[] imageUnPress = new int[]{R.drawable.home_unpress, R.drawable.categories_unpress, R.drawable.download_unpress};

    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(TextQuotesActivity.this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(imageUnPress[position]);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 350 / 1080,
                getResources().getDisplayMetrics().heightPixels * 120 / 1920);
        img.setLayoutParams(tab);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new TQuotesFrag(), "Home");
        adapter.addFragment(new TCategoryFrag(), "Categories");
        adapter.addFragment(new TFavouriteFrag(), "Favourite");

        viewPager.setAdapter(adapter);

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        AdManager.adCounter++;
        if (AdManager.adCounter == AdManager.adDisplayCounter) {
            if (!AdManager.isloadFbAd) {
                AdManager.showInterAd(TextQuotesActivity.this, null, 0);
            } else {
                AdManager.showFbInterAd(TextQuotesActivity.this, null, 0);
            }
        } else {
            super.onBackPressed();
        }
    }
}