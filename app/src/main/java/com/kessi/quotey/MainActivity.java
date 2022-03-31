package com.kessi.quotey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.kessi.quotey.fragment.ICategoryFrag;
import com.kessi.quotey.fragment.IFavouriteFrag;
import com.kessi.quotey.fragment.IQuotesFrag;
import com.kessi.quotey.textquotes.TextQuotesActivity;
import com.kessi.quotey.textquotes.TextQuotesMakerActivity;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.KSUtil;
import com.kessi.quotey.util.Render;
import com.kessi.quotey.util.SharedPrefs;
import com.kessi.quotey.util.Utills;

import java.util.ArrayList;
import java.util.List;

import slidingrootnav.SlidingRootNav;
import slidingrootnav.SlidingRootNavBuilder;

public class MainActivity extends AppCompatActivity {

    private SlidingRootNav slidingRootNav;

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView drawerIV, dTop, dBtm;
    ImageView d_home, d_category, d_download, text_quote, quote_maker, my_quotes,
            d_dark, d_share, d_rate, d_privacy, d_more;
    SwitchCompat modeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        dTop = findViewById(R.id.dTop);
        Glide.with(this)
                .load(R.drawable.mtop)
                .into(dTop);

        dBtm = findViewById(R.id.dBtm);
        Glide.with(this)
                .load(R.drawable.mbtm)
                .into(dBtm);


        viewPager = findViewById(R.id.pagerdiet);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tab_layoutdiet);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabViewUn(i));
        }

        setupTabIcons();

        drawerIV = findViewById(R.id.drawerIV);
        drawerIV.setOnClickListener(v -> {
            slidingRootNav.openMenu(true);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabView(tab.getPosition()));

                if (tab.getPosition() == 2) {
                    ((IFavouriteFrag) MainActivity.this.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + tab.getPosition())).new getDownAsync().execute();
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

        initDrawer();

//        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(MainActivity.this);
//            AdManager.loadBannerAd(MainActivity.this, adContainer);
            AdManager.loadInterAd(MainActivity.this);
        } else {
            //Fb banner Ads
//            AdManager.fbBannerAd(MainActivity.this, adContainer);
            AdManager.loadFbInterAd(MainActivity.this);
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
            AdManager.showInterAd(MainActivity.this, intent,reqCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(MainActivity.this, intent,reqCode);
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
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(imagePress[position]);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 350 / 1080,
                getResources().getDisplayMetrics().heightPixels * 120 / 1920);
        img.setLayoutParams(tab);
        Render render = new Render(MainActivity.this);
        render.setAnimation(KSUtil.Tada(img));
        render.start();
        return v;
    }

    int[] imageUnPress = new int[]{R.drawable.home_unpress, R.drawable.categories_unpress, R.drawable.download_unpress};

    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(imageUnPress[position]);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 350 / 1080,
                getResources().getDisplayMetrics().heightPixels * 125 / 1920);
        img.setLayoutParams(tab);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new IQuotesFrag(), "Home");
        adapter.addFragment(new ICategoryFrag(), "Categories");
        adapter.addFragment(new IFavouriteFrag(), "Favourite");

        viewPager.setAdapter(adapter);

