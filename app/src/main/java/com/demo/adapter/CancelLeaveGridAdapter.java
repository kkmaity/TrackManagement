package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.demo.R;
import com.demo.interfaces.ItemClickListner;
import com.demo.model.leave.ResponseDatum;

import java.util.ArrayList;
import java.util.List;


public class CancelLeaveGridAdapter extends BaseAdapter {
    private Context mContext;
    private ItemClickListner mItemClickListner;
    private  List<ResponseDatum> leaveHisData=new ArrayList<>();
   private LayoutInflater inflter;
    public CancelLeaveGridAdapter(Context applicationContext, List<ResponseDatum> attendenceHisData,ItemClickListner mItemClickListner) {
        this.mContext = applicationContext;
        this.leaveHisData = attendenceHisData;
        this.mItemClickListner=mItemClickListner;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_cancel_leave_history, null);

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
        TextView tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
        final CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
       //String str = "Hello I'm your String";

        tvStatus.setText(leaveHisData.get(i).getLeaveStatus());
        tvStartTime.setText(leaveHisData.get(i).getLeaveStartDate());
        tvEndTime.setText(leaveHisData.get(i).getLeaveEndDate());
        checkbox.setTag(i);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (int) compoundButton.getTag();


                if (b) {
                    compoundButton.isChecked();
                    mItemClickListner.onItemClick(checkbox.getId(), position);
                }

               // Toast.makeText(mContext,""+b,Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }
}
