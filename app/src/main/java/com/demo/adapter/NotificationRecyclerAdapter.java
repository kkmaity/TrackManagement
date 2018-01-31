package com.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.R;
import com.demo.model.notification.NotificationData;

import java.util.List;


public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {

    private List<NotificationData> notiList;
    private Context mContext;
    // private ClickHandler _interface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTittle;
        public TextView txtDescription;
        public TextView txtTime;



        public MyViewHolder(View view) {
            super(view);
            txtTittle = (TextView) view.findViewById(R.id.txtTittle);
            txtDescription=(TextView)view.findViewById(R.id.txtDescription);
            txtTime=(TextView)view.findViewById(R.id.txtTime);


        }
    }


    public NotificationRecyclerAdapter(Context mContext,/* ClickHandler clickHandler*/List<NotificationData> notiList) {
        this.notiList = notiList;
        this.mContext=mContext;
        //this._interface = clickHandler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notifications, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtDescription.setText(notiList.get(position).getDescription());

        holder.txtTime.setText(notiList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }
}
