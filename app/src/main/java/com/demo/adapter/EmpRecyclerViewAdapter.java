package com.demo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.interfaces.ItemClickListner;
import com.demo.model.MenuItem;
import com.demo.model.emplist.EmpListMain;
import com.demo.model.emplist.ResponseDatum;
import com.demo.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class EmpRecyclerViewAdapter extends RecyclerView.Adapter<EmpRecyclerViewAdapter.MyViewHolder> {

    private List<ResponseDatum> empList=new ArrayList<>();
    private Context mContext;
    private ItemClickListner mItemClickListner;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvEmail, tvPhone;
        public CardView cardMain;



        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvPhone = (TextView) view.findViewById(R.id.tvPhone);
            cardMain = (CardView) view.findViewById(R.id.cardMain);
        }
    }


    public EmpRecyclerViewAdapter(Context mContext,List<ResponseDatum> mEL,ItemClickListner mItemClickListner) {
        this.empList = mEL;
        this.mContext=mContext;
        this.mItemClickListner=mItemClickListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ResponseDatum emList = empList.get(position);
        holder.tvName.setText(emList.getName());
        holder.tvEmail.setText(emList.getEmail());
        holder.tvPhone.setText(emList.getPhone());
        holder.cardMain.setTag(position);
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positio = (int) view.getTag();
                mItemClickListner.onItemClick(view.getId(),positio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }
}