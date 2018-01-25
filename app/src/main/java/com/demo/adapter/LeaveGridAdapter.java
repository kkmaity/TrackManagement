package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.R;
import com.demo.model.attendence_history.ResponseAttendenceHisDatum;
import com.demo.model.leave.ResponseDatum;

import java.util.ArrayList;
import java.util.List;


public class LeaveGridAdapter extends BaseAdapter {
    private Context mContext;
    private  List<ResponseDatum> leaveHisData=new ArrayList<>();
   private LayoutInflater inflter;
    public LeaveGridAdapter(Context applicationContext, List<ResponseDatum> attendenceHisData) {
        this.mContext = applicationContext;
        this.leaveHisData = attendenceHisData;
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
        view = inflter.inflate(R.layout.item_leave_history, null);

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
        TextView tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
       //String str = "Hello I'm your String";

        tvStatus.setText(leaveHisData.get(i).getLeaveStatus());
        tvStartTime.setText(leaveHisData.get(i).getLeaveStartDate());
        tvEndTime.setText(leaveHisData.get(i).getLeaveEndDate());
        return view;
    }
}
