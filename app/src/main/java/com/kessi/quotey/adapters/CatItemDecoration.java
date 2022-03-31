package com.kessi.quotey.adapters;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CatItemDecoration extends RecyclerView.ItemDecoration{
    private final int mSpace;

    public CatItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = outRect.right = outRect.bottom = mSpace;

        int position = parent.getChildAdapterPosition(view);

        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)view .getLayoutParams();
        int spanIndex = lp.getSpanIndex();

        if(spanIndex == 1 && position > 0) {

        } else {

        }


    }
}
