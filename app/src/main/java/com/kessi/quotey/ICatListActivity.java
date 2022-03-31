package com.kessi.quotey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kessi.quotey.adapters.CatListAdapter;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.Utills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ICatListActivity extends AppCompatActivity {

    RecyclerView quoteList;
    List<String> quoteArray;
    String folderName;
    CatListAdapter catListAdapter;
    ImageView backIV;
    TextView topTxt;
    RelativeLayout loaderLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);

        folderName = getIntent().getExtras().getString("folder");
        quoteList = findViewById(R.id.iQuoteslList);
        loaderLay = findViewById(R.id.loaderLay);

        topTxt = findViewById(R.id.topTxt);
        topTxt.setText(folderName);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        new getCatList().execute();

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(ICatListActivity.this);
            AdManager.adptiveBannerAd(ICatListActivity.this, adContainer);
            AdManager.loadInterAd(ICatListActivity.this);
        } else {
            //Fb banner Ads
            AdManager.fbAdaptiveBannerAd(ICatListActivity.this, adContainer);
            AdManager.loadFbInterAd(ICatListActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AdManager.adCounter++;
        if (AdManager.adCounter == AdManager.adDisplayCounter) {
            if (!AdManager.isloadFbAd) {
                AdManager.showInterAd(ICatListActivity.this, null, 0);
            } else {
                AdManager.showFbInterAd(ICatListActivity.this, null, 0);
            }
        } else {
            super.onBackPressed();
        }
    }


    class getCatList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderLay.setVisibility(View.VISIBLE);
            quoteList.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            quoteArray = new ArrayList<>(Arrays.asList(Utills.getCatQuotes(ICatListActivity.this, folderName)));
            Collections.shuffle(quoteArray);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {
                loaderLay.setVisibility(View.GONE);
                quoteList.setVisibility(View.VISIBLE);
                catListAdapter = new CatListAdapter(quoteArray, folderName, ICatListActivity.this);
                quoteList.setLayoutManager(new GridLayoutManager(ICatListActivity.this, 2));
                quoteList.setItemAnimator(new DefaultItemAnimator());
                quoteList.setAdapter(catListAdapter);
            }, 2000);
        }
    }


}