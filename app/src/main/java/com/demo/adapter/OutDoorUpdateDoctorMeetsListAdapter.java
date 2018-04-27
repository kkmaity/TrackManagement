package com.demo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.R;
import com.demo.fragments.OutDoorWorkEntryAttendanceHistoryUpdateFragment;
import com.demo.fragments.OutDoorWorkEntryDoctorMeetDetailsFragment;
import com.demo.model.OutDoorHistory;

import java.util.ArrayList;
import java.util.List;


public class OutDoorUpdateDoctorMeetsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<OutDoorHistory> attendenceHisData = new ArrayList<>();
    private LayoutInflater inflter;
    private OutDoorWorkEntryDoctorMeetDetailsFragment outDoorWorkEntryDoctorMeetDetailsFragment;

    public OutDoorUpdateDoctorMeetsListAdapter(Context applicationContext, List<OutDoorHistory> attendenceHisData, OutDoorWorkEntryDoctorMeetDetailsFragment outDoorWorkEntryDoctorMeetDetailsFragment) {
        this.mContext = applicationContext;
        this.attendenceHisData = attendenceHisData;
        inflter = (LayoutInflater.from(applicationContext));
        this.outDoorWorkEntryDoctorMeetDetailsFragment = outDoorWorkEntryDoctorMeetDetailsFragment;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_outdoor_history_with_update_button, null);

        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvStartTime = view.findViewById(R.id.tvStartTime);
        TextView tvEndTime = view.findViewById(R.id.tvEndTime);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        TextView tv_update = view.findViewById(R.id.tv_update);
        //String str = "Hello I'm your String";
        if (attendenceHisData.get(i).getJob_status().equalsIgnoreCase("yes")) {
            tvStatus.setText("Job Status :" + " Ongoing Work");
        } else
            tvStatus.setText("Job Status :" + " Completed Work");
        String[] splited = attendenceHisData.get(i).getStartTime().split("\\s+");
        String[] splitedEnd = attendenceHisData.get(i).getEndTime().split("\\s+");
        tvDate.setText(splited[0]);
        tvStartTime.setText(splited[1]);
        tvEndTime.setText(splitedEnd[1]);

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OutDoorWorkEntryAttendanceHistoryUpdateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("job_id", attendenceHisData.get(i).getJobid());
                fragment.setArguments(bundle);
                outDoorWorkEntryDoctorMeetDetailsFragment.displayView(fragment);
            }
        });
        return view;
    }
}
