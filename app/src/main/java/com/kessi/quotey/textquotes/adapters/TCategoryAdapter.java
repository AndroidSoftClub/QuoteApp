package com.kessi.quotey.textquotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.quotey.R;
import com.kessi.quotey.textquotes.TCatListActivity;
import com.kessi.quotey.textquotes.model.CategoryModel;

import java.util.List;

public class TCategoryAdapter extends RecyclerView.Adapter<TCategoryAdapter.ViewHolder> {
    List<CategoryModel> categoryList;
    Context context;
    int[] mHeight;
    int[] grid_colors;
    int c = 0;

    public TCategoryAdapter(List<CategoryModel> categoryList, Context context, int[] mHeight) {
        this.categoryList = categoryList;
        this.context = context;
        this.mHeight = mHeight;

        grid_colors = context.getResources().getIntArray(R.array.grid_colors);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIV;
        TextView categoryTxt, catSizeTxt;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIV = itemView.findViewById(R.id.categoryIV);
            categoryTxt = itemView.findViewById(R.id.categoryTxt);
            catSizeTxt = itemView.findViewById(R.id.catSizeTxt);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public TCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tcategory, parent, false);
        TCategoryAdapter.ViewHolder vh = new TCategoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TCategoryAdapter.ViewHolder holder, int position) {
        holder.categoryIV.getLayoutParams().height = mHeight[position];

        holder.categoryTxt.setText(categoryList.get(position).getCategory());
        holder.catSizeTxt.setText(categoryList.get(position).getCategory_size() + " Quotes");

        holder.categoryIV.setOnClickListener(v -> {
            Intent intent = new Intent(context, TCatListActivity.class);
            intent.putExtra("category", categoryList.get(position).getCategory());
            context.startActivity(intent);
        });


        int color = grid_colors[c];
        c++;
        if (c == grid_colors.length) {
                c = 0;
        }
        holder.cardView.setCardBackgroundColor(color);
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
        return categoryList.size();
    }


}
