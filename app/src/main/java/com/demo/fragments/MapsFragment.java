package com.demo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.BaseActivity;
import com.demo.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, null, false);

        if (getArguments().getString("startlat").length()>3){
            isStartLatLongAvailable=true;
            startlat=Double.parseDouble(getArguments().getString("startlat"));
            startlong=Double.parseDouble(getArguments().getString("startlong"));
        }
        if (getArguments().getString("endlat").length()>3){
            isEndLatLongAvailable=true;
            endlat=Double.parseDouble(getArguments().getString("endlat"));
            endlong=Double.parseDouble(getArguments().getString("endlong"));

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
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

        List<LatLng> latLngs = new ArrayList<>();

        if(isEndLatLongAvailable){
            LatLng ln =  new LatLng(endlat,endlong);
            latLngs.add(ln);
            cameraPosition = CameraUpdateFactory.newLatLngZoom(ln, 19);

            mMap.moveCamera(cameraPosition);
            mMap.animateCamera(cameraPosition);
            isMapLoaded = true;
        }else if(isStartLatLongAvailable){
            LatLng ln =  new LatLng(startlat,startlong);
            latLngs.add(ln);
            cameraPosition = CameraUpdateFactory.newLatLngZoom(ln, 19);

            mMap.moveCamera(cameraPosition);
            mMap.animateCamera(cameraPosition);
            isMapLoaded = true;
        }


      /*  Polygon polygon = mMap.addPolygon(new PolygonOptions()
                //.add(new LatLng(22.56566, 88.7677), new LatLng(22.6775, 88.6777))
                .addAll(latLngs)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));*/


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
}
