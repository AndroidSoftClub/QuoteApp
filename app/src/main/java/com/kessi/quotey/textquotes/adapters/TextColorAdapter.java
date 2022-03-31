package com.kessi.quotey.textquotes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.TextQuotesMakerActivity;


public class TextColorAdapter extends RecyclerView.Adapter<TextColorAdapter.ViewHolder> {
    Context context;
    String[] colors;

    public TextColorAdapter(Context context, String[] colors) {
        this.context = context;
        this.colors = colors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.color_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String color = colors[position];
        holder.colorImg.setColorFilter(Color.parseColor(color));

        holder.colorImg.setOnClickListener(v -> {
            ((TextQuotesMakerActivity) context).setTextColor(color);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView colorImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorImg = itemView.findViewById(R.id.colorImg);
        }
    }
}


