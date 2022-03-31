package com.kessi.quotey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kessi.quotey.adapters.FullscreenImageAdapter;
import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.DepthTransformation;
import com.kessi.quotey.util.ImageUtils;
import com.kessi.quotey.util.Utills;

import java.io.File;
import java.util.ArrayList;

public class PagerPreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<String> imageList;
    int position;
    ImageView shareIV, wappIV, setAsIV, favoIV, unFavoIV;
    FullscreenImageAdapter fullscreenImageAdapter;
    ImageView backIV;
    String prefixPath, myQuotes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_preview);

        if (Utills.getStatusBarHeight(PagerPreviewActivity.this) > Utills.convertDpToPixel(24)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        backIV = findViewById(R.id.backIV);
        viewPager = findViewById(R.id.viewPager);
        shareIV = findViewById(R.id.shareIV);
        favoIV = findViewById(R.id.favoIV);
        unFavoIV = findViewById(R.id.unFavoIV);
        setAsIV = findViewById(R.id.setAsIV);
        wappIV = findViewById(R.id.wappIV);

        imageList = getIntent().getStringArrayListExtra("quotes");
        position = getIntent().getIntExtra("position", 0);
        prefixPath = getIntent().getStringExtra("prefix");
        myQuotes = getIntent().getStringExtra("my_quotes");

        fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList, prefixPath);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);

        viewPager.setPageTransformer(true, new DepthTransformation());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(PagerPreviewActivity.this, null,0);
                } else {
                    AdManager.adCounter++;
                    AdManager.showFbInterAd(PagerPreviewActivity.this, null,0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        favoIV.setOnClickListener(clickListener);
        shareIV.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);
        setAsIV.setOnClickListener(clickListener);
        unFavoIV.setOnClickListener(clickListener);
        wappIV.setOnClickListener(clickListener);

        if (myQuotes.equals("yes")) {
            unFavoIV.setImageResource(R.drawable.delete);
        } else {
            unFavoIV.setImageResource(R.drawable.unfavorite);
        }

        if (prefixPath.equals("")) {
            favoIV.setVisibility(View.GONE);
            unFavoIV.setVisibility(View.VISIBLE);
        } else {
            favoIV.setVisibility(View.VISIBLE);
            unFavoIV.setVisibility(View.GONE);
        }

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(PagerPreviewActivity.this);
            AdManager.loadBannerAd(PagerPreviewActivity.this, adContainer);
            AdManager.loadInterAd(PagerPreviewActivity.this);
        } else {
            //Fb banner Ads
            AdManager.fbBannerAd(PagerPreviewActivity.this, adContainer);
            AdManager.loadFbInterAd(PagerPreviewActivity.this);
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
                AdManager.showInterAd(PagerPreviewActivity.this, null, 0);
            } else {
                AdManager.showFbInterAd(PagerPreviewActivity.this, null, 0);
            }
        } else {
            super.onBackPressed();
            Animatee.animateSlideDown(PagerPreviewActivity.this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    onBackPressed();
                    break;

                case R.id.unFavoIV:
                    if (imageList.size() > 0) {

                        int currentItem = 0;

                        File file = new File(imageList.get(viewPager.getCurrentItem()));
                        if (file.exists()) {
                            boolean del = file.delete();
                            if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
                                currentItem = viewPager.getCurrentItem();
                            }
                            imageList.remove(viewPager.getCurrentItem());
                            fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList, prefixPath);
                            viewPager.setAdapter(fullscreenImageAdapter);

                            Intent intent = new Intent();
                            setResult(10, intent);

                            if (imageList.size() > 0) {
                                viewPager.setCurrentItem(currentItem);
                            } else {
                                finish();
                            }
                        }

                        if (myQuotes.equals("yes")) {
                            Toast.makeText(PagerPreviewActivity.this, "Quote Removed Successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PagerPreviewActivity.this, "Quote Removed From Favourites Successfully...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.favoIV:
                    if (Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        Glide.with(PagerPreviewActivity.this)
                                .asBitmap()
                                .load(Uri.parse(prefixPath + imageList.get(viewPager.getCurrentItem())))
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        ImageUtils.asyncSave(PagerPreviewActivity.this, resource, imageList.get(viewPager.getCurrentItem()));
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                        Intent intent = new Intent();
                        setResult(10, intent);
                    }
                    break;

                case R.id.shareIV:
                    if (Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        if (prefixPath.equals("")) {
                            ImageUtils.mShare(prefixPath + imageList.get(viewPager.getCurrentItem()), PagerPreviewActivity.this);
                        } else {
                            Glide.with(PagerPreviewActivity.this)
                                    .asBitmap()
                                    .load(Uri.parse(prefixPath + imageList.get(viewPager.getCurrentItem())))
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            ImageUtils.asyncSavenShare(PagerPreviewActivity.this, resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        }
                    }
                    break;

                case R.id.wappIV:
                    if (Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        if (prefixPath.equals("")) {
                            ImageUtils.mShareWApp(prefixPath + imageList.get(viewPager.getCurrentItem()), PagerPreviewActivity.this);
                        } else {
                            Glide.with(PagerPreviewActivity.this)
                                    .asBitmap()
                                    .load(Uri.parse(prefixPath + imageList.get(viewPager.getCurrentItem())))
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            ImageUtils.asyncSavenWApp(PagerPreviewActivity.this, resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        }
                    }
                    break;

                case R.id.setAsIV:
                    if (Utills.hasPermissions(PagerPreviewActivity.this, Utills.permissions)) {
                        ActivityCompat.requestPermissions((Activity) PagerPreviewActivity.this, Utills.permissions, Utills.perRequest);
                    } else {
                        Utills.dialogApplyOption(PagerPreviewActivity.this, prefixPath, imageList.get(viewPager.getCurrentItem()));
                    }
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
