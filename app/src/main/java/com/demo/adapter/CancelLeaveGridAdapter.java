package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.interfaces.ItemClickListner;
import com.demo.interfaces.LeaveItemClickListner;
import com.demo.model.leave.CheckedResponseDatum;
import com.demo.model.leave.ResponseDatum;

import java.util.ArrayList;
import java.util.List;


public class CancelLeaveGridAdapter extends BaseAdapter {
    private Context mContext;
    private LeaveItemClickListner mItemClickListner;
    private  List<CheckedResponseDatum> leaveHisData=new ArrayList<>();
   private LayoutInflater inflter;
    public CancelLeaveGridAdapter(Context applicationContext, List<CheckedResponseDatum> attendenceHisData,LeaveItemClickListner mItemClickListner) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_cancel_leave_history, null);

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
        TextView tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
        final CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
       //String str = "Hello I'm your String";

        String leaveType = "";
        if(leaveHisData.get(i).getResponseDatum().getLeaveType().equalsIgnoreCase("normal")){
            leaveType = "Normal";
        }else{
            leaveType = "Comp off";
        }

        tvStatus.setText(leaveHisData.get(i).getResponseDatum().getLeaveStatus()+"\n"+"("+leaveType+")");
        tvStartTime.setText(leaveHisData.get(i).getResponseDatum().getLeaveStartDate());
        tvEndTime.setText(leaveHisData.get(i).getResponseDatum().getLeaveEndDate());
        if(leaveHisData.get(i).isChecked()){
            checkbox.setChecked(true);
        }else{
            checkbox.setChecked(false);
        }

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListner.onItemClick( i);
            }
        });

        /*checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (int) compoundButton.getTag();


                if (b) {
                    compoundButton.isChecked();
                    mItemClickListner.onItemClick(checkbox.getId(), position);
                }

               // Toast.makeText(mContext,""+b,Toast.LENGTH_LONG).show();

            }
        });*/
        return view;
    }
}
