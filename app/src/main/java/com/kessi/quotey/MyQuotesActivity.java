package com.kessi.quotey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.kessi.quotey.adapters.MyQuotesAdapter;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.ImageUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MyQuotesActivity extends AppCompatActivity {

    private File[] files;
    LinearLayout emptyStatus;
    RecyclerView favouriteList;
    MyQuotesAdapter myQuotesAdapter;
    ArrayList<String> myQuotes;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quotes);

        emptyStatus = findViewById(R.id.emptyStatus);
        favouriteList = findViewById(R.id.favouriteList);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        new GetMyQuotesAsync().execute();

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(MyQuotesActivity.this);
            AdManager.adptiveBannerAd(MyQuotesActivity.this, adContainer);
            AdManager.loadInterAd(MyQuotesActivity.this);
        } else {
            //Fb banner Ads
            AdManager.fbAdaptiveBannerAd(MyQuotesActivity.this, adContainer);
            AdManager.loadFbInterAd(MyQuotesActivity.this);
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
                AdManager.showInterAd(MyQuotesActivity.this, null, 0);
            } else {
                AdManager.showFbInterAd(MyQuotesActivity.this, null, 0);
            }
        } else {
            super.onBackPressed();
        }
    }

    public class GetMyQuotesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myQuotes = new ArrayList<>();
            myQuotes = getMyQuotes();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (myQuotes != null &&  myQuotes.size() != 0){
                emptyStatus.setVisibility(View.GONE);
            }else {
                emptyStatus.setVisibility(View.VISIBLE);
            }
            myQuotesAdapter = new MyQuotesAdapter(myQuotes, MyQuotesActivity.this);
            favouriteList.setLayoutManager(new GridLayoutManager(MyQuotesActivity.this, 2));
            favouriteList.setItemAnimator(new DefaultItemAnimator());
            favouriteList.setAdapter(myQuotesAdapter);
        }
    }
    private ArrayList<String> getMyQuotes() {
        ArrayList<String> mediaList = new ArrayList<>();
        String path = ImageUtils.MY_QUOTES_FOLDER;
        File targetPath = new File(path);

        files = targetPath.listFiles();

        if (files != null && files.length != 0) {
            try {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (!file.getAbsolutePath().contains("temp")) {
                        mediaList.add(file.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mediaList;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            myQuotesAdapter.notifyDataSetChanged();
            new GetMyQuotesAsync().execute();
        }
    }
}