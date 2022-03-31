package com.kessi.quotey.fragment;

import android.content.Intent;
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
import com.kessi.quotey.adapters.IFavouriteAdapter;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.ImageUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class IFavouriteFrag extends Fragment {

    private File[] files;
    LinearLayout emptyStatus;
    RecyclerView favouriteList;
    IFavouriteAdapter iFavouriteAdapter;
    ArrayList<String> myQuotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_favourite, container, false);
        emptyStatus = rootView.findViewById(R.id.emptyStatus);
        favouriteList = rootView.findViewById(R.id.favouriteList);
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
            myQuotes = new ArrayList<>();
            myQuotes = getFavouriteQuotes();
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
            iFavouriteAdapter = new IFavouriteAdapter(myQuotes, IFavouriteFrag.this);
            favouriteList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            favouriteList.setItemAnimator(new DefaultItemAnimator());
            favouriteList.setAdapter(iFavouriteAdapter);
        }
    }
    private ArrayList<String> getFavouriteQuotes() {
        ArrayList<String> mediaList = new ArrayList<>();
        String path = ImageUtils.MY_FAVOURITE_FOLDER;
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
        iFavouriteAdapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            iFavouriteAdapter.notifyDataSetChanged();
            new getDownAsync().execute();
        }
    }
}