//        viewPager.setOffscreenPageLimit(3);
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

    public void initDrawer() {
        d_home = findViewById(R.id.d_home);
        d_category = findViewById(R.id.d_category);
        d_download = findViewById(R.id.d_download);
        d_dark = findViewById(R.id.d_dark);
        quote_maker = findViewById(R.id.quote_maker);
        my_quotes = findViewById(R.id.my_quotes);
        d_share = findViewById(R.id.d_share);
        d_rate = findViewById(R.id.d_rate);
        d_privacy = findViewById(R.id.d_privacy);
        d_more = findViewById(R.id.d_more);
        text_quote = findViewById(R.id.text_quote);

        d_home.setOnClickListener(new ClickListener());
        d_category.setOnClickListener(new ClickListener());
        d_download.setOnClickListener(new ClickListener());
        d_dark.setOnClickListener(new ClickListener());
        quote_maker.setOnClickListener(new ClickListener());
        my_quotes.setOnClickListener(new ClickListener());
        d_share.setOnClickListener(new ClickListener());
        d_rate.setOnClickListener(new ClickListener());
        d_privacy.setOnClickListener(new ClickListener());
        d_more.setOnClickListener(new ClickListener());
        text_quote.setOnClickListener(new ClickListener());

        modeSwitch = findViewById(R.id.modeSwitch);
        int mode = SharedPrefs.getAppNightDayMode(this);
        if (mode == AppCompatDelegate.MODE_NIGHT_YES){
            modeSwitch.setChecked(true);
        }else {
            modeSwitch.setChecked(false);
        }
        modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPrefs.setInt(this, SharedPrefs.PREF_NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPrefs.setInt(this, SharedPrefs.PREF_NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    void setUnpress(){
        d_home.setImageResource(R.drawable.d_home);
        d_category.setImageResource(R.drawable.d_categories);
        d_download.setImageResource(R.drawable.d_download);
    }

    private class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.d_home:
                    setUnpress();
                    d_home.setImageResource(R.drawable.d_home_press);
                    viewPager.setCurrentItem(0);
                    slidingRootNav.closeMenu();
                    startActivityes(null,0);
                    break;

                case R.id.d_category:
                    setUnpress();
                    d_category.setImageResource(R.drawable.d_categories_press);
                    viewPager.setCurrentItem(1);
                    slidingRootNav.closeMenu();
                    startActivityes(null,0);
                    break;

                case R.id.d_download:
                    setUnpress();
                    d_download.setImageResource(R.drawable.d_download_press);
                    viewPager.setCurrentItem(2);
                    slidingRootNav.closeMenu();
                    startActivityes(null,0);
                    break;

                case R.id.quote_maker:
                    slidingRootNav.closeMenu();
                    if (Utills.hasPermissions(MainActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions(MainActivity.this, Utills.permissions, Utills.perRequest);
                    }else {
                        startActivityes(new Intent(MainActivity.this, TextQuotesMakerActivity.class),0);
                    }
                    break;

                case R.id.my_quotes:
                    slidingRootNav.closeMenu();
                    if (Utills.hasPermissions(MainActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions(MainActivity.this, Utills.permissions, Utills.perRequest);
                    }else {
                        startActivityes(new Intent(MainActivity.this, MyQuotesActivity.class),0);
                    }
                    break;

                case R.id.text_quote:
                    slidingRootNav.closeMenu();
                    if (Utills.hasPermissions(MainActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions(MainActivity.this, Utills.permissions, Utills.perRequest);
                    }else {
                        startActivityes(new Intent(MainActivity.this, TextQuotesActivity.class),0);
                    }
                    break;

                case R.id.d_dark:
                    slidingRootNav.closeMenu();
                    break;

                case R.id.d_share:
                    slidingRootNav.closeMenu();
                    shareApp();
                    break;

                case R.id.d_rate:
                    slidingRootNav.closeMenu();
                    rateUs();
                    break;

                case R.id.d_more:
                    slidingRootNav.closeMenu();
                    moreApp();
                    break;

                case R.id.d_privacy:
                    slidingRootNav.closeMenu();
                    startActivityes(new Intent(MainActivity.this, PrivacyActivity.class),0);
                    break;

            }
        }
    }

    public void shareApp() {
        Intent myapp = new Intent(Intent.ACTION_SEND);
        myapp.setType("text/plain");
        myapp.putExtra(Intent.EXTRA_TEXT, "Download this awesome app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
        startActivity(myapp);
    }

    public void rateUs() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void moreApp() {
        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/dev?id=7081479513420377164&hl=en")));
    }

    @Override
    public void onBackPressed() {
        exitAlert();
    }

    void exitAlert() {
        final Dialog exitDialog = new Dialog(MainActivity.this);
        exitDialog.setContentView(R.layout.exit_popup_lay);

        exitDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout alertLay = exitDialog.findViewById(R.id.alertLay);
        TextView noBtn = exitDialog.findViewById(R.id.noBtn);
        TextView yesBtn = exitDialog.findViewById(R.id.yesBtn);

        noBtn.setOnClickListener(arg0 -> exitDialog.dismiss());

        yesBtn.setOnClickListener(arg0 -> {
            exitDialog.dismiss();
            MainActivity.super.onBackPressed();
        });
        exitDialog.show();
        Render render = new Render(MainActivity.this);
        render.setAnimation(KSUtil.ZoomIn(alertLay));
        render.setDuration(400);
        render.start();
    }
}