package com.kessi.quotey.textquotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kessi.quotey.CropImgActivity;
import com.kessi.quotey.MyQuotesActivity;
import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.adapters.BGColorAdapter;
import com.kessi.quotey.textquotes.adapters.TextColorAdapter;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.ImageUtils;
import com.kessi.quotey.util.KSUtil;
import com.kessi.quotey.util.Render;
import com.makeramen.roundedimageview.RoundedImageView;
import com.txusballesteros.AutoscaleEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TextQuotesMakerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView close, pickImage, fontColor, fontGravity, fontsize, save, bgIV;
    private TextView fontStyle;
    private RoundedImageView bgColor;
    private RecyclerView bgColorRecycler, textColorRecycler;
    private String[] colors, textColors;
    private RelativeLayout mainLay, savedLay;
    private AutoscaleEditText quoteTV;
    private List<String> fontList;
    private LinearLayout seekLay;
    private SeekBar fSizeSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_quotes_maker);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainLay = findViewById(R.id.mainLay);
        savedLay = findViewById(R.id.savedLay);
        close = findViewById(R.id.close);
        close.setOnClickListener(this);

        bgIV = findViewById(R.id.bgIV);
        quoteTV = findViewById(R.id.quoteTV);
        bgColor = findViewById(R.id.bgColor);
        bgColor.setOnClickListener(this);

        pickImage = findViewById(R.id.pickImage);
        pickImage.setOnClickListener(this);

        fontStyle = findViewById(R.id.fontStyle);
        fontStyle.setOnClickListener(this);

        fontColor = findViewById(R.id.fontColor);
        fontColor.setOnClickListener(this);

        fontGravity = findViewById(R.id.fontGravity);
        fontGravity.setOnClickListener(this);

        save = findViewById(R.id.save);
        save.setOnClickListener(this);

        fontsize = findViewById(R.id.fontsize);
        fontsize.setOnClickListener(this);

        seekLay = findViewById(R.id.seekLay);
        fSizeSeek = findViewById(R.id.fSizeSeek);
        fSizeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 50)
                    quoteTV.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bgColorRecycler = findViewById(R.id.bgColorRecycler);
        bgColorRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colors = getResources().getStringArray(R.array.color_array);
        BGColorAdapter bgColorAdapter = new BGColorAdapter(TextQuotesMakerActivity.this, colors);
        bgColorRecycler.setAdapter(bgColorAdapter);

        textColorRecycler = findViewById(R.id.textColorRecycler);
        textColorRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        textColors = getResources().getStringArray(R.array.text_color_array);
        TextColorAdapter textColorAdapter = new TextColorAdapter(TextQuotesMakerActivity.this, textColors);
        textColorRecycler.setAdapter(textColorAdapter);

        String randomColor = colors[new Random().nextInt(colors.length - 1)];
        mainLay.setBackgroundColor(Color.parseColor(randomColor));
        savedLay.setBackgroundColor(Color.parseColor(randomColor));
        bgColor.setColorFilter(Color.parseColor(randomColor));

        fontList = new ArrayList<>();
        fontList = loadFonts();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                KSUtil.Bounce(TextQuotesMakerActivity.this, close);
                onBackPressed();
                break;

            case R.id.bgColor:
                KSUtil.Bounce(TextQuotesMakerActivity.this, bgColor);
                textColorRecycler.setVisibility(View.GONE);
                seekLay.setVisibility(View.GONE);
                if (bgColorRecycler.getVisibility() == View.VISIBLE) {
                    bgColorRecycler.setVisibility(View.GONE);
                } else {
                    bgColorRecycler.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.pickImage:
                KSUtil.Bounce(TextQuotesMakerActivity.this, pickImage);
                bgColorRecycler.setVisibility(View.GONE);
                textColorRecycler.setVisibility(View.GONE);
                seekLay.setVisibility(View.GONE);

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 450);
                break;

            case R.id.fontStyle:
                KSUtil.Bounce(TextQuotesMakerActivity.this, fontStyle);
                bgColorRecycler.setVisibility(View.GONE);
                textColorRecycler.setVisibility(View.GONE);
                seekLay.setVisibility(View.GONE);
                setTypeface();
                break;

            case R.id.fontGravity:
                KSUtil.Bounce(TextQuotesMakerActivity.this, fontGravity);
                bgColorRecycler.setVisibility(View.GONE);
                textColorRecycler.setVisibility(View.GONE);
                seekLay.setVisibility(View.GONE);
                setFontPosition();
                break;

            case R.id.fontsize:
                KSUtil.Bounce(TextQuotesMakerActivity.this, fontsize);
                bgColorRecycler.setVisibility(View.GONE);
                textColorRecycler.setVisibility(View.GONE);
                if (seekLay.getVisibility() == View.VISIBLE) {
                    seekLay.setVisibility(View.GONE);
                } else {
                    seekLay.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.fontColor:
                KSUtil.Bounce(TextQuotesMakerActivity.this, fontColor);
                bgColorRecycler.setVisibility(View.GONE);
                seekLay.setVisibility(View.GONE);
                if (textColorRecycler.getVisibility() == View.VISIBLE) {
                    textColorRecycler.setVisibility(View.GONE);
                } else {
                    textColorRecycler.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.save:
                KSUtil.Bounce(TextQuotesMakerActivity.this, save);
                new SaveQuoteAsync().execute();
                break;
        }
    }

    public void setBGColor(String color) {
        mainLay.setBackgroundColor(Color.parseColor(color));
        savedLay.setBackgroundColor(Color.parseColor(color));
        bgColor.setColorFilter(Color.parseColor(color));
    }

    public void setTextColor(String color) {
        quoteTV.setTextColor(Color.parseColor(color));
    }

    class SaveQuoteAsync extends AsyncTask<Void, Void, Void> {
        AlertDialog alertDialog;
        int width = 0, height = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            quoteTV.setCursorVisible(false);

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(TextQuotesMakerActivity.this);
            View view = layoutInflaterAndroid.inflate(R.layout.dialog_loading, null);
            final AlertDialog.Builder alert = new AlertDialog.Builder(TextQuotesMakerActivity.this);
            alert.setView(view);

            final CardView alertLay = view.findViewById(R.id.alertLay);

            alertDialog = alert.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            alertDialog.setCancelable(false);

            Render render = new Render(TextQuotesMakerActivity.this);
            render.setAnimation(KSUtil.ZoomIn(alertLay));
            render.setDuration(400);
            render.start();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                width = savedLay.getWidth();
                height = savedLay.getHeight();
                Log.e("width * height", width + " * " + height);
                Bitmap image = viewToBitmap(savedLay, width, height);
                File quotesFolder = new File(ImageUtils.MY_QUOTES_FOLDER);
                if (!quotesFolder.exists()) {
                    quotesFolder.mkdirs();
                }

                String fileName = String.valueOf(System.currentTimeMillis()) + ".png";
                File photoFile = new File(quotesFolder, fileName);
                image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
            } catch (Exception ex) {
                ex.printStackTrace();
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.dismiss();
            Toast.makeText(TextQuotesMakerActivity.this, "Saved Quote Successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TextQuotesMakerActivity.this, MyQuotesActivity.class));
            finish();
        }
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private List<String> loadFonts() {
        final List<String> result = new ArrayList<String>();
        AssetManager am = getAssets();
        try {
            String[] fontNames = am.list("fonts");
            if (fontNames != null) {
                for (String name : fontNames) {
                    result.add("assets://fonts/" + name);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    int tPos = 0;

    private void setTypeface() {
        if (tPos == fontList.size()) {
            tPos = 0;
        }
        String path = fontList.get(tPos);
        tPos++;

        String assetPath = path.substring("assets://".length());
        quoteTV.setTypeface(Typeface.createFromAsset(getAssets(), assetPath));
        fontStyle.setTypeface(Typeface.createFromAsset(getAssets(), assetPath));
    }

    int fPos = 0;

    private void setFontPosition() {
        if (fPos == 5) {
            fPos = 0;
        }
        if (fPos == 0) {
            quoteTV.setGravity(Gravity.LEFT | Gravity.TOP);
        } else if (fPos == 1) {
            quoteTV.setGravity(Gravity.RIGHT | Gravity.TOP);
        } else if (fPos == 2) {
            quoteTV.setGravity(Gravity.CENTER);
        } else if (fPos == 3) {
            quoteTV.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        } else if (fPos == 4) {
            quoteTV.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        }
        fPos++;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 450) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    Log.e("Image Uri", ""+selectedImageUri);
                    Intent intent = new Intent(TextQuotesMakerActivity.this, CropImgActivity.class);
                    intent.putExtra("image_url", ""+selectedImageUri);
                    startActivityForResult(intent, 500);
                    Animatee.animateSlideUp(TextQuotesMakerActivity.this);
                }
            }else if (requestCode == 500){
                bgIV.setImageBitmap(ImageUtils.cropBitmap);
            }
        }
    }
}