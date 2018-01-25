package com.demo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.OutDoorHistoryGridAdapter;
import com.demo.model.OutDoorHistory;
import com.demo.network.KlHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 25/1/18.
 */

public class OutDoorJobHistoryFragment extends BaseFragment {
    private ListView gridAttendanceHis;
    private OutDoorHistoryGridAdapter outDoorHistoryGridAdapter;
    private ArrayList<OutDoorHistory> outDoorHistories = new ArrayList<>();
    private String empID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_out_door_workhistory, null, false);
        ((MainActivity)getActivity()).setTitle("Outdoor Job History");
        gridAttendanceHis = (ListView)v.findViewById(R.id.gridAttendanceHis);
        outDoorHistoryGridAdapter = new OutDoorHistoryGridAdapter(baseActivity,outDoorHistories);
        gridAttendanceHis.setAdapter(outDoorHistoryGridAdapter);
        if (getArguments().getString("emp_id")!=null&&!getArguments().getString("emp_id").isEmpty()){
          empID= getArguments().getString("emp_id");
                  getHistory();
        }
        gridAttendanceHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Fragment fragment = new MapsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("emp_id",empID);
                bundle.putString("page_type","work_hostory");
                bundle.putString("job_status",outDoorHistories.get(i).getJob_status());
                bundle.putString("job_id",outDoorHistories.get(i).getJobid());

                fragment.setArguments(bundle);
                displayView(fragment);

            }
        });

        return v;
    }
    private void getHistory() {

        // if(getView()!=null){
        if(baseActivity.isNetworkConnected()){
            new HistoryAsynctask().execute();

        }
        // }

    }

    public class HistoryAsynctask extends AsyncTask<String, Void, JSONObject> {

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
                jsonObject.put("userid", empID);

                Log.e("LeaveCount ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/outdoor_job_history.php", jsonObject);
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

               /* "jobid": "3",
                        "category": "1",
                        "challan_no": "98765425",
                        "box_no": "abcd2456hty",
                        "description": "Dummy Text Description",
                        "startTime": "2018-01-20 00:46:36",
                        "endTime": "2018-01-20 00:48:09",
                        "job_status": "no"*/

                try {
                    if (json.getInt("ResponseCode") == 200) {
                        if(json.has("ResponseData")){
                            outDoorHistories.clear();
                            JSONArray jsonArray = json.getJSONArray("ResponseData");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject c = jsonArray.getJSONObject(i);
                                String jobid = c.getString("jobid");
                                String category = c.getString("category");
                                String challan_no = c.getString("challan_no");
                                String box_no = c.getString("box_no");
                                String description = c.getString("description");
                                String startTime = c.getString("startTime");
                                String endTime = c.getString("endTime");
                                String job_status = c.getString("job_status");
                                outDoorHistories.add(new OutDoorHistory(jobid,category,challan_no,box_no,description,startTime,endTime,job_status));

                              /*  if(category.equalsIgnoreCase(getArguments().getString("category_id"))){
                                    outDoorHistories.add(new OutDoorHistory(jobid,category,challan_no,box_no,description,startTime,endTime,job_status));
                                }*/


                            }

                            outDoorHistoryGridAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }
}
