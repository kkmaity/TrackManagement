package com.demo.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;

import com.demo.R;
import com.demo.adapter.OfficeGridAdapter;
import com.demo.model.WorkEntry;
import com.demo.network.KlHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by root on 20/8/15.
 */
public class OfficeJobFragment extends BaseFragment {

  /* private TextView tv_wash;
    private TextView tv_refill;
    private TextView tv_arrange;
    private TextView tv_check_insrtument;
    private TextView tv_check_stock;
    private TextView tv_challan_prepare;
    private TextView tv_bill_prepare;
    private TextView tv_meeting;
    private TextView tv_trainging;
    private TextView tv_computerjob;
    private TextView tv_stoke_receive;
    private TextView tv_others;*/

  private GridView list;
  private OfficeGridAdapter officeGridAdapter;
  private ArrayList<WorkEntry> workEntries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_office_job, null, false);
        list = (GridView) v.findViewById(R.id.list);
        officeGridAdapter = new OfficeGridAdapter(this,baseActivity,workEntries);
        list.setAdapter(officeGridAdapter);
       /* tv_wash = (TextView)v.findViewById(R.id.tv_wash);
        tv_refill = (TextView)v.findViewById(R.id.tv_refill);
        tv_arrange = (TextView)v.findViewById(R.id.tv_arrange);
        tv_check_insrtument = (TextView)v.findViewById(R.id.tv_check_insrtument);
        tv_check_stock = (TextView)v.findViewById(R.id.tv_check_stock);
        tv_challan_prepare = (TextView)v.findViewById(R.id.tv_challan_prepare);
        tv_meeting = (TextView)v.findViewById(R.id.tv_meeting);
        tv_bill_prepare = (TextView)v.findViewById(R.id.tv_bill_prepare);
        tv_trainging = (TextView)v.findViewById(R.id.tv_trainging);
        tv_computerjob = (TextView)v.findViewById(R.id.tv_computerjob);
        tv_stoke_receive = (TextView)v.findViewById(R.id.tv_stoke_receive);
        tv_others = (TextView)v.findViewById(R.id.tv_others);

        tv_wash.setOnClickListener(this);
        tv_refill.setOnClickListener(this);
        tv_arrange.setOnClickListener(this);
        tv_check_insrtument.setOnClickListener(this);
        tv_check_stock.setOnClickListener(this);
        tv_challan_prepare.setOnClickListener(this);
        tv_meeting.setOnClickListener(this);
        tv_bill_prepare.setOnClickListener(this);
        tv_trainging.setOnClickListener(this);
        tv_computerjob.setOnClickListener(this);
        tv_stoke_receive.setOnClickListener(this);
        tv_others.setOnClickListener(this);*/
        getWorkEntrey();

        return v;

    }

    public void callFragment(Fragment fragment){
        displayView(fragment);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = new OfficeWorkEntryDetailsFragment();
        Bundle bundle = new Bundle();
        switch (view.getId()){
           /* case R.id.tv_wash:
                bundle.putString("title",tv_wash.getText().toString());
                break;
            case R.id.tv_refill:
                bundle.putString("title",tv_refill.getText().toString());
                break;
            case R.id.tv_arrange:
                bundle.putString("title",tv_arrange.getText().toString());
                break;
            case R.id.tv_check_insrtument:
                bundle.putString("title",tv_check_insrtument.getText().toString());
                break;
            case R.id.tv_check_stock:
                bundle.putString("title",tv_check_stock.getText().toString());
                break;
            case R.id.tv_challan_prepare:
                bundle.putString("title",tv_challan_prepare.getText().toString());
                break;
            case R.id.tv_meeting:
                bundle.putString("title",tv_meeting.getText().toString());
                break;
            case R.id.tv_bill_prepare:
                bundle.putString("title",tv_bill_prepare.getText().toString());
                break;
            case R.id.tv_trainging:
                bundle.putString("title",tv_trainging.getText().toString());
                break;
            case R.id.tv_computerjob:
                bundle.putString("title",tv_computerjob.getText().toString());
                break;
            case R.id.tv_stoke_receive:
                bundle.putString("title",tv_stoke_receive.getText().toString());
                break;
            case R.id.tv_others:
                bundle.putString("title",tv_others.getText().toString());
                break;*/

        }
        fragment.setArguments(bundle);
        displayView(fragment);
    }

    private void getWorkEntrey() {
        if(baseActivity.isNetworkConnected()){
            new WorkEntryAsynctask().execute();

        }
    }

    public class WorkEntryAsynctask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            baseActivity.showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                  Log.e("LeaveApplied ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/office_job_category.php", jsonObject);
                return json;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            baseActivity.dismissProgressDialog();

            if(json!=null) {

                try {
                    if (json.getInt("ResponseCode") == 200) {

                        JSONArray jsonArray = json.getJSONArray("ResponseData");
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject c = jsonArray.getJSONObject(i);
                            String category_id = c.getString("category_id");
                            String category_title = c.getString("category_title");
                            workEntries.add(new WorkEntry(category_id, category_title));
                        }
                        officeGridAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }
}
