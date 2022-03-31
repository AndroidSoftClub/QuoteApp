package com.kessi.quotey.textquotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.adapters.TQuoteAdapter;
import com.kessi.quotey.textquotes.model.QuotesModel;
import com.kessi.quotey.textquotes.qdatabase.DBHelper_dbfile;
import com.kessi.quotey.util.AdManager;
import java.util.ArrayList;

public class TCatListActivity extends AppCompatActivity {

    String categoryName;
    ImageView backIV;
    TextView topTxt;
    RelativeLayout loaderLay;
    DBHelper_dbfile dbHelper;
    Cursor contain;
    ArrayList<QuotesModel> recList;
    TQuoteAdapter mAdapter;
    RecyclerView tQuoteslList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_cat_list);

        categoryName = getIntent().getExtras().getString("category");
        tQuoteslList = findViewById(R.id.tQuoteslList);
        loaderLay = findViewById(R.id.loaderLay);

        topTxt = findViewById(R.id.topTxt);
        topTxt.setText(categoryName);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        new getTextQuotesAsync().execute();

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(TCatListActivity.this);
            AdManager.adptiveBannerAd(TCatListActivity.this, adContainer);
            AdManager.loadInterAd(TCatListActivity.this);
        } else {
            //Fb banner Ads
            AdManager.fbAdaptiveBannerAd(TCatListActivity.this, adContainer);
            AdManager.loadFbInterAd(TCatListActivity.this);
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
                AdManager.showInterAd(TCatListActivity.this, null, 0);
            } else {
                AdManager.showFbInterAd(TCatListActivity.this, null, 0);
            }
        } else {
            super.onBackPressed();
        }
    }


    class getTextQuotesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper_dbfile(TCatListActivity.this);
            try {
                dbHelper.openDatabase();

            }catch(SQLException sqle){

                throw sqle;
            }
            contain =  dbHelper.getCategoryQuotes(categoryName);



            recList = new ArrayList<>();
            if (contain.moveToFirst()){
                do{
                    recList.add(new QuotesModel(contain.getInt(contain.getColumnIndex("id")),
                            contain.getString(contain.getColumnIndex("category")),
                            contain.getString(contain.getColumnIndex("quotes")),
                            contain.getString(contain.getColumnIndex("favourite"))));
//                    Log.e("hii", contain.getString(contain.getColumnIndex("quotes")));

                }while(contain.moveToNext());
            }
            contain.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Handler().postDelayed(() -> {
                loaderLay.setVisibility(View.GONE);
                mAdapter = new TQuoteAdapter(recList, TCatListActivity.this);
                tQuoteslList.setLayoutManager(new GridLayoutManager(TCatListActivity.this, 1));
                tQuoteslList.setItemAnimator(new DefaultItemAnimator());
                tQuoteslList.setAdapter(mAdapter);
            },1600);

        }
    }
}