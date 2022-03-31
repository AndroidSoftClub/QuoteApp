package com.kessi.quotey.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kessi.quotey.PagerPreviewActivity;
import com.kessi.quotey.R;
import com.kessi.quotey.adapters.IQuoteAdapter;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.Utills;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IQuotesFrag extends Fragment {

    List<String> iQuoteArray;
    List<String> trendArray;
    RecyclerView mRecyclerView;
    IQuoteAdapter mAdapter;
    CarouselView carouselView;
    ScrollView scrollView;
    RelativeLayout loaderLay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_iquotes, container, false);

        carouselView = rootView.findViewById(R.id.carouselView);
        mRecyclerView = rootView.findViewById(R.id.iQuoteslList);
//        mRecyclerView.setNestedScrollingEnabled(false);
        scrollView = rootView.findViewById(R.id.scrollView);
        loaderLay = rootView.findViewById(R.id.loaderLay);

        new getIQuoteAsync().execute();


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

    class getIQuoteAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderLay.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            iQuoteArray = Utills.getAllImageQuotes(getActivity());
            Collections.shuffle(iQuoteArray);

            trendArray = Utills.getTrendQuotes(iQuoteArray);
            Collections.shuffle(trendArray);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {
                loaderLay.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(trendArray.size());
                carouselView.setImageClickListener(position -> {
                    Intent intent = new Intent(getActivity(), PagerPreviewActivity.class);
                    intent.putStringArrayListExtra("quotes", (ArrayList<String>) trendArray);
                    intent.putExtra("position", position);
                    intent.putExtra("prefix", "file:///android_asset/" + Utills.mFolderName + "/");
                    intent.putExtra("my_quotes", "no");
                    getActivity().startActivity(intent);
                    Animatee.animateSlideUp(getActivity());
                });

                mAdapter = new IQuoteAdapter(iQuoteArray, getActivity());
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mAdapter);
            }, 1600);
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getActivity()).
                    load(Uri.parse("file:///android_asset/" + Utills.mFolderName + "/" + trendArray.get(position)))
                    .into(imageView);
        }
    };

}
