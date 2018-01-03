package com.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.interfaces.ItemClickListner;
import com.demo.model.MenuItem;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // An Activity's Context.
    private final Context mContext;
    // The list of Native Express ads and menu items.
    private final List<Object> mRecyclerViewItems;
    private ItemClickListner mItemClickListner;
    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems,ItemClickListner listner) {
        this.mContext = context;
        this.mRecyclerViewItems = recyclerViewItems;
        this.mItemClickListner=listner;
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        MenuItemViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);
            return new MenuItemViewHolder(menuItemLayoutView);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,  int position) {
        int viewType = getItemViewType(position);
       final int posi=position;
        if (viewType==0) {
            MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
            MenuItem menuItem = (MenuItem) mRecyclerViewItems.get(position);
            String imageName = menuItem.getImageName();
            Glide.with(mContext)
                    .load(imageName)
                    .into(((MenuItemViewHolder) holder).imageView);
            ((MenuItemViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj=v.getId();

                    mItemClickListner.onItemClick(obj,posi);
                }
            });
        }
    }

}
