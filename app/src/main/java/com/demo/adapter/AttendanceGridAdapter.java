package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.R;
import com.demo.model.attendence_history.ResponseAttendenceHisDatum;

import java.util.ArrayList;
import java.util.List;


public class AttendanceGridAdapter extends BaseAdapter {
    private Context mContext;
    private  List<ResponseAttendenceHisDatum> attendenceHisData=new ArrayList<>();
   private LayoutInflater inflter;
    public AttendanceGridAdapter(Context applicationContext, List<ResponseAttendenceHisDatum> attendenceHisData) {
        this.mContext = applicationContext;
        this.attendenceHisData = attendenceHisData;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return attendenceHisData.size();
    }

    @Override
    public Object getItem(int i) {
        return attendenceHisData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return attendenceHisData.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_attendence_history, null);

        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
        TextView tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
       //String str = "Hello I'm your String";
        String[] splited = attendenceHisData.get(i).getStartTime().split("\\s+");
        String[] splitedEnd = attendenceHisData.get(i).getEndTime().split("\\s+");
        tvDate.setText(splited[0]);
        tvStartTime.setText(splited[1]);
        tvEndTime.setText(splitedEnd[1]);
        return view;
    }
}
