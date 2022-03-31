package com.kessi.quotey.textquotes.fragment;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.adapters.TFavouriteAdapter;
import com.kessi.quotey.textquotes.model.QuotesModel;
import com.kessi.quotey.textquotes.qdatabase.DBHelper_dbfile;
import com.kessi.quotey.util.AdManager;

import java.util.ArrayList;

public class TFavouriteFrag extends Fragment {

    LinearLayout emptyStatus;
    DBHelper_dbfile dbHelper;
    Cursor contain;
    ArrayList<QuotesModel> myQuotes;
    TFavouriteAdapter mAdapter;
    RecyclerView tFavouriteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_tfavourite, container, false);
        emptyStatus = rootView.findViewById(R.id.emptyStatus);
        tFavouriteList = rootView.findViewById(R.id.favouriteList);
        new getDownAsync().execute();

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

    public class getDownAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper_dbfile(getActivity());
            try {
                dbHelper.openDatabase();

            }catch(SQLException sqle){

                throw sqle;
            }
            contain =  dbHelper.getFav();

            myQuotes = new ArrayList<>();
            if (contain.moveToFirst()){
                do{
                    myQuotes.add(new QuotesModel(contain.getInt(contain.getColumnIndex("id")),
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
            if (myQuotes != null &&  myQuotes.size() != 0){
                emptyStatus.setVisibility(View.GONE);
            }else {
                emptyStatus.setVisibility(View.VISIBLE);
            }

            try {
                mAdapter = new TFavouriteAdapter(myQuotes, getActivity());
                tFavouriteList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                tFavouriteList.setItemAnimator(new DefaultItemAnimator());
                tFavouriteList.setAdapter(mAdapter);
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
