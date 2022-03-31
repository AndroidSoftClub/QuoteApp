package com.kessi.quotey.adapters;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kessi.quotey.PagerPreviewActivity;
import com.kessi.quotey.R;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.ImageUtils;
import com.kessi.quotey.util.KSUtil;

import java.util.ArrayList;
import java.util.List;

public class IFavouriteAdapter extends RecyclerView.Adapter<IFavouriteAdapter.ImageHolder>{
    List<String> categoryList;
    Fragment context;
    int width;

    public IFavouriteAdapter(List<String> categoryList, Fragment context) {
        this.categoryList = categoryList;
        this.context = context;
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        width = displayMetrics.widthPixels;
    }

    public static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView quoteIV, shareIV;
        CardView cardView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            quoteIV = itemView.findViewById(R.id.quoteIV);
            shareIV = itemView.findViewById(R.id.shareIV);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ifavourite, parent, false);
        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((width*485/1080),
                (width*485/1080));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.cardView.setLayoutParams(params);

        Glide.with(context.getActivity()).
                load(categoryList.get(position))
                .into(holder.quoteIV);

        holder.quoteIV.setOnClickListener(v -> {
            Intent intent = new Intent(context.getActivity(), PagerPreviewActivity.class);
            intent.putStringArrayListExtra("quotes", (ArrayList<String>) categoryList);
            intent.putExtra("position", position);
            intent.putExtra("my_quotes", "no");
            intent.putExtra("prefix", "");
            context.startActivityForResult(intent, 10);
            Animatee.animateSlideUp(context.getActivity());
        });

        holder.shareIV.setOnClickListener(v -> {
            KSUtil.Bounce(context.getActivity(), holder.shareIV);
            ImageUtils.mShare(categoryList.get(position), context.getActivity());
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }
}
