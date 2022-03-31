package com.kessi.quotey.textquotes.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.TUtil.TextUtil;
import com.kessi.quotey.textquotes.model.QuotesModel;
import com.kessi.quotey.textquotes.qdatabase.DBHelper_dbfile;
import com.kessi.quotey.util.Utills;

import java.util.List;

public class TQuoteAdapter extends RecyclerView.Adapter<TQuoteAdapter.ImageHolder> {
    List<QuotesModel> quotesList;
    Context context;
    int width;
    DBHelper_dbfile dbHelper;
    int[] grid_colors;
    int c = 0;

    public TQuoteAdapter(List<QuotesModel> quotesList, Context context) {
        this.quotesList = quotesList;
        this.context = context;

        dbHelper = new DBHelper_dbfile(context);
        try {
            dbHelper.openDatabase();

        } catch (SQLException sqle) {

            throw sqle;
        }

        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        width = displayMetrics.widthPixels;

        grid_colors = context.getResources().getIntArray(R.array.grid_colors);
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView favIV;
        TextView quoteTV;
        LinearLayout share, copy, wapp;
        CardView cardView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            quoteTV = itemView.findViewById(R.id.quoteTV);
            favIV = itemView.findViewById(R.id.favIV);
            share = itemView.findViewById(R.id.share);
            copy = itemView.findViewById(R.id.copy);
            wapp = itemView.findViewById(R.id.wapp);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tquote, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {

        holder.quoteTV.setText(quotesList.get(position).getQuotes());

        if (quotesList.get(position).getFavourite().equals("no")) {
            holder.favIV.setImageResource(R.drawable.unpress_download);
        } else {
            holder.favIV.setImageResource(R.drawable.press_download);
        }

        holder.favIV.setOnClickListener(v -> {
            if (Utills.hasPermissions(context, Utills.permissions)) {
                ActivityCompat.requestPermissions((Activity) context, Utills.permissions, Utills.perRequest);
            } else {
                if (quotesList.get(position).getFavourite().equals("no")) {
                    dbHelper.addToFavourite("yes", quotesList.get(position).getId());
                    quotesList.get(position).setFavourite("yes");
                } else {
                    dbHelper.addToFavourite("no", quotesList.get(position).getId());
                    quotesList.get(position).setFavourite("no");
                }
                notifyDataSetChanged();
            }
        });

        holder.share.setOnClickListener(v -> {
            TextUtil.shareQuote(context, quotesList.get(position).getQuotes());
        });

        holder.copy.setOnClickListener(v -> {
            TextUtil.copyQuote(context, quotesList.get(position).getQuotes());
        });

        holder.wapp.setOnClickListener(v -> {
            TextUtil.shareQuoteWApp(context, quotesList.get(position).getQuotes());
        });


        int color = grid_colors[c];
        c++;
        if (c == grid_colors.length) {
            c = 0;
        }
        holder.cardView.setCardBackgroundColor(color);

//        int color = grid_colors[position];

//        for (int c = 1; c < grid_colors.length; c++) {
//
//            holder.cardView.setCardBackgroundColor(color);
//
//            if (c == grid_colors.length) {
//                c = 1;
//            }
//        }

    }


    @Override
    public int getItemCount() {
        return quotesList.size();
    }


}
