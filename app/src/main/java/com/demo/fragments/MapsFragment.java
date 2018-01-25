package com.demo.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.network.KlHttpClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double startlat;
    private double startlong;
    private double endlat;
    private double endlong;
    private boolean isStartLatLongAvailable=false;
    private boolean isEndLatLongAvailable=false;
    private CameraUpdate cameraPosition;
    boolean isMapLoaded = false;
    private String emp_id;
    private String page_type;
    private String job_status;
    private String job_id;
    private  List<LatLng> latLngs ;
    private  BitmapDescriptor icon;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, null, false);
        ((MainActivity)getActivity()).setTitle("View On Map");
       icon = BitmapDescriptorFactory.fromResource(R.drawable.bicycle);

        if (getArguments().getString("startlat")!=null&&getArguments().getString("startlat").length()>3){
            isStartLatLongAvailable=true;
            startlat=Double.parseDouble(getArguments().getString("startlat"));
            startlong=Double.parseDouble(getArguments().getString("startlong"));
        }
        if (getArguments().getString("endlat")!=null&&getArguments().getString("endlat").length()>3){
            isEndLatLongAvailable=true;
            endlat=Double.parseDouble(getArguments().getString("endlat"));
            endlong=Double.parseDouble(getArguments().getString("endlong"));

        }  if (getArguments().getString("page_type")!=null&&getArguments().getString("page_type").equalsIgnoreCase("work_hostory")){
            emp_id=getArguments().getString("emp_id");
            page_type=getArguments().getString("page_type");//work_hostory
            job_status=getArguments().getString("job_status");
            job_id=getArguments().getString("job_id");
            getUserLocation();
        }







        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    private void getUserLocation() {
        if(baseActivity.isNetworkConnected()){
            new GetLocationAsynctask().execute();

        }
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (getIntent().getStringExtra("startlat").length()>3){
            isStartLatLongAvailable=true;
            startlat=Double.parseDouble(getIntent().getStringExtra("startlat"));
            startlong=Double.parseDouble(getIntent().getStringExtra("startlong"));
        }
        if (getIntent().getStringExtra("endlat").length()>3){
            isEndLatLongAvailable=true;
            endlat=Double.parseDouble(getIntent().getStringExtra("endlat"));
            endlong=Double.parseDouble(getIntent().getStringExtra("endlong"));

        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(true);


        latLngs = new ArrayList<>();

        if(isEndLatLongAvailable){
            LatLng ln =  new LatLng(endlat,endlong);
            latLngs.add(ln);
            cameraPosition = CameraUpdateFactory.newLatLngZoom(ln, 19);
            mMap.addMarker(new MarkerOptions().position(ln).title("Work End"));
            mMap.moveCamera(cameraPosition);
            mMap.animateCamera(cameraPosition);
            isMapLoaded = true;
        }else if(isStartLatLongAvailable){
            LatLng ln =  new LatLng(startlat,startlong);
            latLngs.add(ln);
            cameraPosition = CameraUpdateFactory.newLatLngZoom(ln, 19);
            mMap.addMarker(new MarkerOptions().position(ln).title("Work Started"));
            mMap.moveCamera(cameraPosition);
            mMap.animateCamera(cameraPosition);
            isMapLoaded = true;
        }





       /* mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);
        // Add a marker in Sydney and move the camera
        if (isStartLatLongAvailable){
            LatLng stLatLong = new LatLng(startlat, startlong);
            mMap.addMarker(new MarkerOptions().position(stLatLong).title("Work Started"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stLatLong, 10));
        }if (isEndLatLongAvailable){
            LatLng endLatLong = new LatLng(endlat, endlong);
            mMap.addMarker(new MarkerOptions().position(endLatLong).title("Work Ended"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLong, 10));
        }
*/


    }

    private void drawRouteOnMap(List<LatLng> latLngs){
        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                //.add(new LatLng(22.56566, 88.7677), new LatLng(22.6775, 88.6777))
                .addAll(latLngs)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

        cameraPosition = CameraUpdateFactory.newLatLngZoom(latLngs.get(latLngs.size()-1), 16);
        mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size()-1)).icon(icon));
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);
        isMapLoaded = true;
    }


    public class GetLocationAsynctask extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("jobid", job_id);

                baseActivity.preference.setReq(jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/getlocation.php", jsonObject);
                return json;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
           // baseActivity.dismissProgressDialog();

            if(json!=null) {

                try {
                    if (json.getInt("ResponseCode") == 200) {
                        JSONArray ResponseData= json.getJSONArray("ResponseData");
                        if (ResponseData!=null&&ResponseData.length()>0){
                            for (int i=0;i<ResponseData.length();i++){
                                String   lat=ResponseData.getJSONObject(i).getString("userLat");
                                String   lng=ResponseData.getJSONObject(i).getString("userLong");
                                LatLng latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                                latLngs.add(latLng);
                                System.out.println("get lat lng======"+latLngs);
                                if (latLngs.size()>0)
                                drawRouteOnMap(latLngs);



                            }
                            getUserLocation();

                        }



                        //   getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));




                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
