package com.demo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.fragments.OfficeJobFragment;
import com.demo.fragments.OfficeWorkEntryDetailsFragment;
import com.demo.model.WorkEntry;
import com.demo.model.leave.ResponseDatum;

import java.util.ArrayList;
import java.util.List;


public class OfficeGridAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private  List<WorkEntry> leaveHisData=new ArrayList<>();
   private LayoutInflater inflter;
   private OfficeJobFragment officeWorkEntryDetailsFragment;
    public OfficeGridAdapter(OfficeJobFragment officeWorkEntryDetailsFragment, BaseActivity applicationContext, List<WorkEntry> attendenceHisData) {
        this.mContext = applicationContext;
        this.leaveHisData = attendenceHisData;
        this.officeWorkEntryDetailsFragment = officeWorkEntryDetailsFragment;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return leaveHisData.size();
    }

    @Override
    public Object getItem(int i) {
        return leaveHisData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return leaveHisData.get(i).hashCode();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_office_work_entry, null);

        CardView card = (CardView) view.findViewById(R.id.card);
        TextView tv_category = (TextView) view.findViewById(R.id.tv_category);
       // TextView tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
       //String str = "Hello I'm your String";

        tv_category.setText(leaveHisData.get(i).getCategory_title());
        if(i%3 == 0){
            card.setCardBackgroundColor(Color.parseColor("#377838"));
        }else if(i%3 == 1){
            card.setCardBackgroundColor(Color.parseColor("#2b41d4"));
        }else if(i%3 == 2){
            card.setCardBackgroundColor(Color.parseColor("#e14a59"));
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OfficeWorkEntryDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category_id",leaveHisData.get(i).getCategory_id());
                bundle.putString("category_title",leaveHisData.get(i).getCategory_title());
                fragment.setArguments(bundle);
                officeWorkEntryDetailsFragment.callFragment(fragment);

            }
        });


        return view;
    }
}
