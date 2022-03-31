package com.kessi.quotey.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kessi.quotey.PagerPreviewActivity;
import com.kessi.quotey.R;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.Utills;
import java.util.ArrayList;
import java.util.List;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.ImageHolder>{
    List<String> categoryList;
    Context context;
    String folderName;
    int width;

    public CatListAdapter(List<String> categoryList, String folderName, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        this.folderName = folderName;
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        width = displayMetrics.widthPixels;
    }

    public static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView catIV;
        CardView cardView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            catIV = itemView.findViewById(R.id.catIV);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cat_list, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((width*485/1080),
                (width*485/1080));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.cardView.setLayoutParams(params);

        Glide.with(context).
                load(Uri.parse("file:///android_asset/"+Utills.mFolderName+"/" + folderName +"/"+categoryList.get(position)))
                .into(holder.catIV);

        holder.catIV.setOnClickListener(v -> {
            Intent intent = new Intent(context, PagerPreviewActivity.class);
            intent.putStringArrayListExtra("quotes", (ArrayList<String>) categoryList);
            intent.putExtra("position", position);
            intent.putExtra("my_quotes", "no");
            intent.putExtra("prefix", "file:///android_asset/"+Utills.mFolderName+"/" + folderName +"/");
            context.startActivity(intent);
            Animatee.animateSlideUp(context);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
