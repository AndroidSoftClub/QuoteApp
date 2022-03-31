package com.kessi.quotey.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kessi.quotey.PagerPreviewActivity;
import com.kessi.quotey.R;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.ImageUtils;
import com.kessi.quotey.util.KSUtil;
import com.kessi.quotey.util.Utills;

import java.util.ArrayList;
import java.util.List;

public class IQuoteAdapter extends RecyclerView.Adapter<IQuoteAdapter.ImageHolder>{
    List<String> categoryList;
    Context context;
    int width;
    public IQuoteAdapter(List<String> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;

        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        width = displayMetrics.widthPixels;
    }

    public static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView quoteIV, downloadIV;
        CardView cardView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            quoteIV = itemView.findViewById(R.id.quoteIV);
            downloadIV = itemView.findViewById(R.id.favoIV);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_iquote, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((width*485/1080),
                (width*485/1080));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.cardView.setLayoutParams(params);

        Glide.with(context).
                load(Uri.parse("file:///android_asset/" + Utills.mFolderName+ "/" + categoryList.get(position)))
                .into(holder.quoteIV);

        holder.quoteIV.setOnClickListener(v -> {
            Intent intent = new Intent(context, PagerPreviewActivity.class);
            intent.putStringArrayListExtra("quotes", (ArrayList<String>) categoryList);
            intent.putExtra("position", position);
            intent.putExtra("my_quotes", "no");
            intent.putExtra("prefix", "file:///android_asset/" + Utills.mFolderName+ "/");
            context.startActivity(intent);
            Animatee.animateSlideUp(context);
        });

        holder.downloadIV.setOnClickListener(v -> {
            KSUtil.Bounce(context, holder.downloadIV);
            if (Utills.hasPermissions(context, Utills.permissions)) {
                ActivityCompat.requestPermissions((Activity) context, Utills.permissions, Utills.perRequest);
            }else {
                Glide.with(context)
                        .asBitmap()
                        .load(Uri.parse("file:///android_asset/" + Utills.mFolderName + "/" + categoryList.get(position)))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                ImageUtils.asyncSave(context, resource, categoryList.get(position));
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
