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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kessi.quotey.R;
import com.kessi.quotey.adapters.CatItemDecoration;
import com.kessi.quotey.textquotes.adapters.TCategoryAdapter;
import com.kessi.quotey.textquotes.model.CategoryModel;
import com.kessi.quotey.textquotes.qdatabase.DBHelper_dbfile;
import com.kessi.quotey.util.AdManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TCategoryFrag extends Fragment {

    RecyclerView categoryList;
    List<CategoryModel> categoryArray;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int[] mHeight = null;
    private Random mRandom = new Random();

    DBHelper_dbfile dbHelper;
    Cursor contain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_tcategory, container, false);

        categoryList = rootView.findViewById(R.id.categoryList);
        new getCatAsync().execute();

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

    class getCatAsync extends AsyncTask<Void, Void, Void> {

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
            contain =  dbHelper.getCategories();

            categoryArray = new ArrayList<>();
            if (contain.moveToFirst()){
                do{
                    categoryArray.add(new CategoryModel(contain.getString(contain.getColumnIndex("category")),
                            String.valueOf(dbHelper.getCategoryCount(contain.getString(contain.getColumnIndex("category"))))));
//                    Log.e("category", contain.getString(contain.getColumnIndex("category")));

//                    categorySizeArray.add(dbHelper.getCategoryCount(contain.getString(contain.getColumnIndex("category"))));
//                    Log.e("containTotal", ""+dbHelper.getCategoryCount(contain.getString(contain.getColumnIndex("category"))));
                    Collections.shuffle(categoryArray);
                }while(contain.moveToNext());
            }
            contain.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHeight = new int[categoryArray.size()];
            refreshHeight(categoryArray.size());

            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            categoryList.setLayoutManager(mLayoutManager);

            try {
                mAdapter = new TCategoryAdapter(categoryArray, getActivity(), mHeight);
                categoryList.setAdapter(mAdapter);
            }catch (Exception e){
                e.printStackTrace();
            }

            CatItemDecoration decoration = new CatItemDecoration(1);
            categoryList.addItemDecoration(decoration);
            categoryList.getViewTreeObserver().addOnGlobalLayoutListener(() -> { });
            super.onPostExecute(aVoid);
        }
    }
    private void refreshHeight(int length) {
        if (mHeight == null) mHeight = new int[length];

        for(int idx = 0; idx < mHeight.length; idx++) {
            mHeight[idx] = getRandomIntInRange(320, 320);
        }
    }

    protected int getRandomIntInRange(int max, int min) {
        return mRandom.nextInt((max - min) + min) + min;
    }

}
