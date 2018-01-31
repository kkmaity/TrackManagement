package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.R;
import com.demo.model.CommonDialogModel;

import java.util.ArrayList;

/**
 * Created by root on 31/1/18.
 */

public class CommonAdapter extends BaseAdapter {
    private final LayoutInflater inflter;
    private Context mContext;
    private ArrayList<CommonDialogModel> dataList;
    public CommonAdapter(Context context, ArrayList<CommonDialogModel> list) {
        this.dataList=list;
        this.mContext=context;
        inflter = (LayoutInflater.from(mContext));
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataList.hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_common, null);
        TextView txtCommonRow = (TextView) view.findViewById(R.id.txtCommonRow);
        txtCommonRow.setText(dataList.get(i).getName());




        return view;
    }
}
