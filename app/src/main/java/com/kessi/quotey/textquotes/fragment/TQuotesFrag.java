package com.kessi.quotey.textquotes.fragment;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.adapters.TQuoteAdapter;
import com.kessi.quotey.textquotes.model.QuotesModel;
import com.kessi.quotey.textquotes.qdatabase.DBHelper_dbfile;
import com.kessi.quotey.util.AdManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TQuotesFrag extends Fragment {

    DBHelper_dbfile dbHelper;
    Cursor contain;
    ArrayList<QuotesModel> tQuotesList;
    TQuoteAdapter mAdapter;
    RecyclerView tQuoteslList;
    RelativeLayout loaderLay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_tquotes, container, false);

        tQuoteslList = rootView.findViewById(R.id.tQuoteslList);
        loaderLay = rootView.findViewById(R.id.loaderLay);

        new getTextQuotesAsync().execute();

        LinearLayout adContainer = rootView.findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(getActivity());
            AdManager.loadBannerAd(getActivity(), adContainer);
        } else {
            //Fb banner Ads
            AdManager.fbBannerAd(getActivity(), adContainer);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    class getTextQuotesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper_dbfile(getActivity());
            try {
                dbHelper.createDatabase();

            } catch (IOException ioe) {

                throw new Error("Unable to create database");
            }

            try {
                dbHelper.openDatabase();

            } catch (SQLException sqle) {

                throw sqle;
            }
            contain = dbHelper.getInfo();


            tQuotesList = new ArrayList<>();
            if (contain.moveToFirst()) {
                do {
                    tQuotesList.add(new QuotesModel(contain.getInt(contain.getColumnIndex("id")),
                            contain.getString(contain.getColumnIndex("category")),
                            contain.getString(contain.getColumnIndex("quotes")),
                            contain.getString(contain.getColumnIndex("favourite"))));
//                    Log.e("hii", contain.getString(contain.getColumnIndex("quotes")));

                } while (contain.moveToNext());
            }
            contain.close();

            Collections.shuffle(tQuotesList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Handler().postDelayed(() -> {
                loaderLay.setVisibility(View.GONE);

                try {
                    mAdapter = new TQuoteAdapter(tQuotesList, getActivity());
                    tQuoteslList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    tQuoteslList.setItemAnimator(new DefaultItemAnimator());
                    tQuoteslList.setAdapter(mAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }, 1600);
        }
    }

}
