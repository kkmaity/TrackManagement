package com.demo.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.NotificationRecyclerAdapter;
import com.demo.api.ApiAllNotification;
import com.demo.model.notification.NotificationData;
import com.demo.model.notification.NotificationMain;
import com.demo.model.notification.NotificationParam;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificationFragment extends BaseFragment {
    private RecyclerView recyclerNotification;
  //  private Toolbar toolbarHome;
   // private ImageView ivBackBtn;
    private ProgressDialog progressDialog;
    NotificationRecyclerAdapter adapter;
    private ArrayList<NotificationData> notificationDataArrayList = new ArrayList<>();
    private TextView tv_no_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notifications, null, false);

      //  toolbarHome=(Toolbar)findViewById(R.id.toolbar);
       // ivBackBtn=(ImageView)toolbarHome.findViewById(R.id.ivBack);
       // TextView txtTittle = (TextView) toolbarHome.findViewById(R.id.txtTittle);
       // txtTittle.setText("NOTIFICATION");
        ((MainActivity)getActivity()).setTitle("Notification");
      //  progressDialog=new ProgressDialog(this);
      //  progressDialog.setMessage("Loading...");


        recyclerNotification=(RecyclerView)v.findViewById(R.id.recyclerNotification);
        tv_no_data=(TextView)v.findViewById(R.id.tv_no_data);
        LinearLayoutManager horizontalLayoutManagaer4 = new LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false);
        recyclerNotification.setLayoutManager(horizontalLayoutManagaer4);

      //  ivBackBtn.setOnClickListener(this);
        adapter=new NotificationRecyclerAdapter(baseActivity,notificationDataArrayList);
        recyclerNotification.setAdapter(adapter);
        getAllNotification();
        return v;
    }





   void getAllNotification(){
if (baseActivity.isNetworkConnected()){
    new NotificationAsynctask().execute();
   /* baseActivity.showProgressDialog();
    new ApiAllNotification(getParam(), new OnApiResponseListener() {
        @Override
        public <E> void onSuccess(E t) {
            baseActivity.dismissProgressDialog();
            NotificationMain main=(NotificationMain)t;
            if (main.getResponseCode()==200){
                NotificationRecyclerAdapter adapter=new NotificationRecyclerAdapter(baseActivity,main.getResponseData());
                recyclerNotification.setAdapter(adapter);
            }else
                Toast.makeText(baseActivity,main.getMessage(),Toast.LENGTH_LONG).show();


        }

        @Override
        public <E> void onError(E t) {
            baseActivity.dismissProgressDialog();
        }

        @Override
        public void onError() {
            baseActivity.dismissProgressDialog();
        }
    });*/
}
   }

    private NotificationParam getParam() {
        NotificationParam param=new NotificationParam();
        param.setApiKey(Constant.API_KEY);
        return param;
    }



    public class NotificationAsynctask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            baseActivity.showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", Constant.API_KEY);
                Log.e("Notification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/notification.php", jsonObject);
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
                        tv_no_data.setVisibility(View.GONE);
                        recyclerNotification.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = json.getJSONArray("ResponseData");
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject c = jsonArray.getJSONObject(i);
                            String id = c.getString("id");
                            String description = c.getString("description");
                            String date = c.getString("date");
                           notificationDataArrayList.add(new NotificationData(id,description,date));
                        }
                        adapter.notifyDataSetChanged();


                    }else if(json.getInt("ResponseCode") == 400){
                        tv_no_data.setVisibility(View.VISIBLE);
                        recyclerNotification.setVisibility(View.GONE);
                        tv_no_data.setText(json.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }
}
