package com.demo.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.adapter.CommonAdapter;
import com.demo.interfaces.OnRowClickListener;
import com.demo.model.CommonDialogModel;

import java.util.ArrayList;

/**
 * Created by root on 31/1/18.
 */

public class CommonDialog extends Dialog implements AdapterView.OnItemClickListener{
    private BaseActivity baseActivity;
    private Button btnReload, btnSkip;
    private OnRowClickListener listener;
    private ArrayList<CommonDialogModel> listData=new ArrayList<>();
    private ListView listCommon;
    private CommonAdapter adapter;
    public CommonDialog(CommonAdapter adapter,BaseActivity baseActivity, ArrayList<CommonDialogModel> listData, OnRowClickListener listener) {
        super(baseActivity);
        this.baseActivity = baseActivity;
        this.listener = listener;
        this.listData = listData;
        this.adapter=adapter;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.dlg_common);
        setCancelable(false);
        listCommon = (ListView) findViewById(R.id.listCommon);
        listCommon.setAdapter(adapter);
        listCommon.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dismiss();
        listener.onItemClick(i);

    }


 }

