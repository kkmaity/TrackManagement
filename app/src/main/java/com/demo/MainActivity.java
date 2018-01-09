package com.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.Enum.AppMenu;
import com.demo.fragments.RootFragment;
import com.demo.network.KlHttpClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static int ACTIVE_TAB_POSITION = 1;
    private String user;
    private boolean doubleBackToExitPressedOnce = false;
    private  Toolbar toolbar;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FrameLayout frame_container;
    boolean isMapLoaded = false;
    private CameraUpdate cameraPosition;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    new SendTrackNotification().execute();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frame_container = (FrameLayout) findViewById(R.id.frame_container);

        if(bundle!=null){
            user = bundle.getString("user");


        }

        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                 if (id == R.id.bottom_menu_home) {
                     onMenuItemSelect(AppMenu.HOME);
                }else if (id == R.id.bottom_menu_attendence) {
                     onMenuItemSelect(AppMenu.ATTENDENCE);
                }else if (id == R.id.bottom_menu_work_entry) {
                     onMenuItemSelect(AppMenu.WORk_ENTRY);
                }else if (id == R.id.bottom_menu_leaves) {
                     onMenuItemSelect(AppMenu.LEAVES);
                }
                return true;
            }
        });


        if (!checkPermission()) {

            requestPermission();

        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_container, getRootFragment(AppMenu.HOME),AppMenu.HOME.name()).commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        Location location = mMap.getMyLocation();


        if(user.equals("user1")){
            frame_container.setVisibility(View.VISIBLE);
        }else{
            frame_container.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(1,1000);


        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (fragment != null) {
                if (!fragment.getChildFragmentManager().popBackStackImmediate())
                    exitApplication();
            } else {
                exitApplication();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            // Handle the camera action
        } else if (id == R.id.terms_conditions) {

        } else if (id == R.id.notifications) {

        } else if (id == R.id.profile) {

        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMenuItemSelect(AppMenu appMenu){

        Fragment fragment = null;
        String tag = null;

        switch (appMenu){
            case HOME:
                tag = AppMenu.HOME.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 1) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 1;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.HOME);
                break;
            case ATTENDENCE:
                tag = AppMenu.ATTENDENCE.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 2) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 2;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.ATTENDENCE);
                break;
            case WORk_ENTRY:
                tag = AppMenu.WORk_ENTRY.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 3) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 3;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.WORk_ENTRY);
                break;
            case LEAVES: tag = AppMenu.LEAVES.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 4) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 4;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.LEAVES);
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment, tag).commit();
    }

    public Fragment getRootFragment(AppMenu appMenu){
        Fragment fragment = new RootFragment();
        Bundle bundle = new Bundle();
        switch (appMenu){
            case HOME:
                bundle.putString("appMenu",AppMenu.HOME.name());
                break;
            case ATTENDENCE:
                bundle.putString("appMenu",AppMenu.ATTENDENCE.name());
                break;
            case WORk_ENTRY:
                bundle.putString("appMenu",AppMenu.WORk_ENTRY.name());
                break;
            case LEAVES:
                bundle.putString("appMenu",AppMenu.LEAVES.name());
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    private void exitApplication() {
        if (ACTIVE_TAB_POSITION != 1) {
            onMenuItemSelect(AppMenu.HOME);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.double_tap_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    public void setTitle(String title){
        toolbar.setTitle(title);
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(MainActivity.this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {



                }
                break;
        }
    }


    public class SendTrackNotification extends AsyncTask<String, Void, Boolean> {
        List<LatLng> latLngs = new ArrayList<>();
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                 Log.e("SendTrackNotification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/getlocation.php", jsonObject);
                if(json.getInt("ResponseCode") == 200){

                    JSONArray ResponseData = json.getJSONArray("ResponseData");
                    latLngs.clear();

                    for(int i=0; i<ResponseData.length(); i++){
                        JSONObject c = ResponseData.getJSONObject(i);
                        latLngs.add( new LatLng(Double.parseDouble(c.getString("userLat")),Double.parseDouble(c.getString("userLong"))));


                    }


                    return true;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){

                if(latLngs.size()>1){
                    if (!isMapLoaded) {
                        LatLng ln = latLngs.get(latLngs.size()-1);
                        cameraPosition = CameraUpdateFactory.newLatLngZoom(ln, 19);

                        mMap.moveCamera(cameraPosition);
                        mMap.animateCamera(cameraPosition);
                        isMapLoaded = true;
                    }
                }
                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        //.add(new LatLng(22.56566, 88.7677), new LatLng(22.6775, 88.6777))
                         .addAll(latLngs)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
                handler.sendEmptyMessageDelayed(1,5000);
            }

        }
    }

}
