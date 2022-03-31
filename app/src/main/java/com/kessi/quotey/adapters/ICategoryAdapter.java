package com.kessi.quotey.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kessi.quotey.ICatListActivity;
import com.kessi.quotey.R;
import com.kessi.quotey.util.Utills;
import java.util.List;

public class ICategoryAdapter extends RecyclerView.Adapter<ICategoryAdapter.ViewHolder>{
    List<String> categoryList;
    List<Integer> categorySizeArray;
    Context context;
    int[] mHeight;


    public ICategoryAdapter(List<String> categoryList, List<Integer> categorySizeArray, Context context, int[] mHeight) {
        this.categoryList = categoryList;
        this.categorySizeArray = categorySizeArray;
        this.context = context;
        this.mHeight = mHeight;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryIV;
        TextView categoryTxt,catSizeTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIV = itemView.findViewById(R.id.categoryIV);
            categoryTxt = itemView.findViewById(R.id.categoryTxt);
            catSizeTxt = itemView.findViewById(R.id.catSizeTxt);
        }
    }

    @NonNull
    @Override
    public ICategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        ICategoryAdapter.ViewHolder vh = new ICategoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ICategoryAdapter.ViewHolder holder, int position) {
        holder.categoryIV.getLayoutParams().height = mHeight[position];
        Glide.with(context).
                load(Uri.parse("file:///android_asset/" + Utills.getCatThumb(context, categoryList.get(position))))
                .into(holder.categoryIV);
        holder.categoryTxt.setText(categoryList.get(position));
        holder.catSizeTxt.setText(categorySizeArray.get(position)+" Images");

        holder.categoryIV.setOnClickListener(v -> {
            Intent intent = new Intent(context, ICatListActivity.class);
            intent.putExtra("folder", categoryList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
