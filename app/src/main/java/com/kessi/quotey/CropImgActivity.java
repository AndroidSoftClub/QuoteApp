package com.kessi.quotey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.kessi.quotey.util.AdManager;
import com.kessi.quotey.util.ImageUtils;
import com.kessi.quotey.util.Utills;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropImgActivity extends AppCompatActivity {

    private CropImageView mCropImageView;
    String image_url;
    ImageView doneIV, backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_crop);

        if (Utills.getStatusBarHeight(CropImgActivity.this) > Utills.convertDpToPixel(24)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        Intent i = getIntent();
        image_url = i.getStringExtra("image_url");

        mCropImageView = findViewById(R.id.CropImageView);
        mCropImageView.setImageUriAsync(Uri.parse(image_url));
        mCropImageView.setGuidelines(CropImageView.Guidelines.ON);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        doneIV = findViewById(R.id.doneIV);
        doneIV.setOnClickListener(v -> {
            ImageUtils.cropBitmap = mCropImageView.getCroppedImage();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });
        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(CropImgActivity.this);
            AdManager.loadBannerAd(CropImgActivity.this, adContainer);
            AdManager.loadInterAd(CropImgActivity.this);
        } else {
            //Fb banner Ads
            AdManager.fbBannerAd(CropImgActivity.this, adContainer);
            AdManager.loadFbInterAd(CropImgActivity.this);
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
                AdManager.showInterAd(CropImgActivity.this, null, 0);
            } else {
                AdManager.showFbInterAd(CropImgActivity.this, null, 0);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}